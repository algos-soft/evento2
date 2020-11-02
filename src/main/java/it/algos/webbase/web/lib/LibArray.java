package it.algos.webbase.web.lib;

import java.text.Collator;
import java.util.*;


/**
 * Created by Gac on 24 ago 2015.
 * Classe statica di libreria
 * <p>
 * 1) First and Major difference between Array and ArrayList in Java is that Array is a fixed length data structure
 * while ArrayList is a variable length Collection class.
 * You can not change length of Array once created in Java but ArrayList re-size itself when gets full depending upon capacity
 * and load factor. Since ArrayList is internally backed by Array in Java, any resize operation in ArrayList will slow down performance
 * as it involves creating new Array and copying content from old array to new array.
 * 2) Another difference between Array and ArrayList in Java is that you can not use Generics along with Array,
 * as Array instance knows about what kind of type it can hold and throws ArrayStoreException,
 * if you try to store type which is not convertible into type of Array. ArrayList allows you to use Generics to ensure type-safety.
 * 3) You can also compare Array vs ArrayList on How to calculate length of Array or size of ArrayList.
 * All kinds of Array provides length variable which denotes length of Array while ArrayList provides size()
 * method to calculate size of ArrayList in Java.
 * 4) One more major difference between ArrayList and Array is that, you can not store primitives in ArrayList, it can only contain Objects.
 * While Array can contain both primitives and Objects in Java. Though Autoboxing of Java 5 may give you an impression
 * of storing primitives in ArrayList, it actually automatically converts primitives to Object. e.g.
 * <p>
 * Resizable :   Array is static in size that is fixed length data structure, One can not change the length after creating the Array object.
 * Performance : Performance of Array and ArrayList depends on the operation you are performing :
 * Primitives :  ArrayList can not contains primitive data types (like int , float , double) it can only contains Object while
 * Array can contain both primitive data types as well as objects.
 * Iterating the values : We can use iterator  to iterate through ArrayList
 * Type-Safety :  In Java , one can ensure Type Safety through Generics. while Array is a homogeneous data structure ,
 * thus it will contain objects of specific class or primitives of specific  data type.
 * Length :  Length of the ArrayList is provided by the size()
 * Adding elements : We can insert elements into the arraylist object using the add() method while
 * in array we insert elements using the assignment operator.
 * Multi-dimensional :  Array can be multi dimensional , while ArrayList is always single dimensional.
 */
public abstract class LibArray {


    /**
     * Utility di conversione di una stringa.
     * <p>
     * Crea una lista da un testo che usa una virgola come separatore
     * Vengono eliminati gli spazi vuoti iniziali e finali
     *
     * @param testo da suddividere
     * @return una lista contenente le parti di stringa separate
     */
    public static ArrayList<String> fromStringaVirgola(String testo) {
        return fromStringa(testo, ",");
    } // fine del metodo

    /**
     * Utility di conversione di una stringa.
     * <p>
     * Crea una lista da un testo che usa un separatore
     * Vengono eliminati gli spazi vuoti iniziali e finali
     *
     * @param testo da suddividere
     * @param sep   separatore
     * @return una lista contenente le parti di stringa separate
     */
    public static ArrayList<String> fromStringa(String testo, String sep) {
        ArrayList<String> array = null;
        String[] parti = null;

        if (!testo.equals("") && !sep.equals("")) {
            array = new ArrayList<String>();
            if (testo.contains(sep)) {
                parti = testo.split(sep);
            } else {
                array.add(testo);
            }// end of if/else cycle
        }// end of if cycle

        if (parti != null) {
            for (String elemento : parti) {
                array.add(elemento.trim());
            }// end of for cycle
        }// end of if cycle

        return array;
    } // fine del metodo

    /**
     * Convert a stringArray to ArrayList
     *
     * @param stringArray to convert
     * @return the corresponding casted ArrayList
     */
    public static ArrayList<String> fromString(String[] stringArray) {
        return new ArrayList<String>(Arrays.asList(stringArray));
    } // end of static method

    /**
     * Convert a intArray to ArrayList
     *
     * @param intArray to convert
     * @return the corresponding casted ArrayList
     */
    public static ArrayList<Integer> fromInt(int[] intArray) {
        ArrayList<Integer> intList = new ArrayList<Integer>();

        for (Integer intero : intArray) {
            intList.add(intero);
        } // fine del ciclo for-each

        return intList;
    } // end of static method

    /**
     * Convert a longArray to ArrayList
     *
     * @param longArray to convert
     * @return the corresponding casted ArrayList
     */
    public static ArrayList<Long> fromLong(long[] longArray) {
        ArrayList<Long> longList = new ArrayList<Long>();

        for (Long lungo : longArray) {
            longList.add(lungo);
        } // fine del ciclo for-each

        return longList;
    } // end of static method


    /**
     * Convert a objArray to ArrayList
     *
     * @param objArray to convert
     * @return the corresponding casted ArrayList
     */
    public static ArrayList<Object> fromObj(Object[] objArray) {
        ArrayList<Object> objList = new ArrayList<Object>();

        for (Object lungo : objArray) {
            objList.add(lungo);
        } // fine del ciclo for-each

        return objList;
    } // end of static method


    /**
     * Estrae i valori unici da un lista con (eventuali) valori doppi
     *
     * @param listaValoriDoppi in ingresso
     * @return valoriUnici NON ordinati, null se listaValoriDoppi è null
     */
    public static ArrayList valoriUniciDisordinati(ArrayList listaValoriDoppi) {
        return valoriUniciBase(listaValoriDoppi, false);
    }// end of static method


    /**
     * Estrae i valori unici da un matrice (objArray) con (eventuali) valori doppi
     *
     * @param objArray in ingresso
     * @return valoriUnici NON ordinati, null se objArray è null
     */
    public static ArrayList valoriUniciDisordinati(Object[] objArray) {
        return valoriUniciDisordinati(fromObj(objArray));
    }// end of static method


    /**
     * Estrae i valori unici da un lista con (eventuali) valori doppi
     * Ordina l'array secondo la classe utilizzata:
     * alfabetico per le stringhe
     * numerico per i numeri
     * L'ordinamento funziona SOLO se la lista è omogenea (oggetti della stessa classe)
     *
     * @param listaValoriDoppi in ingresso
     * @return valoriUnici ORDINATI, null se listaValoriDoppi è null
     */
    public static ArrayList valoriUnici(ArrayList listaValoriDoppi) {
        return valoriUniciBase(listaValoriDoppi, true);
    }// end of static method


    /**
     * Estrae i valori unici da un matrice (objArray) con (eventuali) valori doppi
     * Ordina l'array secondo la classe utilizzata:
     * alfabetico per le stringhe
     * numerico per i numeri
     * L'ordinamento funziona SOLO se la lista è omogenea (oggetti della stessa classe)
     *
     * @param objArray in ingresso
     * @return valoriUnici ORDINATI, null se listaValoriDoppi è null
     */
    public static ArrayList valoriUnici(Object[] objArray) {
        return valoriUnici(fromObj(objArray));
    }// end of static method

    /**
     * Estrae i valori unici da un matrice (objArray) con (eventuali) valori doppi
     * Ordina l'array secondo la classe utilizzata:
     * alfabetico per le stringhe
     * numerico per i numeri
     * L'ordinamento funziona SOLO se la lista è omogenea (oggetti della stessa classe)
     *
     * @param intArray in ingresso
     * @return valoriUnici ORDINATI, null se listaValoriDoppi è null
     */
    public static ArrayList valoriUnici(int[] intArray) {
        return valoriUnici(fromInt(intArray));
    }// end of static method

    /**
     * Estrae i valori unici da un matrice (objArray) con (eventuali) valori doppi
     * Ordina l'array secondo la classe utilizzata:
     * alfabetico per le stringhe
     * numerico per i numeri
     * L'ordinamento funziona SOLO se la lista è omogenea (oggetti della stessa classe)
     *
     * @param longArray in ingresso
     * @return valoriUnici ORDINATI, null se listaValoriDoppi è null
     */
    public static ArrayList valoriUnici(long[] longArray) {
        return valoriUnici(fromLong(longArray));
    }// end of static method


    /**
     * Estrae i valori unici da un lista con (eventuali) valori doppi
     * Eventualmente (tag booleano) ordina l'array secondo la classe utilizzata:
     * alfabetico per le stringhe
     * numerico per i numeri
     *
     * @param listaValoriDoppi in ingresso
     * @param ordina           tag per forzare l'ordinamento
     * @return valoriUnici disordinati oppure ordinati, null se listaValoriDoppi è null
     */
    @SuppressWarnings("all")
    private static ArrayList valoriUniciBase(ArrayList listaValoriDoppi, boolean ordina) {
        ArrayList listaValoriUniciNonOrdinati = null;
        Set set;

        if (listaValoriDoppi != null) {
            set = new LinkedHashSet((List) listaValoriDoppi);
            listaValoriUniciNonOrdinati = new ArrayList(set);
            if (ordina) {
                return sort(listaValoriUniciNonOrdinati);
            } else {
                return listaValoriUniciNonOrdinati;
            }// fine del blocco if-else
        }// fine del blocco if

        return null;
    }// end of static method

    /**
     * Ordina la lista
     * L'ordinamento funziona SOLO se la lista è omogenea (oggetti della stessa classe)
     *
     * @param listaDisordinata in ingresso
     * @return lista ordinata, null se listaDisordinata è null
     */
    public static ArrayList sort(ArrayList listaDisordinata) {
        ArrayList<Object> objList;
        Object[] objArray = listaDisordinata.toArray();

        try { // prova ad eseguire il codice
            Arrays.sort(objArray);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        objList = fromObj(objArray);

        return objList;
    }// end of static method


    /**
     * Ordina la lista, tenendo conto degli accenti
     *
     * @param listaDisordinata in ingresso
     * @return lista ordinata, null se listaDisordinata è null
     */
    public static ArrayList<String> sortAccentiSensibile(ArrayList<String> listaDisordinata) {
        ArrayList<Object> objList;
        Collator usCollator = Collator.getInstance(Locale.US); //Your locale here
        usCollator.setStrength(Collator.PRIMARY); //desired strength
        Collections.sort(listaDisordinata, usCollator);

        return listaDisordinata;
    }// end of static method


    /**
     * Aggiunge un elemento alla lista solo se non già esistente
     *
     * @param lista    che viene modificata
     * @param elemento da inserire, se manca
     * @return vero se l'elemento è stato aggiunto
     */
    @SuppressWarnings("all")
    public static boolean add(ArrayList lista, Object elemento) {
        boolean aggiunto = false;

        if (!lista.contains(elemento)) {
            aggiunto = lista.add(elemento);
        }// fine del blocco if

        return aggiunto;
    }// end of static method

    /**
     * Somma due array (liste) e restituisce una lista NON ordinata
     * <p>
     * Almeno uno dei due array in ingresso deve essere non nullo
     * Normalmente si usa di meno la somma disordinata
     * I valori negli array sono unici
     * <p>
     * Se entrambi i parametri sono nulli, restituisce un nullo
     * Se uno dei parametri è nullo, restituisce l'altro
     * La lista di valori in uscita è unica (quindi la dimensione può essere minore dalla somma delle due)
     *
     * @param arrayPrimo   - prima lista
     * @param arraySecondo - seconda lista
     * @return arraySomma disordinata
     */
    public static ArrayList sommaDisordinata(ArrayList arrayPrimo, ArrayList arraySecondo) {
        ArrayList arraySomma = null;

        if (arrayPrimo != null && arraySecondo != null) {
            arraySomma = new ArrayList();
            for (Object ogg : arrayPrimo) {
                arraySomma.add(ogg);
            } // fine del ciclo for-each
            for (Object ogg : arraySecondo) {
                arraySomma.add(ogg);
            } // fine del ciclo for-each
            arraySomma = valoriUniciBase(arraySomma, false);
        }// fine del blocco if

        if (arrayPrimo == null) {
            arraySecondo = valoriUniciBase(arraySecondo, false);
            return arraySecondo;
        }// fine del blocco if

        if (arraySecondo == null) {
            arrayPrimo = valoriUniciBase(arrayPrimo, false);
            return arrayPrimo;
        }// fine del blocco if

        return arraySomma;
    }// end of static method


    /**
     * Somma due array (liste) e restituisce una lista ordinata
     * <p>
     * Almeno uno dei due array in ingresso deve essere non nullo
     * Normalmente si usa di più la somma ordinata
     * I valori negli array sono unici
     * <p>
     * Se entrambi i parametri sono nulli, restituisce un nullo
     * Se uno dei parametri è nullo, restituisce l'altro
     * La lista di valori in uscita è unica (quindi la dimensione può essere minore dalla somma delle due)
     *
     * @param arrayPrimo   - prima lista
     * @param arraySecondo - seconda lista
     * @return arraySomma ordinata
     */
    public static ArrayList somma(ArrayList arrayPrimo, ArrayList arraySecondo) {
        ArrayList arraySomma = sommaDisordinata(arrayPrimo, arraySecondo);

        if (arraySomma != null) {
            arraySomma = sort(arraySomma);
        }// fine del blocco if

        return arraySomma;
    }// end of static method


    /**
     * Differenza tra due array (liste) e restituisce una lista NON ordinata
     * <p>
     * Il primo array in ingresso deve essere non nullo
     * Normalmente si usa di meno la differenza disordinata
     * I valori negli array sono unici
     * <p>
     * Se il primo parametro è nullo, restituisce un nullo
     * Se il secondo parametro è nullo, restituisce la prima lista
     * La lista di valori in uscita è unica
     *
     * @param arrayPrimo   - prima lista
     * @param arraySecondo - seconda lista
     * @return arrayDifferenza disordinato
     */
    @SuppressWarnings("all")
    public static ArrayList differenzaDisordinata(ArrayList arrayPrimo, ArrayList arraySecondo) {
        ArrayList arrayDifferenza = null;

        if (arraySecondo == null) {
            return arrayPrimo;
        }// fine del blocco if

        if (arraySecondo.size() == 0) {
            return arrayPrimo;
        }// fine del blocco if

        if (arrayPrimo != null) {
            if (arrayPrimo.get(0).getClass() == arraySecondo.get(0).getClass()) {
                arrayDifferenza = new ArrayList();
                for (Object obj : arrayPrimo) {
                    if (!arraySecondo.contains(obj)) {
                        arrayDifferenza.add(obj);
                    }// fine del blocco if
                } // fine del ciclo for-each
            }// fine del blocco if
        }// fine del blocco if

        return arrayDifferenza;
    }// end of static method

    /**
     * Differenza tra due array (liste) e restituisce una lista ordinata
     * <p>
     * Il primo array in ingresso deve essere non nullo e deve essere una lista
     * I valori negli array sono unici
     * Normalmente si usa di più la differenza ordinata
     * <p>
     * Se il primo parametro è nullo, restituisce un nullo
     * Se il secondo parametro è nullo, restituisce la prima lista
     * La lista di valori in uscita è unica
     *
     * @param arrayPrimo   - prima lista
     * @param arraySecondo - seconda lista
     * @return arrayDifferenza disordinato
     */
    @SuppressWarnings("all")
    public static ArrayList differenza(ArrayList arrayPrimo, ArrayList arraySecondo) {
        ArrayList arrayDifferenza = differenzaDisordinata(arrayPrimo, arraySecondo);

        if (arrayDifferenza != null) {
            arrayDifferenza = sort(arrayDifferenza);
        }// fine del blocco if

        return arrayDifferenza;
    }// end of static method

    /**
     * Costruisce una stringa con i singoli valori divisi da un pipe
     * <p>
     *
     * @param array lista di valori
     * @return stringa con i singoli valori divisi da un separatore
     */
    public static String toStringaPipe(ArrayList array) {
        return toStringa(array, "|");
    }// end of static method

    /**
     * Costruisce una stringa con i singoli valori divisi da una virgola
     * <p>
     *
     * @param array lista di valori
     * @return stringa con i singoli valori divisi da un separatore
     */
    public static String toStringaVirgola(ArrayList array) {
        return toStringa(array, ",");
    }// end of static method

    /**
     * Costruisce una stringa con i singoli valori divisi da un separatore
     * <p>
     *
     * @param array lista di valori
     * @param sep   carattere separatore
     * @return stringa con i singoli valori divisi da un separatore
     */
    public static String toStringa(List array, String sep) {
        String testo;
        StringBuilder textBuffer = new StringBuilder();

        for (Object obj : array) {
            textBuffer.append(obj.toString());
            textBuffer.append(sep);
        } // fine del ciclo for-each
        testo = textBuffer.toString();
        testo = LibText.levaCoda(testo, sep);

        return testo;
    }// end of static method

    /**
     * Costruisce una stringa con i singoli valori divisi da un pipe
     * <p>
     *
     * @param stringArray to convert
     * @return stringa con i singoli valori divisi da un separatore
     */
    public static String fromStringToStringaPipe(String[] stringArray) {
        return fromStringToStringa(stringArray, "|");
    }// end of static method

    /**
     * Costruisce una stringa con i singoli valori divisi da una virgola
     * <p>
     *
     * @param stringArray to convert
     * @return stringa con i singoli valori divisi da un separatore
     */
    public static String fromStringToStringaVirgola(String[] stringArray) {
        return fromStringToStringa(stringArray, ",");
    }// end of static method

    /**
     * Costruisce una stringa con i singoli valori divisi da un separatore
     * <p>
     *
     * @param stringArray to convert
     * @param sep         carattere separatore
     * @return stringa con i singoli valori divisi da un separatore
     */
    public static String fromStringToStringa(String[] stringArray, String sep) {
        List array = LibArray.fromString(stringArray);
        return toStringa(array, sep);
    }// end of static method

    /**
     * Recupera una lista delle chiavi di una mappa
     *
     * @param mappa in ingresso
     * @return lista delle chiavi
     */
    public static ArrayList getKeyFromMap(HashMap mappa) {
        ArrayList listaKeys = null;

        if (mappa != null) {
            listaKeys = new ArrayList();
            for (Object obj : mappa.keySet()) {
                listaKeys.add(obj);
            }// end of for cycle
        }// end of if cycle

        return listaKeys;
    }// end of static method

    /**
     * Recupera una lista dei valori di una mappa
     *
     * @param mappa in ingresso
     * @return lista delle chiavi
     */
    public static ArrayList getValueFromMap(HashMap mappa) {
        ArrayList listaKeys = null;

        if (mappa != null) {
            listaKeys = new ArrayList();
            for (Object obj : mappa.keySet()) {
                listaKeys.add(mappa.get(obj));
            }// end of for cycle
        }// end of if cycle

        return listaKeys;
    }// end of static method

    /**
     * Controlla l'eguaglianza di due array
     * <p>
     * Confronta tutti i valori, INDIPENDENTEMENTE dall'ordine in cui li trova
     *
     * @param expected previsto
     * @param actual   effettivo
     * @return vero, se gli array sono lunghi uguali ed hanno gli stessi valori (disordinati)
     */
    public static boolean isArrayEquals(ArrayList expected, ArrayList actual) {
        boolean uguali = true;

        if (expected == null || actual == null) {
            return false;
        }// end of if cycle

        if (expected.size() != actual.size()) {
            return false;
        }// end of if cycle

        for (Object obj : expected) {
            if (!actual.contains(obj)) {
                uguali = false;
            }// end of if cycle
        }// end of for cycle

        return uguali;
    }// end of static method

    /**
     * Controlla l'eguaglianza di due array
     * <p>
     * Confronta tutti i valori ORDINATI
     *
     * @param expected previsto
     * @param actual   effettivo
     * @return vero, se gli array sono lunghi uguali ed hanno gli stessi valori (ordinati)
     */
    public static boolean isArrayEqualsOrdinati(ArrayList expected, ArrayList actual) {
        boolean uguali = true;

        if (!isArrayEquals(expected, actual)) {
            return false;
        }// end of if cycle

        for (int k = 0; k < actual.size(); k++) {
            if (!actual.get(k).equals(expected.get(k))) {
                uguali = false;
            }// end of if cycle
        }// end of for cycle

        return uguali;
    }// end of static method

    /**
     * Controlla l'eguaglianza di due mappe
     * <p>
     * Confronta la lunghezza
     * Confronta tutte le chiavi
     * Confronta tutti i valori
     *
     * @param expected prevista
     * @param actual   effettiva
     * @return vero, se le mappe sono lunghe uguali, hanno le stesse chiavi e gli stessi valori
     */
    public static boolean isMapEquals(HashMap expected, HashMap actual) {
        boolean uguali = true;
        boolean continua = true;
        ArrayList keysExpected;
        ArrayList keysActual;
        ArrayList valueExpected;
        ArrayList valueActual;

        if (expected == null || actual == null) {
            return false;
        }// end of if cycle

        if (expected.size() != actual.size()) {
            return false;
        }// end of if cycle

        keysExpected = LibArray.getKeyFromMap(expected);
        keysActual = LibArray.getKeyFromMap(actual);
        if (!LibArray.isArrayEquals(keysExpected, keysActual)) {
            return false;
        }// end of if cycle

        valueExpected = LibArray.getValueFromMap(expected);
        valueActual = LibArray.getValueFromMap(actual);
        if (!LibArray.isArrayEquals(valueExpected, valueActual)) {
            return false;
        }// end of if cycle

        return uguali;
    }// end of static method

    /**
     * Numero di cicli
     *
     * @param totale da dividere
     * @param blocco divisore
     * @return numero di cicli
     */
    public static int numCicli(int totale, int blocco) {
        int cicli = 0;
        int resto;

        if (blocco < 0) {
            return 0;
        }// end of if cycle

        if (blocco == 0) {
            return totale;
        }// end of if cycle

        if (blocco > totale) {
            return 1;
        }// end of if cycle

        cicli = totale / blocco;
        resto = totale % blocco;
        if (resto > 0) {
            cicli++;
        }// end of if cycle

        return cicli;
    }// end of static method

    /**
     * Estra un subset dalla lista
     *
     * @param listatTotale  da suddividere
     * @param dimBlocco     di suddivisione
     * @param cicloCorrente attuale
     * @return sublista corrente del ciclo
     */
    public static ArrayList<String> estraeSublistaStr(ArrayList<String> listatTotale, int dimBlocco, int cicloCorrente) {
        ArrayList<String> sublista = null;
        int delta;

        if (listatTotale == null || listatTotale.size() < 1) {
            return null;
        }// end of if cycle

        delta = dimBlocco * (cicloCorrente + 1);
        delta = Math.min(listatTotale.size(), delta);
        sublista = new ArrayList<String>(listatTotale.subList(dimBlocco * cicloCorrente, delta));

        return sublista;
    }// end of static method


    /**
     * Estra un subset dalla lista
     *
     * @param listatTotale  da suddividere
     * @param dimBlocco     di suddivisione
     * @param cicloCorrente attuale
     * @return sublista corrente del ciclo
     */
    public static ArrayList<Integer> estraeSublistaInt(ArrayList<Integer> listatTotale, int dimBlocco, int cicloCorrente) {
        ArrayList<Integer> sublista = null;
        int delta;

        if (listatTotale == null || listatTotale.size() < 1) {
            return null;
        }// end of if cycle

        delta = dimBlocco * (cicloCorrente + 1);
        delta = Math.min(listatTotale.size(), delta);
        sublista = new ArrayList<Integer>(listatTotale.subList(dimBlocco * cicloCorrente, delta));

        return sublista;
    }// end of static method

    /**
     * Estra un subset dalla lista
     *
     * @param listatTotale  da suddividere
     * @param dimBlocco     di suddivisione
     * @param cicloCorrente attuale
     * @return sublista corrente del ciclo
     */
    public static ArrayList<Long> estraeSublistaLong(ArrayList<Long> listatTotale, int dimBlocco, int cicloCorrente) {
        ArrayList<Long> sublista = null;
        int delta;

        if (listatTotale == null || listatTotale.size() < 1) {
            return null;
        }// end of if cycle

        delta = dimBlocco * (cicloCorrente + 1);
        delta = Math.min(listatTotale.size(), delta);
        sublista = new ArrayList<Long>(listatTotale.subList(dimBlocco * cicloCorrente, delta));

        return sublista;
    }// end of static method


    /**
     * Ordina una mappa secondo le chiavi
     * <p>
     * Una HashMap è -automaticamente- ordinata secondo le proprie chiavi
     * Viceversa una LinkedHashMap ha un -proprio ordine interno- fissato alla creazione
     *
     * @param mappaIn ingresso da ordinare
     * @return mappa ordinata
     */
    public static LinkedHashMap ordinaMappa(Map mappaIn) {
//        LinkedHashMap mappaOrdinata = new LinkedHashMap();
//        Set insieme;
//        Object[] matrice;
//        ArrayList listaChiavi;
//        Object valore;
//
//        try { // prova ad eseguire il codice
//            insieme = mappaIn.keySet();
//            matrice = insieme.toArray();
//            listaChiavi = new ArrayList(Arrays.asList(matrice));
//            Collections.sort(listaChiavi);
//
//            for (Object chiave : listaChiavi) {
//                valore = mappaIn.get(chiave);
//                mappaOrdinata.put(chiave, valore);
//            } // fine del ciclo for-each
//        } catch (Exception unErrore) { // intercetta l'errore
//        }// fine del blocco try-catch
//
//        return mappaOrdinata;
        return ordinaMappaBase(mappaIn, (Collator) null);
    }// end of static method


    /**
     * Ordina una mappa secondo le chiavi, tenendo conto degli accenti
     * <p>
     * Una HashMap è -automaticamente- ordinata secondo le proprie chiavi
     * Viceversa una LinkedHashMap ha un -proprio ordine interno- fissato alla creazione
     *
     * @param mappaIn ingresso da ordinare
     * @return mappa ordinata
     */
    public static LinkedHashMap<String, Object> ordinaMappaAccentiSensibile(Map mappaIn) {
//        LinkedHashMap<String, Object> mappaOrdinata = new LinkedHashMap();
//        Set insieme;
//        Object[] matrice;
//        ArrayList<String> listaChiavi;
//        Object valore;
//        Collator usCollator = Collator.getInstance(Locale.US); //Your locale here
//        usCollator.setStrength(Collator.PRIMARY); //desired strength
//
//        try { // prova ad eseguire il codice
//            insieme = mappaIn.keySet();
//            matrice = insieme.toArray();
//            listaChiavi = new ArrayList(Arrays.asList(matrice));
//            Collections.sort(listaChiavi, usCollator);
//
//            for (String chiave : listaChiavi) {
//                valore = mappaIn.get(chiave);
//                mappaOrdinata.put(chiave, valore);
//            } // fine del ciclo for-each
//        } catch (Exception unErrore) { // intercetta l'errore
//        }// fine del blocco try-catch
//
//        return mappaOrdinata;

        return ordinaMappaBase(mappaIn, Collator.getInstance(Locale.US));
    }// end of static method


    /**
     * Ordina una mappa secondo le chiavi, eventualmente tenendo conto degli accenti
     * <p>
     * Una HashMap è -automaticamente- ordinata secondo le proprie chiavi
     * Viceversa una LinkedHashMap ha un -proprio ordine interno- fissato alla creazione
     *
     * @param mappaIn ingresso da ordinare
     * @return mappa ordinata
     */
    private static LinkedHashMap ordinaMappaBase(Map mappaIn, Collator usCollator) {
        LinkedHashMap mappaOrdinata = new LinkedHashMap();
        Set insieme;
        Object[] matrice;
        ArrayList listaChiavi;
        Object valore;

        try { // prova ad eseguire il codice
            insieme = mappaIn.keySet();
            matrice = insieme.toArray();
            listaChiavi = new ArrayList(Arrays.asList(matrice));

            if (usCollator != null) {
                usCollator.setStrength(Collator.PRIMARY); //desired strength
                Collections.sort(listaChiavi, usCollator);
            } else {
                Collections.sort(listaChiavi);
            }// end of if/else cycle

            for (Object chiave : listaChiavi) {
                valore = mappaIn.get(chiave);
                mappaOrdinata.put(chiave, valore);
            } // fine del ciclo for-each
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        return mappaOrdinata;
    }// end of static method


    /**
     * Controlla che l'array esista e non sia vuoto
     *
     * @return true se esiste e non è vuoto
     */
    public static boolean isValido(List lista) {
        boolean valido = false;

        if (lista != null && lista.size() > 0) {
            valido = true;
        }// end of if cycle

        return valido;
    }// end of static method

    /**
     * Controlla che l'array non esista o che sia vuoto
     *
     * @return true se non è valido
     */
    public static boolean isNotValido(List lista) {
        return !isValido(lista);
    }// end of static method

}// end of abstract static class
