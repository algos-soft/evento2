package it.algos.webbase.web.lib;

import com.vaadin.server.Resource;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import it.algos.webbase.domain.pref.Pref;
import it.algos.webbase.domain.pref.PrefType;
import it.algos.webbase.domain.pref.TypePref;
import it.algos.webbase.web.pref.AbsPref;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by gac on 05 set 2015.
 * .
 */
public abstract class LibPref {

    /**
     * Crea una nuova preferenza di tipo booleano
     * La crea solo se non esistente
     *
     * @param code     key code
     * @param value    di default
     * @param descPref dettagliata (obbligatoria per Pref)
     */
    public static void newBool(String code, Boolean value, String descPref) {
        newBase(false, code, value, descPref, PrefType.bool);
    }// end of static method


    /**
     * Crea una nuova preferenza di tipo stringa
     * La crea solo se non esistente
     *
     * @param code     key code
     * @param value    di default
     * @param descPref dettagliata (obbligatoria per Pref)
     */
    public static void newStr(String code, String value, String descPref) {
        newBase(false, code, value, descPref, PrefType.string);
    }// end of static method


    /**
     * Crea una nuova preferenza di tipo intero
     * La crea solo se non esistente
     *
     * @param code     key code
     * @param value    di default
     * @param descPref dettagliata (obbligatoria per Pref)
     */
    public static void newInt(String code, int value, String descPref) {
        newBase(false, code, value, descPref, PrefType.integer);
    }// end of static method


    /**
     * Crea una nuova preferenza di tipo data
     * La crea solo se non esistente
     *
     * @param code     key code
     * @param value    di default
     * @param descPref dettagliata (obbligatoria per Pref)
     */
    public static void newData(String code, Date value, String descPref) {
        newBase(false, code, value, descPref, PrefType.date);
    }// end of static method


    /**
     * Crea una nuova preferenza di tipo booleano
     * La crea solo se non esistente
     * Crea anche un record di log nella tavola Versione
     *
     * @param code     key code
     * @param value    di default
     * @param descPref dettagliata (obbligatoria per Pref)
     */
    public static void newVersBool(String code, boolean value, String descPref) {
        newBase(true, code, value, descPref, "", PrefType.bool);
    }// end of method


    /**
     * Crea una nuova preferenza di tipo stringa
     * La crea solo se non esistente
     * Crea anche un record di log nella tavola Versione
     *
     * @param code     key code
     * @param value    di default
     * @param descPref dettagliata (obbligatoria per Pref)
     * @param descVers dettagliata (facoltativa e aggiuntiva a quella automatica di Versione)
     */
    public static void newVersStr(String code, String value, String descPref, String descVers) {
        newBase(true, code, value, descPref, descVers, PrefType.string);
    }// end of static method


    /**
     * Crea una nuova preferenza di tipo intero
     * La crea solo se non esistente
     * Crea anche un record di log nella tavola Versione
     *
     * @param code     key code
     * @param value    di default
     * @param descPref dettagliata (obbligatoria per Pref)
     */
    public static void newVersInt(String code, int value, String descPref) {
        newBase(true, code, value, descPref, "", PrefType.integer);
    }// end of static method


    /**
     * Crea una nuova preferenza di tipo data
     * La crea solo se non esistente
     * Crea anche un record di log nella tavola Versione
     *
     * @param code     key code
     * @param value    di default
     * @param descPref dettagliata (obbligatoria per Pref)
     */
    public static void newVersData(String code, Date value, String descPref) {
        newBase(true, code, value, descPref, "", PrefType.date);
    }// end of static method


    /**
     * Crea una nuova preferenza di tipo booleano
     * La crea solo se non esistente
     * Crea anche un record di log nella tavola Versione
     *
     * @param code     key code
     * @param value    di default
     * @param descPref dettagliata (obbligatoria per Pref)
     * @param descVers dettagliata (facoltativa e aggiuntiva a quella automatica di Versione)
     */
    public static void newVersBool(String code, boolean value, String descPref, String descVers) {
        newBase(true, code, value, descPref, descVers, PrefType.bool);
    }// end of static method


    /**
     * Crea una nuova preferenza di tipo stringa
     * La crea solo se non esistente
     * Crea anche un record di log nella tavola Versione
     *
     * @param code     key code
     * @param value    di default
     * @param descPref dettagliata (obbligatoria per Pref)
     */
    public static void newVersStr(String code, String value, String descPref) {
        newBase(true, code, value, descPref, "", PrefType.string);
    }// end of static method

    /**
     * Crea una nuova preferenza di tipo intero
     * La crea solo se non esistente
     * Crea anche un record di log nella tavola Versione
     *
     * @param code     key code
     * @param value    di default
     * @param descPref dettagliata (obbligatoria per Pref)
     * @param descVers dettagliata (facoltativa e aggiuntiva a quella automatica di Versione)
     */
    public static void newVersInt(String code, int value, String descPref, String descVers) {
        newBase(true, code, value, descPref, descVers, PrefType.integer);
    }// end of static method

    /**
     * Crea una nuova preferenza di tipo data
     * La crea solo se non esistente
     * Crea anche un record di log nella tavola Versione
     *
     * @param code     key code
     * @param value    di default
     * @param descPref dettagliata (obbligatoria per Pref)
     * @param descVers dettagliata (facoltativa e aggiuntiva a quella automatica di Versione)
     */
    public static void newVersInt(String code, Date value, String descPref, String descVers) {
        newBase(true, code, value, descPref, descVers, PrefType.date);
    }// end of static method


    /**
     * Crea una nuova preferenza (solo se non esistente)
     *
     * @param logVersione flag per registrare una un record di Versione
     * @param code        key code
     * @param value       di default
     * @param descPref    dettagliata (obbligatoria per Pref)
     * @param type        di valore
     */
    private static void newBase(boolean logVersione, String code, Object value, String descPref, PrefType type) {
        newBase(logVersione, code, value, descPref, "", type);
    }// end of static method

    /**
     * Crea una nuova preferenza (solo se non esistente)
     *
     * @param logVersione flag per registrare una un record di Versione
     * @param code        key code
     * @param value       di default
     * @param descPref    dettagliata (obbligatoria per Pref)
     * @param descVers    dettagliata (facoltativa e aggiuntiva a quella automatica di Versione)
     * @param type        di valore
     */
    private static void newBase(boolean logVersione, String code, Object value, String descPref, String descVers, PrefType type) {
        Pref pref;
        String commento;
        String strValue = "";

        pref = Pref.findByCode(code);
        if (pref == null) {
            pref = new Pref();
            pref.setOrdine(Pref.count() + 1);
            pref.setCode(code);
            pref.setDescrizione(descPref);
            pref.setTipo(type);
            pref.setValore(value);
            pref.save();

            if (logVersione) {
                commento = code + ", di default " + value;
                if (!descVers.equals("")) {
                    commento += " " + descVers;
                }// fine del blocco if
                LibVers.nuova("Preferenze", commento);
            }// fine del blocco if
        } else {
            if (logVersione) {
                LibVers.nuova("Preferenze", "La preferenza " + code + " esisteva già e non è stata modificata");
            }// fine del blocco if
        }// end of if/else cycle

    }// end of static method


}// end of abstract static class
