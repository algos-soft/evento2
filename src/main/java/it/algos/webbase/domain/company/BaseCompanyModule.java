package it.algos.webbase.domain.company;

import com.vaadin.data.Item;
import com.vaadin.navigator.View;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.query.AQuery;

import javax.persistence.metamodel.Attribute;

@SuppressWarnings("serial")
public class BaseCompanyModule extends ModulePop implements View {


    public BaseCompanyModule() {
        this(BaseCompany.class);
    }// end of constructor


    public BaseCompanyModule(Class entityClass) {
        super(entityClass);
    }// end of constructor


    /**
     * Crea i campi visibili
     * <p>
     * Come default spazzola tutti i campi della Entity <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     * NON garantisce l'ordine con cui vengono presentati i campi nella scheda <br>
     * Può mostrare anche il campo ID, oppure no <br>
     * Se si vuole differenziare tra Table, Form e Search, <br>
     * sovrascrivere creaFieldsList, creaFieldsForm e creaFieldsSearch <br>
     */
    @Override
    protected Attribute<?, ?>[] creaFieldsAll() {
        return new Attribute[]{
                BaseCompany_.companyCode,
                BaseCompany_.name,
                BaseCompany_.address1,
                BaseCompany_.email,
                BaseCompany_.contact,
                BaseCompany_.contractType,
                BaseCompany_.contractStart,
                BaseCompany_.contractEnd,
        };
    }// end of method

    /**
     * Returns the form used to edit an item. <br>
     * The concrete subclass must override for a specific Form.
     *
     * @return the Form
     */
    public ModuleForm createForm(Item item) {
        ModuleForm form = new ModuleForm(item, this);

        form.setWidth("500px");

        return form;
    }// end of method

//    @Override
//    public ModuleForm createForm(Item item) {
//        return (new BaseCompanyForm(this, item));
//    }// end of method


    public void delete(Object id) {

        // cancella prima tutti i dati
        BaseCompany company = (BaseCompany) AQuery.find(BaseCompany.class, (Long)id);
        company.deleteAllData(null);

        // poi cancella la company
        super.delete(id);

    }// end of method


}// end of class
