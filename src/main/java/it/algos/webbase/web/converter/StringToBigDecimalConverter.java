package it.algos.webbase.web.converter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

@SuppressWarnings("serial")
public class StringToBigDecimalConverter implements Converter<String, BigDecimal> {

	private int numPlaces;

	/**
	 * Creates a Converter
	 * 
	 * @param numPlaces
	 *            the number of decimal places
	 */
	public StringToBigDecimalConverter(int numPlaces) {
		super();
		this.numPlaces = numPlaces;
	}// end of constructor

	/**
	 * Creates a Converter with 2 decimal places (default)
	 */
	public StringToBigDecimalConverter() {
		this(2);
	}// end of method

	@Override
	public BigDecimal convertToModel(String value, Class<? extends BigDecimal> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		return parseString(value, locale);
	}// end of method

	@Override
	public String convertToPresentation(BigDecimal value, Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		return parseBigDecimal(value, locale);
	}// end of method

	public String convertToPresentation(BigDecimal value)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		return parseBigDecimal(value, Locale.getDefault());
	}// end of method

	@Override
	public Class<BigDecimal> getModelType() {
		return BigDecimal.class;
	}// end of method

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}// end of method

	private BigDecimal parseString(String string, Locale locale) {
		BigDecimal bd = new BigDecimal(0);
		if (string != null) {
			try {
				DecimalFormat df = createDecimalFormat(locale);
				bd = (BigDecimal) df.parse(string);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}// end of if cycle
		return bd;
	}// end of method

	private String parseBigDecimal(BigDecimal big, Locale locale) {
		String string = null;
		if (big != null) {
			double dbl = big.doubleValue();
			DecimalFormat df = createDecimalFormat(locale);
			string = df.format(dbl);
		}// end of if cycle
		return string;
	}// end of method

	private DecimalFormat createDecimalFormat(Locale locale) {
		NumberFormat nf;
		if (locale != null) {
			nf = NumberFormat.getNumberInstance(locale);
		} else {
			nf = NumberFormat.getNumberInstance();
		}// end of if/else cycle
		DecimalFormat df = (DecimalFormat) nf;
		df.setParseBigDecimal(true);
		df.setMinimumFractionDigits(numPlaces);
		df.setMaximumFractionDigits(numPlaces);
		return df;
	}// end of method

	/**
	 * Sets the number of decimal places
	 * 
	 * @pamam numPlaces
	 */
	public void setDecimalPlaces(int numPlaces) {
		this.numPlaces = numPlaces;
	}// end of method

}// end of class
