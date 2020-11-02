package it.algos.evento.entities.comune;

import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.web.dialog.AlertDialog;
import it.algos.webbase.web.dialog.BaseDialog;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.importexport.ExcelImportProcessor;
import it.algos.webbase.web.importexport.ExcelImportProcessor.ExcelImportListener;
import it.algos.webbase.web.importexport.ExcelImportProcessor.ImportReport;
import it.algos.webbase.web.lib.BeanValidator;
import it.algos.webbase.web.lib.LibText;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.updown.OnDemandFileDownloader;
import it.algos.webbase.web.updown.StringStreamResource;

import javax.persistence.EntityManager;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class ComuneImport {

	private BeanValidator validator = new BeanValidator();
	private ATable table;
	private Path file;
	private DoneListener listener;
	private BaseCompany company;

//	public ComuneImport(ATable table, DoneListener listener, Company company) {
//		super();
//		this.table = table;
//		this.listener = listener;
//		this.company=company;
//		start();
//	}
	
	public ComuneImport(BaseCompany company) {
		super();
		this.company=company;
	}


//	/**
//	 * Importa i comuni  da un file excel custom.
//	 * <p>
//	 * Presenta il dialogo di scelta, carica il file sul server, lo elabora e crea i record, aggiorna la lista, cancella
//	 * il file importato, presenta il dialogo di esito
//	 */
//	private void start() {
//
//		FileUploader uploader = new FileUploader();
//		uploader.setTitle("Importazione comuni");
//		uploader.setMessage("Formato file: Excel.<br>La prima riga deve contenere i titoli delle colonne.<br>Colonne riconosciute:"
//				+ Columns.getHTMLList());
//		uploader.setButtonText("importa");
//
//		uploader.addUploadFinishedListener(new UploadFinishedListener() {
//
//			@Override
//			public void uploadFinished(final Path file) {
//				setFile(file);
//				mUploadFinished();
//			}
//		});
//
//		uploader.show(UI.getCurrent());
//
//	}
	
	/**
	 * Importa i comuni da un file excel embedded.
	 * <p>
	 * Nessuna GUI visualizzata (esegue su server)
	 */
	public static void doImport(String fullPath, BaseCompany company, EntityManager manager) {
		final ComuneImport imp = new ComuneImport(company);
		
		Path file = Paths.get(fullPath);
		imp.setFile(file);
		
//		EntityManager manager = EM.createEntityManager();
//		manager.getTransaction().begin();

		
		ExcelImportProcessor processor=new ExcelImportProcessor(file, Columns.getColumnNames(), new ExcelImportListener() {

			@Override
			public ConstraintViolationException rowReceived(HashMap<String, String> valueMap) {
				return imp.mRowReceived(valueMap, manager);
			}

			@Override
			public void importTerminated(ImportReport report) {
//				manager.getTransaction().commit();
//				manager.close();
			}

		});

		processor.start();
		
	}


	private void setFile(Path file) {
		this.file = file;
	}

	/**
	 * The file has been downloaded.
	 * <p>
	 * Create an Import Processor with the file and listen to its events
	 */
	private void mUploadFinished() {
		
		EntityManager manager = EM.createEntityManager();
		manager.getTransaction().begin();

		ExcelImportProcessor processor = new ExcelImportProcessor(file, Columns.getColumnNames(), new ExcelImportListener() {

			@Override
			public ConstraintViolationException rowReceived(HashMap<String, String> valueMap) {
				return mRowReceived(valueMap, manager);
			}

			@Override
			public void importTerminated(ImportReport report) {
				manager.getTransaction().commit();
				manager.close();
				mImportTerminated(report);
			}

		});
		
		processor.start();
		
	}

	/**
	 * A row has been received.
	 * <p>
	 * Process the row, save the entity, return violations.
	 */
	private ConstraintViolationException mRowReceived(HashMap<String, String> valueMap, EntityManager manager) {

		// create entity from excel data
		Comune comune = comuneFromExcel(valueMap);
		comune.setCompany(company);

		// try to save the entity, or send back the validation exception
		ConstraintViolationException exception = null;
		try {
			validator.validate(comune); // this throws exception if
										// validation fails
			
			manager.persist(comune);
			//comune.save(); // this handles exceptions internally
		} catch (ConstraintViolationException e) {
			exception = e;
		}

		return exception;
	}

	/**
	 * The import is terminated.
	 * <p>
	 * Delete the imported file, refresh the table, show a dialog with the outcome
	 */
	private void mImportTerminated(ImportReport report) {

		try {
			Files.delete(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// refresh table
		table.refreshRowCache();

		// show dialog
		BaseDialog dialog;
		if (report.getFailed() == 0) {
			String message = "<p>Record importati: " + report.getSuccess();
			dialog = new AlertDialog("Importazione terminata",message);

		} else {

			dialog = new OutcomeDialog(report);

			DoneListener listener = getDoneListener();
			if (listener != null) {
				listener.done(dialog);
			}

		}

	}

	/**
	 * Enum of the desired coulmns to import.
	 * <p>
	 * Here we map our abstract column names to the real column names in the Excel file
	 */
	public enum Columns {
		nome("NOME"), sigla_prov("SIGLA_PROVINCIA");

		private String titoloColonna;

		private Columns(String name) {
			this.titoloColonna = name;
		}

		public String getTitoloColonna() {
			return titoloColonna;
		}

		public static String[] getColumnNames() {
			String[] columnNames = new String[Columns.values().length];
			for (int i = 0; i < columnNames.length; i++) {
				Columns c = Columns.values()[i];
				columnNames[i] = c.titoloColonna;
			}
			return columnNames;
		}

		public static String getHTMLList() {
			String html = "<ul>";
			for (Columns c : Columns.values()) {
				html += "<li>" + c.getTitoloColonna() + "</li>";
			}
			html += "</ul>";
			return html;
		}

	}

	/**
	 * Create an entity from a row of the Excel file.
	 */
	private Comune comuneFromExcel(HashMap<String, String> valueMap) {

		String string;
		Comune comune = new Comune();

		// nome
		string = valueMap.get(Columns.nome.getTitoloColonna());
		string = LibText.fixSpaces(string);
		comune.setNome(string);

		// sigla provincoia
		string = valueMap.get(Columns.sigla_prov.getTitoloColonna());
		string = LibText.fixSpaces(string);
		comune.setSiglaProvincia(string);


		
		return comune;

	}

	/**
	 * The dialog shown in case of errors.
	 */
	@SuppressWarnings("serial")
	private class OutcomeDialog extends ConfirmDialog {

		private ImportReport report;

		public OutcomeDialog(ImportReport report) {
			super(null);
			this.report = report;
			setTitle("Importazione terminata");
			String message = "Record importati: " + report.getSuccess();
			message += "<p>Errori: " + report.getFailed();
			message += "<p>E' stato generato un report degli errori.";
			setMessage(message);
			getConfirmButton().setCaption("Scarica report");
			getCancelButton().setCaption("Chiudi");
			createDownloader();

		}

		private void createDownloader() {
			StringStreamResource streamRes = new StringStreamResource(report.getReport(), "import_report.txt");
			OnDemandFileDownloader downloader = new OnDemandFileDownloader(streamRes);
			downloader.extend(getConfirmButton());
		}

		@Override
		protected void onConfirm() {
		}

	}

	public void addDoneListener() {

	}

	public interface DoneListener {
		public void done(BaseDialog dialog);
	}

	private DoneListener getDoneListener() {
		return listener;
	}

}
