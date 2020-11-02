package it.algos.webbase.multiazienda;

import com.vaadin.data.Container;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.web.field.RelatedComboField;

import java.util.List;

@SuppressWarnings("serial")
public class ERelatedComboField extends RelatedComboField {

    private BaseCompany company = null;

    @SuppressWarnings("rawtypes")
    public ERelatedComboField(Class entityClass) {
        super(entityClass);
    }// end of constructor


    /**
     * Costruttore
     *
     * @param entityClass the Entity class
     * @param caption     da visualizzare nella UI
     */
    @SuppressWarnings("rawtypes")
    public ERelatedComboField(Class entityClass, String caption) {
        super(entityClass, caption);
    }// end of constructor

    /**
     * Costruttore
     *
     * @param entityClass the Entity class
     * @param company     da filtrare
     */
    @SuppressWarnings("rawtypes")
    public ERelatedComboField(Class entityClass, BaseCompany company) {
        this(entityClass, null, company);
    }// end of constructor


    /**
     * Costruttore
     *
     * @param entityClass the Entity class
     * @param caption     da visualizzare nella UI
     * @param company     da filtrare
     */
    @SuppressWarnings("rawtypes")
    public ERelatedComboField(Class entityClass, String caption, BaseCompany company) {
        super(entityClass, caption, false);
        this.company = company;
        super.init();
    }// end of constructor


    /**
     * Creates the container usead as data source for the combo.
     * <p>
     *
     * @return the container
     */
    public Container createContainer() {
        if (company != null) {
            return new EJPAContainer(getEntityClass(), getEntityManager(), company);
        } else {
            return new EJPAContainer(getEntityClass(), getEntityManager());
        }// end of if/else cycle
    }// end of method


}// end of class
