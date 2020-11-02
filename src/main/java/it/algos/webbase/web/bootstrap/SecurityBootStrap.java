package it.algos.webbase.web.bootstrap;

import it.algos.webbase.web.lib.LibSecurity;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Security dell'applicazione
 * Executed on container startup
 * Setup non-UI logic here
 * <p>
 * Normalmente viene sovrascritta nell'applicazione specifica,
 * in quanto il nome è presente nei listeners del file web.WEB-INF.web.xml
 * Se non utilizzata, può essere cancellata:
 * 1) - deve essere cancellata come classe
 * 2) - deve essere cancellata come listener del file web.WEB-INF.web.xml
 */
public abstract class SecurityBootStrap implements ServletContextListener {


    /**
     * Executed on container startup
     * Setup non-UI logic here
     * <p>
     * This method is called prior to the servlet context being
     * initialized (when the Web application is deployed).
     * You can initialize servlet context related data here.
     * <p>
     * Viene sovrascritta dalla sottoclasse:
     * - per controllare i ruoli specifici esistenti e creare quelli eventualmente mancanti <br>
     * - per controllare gli utenti abilitati esistenti e creare quelli eventualmente mancanti <br>
     * Deve (DEVE) richiamare anche il metodo della superclasse (questo)
     * prima (PRIMA) di eseguire le regolazioni specifiche <br>
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        this.creaRuoli();
    }// end of method

    /**
     * This method is invoked when the Servlet Context
     * (the Web application) is undeployed or
     * WebLogic Server shuts down.
     */
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }// end of method

    /**
     * Crea una serie di ruoli generali
     * <p>
     * Alcuni generali controlla se esistono (dovrebbero esserci) e li crea solo se mancano (la prima volta)
     */
    private void creaRuoli() {
        LibSecurity.checkRuoliStandard();
    }// end of method

    /**
     * Crea un singolo ruolo specifico di un'applicazione
     * <p>
     * Controllo esistenza di un ruolo <br>
     * Se manca, lo crea <br>
     *
     * @param nomeRuolo da controllare/creare
     */
    protected void creaRuolo(String nomeRuolo) {
        LibSecurity.creaRuolo(nomeRuolo);
    }// end of method

    /**
     * Crea un utente con ruolo connesso
     *
     * @param nickName  dell'utente
     * @param password  dell'utente
     * @param nomeRuolo da assegnare
     */
    protected void creaUtente(String nickName, String password, String nomeRuolo) {
        LibSecurity.creaUtente(nickName, password, nomeRuolo);
    }// end of static method

}// end of abstract class

