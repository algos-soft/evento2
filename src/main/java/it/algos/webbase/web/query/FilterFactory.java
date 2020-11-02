package it.algos.webbase.web.query;

import javax.persistence.metamodel.Attribute;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Compare;


public class FilterFactory {
	
	/**
	 * Creates a filter of type Compare.Equal
	 * @param attr - the attribute
	 * @param value - the value
	 * @return the filter
	 */
	public static Filter create(Attribute<?, ?> attr, Object value) {
		return new Compare.Equal(attr.getName(), value);
	}// end of static method
	
	/**
	 * Creates a filter as a combination of filters joined with AND
	 * @param filters the source filters
	 * @return the combined filter
	 */
	public static Filter create(Filter... filters) {
		return new And(filters);
	}// end of static method

}// end of factory class
