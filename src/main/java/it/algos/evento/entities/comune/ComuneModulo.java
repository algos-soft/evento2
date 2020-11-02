package it.algos.evento.entities.comune;

import com.vaadin.ui.Notification;
import it.algos.evento.entities.scuola.Scuola;
import it.algos.evento.entities.scuola.Scuola_;
import it.algos.webbase.multiazienda.CompanyModule;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.search.SearchManager;
import it.algos.webbase.web.table.ATable;

import javax.persistence.metamodel.Attribute;
import java.util.List;


@SuppressWarnings("serial")
public class ComuneModulo extends CompanyModule {

	/**
	 * Costruttore senza parametri
	 */
	public ComuneModulo() {
		super(Comune.class);
	}// end of constructor


	// come default usa il titolo standard
	// può essere sovrascritto nelle sottoclassi specifiche
	protected String getCaptionSearch() {
		return "comuni";
	}// end of method

	// come default spazzola tutti i campi della Entity
	// non garantisce l'ordine con cui vengono presentati i campi
	// può essere sovrascritto nelle sottoclassi specifiche (garantendo l'ordine)
	// può mostrare anche il campo ID, oppure no
	// se si vuole differenziare tra Table, Form e Search, sovrascrivere
	// creaFieldsList, creaFieldsForm e creaFieldsSearch
	protected Attribute<?, ?>[] creaFieldsAll() {
		return new Attribute[] { Comune_.nome, Comune_.siglaProvincia };
	}// end of method

	@Override
	public ATable createTable() {
		return (new ComuneTable(this));
	}// end of method


	@Override
	public SearchManager createSearchManager() {
		return new ComuneSearch(this);
	}// end of method

	
	/**
	 * Delete selected items button pressed
	 */
	public void delete() {
		
		// prima controlla se ci sono scuole collegate
		boolean cont=true;
		for (Object id : getTable().getSelectedIds()) {
			BaseEntity entity=getTable().getEntity((Long)id);
			List lista = CompanyQuery.getList(Scuola.class, Scuola_.comune, entity);
			if (lista.size()>0) {
				Notification.show("Impossibile eliminare i comuni selezionati perché ci sono delle scuole collegate.\nEliminate prima le scuole collegate o cambiate il comune nelle scuole.", Notification.Type.WARNING_MESSAGE);
				cont=false;
				break;
			}
		}

		// se tutto ok ritorna il controllo alla superclasse
		if (cont) {
			super.delete();
		}
	}// end of method

}// end of class
