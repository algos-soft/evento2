package it.algos.webbase.web.login;

import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;
import it.algos.webbase.web.dialog.ConfirmDialog;

/**
 * Abstract User Profile form
 * Created by alex on 04/08/16.
 */
public abstract class AbsUserProfileForm extends ConfirmDialog {

    private UserIF user;

    public AbsUserProfileForm() {
        super(null);
    }

    /**
     * Fills the fields in the form with the data from a user
     *
     * @param user the user
     */
    public abstract void setUser(UserIF user);

    public UserIF getUser() {
        return user;
    }

    public Window getWindow() {
        return this;
    }

    @Override
    protected void onConfirm() {
        super.onConfirm();
    }

    /**
     * Starts the procedure to change the password
     */
    public void changePassword() {
        String password = getUser().getPassword();
        ChangePasswordDialog changeDial = new ChangePasswordDialog(password);
        changeDial.setConfirmListener(new ConfirmListener() {
            @Override
            public void confirmed(ConfirmDialog dialog) {
                String newPass = changeDial.getNewPassword();
                getUser().setPassword(newPass);
                getUser().save();
                Notification.show("Password modificata");
            }
        });
        changeDial.show();


    }
}
