package it.algos.webbase.web.bootstrap;

import it.algos.webbase.domain.ruolo.Ruolo;
import it.algos.webbase.domain.ruolo.TipoRuolo;
import it.algos.webbase.web.AlgosApp;
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.query.AQuery;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.lang.reflect.Method;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by gac on 07 ott 2015.
 * .
 */
public class SingletonBootStrap {

    private final static Logger logger = Logger.getLogger(ABootStrap.class.getName());
    private static SingletonBootStrap istanza = null;
    private DBPingRunnable dbPingRunnable;
    private Thread dbPingThread;

    /**
     * Il costruttore private impedisce l'istanza di oggetti da parte di classi esterne
     */
    private SingletonBootStrap() {
        executeStartOnlyOne();
    }// end of constructor

    /**
     * Metodo della classe impiegato per accedere al singleton
     */
    public static synchronized SingletonBootStrap getIstanza() {

        if (istanza == null) {
            istanza = new SingletonBootStrap();
        }// end of if cycle

        return istanza;
    }// end of method


    /**
     * Metodo della classe impiegato per accedere al singleton
     * Executed on container startup
     *
     */
    public static synchronized void executeStart() {

        if (istanza == null) {
            istanza = new SingletonBootStrap();
        }// end of if cycle

    }// end of method

    /**
     * Metodo della classe impiegato per accedere al singleton
     * Executed on container shutdown
     */
    public static synchronized void executeStop() {

        if (istanza != null) {
            istanza.executeStopOnlyOne();
        }// end of if cycle

    }// end of method

    /**
     * Esegue solo una volta
     * Executed on container startup
     */
    private void executeStartOnlyOne() {

        /**
         * Create a dummy new EntityManager to trigger initialization of the EntityManagerFactory <br>
         * EntityManagerFactory is lazily initialized and until this is done, <br>
         * the static metamodel is not available. <br>
         */
        EM.createEntityManager();

        /**
         * Starts a thread which periodically queries the database <br>
         * to avoid connection timeouts (on MySQL wait-timeout is 8 hrs) <br>
         */
        dbPingRunnable = new DBPingRunnable();
        dbPingThread = new Thread(dbPingRunnable);
        dbPingThread.start();
        logger.log(Level.INFO, "database ping thread started");

        System.out.println("Context Created");
    }// end of method


    /**
     * Runnable to timely ping the database to avoid connection timeouts <br>
     */
    public class DBPingRunnable implements Runnable {
        private static final int INTERVAL_SECONDS = 600;
        private boolean stop = false;

        @Override
        public void run() {

            while (!stop) {

                // simple generic SQL query here
                EntityManager em = EM.createEntityManager();
                Query q = em.createNativeQuery("SELECT 1");
                Object obj = q.getResultList();

                // sleep time
                try {
                    for (int i = 0; i < 1000; i++) {
                        Thread.sleep(INTERVAL_SECONDS);
                        if (stop) {
                            break;
                        }// end of if cycle
                    }// end of for cycle
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }// end of try/catch

            }// fine del ciclo while
        }// end of method

        public void stop() {
            stop = true;
        }// end of method

    }// end of inner class

    /**
     * Esegue solo una volta
     * Executed on container shutdown
     */
    private void executeStopOnlyOne() {
        // ferma il thread di database ping
        if (dbPingRunnable != null) {

            // stop the runnable
            dbPingRunnable.stop();

            // wait for thread end
            if (dbPingThread != null) {
                while (dbPingThread.isAlive()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }// end of try/catch
                }// fine del ciclo while
                logger.log(Level.INFO, "database ping thread stopped");
            }// fine del blocco if
        }// fine del blocco if

        // shut down MySQL AbandonedConnectionCleanupThread (if found)
        try {
            Class<?> cls = Class.forName("com.mysql.jdbc.AbandonedConnectionCleanupThread");
            Method mth = (cls == null ? null : cls.getMethod("shutdown"));
            if (mth != null) {
                logger.log(Level.INFO, "MySQL connection cleanup thread shutdown");
                mth.invoke(null);
                logger.log(Level.INFO, "MySQL connection cleanup thread shutdown successful");
            }// fine del blocco if
        } catch (Throwable thr) {
            logger.log(Level.SEVERE, "Failed to shutdown SQL connection cleanup thread: " + thr.getMessage());
            thr.printStackTrace();
        }// end of try/catch

        // deregister JDBC drivers
        // This manually deregisters JDBC driver, which prevents Tomcat 7
        // from complaining about memory leaks wrto this class
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                logger.log(Level.INFO, String.format("deregistering jdbc driver: %s", driver));
            } catch (SQLException e) {
                logger.log(Level.SEVERE, String.format("Error deregistering driver %s", driver), e);
            }// end of try/catch
        }// fine del ciclo while

        System.out.println("Context Destroyed");
    }// end of method

}// end of class
