package it.algos.evento.entities.insegnante;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import it.algos.evento.entities.company.Company;
import it.algos.evento.entities.insegnante.InsegnanteImport.DoneListener;
import it.algos.webbase.web.dialog.BaseDialog;
import it.algos.webbase.web.importexport.ExportConfiguration;
import it.algos.webbase.web.importexport.ExportManager;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.TableToolbar;
import it.algos.webbase.web.updown.ReportDownloadDialog;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;

@SuppressWarnings("serial")
public class InsegnanteTablePortal extends TablePortal {

	public static final String CMD_IMPORT = "Importa...";
	public static final Resource ICON_IMPORT = FontAwesome.UPLOAD;

	public static final String CMD_EXPORT = "Esporta...";
	public static final Resource ICON_EXPORT = FontAwesome.DOWNLOAD;

	public InsegnanteTablePortal(ModulePop modulo) {
		super(modulo);

		// questa tabella ha il bottone Opzioni
		getToolbar().setOptionsButtonVisible(true);

	}// end of constructor

	public TableToolbar createToolbar() {
		final TableToolbar toolbar = super.createToolbar();

		// bottone Altro...
		MenuBar.MenuItem item = toolbar.addButton("Altro...", FontAwesome.BARS, null);

		item.addItem(CMD_IMPORT, ICON_IMPORT, new MenuBar.Command() {
			public void menuSelected(MenuItem selectedItem) {
				DoneListener listener = new DoneListener() {

					@Override
					public void done(BaseDialog dialog) {
						dialog.show(getUI());
					}// end of method
				};


				// personalizzazione Extrateatro - import referenti personalizzato
				String companyName = Company.getCurrent().getName();
				if(companyName.equalsIgnoreCase("extrateatro")){
					new InsegnanteImportExtrateatro(getTable(), listener);
				}else{
					new InsegnanteImport(getTable(), listener);
				}

			}// end of method
		});// end of anonymous class
		
		item.addItem(CMD_EXPORT, ICON_EXPORT, new MenuBar.Command() {
			public void menuSelected(MenuItem selectedItem) {
				ExportConfiguration conf = ExportManager.createExportConfiguration(Insegnante.class);
				conf.setContainer(getTable().getContainerDataSource()); // used only if "only records in table" is selected
				new ExportManager(conf, InsegnanteTablePortal.this).show();
			}// end of method
		});// end of anonymous class

		item.addItem("Pdf...", null, new MenuBar.Command() {
			public void menuSelected(MenuItem selectedItem) {
				JasperReportBuilder report = InsegnanteModulo.createPdfReport();
				ReportDownloadDialog dialog = new ReportDownloadDialog(report, "Testreport.pdf");
				dialog.show(getUI());
			}// end of method
		});// end of anonymous class

		return toolbar;
	}// end of method

}// end of class
