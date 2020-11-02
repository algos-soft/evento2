package it.algos.webbase.web.field;

@SuppressWarnings("serial")
public class TextArea extends com.vaadin.ui.TextArea implements FieldInterface<String> {

	public TextArea(String caption) {
		super(caption);
		init();
	}

	public TextArea() {
		this("");
	}

	private void init() {
		initField();
		setNullRepresentation("");
	}

	public void initField() {
		FieldUtil.initField(this);
	}

	public void setAlignment(FieldAlignment alignment) {
	}

}
