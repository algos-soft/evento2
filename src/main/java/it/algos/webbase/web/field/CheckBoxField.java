package it.algos.webbase.web.field;

import com.vaadin.ui.CheckBox;

@SuppressWarnings("serial")
public class CheckBoxField extends CheckBox implements FieldInterface<Boolean> {

    /**
     * Creates a new checkbox.
     */
    public CheckBoxField() {
        super();
        init();
    }

    /**
     * Creates a new checkbox with a set caption.
     *
     * @param caption the Checkbox caption.
     */
    public CheckBoxField(String caption) {
        super(caption);
        init();
    }

    /**
     * Creates a new checkbox and a set initial state.
     *
     * @param initialState the initial state of the checkbox
     */
    public CheckBoxField(boolean initialState) {
        setValue(initialState);
    }// end of constructor


    /**
     * Creates a new checkbox with a caption and a set initial state.
     *
     * @param caption      the caption of the checkbox
     * @param initialState the initial state of the checkbox
     */
    public CheckBoxField(String caption, boolean initialState) {
        this(caption);
        setValue(initialState);
    }// end of constructor


    private void init() {
        initField();
    }

    public void initField() {
        FieldUtil.initField(this);
    }

    public void setAlignment(FieldAlignment alignment) {
    }

}
