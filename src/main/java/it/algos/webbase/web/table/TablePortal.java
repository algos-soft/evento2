package it.algos.webbase.web.table;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalLayout;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.toolbar.TableToolbar;
import it.algos.webbase.web.toolbar.TableToolbar.TableToolbarListener;

/**
 * Portal hosting a table and a toolbar
 */
@SuppressWarnings("serial")
public class TablePortal extends VerticalLayout implements ATable.SelectionChangedListener {

    public ATable table;
    protected TableToolbar toolbar;
    protected TableFooter footer;
    private ModulePop module;

    public TablePortal(ATable table) {
        super();
        this.table = table;
        init();
    }

    public TablePortal(ModulePop modulo) {
        super();
        this.module = modulo;
        this.table = modulo.createTable();
        init();
    }

    protected void init() {

        // creates the table
        // this.table = createTable();
        this.table.setWidth("100%");
        this.table.setHeight("100%");
        addComponent(this.table);
        setExpandRatio(this.table, 1f);

        // creates the toolbar
        this.toolbar = createToolbar();
        if (toolbar != null) {
            this.toolbar.setWidth("100%");
            addComponent(this.toolbar);
        }// fine del blocco if

        // creates the table footer
        this.footer = createFooter();
        if (footer != null) {
            this.footer.setWidth("100%");
            // addComponent(this.footer);
        }// fine del blocco if

        // aggiunge un double click listener alla table.
        // Quando lo riceve, invoca un apposito metodo fireEdit()
        // sulla toolbar, simulando la pressione del bottone edit
        table.addItemClickListener(new ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    if (getToolbar() != null) {
                        getToolbar().fireEdit();
                    }
                }
            }
        });

        // The toolbar listens to table selection changes
        if (this.getToolbar() != null) {
            getTable().addSelectionChangedListener(this.getToolbar());
        }

        // The portal listens to table selection changes
        //@todo aggiunto da gac - 7.5.16
        getTable().addSelectionChangedListener(this);

    }// end of method


    /**
     * Creates the toolbar
     * Barra standard con 6 menuitems (nuovo, modifica, elimina, cerca, selezione, opzioni)
     * Sovrascrivibile, per aggiungere/modificare menuitems
     */
    public TableToolbar createToolbar() {
        return new TableToolbar(this);
    }


    public TableFooter createFooter() {
        return new TableFooter();
    }

//    /**
//     * Sincronizzazione dei listener per il funzionamento della ATable
//     * <p>
//     * Registra la TableToolBar nei selectionListeners della ATable per gli eventi di tipo ListSelectionEvent
//     */
//    protected void regolaListenerTable() {
//        ATable table = this.getTable();
//        TableToolbar tableToolBar = this.getToolbar();
//        table.addSelectionChangedListener(tableToolBar);
//    }

    public void addToolbarListener(TableToolbarListener listener) {
        TableToolbar tb = getToolbar();
        if (tb != null) {
            tb.addToolbarListener(listener);
        }
    }

    public ATable getTable() {
        return table;
    }

    public TableToolbar getToolbar() {
        return toolbar;
    }

    public ModulePop getModule() {
        return module;
    }

    @Override
    public void selectionChanged(ATable.SelectionChangeEvent e) {
    }// end of method


    /*
     * Elimina un comando dalla GUI.
     */
    public void delCmd(String label) {
        if (toolbar != null) {
            toolbar.delCmd(label);
        }// fine del blocco if
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
        if (toolbar != null) {
            return toolbar.getComp(label);
        } else {
            return null;
        }// fine del blocco if-else
    }// end of method

}// end of class
