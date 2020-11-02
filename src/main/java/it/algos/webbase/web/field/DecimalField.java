package it.algos.webbase.web.field;

import it.algos.webbase.web.converter.StringToBigDecimalConverter;

import java.math.BigDecimal;
import java.util.Locale;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

@SuppressWarnings("serial")
public class DecimalField extends ACustomField<BigDecimal> implements FieldInterface<BigDecimal> {

	private TextField textField;
	private StringToBigDecimalConverter converter = new StringToBigDecimalConverter();

	public DecimalField(String caption) {
		super();
		textField = new TextField();
		initField();
		setAlignment(FieldAlignment.left);

		setCaption(caption);
		setWidth("6em");

		textField.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
				String text = textField.getValue();
				BigDecimal bd = converter.convertToModel(text, BigDecimal.class, Locale.getDefault());
				writeValue(bd);
			}
		});
	}

	public DecimalField() {
		this(null);
	}

	@Override
	protected Component initContent() {
		return textField;
	}

	@Override
	public Class<? extends BigDecimal> getType() {
		return BigDecimal.class;
	}

	@Override
	public void initField() {
		FieldUtil.initField(this);
	}

	private void writeValue(BigDecimal bd) {
		setValue(bd);
	}

	@Override
	protected void setInternalValue(BigDecimal bd) {
		super.setInternalValue(bd);
		String text = converter.convertToPresentation(bd, String.class, Locale.getDefault());
		textField.setValue(text);
	}

	/**
	 * Sets the number of decimal places
	 * 
	 * @pamam numPlaces
	 */
	public void setDecimalPlaces(int numPlaces) {
		converter.setDecimalPlaces(numPlaces);
	}

	@Override
	public void setWidth(String width) {
		textField.setWidth(width);
	}

	@Override
	public boolean isReadOnly() {
		return textField.isReadOnly();
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		textField.setReadOnly(readOnly);
	}

	public void setAlignment(FieldAlignment alignment) {
		FieldUtil.setAlignment(textField, alignment);
	}

}
