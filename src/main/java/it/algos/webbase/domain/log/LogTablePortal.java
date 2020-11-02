package it.algos.webbase.domain.log;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.MenuBar;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.TableToolbar;

import java.util.HashMap;

/**
 * Created by gac on 23 set 2015.
 * <p>
 * Sovrascrive la classe standard per aggiungere un bottone/menu di filtro sul parametro Livello
 */
public class LogTablePortal extends TablePortal {

    private final static String MENU_LIVELLO_CAPTION = "Livello";
    private TableToolbar toolbar;
    private HashMap<Livello, MenuBar.MenuItem> livelli;

    public LogTablePortal(ModulePop modulo) {
        super(modulo);
    }// end of constructor

    public TableToolbar createToolbar() {
        toolbar = super.createToolbar();
        toolbar.setCreate(false);

        addMenuLivelli();
        setFiltro(Livello.info);
        return toolbar;
    }// end of method


    /**
     * Livello selection.
     * <p>
     * Costruisce un menu per selezionare il livello di dettaglio dei records da filtrare
     * Costruisce i menuItem in funzione dei valori della enumeration Livello
     * L'ordine di presentazione è identico all'ordine in cui è stata costruita la enumeration (NON CAMBIARE ORDINE)
     */
    private void addMenuLivelli() {
        MenuBar.MenuItem item = null;
        MenuBar.MenuItem subItem;
        livelli = new HashMap<Livello, MenuBar.MenuItem>();

        item = toolbar.addButton(MENU_LIVELLO_CAPTION, FontAwesome.NAVICON, null);

        for (Livello livello : Livello.values()) {
            subItem = item.addItem(livello.toString(), null, new MenuBar.Command() {
                public void menuSelected(MenuBar.MenuItem selectedItem) {
                    setFiltro(livello);
                }// end of inner method
            });// end of anonymous inner class
            livelli.put(livello, subItem);
        }// end of for cycle

    }// end of method


    /**
     * Shows in the table only the needed level
     * Creates a filter corresponding to the needed level in the table
     * I filtri sono comprensivi del livello sottostante (GreaterOrEqual)
     */
    private void setFiltro(Livello livello) {
        Container.Filter filter = null;
        ATable table = null;
        Container.Filterable cont=null;

        if (livello != null) {
            table = this.getTable();
        }// fine del blocco if

        if (table != null) {
            cont = table.getFilterableContainer();
        }// fine del blocco if

        if (cont != null) {
            filter = new Compare.GreaterOrEqual(Log_.livello.getName(), livello);
        }// fine del blocco if

        if (filter != null) {
            cont.removeAllContainerFilters();
            cont.addContainerFilter(filter);
            table.refresh();
        }// fine del blocco if

        this.spuntaMenu(livello);
    }// end of method

    /**
     * Spunta il menu selezionato
     * Elimina la spunta in tutti gli altri
     */
    private void spuntaMenu(Livello livelloSelezionato) {
        MenuBar.MenuItem subItem;

        if (livelli.containsKey(livelloSelezionato)) {
            subItem = livelli.get(livelloSelezionato);
            if (subItem != null) {
                for (Livello livello : livelli.keySet()) {
                    livelli.get(livello).setIcon(FontAwesome.MINUS);
                }// end of for cycle

                subItem.setIcon(FontAwesome.CHECK);
            }// fine del blocco if
        }// fine del blocco if

    }// end of method

}// end of class

