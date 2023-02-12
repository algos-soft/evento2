package it.algos.evento.daemons;

import it.algos.webbase.web.entity.EM;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Esegue la rotazione del registro eventi prenotazioni
 * <p>
 * Cancella tutti gli elementi del registro eventi prenotazione
 * più vecchi di 2 anni
 */
public class EventiPrenRotator implements Runnable {
    private final static Logger logger = Logger.getLogger(EventiPrenRotator.class.getName());
    private LocalDate checkDate;


    /**
     * @param checkDate la data prima della quale cancellare i record
     */
    public EventiPrenRotator(LocalDate checkDate) {
        super();
        this.checkDate = checkDate;
    }

    @Override
    public void run() {

        logger.log(Level.INFO, "start rotazione eventi precedenti il "+ checkDate.toString());

        EntityManager entityManager=null;
        try {
            entityManager = EM.createEntityManager();

            ZoneId z = ZoneId.systemDefault();
            ZonedDateTime zdt = checkDate.atStartOfDay(z);
            Instant instant = zdt.toInstant();
            java.util.Date date = java.util.Date.from( instant );

            Query query = entityManager.createQuery("delete from EventoPren where timestamp < :date")
                    .setParameter("date", date);

            entityManager.getTransaction().begin();
            query.executeUpdate();
            entityManager.getTransaction().commit();

        } catch (Exception e){
            entityManager.getTransaction().rollback();
            throw e;
        } finally {
            entityManager.close();
        }

        logger.log(Level.INFO, "end rotazione eventi");

    }



}
