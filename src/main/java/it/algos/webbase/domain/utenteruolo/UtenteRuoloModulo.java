package it.algos.webbase.domain.utenteruolo;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.MenuBar;
import it.algos.webbase.domain.utente.Utente;
import it.algos.webbase.domain.vers.Versione;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.navigator.NavPlaceholder;

import javax.persistence.metamodel.Attribute;

@SuppressWarnings("serial")
public class UtenteRuoloModulo extends ModulePop {



	/**
	 * Costruttore senza parametri
	 * <p>
	 * Invoca la superclasse passando i parametri:
	 * (obbligatorio) la Entity specifica
	 * (facoltativo) etichetta del menu (se manca usa il nome della Entity)
	 * (facoltativo) icona del menu (se manca usa un'icona standard)
	 */
	public UtenteRuoloModulo() {
		super(UtenteRuolo.class, "UteRuolo", FontAwesome.LOCK);
	}// end of constructor


	/**
	 * Titolo (caption) dei dialogo nuovo record. <br>
	 * Come default usa il titolo standard <br>
	 * Può essere sovrascritto nelle sottoclassi specifiche <br>
	 */
	@Override
	protected String getCaptionNew() {
		return "Nuova utente/ruolo";
	}// end of method

	/**
	 * Titolo (caption) dei dialogo di modifica. <br>
	 * Come default usa il titolo standard <br>
	 * Può essere sovrascritto nelle sottoclassi specifiche <br>
	 */
	@Override
	protected String getCaptionEdit() {
		return "Modifica utente/ruolo";
	}// end of method

	/**
	 * Titolo (caption) dei dialogo di ricerca. <br>
	 * Come default usa il titolo standard <br>
	 * Può essere sovrascritto nelle sottoclassi specifiche <br>
	 */
	@Override
	protected String getCaptionSearch() {
		return "Ricerca utenti/ruoli";
	}// end of method

	// come default spazzola tutti i campi della Entity
	// non garantisce l'ordine con cui vengono presentati i campi
	// può essere sovrascritto nelle sottoclassi specifiche (garantendo l'ordine)
	// può mostrare anche il campo ID, oppure no
	// se si vuole differenziare tra Table, Form e Search, sovrascrivere
	// creaFieldsList, creaFieldsForm e creaFieldsSearch
	protected Attribute<?, ?>[] creaFieldsAll() {
		return new Attribute[] { UtenteRuolo_.utente, UtenteRuolo_.ruolo };
	}// end of method


}// end of class
