package it.algos.evento.entities.rappresentazione;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.event.Action;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import it.algos.evento.entities.evento.Evento;
import it.algos.evento.entities.evento.Evento_;
import it.algos.evento.entities.prenotazione.Prenotazione;
import it.algos.evento.entities.stagione.Stagione;
import it.algos.webbase.multiazienda.ETable;
import it.algos.webbase.web.dialog.AlertDialog;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.entity.BaseEntity_;
import it.algos.webbase.web.module.ModulePop;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@SuppressWarnings("serial")
public class RappresentazioneTable extends ETable {

    private static final String PROP_EVENTO_STAGIONE = Evento.class.getSimpleName().toLowerCase() + "." + Evento_.stagione.getName();

    private static final String colPostiPren = "pren";
    private static final String colPostiDisp = "disp";


    public RappresentazioneTable(ModulePop modulo) {
        super(modulo);

        setColumnHeader(Rappresentazione_.dataRappresentazione, "data e ora");

        // sort by date
        Object[] properties = {Rappresentazione_.dataRappresentazione.getName()};
        boolean[] ordering = {true};
        sort(properties, ordering);

        // aggiungi un listener di creazione record alla classe Prenotazione
        // rinfresca il container che mostra il n. di posti disponibili
        final BaseEntity.PostPersistListener ppl = Prenotazione.addPostPersistListener(new BaseEntity.PostPersistListener() {

            @Override
            public void postPersist(Class<?> entityClass, long id) {
                if (entityClass.equals(Prenotazione.class)) {
                    refreshRowCache();
                }
            }
        });

        // aggiungi un listener alla modifica di prenotazioni
        // per rinfrescare il container che mostra il n. di posti disponibili
        final BaseEntity.PostUpdateListener pul = Prenotazione.addPostUpdateListener(new BaseEntity.PostUpdateListener() {

            @Override
            public void postUpdate(Class<?> entityClass, long id) {
                if (entityClass.equals(Prenotazione.class)) {
                    refreshRowCache();
                }
            }
        });

        // Al detach rimuove i listeners che ha attaccato alla entity.
        // Essendo la Entity statica, se non rimuovo i listeners questi
        // tengono impegnata la classe e non disponibile al GC
        addDetachListener(new DetachListener() {
            @Override
            public void detach(DetachEvent detachEvent) {
                if (ppl != null) {
                    Prenotazione.removePostPersistListener(ppl);
                }
                if (pul != null) {
                    Prenotazione.removePostUpdateListener(pul);
                }
            }
        });


        setColumnAlignment(Rappresentazione_.capienza.getName(), Align.RIGHT);
        setColumnAlignment(colPostiPren, Align.RIGHT);
        setColumnAlignment(colPostiDisp, Align.RIGHT);

        setColumnUseTotals(Rappresentazione_.capienza, true);
        setColumnUseTotals(colPostiPren, true);
        setColumnUseTotals(colPostiDisp, true);


        // comandi contestuali aggiuntivi
        addActionHandler(new Action.Handler() {

            private final Action actExportPren = new Action(Rappresentazione.CMD_PRENOTAZIONI_EXPORT,
                    Rappresentazione.ICON_MEMO_EXPORT);
            private final Action actExportPart = new Action(Rappresentazione.CMD_PARTECIPANTI_EXPORT,
                    Rappresentazione.ICON_MEMO_EXPORT);


            public Action[] getActions(Object target, Object sender) {
                Action[] actions = null;
                actions = new Action[2];
                actions[0] = actExportPren;
                actions[1] = actExportPart;
                return actions;
            }

            public void handleAction(Action action, Object sender, Object target) {

                BaseEntity[] entities = getSelectedEntities();
                if(entities.length>0) {
                    Rappresentazione[] rapps = Arrays.copyOf(entities, entities.length, Rappresentazione[].class);

                    if(action.equals(actExportPren)){
                        RappresentazioneModulo.esportaPrenotazioni(rapps);
                    }

                    if(action.equals(actExportPart)){
                        RappresentazioneModulo.esportaPartecipanti(rapps);
                    }

                }else{
                    Notification.show("Devi selezionare le rappresentazioni da esportare");
                }

            }
        });



    }// end of constructor





    /**
     * Creates the container
     * <p>
     * @return un container filtrato sulla azienda corrente
     */
    @SuppressWarnings("unchecked")
    @Override
    public Container createContainer() {
        // aggiunge un filtro sulla stagione corrente
        Container cont = super.createContainer();
        Filter filter = new Compare.Equal(PROP_EVENTO_STAGIONE, Stagione.getStagioneCorrente());
        if(cont instanceof Filterable){
            Filterable fcont=(Filterable)cont;
            fcont.addContainerFilter(filter);
        }
        return cont;
    }


    @Override
    protected void createAdditionalColumns() {
        addGeneratedColumn(colPostiPren, new PostiPrenColumnGenerator());
        addGeneratedColumn(colPostiDisp, new PostiDispColumnGenerator());
    }

    @Override
    protected Object[] getDisplayColumns() {
        return new Object[]{Rappresentazione_.dataRappresentazione, Rappresentazione_.evento, Rappresentazione_.sala,
                Rappresentazione_.capienza, colPostiPren, colPostiDisp};
    }


    @Override
    protected String formatPropertyValue(Object rowId, Object colId, Property property) {

        if (colId.equals(Rappresentazione_.dataRappresentazione.getName())) {
            return Rappresentazione.getDateAsString((Date) property.getValue());
        }

        return super.formatPropertyValue(rowId, colId, property);
    }// end of method


    @Override
    protected BigDecimal getTotalForColumn(Object propertyId) {
        BigDecimal bd = null;

        if (propertyId.equals(colPostiPren)) {
            bd = new BigDecimal(0);
            Container cont = getContainerDataSource();
            Collection itemIds = cont.getItemIds();
            EntityManager em = getModule().getEntityManager();
            for (Object itemId : itemIds) {
                Property prop = cont.getContainerProperty(itemId, BaseEntity_.id.getName());
                long idRapp = (long)prop.getValue();
                Rappresentazione rapp=em.find(Rappresentazione.class, idRapp);
                int quanti = RappresentazioneModulo.countPostiPrenotati(rapp, em);
                bd=bd.add(new BigDecimal(quanti));
            }
        }

        if (propertyId.equals(colPostiDisp)) {
            bd = new BigDecimal(0);
            Container cont = getContainerDataSource();
            Collection itemIds = cont.getItemIds();
            EntityManager em = getModule().getEntityManager();
            for (Object itemId : itemIds) {
                Property prop = cont.getContainerProperty(itemId, BaseEntity_.id.getName());
                long idRapp = (long)prop.getValue();
                Rappresentazione rapp=em.find(Rappresentazione.class, idRapp);
                int quanti = RappresentazioneModulo.countPostiDisponibili(rapp, em);
                bd=bd.add(new BigDecimal(quanti));
            }
        }

        if (bd == null) {
            bd = super.getTotalForColumn(propertyId);
        }

        return bd;
    }


    /**
     * Genera la colonna dei posti prenotati.
     */
    class PostiPrenColumnGenerator implements ColumnGenerator {

        public Component generateCell(Table source, Object itemId, Object columnId) {

            return generateCellPosti(source, itemId, true);
        }
    }

    /**
     * Genera la colonna dei posti disponibili.
     */
    class PostiDispColumnGenerator implements ColumnGenerator {

        public Component generateCell(Table source, Object itemId, Object columnId) {

            return generateCellPosti(source, itemId, false);

        }
    }

    /**
     * Genera la cella dei posti prenotati o disponibili.
     * <p>
     *
     * @param source la table
     * @param itemId l'item id
     * @param pren   true per prenotati, false per disponibili
     */
    @SuppressWarnings("unchecked")
    private Component generateCellPosti(Table source, Object itemId, boolean pren) {
        int posti = 0;

        Rappresentazione rapp = (Rappresentazione) getEntity(itemId);

        Label label = new Label();
        if (pren) {
            posti = getRappresentazioneModulo().getPostiPrenotati(rapp);
            label.setValue("" + posti);
        } else {
            posti = getRappresentazioneModulo().getPostiDisponibili(rapp);
            if (posti < 0) {
                label.addStyleName("redbold");
            }
            label.setValue("" + posti);
        }
        label.setSizeUndefined(); // se non metto questo, non allinea a destra la label
        return label;
    }

    private RappresentazioneModulo getRappresentazioneModulo() {
        return (RappresentazioneModulo) getModule();
    }

}// end of class

