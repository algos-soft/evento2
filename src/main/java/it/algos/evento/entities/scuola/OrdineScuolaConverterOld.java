package it.algos.evento.entities.scuola;

import com.vaadin.data.util.converter.Converter;

import java.util.Locale;

@SuppressWarnings("serial")
public class OrdineScuolaConverterOld implements Converter<OrdineScuolaEnumOld, Integer> {

	@Override
	public Class getModelType() {
		return Integer.class;
	}

	@Override
	public Class getPresentationType() {
		return OrdineScuolaEnumOld.class;
	}

	@Override
	public Integer convertToModel(OrdineScuolaEnumOld value, Class<? extends Integer> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value != null) {
			return value.getId();
		} else {
			return null;
		}
	}

	@Override
	public OrdineScuolaEnumOld convertToPresentation(Integer value, Class<? extends OrdineScuolaEnumOld> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value != null) {
			return OrdineScuolaEnumOld.getOrdineScuola(value);
		} else {
			return null;
		}
	}

	public String convertToPresentation(Object value) throws ConversionException {
		String stringa = "";
		Object obj = convertToPresentation((Integer) value, OrdineScuolaEnumOld.class, Locale.getDefault());
		if (obj != null) {
			stringa = obj.toString();
		}
		return stringa;
	}

}
