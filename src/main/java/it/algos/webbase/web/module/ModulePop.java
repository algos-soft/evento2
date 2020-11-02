package it.algos.webbase.web.module;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.Not;
import com.vaadin.data.util.filter.Or;
import com.vaadin.event.Action;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Window;
import it.algos.webbase.web.AlgosApp;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.entity.BaseEntity_;
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.form.AForm;
import it.algos.webbase.web.form.AForm.FormListener;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.lib.LibNum;
import it.algos.webbase.web.menu.AMenuBar;
import it.algos.webbase.web.navigator.MenuCommand;
import it.algos.webbase.web.navigator.NavPlaceholder;
import it.algos.webbase.web.query.AQuery;
import it.algos.webbase.web.search.SearchManager;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.table.ModuleTable;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.TableToolbar;
import it.algos.webbase.web.toolbar.TableToolbar.TableToolbarListener;
import org.apache.commons.beanutils.BeanUtils;
import org.vaadin.addons.lazyquerycontainer.LazyEntityContainer;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.Set;

/**
 * Module displaying a table and allowing to edit the records in a form.
 */
@SuppressWarnings("serial")
public abstract class ModulePop extends Module {

    private final static Resource ICONA_STANDARD = FontAwesome.BARS;
    private static boolean MOSTRA_ID = false;
    // entity manager del modulo
    public EntityManager entityManager;
    protected Class<BaseEntity> entityClass;
    protected TablePortal tablePortal;
    // elenco dei campi da mostrare (ordinati) nel list, nel form e nel search
    protected Attribute<?, ?>[] fieldsList;
    protected Attribute<?, ?>[] fieldsForm;
    protected Attribute<?, ?>[] fieldsSearch;
    // menuitem del modulo (serve nei menu)
    protected MenuBar.MenuItem menuItem;


    // titolo del dialogo nuovo
    private String titoloNew;
    // titolo del dialogo di modifica
    private String titoloEdit;
    // titolo del dialogo di ricerca
    private String titoloSearch;
    //--etichetta del menu
    private String menuLabel = "";
    // icona del modulo (serve nei menu)
    private Resource menuIcon = null;
    private ArrayList<RecordSavedListener> recordSavedListeners = new ArrayList<>();
    private ArrayList<RecordDeletedListener> recordDeletedListeners = new ArrayList<>();

    private Window window;

    /**
     * Costruttore minimo
     *
     * @param entity di riferimento del modulo
     */
    public ModulePop(Class entity) {
        this(entity, "", null);
    }// end of constructor

    /**
     * Costruttore
     *
     * @param entity    di riferimento del modulo
     * @param menuLabel etichetta visibile nella menu bar
     */
    public ModulePop(Class entity, String menuLabel) {
        this(entity, menuLabel, null);
    }// end of constructor

    /**
     * Costruttore
     *
     * @param entity   di riferimento del modulo
     * @param menuIcon icona del menu
     */
    public ModulePop(Class entity, Resource menuIcon) {
        this(entity, "", menuIcon);
    }// end of constructor


    /**
     * Costruttore standard
     *
     * @param entity    di riferimento del modulo
     * @param menuLabel etichetta visibile nella menu bar
     * @param menuIcon  icona del menu
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public ModulePop(Class entity, String menuLabel, Resource menuIcon) {
        super();

        //create and register the EntityManager
        entityManager = EM.createEntityManager();

        if (menuLabel.equals("")) {
            this.menuLabel = entity.getSimpleName();
        } else {
            this.menuLabel = menuLabel;
        }

        if (menuIcon == null) {
            this.menuIcon = ICONA_STANDARD;
        } else {
            this.menuIcon = menuIcon;
        }

        // create the window
        window = new Window();
        window.setResizable(true);
        window.setModal(true);
        window.setHeightUndefined();


        if (entity != null) {
            this.entityClass = (Class<BaseEntity>) entity;

            // spazzola tutti i campi della Entity
            this.setFieldsList(creaFieldsList());
            this.setFieldsForm(creaFieldsForm());
            this.setFieldsSearch(creaFieldsSearch());

            // regola i titoli (caption) dei dialogi specifici
            this.regolaCaptions();

            tablePortal = createTablePortal();
            tablePortal.setHeight("100%");

            tablePortal.addToolbarListener(new TableToolbarListener() {

                @Override
                public void edit_() {
                    edit();
                }// end of inner method

                @Override
                public void delete_() {
                    delete();
                }// end of inner method

                @Override
                public void create_() {
                    create();
                }// end of inner method

                @Override
                public void search_() {
                    search();
                }// end of inner method

                @Override
                public void selectedonly_() {
                    getTable().selectedOnly();
                }// end of inner method

                @Override
                public void removeselected_() {
                    getTable().removeSelected();
                }// end of inner method

                @Override
                public void showall_() {
                    getTable().showAll();
                }

                @Override
                public void deselectall_() {
                    getTable().deselectAll();
                }

                @Override
                public void remembercollapsed_() {
                    boolean state = getTable().isRememberColumnCollapsedStateCookie();
                    getTable().setRememberColumnCollapsedState(!state);
                    if (!state) {
                        getTable().writeColumnStateCookie(true, false);    // remember immediately on activation
                    }
                }

                @Override
                public void rememberwidth_() {
                    boolean state = getTable().isRememberColumnWidthCookie();
                    getTable().setRememberColumnWidth(!state);
                    if (!state) {
                        getTable().writeColumnStateCookie(false, true);    // remember immediately on activation
                    }
                }

            });

            // add a listener to container changed events of the table
            tablePortal.getTable().addContainerChangedListener(new ATable.ContainerChangedListener() {
                @Override
                public void containerChanged(Container.ItemSetChangeEvent e) {
                    tableDataChanged();
                }
            });

//            // add listener to table events
//            tablePortal.getTable().addTableListener(new TableListener() {
//
//                @Override
//                public void datachange_() {
//                    tableDataChanged();
//                }// end of inner method
//
//                @Override
//                public void created_() {
//                }// end of inner method
//
//                @Override
//                public void attached_() {
//                }// end of inner method
//            });// end of anonymous inner class

            // Registers a new action handler for this container
            ATable tavola = getTable();
            addActionHandler(tavola);

            setCompositionRoot(tablePortal);
        }

    }// end of constructor

    /**
     * Registers a new action handler for this container
     * Sovrascritto
     *
     * @see com.vaadin.event.Action.Container#addActionHandler(Action.Handler)
     */
    protected void addActionHandler(ATable tavola) {
    }// end of method

    /**
     * Crea i campi visibili nella lista (table)
     * <p>
     * Come default spazzola tutti i campi della Entity <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     * Serve anche per l'ordine con cui vengono presentati i campi nella lista <br>
     */
    protected Attribute<?, ?>[] creaFieldsList() {
        return creaFieldsAll();
    }// end of method

    /**
     * Crea i campi visibili nella scheda (form)
     * <p>
     * Come default spazzola tutti i campi della Entity <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     * Serve anche per l'ordine con cui vengono presentati i campi nella scheda <br>
     */
    protected Attribute<?, ?>[] creaFieldsForm() {
        return creaFieldsAll();
    }// end of method

    /**
     * Crea i campi visibili nella scheda (search)
     * <p>
     * Come default spazzola tutti i campi della Entity <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     * Serve anche per l'ordine con cui vengono presentati i campi nella scheda <br>
     */
    protected Attribute<?, ?>[] creaFieldsSearch() {
        return creaFieldsAll();
    }// end of method

    /**
     * Crea i campi visibili
     * <p>
     * Come default spazzola tutti i campi della Entity <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     * NON garantisce l'ordine con cui vengono presentati i campi nella scheda <br>
     * Può mostrare anche il campo ID, oppure no <br>
     * Se si vuole differenziare tra Table, Form e Search, <br>
     * sovrascrivere creaFieldsList, creaFieldsForm e creaFieldsSearch <br>
     */
    protected Attribute<?, ?>[] creaFieldsAll() {
        return getAttributesList().toArray(new Attribute[0]);
    }// end of method

    /**
     * @return a list containing all the attributes for the module
     */
    public ArrayList<Attribute<?, ?>> getAttributesList() {
        ArrayList<Attribute<?, ?>> lista = new ArrayList<Attribute<?, ?>>();
        EntityType<?> type = EM.getEntityType(getEntityClass());
        Set<?> attributes = type.getAttributes();
        Attribute<?, ?> attribute;

        if (MOSTRA_ID) {
            lista.add(BaseEntity_.id);
        }// fine del blocco if
        for (Object ogg : attributes) {
            if (ogg instanceof Attribute<?, ?>) {
                attribute = (Attribute<?, ?>) ogg;
                String attrName = attribute.getName();
                String idName = BaseEntity_.id.getName();
                if (!attrName.equals(idName)) {
                    lista.add(attribute);
                }// fine del blocco if
            }// fine del blocco if
        }// end of for cycle

        return lista;
    }// end of method

    /**
     * Regola i titoli (caption) dei dialoghi specifici.
     * <p>
     * Regola il titolo (caption) dei dialogo nuovo record. <br>
     * Regola il titolo (caption) dei dialogo di modifica. <br>
     * Regola il titolo (caption) dei dialogo di ricerca. <br>
     */
    protected void regolaCaptions() {
        this.setTitoloNew(getCaptionNew());
        this.setTitoloEdit(getCaptionEdit());
        this.setTitoloSearch(getCaptionSearch());
    }// end of method

    /**
     * Titolo (caption) dei dialogo nuovo record. <br>
     * Come default usa il titolo standard <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     */
    protected String getCaptionNew() {
        return "Nuovo";
    }// end of method

    /**
     * Titolo (caption) dei dialogo di modifica. <br>
     * Come default usa il titolo standard <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     */
    protected String getCaptionEdit() {
        return "Modifica";
    }// end of method

    /**
     * Titolo (caption) dei dialogo di ricerca. <br>
     * Come default usa il titolo standard <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     */
    protected String getCaptionSearch() {
        return "Ricerca";
    }// end of method

    /**
     * Returns the table used to shows the list. <br>
     * The concrete subclass must override for a specific Table.
     *
     * @return the Table
     */
    public ATable createTable() {
        return (new ModuleTable(this));
    }// end of method


    /**
     * Returns the form used to edit an item. <br>
     * The concrete subclass must override for a specific Form.
     *
     * @param item singola istanza della classe
     * @return the Form
     */
    public ModuleForm createForm(Item item) {
        return (new ModuleForm(item, this));
    }// end of method


    /**
     * Create the Search Manager
     *
     * @return the SearchManager
     */
    public SearchManager createSearchManager() {
        SearchManager manager = new SearchManager(this);
        return manager;
    }// end of method


    /**
     * Create the MenuBar Item for this module
     * <p>
     * Invocato dal metodo AlgosUI.creaMenu()
     * PUO essere sovrascritto dalla sottoclasse
     *
     * @param menuBar     a cui agganciare il menuitem
     * @param placeholder in cui visualizzare il modulo
     * @return menuItem appena creato
     * @deprecated
     */
    public MenuBar.MenuItem createMenuItem(MenuBar menuBar, NavPlaceholder placeholder) {
        MenuBar.MenuItem menuItem;
        MenuCommand comando = new MenuCommand(menuBar, this);
        menuItem = menuBar.addItem(getMenuLabel(), getMenuIcon(), comando);
        menuItem.setStyleName(AMenuBar.MENU_DISABILITATO);

        this.menuItem = menuItem;

        return menuItem;
    }// end of method

    /**
     * Crea i sottomenu specifici del modulo
     * <p>
     * Invocato dal metodo AlgosUI.addModulo()
     * Sovrascritto dalla sottoclasse
     *
     * @param menuItem principale del modulo
     */
    public void addSottoMenu(MenuBar.MenuItem menuItem) {
    }// end of method


    /**
     * Create button pressed in table
     * <p>
     * Create a new item and edit it in a form
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void create() {
        editItem(null, true, getTitoloNew());
    }// end of method


    /**
     * Edit button pressed in table.
     * Display the item in a form
     */
    @SuppressWarnings("rawtypes")
    public void edit() {

//        BaseEntity entity= getTable().getSelectedEntity();
//        if(entity!=null){
//            Long itemId = entity.getId();
//            if (itemId != null) {
//                Item item = getTable().getItem(itemId);
//                editItem(item, false, getTitoloEdit());
//            }
//        }

        // new 6 apr-2016
        // prima di andare alla scheda ricarico l'entity dal database per essere sicuro che sia aggiornata
        Object id = getTable().getSelectedId();
        if (id != null) {
            BaseEntity e = getEntityManager().find(getEntityClass(), id);
            Item item = new BeanItem(e);
            editItem(item, false, getTitoloEdit());
        }

    }// end of method


    /**
     * Edits an Item in a Form
     *
     * @param item      the item
     * @param newRecord if the edited item is a new record
     */
    public void editItem(Item item, boolean newRecord) {
        editItem(item, newRecord, "");
    }// end of method

    /**
     * Edits an Item in a Form
     *
     * @param item      the item
     * @param newRecord if the edited item is a new record
     * @param caption   title for the window
     */
    public void editItem(Item item, boolean newRecord, String caption) {

        final AForm form = createForm(item);

        if (form != null) {

//            final Window window = new Window();
//            window.setResizable(false);
//            window.setHeightUndefined();

            window.setCaption(caption);
            window.setContent(form);


//            // fisso l'altezza della finestra perché se è undefined
//            // e contiene un FormLayout, a causa di un bug
//            // di FormLayout la barra di scorrimento verticale
//            // si comporta in modo incongruo (non appare al momento giusto).
//            // Se invece l'altezza è fissa (assoluta o percentuale, è uguale)
//            // allora la scroll bar si comporta correttamente
//            // anche quando il contenuto è un FormLayout.
//            // Alex 21-05-2016
//            window.setHeight("90%");



            // make a copy of the original entity, in case it is modified
            final BaseEntity origEntity = cloneEntity(form.getEntity());

            // add the FormListener
            form.addFormListener(new FormListener() {

                @SuppressWarnings({"rawtypes", "unchecked"})
                @Override
                public void commit_() {

                    window.close();

                    // after successful editing, if was a new record display only
                    // the new record in the table, if was an edit refresh the row cache
                    if (newRecord) {
                        BaseEntity newEntity = form.getEntity();
                        if (newEntity != null) {

                            if (AlgosApp.DISPLAY_NEW_RECORD_ONLY) {
                                long id = newEntity.getId();
                                Container cont = getTable().getContainerDataSource();
                                if (cont instanceof Container.Filterable) {
                                    Container.Filterable filterable = (Container.Filterable) cont;
                                    filterable.removeAllContainerFilters();
                                    Filter filter = new Compare.Equal(BaseEntity_.id.getName(), id);
                                    filterable.addContainerFilter(filter);
                                    getTable().deselectAll();
                                    getTable().select(id);
                                }
                            }// end of if cycle

                            // fire the recordCreated listener in the Table
                            ATable table = getTable();
                            if (table != null) {
                                RecordEvent e = new RecordEvent(newEntity);
                                for (RecordSavedListener l : recordSavedListeners) {
                                    l.recordCreated(e); // created
                                    l.recordSaved(e);   // and saved
                                }
                            }
                            // after editing, refresh the table's container
                            getTable().refresh();

                            // This is needed to update generated columns in the table.
                            // (standard columns which are bound to properties are updated
                            // automatically when the item changes)
                            getTable().refreshRowCache();

                        }

                    } else {

                        // after editing, refresh the table's container
                        getTable().refresh();

                        // This is needed to update generated columns in the table.
                        // (standard columns which are bound to properties are updated
                        // automatically when the item changes)
                        getTable().refreshRowCache();

                        // fire the recordSaved listener in the Table
                        ATable table = getTable();
                        if (table != null) {
                            BaseEntity newEntity = form.getEntity();
                            RecordEvent e = new RecordEvent(newEntity, origEntity);
                            for (RecordSavedListener l : recordSavedListeners) {
                                l.recordSaved(e);
                            }
                        }

                    }

                    getTable().updateTotals();


                }

                @Override
                public void cancel_() {
                    window.close();
                }// end of inner method

            });


            form.setHeightUndefined();

            window.center();

            this.getUI().addWindow(window);

            // FocusListener and bringToFront() mark the window as dirty upon focus received.
            // (needed if a FormLayout is contained in the window)
            // Due to a FormLayout bug, the size of the FormLayout is not calculated correctly
            // and the scroll bar in the window might not appear when close to the edge.
            // Reattaching the window fixes the issue.
            // Alex may-2016
            window.addFocusListener(new FieldEvents.FocusListener() {
                @Override
                public void focus(FieldEvents.FocusEvent focusEvent) {
                    window.markAsDirty();
                }
            });
            window.bringToFront();


        }// end of if cycle

    }// end of method


    /**
     * Make a clone of an Entity
     */
    private BaseEntity cloneEntity(BaseEntity origEntity) {
        BaseEntity entityCopy = null;
        try {
            entityCopy = (BaseEntity) BeanUtils.cloneBean(origEntity);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return entityCopy;
    }

    /**
     * Invoked before any single deletion.
     * <p>
     *
     * @return true to proceed, false to abort
     */
    protected boolean preDelete(BaseEntity bEntity) {
        return true;
    }// end of method

    /**
     * Delete selected items button pressed
     */
    public void delete() {
        final Object[] ids = getTable().getSelectedIds();
        if (ids.length > 0) {
            showDeleteDialog(ids);
        }// end of if cycle
    }// end of method

    /**
     * Show delete confirmation dialog, delete if confirmed
     */
    public void showDeleteDialog(final Object[] ids) {
        String msg = "";
        if (ids.length == 1) {
            msg = "Sei sicuro?";
        } else {
            msg = "Vuoi eliminare " + ids.length + " records?";
        }// end of if/else cycle

        ConfirmDialog dialog = new ConfirmDialog("Eliminazione", msg, new ConfirmDialog.Listener() {

            @Override
            public void onClose(ConfirmDialog dialog, boolean confirmed) {
                if (confirmed) {
                    delete(ids);
                }// end of if cycle
            }// end of inner method
        });// end of anonymous inner class
        dialog.setConfirmButtonText("Elimina");
        dialog.show(getUI());

    }

    /**
     * Delete items from container
     */
    public void delete(Object[] ids) {
        for (Object id : ids) {
            delete(id);

            for (RecordDeletedListener l : recordDeletedListeners) {
                long num = (long) id;
                RecordEvent e = new RecordEvent(num);
                l.recordDeleted(e);
            }
        }
        getTable().refresh();
        getTable().refreshRowCache();
    }

    public void delete(Object id) {
        BaseEntity entity =  AQuery.find(getEntityClass(), (long)id);
        if (entity!=null) {
            entity.delete();
        }// end of if cycle

//        // delegate to the table's Container
//        Container cont = getTable().getContainerDataSource();
//        cont.removeItem(id);
//        if (cont instanceof Buffered) {
//            Buffered bf = (Buffered) cont;
//            bf.commit();
//        }
    }

    /**
     * Search button pressed in table
     * <p>
     * Displays the Search dialog
     */
    public void search() {
        final SearchManager sm = createSearchManager();
        if (sm != null) {
            sm.setCloseListener(new ConfirmDialog.Listener() {

                @Override
                public void onClose(ConfirmDialog dialog, boolean confirmed) {
                    if (confirmed) {

                        Container cont = getTable().getContainerDataSource();
                        if (cont instanceof Container.Filterable) {
                            Container.Filterable filtCont = (Container.Filterable) cont;
                            Filter filter = sm.getFilter();
                            if (filter != null) {

                                SearchManager.CombineSearchOptions option = sm.getCombineOption();
                                if (option != null) {

                                    // retriever the current filter from the container
                                    Collection<Filter> currentFilters = null;
                                    // method getFilters() is not in interface so we have to cast
                                    if (filtCont instanceof LazyEntityContainer) {
                                        LazyEntityContainer lec = (LazyEntityContainer) filtCont;
                                        currentFilters = lec.getContainerFilters();
                                    }
                                    if (filtCont instanceof JPAContainer) {
                                        JPAContainer jpac = (JPAContainer) filtCont;
                                        currentFilters = jpac.getFilters();
                                    }
                                    // create a filter from the existing filters
                                    Filter[] aFilters = currentFilters.toArray(new Filter[0]);
                                    Filter currentFilter = null;
                                    if (aFilters.length > 0) {
                                        if (aFilters.length > 1) {
                                            currentFilter = new And(aFilters);
                                        } else {
                                            currentFilter = aFilters[0];
                                        }
                                    }

                                    Filter newFilter;
                                    switch (option) {

                                        case addToList: // add to current records (or)

                                            // creates a new filter
                                            newFilter = new Or(currentFilter, filter);

                                            // sets the new filter in the container
                                            filtCont.removeAllContainerFilters();
                                            getTable().refresh(); // refresh before applying

                                            // new filters
                                            if (filter != null) {
                                                filtCont.addContainerFilter(newFilter);
                                            }// end of if cycle
                                            break;

                                        case removeFromList: // subtract from current (and not)

                                            // creates a new filter
                                            Filter notFilter = new Not(filter);
                                            newFilter = new And(currentFilter, notFilter);

                                            // sets the new filter in the container
                                            filtCont.removeAllContainerFilters();
                                            getTable().refresh(); // refresh before applying new filters
                                            if (filter != null) {
                                                filtCont.addContainerFilter(newFilter);
                                            }// end of if cycle
                                            break;

                                        case searchInList: // search in current (and)
                                            // add a new filter to the container
                                            filtCont.addContainerFilter(filter);
                                            break;

                                    }// end of switch cycle

                                } else { // no combine option

                                    filtCont.removeAllContainerFilters();
                                    //cont.refresh(); // refresh before applying
                                    // new filters -- disabled alex 24-09-2014
                                    // too slow
                                    if (filter != null) {
                                        filtCont.addContainerFilter(filter);
                                    }// end of if cycle

                                }// end of if/else cycle

                            } else { // null filter
                                filtCont.removeAllContainerFilters();
                            }// end of if/else cycle
                        }

                    }// end of if cycle
                }// end of inner method
            });// end of anonymous inner class
            sm.show(getUI());
        }// end of if cycle

    }// end of method


    /**
     * Create the Table Portal
     *
     * @return the TablePortal
     */
    public TablePortal createTablePortal() {
        return new TablePortal((ModulePop) this);
    }// end of method

    /**
     * Invoked when table data changes
     */
    protected void tableDataChanged() {
        String visibleTxt = "";
        String totalTxt = "";
        int visible = getTable().getVisibleRows();
        long total = getTable().getTotalRows();

        visibleTxt = LibNum.format(visible);
        totalTxt = LibNum.format(total);

        String text = visibleTxt + "/" + totalTxt + " records";

        TablePortal tp = getTablePortal();
        if (tp != null) {
            TableToolbar tb = tp.getToolbar();
            if (tb != null) {
                tb.setInfoText(text);
            }
        }
    }// end of method

    /**
     * Returns a list of the visible columns
     *
     * @return the Form
     */
    public Object[] getVisibleColumns() {
        return null;
    }// end of method

    public TablePortal getTablePortal() {
        return tablePortal;
    }// end of method

    public ATable getTable() {
        return getTablePortal().getTable();
    }// end of method

    // get the selected rows id
    protected Long getValue() {
        Long rowId = null;

        if (getTable() != null && getTable().getValue() instanceof Long) {
            rowId = (Long) getTable().getValue();
        }// end of if cycle

        return rowId;
    }// end of method

    // get the selected item
    protected Item getTableItem() {
        Item item = null;
        Long rowId = getValue();

        if (rowId != null && rowId > 0) {
            item = getTable().getItem(rowId);
        }// end of if cycle

        return item;
    }// end of method

    @SuppressWarnings("rawtypes")
    private BaseEntity itemToBean(Item item) {
        BaseEntity entity = null;
        if (item instanceof BeanItem) {
            Object bean = ((BeanItem) item).getBean();
            if (bean instanceof BaseEntity) {
                entity = (BaseEntity) bean;
            }// end of if cycle

        }// end of if cycle
        return entity;
    }// end of method

    public Class<BaseEntity> getEntityClass() {
        return entityClass;
    }// end of method

    /**
     * @return the fieldsList
     */
    @SuppressWarnings("rawtypes")
    public Attribute[] getFieldsList() {
        return fieldsList;
    }// end of method

    /**
     * @param fieldsList the fieldsList to set
     */
    @SuppressWarnings("rawtypes")
    public void setFieldsList(Attribute[] fieldsList) {
        this.fieldsList = fieldsList;
    }// end of method

    /**
     * @return the fieldsForm
     */
    @SuppressWarnings("rawtypes")
    public Attribute[] getFieldsForm() {
        return fieldsForm;
    }// end of method

    /**
     * @param fieldsForm the fieldsForm to set
     */
    @SuppressWarnings("rawtypes")
    public void setFieldsForm(Attribute[] fieldsForm) {
        this.fieldsForm = fieldsForm;
    }// end of method

    /**
     * @return the fieldsSearch
     */
    @SuppressWarnings("rawtypes")
    public Attribute[] getFieldsSearch() {
        return fieldsSearch;
    }// end of method

    /**
     * @param fieldsSearch the fieldsSearch to set
     */
    @SuppressWarnings("rawtypes")
    public void setFieldsSearch(Attribute[] fieldsSearch) {
        this.fieldsSearch = fieldsSearch;
    }// end of method

    /**
     * @return the titoloSearch
     */
    public String getTitoloSearch() {
        return titoloSearch;
    }// end of method

    /**
     * @param titoloSearch the titoloSearch to set
     */
    public void setTitoloSearch(String titoloSearch) {
        this.titoloSearch = titoloSearch;
    }// end of method

    /**
     * @return the titoloNew
     */
    public String getTitoloNew() {
        return titoloNew;
    }

    /**
     * @param titoloNew the titoloNew to set
     */
    public void setTitoloNew(String titoloNew) {
        this.titoloNew = titoloNew;
    }

    /**
     * @return the titoloEdit
     */
    public String getTitoloEdit() {
        return titoloEdit;
    }

    /**
     * @param titoloEdit the titoloEdit to set
     */
    public void setTitoloEdit(String titoloEdit) {
        this.titoloEdit = titoloEdit;
    }

    public String getClassName() {
        String name = "";
        String tag = ".";

        if (entityClass != null) {
            name = this.entityClass.getCanonicalName();
        }// end of if cycle

        if (!name.equals("")) {
            if (name.contains(tag)) {
                name = name.substring(name.lastIndexOf(tag) + 1);
            }// end of if cycle
        }// end of if cycle

        return name;
    }// end of method

    public String getMenuLabel() {
        return menuLabel;
    }// end of getter method


    public Resource getMenuIcon() {
        return menuIcon;
    }// end of getter method


    public MenuBar.MenuItem getMenuItem() {
        return menuItem;
    }// end of getter method

    public void setMenuItem(MenuBar.MenuItem menuItem) {
        this.menuItem = menuItem;
    }//end of setter method


    public Window getWindow() {
        return window;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }


    public void addRecordSavedListener(RecordSavedListener l) {
        recordSavedListeners.add(l);
    }

    public void addRecordDeletedListener(RecordDeletedListener l) {
        recordDeletedListeners.add(l);
    }

    /**
     * A record (new or existing) has been saved.
     * When a record is created, both methods are invoked
     */
    public interface RecordSavedListener {
        void recordCreated(ModulePop.RecordEvent e);

        void recordSaved(ModulePop.RecordEvent e);
    }

    /**
     * A record has been deleted
     */
    public interface RecordDeletedListener {
        void recordDeleted(ModulePop.RecordEvent e);
    }


    /**
     * Record Event.
     * Represents an action taken on a record.
     */
    public class RecordEvent extends EventObject {

        private BaseEntity newEntity;
        private BaseEntity oldEntity;
        private long id;

        public RecordEvent(BaseEntity newEntity, BaseEntity oldEntity) {
            super(ModulePop.this);
            this.newEntity = newEntity;
            this.oldEntity = oldEntity;
        }

        public RecordEvent(BaseEntity newEntity) {
            this(newEntity, null);
        }


        public RecordEvent(long id) {
            this(null, null);
            this.id = id;
        }

        public BaseEntity getNewEntity() {
            return newEntity;
        }

        public BaseEntity getOldEntity() {
            return oldEntity;
        }

        public long getId() {
            if (isDeleted()) {
                return id;
            } else {
                if (isNewRecord()) {
                    return getNewEntity().getId();
                } else {
                    return getOldEntity().getId();
                }
            }
        }

        public boolean isNewRecord() {
            return (newEntity != null && oldEntity == null);
        }

        public boolean isDeleted() {
            return (newEntity == null && oldEntity == null);
        }


    }

}// end of class
