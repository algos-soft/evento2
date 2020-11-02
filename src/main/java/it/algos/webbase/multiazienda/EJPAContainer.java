package it.algos.webbase.multiazienda;

import com.vaadin.addon.jpacontainer.EntityProvider;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.provider.LocalEntityProvider;
import com.vaadin.data.util.filter.Compare;
import it.algos.webbase.domain.company.BaseCompany;

import javax.persistence.EntityManager;

/**
 * JPAContainer automatically filtered on a company.
 */
@SuppressWarnings("serial")
public class EJPAContainer extends JPAContainer<CompanyEntity> {

    private static BaseCompany company;

    /**
     * Create a container filtered on the current Company.
     *
     * @param entityClass the entity class
     * @param manager     the entity manager
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public EJPAContainer(Class entityClass, EntityManager manager) {
        this(entityClass, manager, CompanySessionLib.getCompany());
    }// end of constructor


    /**
     * Create a container filtered on a given company.
     *
     * @param entityClass the entity class
     * @param manager     the entity manager
     * @param company     the company on which to filter
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public EJPAContainer(Class entityClass, EntityManager manager, BaseCompany company) {
        super(entityClass);
        EJPAContainer.company = company;
        EntityProvider entityProvider = new LocalEntityProvider<CompanyEntity>(entityClass, manager);
        setEntityProvider(entityProvider);
        addContainerFilter(createCompanyFilter());
    }// end of constructor




    private static Filter createCompanyFilter() {
        return new Compare.Equal(CompanyEntity_.company.getName(), company);
    }

    /**
     * Restores the company filter after removing all the filters
     */
    @Override
    public void removeAllContainerFilters() {
        super.removeAllContainerFilters();
        addContainerFilter(createCompanyFilter());
    }


}// end of class
