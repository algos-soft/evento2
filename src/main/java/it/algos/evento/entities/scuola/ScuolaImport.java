package it.algos.evento.entities.scuola;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Like;
import com.vaadin.ui.UI;
import it.algos.evento.entities.comune.Comune;
import it.algos.evento.entities.comune.Comune_;
import it.algos.evento.entities.ordinescuola.OrdineScuola;
import it.algos.evento.entities.ordinescuola.OrdineScuola_;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.multiazienda.ELazyContainer;
import it.algos.webbase.web.dialog.AlertDialog;
import it.algos.webbase.web.dialog.BaseDialog;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.importexport.ExcelImportProcessor;
import it.algos.webbase.web.importexport.ExcelImportProcessor.ExcelImportListener;
import it.algos.webbase.web.importexport.ExcelImportProcessor.ImportReport;
import it.algos.webbase.web.lib.BeanValidator;
import it.algos.webbase.web.lib.Lib;
import it.algos.webbase.web.lib.LibText;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.updown.FileUploader;
import it.algos.webbase.web.updown.FileUploader.UploadFinishedListener;
import it.algos.webbase.web.updown.OnDemandFileDownloader;
import it.algos.webbase.web.updown.StringStreamResource;
import org.apache.commons.lang.WordUtils;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class ScuolaImport {

	private BeanValidator validator = new BeanValidator();
	private ATable table;
	private Path file;
	private DoneListener listener;
	private ELazyContainer comuniCont;

	public ScuolaImport(ATable table, DoneListener listener) {
		super();
		this.table = table;
		this.listener = listener;
//		comuniCont = new EROContainer(Comune.class, EM.createEntityManager());
		comuniCont = new ELazyContainer(EM.createEntityManager(), Comune.class);
		start();
	}// end of constructor

//	public ScuolaImport(Path file) {
//		super();
//		this.file = file;
//		comuniCont = new EROContainer(Comune.class, EM.createEntityManager());
//		mUploadFinished();
//	}// end of constructor

	/**
	 * Importa gli insegnanti da un file excel custom.
	 * <p>
	 * Presenta il dialogo di scelta, carica il file sul server, lo elabora e crea i record, aggiorna la lista, cancella
	 * il file importato, presenta il dialogo di esito
	 */
	private void start() {

		FileUploader uploader = new FileUploader();
		uploader.setTitle("Importazione scuole");
		uploader.setMessage("Formato file: <b>Excel</b>.<br>La prima riga deve contenere i titoli delle colonne.<br>" +
				"L'ordine delle colonne nel file non è rilevante.<br><br>"+
				"Vengono riconosciute le colonne con i titoli sotto elencati.<br>"+
				"Il file può contenere altre colonne, ma non verranno importate.<br>"+
				"<br>"+
				"Colonne riconosciute:" + Columns.getHTMLList());
		uploader.setButtonText("importa");

		uploader.addUploadFinishedListener(new UploadFinishedListener() {

			@Override
			public void uploadFinished(final Path file) {
				setFile(file);
				mUploadFinished();
			}
		});

		uploader.show(UI.getCurrent());

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
		ExcelImportProcessor processor = new ExcelImportProcessor(file, getColumnNames(), new ExcelImportListener() {

			@Override
			public ConstraintViolationException rowReceived(HashMap<String, String> valueMap) {
				return mRowReceived(valueMap);
			}

			@Override
			public void importTerminated(ImportReport report) {
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
	private ConstraintViolationException mRowReceived(HashMap<String, String> valueMap) {

		// create entity from excel data
		Scuola scuola = scuolaFromExcel(valueMap);

		// try to save the entity, or send back the validation exception
		ConstraintViolationException exception = null;
		try {
			validator.validate(scuola); // this throws exception if
										// validation fails
			scuola.save(); // this handles exceptions internally
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
		if (table != null) {
			table.refreshRowCache();
		}// end of if cycle

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
	 * Ritorna l'array dei nomi delle colonne Excel da elaborare
	 */
	public String[] getColumnNames(){
		return Columns.getColumnNames();
	}


	/**
	 * Enum of the desired coulmns to import.
	 * <p>
	 * Here we map our abstract column names to the real column names in the Excel file
	 */
	private enum Columns {
		sigla("SIGLA","Sigla per ricerca rapida"),

		//tipo("tipologia di scuola secondaria di II grado"),

		nome("NOME", "Nome completo della scuola"),

		ordine("ORDINE","Ordine della scuola, sigle come da tabella Ordini Scuola"),

		tipo("TIPO","Tipologia (liceo scientifico, istituto tecnico, liceo artistico...)"),

		indirizzo("INDIRIZZO","Via, piazza ecc..."),

		cap("CAP","Numerico 5 cifre"),

		comune("COMUNE", "Nome del comune, come da tabella Comuni"),

		telefono("TELEFONO"),

		fax("FAX"),

		email("E-MAIL");

		private String titoloColonna;
		private String specs;

		private Columns(String name, String specs) {
			this.titoloColonna = name;
			this.specs=specs;

		}

		private Columns(String name) {
			this(name,"");
		}


		public String getTitoloColonna() {
			return titoloColonna;
		}

		public String getSpecs() {
			return specs;
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
				html+="<li>";
				html += "<b>"+c.getTitoloColonna()+"</b>";
				String hint = c.getSpecs();
				if((hint!=null) & (!hint.equals(""))){
					html+=" ("+c.getSpecs()+")";
				}
				html+="</li>";

			}
			html += "</ul>";
			return html;
		}

	}

	/**
	 * Create an entity from a row of the Excel file.
	 */
	public Scuola scuolaFromExcel(HashMap<String, String> valueMap) {

		String string;
		Scuola scuola = new Scuola();

		// sigla
		string = valueMap.get(Columns.sigla.getTitoloColonna());
		string = LibText.fixSpaces(string);
		scuola.setSigla(string);

		// nome
		string = valueMap.get(Columns.nome.getTitoloColonna());
		string = LibText.fixSpaces(string);
		string = WordUtils.capitalizeFully(string);
		scuola.setNome(string);

		// ordine
		string = valueMap.get(Columns.ordine.getTitoloColonna());
		string = LibText.fixSpaces(string);
		OrdineScuola ordine = getOrdine(string);
		scuola.setOrdine(ordine);

		// tipo
		string = valueMap.get(Columns.tipo.getTitoloColonna());
		string = LibText.fixSpaces(string);
		scuola.setTipo(string);

		// indirizzo
		string = valueMap.get(Columns.indirizzo.getTitoloColonna());
		string = LibText.fixSpaces(string);
		string = WordUtils.capitalizeFully(string);
		scuola.setIndirizzo(string);

		// cap
		string = valueMap.get(Columns.cap.getTitoloColonna());
		string = LibText.fixSpaces(string);
		scuola.setCap(string);

		// comune
		string = valueMap.get(Columns.comune.getTitoloColonna());
		string = LibText.fixSpaces(string);
		Comune comune = getComune(string);
		scuola.setComune(comune);

		// telefono
		string = valueMap.get(Columns.telefono.getTitoloColonna());
		string = LibText.fixSpaces(string);
		scuola.setTelefono(string);

		// fax
		string = valueMap.get(Columns.fax.getTitoloColonna());
		string = LibText.fixSpaces(string);
		scuola.setFax(string);

		// email
		string = valueMap.get(Columns.email.getTitoloColonna());
		string = LibText.fixSpaces(string);
		scuola.setEmail(string);

		return scuola;

	}


	private OrdineScuola getOrdine(String sigla){
		OrdineScuola ordine = (OrdineScuola) CompanyQuery.getFirstEntity(OrdineScuola.class, OrdineScuola_.sigla, sigla);
		return ordine;
	}

	private Comune getComune(String nome){
		Comune comune=null;
		comuniCont.removeAllContainerFilters();
		comuniCont.refresh(); // refresh container before applying new filters
		Filter filter = new Like(Comune_.nome.getName(), nome, false);
		comuniCont.addContainerFilter(filter);
		if (comuniCont.size() == 1) {
			long id = Lib.getLong(comuniCont.getIdByIndex(0));

//			comune = (Comune)comuniCont.getItem(id).getEntity();

			comune = (Comune)comuniCont.getEntity(id);

		}
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
