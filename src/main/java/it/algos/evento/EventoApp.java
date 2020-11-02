package it.algos.evento;

import it.algos.webbase.web.AlgosApp;


/**
 * Contenitore di costanti della applicazione
 */
public abstract class EventoApp extends AlgosApp {

    /**
     * the application name
     */
    public static final String APP_NAME = "eVento";

    /**
     * Nome dell'utente bot
     */
    public static final String BOT_USER = "SYS_ROBOT";


    /**
     * Nome della pagina di conferma prenotazione
     */
    public static final String CONF_PAGE = "conf";

    /**
     * Nome della pagina del admin
     */
    public static final String MANAGER_PAGE = "admin";


    /**
     * chiave per il flag di utilizzo funzionalità Gestione Mailing Integrata
     */
    public static final Boolean USA_GESTIONE_MAILING = true;

    private static final String KEY_SPLASHIMAGE = "splashimage";
    private static final String KEY_MENUBAR_ICON = "menubaricon";

    /**
     * Name of the local folder for images.<br>
     */
    public static final String IMG_FOLDER_NAME = "WEB-INF/data/img/";

    /**
     * Keys for session attributes.<br>
     */
    public static final String KEY_MOSTRA_PREN_SCADUTE = "mostraPrenScadute";
    public static final String KEY_MOSTRA_PREN_RITARDO_PAGAMENTO_1 = "mostraOpzioniRitardoPagamento1";
    public static final String KEY_MOSTRA_PREN_PAGAMENTO_SCADUTO = "mostraPrenPagamentoScaduto";

    public static final String KEY_MOSTRA_PREN_NON_CONFERMATE = "mostraPrenNonConfermate";
    public static final String KEY_MOSTRA_PREN_PAGAMENTO_CONFERMATO = "mostraPrenPagamentoConfermato";
    public static final String KEY_MOSTRA_PREN_PAGAMENTO_RICEVUTO = "mostraPrenPagamentoRicevuto";
    public static final String KEY_MOSTRA_PREN_PAGAMENTO_NON_CONFERMATO = "mostraPrenPagamentoDaConfermare";
    public static final String KEY_MOSTRA_PREN_CONGELATE = "mostraPrenCongelate";

    // company codes per identificare le personalizzazioni
    public static final String ASTERIA_COMPANY_CODE="asteria";
    public static final String EXTRATEATRO_COMPANY_CODE="extrateatro";


}// end of static abstract class
