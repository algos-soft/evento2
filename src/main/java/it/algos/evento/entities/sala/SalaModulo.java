package it.algos.evento.entities.sala;

import com.vaadin.ui.Notification;
import it.algos.evento.entities.rappresentazione.Rappresentazione;
import it.algos.evento.entities.rappresentazione.Rappresentazione_;
import it.algos.webbase.multiazienda.CompanyModule;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.web.entity.BaseEntity;

import javax.persistence.metamodel.Attribute;
import java.util.List;

@SuppressWarnings("serial")
public class SalaModulo extends CompanyModule {

	/**
	 * Costruttore senza parametri
	 */
	public SalaModulo() {
		super(Sala.class);
	}// end of constructor

	// come default usa il titolo standard
	// può essere sovrascritto nelle sottoclassi specifiche
	protected String getCaptionSearch() {
		return "sale";
	}// end of method

	// come default spazzola tutti i campi della Entity
	// non garantisce l'ordine con cui vengono presentati i campi
	// può essere sovrascritto nelle sottoclassi specifiche (garantendo l'ordine)
	// può mostrare anche il campo ID, oppure no
	// se si vuole differenziare tra Table, Form e Search, sovrascrivere
	// creaFieldsList, creaFieldsForm e creaFieldsSearch
	protected Attribute<?, ?>[] creaFieldsAll() {
		return new Attribute[] { Sala_.nome, Sala_.capienza };
	}// end of method

	/**
	 * Delete selected items button pressed
	 */
	public void delete() {
		
		// prima controlla se ci sono rappresentazioni collegate
		boolean cont=true;
		for (Object id : getTable().getSelectedIds()) {
			BaseEntity entity = getTable().getEntity((Long)id);
			List lista = CompanyQuery.getList(Rappresentazione.class, Rappresentazione_.sala, entity);
			if (lista.size()>0) {
				Notification.show("Impossibile eliminare le sale selezionate perché ci sono delle rappresentazioni collegate.\nEliminate prima le rappresentazioni collegate o cambiate sala.", Notification.Type.WARNING_MESSAGE);
				cont=false;
				break;
			}
		}

		// se tutto ok ritorna il controllo alla superclasse
		if (cont) {
			super.delete();
		}
	}// end of method

	@Override
	public void search() {
		super.search();
	}
}// end of class
