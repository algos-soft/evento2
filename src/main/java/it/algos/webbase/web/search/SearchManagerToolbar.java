package it.algos.webbase.web.search;

import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import it.algos.webbase.web.dialog.DialogToolbar;
import it.algos.webbase.web.field.CheckBoxField;

/**
 * Special toolbar with search options
 */
public class SearchManagerToolbar extends DialogToolbar {
    private CheckBoxField checkAdd;
    private CheckBoxField checkRemove;
    private CheckBoxField checkInside;


    @Override
    protected void addComponents() {
        createSearchOptionFields();
        Component optionsComp=getSearchOptionsComponent();
        addComponent(optionsComp);
        Label spacer = new Label();
        addComponent(spacer);

    }

    /**
     * Creates the fields for the basic search options
     * (checkboxes)
     */
    private void createSearchOptionFields(){
        checkAdd = new CheckBoxField(SearchManager.CombineSearchOptions.addToList.getText());
        checkAdd.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (checkAdd.getValue()) {
                    checkRemove.setValue(false);
                    checkInside.setValue(false);
                }
            }
        });

        checkRemove = new CheckBoxField(SearchManager.CombineSearchOptions.removeFromList.getText());
        checkRemove.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (checkRemove.getValue()) {
                    checkAdd.setValue(false);
                    checkInside.setValue(false);
                }
            }
        });

        checkInside = new CheckBoxField(SearchManager.CombineSearchOptions.searchInList.getText());
        checkInside.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (checkInside.getValue()) {
                    checkAdd.setValue(false);
                    checkRemove.setValue(false);
                }
            }
        });
    }

    /**
     * @returns the component with the standard search options
     * */
    protected Component getSearchOptionsComponent(){
        // layout containing search options
        VerticalLayout layout = new VerticalLayout();
        layout.addComponent(checkAdd);
        layout.addComponent(checkRemove);
        layout.addComponent(checkInside);
        return layout;
    }

    /**
     * Returns the combine search option selected, or null if none selected.
     *
     * @return the combine search option
     */
    SearchManager.CombineSearchOptions getCombineOption() {
        SearchManager.CombineSearchOptions option = null;
        if (checkAdd.getValue()) {
            option = SearchManager.CombineSearchOptions.addToList;
        }
        if (checkRemove.getValue()) {
            option = SearchManager.CombineSearchOptions.removeFromList;
        }
        if (checkInside.getValue()) {
            option = SearchManager.CombineSearchOptions.searchInList;
        }

        return option;
    }

}
