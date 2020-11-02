package it.algos.evento.servlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.*;
import it.algos.evento.ui.admin.AdminUI;
import it.algos.webbase.domain.ruolo.Ruolo;
import it.algos.webbase.domain.utente.Utente;
import it.algos.webbase.domain.utenteruolo.UtenteRuolo;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.lib.LibCrypto;
import it.algos.webbase.web.login.UserIF;
import it.algos.webbase.web.servlet.AlgosServlet;

import javax.servlet.annotation.WebServlet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet 3.0 introduces a @WebServlet annotation which can be used to replace the traditional web.xml.
 * <p>
 * The straightforward approach to create a Vaadin application using servlet 3.0 annotations,
 * is to simply move whatever is in web.xml to a custom servlet class (extends VaadinServlet)
 * and annotate it using @WebServlet and add @WebInitParams as needed.
 * <p><
 * Vaadin 7.1 introduces two features which makes this a lot easier, @VaadinServletConfiguration
 * and automatic UI finding.
 * VaadinServletConfiguration is a type safe, Vaadin version of @WebInitParam
 * which provides you with the option to select UI by referring the UI class
 * directly toggle productionMode using a boolean and more
 */
@WebServlet(value = "/admin/*", asyncSupported = true, displayName = "eVento - admin")
//widgetset = "com.sibvisions.vaadin.Widgetset" rende disponibile il widgetset a tutte le UI del servlet.
@VaadinServletConfiguration(productionMode = false, ui = AdminUI.class, widgetset = "com.sibvisions.vaadin.Widgetset")
public class AdminServlet extends AlgosServlet {

    private final static Logger logger = Logger.getLogger(AdminServlet.class.getName());

    @Override
    public void sessionInit(SessionInitEvent event) throws ServiceException {
        super.sessionInit(event);

        // make sure we have at least one valid admin
        ensureAdmin();

        // attempt to login from the cookies
        if(CompanySessionLib.getAdminLogin().loginFromCookies()){

            // controlla se l'utente ha ruolo di admin
            Ruolo adminRole = Ruolo.read("admin");
            UserIF user= CompanySessionLib.getAdminLogin().getUser();
            if(!user.isAdmin()) {
                CompanySessionLib.setLogin(null);
                String err="L'utente "+user+" (loggato dai cookies) non è abilitato all'accesso come admin. Login fallito.";
                logger.log(Level.SEVERE, err);
            }


        }

    }// end of method


    /**
     * Make sure that a "admin" user and a "admin" role exist, and
     * that a corresponding UserRole exists. Otherwise, create them.
     */
    private void ensureAdmin(){

        // make sure that a admin role exists
        Ruolo ruolo = Ruolo.read("admin");
        if (ruolo==null){
            ruolo = new Ruolo();
            ruolo.setNome("admin");
            ruolo.save();
        }

        // make sure that a user named "admin" exists
        // if not create it now with a default password
        Utente user=Utente.read("admin");
        if (user==null){
            user = new Utente();
            user.setNickname("admin");
            user.setPassword(LibCrypto.encrypt("evento"));
            user.setEnabled(true);
            user.save();
        }

        // make sure that the admin user has the admin role
        if(!user.hasRole(Ruolo.read("admin"))){
            UtenteRuolo ur = new UtenteRuolo();
            ur.setUtente(user);
            ur.setRuolo(ruolo);
            ur.save();
        }

    }

}// end of class
