package it.algos.webbase.web.dialog;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class AlertDialog extends BaseDialog {


    public AlertDialog(String message) {
        this("", message);
    }// end of constructor


    public AlertDialog(String title, String message) {
        super(title, message);
        init();
    }// end of constructor

    private void init() {
        Button cancelButton = new Button("Chiudi");
        cancelButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                close();
            }// end of method
        });// end of anonymous inner class

        getToolbar().addComponent(cancelButton);
    }// end of method

}// end of class
