package it.algos.webbase.web.field;

import com.vaadin.data.Container;
import com.vaadin.ui.Field;

public interface FieldInterface<T> extends Field<T> {

	public void initField();

	/**
	 * sets the alignment of text inside the field
	 * 
	 * @param alignment
	 *            the requested alignment
	 */
	public void setAlignment(FieldAlignment alignment);


}
