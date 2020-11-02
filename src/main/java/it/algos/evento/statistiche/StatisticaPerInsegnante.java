package it.algos.evento.statistiche;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.ui.*;
import it.algos.evento.entities.insegnante.Insegnante;
import it.algos.evento.entities.insegnante.Insegnante_;
import it.algos.evento.entities.rappresentazione.Rappresentazione;
import it.algos.evento.entities.rappresentazione.Rappresentazione_;
import it.algos.webbase.multiazienda.ELazyContainer;
import it.algos.webbase.web.component.DateRangeComponent;
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.lib.LibDate;

import javax.persistence.EntityManager;
import java.util.*;

@SuppressWarnings("serial")
public class StatisticaPerInsegnante extends StatisticaBase {

    private static final boolean DEBUG = true;

    private ArrayList<Rappresentazione> listaRap;
    private LinkedHashMap<String, Integer> footerTotali = new LinkedHashMap<String, Integer>();
    private boolean allInsegnanti = false;

    public String getName() {
        return "Statistica per referente";
    }// end of method

    /**
     * Predispone un componente di controllo/elaborazione dell'intervallo di statistica <br>
     * -visualizza una selezione dell'intervallo di statistica (di solito) <br>
     * -visualizza un bottone di comando (di solito) <br>
     */
    @Override
    protected void creaComponentePannello() {
        super.creaComponentePannello();
        CheckBox box = creaCheckBox();
        HorizontalLayout layout = new HorizontalLayout();

        if (componenteStatistica != null && componenteStatistica instanceof Layout) {
            layout = (HorizontalLayout) componenteStatistica;
            layout.addComponent(box, 1);
            layout.setComponentAlignment(box, Alignment.BOTTOM_CENTER);
        }// fine del blocco if
    }// end of method

    /**
     * Predispone un componente di selezione dell'intervallo di statistica (dipende dalla sottoclasse) <br>
     */
    @Override
    protected Component creaComponentePeriodo() {
        Component componentePeriodo = super.creaComponentePeriodo();

        if (DEBUG) {
            Date dataIni = new Date();
            Date dataFine = LibDate.fromFineMeseAnno("novembre", 2014);
            if (componentePeriodo instanceof DateRangeComponent) {
                DateRangeComponent dataRange = (DateRangeComponent) componentePeriodo;
                dataRange.setDate1(dataIni);
                dataRange.setDate2(dataFine);
            }// fine del blocco if
        }// fine del blocco if

        return componentePeriodo;
    }// end of method

    /**
     * crea il container con le colonne
     */
    @Override
    protected void creaContainer() {
        container = new IndexedContainer();
        creaColonne();
    }// end of method

    /**
     * aggiunge al container le colonne delle rappresentazioni <br>
     * variabili in funzione della data <br>
     */
    private void creaColonne() {
        Date inizio = this.getData1();
        Date fine = this.getData2();
        String titolo;

        // prime 2 colonne fisse
        addContainerProperty(Colonne.nomeInsegnante);
        addContainerProperty(Colonne.materie);

        // lista delle rappresentazioni in ordine di data
        listaRap = super.getRappresentazioni(inizio, fine, Rappresentazione_.dataRappresentazione.getName());

        // titoli delle colonne variabili (oltre a quelle degli insegnanti e del totale)
        for (Rappresentazione rappresentazione : listaRap) {
            titolo = getTitoloColonna(rappresentazione);
            container.addContainerProperty(titolo, Integer.class, 0);
            footerTotali.put(titolo, 0);
        }// end of for cycle

        // ultima colonna
        addContainerProperty(Colonne.totale);
    }// end of method

    private String getTitoloColonna(Rappresentazione rappresentazione) {
        String titolo = "";
        Date data = rappresentazione.getDataRappresentazione();
        titolo = LibDate.toStringDMYYHHMM(data);
        titolo += "<br>";
        titolo += rappresentazione.getEvento().getSigla();

        return titolo;
    }// end of method

    /**
     * popola il container con i dati
     */
    @Override
    protected void popola(Date data1, Date data2) {
        super.popola(data1, data2);
        Table tavola = getTable();

        // lista delle rappresentazioni in ordine di data
        ArrayList<Rappresentazione> listaRap = super.getRappresentazioni(data1, data2);

        // lista degli insegnanti in ordine alfabetico di cognome e nome
        //List<Insegnante> listaIns = Insegnante.getList();
        List<Insegnante> listaIns = getInsegnanti();

        for (Insegnante inse : listaIns) {
            addRiga(inse, listaRap);
        }// end of for cycle

        int totGen = 0;
        for (String chiave : footerTotali.keySet()) {
            int tot = footerTotali.get(chiave);
            totGen += tot;
            tavola.setColumnFooter(chiave, "" + tot);
        }// end of for cycle

        tavola.setColumnFooter(Colonne.totale.getTitolo(), "" + totGen);


    }// end of method


    /**
     * @return la lista coompleta degli insegnanti in ordine alfabetico di cognome e nome
     */
    private List<Insegnante> getInsegnanti() {
        ArrayList<Insegnante> lista = new ArrayList<Insegnante>();

        Collection<Object> listaIds = null;

        EntityManager manager = EM.createEntityManager();

        ELazyContainer container = new ELazyContainer(manager, Insegnante.class);

        container.sort(new Object[]{Insegnante_.cognome.getName(), Insegnante_.nome.getName()}, new boolean[]{true, true});

        for (Object id : container.getItemIds()) {

            Insegnante insegnante = (Insegnante)container.getEntity(id);

            lista.add(insegnante);
        }// end of for cycle

        manager.close();

        return lista;

    }

    // resetta i totali
    protected void resetTotali() {
        // for (int tot : footerTotali) {
        // tot = 0;
        // }// end of for cycle
    }// end of method


    @SuppressWarnings("unchecked")
    private void addRiga(Insegnante inse, ArrayList<Rappresentazione> listaRap) {
        String titolo;
        int value = 0;
        int totalePerRiga = 0;
        int totalePerColonna = 0;

        for (Rappresentazione rap : listaRap) {
            value = haPartecipato(inse, rap);
            totalePerRiga += value;
        }// end of for cycle

        if (totalePerRiga > 0 || allInsegnanti) {
            totalePerRiga = 0;
            Item item = container.getItem(container.addItem());
            item.getItemProperty(Colonne.nomeInsegnante.getTitolo()).setValue(inse.getCognomeNome());
            item.getItemProperty(Colonne.materie.getTitolo()).setValue(inse.getMaterie());
            for (Rappresentazione rap : listaRap) {
                titolo = getTitoloColonna(rap);
                value = haPartecipato(inse, rap);
                item.getItemProperty(titolo).setValue(value);
                totalePerRiga += value;
                // totale per colonna
                totalePerColonna = footerTotali.get(titolo);
                totalePerColonna += value;
                footerTotali.put(titolo, totalePerColonna);
            }// end of for cycle
            item.getItemProperty(Colonne.totale.getTitolo()).setValue(totalePerRiga);
        }// fine del blocco if

    }// end of method


    /**
     * Controlla se un dato insegnante ha partecipato a una data rappresentazione.
     *
     * @param inse l'insegnante
     * @param rap  la rappresentazione
     * @return 1 se ha partecipato, altrimenti 0
     */
    private int haPartecipato(Insegnante inse, Rappresentazione rap) {
        int value = 0;
        List<Insegnante> insegnanti = rap.getInsegnanti();
        if (insegnanti.contains(inse)) {
            value = 1;
        }
        return value;
    }

    /**
     * Crea un checkbox di controllo <br>
     */
    private CheckBox creaCheckBox() {
        CheckBox box = new CheckBox("Tutti i referenti");

        box.addFocusListener(new FocusListener() {
            @Override
            public void focus(FocusEvent event) {
                allInsegnanti = !allInsegnanti;
            }// end of inner method
        });// end of anonymous class

        return box;
    }// end of method

}// end of class
