package it.algos.webbase.domain.pref;

import com.vaadin.server.Resource;
import com.vaadin.ui.Image;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.multiazienda.CompanyEntity;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.lib.LibText;
import it.algos.webbase.web.query.AQuery;
import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.persistence.annotations.Index;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.metamodel.SingularAttribute;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

/**
 * Entity per rappresentare una preferenza.
 * <p>
 * Classe di tipo JavaBean
 * 1) la classe deve avere un costruttore senza argomenti
 * 2) le proprietà devono essere private e accessibili solo con get, set e is (usato per i boolena al posto di get)
 * 3) la classe deve implementare l'interfaccia Serializable (la fa nella superclasse)
 * 4) la classe non deve contenere nessun metodo per la gestione degli eventi
 * <p>
 * Ha una chiave String e memorizza i valori nel database come byte[].
 * Ha una property per individuare il tipo di dato memorizzato, formata dal nome della classe
 * Mantiene la company a cui la preferenza si riferisce. Se l'applicazione non usa le company
 * o se la preferenza si riferisce a tutte le company, il valore è nullo
 * Può contenere qualsiasi tipo di dato (anche immagini, o in generale qualsiasi oggetto serializzabile).
 * <p>
 * I valori possono essere letti e scritti tramite i metodi delle enum.
 * Si possono creare quante enum si desidera enum (preferenze generali, preferenze dell'azienda,
 * preferenze dell'utente...)
 * La enum contengono i metodi per inserire e recuperare i tipi più comuni
 * (getString, getInteger, getDecimal, getResource, getImage...). Il supporto per i tipi comuni è disponibile nella
 * classe AbsPref, ma è anche possibile creare metodi specifici nella enum per memorizzare qualsiasi
 * tipo serializzabile (putIndirizzo, getIndirizzo, putCertificato, getCertificato...)
 * <p>
 * I valori possono essere letti e scritti tramite i metodi statici della classe, passandogli la chiave.
 */
@Entity
public class Pref extends CompanyEntity {

    //------------------------------------------------------------------------------------------------------------------------
    // Properties
    //------------------------------------------------------------------------------------------------------------------------
    //--versione della classe per la serializzazione
    private static final long serialVersionUID = 1L;

    //--company di riferimento (facoltativa nella superclasse)
    //--company di riferimento (facoltativa in questa classe)
    //--private BaseCompany company;

    public static boolean usaCompany = true;

    //--sigla di codifica interna (obbligatoria, non unica)
    //--va inizializzato con una stringa vuota, per evitare che compaia null nel Form nuovoRecord
    @NotEmpty
    @Column(length = 40)
    @Index
    private String code = "";


    //--sigla di codifica interna specifica per company (obbligatoria, unica)
    //--calcolata -> codeCompanyUnico = company.companyCode + pref.code (20+40=60);
    //--va inizializzato con una stringa vuota, per evitare che compaia null nel Form nuovoRecord
    @NotEmpty
    @Column(length = 60, unique = true)
    @Index
    private String codeCompanyUnico = "";


    //--tipo di dato memorizzato (obbligatorio)
    private PrefType tipo;


    //--tipo di dato memorizzato (deprecato)
//    @NotEmpty
    @Deprecated
    private String classe = "";

    //--valore della preferenza (facoltativo)
    private byte[] value;

    //--company di riferimento (facoltativa)
    //--la property BaseCompany è nella superclasse CompanyEntity

    //--descrizione visibile (facoltativa)
    @Column(length = 200)
    private String descrizione = "";

    //--ordine di presentazione (facoltativo)
    @Index
    private int ordine;

    @Deprecated
    private TypePref type;

    @Deprecated
    private Date dateCreated;
    @Deprecated
    private Date lastUpdated;
    @Deprecated
    private String stringa = "";      // VARCHAR(255)
    @Deprecated
    private Boolean bool = false;     // BIT(1)
    @Deprecated
    private Integer intero = 0;    // INTEGER
    @Deprecated
    private Long lungo = 0L;   // BIGINT(20)
    @Deprecated
    private Float reale = 0F;  // FLOAT
    @Deprecated
    private Double doppio = 0D; // DOUBLE
    @Deprecated
    private BigDecimal decimale = null;// DECIMAL(19,2)
    @Deprecated
    private Date data = null;// DATETIME
    @Deprecated
    private String testo = "";// LONGTEXT


    //------------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Costruttore senza argomenti
     * Obbligatorio per le specifiche JavaBean
     * Da non usare MAI per la creazione diretta di una nuova istanza (si perdono i controlli)
     */
    public Pref() {
    }// end of JavaBean constructor

    /**
     * Costruttore minimo con tutte le properties obbligatorie
     * Filtrato sulla company corrente (che viene regolata nella superclasse CompanyEntity)
     * Il codeCompanyUnico (obbligatorio) viene calcolato in automatico prima del persist
     *
     * @param code sigla di codifica interna specifica per ogni company (obbligatoria, unica all'interno della company)
     * @param tipo di dato (obbligatoria)
     */
    public Pref(String code, PrefType tipo) {
        this((BaseCompany) null, code, tipo);
    }// end of constructor

    /**
     * Costruttore
     * Filtrato sulla company passata come parametro.
     * Siccome la property Company NON è obbligatoria in questa classe,
     * se il valore della company è nullo, NON utilizza la company corrente, regolata nella superclasse
     * ma rimane il valore nullo
     * Il codeCompanyUnico (obbligatorio) viene calcolato in automatico prima del persist
     *
     * @param company di appartenenza (property della superclasse)
     * @param code    sigla di codifica interna specifica per ogni company (obbligatoria, unica all'interno della company)
     * @param tipo    di dato (obbligatoria)
     */
    public Pref(BaseCompany company, String code, PrefType tipo) {
        this(company, code, tipo, null);
    }// end of constructor

    /**
     * Costruttore completo
     * Filtrato sulla company passata come parametro.
     * Siccome la property Company NON è obbligatoria in questa classe,
     * se il valore della company è nullo, NON utilizza la company corrente, regolata nella superclasse
     * ma rimane il valore nullo
     * Il codeCompanyUnico (obbligatorio) viene calcolato in automatico prima del persist
     *
     * @param company di appartenenza (property della superclasse)
     * @param code    sigla di codifica interna specifica per ogni company (obbligatoria, unica all'interno della company)
     * @param tipo    di dato (obbligatoria)
     * @param value   della preferenza
     */
    public Pref(BaseCompany company, String code, PrefType tipo, Object value) {
        super();
        this.setCompany(company);
        this.setCode(code);
        this.setTipo(tipo);
        this.setValore(value);
    }// end of constructor

//    /**
//     * Costruttore minimo con tutte le properties obbligatorie
//     *
//     * @param code sigla di riferimento interna (obbligatoria)
//     * @param tipo di dato (obbligatoria)
//     */
//    public Pref(String code, PrefType tipo) {
//        super();
//        this.setCode(code);
//        this.setTipo(tipo);
//    }// end of constructor
//
//
//    /**
//     * Costruttore completo con tutte le properties obbligatorie
//     *
//     * @param code    sigla di riferimento interna (obbligatoria)
//     * @param tipo    di dato (obbligatoria)
//     * @param company di appartenenza
//     */
//    public Pref(String code, PrefType tipo, BaseCompany company) {
//        super();
//        this.setCode(code);
//        this.setTipo(tipo);
//        super.setCompany(company);
//    }// end of constructor
//
//
//    /**
//     * Costruttore completo con tutte le properties obbligatorie
//     *
//     * @param code    sigla di riferimento interna (obbligatoria)
//     * @param tipo    di dato (obbligatoria)
//     * @param company di appartenenza
//     * @param value   della preferenza
//     */
//    public Pref(String code, PrefType tipo, BaseCompany company, Object value) {
//        super();
//        this.setCode(code);
//        this.setTipo(tipo);
//        super.setCompany(company);
//        this.setValore(value);
//    }// end of constructor

    /**
     * Costruttore minimo con tutte le properties obbligatorie
     *
     * @param code   sigla di riferimento interna (obbligatoria)
     * @param classe nome della classe del tipo di dato (obbligatoria)
     * @deprecated
     */
    public Pref(String code, String classe) {
        super();
        this.setCode(code);
        this.setClasse(classe);
    }// end of constructor

    /**
     * Costruttore completo con tutte le properties obbligatorie
     *
     * @param code    sigla di riferimento interna (obbligatoria)
     * @param classe  nome della classe del tipo di dato (obbligatoria)
     * @param company di appartenenza
     * @deprecated
     */
    public Pref(String code, String classe, BaseCompany company) {
        super();
        this.setCode(code);
        this.setClasse(classe);
        super.setCompany(company);
    }// end of constructor

    //------------------------------------------------------------------------------------------------------------------------
    // Count records
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Recupera il numero totale di records della Entity
     * Senza filtri.
     *
     * @return il numero totale di records nella Entity
     */
    public static int countByAllCompanies() {
        return countByAllCompanies((EntityManager) null);
    }// end of static method

    /**
     * Recupera il numero totale di records della Entity
     * Senza filtri.
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param manager the EntityManager to use
     * @return il numero totale di records nella Entity
     */
    public static int countByAllCompanies(EntityManager manager) {
        return AQuery.count(Pref.class, manager);
    }// end of static method

    /**
     * Recupera il numero di records della Entity
     * Filtrato sulla company corrente.
     *
     * @return il numero filtrato di records nella Entity
     */
    public static int countByCurrentCompany() {
        return countByCurrentCompany((EntityManager) null);
    }// end of static method

    /**
     * Recupera il numero di records della Entity
     * Filtrato sulla company corrente (che viene regolata nella superclasse CompanyEntity)
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param manager the EntityManager to use
     * @return il numero filtrato di records nella Entity
     */
    public static int countByCurrentCompany(EntityManager manager) {
        return countByCompany(CompanySessionLib.getCompany(), manager);
    }// end of static method


    /**
     * Recupera il numero di records della Entity
     * Filtrato sulla company passata come parametro.
     *
     * @param company di appartenenza (property della superclasse)
     * @return il numero filtrato di records nella Entity
     */
    public static int countByCompany(BaseCompany company) {
        return countByCompany(company, (EntityManager) null);
    }// end of static method

    /**
     * Recupera il numero di records della Entity
     * Filtrato sulla company passata come parametro.
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param company di appartenenza (property della superclasse)
     * @param manager the EntityManager to use
     * @return il numero filtrato di records nella Entity
     */
    public static int countByCompany(BaseCompany company, EntityManager manager) {
        return CompanyQuery.count(Pref.class, company, manager);
    }// end of static method

    /**
     * Recupera il numero di records della Entity, filtrato sul valore della property indicata
     * Filtrato sulla company passata come parametro.
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param company di appartenenza (property della superclasse)
     * @param attr    the searched attribute
     * @param value   the value to search for
     * @param manager the EntityManager to use
     * @return il numero filtrato di records nella Entity
     */
    public static int countByCompanyAndProperty(BaseCompany company, SingularAttribute attr, Object value, EntityManager manager) {
        return CompanyQuery.count(Pref.class, attr, value, company, manager);
    }// end of static method

    /**
     * Recupera il totale dei records di Pref
     * Filtrato sulla azienda corrente.
     * Se l'azienda corrente non è selezionata (null), conta senza filtri
     *
     * @return numero totale di records di Pref
     */
    public static int count() {
        return countByCurrentCompany();
    }// end of method


    /**
     * Recupera il totale dei records di Pref
     * Filtrato sulla azienda passata come parametro.
     *
     * @param company di appartenenza
     * @return numero totale di records di Pref
     */
    public static int count(BaseCompany company) {
        return countByCompany(company);
    }// end of method


    /**
     * Recupera il totale dei records di Pref
     * Senza filtri.
     *
     * @return numero totale di records di Pref
     */
    public static int countAll() {
        return countByAllCompanies();
    }// end of method

    //------------------------------------------------------------------------------------------------------------------------
    // Find entity by primary key
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Recupera una istanza della Entity usando la query standard della Primary Key
     * Nessun filtro sulla company, perché la primary key è unica
     *
     * @param id valore (unico) della Primary Key
     * @return istanza della Entity, null se non trovata
     */
    public static Pref find(long id) {
        return find(id, (EntityManager) null);
    }// end of static method


    /**
     * Recupera una istanza della Entity usando la query standard della Primary Key
     * Nessun filtro sulla company, perché la primary key è unica
     * Usa l'EntityManager passato come parametro
     * Se il manager è nullo, costruisce al volo un manager standard (and close it)
     * Se il manager è valido, lo usa (must be close by caller method)
     *
     * @param id      valore (unico) della Primary Key
     * @param manager the EntityManager to use
     * @return istanza della Entity, null se non trovata
     */
    public static Pref find(long id, EntityManager manager) {
        return (Pref) AQuery.find(Pref.class, id, manager);
    }// end of static method


    //------------------------------------------------------------------------------------------------------------------------
    // Get single entity by SingularAttribute
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Recupera una istanza della Entity usando la query di una property specifica
     * Nessun filtro sulla company, perché la property è unica
     *
     * @param codeCompanyUnico sigla di codifica interna (obbligatoria, unica in generale indipendentemente dalla company)
     * @return istanza della Entity, null se non trovata
     */
    public static Pref getEntityByCodeCompanyUnico(String codeCompanyUnico) {
        return getEntityByCodeCompanyUnico(codeCompanyUnico, (EntityManager) null);
    }// end of static method

    /**
     * Recupera una istanza della Entity usando la query di una property specifica
     * Nessun filtro sulla company, perché la property è unica
     *
     * @param codeCompanyUnico sigla di codifica interna (obbligatoria, unica in generale indipendentemente dalla company)
     * @return vero se esiste Entity, false se non trovata
     */
    public static boolean isEntityByCodeCompanyUnico(String codeCompanyUnico) {
        return getEntityByCodeCompanyUnico(codeCompanyUnico) != null;
    }// end of static method

    /**
     * Recupera una istanza della Entity usando la query di una property specifica
     * Nessun filtro sulla company, perché la property è unica
     *
     * @param codeCompanyUnico sigla di codifica interna (obbligatoria, unica in generale indipendentemente dalla company)
     * @param manager          the EntityManager to use
     * @return istanza della Entity, null se non trovata
     */
    public static Pref getEntityByCodeCompanyUnico(String codeCompanyUnico, EntityManager manager) {
        return (Pref) AQuery.getEntity(Pref.class, Pref_.codeCompanyUnico, codeCompanyUnico, manager);
    }// end of static method

    /**
     * Recupera una istanza della Entity usando la query di una property specifica
     * Nessun filtro sulla company, perché la property è unica
     *
     * @param codeCompanyUnico sigla di codifica interna (obbligatoria, unica in generale indipendentemente dalla company)
     * @return vero se esiste Entity, false se non trovata
     */
    public static boolean isEntityByCodeCompanyUnico(String codeCompanyUnico, EntityManager manager) {
        return getEntityByCodeCompanyUnico(codeCompanyUnico, manager) != null;
    }// end of static method


    //------------------------------------------------------------------------------------------------------------------------
    // Get entities (list)
    //------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------------
    // Get properties (list)
    //------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------------
    // New and save
    //------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------------
    // Delete
    //------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------------
    // utilities
    //------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------------
    // Getter and setter
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Recupera una lista di records di Pref
     * Filtrato sulla azienda corrente.
     *
     * @return lista di Pref
     */
    public static ArrayList<Pref> getList() {
        return getList(CompanySessionLib.getCompany());
    }// end of method

    /**
     * Recupera una lista di records di Pref
     * Filtrato sulla azienda passata come parametro.
     *
     * @param company di appartenenza
     * @return lista di Pref
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Pref> getList(BaseCompany company) {
        ArrayList<Pref> lista = null;
        Vector vettore = null;


        if (company != null) {
            vettore = (Vector) CompanyQuery.queryList(Pref.class, company);
        } else {
            vettore = (Vector) AQuery.getListOld(Pref.class);
        }// end of if/else cycle

        if (vettore != null) {
            lista = new ArrayList<Pref>(vettore);
        }// end of if cycle

        return lista;
    }// end of method

//    /**
//     * Saves this entity to the database.
//     * <p>
//     * If the provided EntityManager has an active transaction, the operation is performed inside the transaction.<br>
//     * Otherwise, a new transaction is used to save this single entity.
//     *
//     * @param company azienda da filtrare
//     * @return the merged Entity (new entity, unmanaged, has the id)
//     */
//    public BaseEntity save(BaseCompany company) {
//        return save(company, (EntityManager)null);
//    }// end of method

    /**
     * Recupera una lista di records di Pref
     * Senza filtri.
     *
     * @return lista di Pref
     */
    public static ArrayList<Pref> getListAll() {
        return getList(null);
    }// end of method

//    /**
//     * Saves this entity to the database.
//     * Usa l'EntityManager di default
//     *
//     * @return the merged Entity (new entity, unmanaged, has the id), casted as Funzione
//     */
//    public Pref saveSafe() {
//        return save(CompanySessionLib.getCompany(),(EntityManager) null);
//    }// end of method
//
//    /**
//     * Saves this entity to the database.
//     * <p>
//     * If the provided EntityManager has an active transaction, the operation is performed inside the transaction.<br>
//     * Otherwise, a new transaction is used to save this single entity.
//     *
//     * @param company azienda da filtrare
//     * @return the merged Entity (new entity, unmanaged, has the id), casted as Pref
//     */
//    public Pref saveSafe(BaseCompany company) {
//        return save(company, (EntityManager)null);
//    }// end of method
//
//    /**
//     * Saves this entity to the database.
//     * <p>
//     * If the provided EntityManager has an active transaction, the operation is performed inside the transaction.<br>
//     * Otherwise, a new transaction is used to save this single entity.
//     *
//     * @param manager the entity manager to use (if null, a new one is created on the fly)
//     * @return the merged Entity (new entity, unmanaged, has the id), casted as Funzione
//     */
//    public Pref saveSafe(EntityManager manager) {
//        return  save(CompanySessionLib.getCompany(),manager);
//    }// end of method

    //------------------------------------------------------------------------------------------------------------------------
    // Utilities
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Checks if this preference exists in the storage.
     * <p>
     *
     * @param code sigla di riferimento interna (obbligatoria)
     * @return true if the preference exists
     */
    public static boolean exists(String code) {
        return (findByCode(code) != null);
    }// end of method

    /**
     * Recupera una istanza di Pref usando la query della property chiave
     * Filtrato sulla azienda corrente.
     *
     * @param code sigla di riferimento interna (obbligatoria)
     * @return istanza di Pref, null se non trovata
     */
    public static Pref findByCode(String code) {
        return findByCode(code, CompanySessionLib.getCompany());
    }// end of method

    /**
     * Recupera una istanza di Pref usando la query della property chiave
     * Filtrato sulla azienda passata come parametro.
     *
     * @param code    sigla di riferimento interna (obbligatoria)
     * @param company di appartenenza
     * @return istanza di Pref, null se non trovata
     */
    public static Pref findByCode(String code, BaseCompany company) {
        String codeUnico = LibText.creaChiave(company, code);
        return findByCodeUnico(codeUnico);
    }// end of method

    /**
     * Recupera una istanza di Pref usando la query della property chiave
     *
     * @param codeCompanyUnico sigla di codifica interna specifica per company
     * @return istanza di Pref, null se non trovata
     */
    @SuppressWarnings("unchecked")
    public static Pref findByCodeUnico(String codeCompanyUnico) {
        Pref instance = null;
        BaseEntity bean;

        EntityManager manager = EM.createEntityManager();
        bean = AQuery.queryOne(Pref.class, Pref_.codeCompanyUnico, codeCompanyUnico, manager);
        manager.close();

        if (bean != null && bean instanceof Pref) {
            instance = (Pref) bean;
        }// end of if cycle

        return instance;
    }// end of method

    /**
     * Creazione iniziale di una istanza della classe
     * La crea SOLO se non esiste già
     *
     * @param code   sigla di riferimento interna (obbligatoria)
     * @param classe nome della classe del tipo di dato (obbligatoria)
     * @return istanza di Pref
     * @deprecated
     */
    public static Pref crea(String code, String classe) {
        Pref pref = Pref.findByCode(code);

        if (pref == null) {
            pref = new Pref(code, classe);
            pref.save();
        }// end of if cycle

        return pref;
    }// end of static method

    /**
     * Creazione iniziale di una istanza della classe
     * La crea SOLO se non esiste già
     *
     * @param code sigla di riferimento interna (obbligatoria)
     * @param tipo di dato (obbligatoria)
     * @return istanza di Pref
     */
    public static Pref crea(String code, PrefType tipo) {
        Pref pref = Pref.findByCode(code);

        if (pref == null) {
            pref = new Pref(code, tipo);
            pref.save();
        }// end of if cycle

        return pref;
    }// end of static method

    /**
     * Creazione iniziale di una istanza della classe
     * La crea SOLO se non esiste già
     *
     * @param code    sigla di riferimento interna (obbligatoria)
     * @param classe  nome della classe del tipo di dato (obbligatoria)
     * @param company di appartenenza
     * @return istanza di Pref
     * @deprecated
     */
    public static Pref crea(String code, String classe, BaseCompany company) {
        Pref pref = Pref.findByCode(code);

        if (pref == null) {
            pref = new Pref(code, classe, company);
            pref.save();
        }// end of if cycle

        return pref;
    }// end of static method

    /**
     * Creazione iniziale di una istanza della classe
     * La crea SOLO se non esiste già
     *
     * @param code    sigla di riferimento interna (obbligatoria)
     * @param company di appartenenza
     * @param tipo    di dato (obbligatoria)
     * @return istanza di Prefm
     */
    public static Pref crea(String code, BaseCompany company, PrefType tipo) {
        Pref pref = Pref.findByCode(code, company);

        if (pref == null) {
            pref = new Pref(company, code, tipo);
            pref.save();
        }// end of if cycle

        return pref;
    }// end of static method

    /**
     * Creazione iniziale di una istanza della classe
     * La crea SOLO se non esiste già
     *
     * @param code        sigla di riferimento interna (obbligatoria)
     * @param company     di appartenenza
     * @param tipo        di dato (obbligatoria)
     * @param descrizione visibile
     * @return istanza di Prefm
     */
    public static Pref crea(String code,  PrefType tipo, Object value) {
        BaseCompany company = CompanySessionLib.getCompany();
        Pref pref = Pref.findByCode(code, company);

        if (pref == null) {
            pref = new Pref(company, code, tipo);
            pref.save();
        }// end of if cycle

        return pref;
    }// end of static method

    /**
     * Creazione iniziale di una istanza della classe
     * La crea SOLO se non esiste già
     *
     * @param code        sigla di riferimento interna (obbligatoria)
     * @param company     di appartenenza
     * @param tipo        di dato (obbligatoria)
     * @param descrizione visibile
     * @return istanza di Prefm
     */
    public static Pref crea(String code, BaseCompany company, PrefType tipo, String descrizione) {
        Pref pref = Pref.findByCode(code, company);

        if (pref == null) {
            pref = new Pref(company, code, tipo);
            pref.setDescrizione(descrizione);
            pref.save();
        }// end of if cycle

        return pref;
    }// end of static method

    /**
     * Creazione iniziale di una istanza della classe
     * La crea SOLO se non esiste già
     *
     * @param code        sigla di riferimento interna (obbligatoria)
     * @param company     di appartenenza
     * @param tipo        di dato (obbligatoria)
     * @param descrizione visibile
     * @param value       della preferenza
     * @return istanza di Prefm
     */
    public static Pref crea(String code, BaseCompany company, PrefType tipo, String descrizione, Object value) {
        Pref pref = Pref.findByCode(code, company);

        if (pref == null) {
            pref = new Pref(company, code, tipo, value);
            pref.setDescrizione(descrizione);
            pref.save();
        }// end of if cycle

        return pref;
    }// end of static method




    public static Object get(String code) {
        return findByCode(code).getBoolean();
    } // end of static method

    public static String getString(String code) {
        return getString(code, "");
    } // end of static method

    public static String getString(String code, Object defaultValue) {
        return getString(code, CompanySessionLib.getCompany(), defaultValue);
    } // end of static method

    public static String getString(String code, BaseCompany company) {
        return getString(code, company, "");
    } // end of static method

    public static String getString(String code, BaseCompany company, Object defaultValue) {
        Pref pref = Pref.findByCode(code, company);

        if (pref != null) {
            return (String) pref.getValore();
        }// end of if cycle

        if (defaultValue != null && defaultValue instanceof String) {
            return (String) defaultValue;
        }// end of if cycle

        return null;
    } // end of static method

    public static Boolean getBool(String code) {
        return getBool(code, "");
    } // end of static method

    public static Boolean getBool(String code, Object defaultValue) {
        return getBool(code, CompanySessionLib.getCompany(), defaultValue);
    } // end of static method

    public static Boolean getBool(String code, BaseCompany company) {
        return getBool(code, company, "");
    } // end of static method

    public static Boolean getBool(String code, BaseCompany company, Object defaultValue) {
        Pref pref = Pref.findByCode(code, company);

        if (pref != null) {
            return (boolean) pref.getValore();
        }// end of if cycle

        if (defaultValue != null && defaultValue instanceof Boolean) {
            return (boolean) defaultValue;
        }// end of if cycle

        return false;
    } // end of static method

    public static int getInt(String code) {
        return getInt(code, "");
    } // end of static method

    public static int getInt(String code, Object defaultValue) {
        return getInt(code, CompanySessionLib.getCompany(), defaultValue);
    } // end of static method

    public static int getInt(String code, BaseCompany company) {
        return getInt(code, company, "");
    } // end of static method

    public static int getInt(String code, BaseCompany company, Object defaultValue) {
        Pref pref = Pref.findByCode(code, company);

        if (pref != null) {
            return (int) pref.getValore();
        }// end of if cycle

        if (defaultValue != null && defaultValue instanceof Integer) {
            return (int) defaultValue;
        }// end of if cycle

        return 0;
    } // end of static method

    public static BigDecimal getDecimal(String code) {
        return getDecimal(code, "");
    } // end of static method

    public static BigDecimal getDecimal(String code, Object defaultValue) {
        return getDecimal(code, CompanySessionLib.getCompany(), defaultValue);
    } // end of static method

    public static BigDecimal getDecimal(String code, BaseCompany company) {
        return getDecimal(code, company, "");
    } // end of static method

    public static BigDecimal getDecimal(String code, BaseCompany company, Object defaultValue) {
        Pref pref = Pref.findByCode(code, company);

        if (pref != null) {
            return (BigDecimal) pref.getValore();
        }// end of if cycle

        if (defaultValue != null && defaultValue instanceof BigDecimal) {
            return (BigDecimal) defaultValue;
        }// end of if cycle

        return null;
    } // end of static method

    public static Date getDate(String code) {
        return getDate(code, "");
    } // end of static method

    public static Date getDate(String code, Object defaultValue) {
        return getDate(code, CompanySessionLib.getCompany(), defaultValue);
    } // end of static method

    public static Date getDate(String code, BaseCompany company) {
        return getDate(code, company, "");
    } // end of static method

    public static Date getDate(String code, BaseCompany company, Object defaultValue) {
        Pref pref = Pref.findByCode(code, company);

        if (pref != null) {
            return (Date) pref.getValore();
        }// end of if cycle

        if (defaultValue != null && defaultValue instanceof Date) {
            return (Date) defaultValue;
        }// end of if cycle

        return null;
    } // end of static method

    public static Image getImage(String code) {
        return getImage(code, "");
    } // end of static method

    public static Image getImage(String code, Object defaultValue) {
        return getImage(code, CompanySessionLib.getCompany(), defaultValue);
    } // end of static method

    public static Image getImage(String code, BaseCompany company) {
        return getImage(code, company, "");
    } // end of static method

    public static Image getImage(String code, BaseCompany company, Object defaultValue) {
        Pref pref = Pref.findByCode(code, company);

        if (pref != null) {
            return (Image) pref.getValore();
        }// end of if cycle

        if (defaultValue != null && defaultValue instanceof Image) {
            return (Image) defaultValue;
        }// end of if cycle

        return null;
    } // end of static method

    public static Resource getResource(String code) {
        return getResource(code, "");
    } // end of static method

    public static Resource getResource(String code, Object defaultValue) {
        return getResource(code, CompanySessionLib.getCompany(), defaultValue);
    } // end of static method

    public static Resource getResource(String code, BaseCompany company) {
        return getResource(code, company, "");
    } // end of static method

    public static Resource getResource(String code, BaseCompany company, Object defaultValue) {
        Pref pref = Pref.findByCode(code, company);

        if (pref != null) {
            return (Resource) pref.getValore();
        }// end of if cycle

        if (defaultValue != null && defaultValue instanceof Resource) {
            return (Resource) defaultValue;
        }// end of if cycle

        return null;
    } // end of static method

    public static byte[] getBytes(String code) {
        return getBytes(code, "");
    } // end of static method

    public static byte[] getBytes(String code, Object defaultValue) {
        return getBytes(code, CompanySessionLib.getCompany(), defaultValue);
    } // end of static method

    public static byte[] getBytes(String code, BaseCompany company) {
        return getBytes(code, company, "");
    } // end of static method

    public static byte[] getBytes(String code, BaseCompany company, Object defaultValue) {
        Pref pref = Pref.findByCode(code, company);

        if (pref != null) {
            return (byte[]) pref.getValore();
        }// end of if cycle

        if (defaultValue != null && defaultValue instanceof byte[]) {
            return (byte[]) defaultValue;
        }// end of if cycle

        return null;
    } // end of static method

    /**
     * Inserimento del valore della preferenza
     * Se non esiste, la crea
     * Filtrato sulla azienda corrente.
     *
     * @param code  sigla di riferimento interna (obbligatoria)
     * @param typo  di dato (obbligatoria)
     * @param value della preferenza
     */
    public static void put(String code, PrefType typo, Object value) {
        put(code, CompanySessionLib.getCompany(), typo, value);
    } // end of method

    /**
     * Inserimento del valore della preferenza
     * Se non esiste, la crea
     * Filtrato sulla azienda passata come parametro.
     *
     * @param code    sigla di riferimento interna (obbligatoria)
     * @param company di appartenenza
     * @param typo    di dato (obbligatoria)
     * @param value   della preferenza
     */
    public static void put(String code, BaseCompany company, PrefType typo, Object value) {
        put(code, company, typo, value, "");
    } // end of method

    /**
     * Inserimento del valore della preferenza
     * Se non esiste, la crea
     * Filtrato sulla azienda passata come parametro.
     *
     * @param code        sigla di riferimento interna (obbligatoria)
     * @param company     di appartenenza
     * @param typo        di dato (obbligatoria)
     * @param value       della preferenza
     * @param descrizione visibile
     */
    public static void put(String code, BaseCompany company, PrefType typo, Object value, String descrizione) {
        Pref.crea(code, company, typo, descrizione); // La crea SOLO se non esiste già
        set(code, company, value);  // chiama il metodo per inserire il valore
    } // end of method

    /**
     * Inserimento del valore della preferenza
     * Assume che la preferenza esista già
     * Filtrato sulla azienda corrente.
     *
     * @param code  sigla di riferimento interna (obbligatoria)
     * @param value della preferenza
     */
    public static void set(String code, Object value) {
        set(code, CompanySessionLib.getCompany(), value);
    } // end of method

    /**
     * Inserimento del valore della preferenza
     * Assume che la preferenza esista già
     * Filtrato sulla azienda passata come parametro.
     *
     * @param code  sigla di riferimento interna (obbligatoria)
     * @param value della preferenza
     */
    public static void set(String code, BaseCompany company, Object value) {
        Pref pref = Pref.findByCode(code, company);

        if (pref != null) {
            pref.setValore(value);
            pref.save();
        }// end of if cycle

    } // end of method

    public static void remove(String code) {
        remove(code, CompanySessionLib.getCompany());
    } // end of method

    public static void remove(String code, BaseCompany company) {
        Pref pref = Pref.findByCode(code, company);

        if (pref != null) {
            pref.delete();
        }// end of if cycle
    } // end of method

    /**
     * @deprecated
     */
    public static Boolean getBoolean(String code) {
        return findByCode(code).getBoolean();
    } // end of method

    /**
     * @deprecated
     */
    public static Boolean getBoolean(String code, boolean suggerito) {
        Pref pref = findByCode(code);

        if (pref != null && pref.type == TypePref.booleano) {
            return pref.getBoolean();
        } else {
            return suggerito;
        }// fine del blocco if-else
    } // end of method

    /**
     * @deprecated
     */
    public static String getStr(String code) {
        return findByCode(code).getStringa();
    } // end of method

    /**
     * @deprecated
     */
    public static String getStr(String code, String suggerito) {
        Pref pref = findByCode(code);

        if (pref != null && pref.type == TypePref.stringa) {
            return pref.getStringa();
        } else {
            return suggerito;
        }// fine del blocco if-else
    } // end of method

    /**
     * @deprecated
     */
    public static Integer getInteger(String code) {
        return findByCode(code).getInteger();
    } // end of method

    /**
     * @deprecated
     */
    public static Integer getInteger(String code, int suggerito) {
        Pref pref = findByCode(code);

        if (pref != null && pref.type == TypePref.intero) {
            return pref.getInteger();
        } else {
            return suggerito;
        }// fine del blocco if-else
    } // end of method

    /**
     * @deprecated
     */
    public static Date getDataOld(String code) {
        return findByCode(code).getDataOld();
    } // end of method

    /**
     * @deprecated
     */
    public static Date getDataOld(String code, Date suggerito) {
        Pref pref = findByCode(code);

        if (pref != null && pref.type == TypePref.data) {
            return pref.getDataOld();
        } else {
            return suggerito;
        }// fine del blocco if-else
    } // end of method

    /**
     * Delete all the records for the Entity class
     * Bulk delete records with CriteriaDelete
     */
    public static void deleteAll() {
        EntityManager manager = EM.createEntityManager();
        deleteAll(manager);
        manager.close();
    }// end of static method

    /**
     * Delete all the records for the Entity class
     * Bulk delete records with CriteriaDelete
     *
     * @param manager the EntityManager to use
     */
    public static void deleteAll(EntityManager manager) {
        AQuery.deleteAll(Pref.class, manager);
    }// end of static method

    //------------------------------------------------------------------------------------------------------------------------
    // Save
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Saves this entity to the database using a local EntityManager
     * Siccome la property Company NON è obbligatoria in questa classe,
     * se il valore della company è nullo, NON utilizza la company corrente, regolata nella superclasse
     * ma rimane il valore nullo
     *
     * @return the merged Entity (new entity, unmanaged, has the id)
     */
    @Override
    public BaseEntity save() {
        return this.save((EntityManager) null);
    }// end of method

    /**
     * Saves this entity to the database using a local EntityManager
     * Siccome la property Company NON è obbligatoria in questa classe,
     * se il valore della company è nullo, NON utilizza la company corrente, regolata nella superclasse
     * ma rimane il valore nullo
     *
     * @param manager the entity manager to use (if null, a new one is created on the fly)
     * @return the merged Entity (new entity, unmanaged, has the id)
     */
    @Override
    public BaseEntity save(EntityManager manager) {
        return this.saveSafe(manager);
    }// end of method

    /**
     * Saves this entity to the database.
     * Siccome la property Company NON è obbligatoria in questa classe,
     * se il valore della company è nullo, NON utilizza la company corrente, regolata nella superclasse
     * ma rimane il valore nullo
     *
     * @return the merged Entity (new entity, unmanaged, has the id)
     */
    public Pref saveSafe() {
        return this.saveSafe((EntityManager) null);
    }// end of method

    /**
     * Saves this entity to the database.
     * Siccome la property Company NON è obbligatoria in questa classe,
     * se il valore della company è nullo, NON utilizza la company corrente, regolata nella superclasse
     * ma rimane il valore nullo
     * <p>
     * If the provided EntityManager has an active transaction, the operation is performed inside the transaction.<br>
     * Otherwise, a new transaction is used to save this single entity.
     *
     * @param manager the entity manager to use (if null, a new one is created on the fly)
     * @return the merged Entity (new entity, unmanaged, has the id)
     */
    public Pref saveSafe(EntityManager manager) {
        boolean valido;

//        valido = this.checkCompany(manager);
        valido = this.checkChiave(manager);

        if (valido) {
            return (Pref) super.save(manager);
        } else {
            return null;
        }// end of if/else cycle

    }// end of method
    //------------------------------------------------------------------------------------------------------------------------
    // utilities
    //------------------------------------------------------------------------------------------------------------------------


//    /**
//     * Implementa come business logic, la obbligatorietà della company
//     * <p>
//     *
//     * @return true se esiste, false se non esiste
//     */
//    private boolean checkCompany(EntityManager manager) {
//    } // end of method

    /**
     * Controlla l'esistenza della chiave univoca, PRIMA di salvare il valore nel DB
     * La crea se non esiste già
     *
     * @param manager the entity manager to use (if null, a new one is created on the fly)
     */
    private boolean checkChiave(EntityManager manager) {
        boolean valido = true;

        codeCompanyUnico = LibText.creaChiave(getCompany(), code);
        if (codeCompanyUnico.equals("") || isEsistente(codeCompanyUnico, manager)) {
            valido = false;
        }// end of if cycle

        return valido;
    } // end of method

    public boolean isEsistente(String codeCompanyUnico, EntityManager manager) {
        return this.getId() == null && Pref.isEntityByCodeCompanyUnico(codeCompanyUnico, manager);
    } // end of method
    //------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------------

    public Object getValore() {
        Object obj = null;
        PrefType typo = getTipo();

        if (typo != null) {
            obj = typo.bytesToObject(value);
        }// end of if cycle

        return obj;
    }// end of getter method


    public void setValore(Object obj) {
        PrefType typo = getTipo();

        if (typo != null) {
            value = typo.objectToBytes(obj);
        }// end of if cycle
    }//end of setter method


    public byte[] getValue() {
        return value;
    }// end of getter method

    public void setValue(byte[] value) {
        this.value = value;
    }//end of setter method


    public Integer getInteger() {
        if (type == TypePref.intero) {
            return intero;
        } else {
            return null;
        }// fine del blocco if-else
    } // end of method

    public int getOrdine() {
        return ordine;
    }

    public void setOrdine(int ordine) {
        this.ordine = ordine;
    }

    public TypePref getType() {
        return type;
    }

    public void setType(TypePref type) {
        this.type = type;
    }

    public PrefType getTipo() {
        return tipo;
    }// end of getter method

    public void setTipo(PrefType tipo) {
        this.tipo = tipo;
    }//end of setter method

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeCompanyUnico() {
        return codeCompanyUnico;
    }// end of getter method

    public void setCodeCompanyUnico(String codeCompanyUnico) {
        this.codeCompanyUnico = codeCompanyUnico;
    }//end of setter method

    public String getClasse() {
        return classe;
    }// end of getter method

    public void setClasse(String classe) {
        this.classe = classe;
    }//end of setter method

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getStringa() {
        return stringa;
    }

    public void setStringa(String stringa) {
        this.stringa = stringa;
    }

    public Integer getIntero() {
        return intero;
    }

    public void setIntero(Integer intero) {
        this.intero = intero;
    }

    public Long getLungo() {
        return lungo;
    }

    public void setLungo(Long lungo) {
        this.lungo = lungo;
    }

    public Float getReale() {
        return reale;
    }

    public void setReale(Float reale) {
        this.reale = reale;
    }

    public Double getDoppio() {
        return doppio;
    }

    public void setDoppio(Double doppio) {
        this.doppio = doppio;
    }

    public BigDecimal getDecimale() {
        return decimale;
    }

    public void setDecimale(BigDecimal decimale) {
        this.decimale = decimale;
    }

    public Date getDataOld() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }


    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public Boolean getBoolean() {
        if (type == TypePref.booleano) {
            return bool;
        } else {
            return null;
        }// fine del blocco if-else
    } // end of method

    public void setBool(Boolean bool) {
        this.bool = bool;
    }

    @Override
    public Pref clone() throws CloneNotSupportedException {
        try {
            return (Pref) BeanUtils.cloneBean(this);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }// fine del blocco try-catch
    }// end of method

}// end of entity class
