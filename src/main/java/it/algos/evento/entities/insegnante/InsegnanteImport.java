package it.algos.evento.entities.insegnante;

import com.vaadin.ui.UI;
import it.algos.evento.entities.ordinescuola.OrdineScuola;
import it.algos.evento.entities.ordinescuola.OrdineScuola_;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.web.dialog.AlertDialog;
import it.algos.webbase.web.dialog.BaseDialog;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.importexport.ExcelImportProcessor;
import it.algos.webbase.web.importexport.ExcelImportProcessor.ExcelImportListener;
import it.algos.webbase.web.importexport.ExcelImportProcessor.ImportReport;
import it.algos.webbase.web.lib.BeanValidator;
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

public class InsegnanteImport {

    private BeanValidator validator = new BeanValidator();
    private ATable table;
    private Path file;
    private DoneListener listener;

    public InsegnanteImport(ATable table, DoneListener listener) {
        super();
        this.table = table;
        this.listener = listener;
        start();
    }// end of constructor

    public InsegnanteImport(Path file) {
        super();
        this.file = file;
        mUploadFinished();
    }// end of constructor

    /**
     * Importa gli insegnanti da un file excel custom.
     * <p>
     * Presenta il dialogo di scelta, carica il file sul server, lo elabora e crea i record, aggiorna la lista, cancella
     * il file importato, presenta il dialogo di esito
     */
    private void start() {

        FileUploader uploader = new FileUploader();
        uploader.setTitle("Importazione referenti");
        uploader.setMessage("Formato file: <b>Excel</b>.<br>" +
                "La prima riga deve contenere i titoli delle colonne.<br>" +
                "(L'ordine delle colonne nel file non è rilevante).<br><br>" +
                "Vengono riconosciute le colonne con i titoli sotto elencati.<br>" +
                "Il file può contenere altre colonne, ma non verranno importate.<br>" +
                "<br>" +
                "Colonne riconosciute:"
                + Columns.getHTMLList());
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
     * Ritorna l'array dei nomi delle colonne Excel da elaborare
     */
    public String[] getColumnNames(){
        return Columns.getColumnNames();
    }

    /**
     * A row has been received.
     * <p>
     * Process the row, save the entity, return violations.
     */
    private ConstraintViolationException mRowReceived(HashMap<String, String> valueMap) {

        // create entity from excel data
        Insegnante insegnante = insegnanteFromExcel(valueMap);

        // try to save the entity, or send back the validation exception
        ConstraintViolationException exception = null;
        try {
            validator.validate(insegnante); // this throws exception if
            // validation fails
            insegnante.save(); // this handles exceptions internally
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
            dialog = new AlertDialog("Importazione terminata", message);
        } else {

            dialog = new OutcomeDialog(report);

            DoneListener listener = getDoneListener();
            if (listener != null) {
                listener.done(dialog);
            }

        }

        // dialog.show(UI.getCurrent());
        // dialog.show(table.getUI());
        // dialog.show(ui);

    }

    /**
     * Create an entity from a row of the Excel file.
     */
    public Insegnante insegnanteFromExcel(HashMap<String, String> valueMap) {

        String string;
        Insegnante insegnante = new Insegnante();

        string = WordUtils.capitalizeFully(valueMap.get(Columns.titolo.getTitoloColonna()));
        insegnante.setTitolo(string);

        string = WordUtils.capitalizeFully(valueMap.get(Columns.cognome.getTitoloColonna()));
        insegnante.setCognome(string);

        string = WordUtils.capitalizeFully(valueMap.get(Columns.nome.getTitoloColonna()));
        insegnante.setNome(string);

        string = WordUtils.capitalizeFully(valueMap.get(Columns.ordinescuola.getTitoloColonna()));
        OrdineScuola ordine = getOrdine(string);
        insegnante.setOrdineScuola(ordine);

        string = WordUtils.capitalizeFully(valueMap.get(Columns.materia.getTitoloColonna()));
        insegnante.setMaterie(string);

        string = valueMap.get(Columns.email.getTitoloColonna()).toLowerCase();
        string = string.trim();
        insegnante.setEmail(string);

        string = WordUtils.capitalizeFully(valueMap.get(Columns.indirizzo.getTitoloColonna()));
        insegnante.setIndirizzo1(string);

        // indirizzo 2
        String cap = valueMap.get(Columns.cap.getTitoloColonna());
        cap = cap.trim();
        String loc = valueMap.get(Columns.localita.getTitoloColonna());
        loc = loc.trim();
        loc = WordUtils.capitalizeFully(loc);
        String prov = valueMap.get(Columns.provincia.getTitoloColonna());
        prov = prov.trim();
        String full = cap;
        if (!loc.equals("")) {
            full += " " + loc;
        }
        if (!prov.equals("")) {
            full += " " + prov;
        }
        insegnante.setIndirizzo2(full);

        // telefono
        String tel = valueMap.get(Columns.telefono.getTitoloColonna());
        tel = tel.trim();
        String cell = valueMap.get(Columns.cell.getTitoloColonna());
        cell = cell.trim();
        String telfull = "";
        if (!tel.equals("")) {
            telfull += "tel. " + tel;
        }
        if (!cell.equals("")) {
            if (!telfull.equals("")) {
                telfull += ", ";
            }
            telfull += "cell. " + cell;
        }
        insegnante.setTelefono(telfull);

        return insegnante;

    }

    private OrdineScuola getOrdine(String sigla) {
        OrdineScuola ordine = (OrdineScuola) CompanyQuery.getFirstEntity(OrdineScuola.class, OrdineScuola_.sigla, sigla);
        return ordine;
    }

    public void addDoneListener() {

    }

    private DoneListener getDoneListener() {
        return listener;
    }

    /**
     * Enum of the desired coulmns to import.
     * <p>
     * Here we map our abstract column names to the real column names in the Excel file
     */
    private enum Columns {

        titolo("TITOLO"),

        cognome("COGNOME"),

        nome("NOME"),

        ordinescuola("ORDINE", "sigle come da tabella Ordini Scuole"),

        materia("MATERIE", "materie insegnate"),

        email("E-MAIL"),

        cap("CAP"),

        indirizzo("INDIRIZZO"),

        localita("LOCALITA"),

        provincia("PROVINCIA", "sigla standard 2 lettere"),

        telefono("TELEFONO", "numero di telefono fisso"),

        cell("MOBILE", "numero di telefono cellulare");

        private String titoloColonna;
        private String hintColonna;

        Columns(String name, String hint) {
            this.titoloColonna = name;
            this.hintColonna = hint;
        }

        Columns(String name) {
            this(name, "");
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
                html += "<li>";
                html += "<b>" + c.getTitoloColonna() + "</b>";
                String hint = c.getHintColonna();
                if ((hint != null) & (!hint.equals(""))) {
                    html += " (" + c.getHintColonna() + ")";
                }
                html += "</li>";
            }
            html += "</ul>";
            return html;
        }

        public String getTitoloColonna() {
            return titoloColonna;
        }

        public String getHintColonna() {
            return hintColonna;
        }

    }

    public interface DoneListener {
        public void done(BaseDialog dialog);
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

}
