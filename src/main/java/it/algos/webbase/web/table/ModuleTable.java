package it.algos.webbase.web.table;

import com.vaadin.data.Container;
import com.vaadin.event.Action;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.entity.BaseEntity_;
import it.algos.webbase.web.module.ModulePop;
import org.vaadin.addons.lazyquerycontainer.LazyEntityContainer;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Attribute;
import java.util.logging.Logger;

/**
 * Standard table used by a Module
 */
public class ModuleTable extends ATable{
    private final static Logger logger = Logger.getLogger(ModuleTable.class.getName());

    protected ModulePop module;

    /**
     * Creates a new table for a given module.
     *
     * @param module the Module
     */
    public ModuleTable(ModulePop module) {
        super(module.getEntityClass(), module.getEntityManager());
        this.module=module;
        init();
    }

    /**
     * Creates the container whit the filter
     * <p>
     *
     * @return the container
     */
    public Container createContainer(final Filter filter) {
        LazyEntityContainer entityContainer = new LazyEntityContainer<BaseEntity>(getEntityManager(), getEntityClass(), getContainerPageSize(), BaseEntity_.id.getName(), true, true, true);
        entityContainer.addContainerProperty(BaseEntity_.id.getName(), Long.class, 0L, true, true);
        entityContainer.addContainerFilter(filter);
        return entityContainer;
    }// end of method

    /**
     * Creates the container
     * <p>
     * The id property is added here by default.
     *
     * @return the container
     */
    public Container createContainer() {
        LazyEntityContainer entityContainer = new LazyEntityContainer<BaseEntity>(getEntityManager(), getEntityClass(), getContainerPageSize(), BaseEntity_.id.getName(), true, true, true);
        entityContainer.addContainerProperty(BaseEntity_.id.getName(), Long.class, 0L, true, true);
        return entityContainer;
    }// end of method


    /**
     * Returns an array of the visible columns ids. Ids might be of type String
     * or Attribute.<br>
     * This implementations returns all the columns (no order).
     *
     * @return the list
     */
    @SuppressWarnings("rawtypes")
    protected Object[] getDisplayColumns() {
        Attribute[] fieldList = new Attribute[0];
        fieldList = getModule().getFieldsList();
        return fieldList;
    }


    public ModulePop getModule() {
        return module;
    }

    public EntityManager getEntityManager() {
        return getModule().getEntityManager();
    }

    /**
     * Handle the contextual Action
     */
    protected void handleAction(Action action, Object sender, Object target){
        if (action.equals(actionEdit)) {
            getModule().edit();
        }

        if (action.equals(actionDelete)) {
            getModule().delete();
        }
    }




}
