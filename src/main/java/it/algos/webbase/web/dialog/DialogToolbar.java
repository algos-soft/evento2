package it.algos.webbase.web.dialog;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class DialogToolbar extends HorizontalLayout {

	public DialogToolbar() {
		super();

		addStyleName("toolbar");
		setMargin(true);
		setSpacing(true);

		addComponents();

	}

	/**
	 * Add the components to this layout
	 */
	protected void addComponents() {
		Label spacer = new Label();
		addComponent(spacer);
		this.setExpandRatio(spacer, 1f);
	}

}
