package it.algos.webbase.web.lib;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;

/**
 * Bean validation utility
 * <p>
 * Having such a class validation is easy: final Person person = new Person(); new BeanValidator().validate( person );
 * ConstraintViolationException exception will be thrown in case of any validation errors
 */
public class BeanValidator {

	private final ValidatorFactory factory;

	public BeanValidator() {
		factory = Validation.buildDefaultValidatorFactory();
	}

	public <T> void validate(final T instance) {
		final Validator validator = factory.getValidator();

		final Set<ConstraintViolation<T>> violations = validator.validate(instance, Default.class);

		if (!violations.isEmpty()) {
			final Set<ConstraintViolation<?>> constraints = new HashSet<ConstraintViolation<?>>(violations.size());

			for (final ConstraintViolation<?> violation : violations) {
				constraints.add(violation);
			}

			throw new ConstraintViolationException(constraints);
		}
	}
}
