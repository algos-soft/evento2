package it.algos.webbase.web.query;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.EntityItemProperty;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Container;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Compare;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.lib.LibQuery;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility methods for queries.
 * Libreria da usare per tutte le query, quando l'applicazione NON USA la multiazienda (company)
 * Altrimenti si usa CompanyQuery
 * <p>
 * I metodi sono con o senza EntityManager
 */
public abstract class AQuery {

    //------------------------------------------------------------------------------------------------------------------------
    // Count records
    // Con e senza Property. Se la property è nulla, restituisce il numero di tutti i records
    // Con e senza EntityManager
    // Rimanda ad un unico metodo
    // @todo Funzionamento testato nel progetto MultyCompany.AQueryTest
    // Return int (non c'è motivo di usare un long come valore di ritorno)
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Numero totale di records della Entity specificata
     * Senza filtri.
     * Usa l'EntityManager di default
     *
     * @param clazz the Entity class
     *
     * @return il numero totale di records nella Entity
     */
    public static int count(Class<? extends BaseEntity> clazz) {
        return count(clazz, null, null, null);
    }// end of static method

    /**
     * Numero totale di records della Entity specificata
     * Senza filtri.
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param clazz   the Entity class
     * @param manager the EntityManager to use
     *
     * @return il numero totale di records nella Entity
     */
    public static int count(Class<? extends BaseEntity> clazz, EntityManager manager) {
        return count(clazz, null, null, manager);
    }// end of static method

    /**
     * Numero di records della Entity
     * Filtrati sul valore della property indicata
     * Se la property è nulla, restituisce il numero di tutti i records
     * Usa l'EntityManager di default
     *
     * @param clazz the Entity class
     * @param attr  the searched attribute
     * @param value the value to search for
     *
     * @return il numero filtrato di records nella Entity
     */
    public static int count(Class<? extends BaseEntity> clazz, SingularAttribute attr, Object value) {
        return count(clazz, attr, value, null);
    }// end of static method

    /**
     * Numero di records della Entity
     * Filtrati sul valore della property indicata
     * Se la property è nulla, restituisce il numero di tutti i records
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param clazz   the Entity class
     * @param attr    the searched attribute
     * @param value   the value to search for
     * @param manager the EntityManager to use
     *
     * @return il numero filtrato di records nella Entity
     */
//    @SuppressWarnings({"unchecked"})
    public static int count(Class<? extends BaseEntity> clazz, SingularAttribute attr, Object value, EntityManager manager) {
        Container.Filter filter = null;
        ArrayList<Container.Filter> filtroArray = null;

        // aggiunge il vincolo della Property (SingularAttribute attr), trasformato in filtro
        if (attr != null) {
            filtroArray = new ArrayList<>();
            filter = new Compare.Equal(attr.getName(), value);
            filtroArray.add(filter);
        }// end of if cycle

        return count(clazz, manager, filter);
    }// end of static method

    public static int count(Class<? extends BaseEntity> clazz, Filter... filters) {
        return count(clazz, (EntityManager) null, filters);
    }// end of static method

    /**
     * Numero di records della Entity
     * Filtrati sui filtri (eventuali) passati come parametro
     * I filtri sono additivi (ADD) l'uno con l'altro
     * Se mancano i filtri, restituisce il numero di tutti i records
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param clazz   the Entity class
     * @param manager the EntityManager to use
     * @param filters an array of filters (you can use FilterFactory to build filters, or create them as Compare....)
     *
     * @return il numero filtrato di records nella Entity
     */
    public static int count(Class<? extends BaseEntity> clazz, EntityManager manager, Filter... filters) {
        return count(clazz, manager, new ArrayList<>(Arrays.asList(filters)));
    }// end of static method

    /**
     * Numero di records della Entity
     * Filtrati sui filtri (eventuali) passati come parametro
     * I filtri sono additivi (ADD) l'uno con l'altro
     * Se mancano i filtri, restituisce il numero di tutti i records
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param clazz   the Entity class
     * @param manager the EntityManager to use
     * @param filters an array of filters (you can use FilterFactory to build filters, or create them as Compare....)
     *
     * @return il numero filtrato di records nella Entity
     */
    public static int count(Class<? extends BaseEntity> clazz, EntityManager manager, ArrayList<Filter> filters) {
        int count = 0;
        JPAContainer<BaseEntity> container;

        // se non specificato l'EntityManager, ne crea uno locale
        boolean usaManagerLocale = false;
        if (manager == null) {
            usaManagerLocale = true;
            manager = EM.createEntityManager();
        }// end of if cycle

        // create a read-only JPA container for a given domain class (eventually sorted) and filters (eventually)
        container = getContainerRead(clazz, manager, filters);

        // recupera le dimensioni del container
        if (container.getItemIds() != null) {
            count = container.getItemIds().size();
        }// end of if cycle

        // eventualmente chiude l'EntityManager locale
        if (usaManagerLocale) {
            manager.close();
        }// end of if cycle

        return count;
    }// end of static method

    //------------------------------------------------------------------------------------------------------------------------
    // Find single entity by primary key
    // Con e senza EntityManager
    // Rimanda ad un unico metodo
    // @todo Funzionamento testato nel progetto MultyCompany.AQueryTest
    // Parametro in ingresso di tipo long (all Algos primary key are long)
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Searches for a single entity by standard Primary Key (all Algos primary key are long)
     * Multiple entities cannot exist, the primary key is unique.
     * Usa l'EntityManager di default
     *
     * @param clazz the Entity class
     * @param id    the item long id (unico) della Primary Key
     *
     * @return the entity corresponding to the key, or null
     */
    public static BaseEntity find(Class<? extends BaseEntity> clazz, long id) {
        return find(clazz, id, null);
    }// end of static method

    /**
     * Searches for a single entity by standard Primary Key (all Algos primary key are long)
     * Multiple entities cannot exist, the primary key is unique.
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param clazz   the Entity class
     * @param id      the item long id (unico) della Primary Key
     * @param manager the EntityManager to use
     *
     * @return the entity corresponding to the key, or null
     */
    public static BaseEntity find(Class<? extends BaseEntity> clazz, long id, EntityManager manager) {
        BaseEntity entity = null;

        // se non specificato l'EntityManager, ne crea uno locale
        boolean usaManagerLocale = false;
        if (manager == null) {
            usaManagerLocale = true;
            manager = EM.createEntityManager();
        }// end of if cycle

        entity = manager.find(clazz, id);

        // se usato, chiude l'EntityManager locale
        if (usaManagerLocale) {
            manager.close();
        }// end of if cycle

        return entity;
    }// end of static method

    //------------------------------------------------------------------------------------------------------------------------
    // Find single entity by SingularAttribute
    // Con e senza EntityManager
    // Rimanda ad un unico metodo
    // @todo Funzionamento testato nel progetto MultyCompany.AQueryTest
    // Return una entity di classe BaseEntity (casting specifico nel metodo chiamante)
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Search for a single entity with a specified attribute value.
     * If multiple entities exist, null is returned.
     * Usa l'EntityManager di default
     *
     * @param clazz the Entity class
     * @param attr  the searched attribute
     * @param value the value to search for
     *
     * @return the only entity corresponding to the specified criteria, or null
     */
    public static BaseEntity getEntity(Class<? extends BaseEntity> clazz, SingularAttribute attr, Object value) {
        return getEntity(clazz, attr, value, null);
    }// end of static method

    /**
     * Search for a single entity with a specified attribute value.
     * If multiple entities exist, null is returned.
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param clazz   the Entity class
     * @param attr    the searched attribute
     * @param value   the value to search for
     * @param manager the EntityManager to use
     *
     * @return the only entity corresponding to the specified criteria, or null
     */
    @SuppressWarnings({"unchecked"})
    public static BaseEntity getEntity(Class<? extends BaseEntity> clazz, SingularAttribute attr, Object value, EntityManager manager) {
        BaseEntity entity = null;

        // the specified attribute is mandatory
        if (attr == null) {
            return null;
        }// end of if cycle

        // se non specificato l'EntityManager, ne crea uno locale
        boolean usaManagerLocale = false;
        if (manager == null) {
            usaManagerLocale = true;
            manager = EM.createEntityManager();
        }// end of if cycle

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<? extends BaseEntity> criteria = builder.createQuery(clazz);
        Root<? extends BaseEntity> root = criteria.from(clazz);

        // questo predicato è obbligatorio
        Predicate pred = builder.equal(root.get(attr), value);
        criteria.where(pred);
        TypedQuery<? extends BaseEntity> query = manager.createQuery(criteria);

        try { // prova ad eseguire il codice
            entity = query.getSingleResult();
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        // se usato, chiude l'EntityManager locale
        if (usaManagerLocale) {
            manager.close();
        }// end of if cycle

        return entity;
    }// end of static method

    public static BaseEntity getEntity(Class<? extends BaseEntity> clazz, Filter... filters) {
        return getEntity(clazz, (EntityManager) null, filters);
    }// end of static method

    public static BaseEntity getEntity(Class<? extends BaseEntity> clazz, EntityManager manager, Filter... filters) {
        return getEntity(clazz, manager, filters != null ? new ArrayList<>(Arrays.asList(filters)) : null);
    }// end of static method

    /**
     * Search for a single entity.
     * Filtrate sui filtri passati come parametro
     * I filtri sono additivi (ADD) l'uno con l'altro
     * If multiple entities exist, null is returned.
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param clazz   the Entity class
     * @param manager the EntityManager to use
     * @param filters an array of filters (you can use FilterFactory to build filters, or create them as Compare....)
     *
     * @return the only entity corresponding to the specified criteria, or null
     */
    public static BaseEntity getEntity(Class<? extends BaseEntity> clazz, EntityManager manager, ArrayList<Filter> filters) {
        BaseEntity entity = null;
        JPAContainer<BaseEntity> container;
        EntityItem<BaseEntity> item;

        // se non specificato l'EntityManager, ne crea uno locale
        boolean usaManagerLocale = false;
        if (manager == null) {
            usaManagerLocale = true;
            manager = EM.createEntityManager();
        }// end of if cycle

        // create a read-only JPA container for a given domain class (eventually sorted) and filters (eventually)
        container = getContainerRead(clazz, manager, filters);

        // il container deve contenere solo un item perchè la richiesta sia valida
        if (container != null && container.getItemIds() != null && container.getItemIds().size() == 1) {
            for (Object id : container.getItemIds()) {
                item = container.getItem(id);
                entity = item.getEntity();
            }// end of for cycle
        }// end of if cycle

        // eventualmente chiude l'EntityManager locale
        if (usaManagerLocale) {
            manager.close();
        }// end of if cycle

        return entity;
    }// end of static method

    //------------------------------------------------------------------------------------------------------------------------
    // Find entities (list)
    // Con e senza Property (SingularAttribute)
    // Con e senza Sort
    // Con e senza Filters
    // Con e senza EntityManager
    // Rimanda ad un unico metodo
    // @todo Funzionamento testato nel progetto MultyCompany.AQueryTest
    // Return una List di oggetti BaseEntity (casting specifico nel metodo chiamante)
    //------------------------------------------------------------------------------------------------------------------------

    public static List<? extends BaseEntity> getList(Class<? extends BaseEntity> clazz) {
        return getList(clazz, (SortProperty) null, (EntityManager) null, (ArrayList<Filter>) null);
    }// end of static method

    public static List<? extends BaseEntity> getList(Class<? extends BaseEntity> clazz, EntityManager manager) {
        return getList(clazz, (SortProperty) null, manager, (ArrayList<Filter>) null);
    }// end of static method

    public static List<? extends BaseEntity> getList(Class<? extends BaseEntity> clazz, SingularAttribute attr, Object value) {
        return getList(clazz, attr, value, null, (EntityManager) null);
    }// end of static method

    public static List<? extends BaseEntity> getList(Class<? extends BaseEntity> clazz, Filter... filters) {
        return getList(clazz, (SortProperty) null, (EntityManager) null, filters);
    }// end of static method

    public static List<? extends BaseEntity> getList(Class<? extends BaseEntity> clazz, SortProperty sorts) {
        return getList(clazz, sorts, (EntityManager) null, (ArrayList<Filter>) null);
    }// end of static method

    public static List<? extends BaseEntity> getList(Class<? extends BaseEntity> clazz, EntityManager manager, Filter... filters) {
        return getList(clazz, (SortProperty) null, manager, filters);
    }// end of static method

    public static List<? extends BaseEntity> getList(Class<? extends BaseEntity> clazz, SingularAttribute attr, Object value, EntityManager manager) {
        return getList(clazz, attr, value, (SortProperty) null, manager);
    }// end of static method

    public static List<? extends BaseEntity> getList(Class<? extends BaseEntity> clazz, SortProperty sorts, Filter... filters) {
        return getList(clazz, sorts, (EntityManager) null, filters);
    }// end of static method

    public static List<? extends BaseEntity> getList(Class<? extends BaseEntity> clazz, SingularAttribute attr, Object value, SortProperty sorts) {
        return getList(clazz, attr, value, sorts, (EntityManager) null);
    }// end of static method

    public static List<? extends BaseEntity> getList(Class<? extends BaseEntity> clazz, SingularAttribute attr, Object value, SortProperty sorts, EntityManager manager) {
        Container.Filter filter = null;

        // aggiunge il vincolo della Property (SingularAttribute attr), trasformato in filtro
        if (attr != null) {
            filter = new Compare.Equal(attr.getName(), value);
        }// end of if cycle

        return getList(clazz, sorts, manager, filter);
    }// end of static method

    public static List<? extends BaseEntity> getList(Class<? extends BaseEntity> clazz, SortProperty sorts, EntityManager manager, Filter... filters) {
        return getList(clazz, sorts, manager, new ArrayList<>(Arrays.asList(filters)));
    }// end of static method

    /**
     * Search for the entities of a given Entity class
     * Filtrate sul valore (eventuale) della property indicata
     * Filtrate sui filtri (eventuali) passati come parametro
     * Sia la property che i filtri sono additivi (ADD) l'uno con l'altro
     * Se manca sia la property sia i filtri, restituisce tutte le entities della Entity
     * Ordinate secondo il filtro
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param clazz   the Entity class
     * @param sorts   ordinamento (ascendente o discendente)
     * @param manager the EntityManager to use
     * @param filters an array of filters (you can use FilterFactory to build filters, or create them as Compare....)
     *
     * @return a list of founded entities, null if there are no entities
     */
    public static List<? extends BaseEntity> getList(Class<? extends BaseEntity> clazz, SortProperty sorts, EntityManager manager, ArrayList<Filter> filters) {
        ArrayList<BaseEntity> entities = new ArrayList<>();
        JPAContainer<BaseEntity> container;
        EntityItem<BaseEntity> item;

        // se non specificato l'EntityManager, ne crea uno locale
        boolean usaManagerLocale = false;
        if (manager == null) {
            usaManagerLocale = true;
            manager = EM.createEntityManager();
        }// end of if cycle

        // create a read-only JPA container for a given domain class (eventually sorted) and filters (eventually)
        container = getContainerRead(clazz, sorts, manager, filters);

        // costruisce la lista, spazzolando il container
        for (Object id : container.getItemIds()) {
            item = container.getItem(id);
            entities.add(item.getEntity());
        }// end of for cycle

        // eventualmente chiude l'EntityManager locale
        if (usaManagerLocale) {
            manager.close();
        }// end of if cycle

        return entities;
    }// end of static method

    //------------------------------------------------------------------------------------------------------------------------
    // Find properties (list)
    // Con e senza EntityManager
    // Rimanda ad un unico metodo
    // @todo Funzionamento testato nel progetto MultyCompany.AQueryTest
    // Return una List di oggetti del tipo specificato (str, int, ecc)
    //------------------------------------------------------------------------------------------------------------------------
    public static List<? extends BaseEntity> getListProperty(Class<? extends BaseEntity> clazz, SingularAttribute attr, EntityManager manager, Filter... filters) {
        ArrayList<BaseEntity> entities = new ArrayList<>();
        BaseEntity value;
        SortProperty sort;
        JPAContainer<BaseEntity> container;
        EntityItem<BaseEntity> item;
        EntityItemProperty property;
        Object objValue;

        // se non specificato l'EntityManager, ne crea uno locale
        boolean usaManagerLocale = false;
        if (manager == null) {
            usaManagerLocale = true;
            manager = EM.createEntityManager();
        }// end of if cycle

        // create a read-only JPA container for a given domain class (not sorted) and filters
        container = AQuery.getContainerRead(clazz, null, manager, new ArrayList<>(Arrays.asList(filters)));

        // costruisce la lista, spazzolando il container
        for (Object id : container.getItemIds()) {
            item = container.getItem(id);
            property = item.getItemProperty(attr.getName());
            objValue = property.getValue();

            if (objValue instanceof BaseEntity) {
                value = (BaseEntity) objValue;
                entities.add(value);
            }// end of if cycle
        }// end of for cycle

        // eventualmente chiude l'EntityManager locale
        if (usaManagerLocale) {
            manager.close();
        }// end of if cycle

        return entities;
    }// end of static method

    public static List<String> getListStr(Class<? extends BaseEntity> clazz, SingularAttribute attr) {
        return getListStr(clazz, attr, true, (EntityManager) null, (Filter) null);
    }// end of static method

    public static List<String> getListStr(Class<? extends BaseEntity> clazz, SingularAttribute attr, boolean asc) {
        return getListStr(clazz, attr, asc, (EntityManager) null, (Filter) null);
    }// end of static method

    public static List<String> getListStr(Class<? extends BaseEntity> clazz, SingularAttribute attr, EntityManager manager) {
        return getListStr(clazz, attr, true, manager, (Filter) null);
    }// end of static method

    public static List<String> getListStr(Class<? extends BaseEntity> clazz, SingularAttribute attr, Filter... filters) {
        return getListStr(clazz, attr, true, (EntityManager) null, filters);
    }// end of static method

    public static List<String> getListStr(Class<? extends BaseEntity> clazz, SingularAttribute attr, boolean asc, EntityManager manager, Filter... filters) {
        return getListStr(clazz, attr, new SortProperty(attr, asc), manager, new ArrayList<>(Arrays.asList(filters)));
    }// end of static method

    public static List<String> getListStr(Class<? extends BaseEntity> clazz, SingularAttribute attr, boolean asc, EntityManager manager, ArrayList<Filter> filters) {
        return getListStr(clazz, attr, new SortProperty(attr, asc), manager, filters);
    }// end of static method

    public static List<String> getListStr(Class<? extends BaseEntity> clazz, SingularAttribute attr, SortProperty sort, EntityManager manager, Filter... filters) {
        return getListStr(clazz, attr, sort, manager, new ArrayList<>(Arrays.asList(filters)));
    }// end of static method

    /**
     * Search for the values of a given property of the given Entity class
     * Ordinate sul valore della property indicata
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param clazz   the Entity class
     * @param attr    the searched attribute
     * @param sort     ordinamento (ascendente o discendente)
     * @param manager the EntityManager to use
     * @param filters an array of filters (you can use FilterFactory to build filters, or create them as Compare....)
     *
     * @return a list of founded values of the specified type, null if there are no entities
     */
    public static List<String> getListStr(Class<? extends BaseEntity> clazz, SingularAttribute attr, SortProperty sort, EntityManager manager, ArrayList<Filter> filters) {
        ArrayList<String> properties = new ArrayList<>();
        String value;
        JPAContainer<BaseEntity> container;
        EntityItem<BaseEntity> item;
        EntityItemProperty property;
        Object objValue;

        // se non specificato l'EntityManager, ne crea uno locale
        boolean usaManagerLocale = false;
        if (manager == null) {
            usaManagerLocale = true;
            manager = EM.createEntityManager();
        }// end of if cycle

        // create a read-only JPA container for a given domain class (eventually sorted) and filters
        container = AQuery.getContainerRead(clazz, sort, manager, filters);

        // costruisce la lista, spazzolando il container
        for (Object id : container.getItemIds()) {
            item = container.getItem(id);
            property = item.getItemProperty(attr.getName());
            objValue = property.getValue();

            if (objValue instanceof String) {
                value = (String) objValue;
                properties.add(value);
            }// end of if cycle
        }// end of for cycle

        // eventualmente chiude l'EntityManager locale
        if (usaManagerLocale) {
            manager.close();
        }// end of if cycle

        return properties;
    }// end of static method



    public static List<Integer> getListInt(Class<? extends BaseEntity> clazz, SingularAttribute attr) {
        return getListInt(clazz, attr, true, (EntityManager) null, (Filter) null);
    }// end of static method

    public static List<Integer> getListInt(Class<? extends BaseEntity> clazz, SingularAttribute attr, boolean asc) {
        return getListInt(clazz, attr, asc, (EntityManager) null, (Filter) null);
    }// end of static method

    public static List<Integer> getListInt(Class<? extends BaseEntity> clazz, SingularAttribute attr, EntityManager manager) {
        return getListInt(clazz, attr, true, manager, (Filter) null);
    }// end of static method

    public static List<Integer> getListInt(Class<? extends BaseEntity> clazz, SingularAttribute attr, boolean asc, EntityManager manager, Filter... filters) {
        return getListInt(clazz, attr, asc, manager, new ArrayList<>(Arrays.asList(filters)));
    }// end of static method

    /**
     * Search for the values of a given property of the given Entity class
     * Ordinate sul valore della property indicata
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param clazz   the Entity class
     * @param attr    the searched attribute
     * @param asc     ordinamento (ascendente o discendente)
     * @param manager the EntityManager to use
     * @param filters an array of filters (you can use FilterFactory to build filters, or create them as Compare....)
     *
     * @return a list of founded values of the specified type, null if there are no entities
     */
    public static List<Integer> getListInt(Class<? extends BaseEntity> clazz, SingularAttribute attr, boolean asc, EntityManager manager, ArrayList<Filter> filters) {
        ArrayList<Integer> entities = new ArrayList<>();
        int value;
        SortProperty sort;
        JPAContainer<BaseEntity> container;
        EntityItem<BaseEntity> item;
        EntityItemProperty property;
        Object objValue;

        // se non specificato l'EntityManager, ne crea uno locale
        boolean usaManagerLocale = false;
        if (manager == null) {
            usaManagerLocale = true;
            manager = EM.createEntityManager();
        }// end of if cycle

        // ordinamento
        sort = new SortProperty(attr, asc);

        // create a read-only JPA container for a given domain class (eventually sorted) and filters
        container = AQuery.getContainerRead(clazz, sort, manager, filters);

        // costruisce la lista, spazzolando il container
        for (Object id : container.getItemIds()) {
            item = container.getItem(id);
            property = item.getItemProperty(attr.getName());
            objValue = property.getValue();

            if (objValue instanceof Integer) {
                value = (Integer) objValue;
                entities.add(value);
            }// end of if cycle
        }// end of for cycle

        // eventualmente chiude l'EntityManager locale
        if (usaManagerLocale) {
            manager.close();
        }// end of if cycle

        return entities;
    }// end of static method

    public static List<Long> getListLong(Class<? extends BaseEntity> clazz, SingularAttribute attr) {
        return getListLong(clazz, attr, true, (EntityManager) null, (Filter) null);
    }// end of static method

    public static List<Long> getListLong(Class<? extends BaseEntity> clazz, SingularAttribute attr, boolean asc) {
        return getListLong(clazz, attr, asc, (EntityManager) null, (Filter) null);
    }// end of static method

    public static List<Long> getListLong(Class<? extends BaseEntity> clazz, SingularAttribute attr, EntityManager manager) {
        return getListLong(clazz, attr, true, manager, (Filter) null);
    }// end of static method

    public static List<Long> getListLong(Class<? extends BaseEntity> clazz, SingularAttribute attr, boolean asc, EntityManager manager, Filter... filters) {
        return getListLong(clazz, attr, asc, manager, new ArrayList<>(Arrays.asList(filters)));
    }// end of static method

    /**
     * Search for the values of a given property of the given Entity class
     * Ordinate sul valore della property indicata
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param clazz   the Entity class
     * @param attr    the searched attribute
     * @param asc     ordinamento (ascendente o discendente)
     * @param manager the EntityManager to use
     * @param filters an array of filters (you can use FilterFactory to build filters, or create them as Compare....)
     *
     * @return a list of founded values of the specified type, null if there are no entities
     */
    public static List<Long> getListLong(Class<? extends BaseEntity> clazz, SingularAttribute attr, boolean asc, EntityManager manager, ArrayList<Filter> filters) {
        ArrayList<Long> entities = new ArrayList<>();
        long value;
        SortProperty sort;
        JPAContainer<BaseEntity> container;
        EntityItem<BaseEntity> item;
        EntityItemProperty property;
        Object objValue;

        // se non specificato l'EntityManager, ne crea uno locale
        boolean usaManagerLocale = false;
        if (manager == null) {
            usaManagerLocale = true;
            manager = EM.createEntityManager();
        }// end of if cycle

        // ordinamento
        sort = new SortProperty(attr, asc);

        // create a read-only JPA container for a given domain class (eventually sorted) and filters
        container = AQuery.getContainerRead(clazz, sort, manager, filters);

        // costruisce la lista, spazzolando il container
        for (Object id : container.getItemIds()) {
            item = container.getItem(id);
            property = item.getItemProperty(attr.getName());
            objValue = property.getValue();

            if (objValue instanceof Long) {
                value = (Long) objValue;
                entities.add(value);
            }// end of if cycle
        }// end of for cycle

        // eventualmente chiude l'EntityManager locale
        if (usaManagerLocale) {
            manager.close();
        }// end of if cycle

        return entities;
    }// end of static method

    //------------------------------------------------------------------------------------------------------------------------
    // Delete entities
    // Con e senza Property (SingularAttribute)
    // Con e senza Filters
    // Con e senza EntityManager
    // Rimanda a DUE metodi: Filters e CriteriaDelete
    // @todo Funzionamento testato nel progetto MultyCompany.AQueryTest
    // Ritorna il numero di records cancellati
    //------------------------------------------------------------------------------------------------------------------------
    public static int delete(Class<? extends BaseEntity> clazz) {
        return deleteBulk(clazz, (SingularAttribute) null, null, (EntityManager) null);
    }// end of static method

    public static int delete(Class<? extends BaseEntity> clazz, EntityManager manager) {
        return deleteBulk(clazz, (SingularAttribute) null, null, manager);
    }// end of static method

    public static int delete(Class<? extends BaseEntity> clazz, SingularAttribute attr, Object value) {
        return deleteBulk(clazz, attr, value, (EntityManager) null);
    }// end of static method

    public static int delete(Class<? extends BaseEntity> clazz, SingularAttribute attr, Object value, EntityManager manager) {
        return deleteBulk(clazz, attr, value, manager);
    }// end of static method

    public static int delete(Class<? extends BaseEntity> clazz, Filter... filters) {
        return delete(clazz, null, null, (EntityManager) null, filters);
    }// end of static method

    public static int delete(Class<? extends BaseEntity> clazz, EntityManager manager, Filter... filters) {
        return delete(clazz, null, null, manager, filters);
    }// end of static method

    /**
     * Bulk delete records with CriteriaDelete
     * <p>
     * Usa una Property (SingularAttribute)
     * If the provided EntityManager has an active transaction, the operation is performed
     * inside the transaction.<br>
     * Otherwise, a new transaction is used to delete this single entity.
     *
     * @param clazz   the Entity class
     * @param attr    the searched attribute
     * @param value   the value to search for
     * @param manager the entity manager to use (if null, a new one is created on the fly)
     *
     * @return il numero di records cancellati
     */
    @SuppressWarnings("unchecked")
    private static int deleteBulk(Class<? extends BaseEntity> clazz, SingularAttribute attr, Object value, EntityManager manager) {
        int deleted = 0;
        CriteriaBuilder builder;

        // se non specificato l'EntityManager, ne crea uno locale
        boolean usaManagerLocale = false;
        if (manager == null) {
            manager = EM.createEntityManager();
            usaManagerLocale = true;
        }// end of if cycle

        //--controlla se la transazione è attiva
        boolean createTransaction;
        createTransaction = LibQuery.isTransactionNotActive(manager);

        // create delete
        builder = manager.getCriteriaBuilder();
        CriteriaDelete delete = builder.createCriteriaDelete(clazz);

        // set the root class
        Root root = delete.from(clazz);

        // set where clause
        if (attr != null) {
            Predicate pred = builder.equal(root.get(attr), value);
            delete.where(pred);
        }// end of if cycle

        // perform update
        try {
            if (createTransaction) {
                manager.getTransaction().begin();
            }// end of if cycle

            deleted = manager.createQuery(delete).executeUpdate();

            if (createTransaction) {
                manager.getTransaction().commit();
            }// end of if cycle
        } catch (Exception e) {
            if (createTransaction) {
                manager.getTransaction().rollback();
            }// end of if cycle
            deleted = 0;
        }// fine del blocco try-catch

        // eventualmente chiude l'EntityManager locale
        if (usaManagerLocale) {
            manager.close();
        }// end of if cycle

        return deleted;
    }// end of method

    public static int delete(Class<? extends BaseEntity> clazz, SingularAttribute attr, Object value, EntityManager manager, Filter... filters) {
        return delete(clazz, attr, value, manager, new ArrayList<>(Arrays.asList(filters)));
    }// end of static method

    /**
     * Delete the records for a given domain class
     * <p>
     * Usa una Property (SingularAttribute)
     * Usa i Filters
     * Sia la property che i filtri sono additivi (ADD) l'uno con l'altro
     * If the provided EntityManager has an active transaction, the operation is performed
     * inside the transaction.<br>
     * Otherwise, a new transaction is used to delete this single entity.
     *
     * @param clazz   the Entity class
     * @param attr    the searched attribute
     * @param value   the value to search for
     * @param manager the entity manager to use (if null, a new one is created on the fly)
     * @param filters an array of filters (you can use FilterFactory to build filters, or create them as Compare....)
     *
     * @return il numero di records cancellati
     */
    @SuppressWarnings("unchecked")
    public static int delete(Class<? extends BaseEntity> clazz, SingularAttribute attr, Object value, EntityManager manager, ArrayList<Filter> filters) {
        int deleted = 0;
        Container.Filter filter;
        JPAContainer<BaseEntity> container;
        BaseEntity entity;
        boolean usaManagerLocale = false;
        boolean createTransaction = false;

        // se non specificato l'EntityManager, ne crea uno locale
        if (manager == null) {
            usaManagerLocale = true;
            manager = EM.createEntityManager();
        }// end of if cycle

        //--controlla se la transazione è attiva
        createTransaction = LibQuery.isTransactionNotActive(manager);

        // aggiunge il vincolo della Property (SingularAttribute attr), trasformato in filtro
        if (attr != null) {
            filter = new Compare.Equal(attr.getName(), value);
            if (filters == null) {
                filters = new ArrayList<>();
            }// end of if cycle
            filters.add(filter);
        }// end of if cycle

        // create a write JPA container for a given domain class and filters
        container = getContainerWrite(clazz, manager, filters);

        try {
            if (createTransaction) {
                manager.getTransaction().begin();
            }// end of if cycle

            for (Object id : container.getItemIds()) {
                entity = container.getItem(id).getEntity();
                entity = manager.merge(entity);
                manager.remove(entity);
                deleted++;
            }// end of for cycle

            if (createTransaction) {
                manager.getTransaction().commit();
            }// end of if cycle
        } catch (Exception e) {
            // rollback transaction
            if (createTransaction) {
                manager.getTransaction().rollback();
            }// end of if cycle
            deleted = 0;
        }// fine del blocco try-catch

        // eventualmente chiude l'EntityManager locale
        if (usaManagerLocale) {
            manager.close();
        }// end of if cycle

        return deleted;
    }// end of static method

    //------------------------------------------------------------------------------------------------------------------------
    // utilities
    //------------------------------------------------------------------------------------------------------------------------

    public static JPAContainer<BaseEntity> getContainerRead(Class<? extends BaseEntity> clazz, EntityManager manager, ArrayList<Filter> filters) {
        return getContainerRead(clazz, (SortProperty) null, manager, filters);
    }// end of static method

    /**
     * Create a read-only JPA container for a given domain class and filters.
     * <p>
     * Il manager NON può essere nullo
     * Il manager DEVE essere valido (must be close by caller method)
     *
     * @param clazz   the Entity class
     * @param sorts   ordinamento (ascendente o discendente)
     * @param manager the EntityManager to use
     * @param filters an array of filters (you can use FilterFactory to build filters, or create them as Compare....)
     *
     * @return the JPA container
     */
    @SuppressWarnings("unchecked")
    public static JPAContainer<BaseEntity> getContainerRead(Class<? extends BaseEntity> clazz, SortProperty sorts, EntityManager manager, ArrayList<Filter> filters) {
        JPAContainer<BaseEntity> container = (JPAContainer<BaseEntity>) JPAContainerFactory.makeNonCachedReadOnly(clazz, manager);

        if (filters != null) {
            for (Filter filter : filters) {
                container.addContainerFilter(filter);
            }// end of for cycle
        }// end of if cycle

        if (sorts != null) {
            for (String stringa : sorts.getProperties()) {
                if (stringa.contains(".")) {
                    container.addNestedContainerProperty(stringa.substring(0, stringa.lastIndexOf(".")) + ".*");
                }// end of if cycle
            }// end of for cycle
            container.sort(sorts.getProperties(), sorts.getOrdinamenti());
        }// end of if cycle

        return container;
    }// end of static method

    /**
     * Create a write JPA container for a given domain class and filters.
     * <p>
     * Il manager NON può essere nullo
     * Il manager DEVE essere valido (must be close by caller method)
     *
     * @param clazz   the Entity class
     * @param manager the EntityManager to use
     * @param filters an array of filters (you can use FilterFactory to build filters, or create them as Compare....)
     *
     * @return the JPA container
     */
    @SuppressWarnings("unchecked")
    public static JPAContainer<BaseEntity> getContainerWrite(Class<? extends BaseEntity> clazz, EntityManager manager, ArrayList<Container.Filter> filters) {
        JPAContainer<BaseEntity> container = (JPAContainer<BaseEntity>) JPAContainerFactory.make(clazz, manager);

        if (filters != null) {
            for (Container.Filter filter : filters) {
                container.addContainerFilter(filter);
            }// end of for cycle
        }// end of if cycle

        return container;
    }// end of static method

    /**
     * Recupera il massimo valore intero di una property di una Entity
     * Lista ordinata discendente
     * Recupera il primo valore
     * Usa l'EntityManager di default
     *
     * @param clazz the Entity class
     *
     * @return il massimo numero della property, 0 se non ce ne sono
     */
    @SuppressWarnings("unchecked")
    public static int maxInt(Class<? extends BaseEntity> clazz, SingularAttribute attr) {
        return maxInt(clazz, attr, null);
    }// end of method

    /**
     * Recupera il massimo valore intero di una property di una Entity
     * Lista ordinata discendente
     * Recupera il primo valore
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param clazz   the Entity class
     * @param attr    the searched attribute
     * @param manager the EntityManager to use
     *
     * @return il massimo numero della property, 0 se non ce ne sono
     */
    @SuppressWarnings("unchecked")
    public static int maxInt(Class<? extends BaseEntity> clazz, SingularAttribute attr, EntityManager manager) {
        int max = 0;
        List<Integer> lista;

        lista = AQuery.getListInt(clazz, attr, false, manager);

        if (lista != null && lista.size() > 0) {
            max = lista.get(0);
        }// end of if cycle

        return max;
    }// end of method

    //------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------------
    // deprecated
    //------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Search for the all entities of a given Entity class
     * Senza filtri.
     *
     * @param clazz the Entity class
     *
     * @return a list of all entities
     * @deprecated
     */
    public static List<? extends BaseEntity> findAll(Class<? extends BaseEntity> clazz) {
        List<? extends BaseEntity> entities;

        EntityManager manager = EM.createEntityManager();
        entities = findAll(clazz, manager);
        manager.close();

        return entities;
    }// end of method

    /**
     * Search for the all entities of a given Entity class
     * Senza filtri.
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param clazz   the Entity class
     * @param manager the EntityManager to use
     *
     * @return a list of all entities
     * @deprecated
     */
    public static List<? extends BaseEntity> findAll(Class<? extends BaseEntity> clazz, EntityManager manager) {
        List<? extends BaseEntity> entities;

        // se non specificato l'EntityManager, ne crea uno locale
        boolean usaManagerLocale = false;
        if (manager == null) {
            usaManagerLocale = true;
            manager = EM.createEntityManager();
        }// end of if cycle

        CriteriaBuilder builder;
        CriteriaQuery<? extends BaseEntity> criteria;
        TypedQuery<? extends BaseEntity> query;

        builder = manager.getCriteriaBuilder();
        criteria = builder.createQuery(clazz);
        query = manager.createQuery(criteria);
        entities = query.getResultList();

        // eventualmente chiude l'EntityManager locale
        if (usaManagerLocale) {
            manager.close();
        }// end of if cycle

        return entities;
    }// end of method

    /**
     * Return a list of entities for a given domain class and filters.
     * Filtrato coi filtri indicati
     *
     * @param clazz   the Entity class
     * @param sorts   an array of sort wrapper
     * @param filters an array of filters (you can use FilterFactory to build filters, or create them as Compare....)
     *
     * @return the list of founded entities
     * @deprecated
     */
    public static List<? extends BaseEntity> findAll(Class<? extends BaseEntity> clazz, SortProperty sorts, Filter... filters) {
        return findAll(clazz, sorts, filters);
    }// end of method

    /**
     * Delete all the records for a given domain class
     *
     * @param clazz the entity class
     *
     * @deprecated
     */
    public static void deleteAll(Class<? extends BaseEntity> clazz) {
        EntityManager manager = EM.createEntityManager();
        deleteAll(clazz, manager);
        manager.close();
    }// end of method

    /**
     * Delete all the records for a given domain class
     *
     * @param clazz   the entity class
     * @param manager the EntityManager to use
     *
     * @deprecated
     */
    public static void deleteAll(Class<? extends BaseEntity> clazz, EntityManager manager) {
        deleteOld(clazz, null, null, manager);
    }// end of method

    /**
     * Bulk delete records with CriteriaDelete
     * <p>
     *
     * @param clazz the domain class
     * @param attr  the attribute to filter - null for no filtering
     * @param value the value to search for
     *
     * @deprecated
     */
    public static void deleteOld(Class<? extends BaseEntity> clazz, SingularAttribute attr, Object value) {
        EntityManager manager = EM.createEntityManager();
        deleteOld(clazz, attr, value, manager);
        manager.close();
    }// end of method

    /**
     * Bulk delete records with CriteriaDelete
     * <p>
     *
     * @param clazz   the domain class
     * @param attr    the attribute to filter - null for no filtering
     * @param value   the value to search for
     * @param manager the EntityManager to use
     *
     * @deprecated
     */
    @SuppressWarnings("unchecked")
    public static void deleteOld(Class<? extends BaseEntity> clazz, SingularAttribute attr, Object value, EntityManager manager) {
        CriteriaBuilder cb = manager.getCriteriaBuilder();

        // create delete
        CriteriaDelete delete = cb.createCriteriaDelete(clazz);

        // set the root class
        Root root = delete.from(clazz);

        // set where clause
        if (attr != null) {
            Predicate pred = cb.equal(root.get(attr), value);
            delete.where(pred);
        }// end of if cycle

        // perform update
        try {
            manager.getTransaction().begin();
            manager.createQuery(delete).executeUpdate();
            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
        }// fine del blocco try-catch

    }// end of method

    /**
     * Delete all the records for a given domain class
     * <p>
     *
     * @param entityClass the domain class
     * @param filter      the Filter for the records to delete (null for all records)
     *
     * @deprecated
     */
    @SuppressWarnings("unchecked")
    public static void deleteOld(Class<? extends BaseEntity> entityClass, Filter filter) {

        EntityManager manager = EM.createEntityManager();
        JPAContainer<BaseEntity> cont = (JPAContainer<BaseEntity>) JPAContainerFactory.make(entityClass, manager);
        if (filter != null) {
            cont.addContainerFilter(filter);
        }

        try {

            manager.getTransaction().begin();

            for (Object id : cont.getItemIds()) {
                BaseEntity entity = cont.getItem(id).getEntity();
                entity = manager.merge(entity);
                manager.remove(entity);
            }

            manager.getTransaction().commit();

        } catch (Exception e) {
            manager.getTransaction().rollback();
        }
        manager.close();

    }// end of method

    /**
     * Return a list of entities for a given domain class and filters.
     * Filtrato coi filtri indicati
     * <p>
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param clazz   the Entity class
     * @param sorts   an array of sort wrapper
     * @param manager the EntityManager to use
     * @param filters an array of filters (you can use FilterFactory to build filters, or create them as Compare....)
     *
     * @return the list of founded entities
     * @deprecated
     */
    public static List<? extends BaseEntity> findAll(Class<? extends BaseEntity> clazz, SortProperty sorts, EntityManager manager, Filter... filters) {
        List<BaseEntity> entities = new ArrayList<BaseEntity>();
        EntityItem<BaseEntity> item;

        // se non specificato l'EntityManager, ne crea uno locale
        boolean usaManagerLocale = false;
        if (manager == null) {
            usaManagerLocale = true;
            manager = EM.createEntityManager();
        }// end of if cycle

        JPAContainer<BaseEntity> container = getContainer(clazz, manager, filters);

        if (sorts != null) {
            for (String stringa : sorts.getProperties()) {
                if (stringa.contains(".")) {
                    container.addNestedContainerProperty(stringa.substring(0, stringa.lastIndexOf(".")) + ".*");
                }// end of if cycle
            }// end of for cycle

            container.sort(sorts.getProperties(), sorts.getOrdinamenti());

        }// end of if cycle

        for (Object id : container.getItemIds()) {
            item = container.getItem(id);
            entities.add(item.getEntity());
        }// end of for cycle

        // eventualmente chiude l'EntityManager locale
        if (usaManagerLocale) {
            manager.close();
        }// end of if cycle

        return entities;
    }// end of method

    /**
     * Search for all entities with a specified attribute value.
     * Filtrato sul valore della property indicata.
     *
     * @param clazz the Entity class
     * @param attr  the searched attribute
     * @param value the value to search for
     *
     * @return the list of founded entities
     * @deprecated
     */
    public static List<? extends BaseEntity> findAll(Class<? extends BaseEntity> clazz, SingularAttribute attr, Object value) {
        return findAll(clazz, attr, value, null);
    }// end of method

    /**
     * Search for all entities with a specified attribute value.
     * Filtrato sul valore della property indicata.
     * <p>
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param clazz   the Entity class
     * @param attr    the searched attribute
     * @param value   the value to search for
     * @param manager the EntityManager to use
     *
     * @return the list of founded entities
     * @deprecated
     */
    @SuppressWarnings("unchecked")
    public static List<? extends BaseEntity> findAll(Class<? extends BaseEntity> clazz, SingularAttribute attr, Object value, EntityManager manager) {
        List<? extends BaseEntity> entities;

        // se non specificato l'EntityManager, ne crea uno locale
        boolean usaManagerLocale = false;
        if (manager == null) {
            usaManagerLocale = true;
            manager = EM.createEntityManager();
        }// end of if cycle

        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<? extends BaseEntity> cq = cb.createQuery(clazz);
        Root<? extends BaseEntity> root = cq.from(clazz);
        Predicate pred = cb.equal(root.get(attr), value);
        cq.where(pred);
        TypedQuery<? extends BaseEntity> query = manager.createQuery(cq);
        entities = query.getResultList();

        // eventualmente chiude l'EntityManager locale
        if (usaManagerLocale) {
            manager.close();
        }// end of if cycle

        return entities;
    }// end of method

    /**
     * Create a read-only JPA container for a given domain class and filters.
     * <p>
     * Use a standard manager
     *
     * @param clazz   the Entity class
     * @param filters an array of filters (you can use FilterFactory to build filters, or create them as Compare....)
     *
     * @return the JPA container
     * @deprecated
     */
    @SuppressWarnings("unchecked")
    private static JPAContainer<BaseEntity> getContainer(Class<? extends BaseEntity> clazz, Filter... filters) {
        JPAContainer<BaseEntity> container = null;

        EntityManager manager = EM.createEntityManager();
        container = getContainer(clazz, manager, filters);
//        manager.close();

        return container;
    }// end of method

    /**
     * Create a read-only JPA container for a given domain class and filters.
     * <p>
     * Use a specific manager (must be close by caller method)
     *
     * @param clazz   the Entity class
     * @param manager the EntityManager to use
     * @param filters an array of filters (you can use FilterFactory to build filters, or create them as Compare....)
     *
     * @return the JPA container
     * @deprecated
     */
    @SuppressWarnings("unchecked")
    private static JPAContainer<BaseEntity> getContainer(Class<? extends BaseEntity> clazz, EntityManager manager, Filter... filters) {
        JPAContainer<BaseEntity> container = (JPAContainer<BaseEntity>) JPAContainerFactory.makeNonCachedReadOnly(clazz, manager);

        for (Filter filter : filters) {
            container.addContainerFilter(filter);
        }// end of for cycle

        return container;
    }// end of method

    /**
     * Search for all entities with a specified attribute value.
     * <p>
     *
     * @param clazz the entity class
     * @param attr  the searched attribute
     * @param value the value to search for
     *
     * @return a list of entities corresponding to the specified criteria
     * @deprecated
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List<? extends BaseEntity> queryList(Class<? extends BaseEntity> clazz, SingularAttribute attr, Object value) {
        EntityManager manager = EM.createEntityManager();
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<? extends BaseEntity> cq = cb.createQuery(clazz);
        Root<? extends BaseEntity> root = cq.from(clazz);
        Predicate pred = cb.equal(root.get(attr), value);
        cq.where(pred);
        TypedQuery<? extends BaseEntity> query = manager.createQuery(cq);
        List<? extends BaseEntity> entities = query.getResultList();
        manager.close();
        return entities;
    }// end of method

    /**
     * Search for all entities with a specified attribute value.
     * <p>
     *
     * @param clazz the entity class
     * @param attr  the searched attribute
     * @param value the value to search for
     *
     * @return a list of entities corresponding to the specified criteria
     * @deprecated
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List<? extends BaseEntity> queryList(Class<? extends BaseEntity> clazz, SingularAttribute attr, Object value, EntityManager manager) {
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<? extends BaseEntity> cq = cb.createQuery(clazz);
        Root<? extends BaseEntity> root = cq.from(clazz);
        Predicate pred = cb.equal(root.get(attr), value);
        cq.where(pred);
        TypedQuery<? extends BaseEntity> query = manager.createQuery(cq);
        List<? extends BaseEntity> entities = query.getResultList();
        return entities;
    }// end of method

    /**
     * Search for all entities with a specified attribute value.
     * <p>
     *
     * @param clazz the entity class
     * @param attr  the searched attribute
     * @param value the value to search for
     *
     * @return a list of entities corresponding to the specified criteria
     * @deprecated
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static ArrayList<? extends BaseEntity> queryLista(Class<? extends BaseEntity> clazz, SingularAttribute attr, Object value) {
        ArrayList<? extends BaseEntity> lista = null;
        List<? extends BaseEntity> entities = queryList(clazz, attr, value);

        if (entities != null) {
            lista = new ArrayList<BaseEntity>(entities);
        }// end of if cycle

        return lista;
    }// end of method

    /**
     * Search for the first entity with a specified attribute value.
     * <p>
     *
     * @param clazz the entity class
     * @param attr  the searched attribute
     * @param value the value to search for
     *
     * @return the first entity corresponding to the specified criteria
     * @deprecated
     */
    @SuppressWarnings({"rawtypes"})
    public static BaseEntity queryFirst(Class<? extends BaseEntity> clazz, SingularAttribute attr, Object value) {
        BaseEntity bean = null;

        List<? extends BaseEntity> entities = queryList(clazz, attr, value);
        if (entities.size() > 0) {
            bean = entities.get(0);
        }// end of if cycle

        return bean;
    }// end of method

    /**
     * Search for the only entities with a specified attribute value.
     * <p>
     * If multiple entities exist, null is returned.
     *
     * @param clazz the entity class
     * @param attr  the searched attribute
     * @param value the value to search for
     *
     * @return the only entity corresponding to the specified criteria, or null
     * @deprecated
     */
    @SuppressWarnings({"rawtypes"})
    public static BaseEntity queryOne(Class<? extends BaseEntity> clazz, SingularAttribute attr, Object value) {
        BaseEntity bean = null;

        EntityManager manager = EM.createEntityManager();
        bean = queryOne(clazz, attr, value, manager);
        manager.close();

        return bean;
    }// end of method

    /**
     * Search for the only entities with a specified attribute value.
     * <p>
     * If multiple entities exist, null is returned.
     *
     * @param clazz   the entity class
     * @param attr    the searched attribute
     * @param value   the value to search for
     * @param manager the EntityManager to use
     *
     * @return the only entity corresponding to the specified criteria, or null
     * @deprecated
     */
    @SuppressWarnings({"rawtypes"})
    public static BaseEntity queryOne(Class<? extends BaseEntity> clazz, SingularAttribute attr, Object value, EntityManager manager) {
        BaseEntity bean = null;

        List<? extends BaseEntity> entities = queryList(clazz, attr, value, manager);
        if (entities.size() == 1) {
            bean = entities.get(0);
        }// end of if cycle

        return bean;
    }// end of method

    /**
     * Search for the all entities
     *
     * @param entityClass the entity class
     *
     * @return a list of entities
     * @deprecated
     */
    public static List<? extends BaseEntity> getListOld(Class<? extends BaseEntity> entityClass) {
        List<? extends BaseEntity> entities;

        EntityManager manager = EM.createEntityManager();
        entities = getListOld(entityClass, manager);
        manager.close();

        return entities;
    }// end of method

    /**
     * Search for the all entities
     *
     * @param entityClass the entity class
     * @param manager     the EntityManager to use
     *
     * @return a list of entities
     * @deprecated
     */
    public static List<? extends BaseEntity> getListOld(Class<? extends BaseEntity> entityClass, EntityManager manager) {
        List<? extends BaseEntity> entities;
        CriteriaBuilder builder;
        CriteriaQuery<? extends BaseEntity> criteria;
        TypedQuery<? extends BaseEntity> query;

        builder = manager.getCriteriaBuilder();
        criteria = builder.createQuery(entityClass);
        query = manager.createQuery(criteria);
        entities = query.getResultList();

        return entities;
    }// end of method

    /**
     * Search for the all entities
     *
     * @param entityClass the entity class
     *
     * @return an ArrayList of entities
     * @deprecated
     */
    public static ArrayList<? extends BaseEntity> getLista(Class<? extends BaseEntity> entityClass) {
        ArrayList<? extends BaseEntity> lista = null;

        EntityManager manager = EM.createEntityManager();
        lista = getLista(entityClass, manager);
        manager.close();

        return lista;
    }// end of method

    /**
     * Search for the all entities
     *
     * @param entityClass the entity class
     * @param manager     the EntityManager to use
     *
     * @return an ArrayList of entities
     * @deprecated
     */
    public static ArrayList<? extends BaseEntity> getLista(Class<? extends BaseEntity> entityClass, EntityManager manager) {
        ArrayList<? extends BaseEntity> lista = null;
        List<? extends BaseEntity> entities = getListOld(entityClass, manager);

        if (entities != null) {
            lista = new ArrayList<BaseEntity>(entities);
        }// end of if cycle

        return lista;
    }// end of method

    /**
     * Return a list of entities for a given domain class and filters.
     * <p>
     *
     * @param entityClass - the entity class
     * @param filters     - an array of filters (you can use FilterFactory to build
     *                    filters, or create them as Compare....)
     *
     * @return the list with the entities found
     * @deprecated
     */
    public static ArrayList<BaseEntity> getListOld(Class<? extends BaseEntity> entityClass, Filter... filters) {
        return getListOld(entityClass, null, filters);
    }

    /**
     * Return a list of entities for a given domain class and filters.
     * <p>
     *
     * @param entityClass - the entity class
     * @param filters     - an array of filters (you can use FilterFactory to build
     *                    filters, or create them as Compare....)
     *
     * @return an ArrayList of entities
     * @deprecated
     */
    public static ArrayList<? extends BaseEntity> getLista(Class<? extends BaseEntity> entityClass, Filter... filters) {
        ArrayList<? extends BaseEntity> lista = null;
        List<? extends BaseEntity> entities = getListOld(entityClass, filters);

        if (entities != null) {
            lista = new ArrayList<BaseEntity>(entities);
        }// end of if cycle

        return lista;
    }// end of method

    /**
     * Return a list of entities for a given domain class and filters.
     * <p>
     *
     * @param entityClass - the entity class
     * @param sorts       - an array of sort wrapper
     * @param filters     - an array of filters (you can use FilterFactory to build
     *                    filters, or create them as Compare....)
     *
     * @return the list with the entities found
     * @deprecated
     */
    public static ArrayList<BaseEntity> getListOld(Class<? extends BaseEntity> entityClass, SortProperty sorts, Filter... filters) {
        EntityItem<BaseEntity> item;
        ArrayList<BaseEntity> list = new ArrayList<BaseEntity>();
        JPAContainer<BaseEntity> container = getContainer(entityClass, filters);

        if (sorts != null) {
            for (String stringa : sorts.getProperties()) {
                if (stringa.contains(".")) {
                    container.addNestedContainerProperty(stringa.substring(0, stringa.lastIndexOf(".")) + ".*");
                }// end of if cycle
            }// end of for cycle

            container.sort(sorts.getProperties(), sorts.getOrdinamenti());
        }// end of if cycle

        for (Object id : container.getItemIds()) {
            item = container.getItem(id);
            list.add(item.getEntity());
        }// end of for cycle

        // close the EntityManager
        container.getEntityProvider().getEntityManager().close();

        return list;
    }// end of method

//    /**
//     * Return a single entity for a given domain class and filters.
//     * <p>
//     *
//     * @param entityClass - the entity class
//     * @param arguments   - an array of filters (you can use FilterFactory to build
//     *                    filters, or create them as Compare....)
//     * @return the single (or first) entity found
//     * @deprecated
//     */
//    public static BaseEntity getEntity(Class<? extends BaseEntity> entityClass, Filter... arguments) {
//        BaseEntity entity = null;
//        ArrayList<BaseEntity> list = getListOld(entityClass, arguments);
//        if (list.size() > 0) {
//            entity = list.get(0);
//        }
//        return entity;
//}

    /**
     * Searches for a single entity by id
     *
     * @param clazz the Entity class
     * @param id    the item id
     *
     * @deprecated
     */
    public static BaseEntity queryById(Class<? extends BaseEntity> clazz, Object id) {
        EntityManager manager = EM.createEntityManager();
        BaseEntity entity = (BaseEntity) manager.find(clazz, id);
        manager.close();
        return entity;
    }// end of static method

    /**
     * Ritorna il numero totale di record della Entity specificata
     * Senza filtri.
     *
     * @param clazz the Entity class
     *
     * @return il numero totale di record nella Entity
     * @deprecated -> return long, use count() instead
     */
    public static long getCount(Class<? extends BaseEntity> clazz) {
        return getCount(clazz, null, null, null);
    }// end of static method

    /**
     * Ritorna il numero totale di record della entity specificata
     *
     * @param clazz   the Entity class
     * @param manager the EntityManager to use
     *
     * @return il numero totale di record nella Entity
     * @deprecated -> return long, use count() instead
     */
    public static long getCount(Class<? extends BaseEntity> clazz, EntityManager manager) {
        return getCount(clazz, null, null, manager);
    }// end of static method

    /**
     * Ritorna il numero di record filtrati della entity specificata
     * Filtrato sul valore della property indicata.
     *
     * @param clazz the Entity class
     * @param attr  the searched attribute
     * @param value the value to search for
     *
     * @return il numero di record filtrati nella Entity
     * @deprecated -> return long, use count() instead
     */
    public static long getCount(Class<? extends BaseEntity> clazz, Attribute attr, Object value) {
        return getCount(clazz, attr, value, null);
    }// end of static method

    /**
     * Ritorna il numero di record filtrati della entity specificata
     * Filtrato sul valore della property indicata.
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param clazz   the Entity class
     * @param attr    the searched attribute
     * @param value   the value to search for
     * @param manager the EntityManager to use
     *
     * @return il numero di record filtrati nella Entity
     * @deprecated -> return long, use count() instead
     */
    public static long getCount(Class<? extends BaseEntity> clazz, Attribute attr, Object value, EntityManager manager) {
        String propertyName = "";

        // se non specificato l'EntityManager, ne crea uno locale
        boolean usaManagerLocale = false;
        if (manager == null) {
            usaManagerLocale = true;
            manager = EM.createEntityManager();
        }// end of if cycle

        if (attr != null) {
            propertyName = attr.getName();
        }// end of if cycle

        CriteriaBuilder qb = manager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        cq.select(qb.count(cq.from(clazz)));

        if (!propertyName.equals("")) {
            Root root = cq.from(clazz);
            Expression exp = root.get(propertyName);
            Predicate restrictions = qb.equal(exp, value);
            cq.where(restrictions);
        }// end of if cycle

        long count = manager.createQuery(cq).getSingleResult();

        // eventualmente chiude l'EntityManager locale
        if (usaManagerLocale) {
            manager.close();
        }// end of if cycle

        return count;
    }// end of static method

    /**
     * Search for a single entity with a specified attribute value.
     * <p>
     * If multiple entities exist, null is returned.
     * Use a standard manager (and close it)
     *
     * @param clazz the Entity class
     * @param attr  the searched attribute
     * @param value the value to search for
     *
     * @return the only entity corresponding to the specified criteria, or null
     * @deprecated
     */
    public static BaseEntity findOne(Class<? extends BaseEntity> clazz, SingularAttribute attr, Object value) {
        BaseEntity entity = null;

        EntityManager manager = EM.createEntityManager();
        entity = findOne(clazz, attr, value, manager);
        manager.close();

        return entity;
    }// end of static method

    /**
     * Search for a single entity with a specified attribute value.
     * <p>
     * If multiple entities exist, null is returned.
     * Use a specific manager (must be close by caller method)
     *
     * @param clazz   the Entity class
     * @param attr    the searched attribute
     * @param value   the value to search for
     * @param manager the EntityManager to use
     *
     * @return the only entity corresponding to the specified criteria, or null
     * @deprecated
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static BaseEntity findOne(Class<? extends BaseEntity> clazz, SingularAttribute attr, Object value, EntityManager manager) {
        BaseEntity entity = null;

        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<? extends BaseEntity> cq = cb.createQuery(clazz);
        Root<? extends BaseEntity> root = cq.from(clazz);
        Predicate pred = cb.equal(root.get(attr), value);
        cq.where(pred);
        TypedQuery<? extends BaseEntity> query = manager.createQuery(cq);

        try { // prova ad eseguire il codice
            entity = query.getSingleResult();
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        return entity;
    }// end of static method

    /**
     * Search for the first entity with a specified attribute value.
     * <p>
     * Use a standard manager (and close it)
     *
     * @param clazz the Entity class
     * @param attr  the searched attribute
     * @param value the value to search for
     *
     * @return the first entity corresponding to the specified criteria
     * @deprecated
     */
    public static BaseEntity findFirst(Class<? extends BaseEntity> clazz, SingularAttribute attr, Object value) {
        BaseEntity entity = null;

        EntityManager manager = EM.createEntityManager();
        entity = findFirst(clazz, attr, value, manager);
        manager.close();

        return entity;
    }// end of method

    /**
     * Search for the first entity with a specified attribute value.
     * <p>
     * Use a specific manager (must be close by caller method)
     *
     * @param clazz   the Entity class
     * @param attr    the searched attribute
     * @param value   the value to search for
     * @param manager the EntityManager to use
     *
     * @return the first entity corresponding to the specified criteria
     * @deprecated
     */
    @SuppressWarnings({"rawtypes"})
    public static BaseEntity findFirst(Class<? extends BaseEntity> clazz, SingularAttribute attr, Object value, EntityManager manager) {
        BaseEntity entity = null;

        List<? extends BaseEntity> entities = queryList(clazz, attr, value);
        if (entities.size() > 0) {
            entity = entities.get(0);
        }// end of if cycle

        return entity;
    }// end of method

}// end of abstract class
