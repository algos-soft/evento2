package it.algos.webbase.web.lib;

import javax.swing.event.ListSelectionEvent;

/**
 * Created by gac on 23 ott 2015.
 * .
 */
public class LibEvent {

    /**
     * Controlla le righe selezionate
     * <p>
     * Vero se una e solo una riga è selezionata
     *
     * @param event da analizzare
     */
    public static boolean isUnaSolaRigaSelezionata(ListSelectionEvent event) {
        boolean status = false;
        int ini = 0;
        int end = 0;

        if (event != null) {
            ini = event.getFirstIndex();
            end = event.getLastIndex();
        }// end of if cycle

        if (ini > 0 && end > 0 && ini == end) {
            status = true;
        }// end of if cycle

        return status;
    }// end of static method

    /**
     * Controlla le righe selezionate
     * <p>
     * Vero se è selezionata più di una riga
     *
     * @param event da analizzare
     */
    public static boolean isDiverseRigheSelezionate(ListSelectionEvent event) {
        boolean status = false;
        int ini = 0;
        int end = 0;

        if (event != null) {
            ini = event.getFirstIndex();
            end = event.getLastIndex();
        }// end of if cycle

        if (ini > 0 && end > 0 && ini != end) {
            status = true;
        }// end of if cycle

        return status;
    }// end of static method

    /**
     * Controlla le righe selezionate
     * <p>
     * Vero se è selezionata almeno una riga
     *
     * @param event da analizzare
     */
    public static boolean isUnaOPiuRigheSelezionate(ListSelectionEvent event) {
        boolean status = false;
        int ini = 0;
        int end = 0;

        if (event != null) {
            ini = event.getFirstIndex();
            end = event.getLastIndex();
        }// end of if cycle

        if (ini > 0 && end > 0) {
            status = true;
        }// end of if cycle

        return status;
    }// end of static method

    /**
     * Controlla le righe selezionate
     * <p>
     * Vero se non è selezionata nessuna riga
     *
     * @param event da analizzare
     */
    public static boolean isNessunaRigaSelezionata(ListSelectionEvent event) {
        boolean status = false;
        int ini = 0;
        int end = 0;

        if (event != null) {
            ini = event.getFirstIndex();
            end = event.getLastIndex();
        }// end of if cycle

        if (ini == 0 && end == 0) {
            status = true;
        }// end of if cycle

        return status;
    }// end of static method


}// end of abstract static class
