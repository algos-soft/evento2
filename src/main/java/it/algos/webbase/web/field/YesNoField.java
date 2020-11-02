package it.algos.webbase.web.field;

import com.vaadin.data.Property;
import com.vaadin.ui.*;
import it.algos.webbase.web.component.YesNoCheckboxComponent;

/**
 * Created by alex on 28-05-2015.
 * .
 */
public class YesNoField extends ACustomField<Boolean> implements FieldInterface<Boolean>{

    YesNoCheckboxComponent comp;

    public YesNoField() {
        this(null);
    }

    public YesNoField(String caption) {
        super();
        setCaption(caption);
    }

    @Override
    public void initField() {
    }

    @Override
    public void setAlignment(FieldAlignment alignment) {
    }


    @Override
    protected Component initContent() {
        comp=new YesNoCheckboxComponent();
        comp.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                setInternalValue(comp.getValue());
            }
        });
        return comp;
    }

    public void setYesText(String text){
        comp.setYesText(text);
    }

    public void setNoText(String text){
        comp.setNoText(text);
    }


    @Override
    public Class<? extends Boolean> getType() {
        return Boolean.class;
    }



}
