package it.algos.webbase.web.dialog;

import com.vaadin.ui.Notification;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.field.TextField;

/**
 * Created by gac on 19 ago 2015.
 * <p>
 * Dialogo per l'input di un parametro/property/variabile di tipo testo
 */
public class EditDialog extends ConfirmDialog {

    private static String DEFAULT_TITLE = "EditDialog";
    private static String DEFAULT_MESSAGE = "";
    private static String DEFAULT_CAPTION = "TextField";
    private EditListener listener;
    private TextField field;
    private String caption;

    /**
     * Costruttore.
     *
     * @param listener ascoltatore alla chiusura del dialogo
     */
    public EditDialog(EditListener listener) {
        this(DEFAULT_TITLE, listener);
    }// end of constructor

    /**
     * Costruttore.
     *
     * @param title    della finestra
     * @param listener ascoltatore alla chiusura del dialogo
     */
    public EditDialog(String title, EditListener listener) {
        this(title, DEFAULT_CAPTION, listener);
    }// end of constructor

    /**
     * Costruttore.
     *
     * @param title    della finestra
     * @param caption  del campo edit
     * @param listener ascoltatore alla chiusura del dialogo
     */
    public EditDialog(String title, String caption, EditListener listener) {
        this(title, DEFAULT_MESSAGE, caption, listener);
    }// end of constructor

    /**
     * Costruttore completo.
     *
     * @param title    della finestra
     * @param message  eventale spiegazione di dettaglio
     * @param caption  del campo edit
     * @param listener ascoltatore alla chiusura del dialogo
     */
    public EditDialog(String title, String message, String caption, EditListener listener) {
        super(title, message, null);
        this.setCaption(caption);
        this.setEditListener(listener);
        this.initGUI();
    }// end of constructor


    /**
     * Creazione grafica del dialogo
     * Crea il campo edit.
     */
    @SuppressWarnings("rawtypes")
    private void initGUI() {
        field = new TextField();
        field.setCaption(caption);
        field.setRequired(true);
        addComponent(field);
    }// end of method

    @Override
    protected void onCancel() {
        if (listener != null) {
            listener.onClose();
        }// fine del blocco if
        close();
    }// end of method

    @Override
    protected void onConfirm() {
        String value = field.getValue();

        if (value.equals("")) {
            new Notification("ATTENZIONE", "È necessario inserire un valore", Notification.TYPE_ERROR_MESSAGE, true).show(com.vaadin.server.Page.getCurrent());
        } else {
            if (listener != null) {
                listener.onClose(field.getValue());
            }// fine del blocco if
            close();
        }// fine del blocco if-else
    }// end of method

    public void setCaption(String caption) {
        this.caption = caption;
    }// end of method

    public void setEditListener(EditListener listener) {
        this.listener = listener;
    }// end of method

    public TextField getField() {
        return field;
    }// end of getter method

    public interface EditListener {
        public void onClose();

        public void onClose(String field);
    }// end of method

}// end of class
