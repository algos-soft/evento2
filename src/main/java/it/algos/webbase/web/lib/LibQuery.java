package it.algos.webbase.web.lib;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * Created by gac on 22 ott 2016.
 * Libreria
 */
public abstract class LibQuery {


    /**
     * Se la transzazione è già stata attivata a monte, la lascia inalterata
     * altrimenti la inizia, esegue il commit e l'eventuale rollback
     *
     * @return true if the transaction was not active
     */
    public static boolean isTransactionNotActive(EntityManager manager) {
        EntityTransaction transaction;
        boolean transactionActive = false;

        transaction = manager.getTransaction();
        if (transaction != null) {
            transactionActive = transaction.isActive();
        }// end of if cycle

        return !transactionActive;
    }// end of static method


}// end of static class
