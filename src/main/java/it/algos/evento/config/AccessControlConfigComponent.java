package it.algos.evento.config;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import it.algos.evento.entities.company.Company;
import it.algos.evento.pref.EventoPrefs;
import it.algos.webbase.web.field.RelatedComboField;
import it.algos.webbase.web.lib.Lib;

/**
 * Component for access control configuration
 */
@SuppressWarnings("serial")
public class AccessControlConfigComponent extends BaseConfigPanel implements View {

    private static final String KEY_AUTO_LOGIN = "userlogin";

    private RelatedComboField companyField;

    public AccessControlConfigComponent() {
        super();

        // crea e registra i fields
        creaFields();

        // crea la UI
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        String title;

        title = "<b>Gestione controllo accessi</b><p>";
        layout.addComponent(new Label(title, ContentMode.HTML));

        title = "Se è selezionata una azienda di auto-login, tutti gli utenti vengono loggati automaticamente su questa azienda (installazioni mono-azienda).<br>";
        title += "Altrimenti ogni utente deve effettuare il login e l'azienda viene determinata in base all'utente.<p>";
        title += "(Gli amministratori devono sempre effettuare il login).";

        layout.addComponent(companyField);
        layout.addComponent(new Label(title, ContentMode.HTML));

        addComponent(layout);
        addComponent(createButtonPanel());

    }

    // crea e registra i fields
    private void creaFields() {

        // popup azienda auto-login
        companyField = new RelatedComboField(Company.class, "Azienda di auto-login");

        // bind fields to properties
        getGroup().bind(companyField, KEY_AUTO_LOGIN);

    }


    @Override
    public Component getUIComponent() {
        return this;
    }

    @Override
    public String getTitle() {
        return "Controllo accessi";
    }


    public PrefSetItem createItem() {
        return new AccessControlSetItem();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        loadContent();
    }

    /**
     * Item containing form data
     */
    private class AccessControlSetItem extends PropertysetItem implements PrefSetItem {

        public AccessControlSetItem() {
            super();
            Long id = (long)EventoPrefs.autoLoginCompany.getInt();
            addItemProperty(KEY_AUTO_LOGIN, new ObjectProperty<Long>(id));

        }


        public void persist() {
            Object obj = getItemProperty(KEY_AUTO_LOGIN).getValue();
            int i = Lib.getInt(obj);
            EventoPrefs.autoLoginCompany.put(i);

        }

    }


}
