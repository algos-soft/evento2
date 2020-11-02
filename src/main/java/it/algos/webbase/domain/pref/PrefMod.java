package it.algos.webbase.domain.pref;
/**
 * Created by Gac on 17 lug 2015.
 * Using specific Templates (Entity, Domain, Modulo)
 */


import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import it.algos.webbase.multiazienda.CompanyEntity_;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.ATable;

import javax.persistence.metamodel.Attribute;

@SuppressWarnings("serial")
public class PrefMod extends ModulePop {


    /**
     * Costruttore senza parametri
     * <p>
     * Invoca la superclasse passando i parametri:
     * (obbligatorio) la Entity specifica
     * (facoltativo) etichetta del menu (se manca usa il nome della Entity)
     * (facoltativo) icona del menu (se manca usa un'icona standard)
     */
    public PrefMod() {
        super(Pref.class, FontAwesome.WRENCH);
    }// end of constructor


    /**
     * Titolo (caption) dei dialogo nuovo record. <br>
     * Come default usa il titolo standard <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     */
    @Override
    protected String getCaptionNew() {
        return "Nuova preferenza";
    }// end of method

    /**
     * Titolo (caption) dei dialogo di modifica. <br>
     * Come default usa il titolo standard <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     */
    @Override
    protected String getCaptionEdit() {
        return "Modifica preferenza";
    }// end of method

    /**
     * Titolo (caption) dei dialogo di ricerca. <br>
     * Come default usa il titolo standard <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     */
    @Override
    protected String getCaptionSearch() {
        return "Ricerca preferenza";
    }// end of method

    /**
     * Crea i campi visibili nella lista (table)
     * <p>
     * Come default spazzola tutti i campi della Entity <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     * Serve anche per l'ordine con cui vengono presentati i campi nella lista <br>
     */
    @Override
    protected Attribute<?, ?>[] creaFieldsList() {
        return new Attribute[]{
                CompanyEntity_.company,
                Pref_.codeCompanyUnico,
                Pref_.ordine,
                Pref_.code,
                Pref_.descrizione,
                Pref_.tipo,
                Pref_.value};
    }// end of method

    /**
     * Crea i campi visibili nella scheda (form)
     * <p>
     * Come default spazzola tutti i campi della Entity <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     * Serve anche per l'ordine con cui vengono presentati i campi nella scheda <br>
     */
    @Override
    protected Attribute<?, ?>[] creaFieldsForm() {
        return new Attribute[]{Pref_.ordine, Pref_.code, Pref_.descrizione, Pref_.tipo,};
    }// end of method

    /**
     * Returns the form used to edit an item. <br>
     * The concrete subclass must override for a specific Form.
     *
     * @return the Form
     */
    @Override
    public ModuleForm createForm(Item item) {
        return new PrefForm(item, this);
    }// end of method

    /**
     * Returns the table used to shows the list. <br>
     * The concrete subclass must override for a specific Table.
     *
     * @return the Table
     */
    @Override
    public ATable createTable() {
        return new PrefTable(this);
    }// end of method

    /**
     * Post create / pre edit item.
     * Legge il massimo numero esistente nel DB per la property Numero
     * Ordina discendente per usare il primo record
     * Nel dialogo nuovo record, suggerisce il valore incrementato di 1
     * Riordina ascendente (default)
     */
    protected void postCreate(Item item) {
        String sortField;
        Property property;
        Container.Sortable cont;
        Object idKey;
        int progressivo;

        if (item == null) {
            return;
        }// fine del blocco if

        cont = getTable().getSortableContainer();
        sortField = Pref_.ordine.getName();
        property = item.getItemProperty(sortField);

        if (property != null) {
            cont.sort(new String[]{sortField}, new boolean[]{false});
            Container container = getTable().getContainerDataSource();
            idKey = container.getItem(0);

//            idKey = cont.getIdByIndex(0);

            if (idKey != null && idKey instanceof Long) {
                Pref pref = Pref.find((long) idKey);
                if (pref != null) {
                    progressivo = pref.getOrdine();
                    progressivo = progressivo + 1;
                    property.setValue(progressivo);
                }// fine del blocco if
            }// fine del blocco if
            cont.sort(new String[]{sortField}, new boolean[]{true});
        }// fine del blocco if

    }// end of method

//    /**
//     * Create the MenuBar Item for this module
//     * <p>
//     * Invocato dal metodo AlgosUI.creaMenu()
//     * PUO essere sovrascritto dalla sottoclasse
//     *
//     * @param menuBar     a cui agganciare il menuitem
//     * @param placeholder in cui visualizzare il modulo
//     * @return menuItem appena creato
//     */
//    @Override
//    public MenuBar.MenuItem createMenuItem(MenuBar menuBar, NavPlaceholder placeholder) {
//        return super.createMenuItem(menuBar, placeholder, FontAwesome.BARS);
//    }// end of method

}// end of class
