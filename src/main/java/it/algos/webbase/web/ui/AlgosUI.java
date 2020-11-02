package it.algos.webbase.web.ui;

import com.vaadin.navigator.View;
import com.vaadin.server.*;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import it.algos.webbase.domain.log.LogMod;
import it.algos.webbase.domain.pref.PrefMod;
import it.algos.webbase.domain.ruolo.RuoloModulo;
import it.algos.webbase.domain.utente.UtenteModulo;
import it.algos.webbase.domain.utenteruolo.UtenteRuoloModulo;
import it.algos.webbase.domain.vers.VersMod;
import it.algos.webbase.web.AlgosApp;
import it.algos.webbase.web.lib.Cost;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.login.*;
import it.algos.webbase.web.menu.AMenuBar;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.navigator.AlgosNavigator;
import it.algos.webbase.web.navigator.MenuCommand;
import it.algos.webbase.web.navigator.NavPlaceholder;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The topmost component in any component hierarchy. There is one UI for every Vaadin instance in a browser window. A UI
 * may either represent an entire browser window (or tab) or some part of a html page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is the server side entry point for various client side features that are not represented as components added
 * to a layout, e.g notifications, sub windows, and executing javascript in the browser.
 * </p>
 * <p>
 * When a new UI instance is needed, typically because the user opens a URL in a browser window which points to e.g.
 * {@link VaadinServlet}, all {@link UIProvider}s registered to the current {@link VaadinSession} are queried for the UI
 * class that should be used. The selection is by default based on the <code>UI</code> init parameter from web.xml.
 * </p>
 * <p>
 * After a UI has been created by the application, it is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden by the developer to add components to the user interface and initialize non-component
 * functionality. The component hierarchy must be initialized by passing a {@link Component} with the main layout or
 * other content of the view to {@link #setContent(Component)} or to the constructor of the UI.
 * </p>
 *
 * @see #init(VaadinRequest)
 * @see UIProvider
 * @since 7.0
 */
@SuppressWarnings("serial")
public abstract class AlgosUI extends UI implements LoginListener, LogoutListener {

    public static boolean DEBUG_GUI = false;

    protected VerticalLayout mainLayout;        // main
    protected AMenuBar topLayout;               // top
    protected NavPlaceholder bodyLayout;       // body
    protected HorizontalLayout footerLayout;    // footer

    protected String menuAddressModuloPartenza = "";//@todo la stringa vuota serve per non far andare in errore il navigateTo
    protected ArrayList<ModulePop> moduli;
    protected LinkedHashMap<String, MenuBar.MenuItem> mappaItem;


    /**
     * Initializes this UI. This method is intended to be overridden by subclasses to build the view and configure
     * non-component functionality. Performing the initialization in a constructor is not suggested as the state of the
     * UI is not properly set up when the constructor is invoked.
     * <p>
     * The {@link VaadinRequest} can be used to get information about the request that caused this UI to be created.
     * </p>
     * Se viene sovrascritto dalla sottoclasse, deve (DEVE) richiamare anche il metodo della superclasse
     * di norma DOPO aver effettuato alcune regolazioni <br>
     * Nella sottoclasse specifica viene eventualmente regolato il nome del modulo di partenza <br>
     *
     * @param request the Vaadin request that caused this UI to be created
     */
    @Override
    protected void init(VaadinRequest request) {

        // Legge eventuali parametri passati nella request
        this.checkParams(request);

        // Legge i cookies dalla request
        this.checkCookies(request);

        // Controlla il login della security
        if (AlgosApp.USE_SECURITY) {
            this.checkSecurity(request);
        }// fine del blocco if

        // Crea l'interfaccia utente (User Interface) iniziale dell'applicazione
        this.startUI();
    }// end of method

    /**
     * Legge eventuali parametri passati nella request
     * <p>
     */
    public void checkParams(VaadinRequest request) {
        // legge il parametro "developer" (se esiste) e regola la variabile nella sessione
        LibSession.checkDeveloper(request);

        // legge il parametro "debug" (se esiste) e regola la variabile nella sessione
        LibSession.checkDebug(request);
    }// end of method


    /**
     * Legge i cookies dalla request
     * <p>
     */
    protected void checkCookies(VaadinRequest request) {
        // Fetch all cookies
        Cookie[] cookies = request.getCookies();

        // Store the current cookies in the service session
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(Login.COOKIENAME_LOGIN)) {
                    LibSession.setAttribute(Login.COOKIENAME_LOGIN, cookie.getValue());
                }// fine del blocco if
                if (cookie.getName().equals(Login.COOKIENAME_PASSWORD)) {
                    LibSession.setAttribute(Login.COOKIENAME_PASSWORD, cookie.getValue());
                }// fine del blocco if
                if (cookie.getName().equals(Cost.COOKIE_LOGIN_ROLE)) {
                    LibSession.setAttribute(Cost.COOKIE_LOGIN_ROLE, cookie.getValue());
                }// fine del blocco if
            } // fine del ciclo for-each
        }// fine del blocco if


//         Create a new cookie
//        Cookie myCookie = new Cookie(Cost.COOKIE_LOGIN_NICK, "cookie-value");
//        myCookie.setMaxAge(300);
//        myCookie.setPath(VaadinService.getCurrentRequest().getContextPath());
//        VaadinService.getCurrentResponse().addCookie(myCookie);

//        Cookies.setCookie("postCommit", "Some-other-value");

    }// end of method

    /**
     * Controlla il login della security
     * <p>
     * Creazione del wrapper di informazioni mantenuto nella sessione <br>
     */
    protected void checkSecurity(VaadinRequest request) {
        LibSession.setAttribute(Cost.LOGIN_INFO, new Login());
    }// end of method


    /**
     * Crea l'interfaccia utente (User Interface) iniziale dell'applicazione
     * Crea i menu specifici
     * Layout standard composto da:
     * Top      - una barra composita di menu e login
     * Body     - un placeholder per il portale della tavola/modulo
     * Footer   - un striscia per eventuali informazioni (Algo, copyright, ecc)
     * <p>
     * Può essere sovrascritto per gestire la UI in maniera completamente diversa
     */
    protected void startUI() {

        // crea la UI di base, un VerticalLayout
        mainLayout = this.creaMain();

        // crea ed aggiunge la menubar principale e la menubar login tramite una classe dedicata
        topLayout = this.creaTop();
        mainLayout.addComponent(topLayout);

        // crea e aggiunge uno spaziatore verticale
        this.addSpacer();

        // crea e aggiunge il placeholder dove il Navigator inserirà le varie pagine a seconda delle selezioni di menu
        bodyLayout = this.creaBody();
        mainLayout.addComponent(bodyLayout);
        mainLayout.setExpandRatio(bodyLayout, 1.0f);

        // crea ed aggiunge il footer
        footerLayout = this.creaFooter();
        mainLayout.addComponent(footerLayout);

        // Crea i menu per la gestione dei moduli (standard e specifici)
        this.addAllModuli();

        // crea un Navigator e lo configura in base ai contenuti della MenuBar
        this.creaPartenza();

        // set browser window title
        Page.getCurrent().setTitle("Vaadin");

        // assegna la UI
        setContent(mainLayout);

        // Sincronizzazione dei listener per il funzionamento del Login
        regolaListenerLog();
    }// end of method

    /**
     * Crea l'interfaccia utente (User Interface) iniziale dell'applicazione
     * Crea la UI di base, un VerticalLayout
     * Le applicazioni specifiche, possono sovrascrivere questo metodo nella sottoclasse
     *
     * @return layout -  un VerticalLayout
     */
    protected VerticalLayout creaMain() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(false);
        layout.setSizeFull();

        if (DEBUG_GUI) {
            layout.addStyleName("blueBg");
        }// fine del blocco if

        return layout;
    }// end of method

    /**
     * Crea l'interfaccia utente (User Interface) iniziale dell'applicazione
     * Top  - una barra composita di menu e login
     * Le applicazioni specifiche, possono sovrascrivere questo metodo nella sottoclasse
     *
     * @return layout - normalmente un AMenuBar
     */
    protected AMenuBar creaTop() {
        AMenuBar menubar = new AMenuBar(AlgosApp.USE_SECURITY);

        if (DEBUG_GUI) {
            menubar.addStyleName("yellowBg");
        }// fine del blocco if

        return menubar;
    }// end of method

    /**
     * Crea l'interfaccia utente (User Interface) iniziale dell'applicazione
     * Body - un placeholder per il portale della tavola/modulo
     * Le applicazioni specifiche, possono sovrascrivere questo metodo nella sottoclasse
     *
     * @return layout - normalmente un NavPlaceholder
     */
    protected NavPlaceholder creaBody() {
        NavPlaceholder placeholder = new NavPlaceholder(null);
        placeholder.setSizeFull();

        if (DEBUG_GUI) {
            placeholder.addStyleName("greenBg");
        }// fine del blocco if

        return placeholder;
    }// end of method

    /**
     * Crea l'interfaccia utente (User Interface) iniziale dell'applicazione
     * Footer - un striscia per eventuali informazioni (Algo, copyright, ecc)
     * Le applicazioni specifiche, possono sovrascrivere questo metodo nella sottoclasse
     *
     * @return layout - normalmente un HorizontalLayout
     */
    protected HorizontalLayout creaFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(false, false, false, false));
        footer.setSpacing(true);
        footer.setHeight("30px");

        if (DEBUG_GUI) {
            footer.addStyleName("redBg");
        }// fine del blocco if

        footer.addComponent(new Label("Algos s.r.l."));

        return footer;
    }// end of method

    /**
     * Sincronizzazione dei listener per il funzionamento del Login
     * <p>
     * Dopo aver creato questa classe, registra nella Login (singleton nel servlet/sessione) l'istanza di questa classe per l'evento LoginListener
     * Dopo aver creato la LoginBar, registra nella Login (singleton nel servlet/sessione) l'istanza di LoginBar per l'evento LoginListener
     * Dopo aver creato la LoginBar, registra nella LoginBar l'istanza di login (singleton nel servlet/sessione) per l'evento LogformListener
     * Dopo aver creato la LoginBar, registra nella LoginBar l'istanza di AlgoUi (questa classe) per l'evento LogoutListener
     */
    protected void regolaListenerLog() {
        Login login = Login.getLogin();
        LoginButton loginBar;

        if (login != null) {
            loginBar = topLayout.getLoginBar();

            if (loginBar != null) {
                login.addLoginListener(this);
//                login.addLoginListener(loginBar);

//                loginBar.addLogformListener(login);
                login.addLogoutListener(this);
            }// end of if cycle
        }// end of if cycle
    }// end of method


    /**
     * Legge il parametro "azienda" e regola la variabile statica
     */
    protected void leggeAzienda(VaadinRequest request) {
        String company = "";
        Map<String, String[]> mappa = null;

        // legge il parametro "azienda" e regola la variabile statica
        if (AlgosApp.USE_COMPANY) {
            mappa = request.getParameterMap();

            // ci sono 17 valori di sistema
            if (mappa.size() == 18) {
                company = "demo";
            }// fine del blocco if
        }// fine del blocco if

        AlgosApp.COMPANY_CODE = company;
    }// end of method


    /**
     * Aggiunge i moduli (standard e specifici)
     * <p>
     * Mantiene un array di tutti i moduli
     * Alcuni moduli sono specifici di un collegamento come programmatore
     * Alcuni moduli sono già definiti per tutte le applicazioni (LogMod, VersMod, PrefMod)
     * Crea l'array e vi aggiunge i moduli interessati
     * Spazzola l'array dei moduli per creare/recuperare il menuItem ed aggiungerlo alla UI
     */
    private void addAllModuli() {
        moduli = new ArrayList<ModulePop>();
        mappaItem = new LinkedHashMap<String, MenuBar.MenuItem>();

        this.addModuliStandard();
        this.addModuli();
//        this.creaMenu();
    }// end of method


    /**
     * Aggiunge i moduli standard definiti nel progetto webbase
     * Vengono usati o meno secondo i flags (sovrascrivibili): AlgosApp.USE_LOG, AlgosApp.USE_VERS, AlgosApp.USE_PREF
     */
    private void addModuliStandard() {
        if (LibSession.isDeveloper()) {
            this.addModulo(new RuoloModulo());
            this.addModulo(new UtenteModulo());
            this.addModulo(new UtenteRuoloModulo());
        }// fine del blocco if

        if (LibSession.isDeveloper() || AlgosApp.USE_VERS) {
            this.addModulo(new VersMod());
        }// fine del blocco if

        if (AlgosApp.USE_LOG) {
            this.addModulo(new LogMod());
        }// fine del blocco if
        if (AlgosApp.USE_PREF) {
            this.addModulo(new PrefMod());
        }// fine del blocco if
    }// end of method


    /**
     * Aggiunge i moduli specifici
     * <p>
     * Deve (DEVE) essere sovrascritto dalla sottoclasse per aggiungere i moduli alla menubar dell'applicazione <br>
     * Chiama il metodo  addModulo(...) della superclasse per ogni modulo previsto nella barra menu
     */
    protected void addModuli() {
    }// end of method


    /**
     * Adds a cached View to the UI
     * <p>
     * the view will be instantiated only once
     *
     * @param viewClass the view class to instantiate
     */
    protected void addModulo(Class<? extends View> viewClass) {
        String label = viewClass.getSimpleName();
        addView(viewClass, true, label, null);
    }// end of method


    /**
     * Adds a View to the UI
     * <p>
     * the view will be instantiated only once and then cached.
     *
     * @param viewClass the view class to instantiate
     * @param label     the text for the menu item
     * @param icon      the icon for the menu item
     */
    protected void addView(Class<? extends View> viewClass, String label, Resource icon) {
        addView(viewClass, true, label, icon);
    }

    /**
     * Adds a View to the UI
     * <p>
     * Will create a lazy (class-based) view provider
     * The view will be instantiated by the view provider from the provided class
     * The viewCached parameter controls if the view will be instantiated only once
     * or each time is requested by the Navigator.
     * <p>
     * Invocato dalla sottoclasse
     *
     * @param viewClass  the view class to instantiate
     * @param viewCached true to instantiated only once, false to instantiate each time
     * @param label      the text for the menu item
     * @param icon       the icon for the menu item
     */
    protected void addView(Class<? extends View> viewClass, boolean viewCached, String label, Resource icon) {

        String keyModulo = "";
        MenuBar.MenuItem menuItem = createMenuItem(viewClass, label, viewCached, icon);

        if (menuItem != null) {
            keyModulo = viewClass.getSimpleName();
            mappaItem.put(keyModulo, menuItem);
        }// end of if cycle

    }// end of method


    /**
     * Aggiunge un modulo alla UI
     * <p>
     * Il modulo può essere aggiunto come istanza già creata
     * Tipicamente un ModulePop
     * <p>
     * Invocato dalla sottoclasse
     *
     * @param modulo da visualizzare nel placeholder alla pressione del bottone di menu
     */
    protected void addModulo(ModulePop modulo) {
        String menuLabel;
        Resource menuIcon = null;
        MenuBar.MenuItem menuItem = null;

        if (modulo != null) {
            menuLabel = modulo.getMenuLabel();
            menuIcon = modulo.getMenuIcon();
            menuItem = addModulo(modulo, menuLabel, menuIcon);

            if (menuItem != null) {
                modulo.addSottoMenu(menuItem);
            }// end of if cycle
        }// end of if cycle

    }// end of method

    /**
     * Aggiunge un modulo alla UI
     * <p>
     * Il modulo può essere aggiunto come istanza già creata
     * Qualunque oggetto grafico che implementi l'interfaccia View
     * <p>
     * Invocato dalla sottoclasse
     *
     * @param vista     da visualizzare nel placeholder alla pressione del bottone di menu
     * @param menuLabel etichetta visibile nella menu bar
     * @param menuIcon  icona del menu
     */
    protected MenuBar.MenuItem addModulo(View vista, String menuLabel, Resource menuIcon) {
        String keyModulo = "";
        MenuBar.MenuItem menuItem = createMenuItem(vista, menuLabel, menuIcon);

        if (menuItem != null) {
            keyModulo = vista.getClass().getSimpleName();
            mappaItem.put(keyModulo, menuItem);
        }// end of if cycle

        return menuItem;
    }// end of method


//    /**
//     * Create the MenuBar Item for this module
//     * <p>
//     * Invocato dal metodo AlgosUI.creaMenu()
//     * PUO essere sovrascritto dalla sottoclasse
//     *
//     * @param vista da visualizzare nel placeholder alla pressione del bottone di menu
//     * @return menuItem appena creato
//     */
//    private MenuBar.MenuItem createMenuItem(View vista) {
//        return createMenuItem(vista, null);
//    }// end of method

    /**
     * Create the MenuBar Item for this module
     * <p>
     * Invocato dal metodo AlgosUI.creaMenu()
     * PUO essere sovrascritto dalla sottoclasse
     *
     * @param vista    da visualizzare nel placeholder alla pressione del bottone di menu
     * @param menuIcon del menu
     * @return menuItem appena creato
     */
    private MenuBar.MenuItem createMenuItem(View vista, String menuAddress, Resource menuIcon) {
        MenuBar.MenuItem menuItem;
        MenuBar menuBar = topLayout.getMenuBar();

        MenuCommand comando = new MenuCommand(menuBar, vista);
        menuItem = menuBar.addItem(menuAddress, menuIcon, comando);
        menuItem.setStyleName(AMenuBar.MENU_DISABILITATO);

        return menuItem;
    }// end of method


    /**
     * Create the MenuBar Item for this module
     * <p>
     * Invocato dal metodo AlgosUI.creaMenu()
     * PUO essere sovrascritto dalla sottoclasse
     *
     * @param viewClass da visualizzare nel placeholder alla pressione del bottone di menu
     * @param icon      del menu
     * @return menuItem appena creato
     */
    private MenuBar.MenuItem createMenuItem(Class<? extends View> viewClass, String label, boolean cached, Resource icon) {
        MenuBar.MenuItem menuItem;
        MenuBar menuBar = topLayout.getMenuBar();

        MenuCommand comando = new MenuCommand(menuBar, viewClass, cached);
        menuItem = menuBar.addItem(label, icon, comando);
        menuItem.setStyleName(AMenuBar.MENU_DISABILITATO);

        return menuItem;
    }// end of method


    /**
     * Crea i menu per la gestione dei moduli (standard e specifici)
     * <p>
     * Spazzola l'array dei moduli per creare/recuperare il menuItem ed aggiungerlo alla UI
     * Aggiunge alla barra di menu principale il comando per lanciare il modulo indicato
     * Aggiunge il singolo menu (item) alla barra principale di menu
     * Rimanda al metodo omonimo della classe AMenuBar (dedicata per il menu algosMenuBar e loginMenuBar)
     */
    private void creaMenu() {
        MenuBar.MenuItem item;
        MenuCommand comando;
        View vista;
        ModulePop modulo;

        if (mappaItem != null && mappaItem.size() > 0) {
            for (Map.Entry<String, MenuBar.MenuItem> mappa : mappaItem.entrySet()) {
                item = mappa.getValue();
                comando = (MenuCommand) item.getCommand();
                vista = comando.getView();
                modulo = (ModulePop) vista;
                modulo.createMenuItem(topLayout.getMenuBar(), bodyLayout);
            }// end of for cycle
        }// end of if cycle

//        if (moduli != null && moduli.size() > 0) {
//            for (ModulePop modulo : moduli) {
//                modulo.createMenuItem(topLayout.getMenuBar(), bodyLayout);
//            }// end of for cycle
//        }// end of if cycle
    }// end of method


    /**
     * Crea e aggiunge uno spaziatore verticale di altezza fissa
     */
    private void addSpacer() {
        HorizontalLayout spacer = new HorizontalLayout();
        spacer.setMargin(false);
        spacer.setSpacing(false);
        spacer.setHeight("5px");

        if (DEBUG_GUI) {
            spacer.addStyleName("yellowBg");
        }// fine del blocco if

        mainLayout.addComponent(spacer);
    }// end of method

    /**
     * crea un Navigator e lo configura in base ai contenuti della MenuBar
     */
    private void creaPartenza() {
        AlgosNavigator nav = new AlgosNavigator(getUI(), bodyLayout);
        nav.configureFromMenubar(topLayout.getMenuBar());
        nav.navigateTo(menuAddressModuloPartenza);
    }// end of method

    /**
     * Logout
     * Annulla l'oggetto Login nella sessione
     */
    private void logout() {
        // Show the splash page
        getUI().getNavigator().navigateTo("splash");

        // Reload the current page and get a new session
        getPage().reload();
    }// end of method


    //    /**
//     * Invoked after a successful login happened using the Login form.
//     * <p>
//     * La classe Login gestisce il form ed alla chiusura controlla la validità del nuovo utente
//     * Lancia il fire di questo evento, se l'utente è valido.
//     */
    @Override
    public void onUserLogout(LogoutEvent e) {
        logout();
    }// end of method

    public ArrayList<ModulePop> getModuli() {
        return moduli;
    }// end of getter method

    public void setModuli(ArrayList<ModulePop> moduli) {
        this.moduli = moduli;
    }//end of setter method


}// end of class
