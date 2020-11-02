package it.algos.webbase.web.field;

import com.vaadin.data.Validator;

public class EmailField extends TextField implements FieldInterface<String> {

	public EmailField(String caption) {
		super(caption);
		init();
	}

	public EmailField() {
		this("");
	}

	private void init() {
		setWidth("20em");
		Validator validator = new com.vaadin.data.validator.EmailValidator("indirizzo email non valido");
		this.addValidator(validator);
	}

}
