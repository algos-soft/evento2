package it.algos.webbase.domain.company;

import it.algos.webbase.domain.pref.Pref;
import it.algos.webbase.multiazienda.CompanyEntity_;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.entity.DefaultSort;
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.field.AIField;
import it.algos.webbase.web.field.AFType;
import it.algos.webbase.web.query.AQuery;
import it.algos.webbase.web.query.EntityQuery;
import org.eclipse.persistence.annotations.Index;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Classe di tipo JavaBean
 * 1) la classe deve avere un costruttore senza argomenti
 * 2) le proprietà devono essere private e accessibili solo con get, set e is (usato per i boolena al posto di get)
 * 3) la classe deve implementare l'interfaccia Serializable (la fa nella superclasse)
 * 4) la classe non deve contenere nessun metodo per la gestione degli eventi
 */
@Entity
@MappedSuperclass
@Table(name = "COMPANY")
@DefaultSort({"companyCode"})
public class BaseCompany extends BaseEntity {

    private static final long serialVersionUID = 8238775575826490450L;
    public static EntityQuery<BaseCompany> query = new EntityQuery(BaseCompany.class);

    // CascadeType.ALL: quando chiamo persist sul padre, persiste automaticamente tutti i nuovi figli aggiunti
    // alla lista e non ancora registrati (e così per tutte le operazioni dell'EntityManager)
    // orphanRemoval = true: quando registro il padre, cancella tutti i figli eventualmente rimasti orfani.
    // CascadeOnDelete: instaura l'integrità referenziale a livello di database (foreign key on delete cascade)
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pref> preferenze;

    //--sigla di riferimento interna (interna, obbligatoria ed unica)
    @NotEmpty
    @Column(length = 20, unique = true)
    @Index
    private String companyCode = "";

    //--ragione sociale o descrizione della company (visibile - obbligatoria)
    @NotEmpty
    @AIField(type = AFType.text, required = true, width = "27em", caption = "Nome", prompt = "nome", help = "Descrizione completa", error = "Manca il nome")
    private String name = "";

    @Email
    @AIField(type = AFType.text, width = "20em", caption = "Mail", prompt = "pinco.pallino@mail.com", help = "Indirizzo di posta elettronica.")
    private String email = "";

    private String username = "";

    private String password = "";

    @AIField(type = AFType.text, width = "27em", caption = "Indirizzo", help = "Indirizzo postale.")
    private String address1 = "";

    private String address2 = "";

    // persona di riferimento (facoltativo)
    @AIField(type = AFType.text, width = "20em", caption = "Riferimento", help = "Persona di riferimento.")
    private String contact = "";

    //--eventuali note (facoltativo)
    @AIField(type = AFType.text, width = "27em", caption = "Note", help = "Eventuali note aggiuntive.")
    private String note = "";

    private String contractType = "";

    @Temporal(TemporalType.DATE)
    private Date contractStart;

    @Temporal(TemporalType.DATE)
    private Date contractEnd;

    /**
     * Costruttore senza argomenti
     * Obbligatorio per le specifiche JavaBean
     * Da non usare MAI per la creazione diretta di una nuova istanza (si perdono i controlli)
     */
    public BaseCompany() {
    }// end of JavaBean constructor

    /**
     * Costruttore minimo con tutte le properties obbligatorie
     * Filtrato sulla company corrente (che viene regolata nella superclasse CompanyEntity)
     * Il codeCompanyUnico (obbligatorio) viene calcolato in automatico prima del persist
     * Se manca l'ordine di presentazione o è uguale a zero, viene calcolato in automatico prima del persist
     *
     * @param companyCode sigla di riferimento interna (obbligatoria ed unica)
     * @param companyName descrizione della company (obbligatoria)
     */
    public BaseCompany(String companyCode, String companyName) {
        super();
        this.setCompanyCode(companyCode);
        this.setName(companyName);
    }// end of constructor

    /**
     * Recupera il totale dei records di questa classe
     * Senza filtri.
     *
     * @return numero totale di records
     */
    public static int count() {
        int totRec = 0;
        long totTmp;

        totTmp = AQuery.getCount(BaseCompany.class);

        if (totTmp > 0) {
            totRec = (int) totTmp;
        }// fine del blocco if

        return totRec;
    }// end of method

    /**
     * Recupera una lista di tutti i records
     * Senza filtri.
     *
     * @return lista di istanze della classe
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<BaseCompany> getList() {
        ArrayList<BaseCompany> lista = null;
        Vector vettore = (Vector) AQuery.getListOld(BaseCompany.class);

        if (vettore != null) {
            lista = new ArrayList<BaseCompany>(vettore);
        }// end of if cycle

        return lista;
    }// end of method

    /**
     * Controlla l'esistenza del record usando la property unica
     *
     * @param companyCode sigla di riferimento interna (obbligatoria ed unica)
     * @return istanza della classe, null se non trovata
     */
    public static boolean isEsiste(String companyCode) {
        boolean esiste = false;
        BaseEntity entity = AQuery.queryOne(BaseCompany.class, BaseCompany_.companyCode, companyCode);

        if (entity != null) {
            esiste = true;
        }// end of if cycle

        return esiste;
    }// end of static method

    /**
     * Controlla la non-esistenza del record usando la property unica
     *
     * @param companyCode sigla di riferimento interna (obbligatoria ed unica)
     * @return istanza della classe, null se non trovata
     */
    public static boolean isNonEsiste(String companyCode) {
        return !isEsiste(companyCode);
    }// end of static method

    /**
     * Recupera una istanza della classe usando la primary key
     *
     * @param idKey primary key (automatica)
     * @return istanza della classe, null se non trovata
     */
    public static BaseCompany find(long idKey) {
        BaseCompany instance = null;
        BaseEntity entity = AQuery.queryById(BaseCompany.class, idKey);

        if (entity != null) {
            if (entity instanceof BaseCompany) {
                instance = (BaseCompany) entity;
            }// end of if cycle
        }// end of if cycle

        return instance;
    }// end of static method

    /**
     * Recupera una istanza della classe usando la query specifica
     *
     * @param companyCode sigla di riferimento interna (obbligatoria ed unica)
     * @return istanza della classe, null se non trovata
     */
    public static BaseCompany findByCode(String companyCode) {
        BaseCompany instance = null;
        BaseEntity entity = AQuery.queryOne(BaseCompany.class, BaseCompany_.companyCode, companyCode);

        if (entity != null) {
            if (entity instanceof BaseCompany) {
                instance = (BaseCompany) entity;
            }// end of if cycle
        }// end of if cycle

        return instance;
    }// end of static method

    /**
     * Recupera una istanza della classe usando la query specifica
     *
     * @param companyCode sigla di riferimento interna (obbligatoria ed unica)
     * @return istanza della classe, null se non trovata
     */
    public static BaseCompany findByCode(String companyCode, EntityManager manager) {
        BaseCompany instance = null;
        BaseEntity entity = AQuery.getEntity(BaseCompany.class, BaseCompany_.companyCode, companyCode, manager);

        if (entity != null) {
            if (entity instanceof BaseCompany) {
                instance = (BaseCompany) entity;
            }// end of if cycle
        }// end of if cycle

        return instance;
    }// end of static method

    /**
     * Creazione iniziale di una istanza della classe
     * La crea SOLO se non esiste già
     *
     * @param companyCode sigla di riferimento interna (obbligatoria)
     * @param companyName descrizione della company (obbligatoria)
     * @return istanza della classe
     */
    public static BaseCompany crea(String companyCode, String companyName) {
        return crea(companyCode, companyName, "", "", "", "", null, null);
    }// end of static method

    /**
     * Creazione iniziale di una istanza della classe
     * La crea SOLO se non esiste già
     *
     * @param companyCode   sigla di riferimento interna (obbligatoria)
     * @param companyName   descrizione della company (obbligatoria)
     * @param address1      principale della company (facoltativa)
     * @param email         della company (facoltativa)
     * @param contact       persona di riferimento della company (facoltativa)
     * @param contractType  tipologia del contratto (eventuale)
     * @param contractStart inizio del contratto (eventuale)
     * @param contractEnd   fine del contratto (eventuale)
     * @return istanza della classe
     */
    public static BaseCompany crea(
            String companyCode,
            String companyName,
            String address1,
            String email,
            String contact,
            String contractType,
            Date contractStart,
            Date contractEnd) {
        BaseCompany company = null;

        if (isNonEsiste(companyCode)) {
            company = new BaseCompany(companyCode, companyName);

            company.setAddress1(address1);
            company.setEmail(email);
            company.setContact(contact);
            company.setContractType(contractType);
            company.setContractStart(contractStart);
            company.setContractEnd(contractEnd);

            company.save();
        }// end of if cycle

        return company;
    }// end of static method

//    /**
//     * Cancellazione di tutti i records
//     */
//    public static void deleteAll() {
//        for (BaseCompany company : BaseCompany.getListOld()) {
//            company.delete();
//        }// end of for cycle
//    }// end of static method

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
        AQuery.deleteAll(BaseCompany.class, manager);
    }// end of static method

    /**
     * Ritorna la Company corrente.
     *
     * @return la Company corrente
     */
    public static BaseCompany getCurrent() {
        return CompanySessionLib.getCompany();
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the address1
     */
    public String getAddress1() {
        return address1;
    }

    /**
     * @param address1 the address1 to set
     */
    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    /**
     * @return the address2
     */
    public String getAddress2() {
        return address2;
    }

    /**
     * @param address2 the address2 to set
     */
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    /**
     * @return the contact
     */
    public String getContact() {
        return contact;
    }

    /**
     * @param contact the contact to set
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @return the contractType
     */
    public String getContractType() {
        return contractType;
    }

    /**
     * @param contractType the contractType to set
     */
    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    /**
     * @return the contractStart
     */
    public Date getContractStart() {
        return contractStart;
    }

    /**
     * @param contractStart the contractStart to set
     */
    public void setContractStart(Date contractStart) {
        this.contractStart = contractStart;
    }

    /**
     * @return the contractEnd
     */
    public Date getContractEnd() {
        return contractEnd;
    }

    /**
     * @param contractEnd the contractEnd to set
     */
    public void setContractEnd(Date contractEnd) {
        this.contractEnd = contractEnd;
    }

    @Override
    public String toString() {
        return getCompanyCode();
    }// end of method

    ;

    public void createDemoData() {
    }

    public List<Pref> getPreferenze() {
        return preferenze;
    }// end of getter method

    public void setPreferenze(List<Pref> preferenze) {
        this.preferenze = preferenze;
    }//end of setter method

    /**
     * Elimina l'azienda.
     */
    public boolean delete() {
        return delete((EntityManager) null);
    }// end of method

    /**
     * Elimina l'azienda.
     *
     * @param manager the EntityManager to use
     */
    public boolean delete(EntityManager manager) {
//        AQuery.delete(BaseCompany.class, manager);
        deleteAllData(manager);
        return super.delete(manager);
    }// end of method


    /**
     * Elimina tutti i dati di questa azienda.
     */
    public void deleteAllData() {
        deleteAllData((EntityManager) null);
    }// end of method

    /**
     * Elimina tutti i dati di questa azienda.
     * <p>
     * L'ordine di cancellazione è critico per l'integrità referenziale
     *
     * @param manager the EntityManager to use
     */
    public void deleteAllData(EntityManager manager) {
        // elimina le preferenze
        AQuery.delete(Pref.class, CompanyEntity_.company, this, manager);
    }// end of method

}// end of entity class



