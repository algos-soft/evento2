package it.algos.evento.entities.scuola;

import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import it.algos.evento.entities.company.Company;
import it.algos.evento.entities.insegnante.InsegnanteImport;
import it.algos.evento.entities.insegnante.InsegnanteImportExtrateatro;
import it.algos.evento.entities.scuola.ScuolaImport.DoneListener;
import it.algos.webbase.web.dialog.BaseDialog;
import it.algos.webbase.web.importexport.ExportConfiguration;
import it.algos.webbase.web.importexport.ExportManager;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.TableToolbar;

@SuppressWarnings("serial")
public class ScuolaTablePortal extends TablePortal {

	public ScuolaTablePortal(ModulePop modulo) {
		super(modulo);
	}// end of constructor

	public TableToolbar createToolbar() {
		final TableToolbar toolbar = super.createToolbar();

		MenuBar.MenuItem item = toolbar.addButton("Altro...", null);
		item.addItem("Importa...", null, new MenuBar.Command() {
			public void menuSelected(MenuItem selectedItem) {
				DoneListener listener = new DoneListener() {

					@Override
					public void done(BaseDialog dialog) {
						dialog.show(getUI());
					}
				};

				// personalizzazione Extrateatro - import scuole personalizzato
				String companyName = Company.getCurrent().getName();
				if(companyName.equalsIgnoreCase("extrateatro")){
					new ScuolaImportExtrateatro(getTable(), listener);
				}else{
					new ScuolaImport(getTable(), listener);
				}


				// ConfirmDialog dialog = new ConfirmDialog(null);
				// dialog.show(getUI());
			}
		});// end of anonymous class

		item.addItem("Esporta...", null, new MenuBar.Command() {
			public void menuSelected(MenuItem selectedItem) {
				ExportConfiguration expConf = ExportManager.createExportConfiguration(Scuola.class);
				expConf.setContainer(getTable().getContainerDataSource()); // used only if "only records in table" is selected
				new ExportManager(expConf, ScuolaTablePortal.this).show();
			}
		});// end of anonymous class

//		item.addItem("Pdf...", null, new MenuBar.Command() {
//			public void menuSelected(MenuItem selectedItem) {
//				JasperReportBuilder report = InsegnanteModulo.createPdfReport();
//				ReportDownloadDialog dialog = new ReportDownloadDialog(report, "Testreport.pdf");
//				dialog.show(getUI());
//			}
//		});// end of anonymous class

		return toolbar;
	}// end of method

}// end of class
