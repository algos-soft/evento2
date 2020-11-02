package it.algos.webbase.web.query;

import it.algos.webbase.web.entity.BaseEntity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper to a CriteriaQuery.
 */
public class CQuery<T extends BaseEntity> {

    private EntityManager em;
    private CriteriaBuilder cb;
    private Root<T> root;
    private ArrayList<Predicate> predicates = new ArrayList<>();
    private CriteriaQuery cq;

    public CQuery(EntityManager em, Class<T> type) {
        this.em = em;
        cb = em.getCriteriaBuilder();
        cq = cb.createQuery(type);
        root = cq.from(type);
    }

    /**
     * adds a filter
     *
     * @param attr  the attribute
     * @param value the value
     */
    public void addFilter(SingularAttribute attr, Object value) {
        Predicate pred = cb.equal(root.get(attr), value);
        predicates.add(pred);
    }

    /**
     * adds a filter
     *
     * @param pred  the predicate
     */
    public void addFilter(Predicate pred) {
        predicates.add(pred);
    }


    /**
     * Performs the query and retrieves the result list
     * @return the results list
     */
    public List<T> getResultList() {
        if (predicates.size() > 0) {
            Predicate[] preds = predicates.toArray(new Predicate[0]);
            cq.where(preds);
        }
        TypedQuery<T> query = em.createQuery(cq);
        List<T> entities = query.getResultList();
        return entities;
    }

    public CriteriaBuilder getCB(){
        return cb;
    }

    public Root<T> getRoot(){
        return root;
    }

    public Path getPath(SingularAttribute attr){
        return root.get(attr);
    }


}




