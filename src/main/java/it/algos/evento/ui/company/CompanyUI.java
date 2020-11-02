package it.algos.evento.ui.company;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import it.algos.evento.entities.company.Company;
import it.algos.evento.entities.company.Company_;
import it.algos.evento.pref.EventoPrefs;
import it.algos.webbase.domain.utente.Utente;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.login.Login;
import it.algos.webbase.web.login.LoginEvent;
import it.algos.webbase.web.query.AQuery;
import it.algos.webbase.web.ui.AlgosUI;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * UI iniziale di Company.
 */
@Theme("evento")
@Title("eVento")

//questo per rendere disponibile il widgetset solo in questa UI.
//se messo nel servlet, diventa disponibile a tutte le UI del servlet
//@Widgetset("com.sibvisions.vaadin.Widgetset")

//@PreserveOnRefresh
public class CompanyUI extends AlgosUI {

    private final static Logger logger = Logger.getLogger(CompanyUI.class.getName());

    @Override
    public void attach() {
        super.attach();
    }

    @Override
    public void detach() {
        super.detach();
    }

    @Override
    protected void init(VaadinRequest request) {

//        // about 6 months expiry time for company login
//        int time=60*60*24*7*4*6;
//        Login.getLogin().setExpiryTime(time);

        // parse request parameters
        checkParams(request);

        // intervallo di polling della UI
        // consente di vedere i risultati anche quando si aggiorna
        // la UI da un thread separato sul server
        setPollInterval(1000);

        int autoCompanyId = EventoPrefs.autoLoginCompany.getInt();
        if (autoCompanyId == 0) {

            // display the login page or the main page if already logged
            if (LibSession.isLogged()) {
                setContent(new CompanyHome());
            } else {
                setContent(new CompanyLogin());
            }

        } else {
            // user login disabilitato, effettua automaticamente il login alla azienda di default
            // e mostra direttamente la home
            BaseEntity entity = AQuery.getEntity(Company.class, Company_.id, autoCompanyId);
            if (entity != null) {
                if (entity instanceof Company) {
                    Company company = (Company) entity;
                    CompanySessionLib.setCompany(company);
                    setContent(new CompanyHome());
                }
            }
        }


        addAttachListener(new AttachListener() {
            @Override
            public void attach(AttachEvent attachEvent) {
                logger.log(Level.INFO, "UI attached: " + CompanyUI.this);
            }
        });


        addDetachListener(new DetachListener() {
            @Override
            public void detach(DetachEvent detachEvent) {
                logger.log(Level.INFO, "UI detached: " + CompanyUI.this);
            }
        });

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(true){
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    logger.log(Level.INFO, "Last Heartbeat: "+CompanyUI.this.getLastHeartbeatTimestamp());
//                    logger.log(Level.INFO, "is closing: "+CompanyUI.this.isClosing());
//                }
//
//            }
//        }).start();

    }

    /**
     * Legge eventuali parametri passati nella request
     * <p>
     */
    public void checkParams(VaadinRequest request) {

        LibSession.setDeveloper(false);

        // legge il parametro "developer" e regola la variabile statica
        if (request.getParameter("developer") != null) {
            boolean developer = (request.getParameter("developer") != null);
            LibSession.setDeveloper(developer);
        }// fine del blocco if

        // login from url parameters
        // legge il parametro "user" e "password" ed effettua il login
        if (request.getParameter("user") != null) {
            if (request.getParameter("password") != null) {
                String login = request.getParameter("user");
                String pass = request.getParameter("password");
                Utente user = Utente.validate(login, pass);
                if (user != null) {

                    // registra la company nella sessione in base all'utente
                    if (CompanySessionLib.registerCompanyByUser(user)) {
                        Login.getLogin().setUser(user);
                        CompanySessionLib.setLogin(Login.getLogin());
                    } else {
                        CompanySessionLib.setLogin(null);
                        String err = "L'utente " + user + " (loggato tramite parametri url) è registrato, ma non c'è l'azienda corrispondente. Login fallito.";
                        logger.log(Level.SEVERE, err);
                    }

                } else {
                    CompanySessionLib.setLogin(null);
                }
            }
        }


    }

    @Override
    public void onUserLogin(LoginEvent e) {

    }
}