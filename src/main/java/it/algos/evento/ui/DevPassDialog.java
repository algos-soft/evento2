package it.algos.evento.ui;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.VerticalLayout;
import it.algos.webbase.web.dialog.ConfirmDialog;

/**
 * Dialog to request and validate the developer password.
 */
public class DevPassDialog extends ConfirmDialog{

    private PasswordField password;

    public DevPassDialog(Listener closeListener) {
        super(closeListener);

        setTitle("Accesso Developer");

    }

    /**
     * The component shown in the detail area.
     */
    protected VerticalLayout createDetailComponent() {

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setMargin(true);
        layout.setSizeUndefined();

        // Create the password input field
        password = new PasswordField("Developer password:");
        password.setWidth("300px");
        password.setValue("");
        password.setNullRepresentation("");

        layout.addComponent(password);

        return layout;
    }


    @Override
    protected void onConfirm() {
        String pass = password.getValue();
        if(pass.equals("otneve")){
            super.onConfirm();
        }else{
            new Notification("Password non valida").show(Page.getCurrent());
        }
    }
}
