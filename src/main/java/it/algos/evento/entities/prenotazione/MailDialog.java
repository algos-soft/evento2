package it.algos.evento.entities.prenotazione;

import com.vaadin.data.Property;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import it.algos.evento.entities.insegnante.Insegnante;
import it.algos.evento.entities.lettera.Lettera;
import it.algos.evento.entities.lettera.LetteraKeys;
import it.algos.evento.entities.lettera.ModelliLettere;
import it.algos.evento.entities.mailing.DestWrap;
import it.algos.evento.entities.mailing.MailManager;
import it.algos.evento.entities.mailing.MailWrap;
import it.algos.evento.entities.scuola.Scuola;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.field.ArrayComboField;
import it.algos.webbase.web.field.TextField;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("serial")
public class MailDialog extends ConfirmDialog {

    private ArrayList<Long> listaPrenotazioniIds;
    private TextField titoloField;
    private ArrayComboField letteraField;
    private OptionGroup destinatariOptions;
    private String itemReferente = "Referente della prenotazione";
    private String itemScuola = "Mail della scuola";
    private String itemEntrambi = "Entrambi gli indirizzi";
    private String currentItem = "";


    public MailDialog(ArrayList<Long> listaPrenotazioniIds) {
        super(null);
        this.listaPrenotazioniIds = listaPrenotazioniIds;
        this.inizializzazioneGUI();
    }// end of constructor


    /**
     * Creazione grafica del dialogo
     */
    private void inizializzazioneGUI() {
        setTitle("Mailing");
        setMessage("Gestione mailing integrata");

        titoloField = new TextField("Titolo del mailing");
        titoloField.setRequired(true);
        addComponent(titoloField);

        letteraField = new ArrayComboField(this.getLettere(), "Seleziona una lettera");
        letteraField.setRequired(true);
        addComponent(letteraField);

        destinatariOptions = new OptionGroup();
        currentItem = itemReferente;
        destinatariOptions.setImmediate(true);
        destinatariOptions.addItem(itemReferente);
        destinatariOptions.addItem(itemScuola);
        destinatariOptions.addItem(itemEntrambi);
        destinatariOptions.select(currentItem);
        destinatariOptions.addValueChangeListener(new Property.ValueChangeListener() {
            Object obj = null;
            String itemText = "";

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                obj = destinatariOptions.getValue();
                if (obj != null && obj instanceof String) {
                    itemText = (String) obj;
                    setCurrentItem(itemText);
                }// fine del blocco if
            }// end of method
        });// end of inner class Listener
        addComponent(destinatariOptions);
    }// end of method

    /**
     * Dialogo confermato <br>
     * Recupera i valori dalla GUI <br>
     * Recupera le informazioni e prepara un wrapper di tiupo MailWrap, da passare a MailManager
     * MailWrapper necessita del titolo, della lettera e della lista (DestWrap) dei destinatari
     */
    private void dialogoConfermato() {
        ArrayList<DestWrap> destinatari = this.getDestinatari();

        if (destinatari != null && destinatari.size() > 0) {
            this.gestioneSpedizioni(destinatari);
        } else {
            new Notification("Non risulta nessun destinatario del mailing",
                    "Controlla le opzioni",
                    Notification.TYPE_ERROR_MESSAGE, true)
                    .show(Page.getCurrent());
        }// fine del blocco if-else

    }// end of method


    /**
     * Gestione delle spedizioni <br>
     */
    private void gestioneSpedizioni(ArrayList<DestWrap> destinatari) {
        String titolo = this.getTitolo();
        Lettera lettera = this.getLettera();
        MailWrap wrap = new MailWrap(titolo, lettera, destinatari);
        new MailManager(wrap);
    }// end of method

    /**
     * Elenco delle lettere <br>
     */
    private Object[] getLettere() {
        Object[] values = null;
        ArrayList<Lettera> lista = new ArrayList<>();
        ArrayList<Lettera> listaAllDB = Lettera.readAll();
        ArrayList<String> listaModelliSigla = ModelliLettere.getAllDbCode();
        String sigla;

        for (Lettera lettera : listaAllDB) {
            sigla = lettera.getSigla();
            if (!listaModelliSigla.contains(sigla)) {
                lista.add(lettera);
            }// fine del blocco if
        } // fine del ciclo for-each

        if (lista != null) {
            values = lista.toArray();
        }// fine del blocco if

        return values;
    }// end of method


    private boolean dialogoValido() {
        boolean status = false;

        if (esisteTitolo() && esisteLettera() && esistonoDestinatari()) {
            status = true;
        } else {

            if (!esistonoDestinatari()) {
                new Notification("Non risulta nessun destinatario del mailing",
                        "Controlla le opzioni",
                        Notification.TYPE_ERROR_MESSAGE, true)
                        .show(Page.getCurrent());
            } else {
                if (!esisteTitolo()) {
                    new Notification("Manca il titolo del mailing",
                            "Devi inserirlo",
                            Notification.TYPE_ERROR_MESSAGE, true)
                            .show(Page.getCurrent());
                } else {
                    if (!esisteLettera()) {
                        new Notification("Non hai selezionato nessuna lettera",
                                "Devi selezionarla",
                                Notification.TYPE_ERROR_MESSAGE, true)
                                .show(Page.getCurrent());
                    }// fine del blocco if-else
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if-else

        return status;
    }// end of method

    @Override
    protected void onConfirm() {
        if (dialogoValido()) {
            super.onConfirm();
            this.dialogoConfermato();
        }// fine del blocco if
    }// end of method

    private void setCurrentItem(String itemText) {
        this.currentItem = itemText;
    }// end of method

    private String getTitolo() {
        return titoloField.getValue();
    }// end of method

    private boolean esisteTitolo() {
        boolean status = false;
        String titolo = getTitolo();

        if (titolo != null && !titolo.equals("")) {
            status = true;
        }// fine del blocco if

        return status;
    }// end of method

    private Lettera getLettera() {
        return (Lettera) letteraField.getValue();
    }// end of method

    private boolean esisteLettera() {
        boolean status = false;
        Lettera lettera = getLettera();

        if (lettera != null) {
            status = true;
        }// fine del blocco if

        return status;
    }// end of method

    /**
     * Recupera le opzioni (radiobottoni) dalla GUI <br>
     * Sviluppa l'elenco dei destinatari <br>
     * Chi non ha indirizzo email non viene considerato <br>
     */
    private ArrayList<DestWrap> getDestinatari() {
        ArrayList<DestWrap> destinatari = new ArrayList<DestWrap>();
        DestWrap wrap = null;
        boolean usaReferente = false;
        boolean usaScuola = false;

        if (currentItem.equals(itemReferente)) {
            usaReferente = true;
        }// fine del blocco if

        if (currentItem.equals(itemScuola)) {
            usaScuola = true;
        }// fine del blocco if

        if (currentItem.equals(itemEntrambi)) {
            usaReferente = true;
            usaScuola = true;
        }// fine del blocco if

        if (listaPrenotazioniIds != null && listaPrenotazioniIds.size() > 0) {
            for (Long idPren : listaPrenotazioniIds) {
                if (usaReferente) {
                    wrap = getWrapRef(idPren);
                    if (wrap != null) {
                        wrap = this.setWrapInfo(idPren, wrap);
                        destinatari.add(wrap);
                    }// fine del blocco if
                }// fine del blocco if
                if (usaScuola) {
                    wrap = getWrapScuola(idPren);
                    if (wrap != null) {
                        wrap = this.setWrapInfo(idPren, wrap);
                        destinatari.add(wrap);
                    }// fine del blocco if
                }// fine del blocco if
            } // fine del ciclo for-each
        }// fine del blocco if

        return destinatari;
    }// end of method

    /**
     * Costruisce il singolo wrap
     */
    private DestWrap getWrapRef(long idPrenotazione) {
        DestWrap wrap = null;
        Prenotazione prenotazione = null;
        String destRef = "";

        if (idPrenotazione > 0) {
            prenotazione = Prenotazione.read(idPrenotazione);
        }// fine del blocco if

        if (prenotazione != null) {
            destRef = prenotazione.getEmailRiferimento();
        }// fine del blocco if

        if (!destRef.equals("")) {
            wrap = new DestWrap(destRef);
        }// fine del blocco if

        return wrap;
    }// end of method

    /**
     * Costruisce il singolo wrap
     */
    private DestWrap getWrapScuola(long idPrenotazione) {
        DestWrap wrap = null;
        Prenotazione prenotazione = null;
        Scuola scuola = null;
        String destScuola = "";

        if (idPrenotazione > 0) {
            prenotazione = Prenotazione.read(idPrenotazione);
        }// fine del blocco if

        if (prenotazione != null && !prenotazione.isPrivato()) {
            scuola = prenotazione.getScuola();
        }// fine del blocco if

        if (scuola != null) {
            destScuola = scuola.getEmail();
        }// fine del blocco if

        if (!destScuola.equals("")) {
            wrap = new DestWrap(destScuola);
        }// fine del blocco if

        return wrap;
    }// end of method

    /**
     * Aggiunge le informazioni della persona
     */
    private DestWrap setWrapInfo(Long idPren, DestWrap wrap) {
        Prenotazione prenotazione = null;
        HashMap<String, String> escapeMap = new HashMap<String, String>();
        Insegnante insegnante;

        if (idPren > 0) {
            prenotazione = Prenotazione.read(idPren);
        }// fine del blocco if

        if (prenotazione != null) {
            insegnante = prenotazione.getInsegnante();
            if (insegnante != null) {
                escapeMap.put(LetteraKeys.nomeInsegnante.getKey(), insegnante.getNome());
                escapeMap.put(LetteraKeys.cognomeInsegnante.getKey(), insegnante.getCognome());
                wrap.setMappa(escapeMap);
            }// fine del blocco if
        }// fine del blocco if

        return wrap;
    }// end of method

    private boolean esistonoPrenotazioni() {
        boolean status = false;

        if (listaPrenotazioniIds != null && listaPrenotazioniIds.size() > 0) {
            status = true;
        }// fine del blocco if

        return status;
    }// end of method

    private boolean esistonoDestinatari() {
        boolean status = false;
        ArrayList<DestWrap> destinatari = getDestinatari();

        if (destinatari != null && destinatari.size() > 0) {
            status = true;
        }// fine del blocco if

        return status;
    }// end of method

}// end of class
