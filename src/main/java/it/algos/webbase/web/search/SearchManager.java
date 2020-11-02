package it.algos.webbase.web.search;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.ui.*;
import com.vaadin.ui.DateField;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.dialog.DialogToolbar;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.field.*;
import it.algos.webbase.web.field.TextField;
import it.algos.webbase.web.form.AFormLayout;
import it.algos.webbase.web.lib.Lib;
import it.algos.webbase.web.lib.LibField;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.query.AQuery;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.metamodel.Attribute;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Compare;

@SuppressWarnings("serial")
public class SearchManager extends ConfirmDialog {

    protected ModulePop module;
    @SuppressWarnings("rawtypes")
    private LinkedHashMap<Attribute, Field> fieldMap;

    /**
     * Constructor
     */
    public SearchManager() {
        this(null);
    }

    /**
     * Constructor
     *
     * @param module the reference module
     */
    public SearchManager(ModulePop module) {
        super(null);
        this.module = module;

        init();
    }

    /**
     * Initialization
     */
    @SuppressWarnings("rawtypes")
    protected void init() {


        this.fieldMap = new LinkedHashMap<Attribute, Field>();

        if (module != null) {
            setCaption(module.getTitoloSearch());
        }

        // creates the form layout
        FormLayout layout = createFormLayout();

        // create the fields
        createFields(layout);

        // aggiunge i campi al layout
        addComponent(layout);
    }


    /**
     * Creates the FormLayout used to display the fields
     * <p>
     *
     * @return the FormLayout
     */
    protected FormLayout createFormLayout() {
        FormLayout layout = new AFormLayout();
        layout.setSpacing(true);
        return layout;
    }


    /**
     * The component shown in the toolbar area.
     */
    protected DialogToolbar createToolbarComponent() {
        return new SearchManagerToolbar();
    }

    /**
     * Returns the toolbar casted as SearchManagerToolbar
     * to access search options
     */
    protected SearchManagerToolbar getSearchToolbar() {
        return (SearchManagerToolbar) getToolbar();
    }

    /**
     * Return a JPA container composite Filter corresponding to the current search contitions
     *
     * @return the JPAContainer Filter
     */
    public Filter getFilter() {
        Filter outFilter = null;

        // create composite filter
        ArrayList<Filter> filters = createFilters();

        if (filters != null) {

            // removes null filters
            ArrayList<Filter> validFilters = new ArrayList<Filter>();
            for (Filter filter : filters) {
                if (filter != null) {
                    validFilters.add(filter);
                }
            }

            // concatenates the filters with the And clause
            if (validFilters.size() > 0) {
                Filter[] aFilters = validFilters.toArray(new Filter[0]);
                if (validFilters.size() > 1) {
                    outFilter = new And(aFilters);
                } else {
                    outFilter = aFilters[0];
                }
            }
        }

        return outFilter;
    }

    /**
     * Creates and adds the filters for each search field. Invoked before performing the search.
     *
     * @return an array of filters which will be concatenated with the And clause
     */
    public ArrayList<Filter> createFilters() {
        ArrayList<Filter> filters = new ArrayList<>();

        LinkedHashMap<Attribute, Field> fieldMap = getFieldMap();
        for (Map.Entry<Attribute, Field> entry : fieldMap.entrySet()) {
            Attribute attr = entry.getKey();
            Field field = entry.getValue();

            if (!field.isEmpty()) {
                Filter filter = createFilter(attr);
                if (filter != null) {
                    filters.add(filter);
                }
            }
        }

        return filters;
    }


    /**
     * crea un filtro per un Field
     * Usa il valore corrente del field
     *
     * @param attr l'attribute per recuperare il field dalla mappa
     * @return il filtro creato
     */
    protected Filter createFilter(Attribute attr) {
        Filter filter = null;
        Class clazz;

        if (attr != null) {
            clazz = attr.getJavaType();

            if (attr.isAssociation()) {
                filter = createBeanFilter(attr);
            }

            if (clazz.equals(String.class)) {
                if (LibField.getAnnotation(attr) != null) {
                    filter = createStringFilter(attr, LibField.getAnnotation(attr).search());
                } else {
                    filter = createStringFilter(attr, SearchType.CONTAINS);
                }// end of if/else cycle
            }// end of if cycle

            if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
                filter = createBoolFilter(attr);
            }

            if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
                filter = createIntegerFilter(attr);
            }

            if (clazz.equals(Date.class)) {
                filter = createDateFilter(attr);
            }

            // altre classi sono di difficile interpretazione a livello generale
            // e vanno gestite nelo specifico
            if (clazz.equals(BigDecimal.class)) {
            }


        }

        return filter;

    }


    /**
     * Create a filter for String searches.
     *
     * @param attr the StaticMetamodel attribute
     */
    protected Filter createStringFilter(Attribute attr) {
        return createStringFilter(attr, SearchType.STARTS_WITH);
    }

    /**
     * Create a filter for String searches using the STARTS_WITH search type.
     *
     * @param field the field containing the String value
     * @param attr  the StaticMetamodel attribute
     */
    protected Filter createStringFilter(Field field, Attribute attr) {
        return createStringFilter(field, attr, SearchType.STARTS_WITH);
    }

    /**
     * Create a filter for String searches.
     *
     * @param attr the StaticMetamodel attribute
     * @param type the search type
     */
    protected Filter createStringFilter(Attribute attr, SearchType type) {
        Field field = getFieldMap().get(attr);
        return createStringFilter(field, attr, type);
    }

    /**
     * Create a filter for String searches.
     *
     * @param field the field containing the value
     * @param attr  the StaticMetamodel attribute
     * @param type  the search type
     */
    protected Filter createStringFilter(Field field, Attribute attr, SearchType type) {
        Filter filter = null;
        Object value = field.getValue();
        if (value != null) {
            if (!value.equals("")) {
                switch (type) {
                    case STARTS_WITH:
                        filter = new Like(attr.getName(), value.toString() + "%", true);
                        break;
                    case CONTAINS:
                        filter = new SimpleStringFilter(attr.getName(), value.toString(), true, false);
                        break;
                    case MATCHES:
                        filter = new Compare.Equal(attr.getName(), value.toString());
                        break;
                    default:
                        break;
                }
            }
        }

        return filter;
    }


    /**
     * Create a filter for Boolean searches.
     *
     * @param attr the StaticMetamodel attribute
     */
    protected Filter createBoolFilter(Attribute attr) {
        Field field = getFieldMap().get(attr);
        return createBoolFilter(field, attr);
    }


    /**
     * Create a filter for Boolean searches.
     *
     * @param attr the StaticMetamodel attribute
     * @return the filter or null if no checkboxes selected
     */
    protected Filter createBoolFilter(Field field, Attribute attr) {
        Filter filter = null;
        Object value = field.getValue();
        if (value != null) {
            Boolean bool = Lib.getBool(value);
            filter = new Compare.Equal(attr.getName(), bool);
        }
        return filter;
    }


    /**
     * Create a filter for Integer searches.
     *
     * @param attr the StaticMetamodel attribute
     */
    protected Filter createIntegerFilter(Attribute attr) {
        Field field = getFieldMap().get(attr);
        return createIntegerFilter(field, attr);
    }

    /**
     * Create a filter for Integer searches.
     *
     * @param field the field containing the Integer value
     * @param attr  the StaticMetamodel attribute
     */
    protected Filter createIntegerFilter(Field field, Attribute attr) {
        Filter filter = null;
        Object value = field.getValue();
        int intVal = Lib.getInt(value);
        if (intVal != 0) {
            filter = new Compare.Equal(attr.getName(), Lib.getInt(value));
        }
        return filter;
    }

    /**
     * Create a filter for Date searches.
     *
     * @param attr the StaticMetamodel attribute
     */
    protected Filter createDateFilter(Attribute attr) {
        Field field = getFieldMap().get(attr);
        return createDateFilter(field, attr);
    }// end of method

    /**
     * Create a filter for Date searches.
     *
     * @param field the field containing the Date value
     * @param attr  the StaticMetamodel attribute
     */
    protected Filter createDateFilter(Field field, Attribute attr) {
        Container.Filter filter = null;
        Object value = field.getValue();
        Date dateVal = Lib.getDate(value);

        if (dateVal != null) {
            filter = new Compare.Equal(attr.getName(), dateVal);
        }// end of if cycle

        return filter;
    }// end of method

    /**
     * Create a filter for Combo Popup searches.
     *
     * @param attr the StaticMetamodel attribute
     */
    protected Filter createBeanFilter(Attribute attr) {
        RelatedComboField field = (RelatedComboField) getFieldMap().get(attr);
        return createBeanFilter(field, attr);
    }

    /**
     * Create a filter for Combo Popup searches.
     *
     * @param field the RelatedComboField field
     * @param attr  the StaticMetamodel attribute
     */
    protected Filter createBeanFilter(RelatedComboField field, Attribute attr) {
        return createBeanFilter(field, attr.getName());
    }


    /**
     * Create a filter for Combo Popup searches.
     *
     * @param field    the RelatedComboField
     * @param attrName the attribute name
     */
    protected Filter createBeanFilter(RelatedComboField field, String attrName) {
        Filter filter = null;
        Object value = field.getValue();
        if (value != null) {
            BaseEntity bean = AQuery.queryById(field.getEntityClass(), (long) value);
            filter = new Compare.Equal(attrName, bean);
        }
        return filter;
    }


    /**
     * Searches from the value retrieved from an Array.
     * <p>
     * It is assumed that the object returned by getValue() has a method named "getId()"
     * which returns an Integer. If not, null is returned.
     */
    protected Filter createArrayFilter(ArrayComboField field, Attribute attr) {
        Filter filter = null;
        Object value = field.getValue();

        if (value != null) {
            Class clazz = value.getClass();
            Method method;
            try {
                method = clazz.getMethod("getId");
                Object result = method.invoke(value);
                int key = Lib.getInt(result);
                if (key != 0) {
                    filter = new Compare.Equal(attr.getName(), key);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return filter;
    }

    /**
     * Returns the combine search option selected, or null if none selected.
     *
     * @return the combine search option
     */
    public CombineSearchOptions getCombineOption() {
        return getSearchToolbar().getCombineOption();
    }

    /**
     * Create and add a field for each property declared for this search
     * <p>
     * Crea e aggiunge i campi.
     * Implementazione di default nella superclasse. <br>
     * I campi vengono recuperati dal Modello (di default) <br>
     * I campi vengono creati del tipo grafico previsto nella Entity. <br>
     * Se si vuole aggiungere un campo (solo nel search e non nel Modello),
     * usare il metodo sovrascritto nella sottoclasse
     * invocando prima (o dopo) il metodo della superclasse.
     * <p>
     * Per sovrascrivere completamente questo metodo, creare i campi,
     * aggiungerli alla mappa con addField() e aggiungerli al layout passato nei parametri.
     * <code>
     * Field field = new TextField("cognome");
     * addField(Insegnante_.cognome.getName(), field);
     * layout.addComponent(field);
     * </code>
     */
    @SuppressWarnings("rawtypes")
    protected void createFields(FormLayout layout) {
        Attribute<?, ?>[] attributes = null;
        Field field = null;

        if (module != null) {
            attributes = getAttributesList();
            if (attributes != null) {
                // create a field for each attribute
                // and add it to the UI
                for (Attribute<?, ?> attr : attributes) {
                    field = creaField(attr);
                    if (field != null) {
                        addField(attr, field);
                        layout.addComponent(field);
                    }
                }
            }
        }

    }// end of method

    /**
     * Attributes used in this search
     * Di default prende dal modulo
     * Può essere sovrascritto se c'è un Search specifico
     *
     * @return a list containing all the attributes used in this search
     */
    protected Attribute<?, ?>[] getAttributesList() {
        return this.module.getFieldsSearch();
    }// end of method


    /**
     * Crea un campo del tipo corrispondente all'attributo dato.
     * I campi vengono creati del tipo grafico previsto nella Entity.
     */
    @SuppressWarnings("rawtypes")
    protected Field creaField(Attribute attr) {
        Field field = null;
        Class clazz = null;
        String caption = "";

        if (attr != null) {
            clazz = attr.getJavaType();

            if (attr.isAssociation()) {
                field = createCombo(clazz);
            }

            if (clazz.equals(String.class)) {
                field = new TextField();
            }

            if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
                field = new YesNoField();
            }

            if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
                field = new IntegerField();
            }

            if (clazz.equals(BigDecimal.class)) {
                field = new DecimalField();
            }

            if (clazz.equals(Date.class)) {
                field = new DateField();
            }

            if (field == null && !clazz.isEnum()) {
                field = new TextField();
            }
        }

        caption = DefaultFieldFactory.createCaptionByPropertyId(attr.getName());
        if (field != null) {
            field.setCaption(caption);
        }

        return field;
    }


    /**
     * Creates a Combo field to search in associated fields.
     *
     * @param clazz the related class
     * @return the combo field
     */
    protected RelatedComboField createCombo(Class clazz) {
        if (BaseEntity.class.isAssignableFrom(clazz)) {
            return new RelatedComboField(clazz);
        } else {
            return null;
        }// end of if/else cycle
    }

    /**
     * Aggiunge il campo alla mappa
     *
     * @param attr  - the key
     * @param field - the field
     */
    @SuppressWarnings("rawtypes")
    protected void addField(Attribute attr, Field field) {
        LinkedHashMap<Attribute, Field> fieldMap = getFieldMap();

        if (attr != null && field != null && fieldMap != null) {
            fieldMap.put(attr, field);
        }
    }

    /**
     * @return the fieldMap
     */
    @SuppressWarnings("rawtypes")
    public LinkedHashMap<Attribute, Field> getFieldMap() {
        return fieldMap;
    }


    /**
     * Retrieve a filed from the map by attribute
     *
     * @param attr the attribute
     * @return the field
     */
    public Field getField(Attribute attr) {
        return getFieldMap().get(attr);
    }

    @Override
    protected void onConfirm() {

        super.onConfirm();
    }


    protected Attribute getAttr(String fieldName) {
        Attribute attribute = null;
        ArrayList<Attribute<?, ?>> lista = module.getAttributesList();

        for (Attribute attr : lista) {
            if (attr.getName().equals(fieldName)) {
                attribute = attr;
            }// end of if cycle
        }// end of for cycle

        return attribute;
    }// end of constructor

    protected Field getField(String fieldName) {
        Field field = null;
        Attribute attr = getAttr(fieldName);

        if (attr != null) {
            field = getField(attr);
        }// end of if cycle

        return field;
    }// end of constructor

    protected Object getFieldValue(String fieldName) {
        Object value = null;
        Field field = getField(fieldName);

        if (field != null) {
            value = field.getValue();
        }// end of if cycle

        return value;
    }// end of constructor

    /**
     * Enum for search types
     */
    public enum SearchType {
        STARTS_WITH, CONTAINS, MATCHES
    }

    /**
     * Enum of available combine search options in the SearchManager dialog
     */
    public enum CombineSearchOptions {
        addToList("Aggiungi alla lista"), removeFromList("Rimuovi dalla lista"), searchInList("Cerca nella lista");

        private String text;

        private CombineSearchOptions(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

    }


}// end of class
