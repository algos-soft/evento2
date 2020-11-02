package it.algos.webbase.web.form;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Field;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.lib.LibField;
import it.algos.webbase.web.module.ModulePop;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Attribute;

/**
 * A Form for creating and editing records in a Module.
 * Created by alex on 06/01/16.
 */
public class ModuleForm extends AForm {

    private ModulePop module;
    private boolean newRecord; // true if the form is editing a new record.

    public ModuleForm(Item item, ModulePop module) {
        super(item);
        this.module = module;
        init();
    }

    @Override
    protected void init() {

        Item item = getItem();

        // if the item exists and it is a bean item, refresh the entity
        // from the persistence layer before displaying data
        if (item != null && item instanceof BeanItem) {
            BeanItem bi = (BeanItem) item;
            BaseEntity entity = (BaseEntity) bi.getBean();
            EntityManager em = getEntityManager();
            if (em != null) {
                em.refresh(entity);
            }
        }

        // if the item is not present, then it is a new record.
        // create a temporary BeanItem of the proper class
        if (item == null) {
            Object bean = BaseEntity.createBean(getModule().getEntityClass());
            setItem(new BeanItem(bean));
            getBinder().setItemDataSource(getItem());   // link the binder to the new item before adding the fields
            this.newRecord = true;
        }

        super.init();
    }

    //

    /**
     * Create and add a field for each property declared for this form
     * <p>
     * Crea e aggiunge i campi.<br>
     * Implementazione di default nella superclasse.<br>
     * I campi vengono recuperati dal Modello (di default) <br>
     * I campi vengono creati del tipo grafico previsto nella Entity.<br>
     * Se si vuole aggiungere un campo (solo nel form e non nel Modello),<br>
     * usare il metodo sovrascritto nella sottoclasse
     * invocando prima (o dopo) il metodo della superclasse.
     * Se si vuole un layout completamente diverso sovrascrivere
     * senza invocare il metodo della superclasse
     */
    public void createFields() {
        Field field;
        Attribute[] attributes = this.getAttributesList();

        for (Attribute attr : attributes) {
            field = LibField.createField(attr);
            if (field != null) {
                addField(attr, field);
            }// end of if cycle
        }// end of for cycle

    }// end of method

    /**
     * Attributes used in this form
     * Di default prende dal modulo
     * Può essere sovrascritto se c'è un Form specifico
     *
     * @return a list containing all the attributes used in this form
     */
    protected Attribute<?, ?>[] getAttributesList() {
        return this.getModule().getFieldsForm();
    }// end of method

    @Override
    public void postCommit() {

        // merge the bean (creates or updates the record(s) in the db)
        EntityManager em = getEntityManager();
        if (em != null) {
            BaseEntity entity = getEntity();
            entity = getEntity().save(em);
        }

    }


    public ModulePop getModule() {
        return module;
    }

    public boolean isNewRecord() {
        return newRecord;
    }

    public EntityManager getEntityManager() {
        EntityManager em = null;
        ModulePop module = getModule();
        if (module != null) {
            em = module.getEntityManager();
        }
        return em;
    }

}// end of class
