package it.algos.webbase.web.importexport;

import com.vaadin.data.Container;

public class ExportConfiguration {
	private Class<?> domainClass;
	private Container container;
	private ExportProvider provider;
	private String filename;

	/**
	 * Constructor for one ExportConfiguration.
	 * 
	 * @param domainClass
	 *            the reference domain class
	 * @param filename
	 *            the filename for export
	 * @param container
	 *            a Container to read the data from
	 */
	public ExportConfiguration(Class<?> domainClass, String filename, Container container, ExportProvider provider) {
		super();
		this.domainClass = domainClass;
		this.filename = filename;
		this.container = container;
		this.provider=provider;
	}

	public Class<?> getDomainClass() {
		return domainClass;
	}

	public String getFilename() {
		return filename;
	}

	public ExportProvider getExportProvider() {
		return provider;
	}

	public Container getContainer() {
		return container;
	}

	public void setContainer(Container container) {
		this.container = container;
	}

}
