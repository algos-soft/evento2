package it.algos.evento.ui.admin;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import it.algos.evento.EventoApp;
import it.algos.evento.config.AccessControlConfigComponent;
import it.algos.evento.config.GeneralDaemonConfigComponent;
import it.algos.evento.config.SMTPServerConfigComponent;
import it.algos.evento.entities.company.CompanyModule;
import it.algos.evento.ui.DevPassDialog;
import it.algos.evento.ui.NavigatorPlaceholder;
import it.algos.webbase.domain.ruolo.RuoloModulo;
import it.algos.webbase.domain.utente.UtenteModulo;
import it.algos.webbase.domain.utenteruolo.UtenteRuoloModulo;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.login.Login;
import it.algos.webbase.web.navigator.AlgosNavigator;
import it.algos.webbase.web.navigator.MenuCommand;

import java.util.Collection;

/**
 * Home page dell'Admin.
 */
public class AdminHome extends VerticalLayout {

    private MenuBar.MenuItem loginItem; // il menuItem di login

    public AdminHome() {

        // regolazioni di questo layout
        setMargin(true);
        setSpacing(false);
        setSizeFull();

        // crea la MenuBar principale
        MenuBar mainBar = createMainMenuBar();

        // crea la MenuBar di Login
        MenuBar loginBar = createLoginMenuBar();

        // aggiunge la menubar principale e la menubar login
        HorizontalLayout menuLayout = new HorizontalLayout();
        //menuLayout.setHeight("32px");
        menuLayout.setWidth("100%");
        menuLayout.addComponent(mainBar);
        mainBar.setHeight("100%");
        menuLayout.setExpandRatio(mainBar, 1.0f);
        menuLayout.addComponent(loginBar);
        loginBar.setHeight("100%");
        addComponent(menuLayout);

        // crea e aggiunge uno spaziatore verticale
        HorizontalLayout spacer = new HorizontalLayout();
        spacer.setMargin(false);
        spacer.setSpacing(false);
        spacer.setHeight("5px");
        addComponent(spacer);

        // crea e aggiunge il placeholder dove il Navigator inserirà le varie pagine
        // a seconda delle selezioni di menu
        NavigatorPlaceholder placeholder = new NavigatorPlaceholder(null);
        placeholder.setSizeFull();
//        if (DEBUG_GUI) {
//            placeholder.addStyleName("yellowBg");
//        }
        addComponent(placeholder);
        setExpandRatio(placeholder, 1.0f);

        // crea un Navigator e lo configura in base ai contenuti della MenuBar
        AlgosNavigator nav = new AlgosNavigator(UI.getCurrent(), placeholder);
        nav.configureFromMenubar(mainBar);
        nav.navigateTo(AdminSplash.class.getName());

        // set browser window title
        Page.getCurrent().setTitle(EventoApp.APP_NAME+" - admin");

    }



    /**
     * Crea la MenuBar principale.
     */
    private MenuBar createMainMenuBar() {

        //splashScreen = new AdminSplash();

        MenuBar.MenuItem item;
        MenuBar menubar = new MenuBar();

        // Menu Home
//        menubar.addItem("", LibResource.getImgResource(EventoApp.IMG_FOLDER_NAME, "manager_menubar_icon.png"), new MenuCommand(menubar, "splash", AdminSplash.class));
        menubar.addItem("", FontAwesome.HOME, new MenuCommand(menubar, AdminSplash.class));

        // Menu principali
        menubar.addItem("Aziende", null, new MenuCommand(menubar, CompanyModule.class));

        // Menu Utenti e ruoli
        item = menubar.addItem("Utenti e ruoli", null, null);
        item.addItem("Utenti", null, new MenuCommand(menubar, UtenteModulo.class));
        item.addItem("Ruoli", null, new MenuCommand(menubar, RuoloModulo.class));
        item.addItem("Utenti-Ruoli", null, new MenuCommand(menubar, UtenteRuoloModulo.class));

        // Menu Configurazione
        item = menubar.addItem("Configurazione", null, null);

        // submenu controllo accessi
        item.addItem("Controllo accessi", null, new MenuCommand(menubar,  AccessControlConfigComponent.class));

        // submenu smtp server
        item.addItem("SMTP Server", null, new MenuCommand(menubar, SMTPServerConfigComponent.class));

        // submenu daemon controlli automatici
        item.addItem("Daemon controlli automatici", null, new MenuCommand(menubar, GeneralDaemonConfigComponent.class));

        // Modo Programmatore
        if (LibSession.isDeveloper()) {

            DevPassDialog dialog = new DevPassDialog(new ConfirmDialog.Listener() {
                @Override
                public void onClose(ConfirmDialog dialog, boolean confirmed) {
                    if(confirmed) {
                        addMenuProgrammatore(menubar);
                        menubar.addStyleName("redBg");
                    }else{
                        LibSession.setDeveloper(false);
                    }
                }
            });

            dialog.show();

        }

        return menubar;

    }


    /**
     * Crea la menubar di Login
     */
    private MenuBar createLoginMenuBar() {
        MenuBar menubar = new MenuBar();
        Resource icon = FontAwesome.USER;
        String username = CompanySessionLib.getAdminLogin().getUser().getNickname();
        loginItem = menubar.addItem(username, icon, null);
        loginItem.addItem("Logout", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {

                // annulla l'oggetto Login nella sessione
                LibSession.setAttribute(Login.LOGIN_KEY_IN_SESSION, null);

                // Rimetti il login screen in tutte le UI della sessione
                // (serve se la sessione è aperta in diversi tab o finestre del browser)
                Collection<UI> uis = VaadinSession.getCurrent().getUIs();
                for(UI ui:uis){
                    ui.setContent(new AdminLogin());
                }

            }
        });
        return menubar;
    }


    /**
     * Crea il menu Programmatore per la menubar Manager
     */
    private void addMenuProgrammatore(MenuBar menubar) {
        MenuBar.MenuItem item;
        item = menubar.addItem("Programmatore", null, null);


        item.addItem("empty item", null, new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                // do something here
            }
        });


    }// end of method


}
