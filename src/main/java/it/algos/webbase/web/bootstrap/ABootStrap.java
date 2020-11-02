package it.algos.webbase.web.bootstrap;

import it.algos.webbase.domain.ruolo.TipoRuolo;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.lang.reflect.Method;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Executed on container startup
 * Setup non-UI logic here
 * <p>
 * Classe eseguita solo quando l'applicazione viene caricata/parte nel server (Tomcat od altri) <br>
 * Eseguita quindi ad ogni avvio/riavvio del server e NON ad ogni sessione <br>
 * È OBBLIGATORIO aggiungere la sottoclasse concreta nei listeners del file web.WEB-INF.web.xml <br>
 */
public abstract class ABootStrap implements ServletContextListener {

    private final static Logger logger = Logger.getLogger(ABootStrap.class.getName());
    private static ServletContext context;

    public static ServletContext getServletContext() {
        return context;
    }// end of method

    /**
     * Executed on container startup
     * Setup non-UI logic here
     * <p>
     * Viene normalmente sovrascritta dalla sottoclasse:
     * - per registrare nella xxxApp, il servlet context non appena è disponibile
     * - per regolare eventualmente alcuni flag dell'applicazione <br>
     * - per lanciare eventualmente gli schedulers in background <br>
     * - per regolare eventualmente una versione demo <br>
     * - per controllare eventualmente l'esistenza di utenti abilitati all'accesso <br>
     * Deve (DEVE) richiamare anche il metodo della superclasse (questo)
     * prima (PRIMA) delle regolazioni specifiche della sottoclasse <br>
     */
    public void contextInitialized(ServletContextEvent contextEvent) {
        SingletonBootStrap.getIstanza();
        context = contextEvent.getServletContext();
    }// end of method




    /**
     * Executed on container shutdown
     * <p>
     * Clean stuff here <br>
     * Può essere sovrascritta dalla sottoclasse <br>
     * Deve (DEVE) richiamare anche il metodo della superclasse (questo) <br>
     */
    public void contextDestroyed(ServletContextEvent contextEvent) {
        SingletonBootStrap.executeStop();
    }// end of method

}// end of abstract class
