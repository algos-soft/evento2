package it.algos.evento.config;

import com.vaadin.ui.Component;

/**
 * A Configuration Component
 */
public interface ConfigComponent {
	
	/**
	 * Returns the main UI component
	 */
	public Component getUIComponent();

	/**
	 * Returns the title of the component
	 */
	public String getTitle();

	/**
	 * Reads the data from the storage and updates the UI components
	 */
	public void loadContent();

}
