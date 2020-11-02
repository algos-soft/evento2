package it.algos.webbase.web.field;

import com.vaadin.data.Property;

@SuppressWarnings("serial")
public class TextField extends com.vaadin.ui.TextField implements FieldInterface<String> {


    /**
     * Constructs an empty <code>TextField</code> with no caption.
     */
    public TextField() {
        super();
    }// end of constructor


    /**
     * Constructs an empty <code>TextField</code> with given caption.
     *
     * @param caption the caption <code>String</code> for the editor.
     */
    public TextField(String caption) {
        this(caption, "");
    }// end of constructor


    /**
     * Constructs a new <code>TextField</code> with the given caption and
     * initial text contents. The editor constructed this way will not be bound
     * to a Property unless
     * {@link com.vaadin.data.Property.Viewer#setPropertyDataSource(Property)}
     * is called to bind it.
     *
     * @param caption the caption <code>String</code> for the editor.
     * @param value   the initial text content of the editor.
     */
    public TextField(String caption, String value) {
        super(caption, value);
        init();
    }// end of constructor

    private void init() {
        initField();
        setWidth("180px");
        setNullRepresentation("");
    }// end of method

    public void initField() {
        FieldUtil.initField(this);
    }// end of method

    public void setAlignment(FieldAlignment alignment) {
        FieldUtil.setAlignment(this, alignment);
    }// end of method

}// end of field class
