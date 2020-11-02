package it.algos.webbase.domain.utente;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Field;
import com.vaadin.ui.PasswordField;
import it.algos.webbase.web.field.CheckBoxField;
import it.algos.webbase.web.field.TextField;
import it.algos.webbase.web.form.AForm;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.lib.Lib;
import it.algos.webbase.web.lib.LibCrypto;
import it.algos.webbase.web.module.ModulePop;

import java.util.LinkedHashMap;

/**
 * Created by alex on 29-09-2015.
 * .
 */
public class UtenteForm extends ModuleForm {


    private static final String FIELD_NOME_LABEL = "Nome";
    private static final String FIELD_PASSWORD_LABEL = "Password";
    private static final String FIELD_ABILITATO_LABEL = "Abilitato";


    public UtenteForm(Item item, ModulePop modulo) {
        super(item, modulo);
        doInit();
    }// end of constructor


    private void doInit() {
        setWidth("500px");

        // decrypt the password for the UI
        Property prop = getItem().getItemProperty(Utente_.password.getName());
        String plainpass=Lib.getString(prop.getValue());
        getItem().getItemProperty(Utente_.password.getName()).setValue(plainpass);

    }

    /**
     * Populate the map to bind item properties to fields.
     * <p>
     * Crea e aggiunge i campi.<br>
     * Implementazione di default nella superclasse.<br>
     * I campi vengono recuperati dal Modello.<br>
     * I campi vengono creati del tipo grafico previsto nella Entity.<br>
     * Se si vuole aggiungere un campo (solo nel form e non nel Modello),<br>
     * usare il metodo sovrascritto nella sottoclasse
     * invocando prima (o dopo) il metodo della superclasse.
     * Se si vuole un layout completamente diverso sovrascrivere
     * senza invocare il metodo della superclasse
     */
    @Override
    public void createFields() {
        Field field;

        field = new TextField(FIELD_NOME_LABEL);
        addField(Utente_.nickname, field);

        //@todo Questo form viene visto solo dal developer - se l'utente gli chiede la password dimenticata, come fa a leggerla?
        //@todo l'admin non sa le password degli utenti, se l'utente la dimentica se la cambia da solo o chiede all'admin di resettarla - alex
        field = new TextField(FIELD_PASSWORD_LABEL);
        addField(Utente_.password, field);

        field = new CheckBoxField(FIELD_ABILITATO_LABEL);
        addField(Utente_.enabled, field);

    }// end of method




//    @Override
//    protected boolean onPreSave(FieldGroup fields) {
//        Field pField = fields.getField(Utente_.password.getName());
//        String pass = Lib.getString(pField.getValue());
//        pass= LibCrypto.encrypt(pass);
//        pField.setValue(pass);
//        return super.onPreSave(fields);
//    }

}// end of class
