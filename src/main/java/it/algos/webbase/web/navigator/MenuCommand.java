package it.algos.webbase.web.navigator;

import com.vaadin.navigator.View;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import it.algos.webbase.web.module.ModulePop;

import java.util.List;

/**
 * Custom menu command to navigate to a specific state
 */
public class MenuCommand implements MenuBar.Command {

    private MenuBar mb;
    private View view;
    private Class clazz;
    private boolean cached;

    /**
     * Constructor - lazy
     * Will create a lazy (class-based) view provider
     * The view will be instantiated by the view provider from the provided class
     * The viewCached parameter controls if the view will be instantiated only once
     * or each time is requested bu yhe Navigator.
     *
     * @param mb     the MenuBar
     * @param clazz  the class to instantiate (must implement View)
     * @param cached true to instantiated only once, false to instantiate each time
     */
    public MenuCommand(MenuBar mb, Class clazz, boolean cached) {
        this.mb = mb;
        this.clazz = clazz;
        this.cached = cached;
    }

    /**
     * Constructor - lazy, cached
     */
    public MenuCommand(MenuBar mb, Class clazz) {
        this(mb, clazz, true);
    }// end of constructor

    /**
     * Constructor - heavyweight
     * Will create a heavyweight view provider
     * The view provided here will be used
     *
     * @param mb      the MenuBar
     * @param view    the view to diplay
     */
    public MenuCommand(MenuBar mb, View view) {
        this.mb = mb;
        this.view = view;
    }


    /**
     * The item has been selected.
     * Navigate to the View and select the item in the menubar
     */
    public void menuSelected(MenuBar.MenuItem selectedItem) {
        // Navigate to a specific state
        // ui.getNavigator().navigateTo(address);

        String address = getNavigatorAddress();
        UI.getCurrent().getNavigator().navigateTo(address);

        // de-selects all the items in the menubar
        if (mb != null) {
            List<MenuBar.MenuItem> items = mb.getItems();
            for (MenuBar.MenuItem item : items) {
                deselectItem(item);
            } // fine del ciclo for
        }// fine del blocco if

        // highlights the selected item
        // the style name will be prepended automatically with "v-menubar-menuitem-"
        selectedItem.setStyleName("highlight");

        // it this item is inside another item, highlight also the parents in the chain
        MenuBar.MenuItem item = selectedItem;
        while (item.getParent() != null) {
            item = item.getParent();
            item.setStyleName("highlight");
        } // fine del ciclo while

    }// end of method


    /**
     * @return the string used as address by the Navigator
     */
    public String getNavigatorAddress() {
        String addr;
        if (view!=null) {
            addr=view.getClass().getName();
        } else {
            addr=clazz.getName();
        }
        return addr;
    }

    /**
     * Recursively de-selects one item and all its children
     */
    private void deselectItem(MenuBar.MenuItem item) {
        item.setStyleName(null);
        List<MenuBar.MenuItem> items = item.getChildren();
        if (items != null) {
            for (MenuBar.MenuItem child : items) {
                deselectItem(child);
            } // fine del ciclo for
        }// fine del blocco if
    }// end of method

    public Class getClazz() {
        return clazz;
    }

    public boolean isCached() {
        return cached;
    }

    public View getView() {
        return view;
    }
}// end of class
