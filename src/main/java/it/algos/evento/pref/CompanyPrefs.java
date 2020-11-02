package it.algos.evento.pref;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.server.Resource;
import com.vaadin.ui.Image;
import it.algos.evento.EventoApp;
import it.algos.evento.entities.evento.Evento_;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.lib.LibImage;
import it.algos.webbase.web.lib.LibPref;
import it.algos.webbase.web.lib.LibResource;
import it.algos.webbase.web.pref.AbsPref;
import it.algos.webbase.web.pref.AbsPref.PrefType;
import it.algos.webbase.web.pref.PrefIF;
import it.algos.webbase.web.query.AQuery;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Enum delle preferenze del programma con i relativi valori di default.
 * <p>
 * All'avvio del servlet controlla l'esistenza di tutte le preferenze e aggiunge
 * quelle eventualmente mancanti.
 */
public enum CompanyPrefs implements PrefIF {
    nextNumPren("nextNumPren", PrefType.integer, 1),

    ggScadConfermaPrenotazione("ggScadConfermaPrenotazione", PrefType.integer, 7),

    ggScadConfermaPagamento("ggScadConfermaPagamento", PrefType.integer, 60),

    ggProlungamentoConfDopoSollecito("ggProlungamentoConfDopoSollecito", PrefType.integer, 7),

    ggProlungamentoPagamDopoSollecito("ggProlungamentoPagamDopoSollecito", PrefType.integer, 7),

    senderEmailAddress("senderEmailAddress", PrefType.string, ""),

    backupEmail("sendMailToBackup", PrefType.bool, false),

    backupEmailAddress("backupEmailAddress", PrefType.string, ""),

    prezzoPerGruppi("prezzoPerGruppi", PrefType.bool, false),

    importoBaseInteri("importoBaseInteri", PrefType.decimal, new BigDecimal(10)),

    importoBaseRidotti("importoBaseRidotti", PrefType.decimal, new BigDecimal(0)),

    importoBaseDisabili("importoBaseDisabili", PrefType.decimal, new BigDecimal(0)),

    importoBaseAccomp("importoBaseAccomp", PrefType.decimal, new BigDecimal(0)),

    importoBaseGruppi("importoBaseGruppi", PrefType.decimal, new BigDecimal(0)),

    idSalaDefault("idSalaDefault", PrefType.integer, 0),

    oraRunSolleciti("oraRunSolleciti", PrefType.integer, 23),

    doRunSolleciti("doRunSolleciti", PrefType.bool, false),

    splashImage("splashImage", PrefType.bytes, LibResource.getImgBytes(EventoApp.IMG_FOLDER_NAME, "splash_image.png")),

    menubarIcon("menubarIcon", PrefType.bytes, LibResource.getImgBytes(EventoApp.IMG_FOLDER_NAME, "default_menubar_icon.png")),

    sendMailInfoPren("sendMailInfoPren", PrefType.bool, true),
    sendMailScadPren("sendMailScadPren", PrefType.bool, true),
    sendMailConfPren("sendMailConfPren", PrefType.bool, true),
    sendMailScadPaga("sendMailScadPaga", PrefType.bool, false),
    sendMailConfPaga("sendMailConfPaga", PrefType.bool, false),
    sendMailRegisPaga("sendMailRegisPaga", PrefType.bool, true),
    sendMailCongOpzione("sendMailCongOpzione", PrefType.bool, true),

    sendMailInfoPrenRef("sendMailInfoPrenRef", PrefType.bool, true),
    sendMailScadPrenRef("sendMailScadPrenRef", PrefType.bool, true),
    sendMailConfPrenRef("sendMailConfPrenRef", PrefType.bool, true),
    sendMailScadPagaRef("sendMailScadPagaRef", PrefType.bool, false),
    sendMailConfPagaRef("sendMailConfPagaRef", PrefType.bool, false),
    sendMailRegisPagaRef("sendMailRegisPagaRef", PrefType.bool, true),
    sendMailCongOpzioneRef("sendMailCongOpzioneRef", PrefType.bool, true),

    sendMailInfoPrenScuola("sendMailInfoPrenScuola", PrefType.bool, false),
    sendMailScadPrenScuola("sendMailScadPrenScuola", PrefType.bool, false),
    sendMailConfPrenScuola("sendMailConfPrenScuola", PrefType.bool, false),
    sendMailScadPagaScuola("sendMailScadPagaScuola", PrefType.bool, false),
    sendMailConfPagaScuola("sendMailConfPagaScuola", PrefType.bool, false),
    sendMailRegisPagaScuola("sendMailRegisPagaScuola", PrefType.bool, false),
    sendMailCongOpzioneScuola("sendMailCongOpzioneScuola", PrefType.bool, false),

    sendMailInfoPrenNP("sendMailInfoPrenNP", PrefType.bool, false),
    sendMailScadPrenNP("sendMailScadPrenNP", PrefType.bool, false),
    sendMailConfPrenNP("sendMailConfPrenNP", PrefType.bool, false),
    sendMailScadPagaNP("sendMailScadPagaNP", PrefType.bool, false),
    sendMailConfPagaNP("sendMailConfPagaNP", PrefType.bool, false),
    sendMailRegisPagaNP("sendMailRegisPagaNP", PrefType.bool, false),
    sendMailCongOpzioneNP("sendMailCongOpzioneNP", PrefType.bool, false),;

    private String code;
    private PrefType type;
    private Object defaultValue;

    private CompanyPrefs(String key, PrefType type, Object defaultValue) {
        this.code = key;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    public String getCode() {
        return code;
    }

    public PrefType getType() {
        return type;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    /**
     * Recupera il valore di questa preferenza per una data azienda
     * <p>
     * Il valore ritornato è già convertito nel tipo previsto. Se la preferenza
     * manca ritorna il valore di default
     *
     * @param company - l'azienda
     * @return il valore della preferenza
     * <p>
     */
    public Object get(BaseCompany company) {
        Object obj = null;
        PrefEventoEntity entity = getPreference(company);

        if (entity != null) {
            byte[] bytes = entity.getValue();
            obj = bytesToObject(bytes);
        } else {
            obj = getDefaultValue();
        }

        return obj;
    }

    /**
     * Recupera il valore di questa preferenza per l'azienda corrente
     * <p>
     * Il valore ritornato è già convertito nel tipo previsto.
     *
     * @return il valore della preferenza
     */
    public Object get() {
        return get(CompanySessionLib.getCompany());
    }



    public boolean getBool() {
        return (boolean) get();
    }

    public boolean getBool(BaseCompany company) {
        return (boolean) get(company);
    }


    public byte[] getBytes() {
        return (byte[]) get();
    }

    public Date getDate() {
        return (Date) get();
    }

    public BigDecimal getDecimal() {
        return (BigDecimal) get();
    }

    public Image getImage() {
        Image img = null;
        byte[] bytes = getBytes();
        if (bytes.length > 0) {
            img = LibImage.getImage(bytes);
        }
        return img;
    }

    public int getInt() {
        return (int) get();
    }

    public int getInt(BaseCompany company) {
        return (int) get(company);
    }

    public Resource getResource() {
        Resource res = null;
        Image img = getImage();
        if (img != null) {
            res = img.getSource();
        }
        return res;
    }

    public String getString() {
        return (String) get();
    }

    public String getString(BaseCompany company) {
        return (String) get(company);
    }


    /**
     * Scrive un valore nello storage per questa preferenza per una data
     * azienda.
     * <p>
     * Se la preferenza non esiste nello storage la crea ora.
     *
     * @param company l'azienda di riferimento
     * @param value   il valore da scrivere
     */
    public void put(BaseCompany company, Object value) {
        PrefEventoEntity entity = getPreference(company);
        if (entity == null) {
            entity = new PrefEventoEntity();
            entity.setCode(getCode());
            entity.setCompany(company);
        }
        entity.setValue(objectToBytes(value));

        EntityManager manager = EM.createEntityManager();
        manager.getTransaction().begin();
        try {
            if (entity.getId() != null) {
                manager.merge(entity);
            } else {
                manager.persist(entity);
            }
            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
        }

        manager.close();

    }


    /**
     * Scrive un valore nello storage per questa preferenza per una data
     * azienda.
     * <p>
     * Se la preferenza non esiste nello storage la crea ora.
     *
     * @param company l'azienda di riferimento
     * @param value   il valore da scrivere
     */
    public void put(BaseCompany company, Object value, EntityManager manager) {
        PrefEventoEntity entity = getPreference(company);
        if (entity == null) {
            entity = new PrefEventoEntity();
            entity.setCode(getCode());
            entity.setCompany(company);
        }
        entity.setValue(objectToBytes(value));

        if (entity.getId() != null) {
            manager.merge(entity);
        } else {
            manager.persist(entity);
        }


    }


    /**
     * Scrive un valore nello storage per l'azienda corrente.
     * <p>
     * Se la preferenza non esiste nello storage la crea ora.
     *
     * @param value il valore da scrivere
     */
    public void put(Object value) {
        put(CompanySessionLib.getCompany(), value);
    }

    /**
     * Rimuove dallo storage questa preferenza per una data azienda.
     * <p>
     *
     * @param company l'azienda di riferimento
     */

    private void remove(BaseCompany company) {
        PrefEventoEntity entity = getPreference(company);
        if (entity != null) {

            EntityManager manager = EM.createEntityManager();
            manager.getTransaction().begin();
            try {
                entity = manager.merge(entity);
                manager.remove(entity);
                manager.getTransaction().commit();
            } catch (Exception e) {
                manager.getTransaction().rollback();
            }

            manager.close();

        }
    }

    /**
     * Rimuove dallo storage questa preferenza per l'azienda corrente.
     * <p>
     */
    public void remove() {
        //remove(EventoApp.COMPANY);
        remove(CompanySessionLib.getCompany());
    }


    /**
     * Resetta questa preferenza al valore di default per una data azienda.
     * <p>
     *
     * @param company l'azienda di riferimento
     */
    public void reset(BaseCompany company) {
        put(company, getDefaultValue());
    }

    /**
     * Resetta questa preferenza al valore di default per l'azienda corrente.
     */
    public void reset() {
        //Company comp=EventoApp.COMPANY;
        BaseCompany comp = CompanySessionLib.getCompany();
        put(comp, getDefaultValue());
    }

    /**
     * Recupera dallo storage questa preferenza per una data azienda.
     * Non usa le classi filtrate tipo EQuery quindi si può chiamare anche se non c'è sessione
     * <p>
     * param company - l'azienda
     *
     * @return la preferenza, null se non trovata
     */
    private PrefEventoEntity getPreference(BaseCompany company) {
        PrefEventoEntity entity = null;
        Filter f1 = new Compare.Equal(Evento_.company.getName(), company);
        Filter f2 = new Compare.Equal(PrefEventoEntity_.code.getName(), getCode());
        Filter filter = new And(f1,f2);
        List<? extends BaseEntity> list=AQuery.getList(PrefEventoEntity.class, filter);
        if(list.size()>0){
            entity=(PrefEventoEntity)list.get(0);
        }
        return entity;
    }

    /**
     * Converte un valore Object in ByteArray per questa preferenza.
     * <p>
     *
     * @param obj il valore Object
     * @return il valore convertito in byte[]
     */
    private byte[] objectToBytes(Object obj) {
        return AbsPref.objectToBytes(this, obj);
    }

    /**
     * Converte un byte[] in Object del tipo adatto per questa preferenza.
     * <p>
     *
     * @param bytes il valore come byte[]
     * @return il valore convertito nell'oggetto del tipo adeguato
     */
    private Object bytesToObject(byte[] bytes) {
        return AbsPref.bytesToObject(this, bytes);
    }

}
