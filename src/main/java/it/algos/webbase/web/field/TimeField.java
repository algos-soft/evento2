package it.algos.webbase.web.field;

import com.vaadin.shared.ui.datefield.Resolution;

import java.util.Date;

/**
 * Created by gac on 16 set 2015.
 * .
 */
public class TimeField extends DateField {

    public TimeField() {
        this("");
    }// end of constructor

    public TimeField(String caption) {
        this(caption, null);
    }// end of constructor

    public TimeField(String caption, Date value) {
        super(caption, value);
        this.setResolution(Resolution.SECOND);
    }// end of constructor

}// end of class
