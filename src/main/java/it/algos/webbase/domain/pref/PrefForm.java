package it.algos.webbase.domain.pref;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.VerticalLayout;
import it.algos.webbase.web.field.*;
import it.algos.webbase.web.form.AFormLayout;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.module.ModulePop;

/**
 * Created by gac on 13 set 2015.
 * .
 */
public class PrefForm extends ModuleForm {

    private FormLayout extra; //usato per i fields aggiuntivi che vengono visualizzati a comando sotto gli altri
    private Field fValore;

    /**
     * The form used to edit an item.
     * <p>
     * Invoca la superclasse passando i parametri:
     *
     * @param item   singola istanza della classe (obbligatorio in modifica e nullo per newRecord)
     * @param module di riferimento (obbligatorio)
     */
    public PrefForm(Item item, ModulePop module) {
        super(item, module);
    }// end of constructor

    /**
     * Create the detail component (the upper part containing the fields).
     * <p>
     * Usa il FormLayout che ha le label a sinistra dei campi (standard)
     * Se si vogliono le label sopra i campi, sovrascivere questo metodo e usare un VerticalLayout
     *
     * @return the detail component containing the fields
     */
    protected Component createComponent() {
        VerticalLayout layout = new VerticalLayout();

        Component comp = creaFieldsLayoutStandard();
        layout.addComponent(comp);
        extra = creaFieldsLayoutExtra();
        if (extra != null) {
            layout.addComponent(extra);
        }// end of if cycle

        return layout;
    }// end of method

    protected Component creaFieldsLayoutStandard() {
        FormLayout layout = new AFormLayout();
        layout.setMargin(true);
        return super.creaCompDetail(layout);
    }// end of method

    protected FormLayout creaFieldsLayoutExtra() {
        FormLayout layout = new AFormLayout();
        layout.setMargin(true);
        return layout;
    }// end of method


    @Override
    protected void modificaFields() {
        Field field = this.getField(Pref_.tipo);
        PrefType type = null;

        if (field != null && field instanceof ArrayComboField) {
            field.setEnabled(isNewRecord());
            field.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent event) {
                    arrayComboBoxValueChanged(event);
                }// end of method
            });// end of anonymous class

            type = getPreferenza().getTipo();
            syncFieldValore(type);
        }// end of if cycle

    }// end of method


    /**
     * Intercetta i cambiamenti nel combobox.
     * <p>
     * Sovrascritto nella classe specifica
     */
    protected void arrayComboBoxValueChanged(Property.ValueChangeEvent event) {
        Property property = event.getProperty();
        Object objValue = property.getValue();
        PrefType type = null;

        if (objValue instanceof PrefType) {
            type = (PrefType) objValue;
        }// end of if cycle

        syncFieldValore(type);
    }// end of method

    /**
     * Regola il tipo di campo ed il valore.
     */
    protected void syncFieldValore(PrefType type) {
        if (fValore != null) {
            removeComponent(fValore);
            if (extra != null) {
                extra.removeAllComponents();
            }// end of if cycle
        }// end of if cycle

        if (type != null) {
            switch (type) {
                case string:
                    fValore = new TextField();
                    break;
                case bool:
                    fValore = new CheckBoxField();
                    break;
                case integer:
                    fValore = new IntegerField();
                    break;
                case date:
                    fValore = new DateField();
                    break;
                case email:
                    fValore = new EmailField();
                    break;
                case decimal:
                    fValore = new IntegerField();
                    break;
                default: // caso non definito
            } // fine del blocco switch
        }// end of if cycle

        if (fValore != null) {
            if (fValore instanceof CheckBoxField) {
                fValore.setCaption("Valore booleano true/false");
            } else {
                fValore.setCaption("Valore");
            }// end of if/else cycle

            extra.addComponent(fValore);
        }// end of if cycle

        if (fValore != null && !isNewRecord()) {
            Pref pref = getPreferenza();
            byte[] bytes = pref.getValue();
            Object value = type.bytesToObject(bytes);
            fValore.setValue(value);
        }// end of if cycle

    }// end of method


    @Override
    public void postCommit() {
        Pref pref = getPreferenza();
        Object objValue;

        if (fValore != null) {
            objValue = fValore.getValue();
            pref.setValore(objValue);
        }// end of if cycle

        super.postCommit();
    }// end of method

    /**
     * Restituisce la funzione di questo Form
     */
    private Pref getPreferenza() {
        return (Pref) super.getEntity();
    }// end of method

}// end of class

