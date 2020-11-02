package it.algos.webbase.multiazienda;

import com.vaadin.server.Resource;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.search.SearchManager;
import it.algos.webbase.web.table.ATable;

/**
 * Module automatically filtered on the current Company
 * Created by alex on 8-03-2016.
 */
public class CompanyModule extends ModulePop {

    @SuppressWarnings("rawtypes")
    public CompanyModule(Class entity) {
        super(entity);
    }

    public CompanyModule(Class entity, String menuLabel) {
        super(entity, menuLabel);
    }

    public CompanyModule(Class entity, Resource menuIcon) {
        super(entity, menuIcon);
    }

    public CompanyModule(Class entity, String menuLabel, Resource menuIcon) {
        super(entity, menuLabel, menuIcon);
    }

    /**
     * Returns the table used to shows the list of items. <br>
     * The concrete subclass must override for a specific Table.
     * La table è già filtrata sulla company corrente
     *
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
