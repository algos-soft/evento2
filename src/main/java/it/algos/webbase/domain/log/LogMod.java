package it.algos.webbase.domain.log;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.MenuBar;
import it.algos.webbase.web.form.AForm;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.navigator.NavPlaceholder;
import it.algos.webbase.web.table.TablePortal;

import javax.persistence.metamodel.Attribute;

@SuppressWarnings("serial")
public class LogMod extends ModulePop {



    /**
     * Costruttore senza parametri
     * <p>
     * Invoca la superclasse passando i parametri:
     * (obbligatorio) la Entity specifica
     * (facoltativo) etichetta del menu (se manca usa il nome della Entity)
     * (facoltativo) icona del menu (se manca usa un'icona standard)
     */
    public LogMod() {
        super(Log.class, "Log", FontAwesome.CLOCK_O);
    }// end of constructor


    /**
     * Titolo (caption) dei dialogo di modifica. <br>
     * Come default usa il titolo standard <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     */
    @Override
    protected String getCaptionEdit() {
        return "Visione log";
    }// end of method

    /**
     * Titolo (caption) dei dialogo di ricerca. <br>
     * Come default usa il titolo standard <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     */
    @Override
    protected String getCaptionSearch() {
        return "Ricerca log";
    }// end of method

    /**
     * Returns the form used to edit an item. <br>
     * The concrete subclass must override for a specific Form.
     *
     * @return the Form
     */
    @Override
    public ModuleForm createForm(Item item) {
        return (new LogForm(item, this));
    }// end of method

    /**
     * Create the Table Portal
     *
     * @return the TablePortal
     */
    @Override
    public TablePortal createTablePortal() {
        return new LogTablePortal(this);
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
        return new Attribute[]{Log_.livello, Log_.code, Log_.timestamp, Log_.descrizione};
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
        return new Attribute[]{Log_.code, Log_.descrizione, Log_.timestamp};
    }// end of method

    /**
     * Crea i campi visibili nella scheda (search)
     * <p>
     * Come default spazzola tutti i campi della Entity <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     * Serve anche per l'ordine con cui vengono presentati i campi nella scheda <br>
     */
    protected Attribute<?, ?>[] creaFieldsSearch() {
        return new Attribute[]{Log_.livello, Log_.code, Log_.descrizione};
    }// end of method



}// end of class
