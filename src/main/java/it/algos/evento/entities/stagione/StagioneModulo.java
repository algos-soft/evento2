package it.algos.evento.entities.stagione;

import com.vaadin.data.Item;
import com.vaadin.ui.Notification;
import it.algos.evento.entities.evento.Evento;
import it.algos.evento.entities.evento.Evento_;
import it.algos.webbase.multiazienda.CompanyEntity_;
import it.algos.webbase.multiazienda.CompanyModule;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.search.SearchManager;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.table.TablePortal;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import java.util.List;

/**
 * Created by Alex on 31/05/15.
 * .
 */
public class StagioneModulo extends CompanyModule {


    /**
     * Costruttore senza parametri
     */
    public StagioneModulo() {
        super(Stagione.class);
    }// end of constructor

    public TablePortal createTablePortal() {
        return new StagioneTablePortal(this);
    }

    @Override
    public ATable createTable() {
        return (new StagioneTable(this));
    }// end of method

    @Override
    public ModuleForm createForm(Item item) {
        return (new StagioneForm(this, item));
    }// end of method

    @Override
    public SearchManager createSearchManager() {
        return new StagioneSearch(this);
    }// end of method

    /**
     * Crea i campi visibili nella scheda (search)
     * <p>
     * Come default spazzola tutti i campi della Entity <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     * Serve anche per l'ordine con cui vengono presentati i campi nella scheda <br>
     */
    protected Attribute<?, ?>[] creaFieldsSearch() {
        return new Attribute[]{Stagione_.sigla, Stagione_.corrente};
    }// end of method



    /**
     * Imposta una stagione come corrente.
     * <p>
     * Invocato dai menu
     * Pone il flag corrente=false a tutte le stagioni
     * Assegna il flag corrente=true alla stagione desiderata
     */
    public static void cmdSetCorrente(final Stagione stagione, final ATable table) {

        CriteriaUpdate<Stagione> update;
        Root<Stagione> root;
        Predicate condition;

        // setup
        EntityManager manager = EM.createEntityManager();
        CriteriaBuilder cb = manager.getCriteriaBuilder();

        //fase 1) - poni tutti i flag a false

        // create criteria update
        update = cb.createCriteriaUpdate(Stagione.class);

        // set the root class
        root = update.from(Stagione.class);

        // set where clause
        condition = cb.equal(root.get(CompanyEntity_.company), CompanySessionLib.getCompany());
        update.where(condition);

        // what to update
        update.set(root.get(Stagione_.corrente), false);

        // perform update
        try {
            manager.getTransaction().begin();
            manager.createQuery(update).executeUpdate();
            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
        }

        //fase 2) - accendi il flag alla stagione richiesta
        stagione.setCorrente(true);
        stagione.save(manager);

        // alla fine: chiudi EntityManager
        manager.close();

        table.refresh();

    }


    /**
     * Delete selected items button pressed
     */
    public void delete() {

        // prima controlla se ci sono eventi collegati
        boolean cont=true;
        for (Object id : getTable().getSelectedIds()) {
            BaseEntity entity = getTable().getEntity((Long)id);
            List lista = CompanyQuery.getList(Evento.class, Evento_.stagione, entity);
            if (lista.size()>0) {
                Notification.show("Impossibile eliminare le stagioni selezionate perché ci sono degli eventi collegati.\nEliminate prima gli eventi.", Notification.Type.WARNING_MESSAGE);
                cont=false;
                break;
            }
        }

        // se tutto ok ritorna il controllo alla superclasse
        if (cont) {
            super.delete();
        }
    }// end of method


}
