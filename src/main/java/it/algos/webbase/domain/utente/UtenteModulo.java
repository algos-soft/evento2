package it.algos.webbase.domain.utente;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import it.algos.webbase.web.form.AForm;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.module.ModulePop;

import javax.persistence.metamodel.Attribute;

@SuppressWarnings("serial")
public class UtenteModulo extends ModulePop {


    /**
     * Costruttore senza parametri
     * <p>
     * Invoca la superclasse passando i parametri:
     * (obbligatorio) la Entity specifica
     * (facoltativo) etichetta del menu (se manca usa il nome della Entity)
     * (facoltativo) icona del menu (se manca usa un'icona standard)
     */
    public UtenteModulo() {
        super(Utente.class, FontAwesome.KEY);
    }// end of basic constructor

    /**
     * Costruttore
     */
    public UtenteModulo(String menuLabel) {
        super(Utente.class, menuLabel, FontAwesome.KEY);
    }// end of basic constructor


    /**
     * Titolo (caption) dei dialogo nuovo record. <br>
     * Come default usa il titolo standard <br>
     * Può essere sovrascritto nelle sottoclassi specifiche <br>
     */
    @Override
    protected String getCaptionNew() {
        return "Nuovo utente";
    }// end of method

    /**
     * Titolo (caption) dei dialogo di modifica. <br>
     * Come default usa il titolo standard <br>
     * Può essere sovrascritto nelle sottoclassi specifiche <br>
     */
    @Override
    protected String getCaptionEdit() {
        return "Modifica utente";
    }// end of method

    /**
     * Titolo (caption) dei dialogo di ricerca. <br>
     * Come default usa il titolo standard <br>
     * Può essere sovrascritto nelle sottoclassi specifiche <br>
     */
    @Override
    protected String getCaptionSearch() {
        return "Ricerca utenti";
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
        return new Attribute[]{Utente_.nickname, Utente_.enabled};
    }// end of method

    /**
     * Returns the form used to edit an item. <br>
     * The concrete subclass must override for a specific Form.
     *
     * @param item singola istanza della classe
     * @return the Form
     */
    @Override
    public ModuleForm createForm(Item item) {
        return new UtenteForm(item, this);
    }// end of method


}// end of class
