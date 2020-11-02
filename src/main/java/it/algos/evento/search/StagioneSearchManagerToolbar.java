package it.algos.evento.search;

import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import it.algos.webbase.web.field.CheckBoxField;
import it.algos.webbase.web.search.SearchManagerToolbar;

/**
 * Created by Alex on 02/06/15.
 */
public class StagioneSearchManagerToolbar extends SearchManagerToolbar {

    private CheckBoxField checkStagione;
    private CheckBoxChangedListener checkboxChangedListener;

    /**
     * @returns the component with the standard search options
     * */
    protected Component getSearchOptionsComponent(){
        VerticalLayout layout=new VerticalLayout();
        Component baseOptions = super.getSearchOptionsComponent();
        checkStagione = new CheckBoxField("Solo stagione corrente");
        checkStagione.setDescription("Esegue le ricerche solo nella stagione corrente.<br>" +
                "(Puoi impostare la stagione corrente nella tabella Stagioni).");

        layout.addComponent(baseOptions);
        layout.addComponent(checkStagione);

        // ad a listener to the checkbox
        checkStagione.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                checkboxChangedListener.checkBoxChanged(checkStagione.getValue());
            }
        });

        return layout;
    }

    /**
     * @return true se il check Solo stagione corrente è spuntato
     */
    public boolean isStagioneCorrente() {
        return checkStagione.getValue();
    }

    /**
     * Assegna il valore al check "Solo stagione corrente"
     * @param flag il valore da assegnare
     */
    public void setStagioneCorrente(boolean flag) {
        checkStagione.setValue(flag);
    }

    public void setChecboxChahgedListener(CheckBoxChangedListener l){
        checkboxChangedListener=l;
    }

    public interface CheckBoxChangedListener{
        void checkBoxChanged(boolean newValue);
    }
}
