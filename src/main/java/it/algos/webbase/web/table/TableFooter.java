package it.algos.webbase.web.table;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class TableFooter extends HorizontalLayout {

	public TableFooter() {
		super();


		this.setStyleName("v-table-header-wrap");

		Label label = new Label("");
		label.setStyleName("v-table-caption-container");
		label.addStyleName("v-table-caption-container-align-left");
		addComponent(label);

	}// end of constructor

}// end of class
