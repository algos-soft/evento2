package it.algos.webbase.web.importexport;

import com.vaadin.ui.AbstractComponent;
import it.algos.webbase.web.dialog.ConfirmDialog;

@SuppressWarnings("serial")
public class ExportManager extends ConfirmDialog {

	private ExportConfiguration config;
    private AbstractComponent comp;

	public ExportManager(final ExportConfiguration config, AbstractComponent comp) {
		super(null);
		this.config = config;
        this.comp=comp;
		setTitle("Esportazione dati");
		String msg="Tabella: <b>" + config.getDomainClass().getSimpleName() + "</b>";
		msg+="<br>N. di record: <b>"+config.getContainer().size()+"</b>";
		setMessage(msg);

	}

	@Override
	protected void onConfirm() {
        ExportFactory.doExport(config, comp);
		super.onConfirm();
	}

	/**
	 * Helper method to create a new ExportConfiguration object for a given Entity.<br>
	 * Contains all the fields with defaul titles.
	 */
	public static ExportConfiguration createExportConfiguration(Class clazz) {
		ExportProvider provider=new EntityExportProvider(clazz);
		ExportConfiguration config = new ExportConfiguration(clazz, clazz.getSimpleName() + ".xls", null, provider);
		return config;
	}

	public ExportConfiguration getConfig() {
		return config;
	}

	public AbstractComponent getComp() {
		return comp;
	}
}
