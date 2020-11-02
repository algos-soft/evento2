package it.algos.webbase.domain.log;

import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.entity.DefaultSort;
import it.algos.webbase.web.lib.LibTime;
import it.algos.webbase.web.query.AQuery;
import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@DefaultSort({"timestamp,false"})
public class Log extends BaseEntity {

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private Livello livello = Livello.info;

    @NotEmpty
    private String code = "";

    @NotNull
    private Timestamp timestamp = LibTime.adesso();

    @NotEmpty
    private String descrizione = "";



    public Log() {
        super();
    }// end of constructor

    public Log(Livello livello, String code, String descrizione, Timestamp timestamp) {
        super();
        this.setLivello(livello);
        this.setCode(code);
        this.setDescrizione(descrizione);
        this.setTimestamp(timestamp);
    }// end of constructor

    /**
     * Recupera una istanza di Log usando la query specifica
     *
     * @return istanza di Log, null se non trovata
     */
    public static Log find(long id) {
        Log instance = null;
        BaseEntity entity = AQuery.queryById(Log.class, id);

        if (entity != null) {
            if (entity instanceof Log) {
                instance = (Log) entity;
            }// end of if cycle
        }// end of if cycle

        return instance;
    }// end of method

    /**
     * Recupera una istanza di Log usando la query specifica
     *
     * @return istanza di Log, null se non trovata
     */
    public static Log find(String code) {
        Log instance = null;
        BaseEntity entity = AQuery.queryOne(Log.class, Log_.code, code);

        if (entity != null) {
            if (entity instanceof Log) {
                instance = (Log) entity;
            }// end of if cycle
        }// end of if cycle

        return instance;
    }// end of method

    public synchronized static int count() {
        int totRec = 0;
        long totTmp = AQuery.getCount(Log.class);

        if (totTmp > 0) {
            totRec = (int) totTmp;
        }// fine del blocco if

        return totRec;
    }// end of method

    //--registra un avviso
    public static void debug(String code, String descrizione) {
        createBase(Livello.debug, code, descrizione);
    }// fine del metodo

    //--registra un avviso
    public static void info(String code, String descrizione) {
        createBase(Livello.info, code, descrizione);
    }// fine del metodo

    //--registra un avviso
    public static void warning(String code, String descrizione) {
        createBase(Livello.warn, code, descrizione);
    }// fine del metodo

    //--registra un avviso
    public static void error(String code, String descrizione) {
        createBase(Livello.error, code, descrizione);
    }// fine del metodo

    //--registra un evento generico
    private static void createBase(Livello livello, String code, String descrizione) {
        Log logo = new Log(livello, code, descrizione, LibTime.current());
        logo.save();
    }// fine del metodo statico

    //--registra un evento generico
    private static void createBase(Livello livello, String code, String descrizione, Timestamp time) {
        Log logo = new Log(livello, code, descrizione, time);
        logo.save();
    }// fine del metodo statico


    @Override
    public String toString() {
        return code;
    }// end of method

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Livello getLivello() {
        return livello;
    }

    public void setLivello(Livello livello) {
        this.livello = livello;
    }

    @Override
    public Log clone() throws CloneNotSupportedException {
        try {
            return (Log) BeanUtils.cloneBean(this);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }// fine del blocco try-catch
    }// end of method


}// end of entity class
