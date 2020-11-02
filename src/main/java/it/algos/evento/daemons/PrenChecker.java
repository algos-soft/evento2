package it.algos.evento.daemons;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Compare;
import it.algos.evento.EventoApp;
import it.algos.evento.entities.company.Company;
import it.algos.evento.entities.lettera.ModelliLettere;
import it.algos.evento.entities.prenotazione.EmailFailedException;
import it.algos.evento.entities.prenotazione.Prenotazione;
import it.algos.evento.entities.prenotazione.PrenotazioneModulo;
import it.algos.evento.entities.prenotazione.Prenotazione_;
import it.algos.evento.pref.CompanyPrefs;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.multiazienda.ELazyContainer;
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.lib.Lib;
import org.joda.time.DateTime;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controlli prenotazioni scadute e invio email di sollecito
 */
public class PrenChecker implements Runnable {
    private final static Logger logger = Logger.getLogger(PrenChecker.class.getName());
    private Date checkDate;
    private boolean checkConfLevel1 = true;
    private boolean checkConfLevel2 = true;
    private boolean checkScadPagamento = true;


    /**
     * Controlo prenotazioni scadute.
     *
     * @param checkDate la data alla quale eseguire i controlli
     */
    public PrenChecker(Date checkDate) {
        super();
        this.checkDate = checkDate;
    }

    /**
     * Esegue il check per tutte quelle aziende che lo hanno programmato per quest'ora del giorno.<p>
     * ATTENZIONE! Questa procedura è invocata da un thread sul server.
     * Quindi non abbiamo sessioni e di conseguenza non abbiamo company corrente.
     * Pertanto tutte le chiamate che partono da qui devono:
     * - NON usare EQuery perché la company è nulla
     * - Assegnare sempre esplicitamente la company
     */
    @Override
    public void run() {

        // spazzola tutte le aziende
        // se la preferenza è ON e l'ora corrisponde, esegue il controllo
        logger.log(Level.INFO, "start ciclo di controllo posizioni scadute");

        DateTime dt = new DateTime();
        int currentHour = dt.getHourOfDay();

        EntityManager manager = EM.createEntityManager();
        JPAContainer<Company> companies = JPAContainerFactory.makeNonCachedReadOnly(Company.class, manager);
        for (Iterator<Object> i = companies.getItemIds().iterator(); i.hasNext(); ) {

            Object itemId = i.next();
            EntityItem<Company> item = companies.getItem(itemId);
            BaseCompany company = item.getEntity();
            boolean doChecks = Lib.getBool(CompanyPrefs.doRunSolleciti.get(company));
            if (doChecks) {
                int checkHour = Lib.getInt(CompanyPrefs.oraRunSolleciti.get(company));
                if (checkHour == currentHour) {
                    run(company);
                }
            }

        }
        manager.close();

        logger.log(Level.INFO, "end ciclo di controllo posizioni scadute");

    }


    /**
     * Esegue il check ora, per una data azienda.
     *
     * @param company l'azienda
     */
    public void run(BaseCompany company) {

        logger.log(Level.INFO, "Azienda " + company + ": start esecuzione controlli posizioni scadute");

        if (checkConfLevel1) {
            logger.log(Level.INFO, "Azienda " + company + ": start controllo prenotazioni scadute");
            checkMemoSchedaPren(company);
            logger.log(Level.INFO, "Azienda " + company + ": end controllo prenotazioni scadute");
        }

        if (checkConfLevel2) {
            logger.log(Level.INFO, "Azienda " + company + ": start controllo prenotazioni da congelare");
            checkCongelamentoPrenotazioni(company);
            logger.log(Level.INFO, "Azienda " + company + ": end controllo prenotazioni da congelare");
        }

        if (checkScadPagamento) {
            logger.log(Level.INFO, "Azienda " + company + ": start controllo scadenza pagamento");
            checkMemoScadenzaPagamento(company);
            logger.log(Level.INFO, "Azienda " + company + ": end controllo scadenza pagamento");
        }

        logger.log(Level.INFO, "Azienda " + company + ": end esecuzione controlli posizioni scadute");


    }

    /**
     * Sollecito conferma prenotazione,
     * (invio automatico al X° giorno dopo la opzione telefonica,
     * prolunga la scadenza di X giorni dopodiché verrà congelata)
     * <p>
     *
     * @param company - l'azienda da controllare
     */
    private void checkMemoSchedaPren(BaseCompany company) {
        EntityManager manager = EM.createEntityManager();

//		EROContainer cont = new EROContainer(Prenotazione.class, manager, company);
        ELazyContainer cont = new ELazyContainer(manager, Prenotazione.class, company);

        Filter filter;
        filter = new Compare.Equal(Prenotazione_.confermata.getName(), false); // non confermate
        cont.addContainerFilter(filter);
        filter = new Compare.Equal(Prenotazione_.congelata.getName(), false); // non congelate
        cont.addContainerFilter(filter);
        filter = new Compare.Less(Prenotazione_.scadenzaConferma.getName(), checkDate); // scadute conferma
        cont.addContainerFilter(filter);
        filter = new Compare.Less(Prenotazione_.livelloSollecitoConferma.getName(), 1); // liv. sollecito < 1
        cont.addContainerFilter(filter);

        logger.log(Level.INFO, "Azienda " + company + ": trovate " + cont.size() + " prenotazioni scadute");

        // spazzola le prenotazioni scadute ed esegue le azioni previste
        Collection<?> ids = cont.getItemIds();
        for (Object id : ids) {

//			EntityItem<?> item = cont.getItem(id);
//			Prenotazione pren = (Prenotazione) item.getEntity();

            Prenotazione pren = (Prenotazione) cont.getEntity(id);

            // invia la mail di sollecito.
            // controlla se va inviata per questa specifica prenotazione e la invia
            if (ModelliLettere.memoScadPrenotazione.isSend(pren)) {
                try {
                    PrenotazioneModulo.doPromemoriaInvioSchedaPrenotazione(pren, EventoApp.BOT_USER, null);
                } catch (EmailFailedException e) {
                    logger.log(Level.WARNING, "Azienda " + company + ": invio promemoria scheda prenotazione fallito: ");
                    logger.log(Level.WARNING, "Prenotazione: " + pren);
                    logger.log(Level.WARNING, "Errore: " + e.getMessage());
                }
            }

        }

        manager.close();

    }


    /**
     * avviso congelamento prenotazione, invio automatico alla scadenza ( quindi al 15° giorno dalla
     * opzione telefonica, o diversamente concordato) – lista d’attesa
     * <p>
     *
     * @param company - l'azienda da controllare
     */
    private void checkCongelamentoPrenotazioni(BaseCompany company) {
        EntityManager manager = EM.createEntityManager();
//		EROContainer cont = new EROContainer(Prenotazione.class, manager, company);
        ELazyContainer cont = new ELazyContainer(manager, Prenotazione.class, company);
        Filter filter;
        filter = new Compare.Equal(Prenotazione_.confermata.getName(), false); // non confermate
        cont.addContainerFilter(filter);
        filter = new Compare.Equal(Prenotazione_.congelata.getName(), false); // non congelate
        cont.addContainerFilter(filter);
        filter = new Compare.Less(Prenotazione_.scadenzaConferma.getName(), checkDate); // scadute conferma
        cont.addContainerFilter(filter);
        filter = new Compare.GreaterOrEqual(Prenotazione_.livelloSollecitoConferma.getName(), 1); // liv. sollecito >= 1 (già sollecitate)
        cont.addContainerFilter(filter);

        logger.log(Level.INFO, "Trovate " + cont.size() + " opzioni da congelare");

        // spazzola le prenotazioni e le congela
        Collection<?> ids = cont.getItemIds();
        for (Object id : ids) {
            Prenotazione pren = (Prenotazione) cont.getEntity(id);

//			EntityItem<?> item = cont.getItem(id);
//			Prenotazione pren = (Prenotazione) item.getEntity();

            try {
                PrenotazioneModulo.doCongelamentoOpzione(pren, EventoApp.BOT_USER, null);
            } catch (EmailFailedException e) {
                logger.log(Level.WARNING, "Invio avviso congelamento fallito: " + pren + ": " + e.getMessage());
            }
        }
        manager.close();

    }

    /**
     * Controlli pagamenti scaduti.
     * Promemoria scadenza di pagamento, invio automatico al giorno data-XXgg dall’evento nel caso di pagamento
     * non ancora registrato, scadenza pagamento al giorno data-60gg dall’evento
     * <p>
     *
     * @param company - l'azienda da controllare
     */
    private void checkMemoScadenzaPagamento(BaseCompany company) {
        EntityManager manager = EM.createEntityManager();

        ELazyContainer cont = new ELazyContainer(manager, Prenotazione.class, company);
//		EROContainer cont = new EROContainer(Prenotazione.class, manager, company);

        Filter filter;
        filter = new Compare.Equal(Prenotazione_.confermata.getName(), true); // confermate prenotazione
        cont.addContainerFilter(filter);
        filter = new Compare.Equal(Prenotazione_.pagamentoConfermato.getName(), false); // non confermate pagamento
        cont.addContainerFilter(filter);
        filter = new Compare.Less(Prenotazione_.scadenzaPagamento.getName(), checkDate); // scadute pagamento
        cont.addContainerFilter(filter);
        filter = new Compare.Less(Prenotazione_.livelloSollecitoPagamento.getName(), 1); // liv. sollecito < 1
        cont.addContainerFilter(filter);

        logger.log(Level.INFO, "Trovati " + cont.size() + " promemoria scadenza pagamento da inviare");

        // spazzola le prenotazioni ed esegue i controlli
        Collection<?> ids = cont.getItemIds();
        for (Object id : ids) {

//			EntityItem<?> item = cont.getItem(id);
//			Prenotazione pren = (Prenotazione) item.getEntity();

            Prenotazione pren = (Prenotazione) cont.getEntity(id);

            // invia solo se previsto nei settings, altrimenti logga soltanto
            if (ModelliLettere.memoScadPagamento.isSend(pren)) {
                try {
                    PrenotazioneModulo.doPromemoriaScadenzaPagamento(pren, EventoApp.BOT_USER, null);
                } catch (EmailFailedException e) {
                    logger.log(Level.WARNING, "Invio promemoria scadenza pagamento fallito: " + pren + ": " + e.getMessage());
                }
            } else {
                logger.log(Level.WARNING, "Skip invio promemoria scadenza pagamento (vedi config): " + pren);
            }

        }
        manager.close();

    }

    public void setCheckConfLevel1(boolean checkConfLevel1) {
        this.checkConfLevel1 = checkConfLevel1;
    }

    public void setCheckConfLevel2(boolean checkConfLevel2) {
        this.checkConfLevel2 = checkConfLevel2;
    }

    public void setCheckScadPagamento(boolean checkScadPagamento) {
        this.checkScadPagamento = checkScadPagamento;
    }


}
