package it.algos.webbase.web.form;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.lib.Lib;
import it.algos.webbase.web.lib.LibBean;
import it.algos.webbase.web.lib.LibField;
import it.algos.webbase.web.toolbar.FormToolbar;

import javax.persistence.metamodel.Attribute;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

/**
 * A generic form to edit one Item.
 * <p>
 * Usage:
 * <p>
 * - make a subclass, then:
 * - create and add the fields in the createFields() method
 * - add a constructor
 * - call super() with some kind of Item
 * - call the init() method
 * - eventually modify other stuff like toolbar etc
 * <p>
 * To read field values use getFieldValue()
 * To listen for confirm or cancel buttons, add a FormListener.
 * To change the button's text use setCancelText() and setConfirmText().
 * If you need a different toolbar, get it with getToolbar() and customize it.
 */
public abstract class AForm extends VerticalLayout {

    private Item item;
    private FieldGroup binder;
    private FormToolbar toolbar;
    private ArrayList<FormListener> listeners = new ArrayList();

    /**
     * Constructor
     *
     * @param item the item to edit
     */
    public AForm(Item item) {

        setHeight("100%");  // questa riga sembra che non serva - alex 23-05-2016
        //addStyleName("yellowBg");

        this.item = item;

        // create the binder for binding the fields to the item.
        // binder.commit() is needed to update the item data source.
        // validation is run on commit.
        this.binder = new FieldGroup(this.item);
        this.binder.setBuffered(true);

    }

    protected void init() {

        // create the fields
        createFields();

        // create and add the detail component
        Component detail = createComponent();
        this.addComponent(detail);

        //--modifica nelle sottoclassi (eventualmente) i fields appena creati in automatico
        //--tipicamente, aggiunge dei listeneres
        this.modificaFields();

        // queste due righe sembra che non servano - alex 23-05-2016
        detail.setHeight("100%");
        this.setExpandRatio(detail, 1);

        // create and add the form toolbar
        this.toolbar = createToolBar();

        // add the standard toolbar listener
        toolbar.addToolbarListener(new FormToolbar.FormToolbarListener() {

            @Override
            public void save_() {
                if (onPreSave(binder)) {
                    if (save()) {
                        fire(FormEvent.commit);
                        onPostSave(binder);
                    }
                }
            }// end of method

            @Override
            public void reset_() {
            }// end of method

            @Override
            public void cancel_() {
                fire(FormEvent.cancel);
            }// end of method

        });// end of anonymous class

        this.addComponent(this.toolbar);
        toolbar.setWidth("100%");
    }

    /**
     * Populate the map to bind item properties to fields.
     * Create and add a field for each property declared for this form
     */
    public abstract void createFields();


    /**
     * Opportunità per le sottoclassi di modificare alcune caratteristiche dei fields
     * Tipicamente, aggiunge dei listeneres
     * Sovrascritto
     */
    protected  void modificaFields(){
    }// end of method

    protected Field createField(Attribute attr) {
        return LibField.createField(attr);
    }


//    /**
//     * Create a single field.
//     * The field type is chosen according to the Java type.
//     *
//     * @param attr the metamodel Attribute
//     */
//    protected Field createField(Attribute attr) {
//        Field field = null;
//
//        if (attr != null) {
//
//
//            try {
//
//                if (attr.isAssociation()) { // questo può fallire!!
//
//                    // relazione OneToMany - usiamo un campo lista (?)
//                    if (attr instanceof PluralAttribute) {
//                        PluralAttribute pa = (PluralAttribute) attr;
//                        Class clazz = pa.getElementType().getJavaType();
//                        //field = new RelatedComboField(clazz);
//                        //field=null;// todo qui ci vuole una lista
//                    }
//
//                    // relazione ManyToOne - usiamo un campo combo
//                    if (attr instanceof SingularAttribute) {
//                        SingularAttribute sa = (SingularAttribute) attr;
//                        Class clazz = sa.getJavaType();
//                        field = new RelatedComboField(clazz);
//                    }
//
//                } else {
//
//                    Class clazz = attr.getJavaType();
//
//                    if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
//                        field = new CheckBoxField();
//                    }
//
//                    if (clazz.equals(String.class)) {
//                        field = new TextField();
//                    }
//
//                    if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
//                        field = new IntegerField();
//                    }
//
//                    if (clazz.equals(Long.class) || clazz.equals(long.class)) {
//                        field = new LongField();
//                    }
//
//                    if (clazz.equals(Date.class)) {
//                        field = new DateField();
//                    }
//
//                    if (clazz.equals(BigDecimal.class)) {
//                        field = new DecimalField();
//                    }
//
//                    if (clazz.equals(Timestamp.class)) {
//                        field = new DateField();
//                    }
//
//                    if (clazz.isEnum()) {
//                        Object[] values = clazz.getEnumConstants();
//                        ArrayComboField acf = new ArrayComboField(values);
//                        acf.setNullSelectionAllowed(true);
//                        field = acf;
//                    }
//
//                }
//
//                // create and assign the caption
//                if (field != null) {
//                    String caption = DefaultFieldFactory.createCaptionByPropertyId(attr.getName());
//                    field.setCaption(caption);
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//
//        }
//
//
//        return field;
//
//    }


    /**
     * Adds a field with a given key to the fields map and binds the field to the property
     * <p>
     * If the key is of type Attribute, the attribute's name is used as key
     * and a Validator is added for attribute's type.
     *
     * @param key   - the key
     * @param field - the field
     */
    @SuppressWarnings("rawtypes")
    protected void addField(Object key, Field field) {

        if (key instanceof Attribute) {

            Attribute attr = (Attribute) key;


            // if this is an Association field, validate the associated bean
            if (attr.isAssociation()) {

                // remove any existing validators
                // (if you want more validators, add them after the field is added to the form)
                // The reason to remove validators here, is that some fields (e.g. the Email field)
                // can already have their pre-built validator, and in this case we would end up
                // having multiple validators.
                field.removeAllValidators();

                // Add the bean validator
                Class clazz = attr.getJavaMember().getDeclaringClass();
                BeanValidator validator = new BeanValidator(clazz, attr.getName());
                field.addValidator(validator);

            }

            // reassign the the key as the member name
            key = attr.getName();
        }
        //--@todo patch (di gac, ovvio)x
        //--@todo xAlex: se non vedi problemi, leva il todo
        //--siccome la funzione binder si 'mangia' il valore enabled() -lo forza sempre a true-
        //--me lo metto da parte e lo ripristino successivamente alla chiamata
        boolean statusEnabled = field.isEnabled(); //--aggiunta
        this.binder.bind(field, key);
        field.setEnabled(statusEnabled); //--aggiunta

    }


    /**
     * Return a field
     *
     * @param propertyId the key for the field (if the key is Attribute, the attribute's name is used)
     * @return the field
     */
    public Field getField(Object propertyId) {
        if (propertyId instanceof Attribute) {
            propertyId = ((Attribute) propertyId).getName();
        }// end of if cycle
        return binder.getField(propertyId);
    }// end of method

    /**
     * Return all the fields
     *
     * @return the fields
     */
    protected Collection<Field<?>> getFields() {
        return binder.getFields();
    }// end of method


    /**
     * Create the detail component (the upper part containing the fields).
     * <p>
     * Usa il FormLayout che ha le label a sinistra dei campi (standard)
     * Se si vogliono le label sopra i campi, sovrascivere questo metodo e usare un VerticalLayout
     *
     * @return the detail component containing the fields
     */
    protected Component createComponent() {
        FormLayout layout = new AFormLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        return creaCompDetail(layout);
    }// end of method

    /**
     * Crea il componente che visualizza il dettaglio
     * Retrieve the fields from the binder and place them in the UI.
     *
     * @return il componente dettagli
     */
    protected Component creaCompDetail(FormLayout layout) {
        Collection<Field<?>> fields = binder.getFields();

        boolean focused = false;
        for (Field<?> field : fields) {

            layout.addComponent(field);

            // focus to first field
            if (!focused) {
                field.focus();
                focused = true;
            }

        }// end of for cycle

        return layout;
    }// end of method

    /**
     * Create the toolbar.
     *
     * @return the toolbar
     */
    protected FormToolbar createToolBar() {
        return new FormToolbar(this);
    }// end of method


    /**
     * Saves the current values to the storage.
     * <p>
     *
     * @return true if saved successfully
     */
    protected boolean save() {
        boolean saved = false;

        ArrayList<String> reasons = isValid();

        if (reasons.size() == 0) {
            try {

                // This call validates all the fields.
                // - if the validation is successfull the item is updated.
                // - if the validation fails a CommitException is thrown.
                binder.commit();

                // the field group has been committed successfully and
                // the Item now is updated with the new values
                postCommit();

                saved = true;

            } catch (FieldGroup.CommitException e) {
                Notification.show("La scheda non è registrabile", Notification.Type.WARNING_MESSAGE);
            }

        } else {    // the form is invalid due to some specific reason (not covered by the validators)

            // show the reasons
            String string = "";
            for (String reason : reasons) {
                if (!string.equals("")) {
                    string += "<br>";
                }
                string += reason;
            }
            Notification notification = new Notification("La scheda non è valida", string, Notification.Type.HUMANIZED_MESSAGE);
            notification.setDelayMsec(-1);
            notification.show(Page.getCurrent());

        }

        return saved;

    }// end of method


    /**
     * the field group has been committed successfully and
     * the Item now is updated with the new values
     */
    public void postCommit() {
    }

    /**
     * Invoked just before saving the item.
     * Chance for subclasses to override.
     *
     * @param fields the field group
     * @return true to continue saving, false to stop saving
     */
    protected boolean onPreSave(FieldGroup fields) {
        return true;
    }

    /**
     * Invoked after the item has been saved.
     * Chance for subclasses to override.
     *
     * @param fields the field group
     */
    protected void onPostSave(FieldGroup fields) {
    }

    /**
     * Reloads the fields from the Item data source.
     * Unsaved changes are discarded.
     * <p>
     */
    protected void reload() {
        getBinder().discard();
    }

    /**
     * Checks if the current values are valid and ready to be persisted.
     * <p>
     *
     * @return a list of strings containing the reasons if not valid, empty list if valid.
     */
    protected ArrayList<String> isValid() {
        ArrayList<String> reasons = new ArrayList();
        return reasons;
    }

    public FieldGroup getBinder() {
        return binder;
    }


    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    /**
     * @return the Window the component is attached to
     */
    public Window getWindow() {
        return findAncestor(Window.class);
    }


    /**
     * Retrieve the value of a given field
     *
     * @param key the key (String or Attribute)
     * @return the value
     */
    public Object getFieldValue(Object key) {
        Object value = null;
        if (key instanceof Attribute) {
            key = ((Attribute) key).getName();
        }
        Field field = getField(key);
        if (field != null) {
            value = field.getValue();
        }

        return value;
    }

    /**
     * Retrieve the value of a given field as String
     *
     * @param key the key
     * @return the value
     */
    public String getStringValue(Object key) {
        return Lib.getString(getFieldValue(key));
    }

    /**
     * Retrieve the value of a given field as int
     *
     * @param key the key
     * @return the value
     */
    public int getIntValue(Object key) {
        return Lib.getInt(getFieldValue(key));
    }

    /**
     * Retrieve the value of a given field as BigDecimal
     *
     * @param key the key
     * @return the value
     */
    public BigDecimal getBigDecimalValue(Object key) {
        return Lib.getBigDecimal(getFieldValue(key));
    }


    /**
     * Retrieve the value of a given field as long
     *
     * @param key the key
     * @return the value
     */
    public long getLongValue(Object key) {
        return Lib.getLong(getFieldValue(key));
    }


    /**
     * Returns the id of the item currently being edited
     *
     * @return the item id
     */
    public long getItemId() {
        long id = 0;
        Item item = getItem();
        Property prop = item.getItemProperty("id");
        if (prop != null) {
            Object value = prop.getValue();
            if (value != null) {
                id = (long) value;
            }
        }
        return id;
    }

    /**
     * Add a form listener to the form
     */
    public void addFormListener(FormListener listener) {
        listeners.add(listener);
    }

    /**
     * Fires a form event to the registered listeners
     */
    protected void fire(FormEvent event) {
        for (FormListener l : listeners) {
            switch (event) {
                case cancel:
                    l.cancel_();
                    break;
                case commit:
                    l.commit_();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * @return the Entity currently edited, retrieved from the Item
     */
    public BaseEntity getEntity() {
        return LibBean.entityFromItem(getItem());
//        if (item != null) {
//
//            if (item instanceof BeanItem) {
//                BeanItem bi = (BeanItem) item;
//                entity = (BaseEntity) bi.getBean();
//            }
//
//            if (item instanceof CompositeItem) {
//                CompositeItem cItem = (CompositeItem) item;
//                BeanItem bi = (BeanItem) cItem.getItem("bean");
//                entity = (BaseEntity) bi.getBean();
//            }
//
//        }
//        return entity;
    }

    public FormToolbar getToolbar() {
        return toolbar;
    }

    public void setConfirmText(String text) {
        getToolbar().getConfirmItem().setText(text);
    }

    public void setCancelText(String text) {
        getToolbar().getCancelItem().setText(text);
    }

    /**
     * Enum of supported Form events
     */
    public enum FormEvent {
        cancel, commit;
    }

    /**
     * Form event
     */
    public interface FormListener {
        public void cancel_();

        public void commit_();
    }


}
