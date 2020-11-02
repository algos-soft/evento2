package it.algos.webbase.web.login;

import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.Notification;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.field.PasswordField;

/**
 * Created by alex on 04/08/16.
 */
public class ChangePasswordDialog extends ConfirmDialog{

    private String originalPassword;
    private PasswordField oldPassword;
    private PasswordField newPassword;
    private PasswordField repeatPassword;


    public ChangePasswordDialog(String originalPassword) {
        super(null);
        this.originalPassword=originalPassword;

        oldPassword=new PasswordField("Vecchia password");
        oldPassword.setRequired(true);
        newPassword=new PasswordField("Nuova password");
        newPassword.addValidator(new StringLengthValidator("La lunghezza minima è 3",3,null,false));
        repeatPassword=new PasswordField("Ripeti");

        addComponent(oldPassword);
        addComponent(newPassword);
        addComponent(repeatPassword);

        oldPassword.focus();


    }


    @Override
    protected void onConfirm() {

        try {
            newPassword.validate();
        }catch (Exception e){
            Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);
            return;
        }

        if(!oldPassword.getValue().equals(originalPassword)) {
            Notification.show("Password originale errata", Notification.Type.ERROR_MESSAGE);
            return;
        }

        if(!newPassword.getValue().equals(repeatPassword.getValue())) {
            Notification.show("Password ripetuta in modo diverso", Notification.Type.ERROR_MESSAGE);
            return;
        }

        super.onConfirm();

    }

    public String getNewPassword(){
        return newPassword.getValue();
    }

}
