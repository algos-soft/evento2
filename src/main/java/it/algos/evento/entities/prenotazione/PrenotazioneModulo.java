package it.algos.evento.entities.prenotazione;

import com.vaadin.data.Container;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import it.algos.evento.EventoApp;
import it.algos.evento.EventoBootStrap;
import it.algos.evento.entities.comune.Comune;
import it.algos.evento.entities.evento.Evento;
import it.algos.evento.entities.evento.Evento_;
import it.algos.evento.entities.insegnante.Insegnante;
import it.algos.evento.entities.lettera.*;
import it.algos.evento.entities.modopagamento.ModoPagamento;
import it.algos.evento.entities.prenotazione.eventi.EventoPren;
import it.algos.evento.entities.prenotazione.eventi.TipoEventoPren;
import it.algos.evento.entities.rappresentazione.Rappresentazione;
import it.algos.evento.entities.rappresentazione.Rappresentazione_;
import it.algos.evento.entities.scuola.Scuola;
import it.algos.evento.entities.spedizione.Spedizione;
import it.algos.evento.entities.stagione.Stagione;
import it.algos.evento.pref.CompanyPrefs;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.multiazienda.CompanyModule;
import it.algos.webbase.web.converter.StringToBigDecimalConverter;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.field.ArrayComboField;
import it.algos.webbase.web.field.EmailField;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.lib.LibDate;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.search.SearchManager;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.table.TablePortal;
import org.joda.time.DateTime;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("serial")
public class PrenotazioneModulo extends CompanyModule {


    public static final String PROP_PROGETTO = Rappresentazione.class.getSimpleName().toLowerCase() + "." + Rappresentazione_.evento.getName() + "." + Evento_.progetto.getName();
    public static final String PROP_EVENTO = Rappresentazione.class.getSimpleName().toLowerCase() + "." + Rappresentazione_.evento.getName();
    public static final String PROP_STAGIONE = Rappresentazione.class.getSimpleName().toLowerCase() + "." + Rappresentazione_.evento.getName() + "." + Evento_.stagione.getName();
    private final static Logger logger = Logger.getLogger(PrenotazioneModulo.class.getName());
    private ArrayList<StatusChangeListener> statusChangeListeners;


    /**
     * Costruttore senza parametri
     */
    public PrenotazioneModulo() {

        super(Prenotazione.class);


        // listener invocato quando il modulo diventa visibile
        addAttachListener(new AttachListener() {
            @Override
            public void attach(AttachEvent attachEvent) {

                // prenotazioni scadute
                if (LibSession.getAttribute(EventoApp.KEY_MOSTRA_PREN_SCADUTE) != null) {
                    LibSession.setAttribute(EventoApp.KEY_MOSTRA_PREN_SCADUTE, null);
                    changeFilter(PrenotazioneModulo.getFiltroPrenotazioniScadute());
                }

                // prenotazioni in ritardo di conferma pagamento
                if (LibSession.getAttribute(EventoApp.KEY_MOSTRA_PREN_RITARDO_PAGAMENTO_1) != null) {
                    LibSession.setAttribute(EventoApp.KEY_MOSTRA_PREN_RITARDO_PAGAMENTO_1, null);
                    changeFilter(PrenotazioneModulo.getFiltroPagamentiDaConfermare());
                }

                // prenotazioni con pagamento scaduto
                if (LibSession.getAttribute(EventoApp.KEY_MOSTRA_PREN_PAGAMENTO_SCADUTO) != null) {
                    LibSession.setAttribute(EventoApp.KEY_MOSTRA_PREN_PAGAMENTO_SCADUTO, null);
                    changeFilter(PrenotazioneModulo.getFiltroPagamentiScaduti());
                }


                // prenotazioni non confermate
                if (LibSession.getAttribute(EventoApp.KEY_MOSTRA_PREN_NON_CONFERMATE) != null) {
                    LibSession.setAttribute(EventoApp.KEY_MOSTRA_PREN_NON_CONFERMATE, null);
                    changeFilter(PrenotazioneModulo.getFiltroPren(false, null, null));
                }

                // prenotazioni confermate con pagamento non confermato
                if (LibSession.getAttribute(EventoApp.KEY_MOSTRA_PREN_PAGAMENTO_NON_CONFERMATO) != null) {
                    LibSession.setAttribute(EventoApp.KEY_MOSTRA_PREN_PAGAMENTO_NON_CONFERMATO, null);
                    changeFilter(PrenotazioneModulo.getFiltroPren(true, false, false));
                }

                // prenotazioni confermate con pagamento confermato ma non ricevuto
                if (LibSession.getAttribute(EventoApp.KEY_MOSTRA_PREN_PAGAMENTO_CONFERMATO) != null) {
                    LibSession.setAttribute(EventoApp.KEY_MOSTRA_PREN_PAGAMENTO_CONFERMATO, null);
                    changeFilter(PrenotazioneModulo.getFiltroPren(true, true, false));
                }

                // prenotazioni confermate con pagamento ricevuto
                if (LibSession.getAttribute(EventoApp.KEY_MOSTRA_PREN_PAGAMENTO_RICEVUTO) != null) {
                    LibSession.setAttribute(EventoApp.KEY_MOSTRA_PREN_PAGAMENTO_RICEVUTO, null);
                    changeFilter(PrenotazioneModulo.getFiltroPren(true, null, true));
                }

                // prenotazioni congelate
                if (LibSession.getAttribute(EventoApp.KEY_MOSTRA_PREN_CONGELATE) != null) {
                    LibSession.setAttribute(EventoApp.KEY_MOSTRA_PREN_CONGELATE, null);
                    changeFilter(PrenotazioneModulo.getFiltroPrenCongelate(Stagione.getStagioneCorrente()));
                }

            }
        });

    }// end of constructor

    @Override
    protected void init() {
        super.init();
        this.statusChangeListeners = new ArrayList<>();
    }

    public void addStatusChangeListener(StatusChangeListener l) {
        statusChangeListeners.add(l);
    }

    public interface StatusChangeListener {
        public void statusChanged(TipoEventoPren tipoEvento);
    }

//    public void fireStatusChanged(TipoEventoPren tipoEvento) {
//        for (StatusChangeListener l : statusChangeListeners) {
//            l.statusChanged(tipoEvento);
//        }
//    }

    /**
     * Assegna un nuovo filtro alla table
     */
    private void changeFilter(Filter filter) {
        Container.Filterable fCont = getTable().getFilterableContainer();
        fCont.removeAllContainerFilters();
        getTable().refresh(); // refresh container before applying new filters!
        fCont.addContainerFilter(filter);
    }

    public TablePortal createTablePortal() {
        return new PrenotazioneTablePortal(this);
    }// end of method

    @Override
    public ATable createTable() {
        return (new PrenotazioneTable(this));
    }// end of method

//    @Override
//    public ModuleForm createForm(Item item) {
//        PrenotazioneFormOld form = new PrenotazioneFormOld(this, item);
//
//        // refresh table dopo conferma prenotazione
//        form.setPrenotazioneConfermataListener(new PrenotazioneFormOld.PrenotazioneConfermataListener() {
//            @Override
//            public void prenotazioneConfermata(Prenotazione pren, Spedizione sped) {
//
//                getTable().refreshRowCache();
//
//                String detail = pren.toStringNumDataInsegnante();
//                String mailDetail = "";
//                if (sped != null) {
//                    mailDetail = "e-mail inviata a " + sped.getDestinatario();
//                }
//
//                Notification notif = new Notification("Prenotazione confermata: " + detail, mailDetail, Notification.Type.HUMANIZED_MESSAGE);
//                notif.setDelayMsec(-1);
//                notif.show(Page.getCurrent());
//
//            }
//        });
//
//        return form;
//    }// end of method


    @Override
    public ModuleForm createForm(Item item) {
        PrenotazioneForm form = new PrenotazioneForm(this, item);

        // refresh table dopo conferma prenotazione
        form.setPrenotazioneConfermataListener(new PrenotazioneForm.PrenotazioneConfermataListener() {
            @Override
            public void prenotazioneConfermata(Prenotazione pren, Spedizione sped) {

                getTable().refreshRowCache();

                String detail = pren.toStringNumDataInsegnante();
                String mailDetail = "";
                if (sped != null) {
                    mailDetail = "e-mail inviata a " + sped.getDestinatario();
                }

                Notification notif = new Notification("Prenotazione confermata: " + detail, mailDetail, Notification.Type.HUMANIZED_MESSAGE);
                notif.setDelayMsec(-1);
                notif.show(Page.getCurrent());

            }
        });

        return form;
    }// end of method

    @Override
    public SearchManager createSearchManager() {
        return new PrenotazioneSearch();
    }// end of method


    /**
     * Invio email istruzioni (no UI)
     */
    public static void doInvioIstruzioni(Prenotazione pren, String user) throws EmailFailedException {
        TipoEventoPren tipoEvento = TipoEventoPren.invioIstruzioni;
        sendEmailEvento(pren, tipoEvento, user);
    }

//    /**
//     * Invio email istruzioni (no UI) e fire status changed modulo
//     */
//    public void doInvioIstruzioniModulo(Prenotazione pren, String user) throws EmailFailedException {
//        PrenotazioneModulo.doInvioIstruzioni(pren, user);
//        fireStatusChanged(TipoEventoPren.invioIstruzioni);
//    }


    /**
     * Esecuzione conferma prenotazione (no UI)
     *
     * @param pren         la prenotazione
     * @param em           l'entity manager da utilizzare
     * @param dataConferma la data di conferma da registrare
     * @param user         l'utente che effettua questa operazione
     * @param destinatari  eventuali destinatari della mail (stringa separata da virgole)
     * @return la spedizione effettuata (null se non ha spedito nulla)
     */
    public static Spedizione doConfermaPrenotazione(Prenotazione pren, EntityManager em, Date dataConferma, String user, String destinatari) throws EmailFailedException {
        Spedizione sped = null;
        TipoEventoPren tipoEvento = TipoEventoPren.confermaPrenotazione;
        pren.setConfermata(true);
        pren.setDataConferma(dataConferma);
        pren.setCongelata(false);
        pren.save(em);

        PrenotazioneModulo.creaEvento(pren, tipoEvento, "", getUsername());

        logger.log(Level.INFO, tipoEvento.getDescrizione() + " " + pren);
        if (ModelliLettere.confermaPrenotazione.isSend(pren)) {
            if (destinatari != null && !destinatari.equals("")) {
                sped = sendEmailEvento(pren, tipoEvento, user, destinatari);
            }
        }

        return sped;
    }


    /**
     * Invio e-mail promemoria invio scheda prenotazione (no UI)
     * aumenta il livello di sollecito e prolunga la scadenza
     *
     * @param pren        la prenotazione
     * @param user        l'utente che genera l'evento
     * @param destinatari destinatari email - stringa separata da virgole,
     *                    null per usare i default dalle preferenze
     * @return il rapporto di spedizione e-mail (se eseguita)
     */
    public static Spedizione doPromemoriaInvioSchedaPrenotazione(Prenotazione pren, String user, String destinatari) throws EmailFailedException {

        TipoEventoPren tipoEvento = TipoEventoPren.promemoriaInvioSchedaPrenotazione;

        // manda l'e-mail
        Spedizione sped = sendEmailEvento(pren, tipoEvento, user, destinatari);

        // solo se e' riuscito a inviare l'email esegue il blocco seguente:
        // aumenta di 1 il livello di sollecito e prolunga la scadenza a X giorni da oggi
        pren.setLivelloSollecitoConferma(pren.getLivelloSollecitoConferma() + 1);
        int numDays = CompanyPrefs.ggProlungamentoConfDopoSollecito.getInt(pren.getCompany());
        Date date = new DateTime(LibDate.today()).plusDays(numDays).toDate();
        pren.setScadenzaConferma(date);
        pren.save();
        logger.log(Level.INFO, tipoEvento.getDescrizione() + " " + pren);

        return sped;

    }


//    /**
//     * Invio promemoria invio scheda prenotazione (no UI) e fire status changed modulo
//     *
//     * @param pren la prenotazione
//     * @param user l'utente che genera l'evento
//     */
//    public void doPromemoriaInvioSchedaPrenotazioneModulo(Prenotazione pren, String user) throws EmailFailedException {
//        doPromemoriaInvioSchedaPrenotazione(pren, user);
//        fireStatusChanged(TipoEventoPren.promemoriaInvioSchedaPrenotazione);
//    }


    /**
     * Congelamento opzione con eventuale invio di email di avviso (no UI)
     * <p>
     *
     * @param pren        la prenotazione da congelare
     * @param user        l'utente che ha effettuato l'operazione
     * @param destinatari indirizzi email separati da virgola, destinatari
     *                    della email di avviso (stringa vuota = non manda avviso,
     *                    null = agisce in base alle preferenze)
     * @return le info della eventuale spedizione
     */
    public static Spedizione doCongelamentoOpzione(Prenotazione pren, String user, String destinatari) throws EmailFailedException {
        Spedizione sped = null;
        TipoEventoPren tipoEvento = TipoEventoPren.congelamentoOpzione;

        // attiva il flag congelata, toglie la eventuale conferma, logga l'operazione
        pren.setCongelata(true);
        pren.setConfermata(false);
        pren.save();
        logger.log(Level.INFO, tipoEvento.getDescrizione() + " " + pren);


        // eventualmente invia le e-mail
        if (destinatari == null) { // decide in base alle preferenze
            if (ModelliLettere.congelamentoOpzione.isSend(pren)) {
                sped = sendEmailEvento(pren, tipoEvento, user);
            }
        } else {  // usa gli indirizzi forniti
            if (!destinatari.equals("")) {
                sped = sendEmailEvento(pren, tipoEvento, user, destinatari);
            }
        }

        // se ha effettuato la spedizione aumenta di 1 il livello di sollecito conferma
        if (sped != null && sped.isSpedita()) {
            int level = pren.getLivelloSollecitoConferma();
            pren.setLivelloSollecitoConferma(level + 1);
            pren.save();
        }

        return sped;

    }

//    /**
//     * Wrapper per le info di ritorno dalla operazione di congelamento
//     */
//    public class EsitoCongelamento{
//        private boolean success=false;
//        private Spedizione sped;
//
//        public EsitoCongelamento(boolean success, Spedizione sped) {
//            this.success = success;
//            this.sped = sped;
//        }
//
//        public boolean isSuccess() {
//            return success;
//        }
//
//        public Spedizione getSped() {
//            return sped;
//        }
//    }


//    /**
//     * Congelamento opzione (no UI)  e fire status changed modulo
//     * <p>
//     *
//     * @return true se ha inviato l'email
//     */
//    public boolean doCongelamentoOpzioneModulo(Prenotazione pren, String user) throws EmailFailedException {
//        boolean emailInviata=doCongelamentoOpzione(pren, user);
//        fireStatusChanged(TipoEventoPren.congelamentoOpzione);
//        return  emailInviata;
//    }

    /**
     * Controlli scadenza pagamento (no UI).
     *
     * @param pren        la prenotazione di riferimento
     * @param user        l'utente che effettua questa operazione
     * @param destinatari destinatari email - stringa separata da virgole,
     *                    null per usare i default dalle preferenze
     * @return il rapporto di spedizione e-mail (se eseguita)
     */
    public static Spedizione doPromemoriaScadenzaPagamento(Prenotazione pren, String user, String destinatari) throws EmailFailedException {
        TipoEventoPren tipoEvento = TipoEventoPren.promemoriaScadenzaPagamento;

        // invia la mail, incrementa il livello di sollecito
        // e prolunga la scadenza a X giorni da oggi
        pren.setLivelloSollecitoPagamento(pren.getLivelloSollecitoPagamento() + 1);
        int numDays = CompanyPrefs.ggProlungamentoPagamDopoSollecito.getInt(pren.getCompany());
        Date date = new DateTime(LibDate.today()).plusDays(numDays).toDate();
        pren.setScadenzaPagamento(date);
        pren.save();
        logger.log(Level.INFO, tipoEvento.getDescrizione() + " " + pren);
        Spedizione sped = sendEmailEvento(pren, tipoEvento, user, destinatari);

        return sped;

    }


//    /**
//     * Controlli scadenza pagamento (no UI) e fire status changed modulo
//     * @return true se ha inviato la mail di sollecito e spostato la scadenza
//     */
//    public boolean doPromemoriaScadenzaPagamentoModulo(Prenotazione pren, String user) throws EmailFailedException {
//        boolean eseguito=false;
//        eseguito=doPromemoriaScadenzaPagamento(pren, user);
//        if(eseguito){
//            fireStatusChanged(TipoEventoPren.promemoriaScadenzaPagamento);
//        }
//        return eseguito;
//    }


    /**
     * Conferma registrazione pagamento (no UI)
     * <p>
     *
     * @param pren            - la prenotazione
     * @param em              - l'EntityManager da usare per le operazioni
     * @param checkConfermato - true se il pagamento è stato confermato
     * @param checkRicevuto   - true se il pagamento è stato ricevuto
     * @param dataCompetenza  - la data di competenza della operazione
     * @param numInteri       - il nuovo numero di interi
     * @param numRidotti      - il nuovo numero di ridotti
     * @param numDisabili     - il nuovo numero di disabili
     * @param numAccomp       - il nuovo numero di accompagnatori
     * @param importoPagato   - l'importo pagato
     * @param mezzo           - il mezzo di pagamento
     * @param emails          - elenco di indirizzi email dei destinatari, comma-sep.
     * @return l'esito della eventuale spedizione, null se non aveva niente da spedire
     */
    public static Spedizione doConfermaRegistrazionePagamento(Prenotazione pren, EntityManager em, boolean checkConfermato, boolean checkRicevuto, Date dataCompetenza,
                                                              int numInteri, int numRidotti, int numDisabili, int numAccomp,
                                                              BigDecimal importoPagato, ModoPagamento mezzo,
                                                              String emails, String user) throws EmailFailedException {

        Spedizione sped = null;

        // indicatori di passaggio di stato da off a on
        boolean accesoConfermato = false;
        boolean accesoRicevuto = false;

        // modifica i valori nella prenotazione
        pren.setNumInteri(numInteri);
        pren.setNumRidotti(numRidotti);
        pren.setNumDisabili(numDisabili);
        pren.setNumAccomp(numAccomp);

        //pren.setImportoDaPagare(importoPrevisto);
        pren.setImportoPagato(importoPagato);
        pren.setModoPagamento(mezzo);

        // se è acceso pagamento confermato, registro flag confermato e data di conferma
        if ((!pren.isPagamentoConfermato()) && (checkConfermato)) {
            pren.setPagamentoConfermato(true);
            pren.setDataPagamentoConfermato(dataCompetenza);
            accesoConfermato = true;
        }

        // se si è acceso il flag pagamento ricevuto, registro flag ricevuto e data di ricevimento
        if ((!pren.isPagamentoRicevuto()) && (checkRicevuto)) {
            pren.setPagamentoRicevuto(true);
            pren.setDataPagamentoRicevuto(dataCompetenza);
            accesoRicevuto = true;
        }

        // registro la prenotazione
        pren.save(em);


        // invio le email se richiesto
        if (!emails.equals("")) {
            // determino il tipo di evento
            TipoEventoPren tipoEvento = null;
            if (checkConfermato) {
                tipoEvento = TipoEventoPren.confermaPagamento;
            }
            if (checkRicevuto) {
                tipoEvento = TipoEventoPren.registrazionePagamento;
            }
            // mando la(e) email
            if (tipoEvento != null) {
                sped = sendEmailEvento(pren, tipoEvento, user, emails);
            }
        }

        // Se il flag Confermato era spento e ora è stato acceso genero
        // un evento di conferma pagamento nel registro
        if (accesoConfermato) {
            PrenotazioneModulo.creaEvento(pren, TipoEventoPren.confermaPagamento, importoPagato.toString(), user);
        }

        // Se il flag Ricevuto era spento e ora è stato acceso genero
        // un evento di registrazione pagamento nel registro
        if (accesoRicevuto) {
            PrenotazioneModulo.creaEvento(pren, TipoEventoPren.registrazionePagamento, importoPagato.toString(), user);
        }

        return sped;


    }


//    /**
//     * Conferma registrazione pagamento (no UI)e fire status changed modulo
//     * <p>
//     *
//     * @return true se ha inviato una o più mail
//     */
//    public boolean doConfermaRegistrazionePagamentoModulo(Prenotazione pren, int numInteri, int numRidotti,
//                                                           int numDisabili, int numAccomp, BigDecimal importoPrevisto, BigDecimal importoPagato, ModoPagamento mezzo,
//                                                           boolean checkConfermato, boolean checkRicevuto, String user) throws EmailFailedException {
//
//        boolean mailInviata = false;
//        mailInviata=doConfermaRegistrazionePagamento(pren, numInteri, numRidotti, numDisabili, numAccomp, importoPrevisto, importoPagato, mezzo, checkConfermato, checkRicevuto, user);
//
////        // per ora lancio entrambi gli eventi
////        fireStatusChanged(TipoEventoPren.confermaPagamento);
////        fireStatusChanged(TipoEventoPren.registrazionePagamento);
//
//        return mailInviata;
//
//    }


    /**
     * Esecuzione invio attestato di partecipazione (no UI)
     */
    public static void doAttestatoPartecipazione(Prenotazione pren, String user) throws EmailFailedException {
        TipoEventoPren tipoEvento = TipoEventoPren.attestatoPartecipazione;
        PrenotazioneModulo.creaEvento(pren, tipoEvento, "", getUsername());
        logger.log(Level.INFO, tipoEvento.getDescrizione() + " " + pren);
        String addr = pren.getEmailRiferimento();
        if (!addr.equals("")) {
            sendEmailEvento(pren, tipoEvento, user, addr);
        } else {
            throw new EmailFailedException("Manca l'indirizzo email del referente nella prenotazione.");
        }
    }

//    /**
//     * Esecuzione invio attestato di partecipazione (no UI) e fire status changed modulo
//     */
//    public void doAttestatoPartecipazioneModulo(Prenotazione pren, String user) throws EmailFailedException {
//        doAttestatoPartecipazione(pren, user);
//        fireStatusChanged(TipoEventoPren.attestatoPartecipazione);
//    }


    /**
     * Invia una email per una dato evento di prenotazione.
     * <p>
     * I destinatari vengono recuperati dalla prenotazione in base al tipo di lettera
     * Crea un evento di spedizione mail (anche se l'invio fallisce)
     *
     * @param pren       la prenotazione
     * @param tipoEvento il tipo di evento
     * @param user       l'utente che genera l'evento
     */
    public static Spedizione sendEmailEvento(Prenotazione pren, TipoEventoPren tipoEvento, String user)
            throws EmailFailedException {
        return sendEmailEvento(pren, tipoEvento, user, null);
    }


    /**
     * Invia una email per un dato evento di prenotazione
     * <p>
     * Crea un evento di spedizione mail (anche se l'invio fallisce)
     *
     * @param pren       la prenotazione
     * @param tipoEvento il tipo di evento
     * @param user       l'utente che genera l'evento
     * @param addr       elenco indirizzi destinatari - se nullo li recupera dalla prenotazione in base al tipo di lettera
     * @return l'eisto della spedizione
     */
    public static Spedizione sendEmailEvento(Prenotazione pren, TipoEventoPren tipoEvento, String user, String addr)
            throws EmailFailedException {

        Spedizione sped = null;

        // prepara una mappa di informazioni email
        ModelliLettere modelloLettera = tipoEvento.getModelloLettera();

        if (modelloLettera != null) {

            // prepara una mappa di informazioni di sostituzione
            LetteraMap escapeMap = createEscapeMap(pren);

            // crea una mappa di informazioni generale per la stampa/invio delle lettere
            HashMap<String, Object> mailMap;
            try {
                Lettera lettera = Lettera.getLettera(modelloLettera, pren.getCompany());
                mailMap = createMailMap(pren, modelloLettera, lettera.getOggetto(), addr);
            } catch (EmailInfoMissingException e) {
                throw new EmailFailedException(e.getMessage());
            }

            // recupera la lettera da utilizzare
            Lettera lettera = Lettera.getLettera(modelloLettera, pren.getCompany());

            // spedisce la mail
            sped = LetteraService.spedisci(lettera, escapeMap, mailMap);

            // crea un nuovo evento di spedizione email
            creaEventoMail(pren, tipoEvento, user, mailMap, sped.isSpedita());

            if (sped.isSpedita()) {
                logger.log(Level.INFO, "Invio e-mail " + tipoEvento.getDescrizione() + " " + pren);
            } else {
                throw new EmailFailedException(sped.getErrore());
            }
        }

        return sped;

    }

    public static void notifyEmailFailed(EmailFailedException e) {
        Notification notification = new Notification("Invio email fallito", "\n" + e.getMessage(), Notification.Type.ERROR_MESSAGE);
        notification.setDelayMsec(-1);
        notification.show(Page.getCurrent());
    }

    /**
     * Crea un evento di invio mail per una data prenotazione
     *
     * @param pren       la prenotazione di riferimento
     * @param tipoEvento il tipo di evento
     * @param user       l'utente che ha generato l'evento
     * @param mailMap    la mappa mail per il recupero delle info di invio email
     * @param inviata    true se la mail è stata inviata con successo
     */
    private static void creaEventoMail(Prenotazione pren, TipoEventoPren tipoEvento, String user,
                                       HashMap<String, Object> mailMap, boolean inviata) {
        String detailString = "mail to:-> " + mailMap.get(MailKeys.destinatario.getKey());
        creaEvento(pren, tipoEvento, detailString, user, true, inviata);
    }

    /**
     * Crea una mappa di informazioni generale per la stampa/invio delle lettere
     *
     * @param addr elenco indirizzi destinatari - se vuoto o nullo
     *             li recupera dalla prenotazione in base al tipo di lettera
     */
    private static HashMap<String, Object> createMailMap(Prenotazione pren, ModelliLettere modello, String oggetto, String addr)
            throws EmailInfoMissingException {
        HashMap<String, Object> map = new HashMap<String, Object>();

        if (addr == null || addr.equals("")) {
            addr = modello.getEmailDestinatari(pren);
        }

        if (addr.equals("")) {
            throw new EmailInfoMissingException("Nessun indirizzo valido per la prenotazione " + pren);
        }

        // from: dalla company della prenotazione
        BaseCompany company = pren.getCompany();
        String from = CompanyPrefs.senderEmailAddress.getString(company);

        map.put(MailKeys.from.getKey(), from);
        map.put(MailKeys.destinatario.getKey(), addr);
        map.put(MailKeys.modello.getKey(), modello);
        map.put(MailKeys.oggetto.getKey(), oggetto);

        return map;
    }// end of method

    /**
     * Crea una mappa di informazioni di sostituzione per la stampa/invio delle lettere
     */
    private static LetteraMap createEscapeMap(Prenotazione pren) {
        Rappresentazione rapp;
        String string;
        StringToBigDecimalConverter bdConv = new StringToBigDecimalConverter(2);

        // prepara una mappa di informazioni da sostituire
        LetteraMap escapeMap = new LetteraMap();
        escapeMap.add(LetteraKeys.numeroPrenotazione, "" + pren.getNumPrenotazione());
        escapeMap.add(LetteraKeys.dataPrenotazione, LibDate.toStringDDMMYYYY(pren.getDataPrenotazione()));

        String strTitolo = "";
        String strCognome = "";
        String strNome = "";
        Insegnante ins = pren.getInsegnante();
        if (ins != null) {
            strTitolo = ins.getTitolo();
            strCognome = ins.getCognome();
            strNome = ins.getNome();
        }
        escapeMap.add(LetteraKeys.titoloInsegnante, strTitolo);
        escapeMap.add(LetteraKeys.cognomeInsegnante, strCognome);
        escapeMap.add(LetteraKeys.nomeInsegnante, strNome);

        escapeMap.add(LetteraKeys.telReferente, pren.getTelRiferimento());

        string = "";

        String nome = "";
        String indirizzo = "";
        String localita = "";
        String telefono = "";
        String fax = "";
        String email = "";
        Scuola scuola = pren.getScuola();

        if (scuola != null) {

            nome = scuola.getNome();

            // indirizzo
            indirizzo = scuola.getIndirizzo();

            // località
            String cap = scuola.getCap();
            if (cap != null && !cap.equals("")) {
                localita += " " + cap;
            }

            Comune comune = scuola.getComune();
            if (comune != null) {
                localita += " " + comune.toString();
            }

            string = scuola.getTelefono();
            if (string != null && !string.equals("")) {
                telefono = string;
            }

            string = scuola.getFax();
            if (string != null && !string.equals("")) {
                fax = string;
            }

            string = scuola.getEmail();
            if (string != null && !string.equals("")) {
                email = string;
            }

        }
        escapeMap.add(LetteraKeys.nomeScuola, nome);
        escapeMap.add(LetteraKeys.indirizzoScuola, indirizzo);
        escapeMap.add(LetteraKeys.localitaScuola, localita);
        escapeMap.add(LetteraKeys.telefonoScuola, telefono);
        escapeMap.add(LetteraKeys.faxScuola, fax);
        escapeMap.add(LetteraKeys.emailScuola, email);

        escapeMap.add(LetteraKeys.classe, pren.getClasse());

        String sData = "";
        String sOra = "";
        String titoloEvento = "";
        String nomeSala = "";
        rapp = pren.getRappresentazione();
        if (rapp != null) {
            Evento evento = rapp.getEvento();
            if (evento != null) {
                string = evento.getTitolo();
                if (string != null && !string.equals("")) {
                    titoloEvento = string;
                }
            }
            sData = LibDate.toStringDDMMYYYY(rapp.getDataRappresentazione());
            sOra = LibDate.toStringHHMM(rapp.getDataRappresentazione());
            nomeSala = rapp.getSala().getNome();
        }

        String sImpInteri = bdConv.convertToPresentation(pren.getImportoIntero());
        String sImpRidotti = bdConv.convertToPresentation(pren.getImportoRidotto());
        String sImpDisabili = bdConv.convertToPresentation(pren.getImportoDisabili());
        String sImpAccomp = bdConv.convertToPresentation(pren.getImportoAccomp());
        String sImpGruppo = bdConv.convertToPresentation(pren.getImportoGruppo());

        escapeMap.add(LetteraKeys.dataRappresentazione, sData);
        escapeMap.add(LetteraKeys.oraRappresentazione, sOra);
        escapeMap.add(LetteraKeys.nomeSala, nomeSala);

        escapeMap.add(LetteraKeys.titoloEvento, titoloEvento);
        escapeMap.add(LetteraKeys.importoIntero, sImpInteri);
        escapeMap.add(LetteraKeys.importoRidotto, sImpRidotti);
        escapeMap.add(LetteraKeys.importoDisabile, sImpDisabili);
        escapeMap.add(LetteraKeys.importoAccomp, sImpAccomp);
        escapeMap.add(LetteraKeys.importoGruppo, sImpGruppo);

        escapeMap.add(LetteraKeys.numPostiInteri, "" + pren.getNumInteri());
        escapeMap.add(LetteraKeys.numPostiRidotti, "" + pren.getNumRidotti());
        escapeMap.add(LetteraKeys.numPostiDisabili, "" + pren.getNumDisabili());
        escapeMap.add(LetteraKeys.numPostiAccomp, "" + pren.getNumAccomp());
        escapeMap.add(LetteraKeys.numPostiTotali, "" + pren.getNumTotali());

        escapeMap.add(LetteraKeys.dataScadenzaConfermaPrenotazione, LibDate.toStringDDMMYYYY(pren.getScadenzaConferma()));

        escapeMap.add(LetteraKeys.importoTotale, bdConv.convertToPresentation(pren.getImportoDaPagare()));

        string = "";
        ModoPagamento modo = pren.getModoPagamento();
        if (modo != null) {
            string = modo.toString();
        }
        escapeMap.add(LetteraKeys.modoPagamento, string);

        escapeMap.add(LetteraKeys.dataScadenzaPagamento, LibDate.toStringDDMMYYYY(pren.getScadenzaPagamento()));

        // data corrente
        string = LibDate.toStringDDMMYYYY(LibDate.today());
        escapeMap.add(LetteraKeys.dataCorrente, string);

        // data pagamento ricevuto
        string = LibDate.toStringDDMMYYYY(pren.getDataPagamentoRicevuto());
        escapeMap.add(LetteraKeys.dataPagamentoRicevuto, string);

        // importo effettivamente pagato
        escapeMap.add(LetteraKeys.importoPagato, bdConv.convertToPresentation(pren.getImportoPagato()));

        return escapeMap;
    }// end of method


    /**
     * Crea un evento relativo alla prenotazione nel registro Eventi
     * <p>
     *
     * @param pren     la prenotazione
     * @param tipo     il tipo di evento prenotazione
     * @param dettagli testo di dettaglio
     * @param user     nome dell'utente che invia
     * @param email    true se è un evento di tipo invio email
     * @param inviata  true se l'email è stata inviata con successo
     */
    public static EventoPren creaEvento(Prenotazione pren, TipoEventoPren tipo, String dettagli, String user, boolean email, boolean inviata) {
        EventoPren evento = new EventoPren();
        evento.setCompany(pren.getCompany());
        evento.setDettagli(dettagli);
        evento.setPrenotazione(pren);
        evento.setTimestamp(new Date());
        evento.setTipo(tipo.getId());
        evento.setUser(user);
        evento.setInvioEmail(email);
        evento.setEmailInviata(inviata);
        evento.save();
        return evento;
    }// end of method

    /**
     * Crea un evento relativo alla prenotazione nel registro Eventi
     */
    public static EventoPren creaEvento(Prenotazione pren, TipoEventoPren tipo, String dettagli, String user) {
        return creaEvento(pren, tipo, dettagli, user, false, false);
    }// end of method

    /**
     * Ritorna un filtro che seleziona tutte le prenotazioni
     * scadute e non confermate per la stagione corrente
     * (sono escluse le congelate)
     */
    public static Filter getFiltroPrenotazioniScadute() {
        DateTime jToday = new DateTime().withTimeAtStartOfDay();
        Date today = jToday.toDate();
        ArrayList<Filter> filters = new ArrayList<Filter>();
        filters.add(new Compare.Equal(PROP_STAGIONE, Stagione.getStagioneCorrente()));
        filters.add(new Compare.Equal(Prenotazione_.confermata.getName(), false));
        filters.add(new Compare.Less(Prenotazione_.scadenzaConferma.getName(), today));
        filters.add(new Compare.Equal(Prenotazione_.congelata.getName(), false));
        Filter outFilter = new And(filters.toArray(new Filter[0]));
        return outFilter;
    }// end of method


    /**
     * Ritorna un filtro che seleziona tutti i pagamenti scaduti e
     * non confermati per la stagione corrente
     * (sono escluse le prenotazioni congelate)
     */
    public static Filter getFiltroPagamentiDaConfermare() {
        DateTime jToday = new DateTime().withTimeAtStartOfDay();
        Date today = jToday.toDate();
        ArrayList<Filter> filters = new ArrayList<Filter>();
        filters.add(new Compare.Equal(PROP_STAGIONE, Stagione.getStagioneCorrente()));
        filters.add(new Compare.Equal(Prenotazione_.confermata.getName(), true));
        filters.add(new Compare.Equal(Prenotazione_.pagamentoConfermato.getName(), false));
        filters.add(new Compare.Less(Prenotazione_.scadenzaPagamento.getName(), today));
        filters.add(new Compare.Equal(Prenotazione_.congelata.getName(), false));
        Filter outFilter = new And(filters.toArray(new Filter[0]));
        return outFilter;
    }// end of method


    /**
     * Ritorna un filtro che seleziona tutti i pagamenti scaduti e
     * non confermati per la stagione corrente
     * (sono escluse le prenotazioni congelate)
     */
    public static Filter getFiltroPagamentiScaduti() {
        DateTime jToday = new DateTime().withTimeAtStartOfDay();
        Date today = jToday.toDate();
        ArrayList<Filter> filters = new ArrayList<Filter>();
        filters.add(new Compare.Equal(PROP_STAGIONE, Stagione.getStagioneCorrente()));
        filters.add(new Compare.Equal(Prenotazione_.confermata.getName(), true));
        filters.add(new Compare.Equal(Prenotazione_.pagamentoConfermato.getName(), true));
        filters.add(new Compare.Equal(Prenotazione_.pagamentoRicevuto.getName(), false));
        filters.add(new Compare.Less(Prenotazione_.scadenzaPagamento.getName(), today));
        filters.add(new Compare.Equal(Prenotazione_.congelata.getName(), false));
        Filter outFilter = new And(filters.toArray(new Filter[0]));

        return outFilter;
    }// end of method

//
//    /**
//     * Ritorna un filtro che seleziona tutti i pagamenti confermati ma ancora da ricevere
//     */
//    public static Filter getFiltroPagamentiDaRicevere() {
//        ArrayList<Filter> filters = new ArrayList<Filter>();
//        filters.add(new Compare.Equal(PROP_STAGIONE, Stagione.getStagioneCorrente()));
//        filters.add(new Compare.Equal(Prenotazione_.pagamentoConfermato.getName(), true));
//        filters.add(new Compare.Equal(Prenotazione_.pagamentoRicevuto.getName(), false));
//        Filter outFilter = new And(filters.toArray(new Filter[0]));
//        return outFilter;
//    }// end of method
//


    /**
     * Ritorna un filtro che seleziona tutte le prenotazioni della stagione corrente
     * secondo i flag Prenotazione Confermata, Pagamento Confermato, Pagamento Ricevuto.
     * (le prenotazioni congelate sono escluse)
     *
     * @param prenConf flag per prenotazione confermata (null per non selezionare)
     * @param pagaConf flag per pagamento confermato (null per non selezionare)
     * @param pagaRic  flag per pagamento ricevuto (null per non selezionare)
     */
    public static Filter getFiltroPren(Boolean prenConf, Boolean pagaConf, Boolean pagaRic) {
        ArrayList<Filter> filters = new ArrayList<Filter>();
        filters.add(new Compare.Equal(PROP_STAGIONE, Stagione.getStagioneCorrente()));
        if (prenConf != null) {
            filters.add(new Compare.Equal(Prenotazione_.confermata.getName(), prenConf));
        }
        if (pagaConf != null) {
            filters.add(new Compare.Equal(Prenotazione_.pagamentoConfermato.getName(), pagaConf));
        }
        if (pagaRic != null) {
            filters.add(new Compare.Equal(Prenotazione_.pagamentoRicevuto.getName(), pagaRic));
        }

        filters.add(new Compare.Equal(Prenotazione_.congelata.getName(), false));

        Filter outFilter = new And(filters.toArray(new Filter[0]));
        return outFilter;
    }// end of method


    /**
     * Ritorna un filtro che seleziona tutte le prenotazioni
     * congelate della stagione corrente
     */
    public static Filter getFiltroPrenCongelate() {
        return getFiltroPrenCongelate(Stagione.getStagioneCorrente());
    }// end of method

    /**
     * Ritorna un filtro che seleziona tutte le prenotazioni
     * congelate di una stagione
     */
    public static Filter getFiltroPrenCongelate(Stagione stagione) {
        ArrayList<Filter> filters = new ArrayList<Filter>();
        filters.add(new Compare.Equal(PROP_STAGIONE, stagione));
        filters.add(new Compare.Equal(Prenotazione_.congelata.getName(), true));
        Filter outFilter = new And(filters.toArray(new Filter[0]));
        return outFilter;
    }// end of method



    /**
     * Ritorna un filtro che seleziona tutte le prenotazioni
     * con pagamento confermato ma non ricevuto della stagione corrente
     */
    public static Filter getFiltroPrenPagamentoNonRicevuto() {
        return getFiltroPrenPagamentoNonRicevuto(Stagione.getStagioneCorrente());
    }// end of method

    /**
     * Ritorna un filtro che seleziona tutte le prenotazioni
     * con pagamento confermato ma non ricevuto di una stagione
     */
    public static Filter getFiltroPrenPagamentoNonRicevuto(Stagione stagione) {
        ArrayList<Filter> filters = new ArrayList<Filter>();
        filters.add(new Compare.Equal(PROP_STAGIONE, stagione));
        filters.add(new Compare.Equal(Prenotazione_.pagamentoConfermato.getName(), true));
        filters.add(new Compare.Equal(Prenotazione_.pagamentoRicevuto.getName(), false));
        Filter outFilter = new And(filters.toArray(new Filter[0]));
        return outFilter;
    }// end of method



    private static String getUsername() {
        return EventoBootStrap.getUsername();
    }


    /**
     * Invia una lettera a scelta per una data prenotazione
     */
    public static void testLettera(Prenotazione pren) {
        DialogoTestLettera dialogo = new DialogoTestLettera(pren);
        dialogo.show(UI.getCurrent());
    }

    static class DialogoTestLettera extends ConfirmDialog {

        private Prenotazione pren;
        private ArrayComboField letteraField;
        private EmailField emailField;


        public DialogoTestLettera(Prenotazione pren) {
            super(null);
            this.pren = pren;
            setTitle("Test invio lettera");
            letteraField = new ArrayComboField(ModelliLettere.values(), "Modello lettera");
            addComponent(letteraField);
            emailField = new EmailField("email");
            addComponent(emailField);
            setConfirmButtonText("Invia");
        }

        @Override
        protected void onConfirm() {
            boolean cont = true;
            ModelliLettere modello = null;
            String email = null;

            Object value = letteraField.getValue();
            if (value != null) {
                modello = (ModelliLettere) value;
            } else {
                cont = false;
            }

            if (cont) {
                email = emailField.getValue();
                if (email.equals("")) {
                    cont = false;
                }
            }

            if (cont) {

                // prepara una mappa di informazioni per la prenotazione
                LetteraMap escapeMap = createEscapeMap(pren);

                // prepara una mappa di informazioni email
                HashMap<String, Object> mailMap = null;
                try {
                    mailMap = createMailMap(pren, modello, modello.getOggetto(), email);

                    // spedisce la mail
                    Lettera lettera = Lettera.getLettera(modello, pren.getCompany());
                    Spedizione sped = LetteraService.spedisci(lettera, escapeMap, mailMap);
                    if (sped.isSpedita()) {
                        Notification.show("Email spedita");
                        super.onConfirm();
                    } else {
                        Notification notification = new Notification("Invio email fallito", "\n" + sped.getErrore(), Notification.Type.ERROR_MESSAGE);
                        notification.setDelayMsec(-1);
                        notification.show(Page.getCurrent());
                    }


                } catch (EmailInfoMissingException e) {
                    Notification notification = new Notification("Informazioni mancanti", "\n" + e.getMessage(), Notification.Type.ERROR_MESSAGE);
                    notification.setDelayMsec(-1);
                    notification.show(Page.getCurrent());
                }

            }

        }


    }


}// end of class
