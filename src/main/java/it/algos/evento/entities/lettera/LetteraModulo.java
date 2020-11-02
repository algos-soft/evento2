package it.algos.evento.entities.lettera;

import com.vaadin.data.Item;
import it.algos.evento.EventoApp;
import it.algos.webbase.multiazienda.CompanyModule;
import it.algos.webbase.web.AlgosApp;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.table.TablePortal;

import javax.persistence.metamodel.Attribute;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@SuppressWarnings("serial")
public class LetteraModulo extends CompanyModule {

    private static final String ATTACHMENTS_PREFIX = "[ATTACHMENTS]";

    /**
     * Costruttore senza parametri
     */
    public LetteraModulo() {
        super(Lettera.class);
    }// end of constructor


    // come default usa il titolo standard
    // può essere sovrascritto nelle sottoclassi specifiche
    protected String getCaptionSearch() {
        return "lettere";
    }// end of method

    // come default spazzola tutti i campi della Entity
    // può essere sovrascritto nelle sottoclassi specifiche
    // serve anche per l'ordine con cui vengono presentati i campi
    protected Attribute<?, ?>[] creaFieldsList() {
        return new Attribute[]{Lettera_.sigla, Lettera_.oggetto, Lettera_.allegati};
    }// end of method

    // come default spazzola tutti i campi della Entity
    // non garantisce l'ordine con cui vengono presentati i campi
    // può essere sovrascritto nelle sottoclassi specifiche (garantendo l'ordine)
    // può mostrare anche il campo ID, oppure no
    // se si vuole differenziare tra Table, Form e Search, sovrascrivere
    // creaFieldsList, creaFieldsForm e creaFieldsSearch
    protected Attribute<?, ?>[] creaFieldsAll() {
        return new Attribute[]{Lettera_.sigla, Lettera_.oggetto, Lettera_.testo, Lettera_.allegati};
    }// end of method

    @Override
    public ATable createTable() {
        return (new LetteraTable(this));
    }// end of method

    @Override
    public ModuleForm createForm(Item item) {
        return (new LetteraForm(this, item));
    }// end of method

    /**
     * Create the Table Portal
     *
     * @return the TablePortal
     */
    public TablePortal createTablePortal() {
        return new LetteraTablePortal(this);
    }// end of method

    /**
     * Crea la lettera demo per un dato modello.
     * <p>
     *
     * @param modello il modello
     * @return la lettera demo
     */
    public static Lettera getLetteraDemo(ModelliLettere modello) {
        String code = modello.getDbCode();
        String oggetto = modello.getOggettoDefault();
        String testo = getTestoDemo(modello);
        String allegati = getStringaAllegati(modello);
        Lettera lettera = new Lettera(code, oggetto, testo);
        lettera.setHtml(true);
        lettera.setAllegati(allegati);
        return lettera;
    }


    /**
     * Restituisce il testo demo per un dato modello.
     * <p>
     *
     * @param modello il modello
     * @return il testo demo per il modello specificato
     */
    private static String getTestoDemo(ModelliLettere modello) {
        String testo = "Da scrivere";
        Path path = getDemoFile(modello);
        if (path != null) {
            testo = "";
            try {
                List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
                for (String s : lines) {
                    if (!s.startsWith(ATTACHMENTS_PREFIX)) {    // skip attachments line
                        testo += s + "\n";
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return testo;
    }

    /**
     * Restituisce la stringa allegati demo per un dato modello.
     * <p>
     * La riga allegati è contenuta nello stesso file demo del testo ed è prefissata con il codice "[ATTACHMENTS]"
     *
     * @param modello il modello
     * @return la stringa allegati per il modello specificato
     */
    private static String getStringaAllegati(ModelliLettere modello) {
        String testo = "";
        Path path = getDemoFile(modello);
        if (path != null) {
            testo = "";
            try {
                List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
                for (String s : lines) {
                    if (s.startsWith(ATTACHMENTS_PREFIX)) {    // read only attachments line
                        testo = s.substring(ATTACHMENTS_PREFIX.length(), s.length());
                        testo = testo.trim();
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return testo;
    }

    /**
     * Ritorna un file demo per un dato modello.
     * <p>
     * Cerca un file con il nome uguale al codice db del modello e suffisso .txt nei dati demo.<br>
     * Se il file ritornato non è nullo, ha già controllato che il file esista.<br>
     *
     * @param modello il modello
     * @return il file demo per il modello specificato
     */
    private static Path getDemoFile(ModelliLettere modello) {
        Path demofile = null;
        String filename = modello.getDbCode() + ".txt";
        ServletContext svlContext = EventoApp.getServletContext();
        String fullPath = svlContext.getRealPath("/"+AlgosApp.DEMODATA_FOLDER_NAME + "lettere/" + filename);
        Path path = Paths.get(fullPath);
        File file = path.toFile();
        if (file.exists()) {
            demofile = path;
        }
        return demofile;
    }


}// end of class
