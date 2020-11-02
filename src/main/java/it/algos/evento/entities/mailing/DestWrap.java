package it.algos.evento.entities.mailing;

import java.util.HashMap;

/**
 * Created by gac on 14 lug 2015.
 * <p>
 * Wrapper
 * -   un indirizzo e-mail
 * -   una mappa di sostituzione (chiave-valore)
 */
public class DestWrap {

    //-- indirizzo e-mail
    private String indirizzo = "";

    //-- mappa di sostituzione (chiave-valore)
    private HashMap<String, String> mappa = null;

    /**
     * Costruttore ridotto
     * Nessuna mappa di sostituzione
     */
    public DestWrap(String indirizzo) {
        this(indirizzo, null);
    }// end of constructor

    /**
     * Costruttore semplificato
     */
    public DestWrap(String indirizzo, String chiave, String valore) {
        HashMap<String, String> mappa = new HashMap<String, String>();
        this.indirizzo = indirizzo;
        mappa.put(chiave, valore);
        this.mappa = mappa;
    }// end of constructor

    /**
     * Costruttore completo
     */
    public DestWrap(String indirizzo, HashMap<String, String> mappa) {
        this.indirizzo = indirizzo;
        this.mappa = mappa;
    }// end of constructor

    public String getIndirizzo() {
        return indirizzo;
    }

    public HashMap<String, String> getMappa() {
        return mappa;
    }

    public void setMappa(HashMap<String, String> mappa) {
        this.mappa = mappa;
    }
}// end of internal wrapper class
