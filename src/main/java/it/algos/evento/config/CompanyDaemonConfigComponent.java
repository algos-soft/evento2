package it.algos.evento.config;

import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import it.algos.evento.pref.CompanyPrefs;
import it.algos.webbase.web.field.ArrayComboField;
import it.algos.webbase.web.field.CheckBoxField;

@SuppressWarnings("serial")
public class CompanyDaemonConfigComponent extends BaseConfigPanel {

    private static final String KEY_SERVICE_ACTIVE = "serviceactive";
    private static final String KEY_SERVICE_TIME = "servicetime";

    private static final String KEY_CHECK_MAIL_SCAD_PREN = "checkMailScadPren";
    private static final String KEY_CHECK_MAIL_SCAD_PAGA = "checkMailScadPaga";

    private static final String KEY_REF_MAIL_SCAD_PREN = "checkRefScadPren";
    private static final String KEY_REF_MAIL_SCAD_PAGA = "checkRefScadPaga";

    private static final String KEY_SCUOLA_MAIL_SCAD_PREN = "checkScuolaScadPren";
    private static final String KEY_SCUOLA_MAIL_SCAD_PAGA = "checkScuolaScadPaga";

    private static final String KEY_NP_MAIL_SCAD_PREN = "checkNPScadPren";
    private static final String KEY_NP_MAIL_SCAD_PAGA = "checkNPScadPaga";


    private ArrayComboField hourField;
    private Field<?> serviceField;
    private String[] aHours;

    private CheckBoxField checkMailScadPrenField;
    private CheckBoxField checkMailScadPagaField;
    private CheckBoxField checkRefScadPrenField;
    private CheckBoxField checkRefScadPagaField;
    private CheckBoxField checkScuolaScadPrenField;
    private CheckBoxField checkScuolaScadPagaField;
    private CheckBoxField checkNPScadPrenField;
    private CheckBoxField checkNPScadPagaField;


    public CompanyDaemonConfigComponent() {
        super();

        // crea e registra i fields
        creaFields();

        // crea la UI

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        String title = "<b>Servizio di controllo giornaliero delle posizioni scadute</b><p>";
        title += "Ogni giorno, all'orario prestabilito, effettua il controllo delle <br>"
                + "posizioni scadute e invia automaticamente le email di sollecito.";
        Label infoLabel = new Label(title, ContentMode.HTML);
        layout.addComponent(infoLabel);
        layout.addComponent(serviceField);
        layout.addComponent(hourField);

        layout.addComponent(creaComponenteChecks());

        addComponent(layout);
        addComponent(createButtonPanel());

    }

    // crea e registra i fields
    private void creaFields() {

        // check box servizio attivo
        serviceField = new CheckBoxField("Attiva il servizio");

        // combo ora del controllo
        aHours = new String[]{"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",
                "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
        hourField = new ArrayComboField(aHours, "Esegui controllo alle ore:");


        checkMailScadPrenField = new CheckBoxField("Alla scadenza di una prenotazione");
        checkMailScadPrenField.setDescription("Invia una email di promemoria alla scadenza della prenotazione");
        checkMailScadPrenField.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                boolean b = (Boolean) checkMailScadPrenField.getValue();
                checkRefScadPrenField.setVisible(b);
                checkScuolaScadPrenField.setVisible(b);
                checkNPScadPrenField.setVisible(checkRefScadPrenField.getValue() & b);
            }
        });


        checkMailScadPagaField = new CheckBoxField("Alla scadenza dei termini di pagamento");
        checkMailScadPagaField.setDescription("Invia una email quando scadono i termini di pagamento");
        checkMailScadPagaField.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                boolean b = (Boolean) checkMailScadPagaField.getValue();
                checkRefScadPagaField.setVisible(b);
                checkScuolaScadPagaField.setVisible(b);
                checkNPScadPagaField.setVisible(checkRefScadPagaField.getValue() & b);
            }
        });

        String tooltip = "Invia la mail alla scuola";
        checkScuolaScadPrenField = new CheckBoxField();
        checkScuolaScadPrenField.setDescription(tooltip);

        checkScuolaScadPagaField = new CheckBoxField();
        checkScuolaScadPagaField.setDescription(tooltip);

        tooltip = "Invia la mail al referente";
        checkRefScadPrenField = new CheckBoxField();
        checkRefScadPrenField.setDescription(tooltip);
        checkRefScadPrenField.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                boolean b = (Boolean) checkRefScadPrenField.getValue();
                checkNPScadPrenField.setVisible(b);
            }
        });

        checkRefScadPagaField = new CheckBoxField();
        checkRefScadPagaField.setDescription(tooltip);
        checkRefScadPagaField.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                boolean b = (Boolean) checkRefScadPagaField.getValue();
                checkNPScadPagaField.setVisible(b);
            }
        });

        checkNPScadPrenField = new CheckBoxField();
        checkNPScadPrenField.setDescription(tooltip);

        checkNPScadPagaField = new CheckBoxField();
        checkNPScadPagaField.setDescription(tooltip);

        // bind fields to properties
        getGroup().bind(checkMailScadPrenField, KEY_CHECK_MAIL_SCAD_PREN);
        getGroup().bind(checkMailScadPagaField, KEY_CHECK_MAIL_SCAD_PAGA);
        getGroup().bind(checkRefScadPrenField, KEY_REF_MAIL_SCAD_PREN);
        getGroup().bind(checkRefScadPagaField, KEY_REF_MAIL_SCAD_PAGA);

        getGroup().bind(checkScuolaScadPagaField, KEY_SCUOLA_MAIL_SCAD_PAGA);
        getGroup().bind(checkScuolaScadPrenField, KEY_SCUOLA_MAIL_SCAD_PREN);

        getGroup().bind(checkNPScadPrenField, KEY_NP_MAIL_SCAD_PREN);
        getGroup().bind(checkNPScadPagaField, KEY_NP_MAIL_SCAD_PAGA);


        // bind fields to properties
        getGroup().bind(serviceField, KEY_SERVICE_ACTIVE);
        getGroup().bind(hourField, KEY_SERVICE_TIME);

    }


    @Override
    public Component getUIComponent() {
        return this;
    }

    @Override
    public String getTitle() {
        return "Programmazione controlli automatici";
    }


    public PrefSetItem createItem() {
        return new DaemonSetItem();
    }

    /**
     * Item containing form data
     */
    private class DaemonSetItem extends PropertysetItem implements PrefSetItem {

        public DaemonSetItem() {
            super();

            addItemProperty(KEY_SERVICE_ACTIVE, new ObjectProperty<Boolean>(CompanyPrefs.doRunSolleciti.getBool()));

            int nOra = CompanyPrefs.oraRunSolleciti.getInt();
            String sOra = "";
            if (nOra >= 0) {
                sOra = aHours[nOra];
            }
            addItemProperty(KEY_SERVICE_TIME, new ObjectProperty<String>(sOra));

            addItemProperty(KEY_CHECK_MAIL_SCAD_PREN, new ObjectProperty<Boolean>(CompanyPrefs.sendMailScadPren.getBool()));
            addItemProperty(KEY_CHECK_MAIL_SCAD_PAGA, new ObjectProperty<Boolean>(CompanyPrefs.sendMailScadPaga.getBool()));
            addItemProperty(KEY_REF_MAIL_SCAD_PREN, new ObjectProperty<Boolean>(CompanyPrefs.sendMailScadPrenRef.getBool()));
            addItemProperty(KEY_REF_MAIL_SCAD_PAGA, new ObjectProperty<Boolean>(CompanyPrefs.sendMailScadPagaRef.getBool()));
            addItemProperty(KEY_SCUOLA_MAIL_SCAD_PREN, new ObjectProperty<Boolean>(CompanyPrefs.sendMailScadPrenScuola.getBool()));
            addItemProperty(KEY_SCUOLA_MAIL_SCAD_PAGA, new ObjectProperty<Boolean>(CompanyPrefs.sendMailScadPagaScuola.getBool()));
            addItemProperty(KEY_NP_MAIL_SCAD_PREN, new ObjectProperty<Boolean>(CompanyPrefs.sendMailScadPrenNP.getBool()));
            addItemProperty(KEY_NP_MAIL_SCAD_PAGA, new ObjectProperty<Boolean>(CompanyPrefs.sendMailScadPagaNP.getBool()));

        }


        /**
         * Ritorna l'ora registrata nel'iten come int
         * <p>
         *
         * @return l'ora come int, -1 se non selezionata
         */
        private int getIntHour() {
            int nOra = -1;
            Object obj = getItemProperty(KEY_SERVICE_TIME).getValue();
            if (obj != null) {
                String sOra = obj.toString();
                nOra = Integer.parseInt(sOra);
            }
            return nOra;
        }


        public void persist() {
            Object obj;
            boolean cont = true;

            // se il servizio è attivo ci deve essere l'ora
            boolean serviceactive = (boolean) getItemProperty(KEY_SERVICE_ACTIVE).getValue();
            if (serviceactive) {
                int servicetime = getIntHour();
                if (servicetime < 0) {
                    Notification.show("Inserire l'orario di esecuzione.");
                    cont = false;
                }
            }

            if (cont) {

                obj = getItemProperty(KEY_SERVICE_ACTIVE).getValue();
                CompanyPrefs.doRunSolleciti.put(obj);

                obj = getItemProperty(KEY_SERVICE_TIME).getValue();
                CompanyPrefs.oraRunSolleciti.put(getIntHour());

                obj = getItemProperty(KEY_CHECK_MAIL_SCAD_PREN).getValue();
                CompanyPrefs.sendMailScadPren.put(obj);
                obj = getItemProperty(KEY_CHECK_MAIL_SCAD_PAGA).getValue();
                CompanyPrefs.sendMailScadPaga.put(obj);

                obj = getItemProperty(KEY_REF_MAIL_SCAD_PREN).getValue();
                CompanyPrefs.sendMailScadPrenRef.put(obj);
                obj = getItemProperty(KEY_REF_MAIL_SCAD_PAGA).getValue();
                CompanyPrefs.sendMailScadPagaRef.put(obj);

                obj = getItemProperty(KEY_SCUOLA_MAIL_SCAD_PREN).getValue();
                CompanyPrefs.sendMailScadPrenScuola.put(obj);
                obj = getItemProperty(KEY_SCUOLA_MAIL_SCAD_PAGA).getValue();
                CompanyPrefs.sendMailScadPagaScuola.put(obj);

                obj = getItemProperty(KEY_NP_MAIL_SCAD_PREN).getValue();
                CompanyPrefs.sendMailScadPrenNP.put(obj);
                obj = getItemProperty(KEY_NP_MAIL_SCAD_PAGA).getValue();
                CompanyPrefs.sendMailScadPagaNP.put(obj);

            }

        }

    }




    // Crea il GridLayout con i check boxes di abilitazione delle varie spedizioni
    private Component creaComponenteChecks() {
        Component comp;
        GridLayout layout = new GridLayout(4, 8);

		layout.setSpacing(true);

		Alignment align=Alignment.MIDDLE_LEFT;

		comp = new Label("<strong>Invio automatico email di sollecito</strong>", ContentMode.HTML);
		layout.addComponent(comp, 0, 0);
		layout.setComponentAlignment(comp, align);
		layout.addComponent(checkMailScadPrenField, 0, 2);
		layout.addComponent(checkMailScadPagaField,0,4);

		comp = new Label("Alla scuola");
		comp.setWidth("80px");
		layout.addComponent(comp, 1, 0);
		layout.setComponentAlignment(comp, align);
		layout.addComponent(checkScuolaScadPrenField, 1, 2);
		layout.setComponentAlignment(checkScuolaScadPrenField, align);
		layout.addComponent(checkScuolaScadPagaField, 1, 4);
		layout.setComponentAlignment(checkScuolaScadPagaField, align);

		comp = new Label("Al referente");
		comp.setWidth("80px");
		layout.addComponent(comp, 2, 0);
		layout.setComponentAlignment(comp, align);
		layout.addComponent(checkRefScadPrenField, 2, 2);
		layout.setComponentAlignment(checkRefScadPrenField, align);
		layout.addComponent(checkRefScadPagaField, 2, 4);
		layout.setComponentAlignment(checkRefScadPagaField, align);

		comp=new Label("Non inviare a privati");
		comp.setWidth("100px");
		layout.addComponent(comp, 3, 0);
		layout.setComponentAlignment(comp, align);
		layout.addComponent(checkNPScadPrenField, 3, 2);
		layout.setComponentAlignment(checkNPScadPrenField, align);
		layout.addComponent(checkNPScadPagaField, 3, 4);
		layout.setComponentAlignment(checkNPScadPagaField, align);


        return layout;
    }


}
