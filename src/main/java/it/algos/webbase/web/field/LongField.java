package it.algos.webbase.web.field;

import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.data.util.converter.StringToLongConverter;
import com.vaadin.ui.Component;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings({ "serial" })
public class LongField extends ACustomField<Long> implements FieldInterface<Long> {

	private TextField textField;
	private StringToLongConverter converter = new StringToLongConverter();
	private static final Logger logger = Logger.getLogger(LongField.class.getName());

	public LongField(String caption) {
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
					Long aLong = converter.convertToModel(text, Long.class, Locale.getDefault());

					// if no exception, but null is returned, force exception
					if(aLong==null){
						throw new Exception();
					}

					// writes the number
					writeValue(aLong);

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

	public LongField() {
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

	private void writeValue(Long aLong) {
		setValue(aLong);
	}

	@Override
	protected void setInternalValue(Long aLong) {
		super.setInternalValue(aLong);
		String text = converter.convertToPresentation(aLong, String.class, Locale.getDefault());
		textField.setValue(text);
	}

	@Override
	public Class<? extends Long> getType() {
		return Long.class;
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
