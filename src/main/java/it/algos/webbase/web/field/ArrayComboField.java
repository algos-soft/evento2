package it.algos.webbase.web.field;

import com.vaadin.data.Item;
import com.vaadin.ui.ComboBox;

import java.util.List;

/**
 * Combo field driven by a Enum
 */
@SuppressWarnings("serial")
public class ArrayComboField extends ComboBox implements FieldInterface<Object> {

    private Object[] values;

    public ArrayComboField(List arrayValues) {
        this(arrayValues.toArray(), "");
    }// end of constructor

    public ArrayComboField(Object[] values) {
        this(values, "");
    }// end of constructor


    public ArrayComboField(List arrayValues,String caption) {
        this(arrayValues.toArray(), caption);
    }// end of constructor

    public ArrayComboField(Object[] values, String caption) {
        super(caption);
        this.values = values;
        init();
    }// end of constructor


    private void init() {
        initField();
        for (Object value : values) {
            Item item = addItem(value);
            setItemCaption(item, value.toString());
        }
    }// end of method

    public void initField() {
        FieldUtil.initField(this);
    }// end of method

    public void setAlignment(FieldAlignment alignment) {
    }// end of method


}// end of class
