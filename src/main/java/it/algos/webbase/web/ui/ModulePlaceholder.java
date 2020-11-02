package it.algos.webbase.web.ui;

import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class ModulePlaceholder extends VerticalLayout {

	public ModulePlaceholder() {
		super();
		setHeight("100%");
	}// end of constructor

	public void setContent(Component comp) {
		removeAllComponents();
		addComponent(comp);
		comp.setHeight("100%");
		setExpandRatio(comp, 1.0f);
	}// end of method
	
}// end of class
