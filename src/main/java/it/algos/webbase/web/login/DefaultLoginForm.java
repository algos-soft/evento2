package it.algos.webbase.web.login;

import com.vaadin.ui.Component;
import it.algos.webbase.domain.utente.Utente;
import it.algos.webbase.web.field.TextField;

/**
 * Created by alex on 26/05/16.
 */
public class DefaultLoginForm extends AbsLoginForm {

    private TextField nameField;


    public DefaultLoginForm() {
        getPassField().setWidth("15em");
    }

    /**
     * @return the selected user
     */
    public UserIF getSelectedUser(){
        String nome = nameField.getValue();
        UserIF user = Utente.read(nome);
        return user;
    }

    /**
     * Create the component to input the username.
     * @return the username component
     */
    public Component createUsernameComponent(){
        nameField = new TextField("Username");
//        nameField.setWidthUndefined();
        nameField.setWidth("15em");
        return nameField;
    }

    public void setUsername(String name) {
        nameField.setValue(name);
    }



}
