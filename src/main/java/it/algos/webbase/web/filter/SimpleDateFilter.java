package it.algos.webbase.web.filter;

import java.util.Date;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.filter.AbstractJunctionFilter;

public class SimpleDateFilter extends AbstractJunctionFilter {

	private Container.Filter filter;

	public SimpleDateFilter(java.lang.Object propertyId, Date filterDate) {
		super();

		// build filter here

	}

	@Override
	public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
		return filter.passesFilter(itemId, item);
	}

}
