package it.algos.evento.entities.prenotazione.eventi;

import com.vaadin.data.util.converter.Converter;

import java.util.Locale;

@SuppressWarnings("serial")
public class TipoEventoPrenConverter implements Converter<TipoEventoPren, Integer> {

	@Override
	public Class getModelType() {
		return Integer.class;
	}

	@Override
	public Class getPresentationType() {
		return TipoEventoPren.class;
	}

	@Override
	public Integer convertToModel(TipoEventoPren value, Class<? extends Integer> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value != null) {
			return value.getId();
		} else {
			return null;
		}
	}

	@Override
	public TipoEventoPren convertToPresentation(Integer value, Class<? extends TipoEventoPren> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value != null) {
			return TipoEventoPren.getItem(value);
		} else {
			return null;
		}
	}

	public String convertToPresentation(Object value) throws ConversionException {
		String stringa = "";
		Object obj = convertToPresentation((Integer) value, TipoEventoPren.class, Locale.getDefault());
		if (obj != null) {
			stringa = obj.toString();
		}
		return stringa;
	}

}
