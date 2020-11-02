package it.algos.evento.entities.scuola;

import com.vaadin.data.util.converter.Converter;
import it.algos.webbase.web.field.ArrayComboField;

import java.util.Locale;

@SuppressWarnings("serial")
public class OrdineScuolaFieldOld extends ArrayComboField {

	public OrdineScuolaFieldOld(Object[] values, String caption) {
		super(values, caption);
		setConverter(new OrdineScuolaConverter());
	}

	private class OrdineScuolaConverter implements Converter {

		@Override
		public Object convertToModel(Object value, Class targetType, Locale locale) throws ConversionException {
			Integer retval = 0;
			if (value != null) {
				if (value instanceof OrdineScuolaEnumOld) {
					OrdineScuolaEnumOld ordine = (OrdineScuolaEnumOld) value;
					retval = ordine.getId();
				}
			}
			return retval;
		}

		@Override
		public Object convertToPresentation(Object value, Class targetType, Locale locale) throws ConversionException {
			OrdineScuolaEnumOld ordine = null;
			if (value != null) {
				if (value instanceof Integer) {
					Integer id = (Integer) value;
					ordine = OrdineScuolaEnumOld.getOrdineScuola(id);
				}

			}
			return ordine;
		}

		@Override
		public Class getModelType() {
			return Integer.class;
		}

		@Override
		public Class getPresentationType() {
			return OrdineScuolaEnumOld.class;
		}

	}

}
