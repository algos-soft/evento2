package it.algos.webbase.domain.vers;

import com.vaadin.data.Container;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.lib.LibTime;
import it.algos.webbase.web.query.AQuery;
import it.algos.webbase.web.query.SortProperty;
import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.persistence.annotations.Index;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.metamodel.SingularAttribute;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Gac on 17 lug 2015.
 * Entity per una versione
 * Estende la Entity astratta BaseEntity che contiene la key property ID
 * Tipicamente usata dal developer per gestire le versioni, patch e release dell'applicazione
 * <p>
 * Classe di tipo JavaBean
 * 1) la classe deve avere un costruttore senza argomenti
 * 2) le proprietà devono essere private e accessibili solo con get, set e is (usato per i boolena al posto di get)
 * 3) la classe deve implementare l'interfaccia Serializable (la fa nella superclasse)
 * 4) la classe non deve contenere nessun metodo per la gestione degli eventi
 * <p>
 * Questa Entity non usa la multiazienda (company) e quindi estende BaseEntity e non CompanyEntity
 * Il funzionamento viene testato nel progetto MultyCompany.VersioneTest,
 * perché WebBase non ha il file persistence.xml e non può usare il DB
 */
@Entity
public class Versione extends BaseEntity {

    //------------------------------------------------------------------------------------------------------------------------
    // Properties
    //------------------------------------------------------------------------------------------------------------------------
    // versione della classe per la serializzazione
    private static final long serialVersionUID = 1L;


    //--codifica di gruppo per identificare la tipologia della versione (obbligatoria, non unica)
    //--va inizializzato con una stringa vuota, per evitare che compaia null nel Form nuovoRecord
    @NotEmpty
    @Column(length = 20)
    @Index
    private String titolo = "";

    //--descrizione (obbligatoria, non unica)
    //--va inizializzato con una stringa vuota, per evitare che compaia null nel Form nuovoRecord
    @NotEmpty
    private String descrizione = "";

    //--ordine di presentazione nelle liste (obbligatorio, unico, con controllo automatico prima del persist se è zero)
    @NotNull
    @Index
    private int ordine;

    //--momento in cui si effettua la modifica della versione (obbligatoria, non unica)
    //--inserita automaticamente
    @NotNull
    private Timestamp timestamp;


    //------------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Costruttore senza argomenti
     * Obbligatorio per le specifiche JavaBean
     * Da non usare MAI per la creazione diretta di una nuova istanza (si perdono i controlli)
     */
    public Versione() {
    }// end of JavaBean constructor

    /**
     * Costruttore minimo con tutte le properties obbligatorie
     * Se manca l'ordine di presentazione o è uguale a zero, viene calcolato in automatico prima del persist
     * Il timestamp (obbligatorio) viene inserito in automatico
     *
     * @param titolo      codifica di gruppo per identificare la tipologia della versione (obbligatoria, non unica)
     * @param descrizione descrizione (obbligatoria, non unica)
     */
    public Versione(String titolo, String descrizione) {
        this(titolo, descrizione, 0, LibTime.adesso());
    }// end of constructor


    /**
     * Costruttore completo
     * Se manca l'ordine di presentazione o è uguale a zero, viene calcolato in automatico prima del persist
     * Il timestamp (obbligatorio) viene inserito in automatico
     *
     * @param titolo      codifica di gruppo per identificare la tipologia della versione (obbligatoria, non unica)
     * @param descrizione descrizione (obbligatoria, non unica)
     * @param ordine      di presentazione nelle liste (obbligatorio, unico)
     * @param timestamp   di inserimento della versione (obbligatorio, unico)
     */
    public Versione(String titolo, String descrizione, int ordine, Timestamp timestamp) {
        this.setTitolo(titolo);
        this.setDescrizione(descrizione);
        this.setOrdine(ordine);
        this.setTimestamp(timestamp);
    }// end of constructor


    //------------------------------------------------------------------------------------------------------------------------
    // Count records
    //
    // Con e senza EntityManager
    // Rimanda ad un unico metodo
    // @todo Funzionamento testato nella classe MultyCompany.VersioneTest
    // Return int (non c'è motivo di usare un long come valore di ritorno)
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Numero totale di entities della Entity
     * Senza filtri.
     * Usa l'EntityManager di default
     *
     * @return il numero totale di records nella Entity
     */
    public static int count() {
        return count(null);
    }// end of static method

    /**
     * Numero totale di entities della Entity
     * Senza filtri.
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param manager the entity manager to use (if null, a new one is created on the fly)
     * @return il numero totale di records nella Entity
     */
    public static int count(EntityManager manager) {
        return AQuery.count(Versione.class, manager);
    }// end of static method

    /**
     * Numero di entities della Entity
     * Filtrate sul valore della property indicata
     * Usa l'EntityManager di default
     *
     * @param value the value to search for
     * @return il numero filtrato di records nella Entity
     */
    public static int countByTitolo(Object value) {
        return countByTitolo(value, null);
    }// end of static method

    /**
     * Numero di entities della Entity
     * Filtrate sul valore della property indicata
     * Usa l'EntityManager di default
     *
     * @param value   the value to search for
     * @param manager the entity manager to use (if null, a new one is created on the fly)
     * @return il numero filtrato di records nella Entity
     */
    public static int countByTitolo(Object value, EntityManager manager) {
        return AQuery.count(Versione.class, Versione_.titolo, value, manager);
    }// end of static method

    //------------------------------------------------------------------------------------------------------------------------
    // Find single entity by primary key
    //
    // Con e senza EntityManager
    // Rimanda ad un unico metodo
    // @todo Funzionamento testato nella classe MultyCompany.VersioneTest
    // Parametro in ingresso di tipo long (all Algos primary key are long)
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Searches for a single entity by standard Primary Key
     * (all Algos primary key are long)
     * Multiple entities cannot exist, the primary key is unique.
     * Usa l'EntityManager di default
     *
     * @param id valore (unico) della Primary Key
     * @return istanza della Entity, null se non trovata
     */
    public static Versione find(long id) {
        return find(id, null);
    }// end of static method

    /**
     * Searches for a single entity by standard Primary Key
     * (all Algos primary key are long)
     * Multiple entities cannot exist, the primary key is unique.
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param id      valore (unico) della Primary Key
     * @param manager the entity manager to use (if null, a new one is created on the fly)
     * @return istanza della Entity, null se non trovata
     */
    public static Versione find(long id, EntityManager manager) {
        return (Versione) AQuery.find(Versione.class, id, manager);
    }// end of static method

    //------------------------------------------------------------------------------------------------------------------------
    // Find single entity by SingularAttribute
    //
    // Nessuna property è unica. Nessuna ricerca significativa
    //------------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------------
    // Find entities (list)
    //
    // Con e senza Property (SingularAttribute)
    // Con e senza Sort
    // Con e senza Filters
    // Con e senza EntityManager
    // Rimanda ad un unico metodo
    // @todo Funzionamento testato nella classe MultyCompany.VersioneTest
    // Return una List di oggetti col casting specifico a Versione
    //------------------------------------------------------------------------------------------------------------------------

    public static List<Versione> getList() {
        return getList((SingularAttribute) null, null, (SortProperty) null, (EntityManager) null);
    }// end of static method

    public static List<Versione> getList(EntityManager manager) {
        return getList((SingularAttribute) null, null, (SortProperty) null, manager);
    }// end of static method

    public static List<Versione> getList(SingularAttribute attr, Object value) {
        return getList(attr, value, (SortProperty) null, (EntityManager) null);
    }// end of static method

    public static List<Versione> getList(Container.Filter... filters) {
        return getList((SortProperty) null, (EntityManager) null, filters);
    }// end of static method

    public static List<Versione> getList(SortProperty sorts) {
        return getList((SingularAttribute) null, null, sorts, (EntityManager) null);
    }// end of static method

    public static List<Versione> getList(EntityManager manager, Container.Filter... filters) {
        return getList((SortProperty) null, manager, filters);
    }// end of static method

    public static List<Versione> getList(SingularAttribute attr, Object value, EntityManager manager) {
        return getList(attr, value, (SortProperty) null, manager);
    }// end of static method

    public static List<Versione> getList(SortProperty sorts, Container.Filter... filters) {
        return getList(sorts, (EntityManager) null, filters);
    }// end of static method

    public static List<Versione> getList(SingularAttribute attr, Object value, SortProperty sorts) {
        return getList(attr, value, sorts, (EntityManager) null);
    }// end of static method

    /**
     * Recupera una lista (array) di entities della Entity
     * Filtrate sul valore (eventuale) della property indicata
     * Filtrate sui filtri (eventuali) passati come parametro
     * Sia la property che i filtri sono additivi (ADD) l'uno con l'altro
     * Se manca sia la property sia i filtri, restituisce tutte le entities della Entity
     * Ordinate secondo il filtro
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param attr    the searched attribute
     * @param value   the value to search for
     * @param sorts   ordinamento (ascendente o discendente)
     * @param manager the entity manager to use (if null, a new one is created on the fly)
     * @return a list of founded entities
     */
    public static List<Versione> getList(SingularAttribute attr, Object value, SortProperty sorts, EntityManager manager) {
        return (List<Versione>) AQuery.getList(Versione.class, attr, value, sorts, manager);
//        List<? extends BaseEntity> lista = AQuery.getList(Versione.class, attr, value, sorts, manager);
//        return check(lista);
    }// end of static method

    /**
     * Recupera una lista (array) di entities della Entity
     * Filtrate sul valore (eventuale) della property indicata
     * Filtrate sui filtri (eventuali) passati come parametro
     * Sia la property che i filtri sono additivi (ADD) l'uno con l'altro
     * Se manca sia la property sia i filtri, restituisce tutte le entities della Entity
     * Ordinate secondo il filtro
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param sorts   ordinamento (ascendente o discendente)
     * @param manager the entity manager to use (if null, a new one is created on the fly)
     * @param filters an array of filters (you can use FilterFactory to build filters, or create them as Compare....)
     * @return a list of founded entities
     */
    public static List<Versione> getList(SortProperty sorts, EntityManager manager, Container.Filter... filters) {
        return (List<Versione>)AQuery.getList(Versione.class, sorts, manager, filters);
//        List<? extends BaseEntity> lista = AQuery.getList(Versione.class, sorts, manager, filters);
//        return check(lista);
    }// end of static method


    //------------------------------------------------------------------------------------------------------------------------
    // New and save
    //
    // Con e senza EntityManager
    // Rimanda ad un unico metodo
    // @todo Funzionamento testato nella classe MultyCompany.VersioneTest
    // Return una entity Versione
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Creazione iniziale di una istanza della Entity
     * La crea sempre, non essendoci property uniche
     * <p>
     * Se manca l'ordine di presentazione o è uguale a zero, viene calcolato in automatico prima del persist
     * Se manca il timestamp (obbligatorio), viene inserito in automatico
     * Usa l'EntityManager di default
     *
     * @param titolo      codifica di gruppo per identificare la tipologia della versione (obbligatoria, non unica)
     * @param descrizione descrizione (obbligatoria, non unica)
     * @return nuova istanza della Entity
     */
    public static Versione crea(String titolo, String descrizione) {
        return crea(titolo, descrizione, (EntityManager) null);
    }// end of static method

    /**
     * Creazione iniziale di una istanza della Entity
     * La crea sempre, non essendoci property uniche
     * <p>
     * Se manca l'ordine di presentazione o è uguale a zero, viene calcolato in automatico prima del persist
     * Se manca il timestamp (obbligatorio), viene inserito in automatico
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param titolo      codifica di gruppo per identificare la tipologia della versione (obbligatoria, non unica)
     * @param descrizione descrizione (obbligatoria, non unica)
     * @param manager     the entity manager to use (if null, a new one is created on the fly)
     * @return nuova istanza della Entity
     */
    public static Versione crea(String titolo, String descrizione, EntityManager manager) {
        return crea(titolo, descrizione, 0, null, manager);
    }// end of static method

    /**
     * Creazione iniziale di una istanza della Entity
     * La crea sempre, non essendoci property uniche
     * <p>
     * Se manca l'ordine di presentazione o è uguale a zero, viene calcolato in automatico prima del persist
     * Se manca il timestamp (obbligatorio), viene inserito in automatico
     * Usa l'EntityManager di default
     *
     * @param titolo      codifica di gruppo per identificare la tipologia della versione (obbligatoria, non unica)
     * @param descrizione descrizione (obbligatoria, non unica)
     * @param ordine      di presentazione nelle liste (obbligatorio, unico)
     * @param timestamp   di inserimento della versione (obbligatorio, unico)
     * @return nuova istanza della Entity
     */
    public static Versione crea(String titolo, String descrizione, int ordine, Timestamp timestamp) {
        return crea(titolo, descrizione, ordine, timestamp, null);
    }// end of static method

    /**
     * Creazione iniziale di una istanza della Entity
     * La crea sempre, non essendoci property uniche
     * <p>
     * Se manca l'ordine di presentazione o è uguale a zero, viene calcolato in automatico prima del persist
     * Se manca il timestamp (obbligatorio), viene inserito in automatico
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param titolo      codifica di gruppo per identificare la tipologia della versione (obbligatoria, non unica)
     * @param descrizione descrizione (obbligatoria, non unica)
     * @param ordine      di presentazione nelle liste (obbligatorio, unico)
     * @param timestamp   di inserimento della versione (obbligatorio, unico)
     * @param manager     the entity manager to use (if null, a new one is created on the fly)
     * @return nuova istanza della Entity
     */
    public static Versione crea(String titolo, String descrizione, int ordine, Timestamp timestamp, EntityManager manager) {
        Versione versione;

        versione = new Versione(titolo, descrizione, ordine, timestamp);
        versione.save(manager);

        if (versione.getId() != null) {
            return versione;
        } else {
            return null;
        }// end of if/else cycle
    }// end of static method

    //------------------------------------------------------------------------------------------------------------------------
    // Delete entities (static)
    //
    // Con e senza Property (SingularAttribute)
    // Con e senza Filters
    // Con e senza EntityManager
    // Rimanda a DUE metodi: Filters e CriteriaDelete
    // @todo Funzionamento testato nella classe MultyCompany.VersioneTest
    //------------------------------------------------------------------------------------------------------------------------
    public static int deleteAll() {
        return deleteBulk((SingularAttribute) null, null, (EntityManager) null);
    }// end of static method

    public static int deleteAll(EntityManager manager) {
        return deleteBulk((SingularAttribute) null, null, manager);
    }// end of static method

    public static int delete(SingularAttribute attr, Object value) {
        return deleteBulk(attr, value, (EntityManager) null);
    }// end of static method

    public static int delete(SingularAttribute attr, Object value, EntityManager manager) {
        return deleteBulk(attr, value, manager);
    }// end of static method

    public static int delete(Container.Filter... filters) {
        return delete(null, null, (EntityManager) null, filters);
    }// end of static method

    public static int delete(EntityManager manager, Container.Filter... filters) {
        return delete(null, null, manager, filters);
    }// end of static method

    /**
     * Bulk delete records with CriteriaDelete
     * <p>
     * Usa una Property (SingularAttribute)
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param attr    the searched attribute
     * @param value   the value to search for
     * @param manager the entity manager to use (if null, a new one is created on the fly)
     * @return il numero di records cancellati
     */
    @SuppressWarnings("unchecked")
    public static int deleteBulk(SingularAttribute attr, Object value, EntityManager manager) {
        return AQuery.delete(Versione.class, attr, value, manager);
    }// end of method


    /**
     * Delete the records of the Entity
     * <p>
     * Usa una Property (SingularAttribute)
     * Usa i Filters
     * Usa l'EntityManager passato come parametro
     * Sia la property che i filtri sono additivi (ADD) l'uno con l'altro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param attr    the searched attribute
     * @param value   the value to search for
     * @param manager the entity manager to use (if null, a new one is created on the fly)
     * @param filters an array of filters (you can use FilterFactory to build filters, or create them as Compare....)
     * @return il numero di records cancellati
     */
    @SuppressWarnings("unchecked")
    public static int delete(SingularAttribute attr, Object value, EntityManager manager, Container.Filter... filters) {
        return AQuery.delete(Versione.class, attr, value, manager, filters);
    }// end of static method

    //------------------------------------------------------------------------------------------------------------------------
    // utilities (static)
    //------------------------------------------------------------------------------------------------------------------------

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }// end of getter method

    /**
     * Controlla se la lista (array) di entities esiste ed è della classe corretta
     *
     * @param lista (BaseEntity) restituita dalla query generica
     * @return lista della Entity specifica, null se non trovata
     */
    @SuppressWarnings("unchecked")
    private static List<Versione> check(List<? extends BaseEntity> lista) {
        List<Versione> listaCastomizzata = null;

        if (lista != null && lista.size() > 0 && lista.get(0) instanceof Versione) {
            listaCastomizzata = (List<Versione>) lista;
        }// end of if cycle

        return listaCastomizzata;
    }// end of static method

    //------------------------------------------------------------------------------------------------------------------------
    // deprecated
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Recupera una istanza di Versione usando la query specifica
     *
     * @return istanza di Versione, null se non trovata
     * @deprecated
     */
    public static Versione find(String titolo) {
        Versione instance = null;
        BaseEntity entity = AQuery.queryOne(Versione.class, Versione_.titolo, titolo);

        if (entity != null) {
            if (entity instanceof Versione) {
                instance = (Versione) entity;
            }// end of if cycle
        }// end of if cycle

        return instance;
    }// end of method


    /**
     * @deprecated
     */
    public synchronized static List<Versione> findAll() {
        return (List<Versione>) AQuery.getListOld(Versione.class);
    }// end of method

    /**
     * Delete all the records for the Entity class
     * Bulk delete records with CriteriaDelete
     */
//    /**
//     * @deprecated

//     */
//    public static void deleteAll() {
//        EntityManager manager = EM.createEntityManager();
//        deleteAll(manager);
//        manager.close();
//    }// end of static method
//
//    /**
//     * Delete all the records for the Entity class
//     * Bulk delete records with CriteriaDelete
//     *
//     * @param manager the EntityManager to use
//     */
//    /**
//     * @deprecated
//     */
//    public static void deleteAll(EntityManager manager) {
//        AQuery.deleteAll(Versione.class, manager);
//    }// end of static method


    //------------------------------------------------------------------------------------------------------------------------
    // Getter and setter
    //------------------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return titolo;
    }// end of method

    public String getTitolo() {
        return titolo;
    }// end of getter method

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }//end of setter method

    public String getDescrizione() {
        return descrizione;
    }// end of getter method

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }//end of setter method

    public int getOrdine() {
        return ordine;
    }// end of getter method

    public void setOrdine(int ordine) {
        this.ordine = ordine;
    }//end of setter method

    public Timestamp getTimestamp() {
        return timestamp;
    }// end of getter method

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }//end of setter method


    //------------------------------------------------------------------------------------------------------------------------
    // Delete single entity
    //
    // Nessuna caratteristica particolare. Usa i metodi della supeclasse BaseEntity
    //------------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------------
    // Save
    //
    // Con e senza EntityManager
    // Rimanda ad un unico metodo
    // @todo Funzionamento testato nella classe VersioneTest
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Saves this entity to the database
     * <p>
     * Usa l'EntityManager di default
     *
     * @return the merged Entity (new entity, unmanaged, has the id)
     */
    @Override
    public BaseEntity save() {
        return this.save(null);
    }// end of method


    /**
     * Saves this entity to the database
     * <p>
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param manager the entity manager to use (if null, a new one is created on the fly)
     * @return the merged Entity (new entity, unmanaged, has the id)
     */
    @Override
    public BaseEntity save(EntityManager manager) {
        boolean valido;

        valido = this.checkTitolo();
        if (valido) {
            valido = this.checkDescrizione();
        }// end of if cycle
        if (valido) {
            this.checkOrdine(manager);
        }// end of if cycle
        if (valido) {
            this.checkTimestamp();
        }// end of if cycle
        if (valido) {
            return super.save(manager);
        } else {
            return null;
        }// end of if/else cycle
    }// end of method

    /**
     * Saves this entity to the database.
     * <p>
     * Usa l'EntityManager di default
     *
     * @return the merged Entity (new entity, unmanaged, has the id), casted as Versione
     */
    public Versione saveSafe() {
        return saveSafe(null);
    }// end of method

    /**
     * Saves this entity to the database.
     * <p>
     * If the provided EntityManager has an active transaction, the operation is performed inside the transaction.<br>
     * Otherwise, a new transaction is used to save this single entity.
     *
     * @param manager the entity manager to use (if null, a new one is created on the fly)
     * @return the merged Entity (new entity, unmanaged, has the id), casted as Versione
     */
    public Versione saveSafe(EntityManager manager) {
        return (Versione) this.save(manager);
    }// end of method

    //------------------------------------------------------------------------------------------------------------------------
    // utilities (non-static)
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Implementa come business logic, la obbligatorietà della property
     * <p>
     *
     * @return true se esiste, false se non esiste
     */
    private boolean checkTitolo() {
        String caption = "La versione non può essere accettata, perché manca il titolo che è obbligatorio";

        if (getTitolo() != null && !getTitolo().equals("")) {
            return true;
        } else {
            //@todo levato, perché nei test dà errore (forse manca la UI)
//            Notification.show(caption, Notification.Type.WARNING_MESSAGE);
            return false;
        }// end of if/else cycle
    } // end of method

    /**
     * Implementa come business logic, la obbligatorietà della descrizione
     * <p>
     *
     * @return true se esiste, false se non esiste
     */
    private boolean checkDescrizione() {
        String caption = "La versione non può essere accettata, perché manca la descrizione che è obbligatoria";

        if (getDescrizione() != null && !getDescrizione().equals("")) {
            return true;
        } else {
            //@todo levato, perché nei test dà errore (forse manca la UI)
//            Notification.show(caption, Notification.Type.WARNING_MESSAGE);
            return false;
        }// end of if/else cycle
    } // end of method

    /**
     * Appena prima di persistere sul DB
     * Elimino l'annotazione ed uso una chiamata dal metodo save(),
     * perché altrimenti non riuscirei a passare il parametro manager
     *
     * @param manager the entity manager to use (if null, a new one is created on the fly)
     */
    private void checkOrdine(EntityManager manager) {
        int max;

        if (getOrdine() == 0) {
            max = AQuery.maxInt(Versione.class, Versione_.ordine, manager);
            setOrdine(max + 1);
        }// end of if cycle
    }// end of method

    /**
     * Implementa come business logic, la obbligatorietà del timestamp
     * <p>
     *
     * @return always true
     */
    private boolean checkTimestamp() {
        if (getTimestamp() == null) {
            setTimestamp(LibTime.adesso());
        }// end of if cycle
        return true;
    } // end of method

    //------------------------------------------------------------------------------------------------------------------------
    // Clone
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Clone di questa istanza
     * Una DIVERSA istanza (indirizzo di memoria) con gi STESSI valori (property)
     * È obbligatorio invocare questo metodo all'interno di un codice try/catch
     *
     * @return nuova istanza della Entiry con gli stessi valori dei parametri di questa istanza
     */
    @Override
    @SuppressWarnings("all")
    public Versione clone() throws CloneNotSupportedException {
        try {
            return (Versione) BeanUtils.cloneBean(this);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }// fine del blocco try-catch
    }// end of method

}// end of entity class
