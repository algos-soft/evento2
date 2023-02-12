package it.algos.webbase.web.dialog;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 * A modal alert dialog with a message and no buttons
 */
@SuppressWarnings("serial")
public class AlertDialog extends BaseDialog {


    public AlertDialog(String message) {
        this("", message);
    }

    public AlertDialog(String title, String message) {
        super(title, message);
        init();
    }

    private void init() {
    }

    @Override
    protected DialogToolbar createToolbarComponent() {
        return null;
    }
}
