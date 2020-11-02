package it.algos.webbase.web.toolbar;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.VerticalLayout;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.table.TablePortal;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("serial")
public class TableToolbar extends Toolbar implements ATable.SelectionChangedListener {

    public static final String CMD_NEW = "Nuovo";
    public static final String CMD_EDIT = "Modifica";
    public static final String CMD_DELETE = "Elimina";
    public static final String CMD_SEARCH = "Ricerca";
    public static final String CMD_SELECTION = "Selezione";
    public static final String CMD_TABLE_OPTIONS = "Opzioni";

    private TablePortal tablePortal;
    protected HashMap<Bottoni, MenuItem> menuitems = new HashMap<>();
    private ArrayList<TableToolbarListener> listeners = new ArrayList<TableToolbarListener>();
    private InfoPanel infoPanel = new InfoPanel();

    // main buttons in the toolbar
    private MenuBar.MenuItem itemCreate;
    private MenuBar.MenuItem itemEdit;
    private MenuBar.MenuItem itemDelete;
    private MenuBar.MenuItem itemSearch;
    private MenuBar.MenuItem itemSelect;
    private MenuBar.MenuItem itemOptions;

    public TableToolbar(TablePortal tablePortal) {
        super();

        this.tablePortal=tablePortal;

        addCreate();
        addEdit();
        addDelete();
        addSearch();
        addSelect();
        addTableOptions();

        // default button visibility
        setCreateButtonVisible(true);
        setEditButtonVisible(true);
        setDeleteButtonVisible(true);
        setSearchButtonVisible(true);
        setSelectButtonVisible(true);
        setOptionsButtonVisible(false);

        addHelperComponent(infoPanel);

        // initial sync call (no rows selected)
        syncButtons(false, false);

    }


    public void setCreateButtonVisible(boolean visible){
        setButtonVisible(itemCreate, visible);
    }

    public void setEditButtonVisible(boolean visible){
        setButtonVisible(itemEdit, visible);
    }

    public void setDeleteButtonVisible(boolean visible){
        setButtonVisible(itemDelete, visible);
    }

    public void setSearchButtonVisible(boolean visible){
        setButtonVisible(itemSearch, visible);
    }

    public void setSelectButtonVisible(boolean visible){
        setButtonVisible(itemSelect, visible);
    }

    public void setOptionsButtonVisible(boolean visible){
        setButtonVisible(itemOptions, visible);
    }



    private void setButtonVisible(MenuBar.MenuItem item, boolean visible){
        if(item!=null){
            Component comp = getComp(item);
            if(comp!=null){
                comp.setVisible(visible);
            }
        }
    }


    /**
     * Bottone new.
     * Usa il Font Awesome; deprecato l'uso dell'icona del Theme corrente
     */
    protected void addCreate() {
        itemCreate = addButton(CMD_NEW, FontAwesome.PLUS, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                fire(Bottoni.create);
            }
        });
        menuitems.put(Bottoni.create, itemCreate);
    }

    /**
     * Bottone edit.
     * Usa il Font Awesome; deprecato l'uso dell'icona del Theme corrente
     */
    protected void addEdit() {
        itemEdit = addButton(CMD_EDIT, FontAwesome.PENCIL, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                fire(Bottoni.edit);
            }
        });
        menuitems.put(Bottoni.edit, itemEdit);
    }

    /**
     * Bottone delete.
     * Usa il Font Awesome; deprecato l'uso dell'icona del Theme corrente
     */
    protected void addDelete() {
        itemDelete = addButton(CMD_DELETE, FontAwesome.TRASH_O, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                fire(Bottoni.delete);
            }
        });
        menuitems.put(Bottoni.delete, itemDelete);
    }

    /**
     * Bottone search.
     * Usa il Font Awesome; deprecato l'uso dell'icona del Theme corrente
     */
    protected void addSearch() {
        itemSearch = addButton(CMD_SEARCH, FontAwesome.SEARCH, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                fire(Bottoni.search);
            }
        });
        menuitems.put(Bottoni.search, itemSearch);
    }

    /**
     * Bottone selection.
     * Usa il Font Awesome; deprecato l'uso dell'icona del Theme corrente
     */
    protected void addSelect() {
        MenuBar.MenuItem item;

        itemSelect = addButton(CMD_SELECTION, FontAwesome.LIST_UL, null);

        item = itemSelect.addItem("Solo selezionati", FontAwesome.FILE_TEXT, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                fire(Bottoni.selectedonly);
            }
        });
        menuitems.put(Bottoni.selectedonly, item);

        item = itemSelect.addItem("Rimuovi selezionati", FontAwesome.FILE_TEXT_O, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                fire(Bottoni.removeselected);
            }
        });
        menuitems.put(Bottoni.removeselected, item);

        item = itemSelect.addItem("Mostra tutti", FontAwesome.FILE, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                fire(Bottoni.showall);
            }
        });
        menuitems.put(Bottoni.showall, item);

        item = itemSelect.addItem("Deseleziona tutti", FontAwesome.FILE_O, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                fire(Bottoni.deselectall);
            }
        });
        menuitems.put(Bottoni.deselectall, item);

    }


    /**
     * Table options button.
     */
    protected void addTableOptions() {
        MenuBar.MenuItem item;

        itemOptions = addButton(CMD_TABLE_OPTIONS, FontAwesome.COG, null);

        item = itemOptions.addItem("Ricorda colonne visibili", FontAwesome.COLUMNS, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                fire(Bottoni.remembercollapsed);
            }
        });
        item.setCheckable(true);
        menuitems.put(Bottoni.remembercollapsed, item);

        item = itemOptions.addItem("Ricorda larghezza colonne", FontAwesome.ARROWS_H, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                fire(Bottoni.rememberwidth);
            }
        });
        item.setCheckable(true);
        menuitems.put(Bottoni.rememberwidth, item);

    }


    public void addToolbarListener(TableToolbarListener listener) {
        this.listeners.add(listener);
    }// end of method

    private void fire(Bottoni bottone) {
        for (TableToolbarListener l : listeners) {
            switch (bottone) {
                case create:
                    l.create_();
                    break;
                case edit:
                    l.edit_();
                    break;
                case delete:
                    l.delete_();
                    break;
                case search:
                    l.search_();
                    break;
                case selectedonly:
                    l.selectedonly_();
                    break;
                case removeselected:
                    l.removeselected_();
                    break;
                case showall:
                    l.showall_();
                    break;
                case deselectall:
                    l.deselectall_();
                    break;
                case remembercollapsed:
                    l.remembercollapsed_();
                    break;
                case rememberwidth:
                    l.rememberwidth_();
                    break;



                default:
                    break;
            }
        }

    }

    public void fireEdit() {
        fire(Bottoni.edit);
    }

    public String getInfoText() {
        return infoPanel.getInfoText();
    }

    public void setInfoText(String text) {
        infoPanel.setInfoText(text);
    }

    /**
     * Cambiata la selezione delle righe.
     * Possibilità di modificare l'aspetto (e la funzionalità) dei menuitems, eventualmente disabilitandoli
     * <p>
     * Nuovo: sempre acceso
     * Modifica: acceso se è selezionata una ed una sola riga
     * Elimina: acceso se è selezionata una riga o più di una riga
     * Ricerca: sempre acceso
     * <p>
     * Solo selezionati: acceso se è selezionata una riga o più di una riga
     * Rimuovi selezionati: acceso se è selezionata una riga o più di una riga
     * Mostra tutti: sempre acceso
     * Deseleziona tutti: acceso se è selezionata una riga o più di una riga
     */
    @Override
    public void selectionChanged(ATable.SelectionChangeEvent e) {
        syncButtons(e.isSingleRowSelected(), e.isMultipleRowsSelected());
    }

    /**
     * Sync the buttons state
     * <p>
     *
     * @param singleSelected if a single row is selected in the table
     * @param multiSelected  if multiple rows (1+) are selected in the table
     */
    public void syncButtons(boolean singleSelected, boolean multiSelected) {
        MenuItem item;

        item=menuitems.get(Bottoni.edit);
        if (item != null) {
            item.setEnabled(singleSelected);
        }

        item=menuitems.get(Bottoni.delete);
        if (item != null) {
            item.setEnabled(multiSelected);
        }

        item=menuitems.get(Bottoni.selectedonly);
        if (item != null) {
            item.setEnabled(multiSelected);
        }

        item=menuitems.get(Bottoni.removeselected);
        if (item != null) {
            item.setEnabled(multiSelected);
        }

        item=menuitems.get(Bottoni.deselectall);
        if (item != null) {
            item.setEnabled(multiSelected);
        }

        item = menuitems.get(Bottoni.remembercollapsed);
        if (item != null) {
            item.setChecked(getTable().isRememberColumnCollapsedStateCookie());
        }

        item = menuitems.get(Bottoni.rememberwidth);
        if (item != null) {
            item.setChecked(getTable().isRememberColumnWidthCookie());
        }


    }

    public void setCreate(boolean enabled) {
        menuitems.get(Bottoni.create).setEnabled(enabled);
    }

    public void setEdit(boolean enabled) {
        menuitems.get(Bottoni.edit).setEnabled(enabled);
    }

    public void setDelete(boolean enabled) {
        menuitems.get(Bottoni.delete).setEnabled(enabled);
    }

    public void setSearch(boolean enabled) {
        menuitems.get(Bottoni.search).setEnabled(enabled);
    }

    public void setSelect(boolean enabled) {
        itemSelect.setEnabled(enabled);
    }

    public void setOptionsEnabled(boolean enabled) {
        itemOptions.setEnabled(enabled);
    }


    /*
     * Elimina un comando dalla GUI.
     */
    public void delCmd(String label) {
        Component comp;

        comp = getComp(label);
        if (comp != null) {
            commandLayout.removeComponent(comp);
        }// end of if cycle
    }// end of method

    /**
     * Recupera il componente grafico corrispondente al comando indicato.
     */
    public Component getComp(MenuBar.MenuItem item) {
        return getComp(item.getText());
    }// end of method

    /**
     * Recupera il componente grafico corrispondente al comando indicato.
     */
    public Component getComp(String label) {
        Component comp = null;
        int max = commandLayout.getComponentCount();
        MenuBar cmd;
        MenuBar.MenuItem itemTmp;
        String labelTmp;

        for (int k = 0; k < max; k++) {
            comp = commandLayout.getComponent(k);
            if (comp instanceof MenuBar) {
                cmd = (MenuBar) comp;
                itemTmp = cmd.getItems().get(0);
                labelTmp = itemTmp.getText();
                if (labelTmp.equals(label)) {
                    return comp;
                }// end of if cycle
            }// end of if cycle
        }// end of for cycle

        return null;
    }// end of method


    public TablePortal getTablePortal() {
        return tablePortal;
    }

    private ATable getTable(){
        return tablePortal.getTable();
    }

    public enum Bottoni {
        create, edit, delete, search, selectedonly, removeselected, showall, deselectall, remembercollapsed, rememberwidth;
    }

    public interface TableToolbarListener {
        void create_();

        void edit_();

        void delete_();

        void search_();

        void selectedonly_();

        void removeselected_();

        void showall_();

        void deselectall_();

        void remembercollapsed_();

        void rememberwidth_();



    }

    private class InfoPanel extends VerticalLayout {

        private Label infoLabel = new Label();

        public InfoPanel() {
            super();
            // addStyleName("yellowBg");
            addComponent(infoLabel);
        }

        public String getInfoText() {
            return infoLabel.getValue();
        }

        public void setInfoText(String text) {
            infoLabel.setValue(text);
        }
    }// end of inner class

}// end of class
