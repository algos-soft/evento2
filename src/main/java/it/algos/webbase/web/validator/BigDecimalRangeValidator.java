package it.algos.webbase.web.validator;

import java.math.BigDecimal;

import com.vaadin.data.validator.RangeValidator;

@SuppressWarnings("serial")
public class BigDecimalRangeValidator extends RangeValidator<BigDecimal> {

	/**
	 * Creates a validator for checking that an BigDecimal is within a given range.
	 * 
	 * By default the range is inclusive i.e. both minValue and maxValue are valid values. Use
	 * {@link #setMinValueIncluded(boolean)} or {@link #setMaxValueIncluded(boolean)} to change it.
	 * 
	 * 
	 * @param errorMessage
	 *            the message to display in case the value does not validate.
	 * @param minValue
	 *            The minimum value to accept or null for no limit
	 * @param maxValue
	 *            The maximum value to accept or null for no limit
	 */
	public BigDecimalRangeValidator(String errorMessage, BigDecimal minValue, BigDecimal maxValue) {
		super(errorMessage, BigDecimal.class, minValue, maxValue);
	}

}
