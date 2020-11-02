package it.algos.evento.entities.prenotazione.eventi;

import com.vaadin.data.Item;
import it.algos.webbase.multiazienda.CompanyModule;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.search.SearchManager;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.table.TablePortal;

import javax.persistence.metamodel.Attribute;

@SuppressWarnings("serial")
public class EventoPrenModulo extends CompanyModule {

	/**
	 * Costruttore senza parametri
	 */
	public EventoPrenModulo() {
		super(EventoPren.class);
	}// end of constructor


	@SuppressWarnings("rawtypes")
	protected Attribute[] creaFieldsForm() {
		return new Attribute[] { EventoPren_.timestamp, EventoPren_.tipo, EventoPren_.user, EventoPren_.dettagli };
	}// end of method

	/**
	 * Create the Table Portal
	 * 
	 * @return the TablePortal
	 */
	public TablePortal createTablePortal() {
		return new EventoPrenTablePortal(this);
	}

	@Override
	public ATable createTable() {
		return (new EventoPrenTable(this));
	}// end of method

	@Override
	public ModuleForm createForm(Item item) {
		return (null);
	}// end of method

	@Override
	public SearchManager createSearchManager() {
		return new EventoPrenSearch();
	}// end of method

}
