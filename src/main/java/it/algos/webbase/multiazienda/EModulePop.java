package it.algos.webbase.multiazienda;

import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.search.SearchManager;
import it.algos.webbase.web.table.ATable;

@SuppressWarnings("serial")
public class EModulePop extends ModulePop {

	@SuppressWarnings("rawtypes")
	public EModulePop(Class entity) {
		super(entity);
	}
	
	/**
	 * Crea una Table già filtrata sulla company corrente
	 * <p>
	 * @return the Table
	 */
	public ATable createTable() {
		return (new ETable(this));
	}// end of method

	/**
	 * Create the Search Manager.
	 * Il SearchManager crea i campi pop di ricerca già filtrati
	 *
	 * @return the SearchManager
	 */
	public SearchManager createSearchManager() {
		SearchManager manager = new ESearchManager(this);
		return manager;
	}// end of method

}
