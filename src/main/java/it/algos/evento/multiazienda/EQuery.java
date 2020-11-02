package it.algos.evento.multiazienda;

import it.algos.evento.entities.evento.Evento;
import it.algos.evento.entities.evento.Evento_;
import it.algos.evento.entities.prenotazione.Prenotazione;
import it.algos.evento.entities.prenotazione.Prenotazione_;
import it.algos.evento.entities.rappresentazione.Rappresentazione;
import it.algos.evento.entities.rappresentazione.Rappresentazione_;
import it.algos.evento.entities.stagione.Stagione;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.multiazienda.CompanyEntity_;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.entity.EM;
import org.joda.time.DateTime;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Utility methods for FILTERED queries.
 * <p>
 * The results of these methods are always filtered on the current Company.
 */
public class EQuery {

    /**
     * Ritorna il numero di prenotazioni per l'azienda corrente in una data stagione.
     *
     * @param stagione  la stagione
     * @param congelate 1 per congelata=true, 0 per congelata=false, -1 per non filtrare sul campo congelata
     * @return il numero totale di prenotazioni
     */
    public static int countPrenotazioni(Stagione stagione, int congelate) {

        EntityManager em = EM.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<Prenotazione> root = cq.from(Prenotazione.class);
        Join<Prenotazione, Rappresentazione> jRapp = root.join(Prenotazione_.rappresentazione);
        Join<Rappresentazione, Evento> jEve = jRapp.join(Rappresentazione_.evento);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(creaFiltroCompany(root, cb));
        predicates.add(cb.equal(jEve.get(Evento_.stagione), stagione));

        if (congelate >= 0) {
            if (congelate == 0) {
                predicates.add(cb.equal(root.get(Prenotazione_.congelata), false));
            }
            if (congelate == 1) {
                predicates.add(cb.equal(root.get(Prenotazione_.congelata), true));
            }
        }

        cq.select(cb.count(root));

        cq.where(predicates.toArray(new Predicate[]{}));

        TypedQuery<Long> q = em.createQuery(cq);
        Long num = q.getSingleResult();
        if(num==null){
            num=0l;
        }

        em.close();

        return num.intValue();
    }

    /**
     * Ritorna il numero totale di prenotazioni per l'azienda
     * corrente nella stagione corrente.
     *
     * @return il numero totale di prenotazioni congelate
     */
    public static int countPrenotazioni() {
        return countPrenotazioni(Stagione.getStagioneCorrente(), -1);
    }

    /**
     * Ritorna il numero di prenotazioni congelate per l'azienda
     * corrente nela stagione corrente.
     *
     * @return il numero di prenotazioni congelate
     */
    public static int countPrenotazioniCongelate() {

        EntityManager em = EM.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<Prenotazione> root = cq.from(Prenotazione.class);

        cq.where(creaFiltroPrenCongelate(cb, root));

        cq.select(cb.count(root));

        Long num = em.createQuery(cq).getSingleResult();
        if(num==null){
            num=0l;
        }

        em.close();

        return num.intValue();

    }

    /**
     * Ritorna il numero di posti totale delle prenotazioni non confermate
     * per l'azienda corrente nella stagione corrente.
     *
     * @return il numero di posti totale delle prenotazioni
     */
    public static int sumPostiPrenotazioniCongelate() {
        EntityManager em = EM.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);

        Root<Prenotazione> root = cq.from(Prenotazione.class);

        cq.where(creaFiltroPrenCongelate(cb, root));

        cq.select(cb.sum(getExprPostiPrenotati(cb, root)));

        TypedQuery<Integer> q = em.createQuery(cq);
        Integer num = q.getSingleResult();
        if(num==null){
            num=0;
        }

        em.close();

        return num;
    }


    /**
     * Ritorna l'importo totale delle prenotazioni non confermate
     * per l'azienda corrente nella stagione corrente.
     *
     * @return l'importo totale delle prenotazioni
     */
    public static BigDecimal sumImportoPrenotazioniCongelate() {

        EntityManager em = EM.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Number> cq = cb.createQuery(Number.class);

        Root<Prenotazione> root = cq.from(Prenotazione.class);

        cq.where(creaFiltroPrenCongelate(cb, root));

        cq.select(cb.sum(getExprImportoPrevisto(cb, root)));

        Number num = em.createQuery(cq).getSingleResult();
        em.close();

        BigDecimal bd=new BigDecimal(0);
        if (num != null) {
            if (num instanceof BigDecimal){
                bd=(BigDecimal)num;
            }
        }

        return bd;
    }


    /**
     * Ritorna il numero di eventi per l'azienda corrente in una data stagione.
     *
     * @param stagione la stagione
     * @return il numero totale di eventi
     */
    public static int countEventi(Stagione stagione) {

        EntityManager em = EM.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<Evento> root = cq.from(Evento.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(creaFiltroCompany(root, cb));
        predicates.add(cb.equal(root.get(Evento_.stagione), stagione));
        cq.select(cb.count(root));

        cq.where(predicates.toArray(new Predicate[]{}));

        TypedQuery<Long> q = em.createQuery(cq);
        Long num = q.getSingleResult();
        if(num==null){
            num=0l;
        }

        em.close();

        return num.intValue();
    }


    /**
     * Ritorna il numero di rappresentazioni per l'azienda corrente in una data stagione.
     *
     * @param stagione la stagione
     * @param primaDel solo quelle precedenti questa data (null per tutte)
     * @return il numero totale di rappresentazioni
     */
    public static int countRappresentazioni(Stagione stagione, Date primaDel) {

        EntityManager em = EM.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<Rappresentazione> root = cq.from(Rappresentazione.class);
        Join<Rappresentazione, Evento> joinEve = root.join(Rappresentazione_.evento, JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(creaFiltroCompany(root, cb));
        predicates.add(cb.equal(joinEve.get(Evento_.stagione), stagione));
        if (primaDel != null) {
            predicates.add(cb.lessThan(root.get(Rappresentazione_.dataRappresentazione), primaDel));
        }

        cq.where(predicates.toArray(new Predicate[]{}));

        cq.select(cb.count(root));

        TypedQuery<Long> q = em.createQuery(cq);
        Long num = q.getSingleResult();
        if(num==null){
            num=0l;
        }

        em.close();

        return num.intValue();
    }

    /**
     * Ritorna il numero di rappresentazioni per l'azienda corrente in una data stagione.
     *
     * @param stagione la stagione
     * @return il numero totale di rappresentazioni
     */
    public static int countRappresentazioni(Stagione stagione) {
        return countRappresentazioni(stagione, null);
    }


    /**
     * Ritorna il numero di posti prenotati per l'azienda corrente in una data stagione.
     * (escluse le prenotazioni congelate)
     * @param stagione la stagione
     * @return il numero totale di posti prenotati
     */
    public static int countPostiPrenotati(Stagione stagione) {

        EntityManager em = EM.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);

        Root<Prenotazione> root = cq.from(Prenotazione.class);
        Join<Prenotazione, Rappresentazione> joinRapp = root.join(Prenotazione_.rappresentazione);
        Join<Rappresentazione, Evento> joinEve = joinRapp.join(Rappresentazione_.evento);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(creaFiltroCompany(root, cb));
        predicates.add(cb.equal(root.get(Prenotazione_.congelata), false));
        predicates.add(cb.equal(joinEve.get(Evento_.stagione), stagione));

        cq.where(predicates.toArray(new Predicate[]{}));

        Expression<Integer> sTot=cb.sum(getExprPostiPrenotati(cb, root));
        cq.select(sTot);

        TypedQuery<Integer> q = em.createQuery(cq);
        Integer num = q.getSingleResult();
        if(num==null){
            num=0;
        }

        em.close();

        return num;
    }



    /**
     * Ritorna la capienza totale per l'azienda corrente in una data stagione.
     *
     * @param stagione la stagione
     * @return la capienza totale
     */
    public static int countCapienza(Stagione stagione) {

        EntityManager em = EM.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);

        Root<Rappresentazione> root = cq.from(Rappresentazione.class);
        Join<Rappresentazione, Evento> joinEve = root.join(Rappresentazione_.evento);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(creaFiltroCompany(root, cb));
        predicates.add(cb.equal(joinEve.get(Evento_.stagione), stagione));

        cq.where(predicates.toArray(new Predicate[]{}));

        Expression<Integer> e1 = cb.sum(root.get(Rappresentazione_.capienza));

        cq.select(e1);

        TypedQuery<Integer> q = em.createQuery(cq);
        Integer num = q.getSingleResult();
        if(num==null){
            num=0;
        }
        em.close();

        return num;
    }


    /**
     * Ritorna il numero prenotazioni scadute per
     * l'azienda corrente nella stagione corrente.
     * (sono escluse le congelate)
     *
     * @return il numero di prenotazioni in ritardo di conferma
     */
    public static int countPrenotazioniScadute() {

        EntityManager em = EM.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<Prenotazione> root = cq.from(Prenotazione.class);
        Join<Prenotazione, Rappresentazione> joinRapp = root.join(Prenotazione_.rappresentazione);
        Join<Rappresentazione, Evento> joinEve = joinRapp.join(Rappresentazione_.evento);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(creaFiltroCompany(root, cb));
        predicates.add(cb.equal(joinEve.get(Evento_.stagione), Stagione.getStagioneCorrente()));
        predicates.add(cb.equal(root.get(Prenotazione_.confermata), false));
        predicates.add(cb.lessThan(root.get(Prenotazione_.scadenzaConferma), today()));
        predicates.add(cb.equal(root.get(Prenotazione_.congelata), false));

        cq.where(predicates.toArray(new Predicate[]{}));

        cq.select(cb.count(root));

        TypedQuery<Long> q = em.createQuery(cq);
        Integer num = q.getSingleResult().intValue();
        if(num==null){
            num=0;
        }

        em.close();

        return num;
    }


    /**
     * Ritorna il numero prenotazioni in ritardo di conferma pagamento (fase 1)
     * per l'azienda corrente nella stagione corrente.
     * (sono escluse le prenotazioni congelate)
     *
     * @return il numero di prenotazioni in ritardo di conferma pagamento (fase 1)
     */
    public static int countPrenRitardoPagamento1() {

        EntityManager em = EM.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<Prenotazione> root = cq.from(Prenotazione.class);
        Join<Prenotazione, Rappresentazione> joinRapp = root.join(Prenotazione_.rappresentazione);
        Join<Rappresentazione, Evento> joinEve = joinRapp.join(Rappresentazione_.evento);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(creaFiltroCompany(root, cb));
        predicates.add(cb.equal(joinEve.get(Evento_.stagione), Stagione.getStagioneCorrente()));
        predicates.add(cb.equal(root.get(Prenotazione_.confermata), true));
        predicates.add(cb.equal(root.get(Prenotazione_.pagamentoConfermato), false));
        predicates.add(cb.lessThan(root.get(Prenotazione_.scadenzaPagamento), today()));
        predicates.add(cb.equal(root.get(Prenotazione_.congelata), false));

        cq.where(predicates.toArray(new Predicate[]{}));

        cq.select(cb.count(root));

        TypedQuery<Long> q = em.createQuery(cq);
        Integer num = q.getSingleResult().intValue();
        if(num==null){
            num=0;
        }

        em.close();

        return num;
    }


    /**
     * Ritorna il numero prenotazioni con pagamento scaduto
     * per l'azienda corrente nella stagione corrente.
     * (sono escluse le prenotazioni congelate)
     *
     * @return il numero di prenotazioni con pagamento scaduto
     */
    public static int countPrenPagamentoScaduto() {

        EntityManager em = EM.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<Prenotazione> root = cq.from(Prenotazione.class);
        Join<Prenotazione, Rappresentazione> joinRapp = root.join(Prenotazione_.rappresentazione);
        Join<Rappresentazione, Evento> joinEve = joinRapp.join(Rappresentazione_.evento);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(creaFiltroCompany(root, cb));
        predicates.add(cb.equal(joinEve.get(Evento_.stagione), Stagione.getStagioneCorrente()));
        predicates.add(cb.equal(root.get(Prenotazione_.confermata), true));
        predicates.add(cb.equal(root.get(Prenotazione_.pagamentoConfermato), true));
        predicates.add(cb.equal(root.get(Prenotazione_.pagamentoRicevuto), false));
        predicates.add(cb.lessThan(root.get(Prenotazione_.scadenzaPagamento), today()));
        predicates.add(cb.equal(root.get(Prenotazione_.congelata), false));

        cq.where(predicates.toArray(new Predicate[]{}));

        cq.select(cb.count(root));

        TypedQuery<Long> q = em.createQuery(cq);
        Integer num = q.getSingleResult().intValue();
        if(num==null){
            num=0;
        }

        em.close();

        return num;
    }




    /**
     * Ritorna il numero di prenotazioni non confermate
     * per l'azienda corrente nella stagione corrente.
     *
     * @return il numero di prenorazioni con pagamento non confermato
     */
    public static int countPrenotazioniNonConfermate() {

        EntityManager em = EM.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<Prenotazione> root = cq.from(Prenotazione.class);

        cq.where(creaFiltroPrenNonConf(cb, root));

        cq.select(cb.count(root));

        Long num = em.createQuery(cq).getSingleResult();
        if(num==null){
            num=0l;
        }

        em.close();

        return num.intValue();

    }

    /**
     * Ritorna il numero di posti totale delle prenotazioni non confermate
     * per l'azienda corrente nella stagione corrente.
     *
     * @return il numero di posti totale delle prenotazioni
     */
    public static int sumPostiPrenotazioniNonConfermate() {
        EntityManager em = EM.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);

        Root<Prenotazione> root = cq.from(Prenotazione.class);

        cq.where(creaFiltroPrenNonConf(cb, root));

        cq.select(cb.sum(getExprPostiPrenotati(cb, root)));

        TypedQuery<Integer> q = em.createQuery(cq);
        Integer num = q.getSingleResult();
        if(num==null){
            num=0;
        }

        em.close();

        return num;
    }


        /**
         * Ritorna l'importo totale delle prenotazioni non confermate
         * per l'azienda corrente nella stagione corrente.
         *
         * @return l'importo totale delle prenotazioni
         */
    public static BigDecimal sumImportoPrenotazioniNonConfermate() {

        EntityManager em = EM.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Number> cq = cb.createQuery(Number.class);

        Root<Prenotazione> root = cq.from(Prenotazione.class);

        cq.where(creaFiltroPrenNonConf(cb, root));

        cq.select(cb.sum(getExprImportoPrevisto(cb, root)));

        Number num = em.createQuery(cq).getSingleResult();
        em.close();

        BigDecimal bd=new BigDecimal(0);
        if (num != null) {
            if (num instanceof BigDecimal){
                bd=(BigDecimal)num;
            }
        }

        return bd;
    }


    /**
     * Crea un filtro per selezionare le prenotazioni congelate
     * per l'azienda corrente e la stagione corrente
     */
    private static Predicate[] creaFiltroPrenCongelate(CriteriaBuilder cb, Root<Prenotazione> root){
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(creaFiltroCompany(root, cb));
        predicates.add(creaFiltroStagioneCorrente(root, cb));
        predicates.add(cb.equal(root.get(Prenotazione_.congelata), true));
        return predicates.toArray(new Predicate[]{});
    }

    /**
     * Crea un filtro per selezionare le prenotazioni non confermate
     * per l'azienda corrente e la stagione corrente
     * (sono escluse le congelate)
     */
    private static Predicate[] creaFiltroPrenNonConf(CriteriaBuilder cb, Root<Prenotazione> root){
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(creaFiltroCompany(root, cb));
        predicates.add(creaFiltroStagioneCorrente(root, cb));
        predicates.add(cb.equal(root.get(Prenotazione_.confermata), false));
        predicates.add(cb.equal(root.get(Prenotazione_.congelata), false));
        return predicates.toArray(new Predicate[]{});
    }




    /**
     * Ritorna il numero di prenotazioni con pagamento non confermato
     * per l'azienda corrente nella stagione corrente.
     *
     * @return il numero di prenorazioni
     */
    public static int countPrenotazioniPagamentoNonConfermato() {

        EntityManager em = EM.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<Prenotazione> root = cq.from(Prenotazione.class);

        cq.where(getFiltroPagamento(root, cb, false, false));

        cq.select(cb.count(root));

        Long num = em.createQuery(cq).getSingleResult();
        if(num==null){
            num=0l;
        }

        em.close();

        return num.intValue();

    }


    /**
     * Ritorna il numero di posti totale delle prenotazioni con pagamento non confermato
     * per l'azienda corrente nella stagione corrente.
     *
     * @return il numero di posti totale delle prenotazioni
     */
    public static int sumPostiPrenotazioniPagamentoNonConfermato() {
        EntityManager em = EM.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);

        Root<Prenotazione> root = cq.from(Prenotazione.class);

        cq.where(getFiltroPagamento(root, cb, false, false));

        cq.select(cb.sum(getExprPostiPrenotati(cb, root)));

        TypedQuery<Integer> q = em.createQuery(cq);
        Integer num = q.getSingleResult();
        if(num==null){
            num=0;
        }

        em.close();

        return num;
    }




    /**
     * Ritorna l'importo totale delle prenorazioni con pagamento da confermare (scaduto)
     * per l'azienda corrente nella stagione corrente.
     *
     * @return l'importo totale delle prenorazioni
     */
    public static BigDecimal sumImportoPrenotazioniPagamentoNonConfermato() {

        EntityManager em = EM.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Number> cq = cb.createQuery(Number.class);

        Root<Prenotazione> root = cq.from(Prenotazione.class);
        cq.where(getFiltroPagamento(root, cb, false, false));

        cq.select(cb.sum(getExprImportoPrevisto(cb, root)));

        Number num = em.createQuery(cq).getSingleResult();
        em.close();

        BigDecimal bd=new BigDecimal(0);
        if (num != null) {
            if (num instanceof BigDecimal){
                bd=(BigDecimal)num;
            }
        }

        return bd;
    }


    /**
     * Crea una Expression che calcola il totale importo previsto
     */
    public static Expression<Number> getExprImportoPrevisto(CriteriaBuilder cb, Root<Prenotazione> root){

        // coalesce() ritorna il primo non nullo in una lista di Expression
        // evita che entrino eventuali null nei prodotti, che renderebbero null tutto il risultato.
        // in questo modo, se ci sono dei null vengono trattati come zero.
        // Dopo questa modifica ho preso alcune misure nelle entities per evitare numeri nulli sul database.
        // Alex dic-2015
        CriteriaBuilder.Coalesce zero = cb.coalesce().value(0); // creo una Expression che rappresenta lo zero, che uso nei coalesce() successivi
        Expression<Number> e1 = cb.prod(cb.coalesce(root.get(Prenotazione_.numInteri), zero), cb.coalesce(root.get(Prenotazione_.importoIntero), zero));
        Expression<Number> e2 = cb.prod(cb.coalesce(root.get(Prenotazione_.numRidotti), zero), cb.coalesce(root.get(Prenotazione_.importoRidotto), zero));
        Expression<Number> e3 = cb.prod(cb.coalesce(root.get(Prenotazione_.numDisabili), zero), cb.coalesce(root.get(Prenotazione_.importoDisabili), zero));
        Expression<Number> e4 = cb.prod(cb.coalesce(root.get(Prenotazione_.numAccomp), zero), cb.coalesce(root.get(Prenotazione_.importoAccomp), zero));
        Expression<Number> e1e2 = cb.sum(e1, e2);   //int+rid
        Expression<Number> e3e4 = cb.sum(e3, e4);   //dis+accomp
        Expression<Number> e1234 = cb.sum(e1e2, e3e4);  //tutti
        Expression<Number> eGruppo = cb.coalesce(root.get(Prenotazione_.importoGruppo), zero);   //imp fisso gruppo
        Expression<Number> expr = cb.sum(e1234, eGruppo);   // totale

        return expr;
    }

    /**
     * Crea una Expression che calcola il totale posti prenotati
     */
    public static Expression<Integer> getExprPostiPrenotati(CriteriaBuilder cb, Root<Prenotazione> root){
        Expression<Integer> e1 = cb.sum(root.get(Prenotazione_.numInteri), root.get(Prenotazione_.numRidotti));
        Expression<Integer> e2 = cb.sum(root.get(Prenotazione_.numDisabili), root.get(Prenotazione_.numAccomp));
        return cb.sum(e1, e2);
    }



    /**
     * Ritorna il numero di prenorazioni con pagamento confermato
     * per l'azienda corrente nella stagione corrente.
     *
     * @return il numero di prenorazioni con pagamento confermato
     */
    public static int countPrenotazioniPagamentoConfermato() {

        EntityManager em = EM.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<Prenotazione> root = cq.from(Prenotazione.class);
        cq.where(getFiltroPagamento(root, cb, true, false));

        cq.select(cb.count(root));

        TypedQuery<Long> q = em.createQuery(cq);
        Integer num = q.getSingleResult().intValue();
        if(num==null){
            num=0;
        }

        em.close();

        return num;

    }


    /**
     * Ritorna il numero di posti totale delle prenotazioni con pagamento confermato
     * per l'azienda corrente nella stagione corrente.
     *
     * @return il numero di posti totale delle prenotazioni
     */
    public static int sumPostiPrenotazioniPagamentoConfermato() {
        EntityManager em = EM.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);

        Root<Prenotazione> root = cq.from(Prenotazione.class);

        cq.where(getFiltroPagamento(root, cb, true, false));

        cq.select(cb.sum(getExprPostiPrenotati(cb, root)));

        TypedQuery<Integer> q = em.createQuery(cq);
        Integer num = q.getSingleResult();
        if(num==null){
            num=0;
        }

        em.close();

        return num;
    }


    /**
     * Ritorna l'importo totale delle prenorazioni con pagamento confermato
     * per l'azienda corrente nella stagione corrente.
     *
     * @return l'importo totale delle prenorazioni
     */
    public static BigDecimal sumImportoPrenotazioniPagamentoConfermato() {
        BigDecimal num;

        EntityManager em = EM.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> cq = cb.createQuery(BigDecimal.class);

        Root<Prenotazione> root = cq.from(Prenotazione.class);
        cq.where(getFiltroPagamento(root, cb, true, false));

        Expression<BigDecimal> e1 = cb.sum(root.get(Prenotazione_.importoPagato));

        cq.select(e1);

        num = em.createQuery(cq).getSingleResult();
        em.close();

        if (num == null) {
            num = new BigDecimal(0);
        }

        return num;
    }











    /**
     * Ritorna il numero di prenotazioni con pagamento ricevuto
     * per l'azienda corrente nella stagione corrente.
     *
     * @return il numero di prenorazioni con pagamento ricevuto
     */
    public static int countPrenotazioniPagamentoRicevuto() {

        EntityManager em = EM.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<Prenotazione> root = cq.from(Prenotazione.class);
        cq.where(getFiltroPagamento(root, cb, null, true));

        cq.select(cb.count(root));

        Long num = em.createQuery(cq).getSingleResult();
        if(num==null){
            num=0l;
        }

        em.close();

        return num.intValue();

    }

    /**
     * Ritorna il numero di posti totale delle prenotazioni con pagamento ricevuto
     * per l'azienda corrente nella stagione corrente.
     *
     * @return il numero di posti totale delle prenotazioni
     */
    public static int sumPostiPrenotazioniPagamentoRicevuto() {
        EntityManager em = EM.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);

        Root<Prenotazione> root = cq.from(Prenotazione.class);

        cq.where(getFiltroPagamento(root, cb, null, true));

        cq.select(cb.sum(getExprPostiPrenotati(cb, root)));

        TypedQuery<Integer> q = em.createQuery(cq);
        Integer num = q.getSingleResult();
        if (num==null){
            num=0;
        }
        em.close();

        return num;
    }



    /**
     * Ritorna l'importo totale delle prenorazioni con pagamento ricevuto
     * per l'azienda corrente nella stagione corrente.
     *
     * @return l'importo totale delle prenorazioni
     */
    public static BigDecimal sumImportoPrenotazioniPagamentoRicevuto() {

        EntityManager em = EM.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> cq = cb.createQuery(BigDecimal.class);

        Root<Prenotazione> root = cq.from(Prenotazione.class);
        cq.where(getFiltroPagamento(root, cb, null, true));

        Expression<BigDecimal> e1 = cb.sum(root.get(Prenotazione_.importoPagato));

        cq.select(e1);

        BigDecimal num = em.createQuery(cq).getSingleResult();
        em.close();

        if(num==null){
            num=new BigDecimal(0);
        }

        return num;
    }






    /**
     * Crea un filtro che seleziona le prenotazioni CONFERMATE con pagamento
     * confermato o non confermato per l'azienda corrente e la stagione corrente.
     * (le prenotazioni congelate sono escluse)
     *
     * @param root       root della query
     * @param cb         il CriteriaBuilder da usare
     * @param confermato flag di selezione confermato, null per non selezionare
     * @param ricevuto flag di selezione ricevuto, null per non selezionare
     */
    private static Predicate[] getFiltroPagamento(Root<Prenotazione> root, CriteriaBuilder cb, Boolean confermato, Boolean ricevuto) {
        Join<Prenotazione, Rappresentazione> joinRapp = root.join(Prenotazione_.rappresentazione);
        Join<Rappresentazione, Evento> joinEve = joinRapp.join(Rappresentazione_.evento);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(creaFiltroCompany(root, cb));
        predicates.add(cb.equal(root.get(Prenotazione_.confermata), true));
        predicates.add(cb.equal(joinEve.get(Evento_.stagione), Stagione.getStagioneCorrente()));
        if (confermato!=null){
            predicates.add(cb.equal(root.get(Prenotazione_.pagamentoConfermato), confermato));
        }
        if (ricevuto!=null){
            predicates.add(cb.equal(root.get(Prenotazione_.pagamentoRicevuto), ricevuto));
        }
        predicates.add(cb.equal(root.get(Prenotazione_.congelata), false));
        return predicates.toArray(new Predicate[0]);
    }

    /**
     * Crea un filtro sulla company corrente a una query.
     */
    private static Predicate creaFiltroCompany(Root root, CriteriaBuilder cb) {
        BaseCompany company = CompanySessionLib.getCompany();
        return cb.equal(root.get(CompanyEntity_.company), company);
    }

    /**
     * Crea un filtro sulla stagione corrente.
     */
    private static Predicate creaFiltroStagioneCorrente(Root root, CriteriaBuilder cb) {
        Stagione stagione = Stagione.getStagioneCorrente();
        Join<Prenotazione, Rappresentazione> joinRapp = root.join(Prenotazione_.rappresentazione);
        Join<Rappresentazione, Evento> joinEve = joinRapp.join(Rappresentazione_.evento);
        return cb.equal(joinEve.get(Evento_.stagione), stagione);
    }


    /**
     * @return la data di oggi ale ore 0:00
     */
    private static Date today() {
        return new DateTime().withTimeAtStartOfDay().toDate();
    }


}

