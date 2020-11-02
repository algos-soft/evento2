package it.algos.webbase.bootstrap;

import it.algos.webbase.domain.pref.Pref;
import it.algos.webbase.multiazienda.CompanyEntity;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.ArrayList;

/**
 * Created by gac on 19 lug 2016.
 * <p>
 * Executed on container startup
 * Setup non-UI logic here
 * <p>
 * Classe eseguita solo quando l'applicazione viene caricata/parte nel server (Tomcat od altri) <br>
 * Eseguita quindi ad ogni avvio/riavvio del server e NON ad ogni sessione <br>
 * È OBBLIGATORIO aggiungere questa classe nei listeners del file web.WEB-INF.web.xml
 */
public class PrefBootStrap implements ServletContextListener {

    /**
     * Executed on container startup
     * Setup non-UI logic here
     * <p>
     * This method is called prior to the servlet context being
     * initialized (when the Web application is deployed).
     * You can initialize servlet context related data here.
     */
    @Override
    public void contextInitialized(ServletContextEvent contextEvent) {

        // Cancellazione iniziale di tutte le (eventuali) company
        this.eliminaRecords();

        // Creazione iniziale di alcune company di prova
        this.creaRecords();
    }// end of method


    /**
     * Cancellazione iniziale di tutti i records
     * Usa un DB di prova NON in linea (webbase)
     */
    private void eliminaRecords() {
        for (Pref pref : Pref.getListAll()) {
            pref.delete();
        }// end of for cycle
    }// end of method

    /**
     * Creazione iniziale di alcuni records di prova
     * Li crea SOLO se non esistono già
     * Vengono creati in un DB di prova NON in linea (webbase)
     * Servono per visualizzare una lista e per la classe di test
     */
    private void creaRecords() {
        Pref.crea("alfa", "pippo");
        Pref.crea("beta", "pluto");
        Pref.crea("ret", "rot");
    }// end of method

    /**
     * This method is invoked when the Servlet Context
     * (the Web application) is undeployed or
     * WebLogic Server shuts down.
     */
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }// end of method

}// end of bootstrap class
