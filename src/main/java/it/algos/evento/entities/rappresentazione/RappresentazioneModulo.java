package it.algos.evento.entities.rappresentazione;

import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.Or;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import it.algos.evento.entities.evento.Evento;
import it.algos.evento.entities.insegnante.Insegnante;
import it.algos.evento.entities.prenotazione.Prenotazione;
import it.algos.evento.entities.prenotazione.Prenotazione_;
import it.algos.evento.multiazienda.EQuery;
import it.algos.webbase.multiazienda.CompanyModule;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.web.dialog.AlertDialog;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.entity.BaseEntity_;
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.entity.Entities;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.lib.LibDate;
import it.algos.webbase.web.lib.LibFilter;
import it.algos.webbase.web.search.SearchManager;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.table.TablePortal;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.addons.lazyquerycontainer.LazyEntityContainer;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.Attribute;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class RappresentazioneModulo extends CompanyModule {

    /**
     * Costruttore senza parametri
     */
    public RappresentazioneModulo() {
        super(Rappresentazione.class);
    }// end of constructor


    /**
     * Ritorna i posti prenotati per una data rappresentazione.
     */
    public int getPostiPrenotati(Rappresentazione rapp) {
        return RappresentazioneModulo.countPostiPrenotati(rapp, getEntityManager());
    }



    /**
     * Ritorna i posti prenotati per una data rappresentazione.
     * (sono esclusi i posti delle prenotazioni congelate)
     *
     * @param rapp la rappresentazione
     * @return il numero di posti prenotati
     */
    public static int countPostiPrenotati(Rappresentazione rapp, EntityManager em) {

        // filtro che seleziona tutte le prenotazioni non congelate della rappresentazione
        Filter[] aFilters = {
                new Compare.Equal(Prenotazione_.rappresentazione.getName(), rapp),
                new Compare.Equal(Prenotazione_.congelata.getName(), false)
        };

        int quantiPrenotati = countPostiPrenotati(aFilters, em);
        return quantiPrenotati;
    }


    /**
     * Ritorna il numero posti prenotati per una lista di rappresentazioni.
     * Esclude le prenotazioni congelate.
     *
     * @param filters il filtro sulle rappresentazioni
     */
    public static int countPostiPrenotati(Filter[] filters, EntityManager em) {

//        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
        Root<Prenotazione> root = cq.from(Prenotazione.class);

        List<Predicate> predicates = new ArrayList<>();
        for (Filter f : filters) {
            Predicate pred = LibFilter.getPredicate(f, cb, cq, root);
            predicates.add(pred);
        }

        cq.where(predicates.toArray(new Predicate[]{}));

        Expression<Integer> sTot = cb.sum(EQuery.getExprPostiPrenotati(cb, root));
        cq.select(sTot);

        TypedQuery<Integer> q = em.createQuery(cq);
        Integer num = q.getSingleResult();
        if (num == null) {
            num = 0;
        }

        return num;

    }


    /**
     * Ritorna i posti disponibili per una data rappresentazione.
     */
    public static int countPostiDisponibili(Rappresentazione rapp, EntityManager em) {
        return rapp.getCapienza() - countPostiPrenotati(rapp, em);
    }

    /**
     * Ritorna i posti disponibili per una data rappresentazione.
     */
    public int getPostiDisponibili(Rappresentazione rapp) {
        return countPostiDisponibili(rapp, getEntityManager());
    }


    /**
     * Esporta tutte le prenotazioni relative a un elenco di rappresentazioni.
     *
     * @param rapps l'elenco delle rappresentazioni
     */
    public static void esportaPrenotazioni(Rappresentazione[] rapps) {
        EntityManager em = EM.createEntityManager();
        String titoloReport = "Report prenotazioni "+ LibDate.toStringDDMMYYYY(LibDate.today());;
        LazyEntityContainer cont = new LazyEntityContainer<BaseEntity>(em, Prenotazione.class, 100, BaseEntity_.id.getName(), true, true, true);
        Entities.addPropertiesToContainer(cont, Prenotazione.class);

        // filtro che seleziona le prenotazioni delle rappresentazioni
        Filter[] filtersToAdd = new Filter[rapps.length];
        int i = 0;
        for (Rappresentazione rapp : rapps) {
            filtersToAdd[i++] = new Compare.Equal(Prenotazione_.rappresentazione.getName(), rapp);
        }
        Filter f;
        if (filtersToAdd.length > 1) {
            f = new Or(filtersToAdd);
        } else {
            f = filtersToAdd[0];
        }
        cont.addContainerFilter(f);

        // ordina per rappresentazione e sotto per n.prenotazione
        cont.sort(new Object[]{Prenotazione_.rappresentazione.getName(), Prenotazione_.numPrenotazione.getName()}, new boolean[]{true, true});
        Table table = new Table();
        table.setContainerDataSource(cont);

        table.setVisibleColumns(
                Prenotazione_.rappresentazione.getName(),
                Prenotazione_.numPrenotazione.getName(),
                Prenotazione_.scuola.getName(),
                Prenotazione_.insegnante.getName(),
                Prenotazione_.numInteri.getName(),
                Prenotazione_.numRidotti.getName(),
                Prenotazione_.numDisabili.getName(),
                Prenotazione_.numAccomp.getName(),
                Prenotazione_.numTotali.getName());

        table.setColumnHeaders("Rappresentazione",
                "N.Prenotazione",
                "Scuola",
                "Insegnante",
                "Interi",
                "Ridotti",
                "Disabili",
                "Accomp.",
                "Totale");


        final ExcelExport excelExport = new ExcelExport(table);
        excelExport.setReportTitle(titoloReport);
        String filename = StringUtils.stripAccents(titoloReport) + ".xls";    // or ExcelExport throws errors!
        excelExport.setExportFileName(filename);

        UI ui = UI.getCurrent();
        Component oldContent = ui.getContent();
        ui.setContent(table);
        excelExport.export();
        ui.setContent(oldContent);
        em.close();
    }


    /**
     * Esporta tutti i partecipanti relativi a un elenco di rappresentazioni.
     *
     * @param rapps l'elenco delle rappresentazioni
     */
    public static void esportaPartecipanti(Rappresentazione[] rapps) {
        EntityManager em = EM.createEntityManager();
        String titoloReport = "Report partecipanti "+ LibDate.toStringDDMMYYYY(LibDate.today());

        // crea un container contenente un wrapper per ogni
        // partecipazione alle rappresentazioni
        BeanItemContainer<PartecipazioneBean> container = new BeanItemContainer(PartecipazioneBean.class);
        for (Rappresentazione rapp : rapps) {
            List<Insegnante> insegnanti = rapp.getInsegnanti();
            for (Insegnante ins : insegnanti) {
                PartecipazioneBean bean = new PartecipazioneBean(ins, rapp);
                container.addBean(bean);
            }
        }

        // crea una table da esportare
        Table table = new Table();
        table.setContainerDataSource(container);

        // i nomi delle visible columns devono corrispondere alle properties
        // del bean (metodi getter senza parola "get")!
        table.setVisibleColumns(new Object[]{"data", "nomeEvento", "cognome", "nome", "email"});
        table.setColumnHeaders(new String[]{"Data", "Evento", "Cognome", "Nome", "Email"});

        final ExcelExport excelExport = new ExcelExport(table);
        excelExport.setReportTitle(titoloReport);
        String filename = StringUtils.stripAccents(titoloReport) + ".xls";    // or ExcelExport throws errors!
        excelExport.setExportFileName(filename);

        UI ui = UI.getCurrent();
        Component oldContent = ui.getContent();
        ui.setContent(table);
        excelExport.export();
        ui.setContent(oldContent);
        em.close();
    }



    public void esportaPartecipanti(UI ui) {


        // crea un container contenente un wrapper per ogni partecipazione
        // alle rappresentazioni correntemente elencate in tabella
        BeanItemContainer<PartecipazioneBean> container = new BeanItemContainer(PartecipazioneBean.class);
        BaseEntity[] entities = getTable().getSelectedEntities();
        for (BaseEntity entity : entities) {
            Rappresentazione rapp = (Rappresentazione) entity;
            List<Insegnante> insegnanti = rapp.getInsegnanti();
            for (Insegnante ins : insegnanti) {
                PartecipazioneBean bean = new PartecipazioneBean(ins, rapp);
                container.addBean(bean);
            }
        }

        // crea una table da esportare
        Table table = new Table();
        String titoloReport = "Riepilogo partecipanti";
        table.setContainerDataSource(container);

        // i nomi devono corrispondere alle properties del bean (metodi getter senza parola "get")!
        table.setVisibleColumns(new Object[]{"data", "evento", "cognome", "nome", "email"});
        table.setColumnHeaders(new String[]{"Data", "Evento", "Cognome", "Nome", "Email"});

        final ExcelExport excelExport;

        excelExport = new ExcelExport(table);
        excelExport.setReportTitle(titoloReport);
        excelExport.setExportFileName(titoloReport + ".xls");
        excelExport.setDisplayTotals(false);

        Component oldContent = ui.getContent();
        ui.setContent(table);
        excelExport.export();
        ui.setContent(oldContent);


    }// end of method


//    /**
//     * @param id    id della rappresentazione
//     * @param lista lista delle prenotazioni della rappresentazione
//     * @param ui    la ui
//     */
//    private static void tableExport(Object id, ArrayList<Prenotazione> lista, UI ui) {
//        Table table = new Table();
//        String titoloReport = getTitoloReport(id);
//        BeanItemContainer<Prenotazione> container = new BeanItemContainer<Prenotazione>(Prenotazione.class);
//
//        for (Prenotazione bean : lista) {
//            container.addBean(bean);
//        }// end of for cycle
//
//        table.setContainerDataSource(container);
//
//        table.setVisibleColumns(new Object[]{Prenotazione_.scuola.getName(), Prenotazione_.insegnante.getName(), Prenotazione_.numInteri.getName(), Prenotazione_.numRidotti.getName(),
//                Prenotazione_.numDisabili.getName(), Prenotazione_.numAccomp.getName(), Prenotazione_.numTotali.getName()});
//        table.setColumnHeaders(new String[]{"Scuola", "Insegnante", "Interi", "Ridotti", "Disabili", "Accomp.",
//                "Totale"});
//
//        //comp.addComponent(table);
//        final ExcelExport excelExport;
//
//        excelExport = new ExcelExport(table);
//        excelExport.setReportTitle(titoloReport);
//        String filename = StringUtils.stripAccents(titoloReport) + ".xls";    // or ExcelExport throws errors!
//        excelExport.setExportFileName(filename);
//
//        Component oldContent = ui.getContent();
//        ui.setContent(table);
//        excelExport.export();
//        ui.setContent(oldContent);
//
//        //comp.removeComponent(table);
//
//
//    }// end of method


    private static String getTitoloReport(Object id) {
        String titoloReport = "";
        Rappresentazione rappresentazione = Rappresentazione.read(id);
        Evento evento;

        if (rappresentazione != null) {
            evento = rappresentazione.getEvento();
            if (evento != null) {
                titoloReport = evento.getTitolo();
                titoloReport += " - " + rappresentazione.getDateAsString();
            }// end of if cycle
        }// end of if cycle

        return titoloReport;
    }// end of method

    private static ArrayList<Prenotazione> getListaPrenotazioni(Object id) {
        ArrayList<Prenotazione> lista = null;
        List<? extends BaseEntity> listaBean;
        Rappresentazione rappresentazione = Rappresentazione.read(id);

        // qui potrei usare AQuery o EQuery indifferentemente tanto le
        // prenotazioni sono legate direttamente alla rappresentazione
        listaBean = CompanyQuery.getList(Prenotazione.class, Prenotazione_.rappresentazione, rappresentazione);

        if (listaBean != null) {
            lista = new ArrayList<Prenotazione>();
            for (BaseEntity bean : listaBean) {
                lista.add((Prenotazione) bean);
            }// end of for cycle
        }// end of if cycle

        return lista;
    }// end of method

    // come default usa il titolo standard
    // può essere sovrascritto nelle sottoclassi specifiche
    protected String getCaptionSearch() {
        return "rappresentazioni";
    }// end of method

    // come default spazzola tutti i campi della Entity
    // può essere sovrascritto nelle sottoclassi specifiche
    // serve anche per l'ordine con cui vengono presentati i campi
    protected Attribute<?, ?>[] creaFieldsList() {
        return new Attribute[]{Rappresentazione_.dataRappresentazione, Rappresentazione_.evento,
                Rappresentazione_.sala};
    }// end of method

    // come default spazzola tutti i campi della Entity
    // non garantisce l'ordine con cui vengono presentati i campi
    // può essere sovrascritto nelle sottoclassi specifiche (garantendo l'ordine)
    // può mostrare anche il campo ID, oppure no
    // se si vuole differenziare tra Table, Form e Search, sovrascrivere
    // creaFieldsList, creaFieldsForm e creaFieldsSearch
    protected Attribute<?, ?>[] creaFieldsAll() {
        return new Attribute[]{Rappresentazione_.evento, Rappresentazione_.sala, Rappresentazione_.capienza,
                Rappresentazione_.dataRappresentazione, Rappresentazione_.note, Rappresentazione_.insegnanti};
    }// end of method

    @Override
    public ATable createTable() {
        return (new RappresentazioneTable(this));
    }// end of method

    @Override
    public ModuleForm createForm(Item item) {
        return (new RappresentazioneForm(this, item));
    }// end of method

    @Override
    public SearchManager createSearchManager() {
        return new RappresentazioneSearch();
    }// end of method

    @Override
    public TablePortal createTablePortal() {
        return new RappresentazioneTablePortal(this);
    }// end of method

    /**
     * Delete selected items button pressed
     */
    public void delete() {

        // prima controlla se ci sono prenotazioni collegate
        boolean cont = true;
        for (Object id : getTable().getSelectedIds()) {
            BaseEntity entity = getTable().getEntity((Long) id);
            List listaPren = CompanyQuery.getList(Prenotazione.class, Prenotazione_.rappresentazione, entity);
            if (listaPren.size() > 0) {
                Notification.show("Impossibile eliminare le rappresentazioni selezionate perché ci sono delle prenotazioni.\nEliminate prima le prenotazioni collegate o  assegnatele a un'altra rappresentazione.", Notification.Type.WARNING_MESSAGE);
                cont = false;
                break;
            }
        }

        // se tutto ok ritorna il controllo alla superclasse
        if (cont) {
            super.delete();
        }
    }// end of method


}// end of class
