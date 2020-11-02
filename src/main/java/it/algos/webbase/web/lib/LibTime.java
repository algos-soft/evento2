package it.algos.webbase.web.lib;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by gac on 06 set 2015.
 * .
 */
public abstract class LibTime {

    private static final String INFERIORE_SECONDO = "meno di un sec.";
    private static final String SECONDI = " sec.";
    private static final String MINUTI = " min.";
    private static final String ORA = " ora";
    private static final String ORE = " ore";
    private static final String GIORNO = " giorno";
    private static final String GIORNI = " gg.";
    private static final String ANNO = " anno";
    private static final String ANNI = " anni";

    private static final long MAX_MILLISEC = 1000;
    private static final long MAX_SECONDI = MAX_MILLISEC * 60;
    private static final long MAX_MINUTI = MAX_SECONDI * 60;
    private static final long MAX_ORE = MAX_MINUTI * 24;
    private static final long MAX_GIORNI = MAX_ORE * 365;

    /**
     * Tempo attuale (current) espresso come mumero long
     *
     * @return currentTimeMillis
     */
    public static long currentLong() {
        return getBaseLong();
    }// end of static method

    /**
     * Tempo attuale (current) espresso come mumero long
     *
     * @return currentTimeMillis
     */
    public static long adessoLong() {
        return getBaseLong();
    }// end of static method

    /**
     * Tempo attuale (current) espresso come mumero long
     *
     * @return currentTimeMillis
     */
    public static long getLong() {
        return getBaseLong();
    }// end of static method

    /**
     * Tempo attuale (current) espresso in Timestamp
     *
     * @return current Timestamp
     */
    public static Timestamp current() {
        return getBase();
    }// end of static method

    /**
     * Tempo attuale (current) espresso in Timestamp
     *
     * @return current Timestamp
     */
    public static Timestamp adesso() {
        return getBase();
    }// end of static method

    /**
     * Tempo attuale (current) espresso in Timestamp
     *
     * @return current Timestamp
     */
    public static Timestamp get() {
        return getBase();
    }// end of static method

    /**
     * Restituisce come stringa (intelligente) la differenza tra una data e il momento attuale
     *
     * @param inizio del controllo
     * @return tempo in forma leggibile
     */
    public static String difText(long inizio) {
        return toText(getLong() - inizio);
    }// end of static method

    /**
     * Restituisce come stringa (intelligente) la differenza tra due date
     *
     * @return tempo in forma leggibile
     */
    public static String difText(Date prima, Date seconda) {
        return toText(seconda.getTime() - prima.getTime());
    }// end of static method

    /**
     * Restituisce come stringa (intelligente) la differenza tra due timestamp
     *
     * @return tempo in forma leggibile
     */
    public static String difText(Timestamp primo, Timestamp secondo) {
        return toText(secondo.getTime() - primo.getTime());
    }// end of static method

    /**
     * Restituisce come stringa (intelligente) un durata espressa in long
     *
     * @return tempo in forma leggibile
     */
    public static String toText(long durata) {
        String tempo = "null";
        long div;
        long mod;

        if (durata < MAX_MILLISEC) {
            tempo = INFERIORE_SECONDO;
        } else {
            if (durata < MAX_SECONDI) {
                div = Math.floorDiv(durata, MAX_MILLISEC);
                mod = Math.floorMod(durata, MAX_MILLISEC);
                if (mod >= MAX_MILLISEC / 2) {
                    div++;
                }// fine del blocco if
                if (div < 60) {
                    tempo = div + SECONDI;
                } else {
                    tempo = "1" + MINUTI;
                }// fine del blocco if-else
            } else {
                if (durata < MAX_MINUTI) {
                    div = Math.floorDiv(durata, MAX_SECONDI);
                    mod = Math.floorMod(durata, MAX_SECONDI);
                    if (mod >= MAX_SECONDI / 2) {
                        div++;
                    }// fine del blocco if
                    if (div < 60) {
                        tempo = div + MINUTI;
                    } else {
                        tempo = "1" + ORA;
                    }// fine del blocco if-else
                } else {
                    if (durata < MAX_ORE) {
                        div = Math.floorDiv(durata, MAX_MINUTI);
                        mod = Math.floorMod(durata, MAX_MINUTI);
                        if (mod >= MAX_MINUTI / 2) {
                            div++;
                        }// fine del blocco if
                        if (div < 24) {
                            if (div == 1) {
                                tempo = div + ORA;
                            } else {
                                tempo = div + ORE;
                            }// fine del blocco if-else
                        } else {
                            tempo = "1" + GIORNO;
                        }// fine del blocco if-else
                    } else {
                        if (durata < MAX_GIORNI) {
                            div = Math.floorDiv(durata, MAX_ORE);
                            mod = Math.floorMod(durata, MAX_ORE);
                            if (mod >= MAX_ORE / 2) {
                                div++;
                            }// fine del blocco if
                            if (div < 365) {
                                if (div == 1) {
                                    tempo = div + GIORNO;
                                } else {
                                    tempo = div + GIORNI;
                                }// fine del blocco if-else
                            } else {
                                tempo = "1" + ANNO;
                            }// fine del blocco if-else
                        } else {
                            div = Math.floorDiv(durata, MAX_GIORNI);
                            mod = Math.floorMod(durata, MAX_GIORNI);
                            if (mod >= MAX_GIORNI / 2) {
                                div++;
                            }// fine del blocco if
                            if (div == 1) {
                                tempo = div + ANNO;
                            } else {
                                tempo = div + ANNI;
                            }// fine del blocco if-else
                        }// fine del blocco if-else
                    }// fine del blocco if-else
                }// fine del blocco if-else
            }// fine del blocco if-else
        }// fine del blocco if-else

        return tempo;
    }// end of static method

    private static Timestamp getBase() {
        return new Timestamp(getBaseLong());
    }// end of static method

    private static long getBaseLong() {
        return System.currentTimeMillis();
    }// end of static method

    /**
     * Crea la data da un long.
     * La data parte dalla mezzanotte
     *
     * @param time di riferimento
     * @return la data creata
     */
    public static Date creaData(long time) {
        return creaData(new Timestamp(time), false);
    }// fine del metodo

    /**
     * Crea la data da un timestamp.
     * La data parte dalla mezzanotte
     *
     * @param timestamp di riferimento
     * @return la data creata
     */
    public static Date creaData(Timestamp timestamp) {
        return creaData(timestamp, false);
    }// fine del metodo

    /**
     * Crea la data da un timestamp.
     * Azzera eventuali valori di ore, minuti, secondi e millisecondi
     * La data parte dalla mezzanotte
     *
     * @param timestamp di riferimento
     * @return la data creata
     */
    public static Date creaDataMezzanotte(Timestamp timestamp) {
        return creaData(timestamp, true);
    }// fine del metodo

    /**
     * Crea la data da un timestamp.
     * Azzera eventuali valori di ore, minuti, secondi e millisecondi
     * La data parte dalla mezzanotte
     *
     * @param timestamp         di riferimento
     * @param parteDaMezzanotte flag di controllo
     * @return la data creata
     */
    private static Date creaData(Timestamp timestamp, boolean parteDaMezzanotte) {
        Date giorno = null;
        Calendar cal;

        try { // prova ad eseguire il codice
            cal = Calendar.getInstance();
            cal.setTime(timestamp);
            if (parteDaMezzanotte) {
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
            }// end of if cycle
            giorno = new Date(cal.getTime().getTime());

        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        return giorno;
    }// fine del metodo


    /**
     * Presentazione della data.
     */
    public static String getGioMeseAnno(Date data) {
        return getGioMeseAnnoBase(data, true);
    }// fine del metodo

    /**
     * Presentazione della data.
     */
    public static String getGioMeseAnnoLungo(Date data) {
        return getGioMeseAnnoBase(data, false);
    }// fine del metodo

    /**
     * Presentazione della data.
     */
    private static String getGioMeseAnnoBase(Date data, boolean corta) {
        String dataFormattata = "";
        GregorianCalendar cal = new GregorianCalendar();
        int giorno;
        int numMese;
        int numAnno;
        int numOre;
        int numMinuti;
        String sep;
        String mese;
        String anno;
        String ore;
        String minuti;

        if (corta) {
            sep = "-";
        } else {
            sep = " ";
        }// fine del blocco if-else

        try { // prova ad eseguire il codice
            if (data != null) {
                cal.setTime(data);
                giorno = cal.get(Calendar.DAY_OF_MONTH);
                numMese = cal.get(Calendar.MONTH);
                numMese++;
                mese = MeseEnum.getShort(numMese);
                numAnno = cal.get(Calendar.YEAR);
                numOre = cal.get(Calendar.HOUR_OF_DAY);
                numMinuti = cal.get(Calendar.MINUTE);
                anno = numAnno + "";
                if (corta) {
                    anno = anno.substring(2);
                }// fine del blocco if
                dataFormattata += giorno;
                dataFormattata += sep;
                dataFormattata += mese;
                dataFormattata += sep;
                dataFormattata += anno;
                if (corta) {
                    if (numOre < 10) {
                        ore = "0" + numOre;
                    } else {
                        ore = "" + numOre;
                    }// end of if/else cycle
                    if (numMinuti < 10) {
                        minuti = "0" + numMinuti;
                    } else {
                        minuti = "" + numMinuti;
                    }// end of if/else cycle

                    dataFormattata += " ";
                    dataFormattata += ore;
                    dataFormattata += ":";
                    dataFormattata += minuti;
                }// fine del blocco if
            }// fine del blocco if

        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        return dataFormattata;
    }// fine del metodo

    /**
     * Recupera la data in formato testo
     *
     * @param oldTime la data in formato Timestamp
     * @return la data, in formato testo
     */
    public static String getData(Timestamp oldTime) {
        String oldDataTxt = "";
        Date oldData = null;

        if (oldTime != null) {
            oldData = LibTime.creaData(oldTime);
            oldDataTxt = LibTime.getGioMeseAnno(oldData);
        }// fine del blocco if

        return oldDataTxt;
    }// fine del metodo

    /**
     * Recupera la prima data della lista, in formato testo
     *
     * @param listaTimestamp una lista di date di cui utilizzare la prima
     * @return la prima data, in formato testo
     */
    public static String getData(ArrayList listaTimestamp) {
        String oldDataTxt = "";
        Timestamp oldTime;

        if (listaTimestamp != null && listaTimestamp.size() > 0) {
            oldTime = (Timestamp) listaTimestamp.get(0);
            oldDataTxt = getData(oldTime);
        }// fine del blocco if

        return oldDataTxt;
    }// fine del metodo

}// end of abstract static class
