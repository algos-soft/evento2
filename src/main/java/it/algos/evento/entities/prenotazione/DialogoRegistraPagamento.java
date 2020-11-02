package it.algos.evento.entities.prenotazione;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.DateField;
import it.algos.evento.EventoBootStrap;
import it.algos.evento.entities.comune.Comune;
import it.algos.evento.entities.insegnante.Insegnante;
import it.algos.evento.entities.modopagamento.ModoPagamento;
import it.algos.evento.entities.rappresentazione.Rappresentazione;
import it.algos.evento.entities.scuola.Scuola;
import it.algos.evento.entities.spedizione.Spedizione;
import it.algos.evento.pref.CompanyPrefs;
import it.algos.webbase.multiazienda.ERelatedComboField;
import it.algos.webbase.web.component.HorizontalLine;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.field.*;
import it.algos.webbase.web.lib.Lib;
import it.algos.webbase.web.lib.LibDate;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@SuppressWarnings("serial")
public class DialogoRegistraPagamento extends DialogoConfermaInvioManuale {

    private PagamentoRegistratoListener prListener;

    private EntityManager entityManager;
    private VerticalLayout placeholderLayout;
    private Field numInteri;
    private Field numRidotti;
    private Field numDisabili;
    private Field numAccomp;

    private DecimalField impInteri;
    private DecimalField impRidotti;
    private DecimalField impDisabili;
    private DecimalField impAccomp;

    private DecimalField totInteri;
    private DecimalField totRidotti;
    private DecimalField totDisabili;
    private DecimalField totAccomp;

    private DecimalField totPrevisto;
    private DecimalField totPagato;

    private DateField dataCompetenza;
    private RelatedComboField pagatoAMezzo;

    private CheckBoxField checkConfermato;
    private CheckBoxField checkRicevuto;

    private boolean emailSent;    // viene acceso quando si tenda ti inviare una email
    private boolean emailFailed;    // viene acceso se l'invio email fallisce

    // list of fields subject to validation
    ArrayList<Field> validatableFields = new ArrayList<Field>();

    public void setPagamentoRegistratoListener(PagamentoRegistratoListener l) {
        prListener = l;
    }

    public interface PagamentoRegistratoListener {
        void pagamentoRegistrato(boolean confermato, boolean ricevuto, boolean emailSent, boolean emailFailed);
    }

    /**
     * @param pren          la prenotazione di cui registrare il pagamento
     * @param entityManager l'entity manager per le operazioni
     */
    public DialogoRegistraPagamento(Prenotazione pren, EntityManager entityManager) {
        super(pren, "Registrazione pagamento", null, false);
        this.entityManager = entityManager;

        setConfirmButtonText("Registra");

        placeholderLayout.addComponent(createTitle());
        placeholderLayout.addComponent(createGrid());
        placeholderLayout.addComponent(new HorizontalLine());
        placeholderLayout.addComponent(getEmailComponent());

        caricaDatiDaPrenotazione();
        syncTotali();
        addListeners();

        // alla confrma definitiva esegue le movimentazioni
        addConfirmListener(new ConfirmListener() {
            @Override
            public void confirmed(ConfirmDialog confirmDialog) {
                dialogoConfermato();
            }
        });

    }


    private void createFields() {
        numInteri = new IntegerField();
        //numInteri.addValidator(new IntegerRangeValidator("Specificare quanti posti", 1, null));
        //validatableFields.add(numInteri);
        numRidotti = new IntegerField();
        numDisabili = new IntegerField();
        numAccomp = new IntegerField();

        impInteri = new DecimalField();
        impInteri.setAlignment(FieldAlignment.right);
        impInteri.setReadOnly(true);
        impRidotti = new DecimalField();
        impRidotti.setAlignment(FieldAlignment.right);
        impRidotti.setReadOnly(true);
        impDisabili = new DecimalField();
        impDisabili.setAlignment(FieldAlignment.right);
        impDisabili.setReadOnly(true);
        impAccomp = new DecimalField();
        impAccomp.setAlignment(FieldAlignment.right);
        impAccomp.setReadOnly(true);

        totInteri = new DecimalField();
        totInteri.setAlignment(FieldAlignment.right);
        totInteri.setReadOnly(true);
        totRidotti = new DecimalField();
        totRidotti.setAlignment(FieldAlignment.right);
        totRidotti.setReadOnly(true);
        totDisabili = new DecimalField();
        totDisabili.setAlignment(FieldAlignment.right);
        totDisabili.setReadOnly(true);
        totAccomp = new DecimalField();
        totAccomp.setAlignment(FieldAlignment.right);
        totAccomp.setReadOnly(true);

        totPrevisto = new DecimalField();
        totPrevisto.setAlignment(FieldAlignment.right);
        totPrevisto.setReadOnly(true);

        totPagato = new DecimalField();
        totPagato.setAlignment(FieldAlignment.right);
        //totPagato.addValidator(new BigDecimalRangeValidator("Inserire il totale pagato", new BigDecimal(1.0), null));
        //validatableFields.add(totPagato);

        dataCompetenza = new DateField();
        dataCompetenza.setValue(LibDate.today());

        pagatoAMezzo = new ERelatedComboField(ModoPagamento.class);
        pagatoAMezzo.addValidator(new NullValidator("Specificare un mezzo di pagamento", false));
        validatableFields.add(pagatoAMezzo);

        checkConfermato = new CheckBoxField("Pagamento confermato");
        checkConfermato.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent valueChangeEvent) {
                syncMail();
            }
        });
        checkRicevuto = new CheckBoxField("Pagamento ricevuto");
        checkRicevuto.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent valueChangeEvent) {
                syncMail();
            }
        });

        // regola i mail checkboxes
        syncMail();

    }

    /**
     * The component shown in the detail area.
     */
    protected VerticalLayout createDetailComponent() {
        placeholderLayout = new VerticalLayout();
        placeholderLayout.setSpacing(true);
        placeholderLayout.setMargin(true);
        return placeholderLayout;
    }


    /**
     * Sincronizza i checkboxes dell'invio email
     */
    private void syncMail() {
        sendRef.setValue(false);
        sendScuola.setValue(false);

        CompanyPrefs prefGen = null;
        CompanyPrefs prefReferente = null;
        CompanyPrefs prefScuola = null;
        CompanyPrefs prefNP = null;

        // se confermato usa le relative preferenze
        if (isConfermato()) {
            prefGen = CompanyPrefs.sendMailConfPaga;
            prefReferente = CompanyPrefs.sendMailConfPagaRef;
            prefScuola = CompanyPrefs.sendMailConfPagaScuola;
            prefNP = CompanyPrefs.sendMailConfPagaNP;
        }

        // se ricevuto usa le relative preferenze e va sopra al confermato (è più forte)
        if (isRicevuto()) {
            prefGen = CompanyPrefs.sendMailRegisPaga;
            prefReferente = CompanyPrefs.sendMailRegisPagaRef;
            prefScuola = CompanyPrefs.sendMailRegisPagaScuola;
            prefNP = CompanyPrefs.sendMailRegisPagaNP;
        }

        // regola i checkboxes in base alle preferenze
        if (prefGen != null) {
            if (!prefGen.getBool()) {
                sendRef.setValue(false);
                sendScuola.setValue(false);
            } else {

                // qui l'opzione generale è attiva, copia i flag per Referente e Scuola
                sendRef.setValue(prefReferente.getBool());
                sendScuola.setValue(prefScuola.getBool());

                // controllo opzione No Privati
                if (getPrenotazione().isPrivato()) {
                    if (prefNP.getBool()) {
                        sendRef.setValue(false);
                    }
                }
            }
        }


    }


    private Component createTitle() {
        Prenotazione pren = getPrenotazione();
        Rappresentazione rapp = pren.getRappresentazione();

        String strRapp = rapp.toString();
        String str = strRapp;

        Scuola scuola = pren.getScuola();
        if (scuola != null) {
            String strScuola = "";
            strScuola = scuola.getNome();
            Comune comune = scuola.getComune();
            if (comune != null) {
                strScuola += " - " + comune.toString();
            }
            str += "<br>" + strScuola;
        }

        Insegnante ins = pren.getInsegnante();
        if (ins != null) {
            String nome = "";
            nome += ins.getCognome() + " " + ins.getNome();
            str += "<br>" + nome;
        }

        str += "</b>";
        Label label = new Label(str);
        label.setContentMode(ContentMode.HTML);
        return label;
    }

    private GridLayout createGrid() {
        Label label;

        createFields();

        GridLayout grid = new GridLayout(6, 10);
        grid.setSpacing(true);

        label = new Label("Posti");
        grid.addComponent(label, 1, 0);
        grid.setComponentAlignment(label, com.vaadin.ui.Alignment.TOP_CENTER);
        label = new Label("Prezzo");
        grid.addComponent(label, 2, 0);
        grid.setComponentAlignment(label, com.vaadin.ui.Alignment.TOP_RIGHT);
        label = new Label("Importo");
        grid.addComponent(label, 3, 0);
        grid.setComponentAlignment(label, com.vaadin.ui.Alignment.TOP_RIGHT);

        grid.addComponent(new Label("Interi"), 0, 1);
        grid.addComponent(new Label("Ridotti"), 0, 2);
        grid.addComponent(new Label("Disabili"), 0, 3);
        grid.addComponent(new Label("Accomp."), 0, 4);

        grid.addComponent(numInteri, 1, 1);
        grid.addComponent(numRidotti, 1, 2);
        grid.addComponent(numDisabili, 1, 3);
        grid.addComponent(numAccomp, 1, 4);

        grid.addComponent(impInteri, 2, 1);
        grid.addComponent(impRidotti, 2, 2);
        grid.addComponent(impDisabili, 2, 3);
        grid.addComponent(impAccomp, 2, 4);

        grid.addComponent(totInteri, 3, 1);
        grid.addComponent(totRidotti, 3, 2);
        grid.addComponent(totDisabili, 3, 3);
        grid.addComponent(totAccomp, 3, 4);


        // riga tot previsto
        label = new Label("Totale previsto");
        grid.addComponent(label, 2, 5);
        grid.setComponentAlignment(label, com.vaadin.ui.Alignment.MIDDLE_RIGHT);

        grid.addComponent(totPrevisto, 3, 5);
        grid.setComponentAlignment(totPrevisto, com.vaadin.ui.Alignment.MIDDLE_RIGHT);

        // riga tot pagato
        label = new Label("Totale pagato");
        grid.addComponent(label, 2, 6);
        grid.setComponentAlignment(label, com.vaadin.ui.Alignment.MIDDLE_RIGHT);

        grid.addComponent(totPagato, 3, 6);
        grid.setComponentAlignment(totPagato, com.vaadin.ui.Alignment.MIDDLE_RIGHT);

        label = new Label("in data");
        grid.addComponent(label, 4, 5);
        grid.setComponentAlignment(label, com.vaadin.ui.Alignment.MIDDLE_RIGHT);
        grid.addComponent(dataCompetenza, 5, 5);
        grid.setComponentAlignment(dataCompetenza, Alignment.MIDDLE_LEFT);

        label = new Label("a mezzo");
        grid.addComponent(label, 4, 6);
        grid.setComponentAlignment(label, com.vaadin.ui.Alignment.MIDDLE_RIGHT);
        grid.addComponent(pagatoAMezzo, 5, 6);
        grid.setComponentAlignment(pagatoAMezzo, com.vaadin.ui.Alignment.MIDDLE_RIGHT);

        // checkboxes
        grid.addComponent(checkConfermato, 5, 8);
        grid.addComponent(checkRicevuto, 5, 9);

        return grid;
    }

    /**
     * Aggiunge i listeners ai campi editabili
     */
    private void addListeners() {

        numInteri.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                syncTotali();
            }
        });

        numRidotti.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                syncTotali();
            }
        });

        numDisabili.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                syncTotali();
            }
        });

        numAccomp.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                syncMail();
                syncTotali();
            }
        });


        checkConfermato.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent valueChangeEvent) {
                // se tolgo confermato va via anche ricevuto
                if (!checkConfermato.getValue()) {
                    checkRicevuto.setValue(false);
                }
                syncMail();
            }
        });

        checkRicevuto.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                // se metto ricevuto metto anche confermato
                if (checkRicevuto.getValue()) {
                    checkConfermato.setValue(true);
                }
                syncMail();
            }
        });


    }

    /**
     * Carica i dati dalla prenotazione ai campi del dialogo
     */
    @SuppressWarnings("unchecked")
    private void caricaDatiDaPrenotazione() {

        Prenotazione pren = getPrenotazione();

        numInteri.setValue(pren.getNumInteri());
        numRidotti.setValue(pren.getNumRidotti());
        numDisabili.setValue(pren.getNumDisabili());
        numAccomp.setValue(pren.getNumAccomp());


        // pone tutto rw
        impInteri.setReadOnly(false);
        impRidotti.setReadOnly(false);
        impDisabili.setReadOnly(false);
        impAccomp.setReadOnly(false);

        // assegna i valori
        impInteri.setValue(pren.getImportoIntero());
        impRidotti.setValue(pren.getImportoRidotto());
        impDisabili.setValue(pren.getImportoDisabili());
        impAccomp.setValue(pren.getImportoAccomp());

        totPagato.setValue(pren.getImportoPagato());

        ModoPagamento modo = pren.getModoPagamento();
        if (modo != null) {
            pagatoAMezzo.setValue(modo.getId());
        }

        checkConfermato.setValue(pren.isPagamentoConfermato());

        // rimette tutto ro
        impInteri.setReadOnly(true);
        impRidotti.setReadOnly(true);
        impDisabili.setReadOnly(true);
        impAccomp.setReadOnly(true);

    }

    /**
     * Sincronizza i totali
     */
    private void syncTotali() {

        // pone tutto rw
        totInteri.setReadOnly(false);
        totRidotti.setReadOnly(false);
        totDisabili.setReadOnly(false);
        totAccomp.setReadOnly(false);
        totPrevisto.setReadOnly(false);


        // effettua i calcoli
        totInteri.setValue(multiplyFields(numInteri, impInteri));
        totRidotti.setValue(multiplyFields(numRidotti, impRidotti));
        totDisabili.setValue(multiplyFields(numDisabili, impDisabili));
        totAccomp.setValue(multiplyFields(numAccomp, impAccomp));
        totPrevisto.setValue(getTotPrevisto());

        // ripristina lo stato di read-only
        totInteri.setReadOnly(true);
        totRidotti.setReadOnly(true);
        totDisabili.setReadOnly(true);
        totAccomp.setReadOnly(true);
        totPrevisto.setReadOnly(true);
    }

    @SuppressWarnings("rawtypes")
    private BigDecimal multiplyFields(Field f1, Field f2) {
        BigDecimal bd1 = Lib.getBigDecimal(f1.getValue());
        BigDecimal bd2 = Lib.getBigDecimal(f2.getValue());
        return bd1.multiply(bd2);
    }

    private BigDecimal getTotPrevisto() {
        BigDecimal bdRet = new BigDecimal(0);
        bdRet = bdRet.add(Lib.getBigDecimal(totInteri.getValue()));
        bdRet = bdRet.add(Lib.getBigDecimal(totRidotti.getValue()));
        bdRet = bdRet.add(Lib.getBigDecimal(totDisabili.getValue()));
        bdRet = bdRet.add(Lib.getBigDecimal(totAccomp.getValue()));
        return bdRet;
    }

    @Override
    protected void onConfirm() {
        boolean cont = true;

        // check validators
        String error = checkDataValid();
        if (!error.equals("")) {
            Notification.show(null, error, Notification.Type.WARNING_MESSAGE);
            cont = false;
        }

        // check importo pagato
        if (cont) {
            if (!totPagato.getValue().equals(totPrevisto.getValue())) {
                ConfirmDialog dialog = new ConfirmDialog(new ConfirmDialog.Listener() {

                    @Override
                    public void onClose(ConfirmDialog dialog, boolean confirmed) {
                        if (confirmed) {
                            DialogoRegistraPagamento.super.onConfirm();
                        }
                    }
                });
                dialog.setTitle("Verifica importo");
                dialog.setMessage("Attenzione! L'importo pagato è diverso da quello previsto.");
                dialog.setConfirmButtonText("Continua");
                dialog.show(getUI());
            } else {
                DialogoRegistraPagamento.super.onConfirm();
            }
        }

    }

    /**
     * Valida tutti i field validabili e accumula tutti i messaggi in una stringa
     * <p>
     * Se la stringa ritornata è vuota la validazione è passata
     */
    private String checkDataValid() {
        String string = "";

        // validate fields
        for (Field field : validatableFields) {
            Collection<Validator> validators = field.getValidators();
            for (Validator validator : validators) {
                try {
                    validator.validate(field.getValue());
                } catch (Validator.InvalidValueException e) {
                    if (!string.equals("")) {
                        string += "\n";
                    }
                    string += e.getMessage();
                }
            }
        }

        // validate checkboxes
        if ((!checkConfermato.getValue()) && (!checkRicevuto.getValue())) {
            if (!string.equals("")) {
                string += "\n";
            }
            string += "Specificare se il pagamento è confermato e/o ricevuto";
        }

        // validate importo pagato
        if (totPagato.getValue().compareTo(BigDecimal.ZERO) <= 0) {
            if (!string.equals("")) {
                string += "\n";
            }
            string += "L'importo pagato deve essere maggiore di zero";
        }

        // validate data competenza
        if (dataCompetenza.isEmpty()) {
            if (!string.equals("")) {
                string += "\n";
            }
            string += "Specificare la data di competenza";
        }


        return string;
    }


    /**
     * Conferma definitiva del dialogo
     * Lancia un nuovo thread che esegue le movimentazioni
     */
    private void dialogoConfermato() {
        int numInteri = getNumInteri();
        int numRidotti = getNumRidotti();
        int numDisabili = getNumDisabili();
        int numAccomp = getNumAccomp();
        BigDecimal importoPagato = getImportoPagato();
        ModoPagamento mezzo = getModoPagamento();
        boolean confermato = isConfermato();
        boolean ricevuto = isRicevuto();
        String user = EventoBootStrap.getUsername();

        // esegue l'operazione di conferma e l'invio mail in un thread separato
        new Thread(
                () -> {

                    emailSent = false;
                    emailFailed = false;
                    try {


                        Spedizione sped=PrenotazioneModulo.doConfermaRegistrazionePagamento(
                                getPrenotazione(),
                                entityManager,
                                isConfermato(),
                                isRicevuto(),
                                getDataCompetenza(),
                                numInteri,
                                numRidotti,
                                numDisabili,
                                numAccomp,
                                importoPagato,
                                mezzo,
                                getDestinatari(),
                                user);

                        // registra l'esito della spedizione
                        if(sped!=null){
                            emailSent=sped.isSpedita();
                        }

                    } catch (EmailFailedException e) {
                        PrenotazioneModulo.notifyEmailFailed(e);
                        emailFailed = true;
                    }

                    // notifica il listener se registrato
                    if (prListener != null) {
                        prListener.pagamentoRegistrato(confermato, ricevuto, emailSent, emailFailed);
                    }

                }

        ).start();

    }



    public int getNumInteri() {
        return Lib.getInt(numInteri.getValue());
    }

    public int getNumRidotti() {
        return Lib.getInt(numRidotti.getValue());
    }

    public int getNumDisabili() {
        return Lib.getInt(numDisabili.getValue());
    }

    public int getNumAccomp() {
        return Lib.getInt(numAccomp.getValue());
    }

    public BigDecimal getImportoPrevisto() {
        return totPrevisto.getValue();
    }

    public BigDecimal getImportoPagato() {
        return totPagato.getValue();
    }

    public ModoPagamento getModoPagamento() {
        ModoPagamento modo = null;
        Object bean = pagatoAMezzo.getSelectedBean();
        if (bean != null) {
            modo = (ModoPagamento) bean;
        }
        return modo;
    }

    public boolean isConfermato() {
        return checkConfermato.getValue();
    }

    public boolean isRicevuto() {
        return checkRicevuto.getValue();
    }

    public Date getDataCompetenza() {
        return dataCompetenza.getValue();
    }

    /**
     * Mostra una notifica con l'esito della operazione
     */
    public void notificaEsito() {
        String msg = "";
        if (isConfermato()) {
            msg = "Pagamento confermato";
        }
        if (isRicevuto()) {
            msg = "Pagamento registrato";
        }

        String strEmail = "";
        if (emailSent) {
            strEmail = "e-mail inviata";
        }
        if (emailFailed) {
            strEmail = "Invio e-mail fallito";
        }

        Notification notification = new Notification(msg, strEmail, Notification.Type.HUMANIZED_MESSAGE);
        notification.setDelayMsec(-1);
        notification.show(Page.getCurrent());

    }

}
