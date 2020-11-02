package it.algos.evento.entities.evento;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Notification;
import it.algos.webbase.multiazienda.CompanyModule;
import it.algos.evento.entities.rappresentazione.Rappresentazione;
import it.algos.evento.entities.rappresentazione.Rappresentazione_;
import it.algos.evento.entities.stagione.Stagione;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.search.SearchManager;
import it.algos.webbase.web.table.ATable;

import javax.persistence.metamodel.Attribute;
import java.util.List;

@SuppressWarnings("serial")
public class EventoModulo extends CompanyModule {

	/**
	 * Costruttore senza parametri
	 */
	public EventoModulo() {
		super(Evento.class);
	}// end of constructor


	// come default usa il titolo standard
	// può essere sovrascritto nelle sottoclassi specifiche
	protected String getCaptionSearch() {
		return "eventi";
	}// end of method


	// come default spazzola tutti i campi della Entity
	// può essere sovrascritto nelle sottoclassi specifiche
	// serve anche per l'ordine con cui vengono presentati i campi
	protected Attribute<?, ?>[] creaFieldsForm() {
		return new Attribute[] { Evento_.sigla, Evento_.titolo, Evento_.progetto, Evento_.importoIntero,
				Evento_.importoRidotto };
	}// end of method

	// come default spazzola tutti i campi della Entity
	// può essere sovrascritto nelle sottoclassi specifiche
	// serve anche per l'ordine con cui vengono presentati i campi
	protected Attribute<?, ?>[] creaFieldsSearch() {
		return new Attribute[] { Evento_.sigla, Evento_.titolo, Evento_.progetto, Evento_.stagione };
	}// end of method


	@Override
	public ATable createTable() {
		return (new EventoTable(this));
	}// end of method

	@Override
	public ModuleForm createForm(Item item) {
		return (new EventoForm(this, item));
	}// end of method

	public SearchManager createSearchManager() {
		return new EventoSearch();
	}// end of method

	/**
	 * Delete selected items button pressed
	 */
	public void delete() {
		
		// prima controlla se ci sono rappresentazioni collegate
		boolean cont=true;
		for (Object id : getTable().getSelectedIds()) {
			BaseEntity entity = getTable().getEntity((Long)id);
			List lista = CompanyQuery.getList(Rappresentazione.class, Rappresentazione_.evento, entity);
			if (lista.size()>0) {
				Notification.show("Impossibile eliminare gli eventi selezionati perché ci sono delle rappresentazioni.\nEliminate prima le rappresentazioni collegate.", Notification.Type.WARNING_MESSAGE);
				cont=false;
				break;
			}
		}

		// se tutto ok ritorna il controllo alla superclasse
		if (cont) {
			super.delete();
		}
	}// end of method

	/**
	 * Post create / pre edit item.
	 * Assegna la stagione corrente ai nuovi record
	 */
	protected void postCreate(Item item) {
		Property prop = item.getItemProperty(Evento_.stagione.getName());
		if (prop!=null){
			prop.setValue(Stagione.getStagioneCorrente());
		}
	}// end of method


}// end of class
