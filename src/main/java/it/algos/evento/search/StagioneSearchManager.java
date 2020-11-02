package it.algos.evento.search;

import com.vaadin.data.Container;
import it.algos.webbase.multiazienda.ESearchManager;
import it.algos.webbase.web.dialog.DialogToolbar;

import java.util.ArrayList;

/**
 * Search Manager con funzionalità di filtraggio per Stagione
 * Created by Alex on 02/06/15.
 */
public class StagioneSearchManager extends ESearchManager {

    public StagioneSearchManager() {
        setStagioneCorrente(true);
    }

    /**
     * The component shown in the toolbar area.
     */
    protected DialogToolbar createToolbarComponent() {
        StagioneSearchManagerToolbar toolbar = new StagioneSearchManagerToolbar();
        toolbar.setChecboxChahgedListener(new StagioneSearchManagerToolbar.CheckBoxChangedListener() {
            @Override
            public void checkBoxChanged(boolean newValue) {
                checkStagioneChanged(newValue);
            }
        });
        return toolbar;
    }// end of method

    /**
     * Creates and adds the filters for each search field. Invoked before performing the search.
     *
     * @return an array of filters which will be concatenated with the And clause
     */
    public ArrayList<Container.Filter> createFilters() {
        // ArrayList<Filter> filters = new ArrayList<Filter>();
        // filters.add(createStringFilter(new TextField("Abcd"), Sala_.nome, SearchType.CONTAINS));
        // filters.add(createStringFilter(new TextField("pippox"), Sala_.capienza, SearchType.MATCHES));
        // return filters;
        return null;
    }// end of method

    /**
     * @return true se il check Solo stagione corrente è spuntato
     */
    public boolean isStagioneCorrente(){
        return getStagioneSearchToolbar().isStagioneCorrente();
    }


    /**
     * Assegna il valore al check "Solo stagione corrente"
     * @param flag il valore da assegnare
     */
    public void setStagioneCorrente(boolean flag){
        getStagioneSearchToolbar().setStagioneCorrente(flag);
    }

    private StagioneSearchManagerToolbar getStagioneSearchToolbar(){
        return (StagioneSearchManagerToolbar)getSearchToolbar();
    }

    /**
     * Invocato quando il checkbox "Solo stagione corrente" cambia
     * @param newValue il vuovo valore
     */
    public void checkStagioneChanged(boolean newValue){
    }


}
