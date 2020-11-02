package it.algos.webbase.web.field;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

@SuppressWarnings({ "serial" })
public class IntegerField extends ACustomField<Integer> implements FieldInterface<Integer> {

	private TextField textField;
	private StringToIntegerConverter converter = new StringToIntegerConverter();
	private static final Logger logger = Logger.getLogger(IntegerField.class.getName());

	public IntegerField(String caption) {
		super();
		textField = new TextField();
		setCaption(caption);
		setWidth("5em");

		textField.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
				String text = textField.getValue();
				try{

					// convert UI to model (may throw exception)
					Integer integer = converter.convertToModel(text, Integer.class, Locale.getDefault());

					// if no exception, but null is returned, force exception
					if(integer==null){
						throw new Exception();
					}

					// writes the number
					writeValue(integer);

				}catch(Exception e){
					// if the conversion fails force the UI to zero
					// setValue() triggers a new valueChange event and this method
					// gets called again, this time with "0" in the UI
					logger.log(Level.INFO, "Conversion error: '"+text+"' to integer - forced to zero");
					textField.setValue("0");
				}
			}
		});
	}

	public IntegerField() {
		this(null);
	}

	@Override
	protected Component initContent() {
		return textField;
	}

	@Override
	public void initField() {
		FieldUtil.initField(this);
	}

	private void writeValue(Integer integer) {
		setValue(integer);
	}

	@Override
	protected void setInternalValue(Integer integer) {
		super.setInternalValue(integer);
		String text = converter.convertToPresentation(integer, String.class, Locale.getDefault());
		textField.setValue(text);
	}

	@Override
	public Class<? extends Integer> getType() {
		return Integer.class;
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
