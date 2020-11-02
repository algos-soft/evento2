package it.algos.webbase.web.login;

import com.vaadin.ui.Button;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.field.TextField;


/**
 * Abstract User Profile form
 * Created by alex on 04/08/16.
 */
public class DefaultUserProfileForm extends AbsUserProfileForm {

    TextField usernameField;

    public DefaultUserProfileForm() {
        usernameField = new TextField("Username");
        addComponent(usernameField);

        Button button = new Button("Cambia password");
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                changePassword();
            }
        });
        addComponent(button);

    }

    @Override
    public void setUser(UserIF user) {
        usernameField.setValue(user.getNickname());
    }

}
