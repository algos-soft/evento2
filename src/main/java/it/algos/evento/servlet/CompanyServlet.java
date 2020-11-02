package it.algos.evento.servlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.*;
import it.algos.evento.ui.company.CompanyUI;
import it.algos.webbase.domain.utente.Utente;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.login.Login;
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
@WebServlet(value = {"/*"}, asyncSupported = true, displayName = "eVento")
@VaadinServletConfiguration(productionMode = false, ui = CompanyUI.class, widgetset = "com.sibvisions.vaadin.Widgetset")
//@Widgetset("com.sibvisions.vaadin.Widgetset")

public class CompanyServlet extends AlgosServlet {

    private final static Logger logger = Logger.getLogger(CompanyServlet.class.getName());

    @Override
    public void sessionInit(SessionInitEvent event) throws ServiceException {
        super.sessionInit(event);

        int hb = VaadinService.getCurrent().getDeploymentConfiguration().getHeartbeatInterval();
        logger.log(Level.INFO, "session started: "+event.getSession()+" heartbeat: "+hb);


        // attempt to login from the cookies
        if(Login.getLogin().loginFromCookies()){

            // registra la company nella sessione in base all'utente loggato
            UserIF user = Login.getLogin().getUser();
            if(!CompanySessionLib.registerCompanyByUser(user)) {
                CompanySessionLib.setLogin(null);
                String err="L'utente " + user + " (loggato dai cookies) è registrato ma non c'è l'azienda corrispondente. Login fallito.";
                logger.log(Level.SEVERE, err);
            }

        }


    }// end of method



    /**
     * Called when a Vaadin service session is no longer used.
     *
     * @param event the event with details about the destroyed session
     */
    @Override
    public void sessionDestroy(SessionDestroyEvent event) {
        super.sessionDestroy(event);

        logger.log(Level.INFO, "session terminated: "+event.getSession());

    }// end of method


}// end of class
