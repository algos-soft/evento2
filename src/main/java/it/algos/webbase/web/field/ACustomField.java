package it.algos.webbase.web.field;

import com.vaadin.ui.CustomField;

/**
 * Abstract class adding some functionalities to Vaadin's CustomField
 * Created by alex on 8-02-2016.
 */
public abstract class ACustomField<T> extends CustomField<T> {

    /**
     * Routes the request to the custom component
     */
    public void addStyleName(String style){
        getContent().addStyleName(style);
    }

    /**
     * Routes the request to the custom component
     */
    public void removeStyleName(String style){
        getContent().removeStyleName(style);
    }

    /**
     * Routes the request to the custom component
     */
    public void setStyleName(String style){
        getContent().setStyleName(style);
    }

}
