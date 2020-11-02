package it.algos.evento.entities.company;

import com.vaadin.data.Item;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.Layout;
import it.algos.webbase.domain.company.BaseCompanyForm;
import it.algos.webbase.web.field.CheckBoxField;
import it.algos.webbase.web.field.TextField;
import it.algos.webbase.web.module.ModulePop;

import java.util.ArrayList;

/**
 * Form to create a new Company.
 * Allows to input additional data
 * needed for full activation.
 */
public class ActivateCompanyForm extends BaseCompanyForm {

    private TextField passwordField;
    private CheckBoxField createDataField;

    public ActivateCompanyForm(ModulePop modulo, Item item) {
        super(modulo, item);
    }

    protected void addComponents(Layout layout){
        super.addComponents(layout);

        passwordField = new TextField("Password");
        passwordField.addValidator(new StringLengthValidator("La password è obbligatoria (min 4 caratteri)", 4, null, false));

        createDataField = new CheckBoxField("Crea dati demo");
        createDataField.setValue(true);

        layout.addComponent(passwordField);
        layout.addComponent(createDataField);
    }

    @Override
    protected ArrayList<String> isValid() {
        ArrayList<String> reasons = super.isValid();
        if(passwordField.isEmpty()){
            reasons.add("La password è obbligatoria (min 4 caratteri)");
        }
        return reasons;
    }

    public String getPassword(){
        return passwordField.getValue();
    }

    public boolean isCreateData(){
        return createDataField.getValue();
    }


}
