package it.algos.evento.entities.prenotazione;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.TextArea;
import it.algos.evento.EventoApp;
import it.algos.evento.EventoBootStrap;
import it.algos.evento.entities.company.Company;
import it.algos.evento.entities.evento.Evento;
import it.algos.evento.entities.evento.Evento_;
import it.algos.evento.entities.insegnante.Insegnante;
import it.algos.evento.entities.insegnante.InsegnanteForm;
import it.algos.evento.entities.insegnante.Insegnante_;
import it.algos.evento.entities.lettera.ModelliLettere;
import it.algos.evento.entities.modopagamento.ModoPagamento;
import it.algos.evento.entities.prenotazione.PrenotazioneFormToolbar.PrenotazioneFormToolbarListener;
import it.algos.evento.entities.prenotazione.eventi.EventoPrenModulo;
import it.algos.evento.entities.prenotazione.eventi.EventoPrenTable;
import it.algos.evento.entities.prenotazione.eventi.EventoPren_;
import it.algos.evento.entities.rappresentazione.Rappresentazione;
import it.algos.evento.entities.rappresentazione.RappresentazioneModulo;
import it.algos.evento.entities.rappresentazione.Rappresentazione_;
import it.algos.evento.entities.scuola.Scuola;
import it.algos.evento.entities.scuola.ScuolaForm;
import it.algos.evento.entities.scuola.Scuola_;
import it.algos.evento.entities.spedizione.Spedizione;
import it.algos.evento.entities.stagione.Stagione;
import it.algos.evento.entities.tiporicevuta.TipoRicevuta;
import it.algos.evento.pref.CompanyPrefs;
import it.algos.webbase.multiazienda.ERelatedComboField;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.field.*;
import it.algos.webbase.web.field.DateField;
import it.algos.webbase.web.field.TextField;
import it.algos.webbase.web.form.AFormLayout;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.lib.Lib;
import it.algos.webbase.web.module.Module;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.toolbar.FormToolbar;
import org.joda.time.DateTime;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

@SuppressWarnings("serial")
public class PrenotazioneForm extends ModuleForm {

    private static final String WF = "5em"; // larghezza dei campi numerici
    private static final String WT = "6em"; // larghezza dei totali
    private IntegerField fieldNumTotale;
    private IntegerField fieldDisponibili;
    private DecimalField fieldImportoTotale;
    private DecimalField fieldImportoTotale2;    // nella seconda pagina
    private CheckBoxField fieldConfermata;
    private CheckBoxField fieldCongelata;
    private TextField fieldClasse;
    private RelatedComboField comboScuola;
    private CheckBoxField fieldPrivato;
//    private CheckBoxField fieldRichiestoBus;
//    private CheckBoxField fieldPagatoBus;


    private Label dettaglioInsegnante;
    private Component compPrezziSingoli;
    private Component compPrezziGruppo;
    private HorizontalLayout placeholderPrezzi;

    private boolean inValueChange = false;   // flag per evitare di reagire ricorsivamente agli eventi di cambio valore di alcuni campi

    // Modulo EventiPrenotazioni interno per la gestione
    // della lista eventi interna alla scheda
    private EventoPrenModulo modEventi;


    // listener e relativi metodi per ascoltare la prenotazione confermata
    private PrenotazioneConfermataListener pcListener;

    public interface PrenotazioneConfermataListener {
        /**
         * @param pren la prenotazione
         * @param sped l'esito della spedizione (se effettuata)
         */
        void prenotazioneConfermata(Prenotazione pren, Spedizione sped);
    }

    public void setPrenotazioneConfermataListener(PrenotazioneConfermataListener l) {
        this.pcListener = l;
    }


    public PrenotazioneForm(ModulePop modulo, Item item) {
        super(item, modulo);
    }


    protected void init() {

        modEventi = new EventiPrenModuloInterno();

        super.init();

        refreshDettaglioInsegnante();

        // listener invocato quando il componente diventa visibile
        addAttachListener(new AttachListener() {
            @Override
            public void attach(AttachEvent attachEvent) {
                if (isNewRecord()) {
                    getWindow().setCaption("Nuova prenotazione");

                    // scrive in anteprima nel campo il prossimo numero di prenotazione
                    // (se il field è read-only lo mette provvisoriamente in rw per scriverlo)
                    // (il numero viene comunque riattribuito nel @PrePersist della entity)
                    Field field = getField(Prenotazione_.numPrenotazione);
                    if (field != null) {
                        boolean ro = field.isReadOnly();
                        field.setReadOnly(false);
                        field.setValue(CompanyPrefs.nextNumPren.getInt());
                        field.setReadOnly(ro);
                    }

                } else {
                    getWindow().setCaption("Modifica prenotazione");
                }
            }
        });

    }


    /**
     * Aggiorna l'area di dettaglio insegnante in base all'insegnante correntemente selezionato
     */
    private void refreshDettaglioInsegnante() {
        RelatedComboField field = (RelatedComboField) getField(Prenotazione_.insegnante);
        Object bean = field.getSelectedBean();
        if (bean != null) {
            Insegnante ins = (Insegnante) bean;
            dettaglioInsegnante.setValue(ins.getDettaglioPren());
        }
    }

    /**
     * Sincronizza i riferimenti in scheda (email, telefono, flag privato) con quelli dell'insegnante
     */
    @SuppressWarnings("unchecked")
    private void syncRiferimentiInsegnante() {
        RelatedComboField field = (RelatedComboField) getField(Prenotazione_.insegnante);
        Object bean = field.getSelectedBean();
        if (bean != null) {
            Insegnante ins = (Insegnante) bean;
            getField(Prenotazione_.telRiferimento).setValue(ins.getTelefono());
            getField(Prenotazione_.emailRiferimento).setValue(ins.getEmail());
            Field fp = getField(Prenotazione_.privato);
            boolean priv = ins.isPrivato();
            fp.setValue(priv);
        }
    }


    protected FormToolbar createToolBar() {
        // create the toolbar
        PrenotazioneFormToolbar toolbar = new PrenotazioneFormToolbar(this);

        // listener per eventi specifici lanciati dalla toolbar
        toolbar.addToolbarListener(new PrenotazioneFormToolbarListener() {

            @Override
            public void confermaPrenotazione() {
                confermaPrenotazioneForm();
            }

            @Override
            public void registraPagamento() {
                registraPagamentoForm();
            }
        });

        return toolbar;
    }



    @Override
    public void createFields() {

        @SuppressWarnings("rawtypes")
        Field field;
        RelatedComboField rcField;

        field = new IntegerField("Numero prenotazione");
        field.setWidth("7em");
        addField(Prenotazione_.numPrenotazione, field);

        rcField = new ERelatedComboField(Rappresentazione.class, "Rappresentazione");
        rcField.sort(Rappresentazione_.dataRappresentazione);
        addField(Prenotazione_.rappresentazione, rcField);

        // il popup delle rappresentazioni mostra solo le rappresentazioni della stagione corrente
        JPAContainer cont = rcField.getJPAContainer();
        String prop = Evento.class.getSimpleName().toLowerCase() + "." + Evento_.stagione.getName();
        cont.addNestedContainerProperty(prop);
        Container.Filter filter = new Compare.Equal(prop, Stagione.getStagioneCorrente());
        // Se record esistente, aggiunge al filtro la rappresentazione della prenotazione.
        // In questo modo, se si modifica una prenotazione che non è della stagione corrente
        // si ha la possibilità di aprire il popup delle rappresentazioni senza perdere la selezione originale
        if (!isNewRecord()) {
            Container.Filter filterSelf = new Compare.Equal(Rappresentazione_.id.getName(), getPrenotazione().getRappresentazione().getId());
            filter=new Or(filter, filterSelf);
        }
        rcField.getFilterableContainer().addContainerFilter(filter);




        // invocato quando si seleziona una rappresentazione nel popup
        rcField.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                copyPrezziDaEvento();
                syncTotImporto();
                syncCompPrezzi();
            }
        });


        field = new DateField("Data prenotazione");
        addField(Prenotazione_.dataPrenotazione, field);

        comboScuola = new ERelatedComboField(Scuola.class, "Scuola");
        comboScuola.sort(Scuola_.sigla);
        comboScuola.setNewItemHandler(ScuolaForm.class, Scuola_.sigla);
        addField(Prenotazione_.scuola, comboScuola);

        RelatedComboField comboInsegnante = new ERelatedComboField(Insegnante.class, "Insegnante");
        //comboInsegnante.sort(Insegnante_.cognome, Insegnante_.nome);
        comboInsegnante.setNewItemHandler(InsegnanteForm.class, Insegnante_.cognome);
        addField(Prenotazione_.insegnante, comboInsegnante);

        // invocato quando si registrano delle modifiche dal popup
        comboInsegnante.addRecordEditedListener(new RelatedComboField.RecordEditedListener() {

            @Override
            public void save_(Item bi, boolean newRecord) {
                syncRiferimentiInsegnante();
                refreshDettaglioInsegnante();
            }
        });

        // invocato quando si seleziona un insegnante nel popup
        comboInsegnante.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                RelatedComboField rcField = (RelatedComboField) getField(Prenotazione_.insegnante);
                Object bean = rcField.getSelectedBean();
                if (bean == null) {
                    dettaglioInsegnante.setValue("");
                } else {
                    syncRiferimentiInsegnante();
                    refreshDettaglioInsegnante();
//					refreshDisplayPrivato();
                }
            }
        });

        fieldClasse = new TextField("Classe");
        addField(Prenotazione_.classe, fieldClasse);

        field = new TextField("Tel. referente");
        field.setWidth("20em");
        addField(Prenotazione_.telRiferimento, field);

        field = new EmailField("E-mail referente");
        addField(Prenotazione_.emailRiferimento, field);

        field = new IntegerField("Interi");
        field.setWidth(WF);
        addField(Prenotazione_.numInteri, field);

        field = new IntegerField("Ridotti");
        field.setWidth(WF);
        addField(Prenotazione_.numRidotti, field);

        field = new IntegerField("Disabili");
        field.setWidth(WF);
        addField(Prenotazione_.numDisabili, field);

        field = new IntegerField("Accomp.");
        field.setWidth(WF);
        addField(Prenotazione_.numAccomp, field);

        field = new DecimalField("Interi");
        field.setWidth(WF);
        addField(Prenotazione_.importoIntero, field);

        field = new DecimalField("Ridotti");
        field.setWidth(WF);
        addField(Prenotazione_.importoRidotto, field);

        field = new DecimalField("Disabili");
        field.setWidth(WF);
        addField(Prenotazione_.importoDisabili, field);

        field = new DecimalField("Accomp.");
        field.setWidth(WF);
        addField(Prenotazione_.importoAccomp, field);

        field = new DecimalField("Importo per gruppo");
        field.setWidth(WF);
        addField(Prenotazione_.importoGruppo, field);


        field = new ERelatedComboField(ModoPagamento.class, "Modo");
        field.setWidth("14em");
        addField(Prenotazione_.modoPagamento, field);

        field = new DateField("Scadenza pagamento");
        addField(Prenotazione_.scadenzaPagamento, field);

        field = new CheckBoxField("Pagamento confermato");
        addField(Prenotazione_.pagamentoConfermato, field);

        field = new DecimalField("Importo");
        addField(Prenotazione_.importoPagato, field);

        field = new DateField("Data pagam. confermato");
        addField(Prenotazione_.dataPagamentoConfermato, field);

        field = new ERelatedComboField(TipoRicevuta.class, "Tipo ricevuta");
        field.setWidth("14em");
        addField(Prenotazione_.tipoRicevuta, field);

        TextArea area = new TextArea();
        area.setRows(10);
        addField(Prenotazione_.note, area);

        field = new DateField("Scadenza conferma");
        addField(Prenotazione_.scadenzaConferma, field);

        field = new IntegerField("Livello sollecito conf.");
        addField(Prenotazione_.livelloSollecitoConferma, field);

        fieldCongelata = new CheckBoxField("Congelata");
        addField(Prenotazione_.congelata, fieldCongelata);

        // un listener che chiede conferma se si vuole accendere il flag manualmente.
        fieldCongelata.addValueChangeListener(event -> {
            if (!inValueChange) {
                boolean newValue = Lib.getBool(event.getProperty().getValue());
                if (newValue == true) {
                    ConfirmDialog dialog = new ConfirmDialog(null, "Questo campo è gestito automaticamente.<br>Sei sicuro di volerlo modificare manualmente?", (dialog1, confirmed) -> {
                        if (confirmed) {
                            // se si accende manualmente Congelata, il flag Confermata viene tolto automaticamente
                            inValueChange = true;
                            fieldConfermata.setValue(false);
                            inValueChange = false;
                        } else {
                            inValueChange = true;
                            fieldCongelata.setValue(!fieldCongelata.getValue());
                            inValueChange = false;
                        }
                    });
                    dialog.setConfirmButtonText("Modifica");
                    dialog.show();
                }
            }
        });


        fieldConfermata = new CheckBoxField("Confermata");
        addField(Prenotazione_.confermata, fieldConfermata);
        fieldConfermata.addValueChangeListener(event -> {
            if (!inValueChange) {
                boolean newValue = Lib.getBool(event.getProperty().getValue());
                if (newValue == true) {
                    ConfirmDialog dialog = new ConfirmDialog(null, "Questo campo è gestito automaticamente.<br>Sei sicuro di volerlo modificare manualmente?", (dialog1, confirmed) -> {
                        if (confirmed) {
                            // se si accende manualmente Confermata, il flag Congelata viene tolto automaticamente
                            inValueChange = true;
                            fieldCongelata.setValue(false);
                            inValueChange = false;
                        } else {
                            inValueChange = true;
                            fieldConfermata.setValue(!fieldConfermata.getValue());
                            inValueChange = false;
                        }
                    });
                    dialog.setConfirmButtonText("Modifica");
                    dialog.show();
                }
            }
        });


        field = new DateField("Data conferma");
        addField(Prenotazione_.dataConferma, field);

        field = new CheckBoxField("Pagamento ricevuto");
        addField(Prenotazione_.pagamentoRicevuto, field);

        field = new DateField("Data pagam. ricevuto");
        addField(Prenotazione_.dataPagamentoRicevuto, field);

        field = new IntegerField("Livello sollecito pagam.");
        addField(Prenotazione_.livelloSollecitoPagamento, field);

        fieldPrivato = new CheckBoxField("Privato");
        addField(Prenotazione_.privato, fieldPrivato);
        fieldPrivato.addValueChangeListener(event -> {
            onPrivatoChange();
        });

        field = new CheckBoxField("Richiesto bus");
        addField(Prenotazione_.richiestoBus, field);

        area = new TextArea("Dettagli");
        area.setRows(4);
        addField(Prenotazione_.dettagliBus, area);

        field = new DecimalField("Importo");
        field.setWidth(WF);
        addField(Prenotazione_.importoBus, field);

        field = new CheckBoxField("Pagato");
        addField(Prenotazione_.pagatoBus, field);


        field = new CheckBoxField("Richiesto laboratorio");
        addField(Prenotazione_.richiestoLab, field);

        area = new TextArea("Dettagli");
        area.setRows(4);
        addField(Prenotazione_.dettagliLab, area);

        field = new DecimalField("Importo");
        field.setWidth(WF);
        addField(Prenotazione_.importoLab, field);

        field = new CheckBoxField("Pagato");
        addField(Prenotazione_.pagatoLab, field);



    }


    /**
     * Invocato quando il valore del flag privato cambia.
     * Sincronizza i campi dipendenti.
     */
    private void onPrivatoChange() {
        boolean privato = fieldPrivato.getValue();
        fieldClasse.setVisible(!privato);
        fieldClasse.removeAllValidators();
        comboScuola.removeAllValidators();
        Component comp = comboScuola.getEditComponent();
        comp.setVisible(!privato);
        if (!privato) {
            fieldClasse.addValidator(new StringLengthValidator("La classe è obbligatoria", 1, null, true));
            comboScuola.addValidator(new NullValidator("La scuola è obbligatoria", false));
        }
    }

    protected Component createComponent() {
        String width = "64em";
        TabSheet.Tab tab;

        TabSheet tabsheet = new TabSheet();
        tabsheet.setWidthUndefined();
        tab = tabsheet.addTab(creaTabGenerale(), "Generale");
        tab.getComponent().setWidth(width);
        tab = tabsheet.addTab(creaTabPagamento(), "Pagamento");
        tab.getComponent().setWidth(width);
        tab = tabsheet.addTab(creaTabEventi(), "Eventi e note");
        tab.getComponent().setWidth(width);

        // tab bus (personalizzazione Extrateatro)
        if (Company.getCurrent().getCompanyCode().equals(EventoApp.EXTRATEATRO_COMPANY_CODE)) {
            tab = tabsheet.addTab(creaTabAltro(), "Bus");
            tab.getComponent().setWidth(width);
        }

        // tab laboratorio (personalizzazione Extrateatro)
        if (Company.getCurrent().getCompanyCode().equals(EventoApp.EXTRATEATRO_COMPANY_CODE)) {
            tab = tabsheet.addTab(creaTabLab(), "Laboratorio");
            tab.getComponent().setWidth(width);
        }


        postLayout();
        return tabsheet;

    }// end of method


    private Component creaTabGenerale() {

        Component comp;

        // campi esistenti solo nella Presentation
        fieldNumTotale = new IntegerField("Totale");
        fieldNumTotale.setWidth(WT);
        fieldNumTotale.setReadOnly(true);

        fieldDisponibili = new IntegerField("Rimasti");
        fieldDisponibili.setWidth(WF);
        fieldDisponibili.setReadOnly(true);

        fieldImportoTotale = new DecimalField("Importo");
        fieldImportoTotale.setWidth(WT);
        fieldImportoTotale.setReadOnly(true);

        dettaglioInsegnante = new Label("", ContentMode.HTML);
        dettaglioInsegnante.addStyleName("text-85");

        AFormLayout layout = new AFormLayout();
        layout.setMargin(true);

        // start riga numero e data
        HorizontalLayout hl = new HorizontalLayout();
        hl.setSpacing(true);
        hl.setMargin(false);
        comp = getField(Prenotazione_.numPrenotazione);

        // sposta la caption sul layout esterno
        String str1 = comp.getCaption();
        comp.setCaption(null);
        hl.setCaption(str1);

        hl.addComponent(comp);
        FormLayout fl = new FormLayout();
        fl.setMargin(false);
        fl.addComponent(getField(Prenotazione_.dataPrenotazione));
        hl.addComponent(fl);
        layout.addComponent(hl);
        // end riga numero e data


        layout.addComponent(getField(Prenotazione_.rappresentazione));

        // start pannello insegnante
        VerticalLayout panIns = new VerticalLayout();

        HorizontalLayout rigaIns = new HorizontalLayout();
        rigaIns.setSpacing(true);
        RelatedComboField comboInsegnante = (RelatedComboField) getField(Prenotazione_.insegnante);
        Component comboEdit = comboInsegnante.getEditComponent();

        // sposta la caption del campo sul pannello esterno
        // così il FormLayout allinea correttamente
        panIns.setCaption(comboEdit.getCaption());
        comboEdit.setCaption(null);

        rigaIns.addComponent(comboEdit);
        rigaIns.setComponentAlignment(comboEdit, Alignment.BOTTOM_LEFT);
        rigaIns.addComponent(fieldPrivato);
        rigaIns.setComponentAlignment(fieldPrivato, Alignment.MIDDLE_LEFT);
        panIns.addComponent(rigaIns);
        panIns.addComponent(dettaglioInsegnante);
        layout.addComponent(panIns);
        // end pannello insegnante

        //RelatedComboField comboScuola = (RelatedComboField)getField(Prenotazione_.scuola);
        layout.addComponent(comboScuola.getEditComponent());

        layout.addComponent(fieldClasse);
        layout.addComponent(getField(Prenotazione_.telRiferimento));
        layout.addComponent(getField(Prenotazione_.emailRiferimento));

        // componente numero persone
        layout.addComponent(creaCompPersone());

        // componente prezzi
        creaCompPrezzi();
        placeholderPrezzi=new HorizontalLayout();
        layout.addComponent(placeholderPrezzi);
        syncCompPrezzi();


        // pannello conferma prenotazione
        HorizontalLayout confermaPanel = new HorizontalLayout();
        confermaPanel.setSpacing(true);
        confermaPanel.addComponent(getField(Prenotazione_.scadenzaConferma));
        confermaPanel.addComponent(getField(Prenotazione_.dataConferma));
        confermaPanel.addComponent(getField(Prenotazione_.livelloSollecitoConferma));

        Component fieldCongelata = getField(Prenotazione_.congelata);
        confermaPanel.addComponent(fieldCongelata);
        confermaPanel.setComponentAlignment(fieldCongelata, Alignment.BOTTOM_LEFT);

        Component fieldConfermata = getField(Prenotazione_.confermata);
        confermaPanel.addComponent(fieldConfermata);
        confermaPanel.setComponentAlignment(fieldConfermata, Alignment.BOTTOM_LEFT);

        layout.addComponent(confermaPanel);

        return layout;
    }


    private Component creaCompPersone() {

        // recupera i Fields
        Field fldNumInt = getField(Prenotazione_.numInteri);
        Field fldNumRid = getField(Prenotazione_.numRidotti);
        Field fldNumDis = getField(Prenotazione_.numDisabili);
        Field fldNumAcc = getField(Prenotazione_.numAccomp);
        Field fldNumTot = fieldNumTotale;
        Field fldNumAvail = fieldDisponibili;

        GridLayout grid = new GridLayout(6, 1);
        grid.setSpacing(true);
        grid.setCaption("n. posti");
        grid.addComponent(fldNumInt);
        grid.addComponent(fldNumRid);
        grid.addComponent(fldNumDis);
        grid.addComponent(fldNumAcc);
        grid.addComponent(fldNumTot);
        grid.addComponent(fldNumAvail);

        return grid;

    }


    /**
     * Crea i componenti per l'editing dei prezzi
     * per entrambi i casi: singoli o gruppo
     */
    private void creaCompPrezzi() {

        // recupera i Fields
        Field fldImpInt = getField(Prenotazione_.importoIntero);
        fldImpInt.setCaption(null);

        Field fldImpRid = getField(Prenotazione_.importoRidotto);
        fldImpRid.setCaption(null);

        Field fldImpDis = getField(Prenotazione_.importoDisabili);
        fldImpDis.setCaption(null);

        Field fldImpAcc = getField(Prenotazione_.importoAccomp);
        fldImpAcc.setCaption(null);

        Field fldImpGru = getField(Prenotazione_.importoGruppo);
        fldImpGru.setCaption(null);

        Field fldImpTot = fieldImportoTotale;
        fldImpTot.setCaption(null);

        GridLayout grid;

        // componente usato nel caso di prezzi singoli
        grid = new GridLayout(5, 1);
        grid.setSpacing(true);
        grid.addComponent(fldImpInt);
        grid.addComponent(fldImpRid);
        grid.addComponent(fldImpDis);
        grid.addComponent(fldImpAcc);
        grid.addComponent(fldImpTot);
        compPrezziSingoli=grid;

        // componente usato nel caso di prezzi per gruppo
        grid = new GridLayout(1, 1);
        grid.setSpacing(true);
        grid.addComponent(fldImpGru);
        compPrezziGruppo=grid;

    }


    private Component creaTabPagamento() {
        AFormLayout layout = new AFormLayout();
        layout.setMargin(true);

        fieldImportoTotale2 = new DecimalField("Importo");
        fieldImportoTotale2.setWidth("6em");
        fieldImportoTotale2.setReadOnly(true);


        // pannello pagamento da effettuare
        HorizontalLayout pagarePanel = new HorizontalLayout();
        pagarePanel.setSpacing(true);
        pagarePanel.setCaption("A pagare");
        pagarePanel.addComponent(fieldImportoTotale2);
        pagarePanel.addComponent(getField(Prenotazione_.modoPagamento));
        pagarePanel.addComponent(getField(Prenotazione_.scadenzaPagamento));
        layout.addComponent(pagarePanel);

        layout.addComponent(new Label("&nbsp;", com.vaadin.shared.ui.label.ContentMode.HTML));

        // pannello pagamento effettuato
        HorizontalLayout pagatoPanel = new HorizontalLayout();
        // FormLayout pagatoPanel = new FormLayout();
        pagatoPanel.setSpacing(true);
        pagatoPanel.setCaption("Conferma pagamento");
        pagatoPanel.addComponent(getField(Prenotazione_.importoPagato));
        pagatoPanel.addComponent(getField(Prenotazione_.dataPagamentoConfermato));
        Component fieldPagato = getField(Prenotazione_.pagamentoConfermato);
        pagatoPanel.addComponent(fieldPagato);
        pagatoPanel.setComponentAlignment(fieldPagato, Alignment.BOTTOM_LEFT);
        layout.addComponent(pagatoPanel);

        layout.addComponent(new Label("&nbsp;", com.vaadin.shared.ui.label.ContentMode.HTML));

        // pannello pagamento ricevuto
        HorizontalLayout contabPanel = new HorizontalLayout();
        contabPanel.setSpacing(true);
        contabPanel.addComponent(getField(Prenotazione_.dataPagamentoRicevuto));
        contabPanel.addComponent(getField(Prenotazione_.livelloSollecitoPagamento));
        Component fieldContabilizzato = getField(Prenotazione_.pagamentoRicevuto);
        contabPanel.addComponent(fieldContabilizzato);
        contabPanel.setComponentAlignment(fieldContabilizzato, Alignment.BOTTOM_LEFT);
        layout.addComponent(contabPanel);

        layout.addComponent(new Label("&nbsp;", com.vaadin.shared.ui.label.ContentMode.HTML));

        // tipo di tiporicevuta
        layout.addComponent(getField(Prenotazione_.tipoRicevuta));

        return layout;

    }

    private Component creaTabEventi() {
        Component comp;

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);


        ATable tableEventi = modEventi.getTable();
        layout.addComponent(new Label("Eventi della prenotazione"));
        tableEventi.setWidth("100%");
        tableEventi.setPageLength(7);
        layout.addComponent(tableEventi);
        layout.setExpandRatio(tableEventi, 1);


//        eventsTable = new EventiInPrenTable(getModule().getEntityManager());
//        eventsTable.setPageLength(7);
//        SingularAttribute attr = EventoPren_.prenotazione;
//        String name = attr.getName();
//        Filter filter = new Compare.Equal(name, getPrenotazione());
//        Container.Filterable fCont = eventsTable.getFilterableContainer();
//        fCont.removeAllContainerFilters();
//        fCont.addContainerFilter(filter);
//
//        Container.Sortable sCont = eventsTable.getSortableContainer();
//        sCont.sort(new String[]{EventoPren_.timestamp.getName()}, new boolean[]{true});
//
//        eventsTable.setWidth("100%");
//        layout.addComponent(eventsTable);

        comp = getField(Prenotazione_.note);
        comp.setWidth("100%");
        layout.addComponent(comp);

        return layout;
    }

    private Component creaTabAltro() {
        Component comp;

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        comp = getField(Prenotazione_.richiestoBus);
        layout.addComponent(comp);

        comp = getField(Prenotazione_.dettagliBus);
        comp.setWidth("100%");
        layout.addComponent(comp);

        HorizontalLayout hl = new HorizontalLayout();
        hl.setSpacing(true);
        hl.setDefaultComponentAlignment(Alignment.BOTTOM_LEFT);
        comp = getField(Prenotazione_.importoBus);
        hl.addComponent(comp);
        comp = getField(Prenotazione_.pagatoBus);
        hl.addComponent(comp);
        layout.addComponent(hl);


        Panel panel = new Panel(layout);
        panel.setCaption("Gestione Bus");

        return panel;
    }


    private Component creaTabLab() {
        Component comp;

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        comp = getField(Prenotazione_.richiestoLab);
        layout.addComponent(comp);

        comp = getField(Prenotazione_.dettagliLab);
        comp.setWidth("100%");
        layout.addComponent(comp);

        HorizontalLayout hl = new HorizontalLayout();
        hl.setSpacing(true);
        hl.setDefaultComponentAlignment(Alignment.BOTTOM_LEFT);
        comp = getField(Prenotazione_.importoLab);
        hl.addComponent(comp);
        comp = getField(Prenotazione_.pagatoLab);
        hl.addComponent(comp);
        layout.addComponent(hl);


        Panel panel = new Panel(layout);
        panel.setCaption("Gestione Laboratorio");

        return panel;
    }






    /**
     * Eseguito dopo il disegno dei layout di tutte le pagine
     */
    @SuppressWarnings("rawtypes")
    private void postLayout() {
        // set read-only fields
        getField(Prenotazione_.numPrenotazione).setReadOnly(true);

        // aggiungi i listeners per la sincronizzazione dei totali
        Field field;
        field = getField(Prenotazione_.rappresentazione);
        field.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                syncTotali();
                syncDataScadenzaPagamento();
            }
        });

        field = getField(Prenotazione_.numInteri);
        field.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                syncTotali();
            }
        });
        field = getField(Prenotazione_.numRidotti);
        field.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                syncTotali();
            }
        });
        field = getField(Prenotazione_.numDisabili);
        field.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                syncTotali();
            }
        });
        field = getField(Prenotazione_.numAccomp);
        field.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                syncTotali();
            }
        });

        field = getField(Prenotazione_.importoIntero);
        field.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                syncTotImporto();
            }
        });
        field = getField(Prenotazione_.importoRidotto);
        field.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                syncTotImporto();
            }
        });
        field = getField(Prenotazione_.importoDisabili);
        field.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                syncTotImporto();
            }
        });
        field = getField(Prenotazione_.importoAccomp);
        field.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                syncTotImporto();
            }
        });
        field = getField(Prenotazione_.importoGruppo);
        field.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                syncTotImporto();
            }
        });



        field = getField(Prenotazione_.congelata);
        field.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                syncTotali();
            }
        });
        field = getField(Prenotazione_.dataPrenotazione);
        field.addValueChangeListener(new ValueChangeListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void valueChange(ValueChangeEvent event) {
                Object obj = getField(Prenotazione_.dataPrenotazione).getValue();
                if ((obj != null) && (obj instanceof Date)) {
                    Date dataPren = (Date) obj;
                    Date dataScadConf = Prenotazione.getDataScadenzaConferma(dataPren);
                    getField(Prenotazione_.scadenzaConferma).setValue(dataScadConf);
                }
            }
        });

        syncTotali();

        onPrivatoChange();

    }

    private void syncTotali() {
        syncTotSpettatori();
        syncTotImporto();
        syncDisponibili();
    }

    /**
     * Calcola la data scadenza pagamento in base alla data della rappresentazione
     */
    @SuppressWarnings("unchecked")
    private void syncDataScadenzaPagamento() {
        RelatedComboField fRappresentazione = (RelatedComboField) getField(Prenotazione_.rappresentazione);
        Rappresentazione rapp = (Rappresentazione) fRappresentazione.getSelectedBean();
        Date dataScad = null;
        if (rapp != null) {
            Date dataRapp = rapp.getDataRappresentazione();
            DateTime dt = new DateTime(dataRapp);
            dt = dt.minusDays(CompanyPrefs.ggScadConfermaPagamento.getInt());
            dataScad = dt.toDate();
        }

        getField(Prenotazione_.scadenzaPagamento).setValue(dataScad);

    }

    private void syncTotSpettatori() {

        int interi = getIntValue(Prenotazione_.numInteri);
        int ridotti = getIntValue(Prenotazione_.numRidotti);
        int disabili = getIntValue(Prenotazione_.numDisabili);
        int accomp = getIntValue(Prenotazione_.numAccomp);
        Integer tot = interi + ridotti + disabili + accomp;
        boolean roState = fieldNumTotale.isReadOnly();
        fieldNumTotale.setReadOnly(false);
        fieldNumTotale.setValue(tot);
        fieldNumTotale.setReadOnly(roState);
    }

    /**
     * Sincronizza l'importo totale come somma dei vari importi (q.tà x prezzo)
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void syncTotImporto() {

        // recupera l'importo totale
        int nInteri = getIntValue(Prenotazione_.numInteri);
        int nRidotti = getIntValue(Prenotazione_.numRidotti);
        int nDisabili = getIntValue(Prenotazione_.numDisabili);
        int nAccomp = getIntValue(Prenotazione_.numAccomp);
        BigDecimal iInteri = getBigDecimalValue(Prenotazione_.importoIntero);
        BigDecimal iRidotti = getBigDecimalValue(Prenotazione_.importoRidotto);
        BigDecimal iDisabili = getBigDecimalValue(Prenotazione_.importoDisabili);
        BigDecimal iAccomp = getBigDecimalValue(Prenotazione_.importoAccomp);
        BigDecimal iFisso = getBigDecimalValue(Prenotazione_.importoGruppo);

        BigDecimal totPren = Prenotazione.getTotImporto(nInteri, nRidotti, nDisabili, nAccomp, iInteri, iRidotti, iDisabili, iAccomp, iFisso);

        // scrive il valore nei campi totale
        Field field;
        boolean roState;

        field = fieldImportoTotale;
        roState = field.isReadOnly();
        field.setReadOnly(false);
        field.setValue(totPren);
        field.setReadOnly(roState);

        field = fieldImportoTotale2;
        roState = field.isReadOnly();
        field.setReadOnly(false);
        field.setValue(totPren);
        field.setReadOnly(roState);


    }


    /**
     * Inserisce nel placeholder il componente adeguato in base al tipo
     * di rappresentazione (prezzi singoli o per gruppo)
     */
    private void syncCompPrezzi(){
        placeholderPrezzi.removeAllComponents();
        placeholderPrezzi.setCaption("");

        RelatedComboField field = (RelatedComboField)getField(Prenotazione_.rappresentazione);
        Object bean = field.getSelectedBean();
        if(bean!=null){
            Rappresentazione rapp=(Rappresentazione)bean;
            if(rapp.getEvento().isPrezzoPerGruppi()){
                placeholderPrezzi.addComponent(compPrezziGruppo);
                placeholderPrezzi.setCaption("Prezzo per gruppo");
            }else {
                placeholderPrezzi.addComponent(compPrezziSingoli);
                placeholderPrezzi.setCaption("Prezzo per persona");
            }
        }

    }


    /**
     * Copia i prezzi dall'evento correntemente selezionato ai campi della scheda
     */
    private void copyPrezziDaEvento() {
        RelatedComboField field = (RelatedComboField) getField(Prenotazione_.rappresentazione);
        Object bean = field.getSelectedBean();
        if (bean != null) {
            Rappresentazione rapp = (Rappresentazione) bean;
            Evento evento = rapp.getEvento();

            // azzera tutto
            BigDecimal zero = new BigDecimal(0);
            getField(Prenotazione_.importoIntero).setValue(zero);
            getField(Prenotazione_.importoRidotto).setValue(zero);
            getField(Prenotazione_.importoDisabili).setValue(zero);
            getField(Prenotazione_.importoAccomp).setValue(zero);
            getField(Prenotazione_.importoGruppo).setValue(zero);

            // valorizza dall'evento in base al tipo di prezzo
            if(evento.isPrezzoPerGruppi()){
                getField(Prenotazione_.importoGruppo).setValue(evento.getImportoGruppo());
            }else{
                getField(Prenotazione_.importoIntero).setValue(evento.getImportoIntero());
                getField(Prenotazione_.importoRidotto).setValue(evento.getImportoRidotto());
                getField(Prenotazione_.importoDisabili).setValue(evento.getImportoDisabili());
                getField(Prenotazione_.importoAccomp).setValue(evento.getImportoAccomp());
            }

        }
    }

    /**
     * Sincronizza l'indicatore dei posti diponibili
     */
    private void syncDisponibili() {
        int disponibili = getPostiDisponibili();
        boolean roState = this.fieldDisponibili.isReadOnly();
        this.fieldDisponibili.setReadOnly(false);
        this.fieldDisponibili.setValue(disponibili);
        if(disponibili<0) {
            this.fieldDisponibili.addStyleName("red");
        }else{
            this.fieldDisponibili.removeStyleName("red");
        }
        this.fieldDisponibili.setReadOnly(roState);

    }

    /**
     * Ritorna il numero di posti ancora disponibili per questa rappresentazione.
     * Per la prenotazione corrente, non considera i posti registrati sul db ma
     * considera quelli correntemente presenti nella scheda
     */
    private int getPostiDisponibili() {
        int disponibili = 0;
        Rappresentazione questaRapp = null;

        long idRapp = getLongValue(Prenotazione_.rappresentazione);
        questaRapp = Rappresentazione.read(idRapp);

        if (questaRapp != null) {

            // tutte le prenotazioni non congelate (compresa questa)
            EntityManager em = getModule().getEntityManager();
            disponibili = RappresentazioneModulo.countPostiDisponibili(questaRapp, em);

            // tolgo questa come risulta dal db
            Prenotazione prenDb = Prenotazione.read(getItemId());
            if (prenDb != null) {
                if (!prenDb.isCongelata()) {
                    disponibili += prenDb.getNumTotali();
                }
            }

            // aggiungo questa come risulta dalla scheda
            if (!fieldCongelata.getValue()) {
                disponibili -= fieldNumTotale.getValue();
            }

        }

        return disponibili;

    }


    protected boolean save() {
        boolean saved;

        saved = super.save();

        if (saved) {

            // avviso posti esauriti
            int disponibili = getPostiDisponibili();
            if (disponibili < 0) {
                Notification.show("Attenzione", "\nPosti esauriti! (" + disponibili + ")", Notification.Type.WARNING_MESSAGE);
            }

            // se si tratta di nuova prenotazione, eventualmente invia email di istruzioni
            if (isNewRecord()) {

                if (ModelliLettere.istruzioniPrenotazione.isSend(getPrenotazione())) {
                    Prenotazione pren = getPrenotazione();

                    // invia la mail di istruzioni in un thread separato
                    // (usa una lambda al posto del runnable)
                    new Thread(
                            () -> {

                                Notification notification1 = null;
                                String detail = pren.toStringNumDataInsegnante();

                                try {
                                    PrenotazioneModulo.doInvioIstruzioni(pren, getUsername());
                                    notification1 = new Notification("Inviata email di istruzioni", detail, Notification.Type.HUMANIZED_MESSAGE);
                                } catch (EmailFailedException e) {
                                    notification1 = new Notification("Invio email istruzioni fallito: " + e.getMessage(), detail, Notification.Type.ERROR_MESSAGE);
                                } catch (Exception e) {
                                    notification1 = new Notification("Errore durante l'invio della email di istruzioni: " + e.getMessage(), detail, Notification.Type.ERROR_MESSAGE);
                                    e.printStackTrace();
                                }
                                notification1.setDelayMsec(-1);
                                notification1.show(Page.getCurrent());

                            }
                    ).start();

                }
            }


        }

        return saved;

    }

    /**
     * Checks if the current values are valid and ready to be persisted.
     * <p>
     *
     * @return a list of strings containing the reasons if not valid, empty list if valid.
     */
    protected ArrayList<String> isValid() {
        ArrayList<String> reasons = super.isValid();
        int numTot = Lib.getInt(fieldNumTotale.getValue());
        if (numTot <= 0) {
            reasons.add("Il numero totale di spettatori è zero.");
        }
        return reasons;
    }


    private Prenotazione getPrenotazione() {
        Prenotazione pren = null;
        BaseEntity entity = getEntity();
        if (entity != null) {
            pren = (Prenotazione) entity;
        }
        return pren;
    }



    /**
     * Tenta di confermare la prenotazione
     */
    @SuppressWarnings("rawtypes")
    private void confermaPrenotazioneForm() {
        boolean cont = true;
        Prenotazione pren = getPrenotazione();

        // controllo logico che la prenotazione sia confermabile
        if (cont) {
            String err = pren.isConfermabile();
            if(!err.equals("")){
                cont = false;
                Notification.show(err);
            }
        }

        // controllo che la scheda sia valida e registrabile
        if (cont) {
            try {
                getBinder().commit();
            } catch (CommitException e) {
                Notification.show("Questa prenotazione non è valida.");
                cont = false;
            }
        }


        // delega il resto al dialogo
        if (cont) {
            DialogoConfermaPrenotazione dialogo = new DialogoConfermaPrenotazione(pren, getEntityManager(), new Date());


            // dopo la conferma e l'invio email (che avvengono in un thread separato),
            // aggiorno la scheda e mostro una notifica
            dialogo.setPrenotazioneConfermataListener(new DialogoConfermaPrenotazione.PrenotazioneConfermataListener() {
                @Override
                public void prenotazioneConfermata() {

                    // aggiorno la scheda
                    inValueChange=true;
                    reload();
                    inValueChange=false;

                    // mostro una notifica
                    dialogo.notificaEsito();

                }
            });
            dialogo.show();
        }


    }


    /**
     * Tenta di registrare il pagamento
     */
    @SuppressWarnings("rawtypes")
    private void registraPagamentoForm() {
        boolean cont=true;
        Prenotazione pren = getPrenotazione();


        // controllo logico che il pagamento sia registrabile
        if (cont) {
            String err = pren.isPagamentoRegistrabile();
            if(!err.equals("")){
                cont = false;
                Notification.show(err);
            }
        }

        // controllo che la scheda sia valida e registrabile
        if (cont) {
            try {
                getBinder().commit();
            } catch (CommitException e) {
                Notification.show("Questa prenotazione non è valida.");
                cont = false;
            }
        }


        // delega il resto al dialogo
        if (cont) {
            DialogoRegistraPagamento dialogo = new DialogoRegistraPagamento(pren, getEntityManager());

            // Durante la procedura di registrazione pagamento, tutta la prenotazione viene registrata.
            // Dopo la registrazione del pagamento e l'invio email (che avvengono in un thread separato),
            // aggiorno i dati visualizzati nella scheda e mostro una notifica.
            dialogo.setPagamentoRegistratoListener(new DialogoRegistraPagamento.PagamentoRegistratoListener() {
                @Override
                public void pagamentoRegistrato(boolean confermato, boolean ricevuto, boolean mailInviata, boolean emailFailed) {

                    // aggiorno la scheda
                    inValueChange=true;
                    reload();
                    inValueChange=false;

                    // mostro una notifica
                    dialogo.notificaEsito();

                }
            });
            dialogo.show();
        }

    }


    private static String getUsername() {
        return EventoBootStrap.getUsername();
    }

    private PrenotazioneModulo getPrenotazioneModulo() {
        PrenotazioneModulo pm = null;
        Module mod = getModule();
        if (mod != null && mod instanceof PrenotazioneModulo) {
            pm = (PrenotazioneModulo) mod;
        }
        return pm;
    }


    /**
     * Modulo EventoPrenModulo dedicato alla gestione Eventi Prenotazioni
     * all'interno della scheda Prenotazione.
     * Usato per la gestione della lista e della scheda interne.
     */
    private class EventiPrenModuloInterno extends EventoPrenModulo {

        /**
         * Usa una tabella specifica
         */
        @Override
        public ATable createTable() {
            return new TableEventiInterna(this);
        }

        /**
         * Questo modulo non è inserito graficamente in nessuna UI
         * perciò ritorna la UI della scheda che lo contiene.
         * La UI è richiesta quando deve mostrare la scheda.
         */
        @Override
        public UI getUI() {
            return PrenotazioneForm.this.getUI();
        }

        /**
         * Questo modulo usa lo stesso EntityManager della
         * scheda che lo contiene
         */
        @Override
        public EntityManager getEntityManager() {
            return PrenotazioneForm.this.getEntityManager();
        }
    }

    /**
     * Tabella Prenotazioni del modulo Prenotazioni interno
     */
    private class TableEventiInterna extends EventoPrenTable {

        public TableEventiInterna(EventoPrenModulo modulo) {
            super(modulo);
        }

        /**
         * Filtra il container sulla prenotazione corrente
         */
        @Override
        public Container createContainer() {
            Filterable cont = (Filterable) super.createContainer();
            Prenotazione pren = getPrenotazione();
            cont.addContainerFilter(new Compare.Equal(EventoPren_.prenotazione.getName(), pren));
            return cont;
        }


        /**
         * Mostra solo alcune colonne
         */
        protected Object[] getDisplayColumns() {
            return new Object[]{EventoPren_.timestamp,
                    EventoPren_.tipo,
                    EventoPren_.dettagli,
                    EventoPren_.user,
                    colEmail,
                    colEsito};
        }// end of method


    }
}
