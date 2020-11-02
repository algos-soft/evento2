package it.algos.webbase.web.field;

import com.vaadin.ui.AbstractField;

public class FieldUtil {

	/**
	 * Default settings for all fields Invoked by each field at construction
	 */
	public static void initField(AbstractField f) {
		f.setImmediate(true);
	}

	/**
	 * sets the alignment of text inside the field
	 * 
	 * @param textfield
	 *            the TextField to be aligned
	 * @param alignment
	 *            the requested alignment
	 */
	public static void setAlignment(com.vaadin.ui.TextField textfield, FieldAlignment alignment) {

		textfield.removeStyleName("align-left");
		textfield.removeStyleName("align-center");
		textfield.removeStyleName("align-right");

		switch (alignment) {
		case left:
			textfield.addStyleName("align-left");
			break;
		case center:
			textfield.addStyleName("align-center");
			break;
		case right:
			textfield.addStyleName("align-right");
			break;
		default:
			break;
		}

	}

}
