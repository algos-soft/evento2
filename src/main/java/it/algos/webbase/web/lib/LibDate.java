package it.algos.webbase.web.lib;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public abstract class LibDate {

    /**
     * Oggetti formattatori per le date.
     * <p>
     * Converte data->testo e testo->data
     */

    // Uso i formatter di JodaTime perché SimpleDateFormat ha dei seri bug e sui
    // PC Windows con locale IT (Asteria) provoca errori - alex giu-2015
    // http://stackoverflow.com/questions/11554595/null-date-assigned-by-simpledateformat-parse
    private static DateTimeFormatter DATE_EXTRA_LONG = DateTimeFormat.forPattern("dd-MMMM-yyyy");
    private static DateTimeFormatter DATE_LONG = DateTimeFormat.forPattern("dd-MMM-yyyy");
    private static DateTimeFormatter DATE_NORMAL = DateTimeFormat.forPattern("d-MMM-yy");
    private static DateTimeFormatter DATE_SHORT = DateTimeFormat.forPattern("d-M-yy");
    private static DateTimeFormatter TIME_EXTRA_LONG = DateTimeFormat.forPattern("dd-MM-YYYY HH:mm");
    private static DateTimeFormatter TIME_LONG = DateTimeFormat.forPattern("d-MMM-YY HH:mm");
    private static DateTimeFormatter TIME_SHORT = DateTimeFormat.forPattern("d-M-yy HH:mm");
    private static DateTimeFormatter TIME_ONLY = DateTimeFormat.forPattern("HH:mm");
    private static DateTimeFormatter TIME_SECONDS = DateTimeFormat.forPattern("HH:mm:ss");
    private static DateTimeFormatter TIME_LONG_SECONDS = DateTimeFormat.forPattern("dd-MM-YY HH:mm:ss");

    /**
     * Converte una data in stringa nel formato dd-MMMM-YYYY
     * <p>
     * Returns a string representation of the date <br>
     * Using leading zeroes in day <br>
     * Alphabetic representation of month in long descrition <br>
     * Four numbers for year <b>
     *
     * @param date da convertire
     *
     * @return la data sotto forma di stringa
     */
    public static String toStringDDMMMMYYYY(Date date) {
        return DATE_EXTRA_LONG.print(new DateTime(date));
    }// end of static method

    /**
     * Converte una data in stringa nel formato dd-MMM-YYYY
     * <p>
     * Returns a string representation of the date <br>
     * Using leading zeroes in day <br>
     * Alphabetic representation of month in short description <br>
     * Four numbers for year <b>
     *
     * @param date da convertire
     *
     * @return la data sotto forma di stringa
     */
    public static String toStringDDMMMYYYY(Date date) {
        return DATE_LONG.print(new DateTime(date));
    }// end of static method

    /**
     * Converte una data in stringa nel formato d-MMM-YY
     * <p>
     * Returns a string representation of the date <br>
     * Suppressing leading zeroes in day <br>
     * Alphabetic representation of month in short description <br>
     * Two numbers for year <b>
     *
     * @param date da convertire
     *
     * @return la data sotto forma di stringa
     */
    public static String toStringDMMMYY(Date date) {
        return DATE_NORMAL.print(new DateTime(date));
    }// end of static method

    /**
     * Converte una data in stringa nel formato d-M-YY
     * <p>
     * Returns a string representation of the date <br>
     * Suppressing leading zeroes in day and month <br>
     * Numeric representation of month <br>
     * Two numbers for year <b>
     *
     * @param date da convertire
     *
     * @return la data sotto forma di stringa
     */
    public static String toStringDMYY(Date date) {
        return DATE_SHORT.print(new DateTime(date));
    }// end of static method

    /**
     * Converte una data in stringa nel formato d-M-YYYY
     * <p>
     * Returns a string representation of the date <br>
     * Suppressing leading zeroes in day and month <br>
     * Numeric representation of month <br>
     * Four numbers for year <b>
     *
     * @param date da convertire
     *
     * @return la data sotto forma di stringa
     *
     * @deprecated
     */
    public static String toStringDMYYYY(Date date) {
        DateTime dt = new DateTime(date);
        DateTimeFormatter fmt = DateTimeFormat.forPattern("d-M-YYYY");
        return fmt.print(dt);
    }// end of static method

    /**
     * Converte una data in stringa nel formato dd-MM-YYYY
     * <p>
     * Returns a string representation of the date <br>
     * Using leading zeroes in day <br>
     * Using leading zeroes in month <br>
     * Numeric representation of month <br>
     * Four numbers for year <b>
     *
     * @param date da convertire
     *
     * @return la data sotto forma di stringa
     */
    public static String toStringDDMMYYYY(Date date) {
        DateTime dt = new DateTime(date);
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd-MM-YYYY");
        return fmt.print(dt);
    }// end of static method

    /**
     * Converte una data in stringa nel formato dd-MM-YYYY-HH:mm
     * <p>
     * Returns a string representation of the date <br>
     * Using leading zeroes in day <br>
     * Using leading zeroes in month <br>
     * Numeric representation of month <br>
     * Two numbers for year <b>
     * Using leading zeroes in hour <br>
     * Using leading zeroes in minute <br>
     *
     * @param date da convertire
     *
     * @return la data sotto forma di stringa
     */
    public static String toStringDDMMYYYYHHMM(Date date) {
        return TIME_EXTRA_LONG.print(new DateTime(date));
    }// end of static method

    /**
     * Converte una data in stringa nel formato d-MMM-YY-HH:mm
     * <p>
     * Returns a string representation of the date <br>
     * Suppressing leading zeroes in day <br>
     * Alphabetic representation of month in short description <br>
     * Two numbers for year <b>
     * Using leading zeroes in hour <br>
     * Using leading zeroes in minute <br>
     *
     * @param date da convertire
     *
     * @return la data sotto forma di stringa
     */
    public static String toStringDMMMYYHHMM(Date date) {
        return TIME_LONG.print(new DateTime(date));
    }// end of static method

    /**
     * Converte una data in stringa nel formato d-M-YY-HH:mm
     * <p>
     * Returns a string representation of the date <br>
     * Suppressing leading zeroes in day and month <br>
     * Two numbers for year <b>
     * Using leading zeroes in hour <br>
     * Using leading zeroes in minute <br>
     *
     * @param date da convertire
     *
     * @return la data sotto forma di stringa
     */
    public static String toStringDMYYHHMM(Date date) {
        return TIME_SHORT.print(new DateTime(date));
    }// end of static method

    /**
     * Converte una data in stringa nel formato HH:mm
     * <p>
     * Returns a string representation of the time only <br>
     * Using leading zeroes in hour <br>
     * Using leading zeroes in minute <br>
     *
     * @param date da convertire
     *
     * @return la data sotto forma di stringa
     */
    public static String toStringHHMM(Date date) {
        return TIME_ONLY.print(new DateTime(date));
    }// end of static method

    /**
     * Converte una data in stringa nel formato HH:mm:ss
     * <p>
     * Returns a string representation of the time only <br>
     * Using leading zeroes in hour <br>
     * Using leading zeroes in minute <br>
     * Using leading zeroes in second <br>
     *
     * @param date da convertire
     *
     * @return la data sotto forma di stringa
     */
    public static String toStringHHMMSS(Date date) {
        return TIME_SECONDS.print(new DateTime(date));
    }// end of static method

    /**
     * Converte una data in stringa nel formato dd-MM-YY HH:mm:ss
     * <p>
     * Returns a string representation of the date <br>
     * Using leading zeroes in day <br>
     * Using leading zeroes in month <br>
     * Numeric representation of month <br>
     * Two numbers for year <b>
     * Using leading zeroes in hour <br>
     * Using leading zeroes in minute <br>
     * Using leading zeroes in second <br>
     *
     * @param date da convertire
     *
     * @return la data sotto forma di stringa
     */
    public static String toStringDDMMYYHHMMSS(Date date) {
        return TIME_LONG_SECONDS.print(new DateTime(date));
    }// end of static method

    /**
     * Restituisce la data corrente
     * <p>
     * Ore e minuti e secondi regolati alla mezzanotte
     *
     * @return la data in formato data
     */
    public static Date today() {
        return new DateTime().withTimeAtStartOfDay().toDate();
    }// end of static method

    /**
     * Restituisce la data del primo giorno del mese dell'anno
     * <p>
     * Ore e minuti e secondi regolati subito dopo la mezzanotte
     *
     * @param numMeseDellAnno L'anno parte da gennaio che è il mese numero 1
     * @param anno            di riferimento
     *
     * @return la data in formato data
     */
    public static Date fromInizioMeseAnno(int numMeseDellAnno, int anno) {
        return fromMeseAnno(numMeseDellAnno, anno, InizioOppureFine.inizio);
    }// end of static method

    /**
     * Restituisce la data del primo giorno del mese dell'anno
     * <p>
     * Ore e minuti e secondi regolati subito dopo la mezzanotte
     *
     * @param nomeBreveLungo (descrizione short o long)
     * @param anno           di riferimento
     *
     * @return la data in formato data
     */
    public static Date fromInizioMeseAnno(String nomeBreveLungo, int anno) {
        int numMeseDellAnno = MeseEnum.getOrd(nomeBreveLungo);
        return fromInizioMeseAnno(numMeseDellAnno, anno);
    }// end of static method

    /**
     * Restituisce la data dell'ultimo giorno del mese dell'anno
     * <p>
     * Ore e minuti e secondi regolati subito prima della mezzanotte
     *
     * @param numMeseDellAnno L'anno parte da gennaio che è il mese numero 1
     * @param anno            di riferimento
     *
     * @return la data in formato data
     */
    public static Date fromFineMeseAnno(int numMeseDellAnno, int anno) {
        return fromMeseAnno(numMeseDellAnno, anno, InizioOppureFine.fine);
    }// end of static method

    /**
     * Restituisce la data dell'ultimo giorno del mese dell'anno
     * <p>
     * Ore e minuti e secondi regolati subito prima della mezzanotte
     *
     * @param nomeBreveLungo (descrizione short o long)
     * @param anno           di riferimento
     *
     * @return la data in formato data
     */
    public static Date fromFineMeseAnno(String nomeBreveLungo, int anno) {
        int numMeseDellAnno = MeseEnum.getOrd(nomeBreveLungo);
        return fromFineMeseAnno(numMeseDellAnno, anno);
    }// end of static method

    /**
     * Restituisce la data del primo giorno del mese dell'anno
     * <p>
     * Ore e minuti e secondi regolati subito dopo la mezzanotte
     *
     * @param numMeseDellAnno L'anno parte da gennaio che è il mese numero 1
     * @param anno            di riferimento
     * @param flag            di inizio oppure fine
     *
     * @return la data in formato data
     */
    private static Date fromMeseAnno(int numMeseDellAnno, int anno, InizioOppureFine flag) {
        Date data = null;
        Calendar calendario = getCal();
        int giorniMese = MeseEnum.getGiorni(numMeseDellAnno, anno);

        if (numMeseDellAnno > 0) {
            numMeseDellAnno--;
        }// fine del blocco if

        calendario.set(Calendar.YEAR, anno);
        calendario.set(Calendar.MONTH, numMeseDellAnno);

        switch (flag) {
            case inizio:
                calendario.set(Calendar.DATE, 1);
                break;
            case fine:
                calendario.set(Calendar.DATE, giorniMese);
                break;
            default: // caso non definito
                break;
        } // fine del blocco switch

        data = calendario.getTime();

        switch (flag) {
            case inizio:
                data = dropTime(data);
                break;
            case fine:
                data = lastTime(data);
                break;
            default: // caso non definito
                break;
        } // fine del blocco switch

        return data;
    }// end of static method

    /**
     * Aggiunge (o sottrae) ad un una data i mesi indicati.
     * <p>
     * Se i mesi sono negativi, li sottrae <br>
     * Esegue solo se la data è valida (non nulla e non vuota) <br>
     *
     * @param data di riferimento
     * @param mesi da aggiungere
     *
     * @return la data risultante
     */
    public static Date addMonths(Date data, int mesi) {
        Calendar calendario = getCal(data);
        calendario.add(Calendar.MONTH, mesi);
        return calendario.getTime();
    }// end of static method

    /**
     * Aggiunge (o sottrae) ad un una data i giorni indicati.
     * <p>
     * Se i giorni sono negativi, li sottrae <br>
     * Esegue solo se la data è valida (non nulla e non vuota) <br>
     *
     * @param data   di riferimento
     * @param giorni da aggiungere
     *
     * @return la data risultante
     */
    public static Date addDays(Date data, int giorni) {
        Calendar calendario = getCal(data);
        calendario.add(Calendar.DAY_OF_MONTH, giorni);
        return calendario.getTime();
    }// end of static method

    /**
     * Aggiunge (o sottrae) ad un una data le ore indicate.
     * <p>
     * Se le ore sono negative, le sottrae <br>
     * Esegue solo se la data è valida (non nulla e non vuota) <br>
     *
     * @param data di riferimento
     * @param ore  da aggiungere
     *
     * @return la data risultante
     */
    public static Date addHour(Date data, int ore) {
        Calendar calendario = getCal(data);
        calendario.add(Calendar.HOUR, ore);
        return calendario.getTime();
    }// end of static method

    /**
     * Aggiunge (o sottrae) ad un una data i giorni indicati.
     * <p>
     * Se i giorni sono negativi, li sottrae <br>
     * Esegue solo se la data è valida (non nulla e non vuota) <br>
     *
     * @param data   di riferimento
     * @param giorni da aggiungere
     *
     * @return la data risultante
     */
    public static Date add(Date data, int giorni) {
        return addDays(data, giorni);
    }// end of static method

    /**
     * Differenza in giorni tra le date indicate.
     * <p>
     * Esegue solo se le data sono valide
     * Se le due date sono uguali la differenza è zero Se le due date sono consecutive la differenza è uno, ecc...
     *
     * @param dataIniziale data iniziale
     * @param dataFinale   data finale
     *
     * @return il numero di giorni passati tra le due date (positivo se la prima data e' precedente la seconda,
     * altrimenti negativo)
     */
    public static int diffDays(Date dataIniziale, Date dataFinale) {
        long diff = dataFinale.getTime() - dataIniziale.getTime();
        return (int) TimeUnit.MILLISECONDS.toDays(diff);
    }// end of static method

    /**
     * Differenza effettiva in ore tra le date indicate.
     * <p>
     * Esegue solo se le data sono valide
     *
     * @param dataIniziale data iniziale
     * @param dataFinale   data finale
     *
     * @return ore effettive di differenza
     */
    public static int diffHours(Date dataIniziale, Date dataFinale) {
        long diff = dataFinale.getTime() - dataIniziale.getTime();
        return (int) TimeUnit.MILLISECONDS.toHours(diff);
    }// end of static method

    /**
     * Differenza effettiva in minuti tra le date indicate.
     * <p>
     * Esegue solo se le data sono valide
     *
     * @param dataIniziale data iniziale
     * @param dataFinale   data finale
     *
     * @return minuti effettivi di differenza
     */
    public static int diffMinutes(Date dataIniziale, Date dataFinale) {
        long diff = dataFinale.getTime() - dataIniziale.getTime();
        return (int) TimeUnit.MILLISECONDS.toMinutes(diff);
    }// end of static method

    /**
     * Differenza effettiva in secondi tra le date indicate.
     * <p>
     * Esegue solo se le data sono valide
     *
     * @param dataIniziale data iniziale
     * @param dataFinale   data finale
     *
     * @return secondi effettivi di differenza
     */
    public static int diffSeconds(Date dataIniziale, Date dataFinale) {
        long diff = dataFinale.getTime() - dataIniziale.getTime();
        return (int) TimeUnit.MILLISECONDS.toSeconds(diff);
    }// end of static method

    /**
     * Differenza in giorni tra le date indicate.
     * <p>
     * Esegue solo se le data sono valide
     * Se le due date sono uguali la differenza è zero Se le due date sono consecutive la differenza è uno, ecc...
     *
     * @param dataIniziale data iniziale
     * @param dataFinale   data finale
     *
     * @return il numero di giorni passati tra le due date (positivo se la prima data e' precedente la seconda,
     * altrimenti negativo)
     */
    public static int diff(Date dataIniziale, Date dataFinale) {
        return diffDays(dataIniziale, dataFinale);
    }// end of static method

    /**
     * Ritorna il numero della settimana dell'anno di una data fornita.
     * <p>
     *
     * @param data fornita
     *
     * @return il numero della settimana dell'anno
     */
    public static int getWeekYear(Date data) {
        Calendar calendario = getCal(data);
        return calendario.get(Calendar.WEEK_OF_YEAR);
    }// end of static method

    /**
     * Ritorna il numero della settimana del mese di una data fornita.
     * <p>
     *
     * @param data fornita
     *
     * @return il numero della settimana del mese
     */
    public static int getWeekMonth(Date data) {
        Calendar calendario = getCal(data);
        return calendario.get(Calendar.WEEK_OF_MONTH);
    }// end of static method

    /**
     * Ritorna il numero del giorno dell'anno di una data fornita.
     * <p>
     *
     * @param data fornita
     *
     * @return il numero del giorno dell'anno
     */
    public static int getDayYear(Date data) {
        Calendar calendario = getCal(data);
        return calendario.get(Calendar.DAY_OF_YEAR);
    }// end of static method

    /**
     * Ritorna il numero del giorno del mese di una data fornita.
     * <p>
     *
     * @param data fornita
     *
     * @return il numero del giorno del mese
     */
    public static int getDayMonth(Date data) {
        Calendar calendario = getCal(data);
        return calendario.get(Calendar.DAY_OF_MONTH);
    }// end of static method

    /**
     * Ritorna il numero del giorno della settimana di una data fornita.
     * <p>
     *
     * @param data fornita
     *
     * @return il numero del giorno della settimana (1=dom, 7=sab)
     */
    public static int getDayWeek(Date data) {
        Calendar calendario = getCal(data);
        return calendario.get(Calendar.DAY_OF_WEEK);
    }// end of static method

    /**
     * Ritorna il numero delle ore di una data fornita.
     * <p>
     *
     * @param data fornita
     *
     * @return il numero delle ore
     */
    public static int getOre(Date data) {
        Calendar calendario = getCal(data);
        return calendario.get(Calendar.HOUR_OF_DAY);
    }// end of static method

    /**
     * Ritorna il numero dei minuti di una data fornita.
     * <p>
     *
     * @param data fornita
     *
     * @return il numero dei minuti
     */
    public static int getMinuti(Date data) {
        Calendar calendario = getCal(data);
        return calendario.get(Calendar.MINUTE);
    }// end of static method

    /**
     * Ritorna il numero dei secondi di una data fornita.
     * <p>
     *
     * @param data fornita
     *
     * @return il numero dei secondi
     */
    public static int getSecondi(Date data) {
        Calendar calendario = getCal(data);
        return calendario.get(Calendar.SECOND);
    }// end of static method

    /**
     * Ritorna il numero dell'anno di una data fornita.
     * <p>
     *
     * @return il numero dell'anno
     */
    public static int getYear(Date data) {
        Calendar calendario = getCal(data);
        return calendario.get(Calendar.YEAR);
    }// end of static method

    /**
     * Ritorna il numero del giorno dell'anno della data corrente.
     * <p>
     *
     * @return il numero del giorno dell'anno
     */
    public static int getDayYear() {
        return getDayYear(new Date());
    }// end of static method

    /**
     * Ritorna il numero del giorno del mese della data corrente.
     * <p>
     *
     * @return il numero del giorno del mese
     */
    public static int getDayMonth() {
        return getDayMonth(new Date());
    }// end of static method

    /**
     * Ritorna il numero del giorno della settimana della data corrente.
     * <p>
     *
     * @return il numero del giorno della settimana (1=dom, 7=sab)
     */
    public static int getDayWeek() {
        return getDayWeek(new Date());
    }// end of static method


    /**
     * Ritorna il numero delle ore della data corrente.
     *
     * @return il numero delle ore
     */
    public static int getOre() {
        return getOre(new Date());
    }// end of static method

    /**
     * Ritorna il numero dei minuti della data corrente.
     *
     * @return il numero dei minuti
     */
    public static int getMinuti() {
        return getMinuti(new Date());
    }// end of static method

    /**
     * Ritorna il numero dei secondi della data corrente.
     *
     * @return il numero dei secondi
     */
    public static int getSecondi() {
        return getSecondi(new Date());
    }// end of static method

    /**
     * Ritorna il numero dell'anno della data corrente.
     *
     * @return il numero dei secondi
     */
    public static int getYear() {
        return getYear(new Date());
    }// end of static method

    /**
     * Costruisce la data per il 1° gennaio dell'anno corrente.
     * <p>
     *
     * @return primo gennaio dell'anno
     */
    public static Date getPrimoGennaio() {
        return creaData(1, 1, LibDate.getYear());
    }// end of static method


    /**
     * Costruisce la data per il 1° gennaio dell'anno indicato.
     * <p>
     *
     * @param anno di riferimento
     *
     * @return primo gennaio dell'anno
     */
    public static Date getPrimoGennaio(int anno) {
        return creaData(1, 1, anno);
    }// end of static method

    /**
     * Costruisce la data per il 31° dicembre dell'anno indicato.
     * <p>
     *
     * @param anno di riferimento
     *
     * @return ultimo dell'anno
     */
    public static Date getTrentunoDicembre(int anno) {
        Date data = creaData(31, 12, anno);
        return lastTime(data);
    }// end of static method

    /**
     * Crea una data.
     * <p>
     *
     * @param giorno          il giorno del mese (1 per il primo)
     * @param numMeseDellAnno il mese dell'anno (1 per gennaio)
     * @param anno            l'anno
     *
     * @return la data creata
     */
    public static Date creaData(int giorno, int numMeseDellAnno, int anno) {
        return creaData(giorno, numMeseDellAnno, anno, 0, 0, 0);
    }// end of static method

    /**
     * Crea una data.
     * <p>
     *
     * @param giorno          il giorno del mese (1 per il primo)
     * @param numMeseDellAnno il mese dell'anno (1 per gennaio)
     * @param anno            l'anno
     * @param ora             ora (24H)
     * @param minuto          il minuto
     * @param secondo         il secondo
     *
     * @return la data creata
     */
    public static Date creaData(int giorno, int numMeseDellAnno, int anno, int ora, int minuto, int secondo) {
        Calendar calendario;

        if (numMeseDellAnno > 0) {
            numMeseDellAnno--;
        }// fine del blocco if

        calendario = new GregorianCalendar(anno, numMeseDellAnno, giorno, ora, minuto, secondo);
        return calendario.getTime();
    }// end of static method

    /**
     * Elimina ore/minuti/secondi da una data.
     * <p>
     *
     * @param dateIn la data dalla quale eliminare ore/minuti/secondi
     *
     * @return la data senza ore/minuti/secondi
     */
    public static Date dropTime(Date dateIn) {
        Calendar calendario = getCal(dateIn);

        calendario.set(Calendar.HOUR_OF_DAY, 0);
        calendario.set(Calendar.MINUTE, 0);
        calendario.set(Calendar.SECOND, 0);
        calendario.set(Calendar.MILLISECOND, 0);

        return calendario.getTime();
    }// end of static method

    /**
     * Forza la data all'ultimo millisecondo.
     * <p>
     *
     * @param dateIn la data da forzare
     *
     * @return la data con ore/minuti/secondi/millisecondi al valore massimo
     */
    public static Date lastTime(Date dateIn) {
        Calendar calendario = getCal(dateIn);

        calendario.set(Calendar.HOUR_OF_DAY, 23);
        calendario.set(Calendar.MINUTE, 59);
        calendario.set(Calendar.SECOND, 59);
        calendario.set(Calendar.MILLISECOND, 999);

        return calendario.getTime();
    }// end of static method

    /**
     * Controlla se una data è precedente ad una data di riferimento.
     * <p>
     * Esegue solo se le date sono valide (non nulle e non vuote) <br>
     * Gli estremi sono o non sono compresi (come da flag) <br>
     *
     * @param dataTest da controllare
     * @param dataRif  di riferimento
     * @param flag     sugli estremi dell'intervallo
     *
     * @return vero se la seconda data è precedente alla prima
     */
    private static boolean isPrecedenteBase(Date dataTest, Date dataRif, Estremi flag) {
        boolean status = false;
        long longRif = dataRif.getTime();
        long longTest = dataTest.getTime();

        status = longTest < longRif;

        if (flag == Estremi.compresi) {
            if (!status) {
                if (dataTest.equals(dataRif)) {
                    status = true;
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        return status;
    }// end of static method

    /**
     * Controlla se una data è precedente ad una data di riferimento.
     * <p>
     * Esegue solo se le date sono valide (non nulle e non vuote) <br>
     * Gli estremi NON sono compresi <br>
     * Se le date coincidono, la risposta è FALSE <br>
     *
     * @param dataTest da controllare
     * @param dataRif  di riferimento
     *
     * @return vero se la seconda data è precedente alla prima
     */
    public static boolean isPrecedente(Date dataTest, Date dataRif) {
        return isPrecedenteEsclusi(dataTest, dataRif);
    }// end of static method

    /**
     * Controlla se una data è precedente ad una data di riferimento.
     * <p>
     * Esegue solo se le date sono valide (non nulle e non vuote) <br>
     * Gli estremi NON sono compresi <br>
     * Se le date coincidono, la risposta è FALSE <br>
     *
     * @param dataTest da controllare
     * @param dataRif  di riferimento
     *
     * @return vero se la seconda data è precedente alla prima
     */
    public static boolean isPrecedenteEsclusi(Date dataTest, Date dataRif) {
        return isPrecedenteBase(dataTest, dataRif, Estremi.esclusi);
    }// end of static method

    /**
     * Controlla se una data è precedente ad una data di riferimento.
     * <p>
     * Esegue solo se le date sono valide (non nulle e non vuote) <br>
     * Gli estremi SONO compresi <br>
     * Se le date coincidono, la risposta è TRUE <br>
     *
     * @param dataTest da controllare
     * @param dataRif  di riferimento
     *
     * @return vero se la seconda data è precedente alla prima
     */
    public static boolean isPrecedenteUguale(Date dataTest, Date dataRif) {
        return isPrecedenteBase(dataTest, dataRif, Estremi.compresi);
    }// end of static method

    /**
     * Controlla se una data è successiva ad una data di riferimento.
     * <p>
     * Esegue solo se le date sono valide (non nulle e non vuote) <br>
     * Gli estremi sono o non sono compresi (come da flag) <br>
     *
     * @param dataTest da controllare
     * @param dataRif  di riferimento
     * @param flag     sugli estremi dell'intervallo
     *
     * @return vero se la seconda data è successiva alla prima
     */
    private static boolean isSuccessivaBase(Date dataTest, Date dataRif, Estremi flag) {
        boolean status = false;
        long longRif = dataRif.getTime();
        long longTest = dataTest.getTime();

        status = longTest > longRif;

        if (flag == Estremi.compresi) {
            if (!status) {
                if (dataTest.equals(dataRif)) {
                    status = true;
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        return status;
    }// end of static method

    /**
     * Controlla se una data è successiva ad una data di riferimento.
     * <p>
     * Esegue solo se le date sono valide (non nulle e non vuote) <br>
     * Gli estremi NON sono compresi <br>
     * Se le date coincidono, la risposta è FALSE <br>
     *
     * @param dataTest da controllare
     * @param dataRif  di riferimento
     *
     * @return vero se la seconda data è successiva alla prima
     */
    public static boolean isSuccessiva(Date dataTest, Date dataRif) {
        return isSuccessivaEsclusi(dataTest, dataRif);
    }// end of static method

    /**
     * Controlla se una data è successiva ad una data di riferimento.
     * <p>
     * Esegue solo se le date sono valide (non nulle e non vuote) <br>
     * Gli estremi NON sono compresi <br>
     * Se le date coincidono, la risposta è FALSE <br>
     *
     * @param dataTest da controllare
     * @param dataRif  di riferimento
     *
     * @return vero se la seconda data è successiva alla prima
     */
    public static boolean isSuccessivaEsclusi(Date dataTest, Date dataRif) {
        return isSuccessivaBase(dataTest, dataRif, Estremi.esclusi);
    }// end of static method

    /**
     * Controlla se una data è successiva ad una data di riferimento.
     * <p>
     * Esegue solo se le date sono valide (non nulle e non vuote) <br>
     * Gli estremi SONO compresi <br>
     * Se le date coincidono, la risposta è TRUE <br>
     *
     * @param dataTest da controllare
     * @param dataRif  di riferimento
     *
     * @return vero se la seconda data è successiva alla prima
     */
    public static boolean isSuccessivaUguale(Date dataTest, Date dataRif) {
        return isSuccessivaBase(dataTest, dataRif, Estremi.compresi);
    }// end of static method

    /**
     * Controlla se una data è compresa tra due date di riferimento.
     * <p>
     * Esegue solo se le date sono valide (non nulle e non vuote) <br>
     * Gli estremi sono o non sono compresi (come da flag) <br>
     *
     * @param dataTest   data da controllare
     * @param dataInizio data di riferimento iniziale dell'intervallo
     * @param dataFine   data di riferimento finale dell'intervallo
     * @param flag       sugli estremi dell'intervallo
     *
     * @return vero se la data è compresa nell'intervallo di date
     */
    private static boolean isCompresaBase(Date dataTest, Date dataInizio, Date dataFine, Estremi flag) {
        boolean status = false;
        boolean precedente;
        boolean successiva;

        precedente = isPrecedente(dataTest, dataFine);
        successiva = isSuccessiva(dataTest, dataInizio);
        status = precedente && successiva;

        if (flag == Estremi.compresi) {
            if (dataTest.equals(dataInizio) || dataTest.equals(dataFine)) {
                status = true;
            }// fine del blocco if
        }// fine del blocco if

        return status;
    }// end of static method

    /**
     * Controlla se una data è compresa tra due date di riferimento.
     * <p>
     * Esegue solo se le date sono valide (non nulle e non vuote) <br>
     * Gli estremi NON sono compresi <br>
     * Se la data coincide con uno degli estremi, la risposta è FALSE <br>
     *
     * @param dataTest   data da controllare
     * @param dataInizio data di riferimento iniziale dell'intervallo
     * @param dataFine   data di riferimento finale dell'intervallo
     *
     * @return vero se la data è compresa nell'intervallo di date
     */
    public static boolean isCompresa(Date dataTest, Date dataInizio, Date dataFine) {
        return isCompresaEsclusi(dataTest, dataInizio, dataFine);
    }// end of static method

    /**
     * Controlla se una data è compresa tra due date di riferimento.
     * <p>
     * Esegue solo se le date sono valide (non nulle e non vuote) <br>
     * Gli estremi NON sono compresi <br>
     * Se la data coincide con uno degli estremi, la risposta è FALSE <br>
     *
     * @param dataTest   data da controllare
     * @param dataInizio data di riferimento iniziale dell'intervallo
     * @param dataFine   data di riferimento finale dell'intervallo
     *
     * @return vero se la data è compresa nell'intervallo di date
     */
    public static boolean isCompresaEsclusi(Date dataTest, Date dataInizio, Date dataFine) {
        return isCompresaBase(dataTest, dataInizio, dataFine, Estremi.esclusi);
    }// end of static method

    /**
     * Controlla se una data è compresa tra due date di riferimento.
     * <p>
     * Esegue solo se le date sono valide (non nulle e non vuote) <br>
     * Gli estremi SONO compresi <br>
     * Se la data coincide con uno degli estremi, la risposta è TRUE <br>
     *
     * @param dataTest   data da controllare
     * @param dataInizio data di riferimento iniziale dell'intervallo
     * @param dataFine   data di riferimento finale dell'intervallo
     *
     * @return vero se la data è compresa nell'intervallo di date
     */
    public static boolean isCompresaUguale(Date dataTest, Date dataInizio, Date dataFine) {
        return isCompresaBase(dataTest, dataInizio, dataFine, Estremi.compresi);
    }// end of static method

    /**
     * Restituisce la data minore (più vecchia) delle due.
     * <p>
     * Esegue solo se le date sono valide (non nulle e non vuote) <br>
     *
     * @param dataUno   data da controllare
     * @param dataDue   data da controllare
     *
     * @return data maggiore delle due
     */
    public static Date minore(Date dataUno, Date dataDue) {
        if (isPrecedente(dataUno,dataDue)) {
            return dataUno;
        } else {
            return dataDue;
        }// end of if/else cycle
    }// end of static method

    /**
     * Restituisce la data maggiore (più recente) delle due.
     * <p>
     * Esegue solo se le date sono valide (non nulle e non vuote) <br>
     *
     * @param dataUno   data da controllare
     * @param dataDue   data da controllare
     *
     * @return data maggiore delle due
     */
    public static Date maggiore(Date dataUno, Date dataDue) {
        if (isSuccessiva(dataUno,dataDue)) {
            return dataUno;
        } else {
            return dataDue;
        }// end of if/else cycle
    }// end of static method

    /**
     * Ritorna la data del primo giorno del mese relativo a una data fornita.
     * <p>
     *
     * @param dataIn fornita
     *
     * @return la data rappresentante il giorno del mese
     */
    public static Date getInizioMese(Date dataIn) {
        Calendar calendario = getCal(dataIn);
        int num = calendario.getActualMinimum(Calendar.DAY_OF_MONTH);
        calendario.set(Calendar.DAY_OF_MONTH, num);

        return calendario.getTime();
    }// end of static method

    /**
     * Ritorna la data dell'ultimo giorno del mese relativo a una data fornita.
     * <p>
     *
     * @param dataIn fornita
     *
     * @return la data rappresentante l'ultimo giorno del mese
     */
    public static Date getFineMese(Date dataIn) {
        Calendar calendario = getCal(dataIn);
        int num = calendario.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendario.set(Calendar.DAY_OF_MONTH, num);

        return calendario.getTime();
    }// end of static method

    /**
     * Ritorna la data del primo giorno del mese della data corrente.
     * <p>
     *
     * @return la data rappresentante il giorno del mese
     */
    public static Date getInizioMese() {
        return getInizioMese(new Date());
    }// end of static method

    /**
     * Ritorna la data dell'ultimo giorno del mese della data corrente.
     * <p>
     *
     * @return la data rappresentante l'ultimo giorno del mese
     */
    public static Date getFineMese() {
        return getFineMese(new Date());
    }// end of static method

    /**
     * Calendario gregoriano
     */
    private static Calendar getCal() {
        /* crea il calendario */
        Calendar calendario = new GregorianCalendar(0, 0, 0, 0, 0, 0);

        /**
         * regola il calendario come non-lenient (se la data non è valida non effettua la rotazione automatica dei
         * valori dei campi, es. 32-12-2004 non diventa 01-01-2005)
         */
        calendario.setLenient(false);

        return calendario;
    }// end of static method

    /**
     * Calendario con regolata la data
     *
     * @param data da inserire nel calendario
     *
     * @return calendario regolato
     */
    private static Calendar getCal(Date data) {
        Calendar calendario = getCal();
        calendario.setTime(data);
        return calendario;
    }// end of static method

    /**
     * Enumeration locale per il flag di inizio oppure fine
     */
    private enum InizioOppureFine {
        inizio, fine
    }// end of inner class

    /**
     * Enumeration locale per il flag sugli estremi dell'intervallo
     */
    private enum Estremi {
        esclusi, compresi
    }// end of inner class


}// end of abstract static class
