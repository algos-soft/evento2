package it.algos.webbase.web;

import javax.servlet.ServletContext;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Repository di costanti della applicazione
 * <p>
 * Alcune costanti sono 'static final', uguali per tutte le applicazioni e non modificabili
 * <p>
 * Altre costanti, pur esendo utilizzate da tutti i progetti, sono solo 'static' e quindi modificabili;
 * qui viene impostato solo il valore 'standard' iniziale
 * i progetti specifici possono modificare, normalmente nella classe xxxBootStrap,
 * queste costanti per l'utilizzo successivo
 * <p>
 * I singoli progetti hanno/possono avere una classe specifica xxxApp
 * (tecnicamente non è una sottoclasse perché sono classi astratte di metodi statici)
 * per gestire delle costanti specifiche del progetto stesso che non avrebbe senso generalizzare
 * <p>
 * Altre costanti 'static final' sono raggruppate nella classe it.algos.webbase.web.lib.Cost
 */
public abstract class AlgosApp {

    /**
     * Name of the folder for temporary uploaded files<br>
     * The folder is located in the context folder of the container
     */
    public static final String UPLOAD_FOLDER_NAME = "uploads";
    /**
     * Name of the folder for the files to download<br>
     * The folder is located in the context folder of the container
     */
    public static final String DOWNLOAD_FOLDER_NAME = "downloads";
    /**
     * Name of the base folder for demo data.<br>
     * Demo data is loaded at bootstrap to populate empty tables.<br>
     */
    public static final String DEMODATA_FOLDER_NAME = "WEB-INF/data/demo/";

    /**
     * Name of the base folder for images.<br>
     */
    public static final String IMG_FOLDER_NAME = "WEB-INF/img/";

//    /**
//     * Name of the local folder for images.<br>
//     * Must be overwritten on local xxxApp class
//     */
//    public static String LOC_IMG_FOLDER_NAME = "";

    /**
     * Use security access.<br>
     * Not final flag<br>
     * Can be overwritten on local xxxBootStrap.contextInitialized() method
     */
    public static boolean USE_SECURITY = false;

    /**
     * Use preferences entity.<br>
     * Not final flag<br>
     * Can be overwritten on local xxxBootStrap.contextInitialized() method
     */
    public static boolean USE_LOG = false;

    /**
     * Use versione entity.<br>
     * Not final flag<br>
     * Can be overwritten on local xxxBootStrap.contextInitialized() method
     */
    public static boolean USE_VERS = false;

    /**
     * Use logo entity.<br>
     * Not final flag<br>
     * Can be overwritten on local xxxBootStrap.contextInitialized() method
     */
    public static boolean USE_PREF = true;

    /**
     * Use company.<br>
     * Not final<br>
     * Can be overwritten on local xxxBootStrap.contextInitialized() method
     */
    public static boolean USE_COMPANY = false;

    /**
     * Use debug.<br>
     * Not final<br>
     */
    public static boolean IS_DEBUG = false;

    /**
     * Code of the company.<br>
     * Not final<br>
     */
    public static String COMPANY_CODE = "";
    /**
     * Path for the cookies.<br>
     * Not final<br>
     */
    public static String COOKIES_PATH = "/";
    /**
     * Null selection allowed in combobox.<br>
     * Not final<br>
     * Can be overwritten on local xxxBootStrap.contextInitialized() method
     */
    public static boolean COMBO_BOX_NULL_SELECTION_ALLOWED = true;
    /**
     * Display only the new record in the table, after successful editing (persisted).<br>
     * Not final flag<br>
     * Can be overwritten on local xxxApp class
     */
    public static boolean DISPLAY_NEW_RECORD_ONLY = true;
    /**
     * Display tooltips on rollover the field<br>
     * Not final flag<br>
     * Can be overwritten on local xxxApp class
     */
    public static boolean DISPLAY_TOOLTIPS = false;

    /**
     * Servlet context<br>
     * registered as soon as possible to make it available to every component
     */
    private static ServletContext servletContext;

    /**
     * Returns the path to the Uploads folder.
     *
     * @return the path to the Uploads folder
     */
    public static Path getUploadPath() {
        ServletContext sc = AlgosApp.getServletContext();
        return Paths.get(sc.getRealPath("/" + AlgosApp.UPLOAD_FOLDER_NAME));
    }// end of method

    public static Path getDownloadPath() {
        ServletContext sc = AlgosApp.getServletContext();
        return Paths.get(sc.getRealPath("/" + AlgosApp.DOWNLOAD_FOLDER_NAME));
    }

    public static Path getDemoDataFolderPath() {
        ServletContext sc = AlgosApp.getServletContext();
        return Paths.get(sc.getRealPath("/" + AlgosApp.DEMODATA_FOLDER_NAME));
    }

//    public static Path getLocImgFolderPath() {
//        ServletContext sc = AlgosApp.getServletContext();
//        return Paths.get(sc.getRealPath("/" + AlgosApp.LOC_IMG_FOLDER_NAME));
//    }// end of method


//    public static Path getImgFolderPath() {
//        Path path = null;
//        ServletContext sc = AlgosApp.getServletContext();
//        String pathTxt = AlgosApp.IMG_FOLDER_NAME;
//
//        if (sc != null) {
//            if (!pathTxt.startsWith("it")) {   //come può iniziare per it ? a cosa serve?
//                pathTxt = "/" + pathTxt;
//            }// fine del blocco if
//
//            path = Paths.get(sc.getRealPath(pathTxt));
//        }// fine del blocco if
//
//        return path;
//    }// end of method


    public static String getStrProjectPath() {
        ServletContext sc = AlgosApp.getServletContext();
        return Paths.get(sc.getRealPath("/")).toString();
    }// end of method

    public static ServletContext getServletContext() {
        return servletContext;
    }// end of method

    public static void setServletContext(ServletContext servletContext) {
        AlgosApp.servletContext = servletContext;
    }// end of method

}// end of class