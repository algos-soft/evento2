package it.algos.webbase.multiazienda;

import com.vaadin.data.util.filter.Compare;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.web.entity.BaseEntity_;
import org.vaadin.addons.lazyquerycontainer.LazyEntityContainer;

import javax.persistence.EntityManager;

/**
 * LazyEntityContainer automatically filtered on a company.
 */
public class ELazyContainer extends LazyEntityContainer {

    private static BaseCompany company;

    /**
     * Create a container filtered on a given company.
     *
     * @param entityManager the entity manager
     * @param entityClass   the entity class
     * @param batchSize     the paging size
     * @param company       the company on which to filter
     */
    public ELazyContainer(EntityManager entityManager, Class entityClass, int batchSize, BaseCompany company) {
        super(entityManager, entityClass, batchSize, BaseEntity_.id.getName(), true, true, true);
        ELazyContainer.company = company;

        // automatically add the id property
        addContainerProperty(BaseEntity_.id.getName(), Long.class, 0L, true, true);

        // add the company filter
        Filter filter = createCompanyFilter();
        if (filter!=null){
            addContainerFilter(filter);
        }

    }

    /**
     * Create a container filtered on the current Company.
     *
     * @param entityManager the entity manager
     * @param entityClass   the entity class
     * @param batchSize     the paging size
     */
    public ELazyContainer(EntityManager entityManager, Class entityClass, int batchSize) {
        this(entityManager, entityClass, batchSize, CompanySessionLib.getCompany());
    }

    /**
     * Create a container with a default paging size.
     *
     * @param entityManager the entity manager
     * @param entityClass   the entity class
     * @param company       the company on which to filter
     */
    public ELazyContainer(EntityManager entityManager, Class entityClass, BaseCompany company) {
        this(entityManager, entityClass, 1000, company);
    }

    /**
     * Create a container with a default paging size and filtered on the current company.
     *
     * @param entityManager the entity manager
     * @param entityClass   the entity class
     */
    public ELazyContainer(EntityManager entityManager, Class entityClass) {
        this(entityManager, entityClass, CompanySessionLib.getCompany());
    }

    private static Filter createCompanyFilter() {
        Filter filter=null;
        if (company!=null){
            filter = new Compare.Equal(CompanyEntity_.company.getName(), company);
        }
        return filter;
    }

    public void setFilter(BaseCompany company) {
        ELazyContainer.company=company;
        // add the company filter
        super.removeAllContainerFilters();
        Filter filter = createCompanyFilter();
        if (filter!=null){
            addContainerFilter(filter);
        }
    }


    /**
     * Restores the company filter after removing all the filters
     */
    @Override
    public void removeAllContainerFilters() {
        super.removeAllContainerFilters();
        Filter filter = createCompanyFilter();
        if(filter!=null){
            addContainerFilter(filter);
        }
    }



}
