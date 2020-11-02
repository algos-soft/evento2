package it.algos.webbase.web.lib;

/**
 * Created by gac on 12 set 2015.
 */
public abstract class LibNum {

    /**
     * tag per il carattere punto
     */
    public static final String PUNTO = ".";

    /**
     * tag per il carattere barra
     */
    public static final String BARRA = "/";

    /**
     * tag per la stringa vuota
     */
    public static final String VUOTA = "";

    /**
     * Formattazione di un numero.
     * <p>
     * Il numero può arrivare come stringa, intero o double
     * Se la stringa contiene punti e virgole, viene pulita
     * Se la stringa non è convertibile in numero, viene restituita uguale
     * Inserisce il punto separatore ogni 3 cifre
     * Se arriva un oggetto non previsto, restituisce null
     *
     * @param numObj da formattare (stringa, intero, long o double)
     * @return stringa formattata
     */
    public static String format(Object numObj) {
        String formattato = VUOTA;
        String numText = VUOTA;
        String sep = PUNTO;
        int numTmp = 0;
        int len;
        String num3;
        String num6;
        String num9;
        String num12;

        if (numObj instanceof String || numObj instanceof Integer || numObj instanceof Long || numObj instanceof Double) {
            if (numObj instanceof String) {
                numText = (String) numObj;
                numText = LibText.levaVirgole(numText);
                numText = LibText.levaPunti(numText);
                try { // prova ad eseguire il codice
                    numTmp = Integer.decode(numText);
                } catch (Exception unErrore) { // intercetta l'errore
                    return (String)numObj;
                }// fine del blocco try-catch
            } else {
                if (numObj instanceof Integer) {
                    numText = Integer.toString((int) numObj);
                }// fine del blocco if
                if (numObj instanceof Long) {
                    numText = Long.toString((long) numObj);
                }// fine del blocco if
                if (numObj instanceof Double) {
                    numText = Double.toString((double) numObj);
                }// fine del blocco if
            }// fine del blocco if-else
        } else {
            return null;
        }// fine del blocco if-else

        formattato = numText;
        len = numText.length();
        if (len > 3) {
            num3 = numText.substring(0, len - 3);
            num3 += sep;
            num3 += numText.substring(len - 3);
            formattato = num3;
            if (len > 6) {
                num6 = num3.substring(0, len - 6);
                num6 += sep;
                num6 += num3.substring(len - 6);
                formattato = num6;
                if (len > 9) {
                    num9 = num6.substring(0, len - 9);
                    num9 += sep;
                    num9 += num6.substring(len - 9);
                    formattato = num9;
                    if (len > 12) {
                        num12 = num9.substring(0, len - 12);
                        num12 += sep;
                        num12 += num9.substring(len - 12);
                        formattato = num12;
                    }// fine del blocco if
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return formattato;
    }// end of static method

}// end of abstract static class
