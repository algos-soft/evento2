package it.algos.webbase.web.lib;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;

public abstract class Lib {

    /**
     * Tries to convert an Object in int.
     *
     * @param obj to convert
     * @return the corresponding int
     */
    public static int getInt(Object obj) {
        int intero = 0;

        if (obj == null) {
            return 0;
        }// fine del blocco if

        if (obj instanceof Number) {
            Number number = (Number) obj;
            intero = number.intValue();
            return intero;
        }// fine del blocco if

        if (obj instanceof String) {
            String string = (String) obj;
            try { // prova ad eseguire il codice
                intero = Integer.parseInt(string);
            } catch (Exception unErrore) { // intercetta l'errore
            }// fine del blocco try-catch
            return intero;
        }// fine del blocco if

        return 0;
    }// end of static method

    /**
     * Tries to convert an Object in long.
     *
     * @param obj to convert
     * @return the corresponding int
     */
    public static long getLong(Object obj) {
        long nlong = 0l;

        if (obj == null) {
            return 0l;
        }

        if (obj instanceof Number) {
            Number number = (Number) obj;
            nlong = number.longValue();
            return nlong;
        }

        if (obj instanceof String) {
            String string = (String) obj;
            nlong = Long.parseLong(string);
            return nlong;
        }

        return 0;
    }// end of static method

    /**
     * Tries to convert an Object in String.
     *
     * @param obj to convert
     * @return the corresponding String
     */
    public static String getString(Object obj) {
        String string = "";

        if (obj == null) {
            return "";
        }

        if (obj instanceof String) {
            string = (String) obj;
            return string;
        }

        return obj.toString();
    }// end of static method


    /**
     * Tries to convert an Object in Date.
     *
     * @param obj to convert
     * @return the corresponding date
     */
    public static Date getDate(Object obj) {
        Date date = null;

        if (obj == null) {
            return null;
        }// end of if cycle

        if (obj instanceof Date) {
            date = (Date) obj;
        }// end of if cycle

        return date;
    }// end of static method


    /**
     * Tries to convert an Object in BigDecimal.
     *
     * @param obj to convert
     * @return the corresponding BigDecimal
     */
    public static BigDecimal getBigDecimal(Object obj) {
        BigDecimal bd = new BigDecimal(0);

        if (obj == null) {
            return new BigDecimal(0);
        }

        if (obj instanceof BigDecimal) {
            return (BigDecimal) obj;
        }

        if (obj instanceof BigInteger) {
            return new BigDecimal((BigInteger) obj);
        }

        if (obj instanceof Long) {
            return new BigDecimal((long) obj);
        }

        if (obj instanceof Integer) {
            return new BigDecimal((int) obj);
        }

        if (obj instanceof Double) {
            return new BigDecimal((double) obj);
        }

        if (obj instanceof Float) {
            return new BigDecimal((float) obj);
        }

        return bd;
    }// end of static method

    /**
     * Tries to convert an Object in Boolean.
     *
     * @param obj to convert
     * @return the corresponding BigDecimal
     */
    public static boolean getBool(Object obj) {
        boolean b = false;

        if (obj == null) {
            return false;
        }

        if (obj instanceof Boolean) {
            return (boolean) obj;
        }

        return b;
    }// end of static method

    /**
     * Parser di una stringa
     *
     * @param testo da suddividere
     * @return array di stringhe
     */
    public static ArrayList<String> getArrayDaTesto(String testo) {
        return getArrayDaTesto(testo, ",");
    }// end of static method

    /**
     * Parser di una stringa
     *
     * @param testo  da suddividere
     * @param escape di separazione
     * @return array di stringhe
     */
    public static ArrayList<String> getArrayDaTesto(String testo, String escape) {
        ArrayList<String> array = new ArrayList<String>();
        String[] lista = null;

        if (!testo.equals("") && !escape.equals("")) {
            lista = testo.split(escape);
        }// end of if cycle

        if (lista != null) {
            for (String string : lista) {
                array.add(string.trim());
            }// end of for cycle
        }// end of if cycle

        return array;
    }// end of static method

}// end of abstract static class
