package it.algos.webbase.web.field;

@SuppressWarnings("serial")
public class PasswordField extends com.vaadin.ui.PasswordField implements FieldInterface<String> {

	public PasswordField() {
		this(null);
	}

	public PasswordField(String caption) {
		super(caption);
		initField();
	}

	public void initField() {
		FieldUtil.initField(this);
	}

	public void setAlignment(FieldAlignment alignment) {
	}

}
