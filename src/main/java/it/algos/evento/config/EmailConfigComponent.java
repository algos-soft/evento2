package it.algos.evento.config;

import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import it.algos.evento.pref.CompanyPrefs;
import it.algos.webbase.web.field.CheckBoxField;
import it.algos.webbase.web.field.EmailField;
import it.algos.webbase.web.field.TextField;

@SuppressWarnings("serial")
public class EmailConfigComponent extends BaseConfigPanel {

	private static final String KEY_SENDER = "sender";
	private static final String KEY_BACKUP_EMAIL = "emailbackup";
	private static final String KEY_BACKUP_ADDRESS = "emailbackupaddress";

	private static final String KEY_CHECK_MAIL_INFO_PREN = "checkMailInfoPren";
	private static final String KEY_CHECK_MAIL_CONF_PREN = "checkMailConfPren";
	private static final String KEY_CHECK_MAIL_CONF_PAGA = "checkMailConfPaga";
	private static final String KEY_CHECK_MAIL_REGIS_PAGA = "checkMailRegisPaga";
	private static final String KEY_CHECK_MAIL_CONG_OPZ = "checkMailCongOpzione";


	private static final String KEY_REF_MAIL_INFO_PREN = "checkRefInfoPren";
	private static final String KEY_REF_MAIL_SCAD_PREN = "checkRefScadPren";
	private static final String KEY_REF_MAIL_CONF_PREN = "checkRefConfPren";
	private static final String KEY_REF_MAIL_SCAD_PAGA = "checkRefScadPaga";
	private static final String KEY_REF_MAIL_CONF_PAGA = "checkRefConfPaga";
	private static final String KEY_REF_MAIL_REGIS_PAGA = "checkRefRegisPaga";
	private static final String KEY_REF_MAIL_CONG_OPZ = "checkRefCongOpzione";
	private static final String KEY_REF_MAIL_ATT_PART = "checkRefAttPart";


	private static final String KEY_SCUOLA_MAIL_INFO_PREN = "checkScuolaInfoPren";
	private static final String KEY_SCUOLA_MAIL_SCAD_PREN = "checkScuolaSCADPren";
	private static final String KEY_SCUOLA_MAIL_CONF_PREN = "checkScuolaConfPren";
	private static final String KEY_SCUOLA_MAIL_SCAD_PAGA = "checkScuolaScadPaga";
	private static final String KEY_SCUOLA_MAIL_CONF_PAGA = "checkScuolaConfPaga";
	private static final String KEY_SCUOLA_MAIL_REGIS_PAGA = "checkScuolaRegisPaga";
	private static final String KEY_SCUOLA_MAIL_CONG_OPZ = "checkScuolaCongOpzione";
	private static final String KEY_SCUOLA_MAIL_ATT_PART = "checkScuolaAttPart";


	private static final String KEY_NP_MAIL_INFO_PREN = "checkNPInfoPren";
	private static final String KEY_NP_MAIL_SCAD_PREN = "checkNPScadPren";
	private static final String KEY_NP_MAIL_CONF_PREN = "checkNPConfPren";
	private static final String KEY_NP_MAIL_SCAD_PAGA = "checkNPScadPaga";
	private static final String KEY_NP_MAIL_CONF_PAGA = "checkNPConfPaga";
	private static final String KEY_NP_MAIL_REGIS_PAGA = "checkNPRegisPaga";
	private static final String KEY_NP_MAIL_CONG_OPZ = "checkNPCongOpzione";
	private static final String KEY_NP_MAIL_ATT_PART = "checkNPAttPart";

	private TextField senderField;
	private CheckBoxField sendBackupMailField;
	private TextField backupEmailAddressField;

	private CheckBoxField checkMailInfoPrenField;
	private CheckBoxField checkMailConfPrenField;
	private CheckBoxField checkMailConfPagaField;
	private CheckBoxField checkMailRegisPagaField;
	private CheckBoxField checkMailCongOpzioneField;

	private CheckBoxField checkRefInfoPrenField;
	private CheckBoxField checkRefScadPrenField;
	private CheckBoxField checkRefConfPrenField;
	private CheckBoxField checkRefScadPagaField;
	private CheckBoxField checkRefConfPagaField;
	private CheckBoxField checkRefRegisPagaField;
	private CheckBoxField checkRefCongOpzioneField;
	private CheckBoxField checkRefAttPartField;


	private CheckBoxField checkScuolaInfoPrenField;
	private CheckBoxField checkScuolaScadPrenField;
	private CheckBoxField checkScuolaConfPrenField;
	private CheckBoxField checkScuolaScadPagaField;
	private CheckBoxField checkScuolaConfPagaField;
	private CheckBoxField checkScuolaRegisPagaField;
	private CheckBoxField checkScuolaCongOpzioneField;
	private CheckBoxField checkScuolaAttPartField;

	private CheckBoxField checkNPInfoPrenField;
	private CheckBoxField checkNPScadPrenField;
	private CheckBoxField checkNPConfPrenField;
	private CheckBoxField checkNPScadPagaField;
	private CheckBoxField checkNPConfPagaField;
	private CheckBoxField checkNPRegisPagaField;
	private CheckBoxField checkNPCongOpzioneField;
	private CheckBoxField checkNPAttPartField;


	public EmailConfigComponent() {
		super();
		//addStyleName("yellowBg");

		
		// crea i fields
		createFields();
		
		// crea la UI
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.addComponent(creaComponenteAzioni());
		layout.addComponent(creaComponenteDefaults());
		layout.addComponent(creaComponenteOpzioni());

		addComponent(layout);
		addComponent(createButtonPanel());

		// sincronizza i checks
		//syncMailChecks();


	}
	
	// crea e registra i fields
	private void createFields(){
		// create and add fields and other components
		senderField = new EmailField("Indirizzo mittente");
		senderField.setDescription("L'indirizzo che risulta come mittente di tutte le email in uscita. I destinatari potrebbero rispondere a questo indirizzo.");

		// backup email
		sendBackupMailField = new CheckBoxField("Invia una copia di tutte le email");
		sendBackupMailField.setDescription("Invia una copia di tutte le email in uscita a un proprio indirizzo");
		backupEmailAddressField = new EmailField("Indirizzo email per copie");
		backupEmailAddressField.setDescription("L'indirizzo al quale inviare le copie delle email in uscita");

		// checks invio email
		checkMailInfoPrenField = new CheckBoxField("All'inserimento di una nuova opzione");
		checkMailInfoPrenField.setDescription("Quando si registra una nuova opzione, invia una email che notifica che abbiamo acquisito l'opzione, ne riepiloga i dati al referente e lo invita a verificare/rettificare i dati ed a inviare a sua volta una conferma entro un certo tempo");

		checkMailConfPrenField= new CheckBoxField("Alla conferma della prenotazione");
		checkMailConfPrenField.setDescription("Quando si conferma una prenotazione, invia una email che notifica che la prenotazione è stata confermata, comunica i termini di pagamento e invita il referente a inviare evidenza del pagamento entro un certo tempo");

		checkMailConfPagaField= new CheckBoxField("Alla conferma del pagamento");
		checkMailConfPagaField.setDescription("Quando si conferma il pagamento, invia una email che notifica al referente l'avvenuta acquisizione della evidenza di pagamento");

		checkMailRegisPagaField= new CheckBoxField("Alla registrazione del pagamento");
		checkMailRegisPagaField.setDescription("Quando si registra il pagamento, invia una email che notifica che il pagamento è stato effettivamente ricevuto e contabilizzato");

		checkMailCongOpzioneField= new CheckBoxField("Al congelamento di una prenotazione");
		checkMailCongOpzioneField.setDescription("Quando si congela una prenotazione, invia una email che notifica che abbiamo congelato l'opzione e che i posti potrebbero non essere più disponibili");

		String tooltip;


		// checks invia alla scuola
		tooltip="Invia la mail alla scuola";
		checkScuolaInfoPrenField= new CheckBoxField();
		checkScuolaInfoPrenField.setDescription(tooltip);
		checkScuolaScadPrenField= new CheckBoxField();
		checkScuolaScadPrenField.setDescription(tooltip);
		checkScuolaConfPrenField= new CheckBoxField();
		checkScuolaConfPrenField.setDescription(tooltip);
		checkScuolaScadPagaField= new CheckBoxField();
		checkScuolaScadPagaField.setDescription(tooltip);
		checkScuolaConfPagaField= new CheckBoxField();
		checkScuolaConfPagaField.setDescription(tooltip);
		checkScuolaRegisPagaField= new CheckBoxField();
		checkScuolaRegisPagaField.setDescription(tooltip);
		checkScuolaCongOpzioneField= new CheckBoxField();
		checkScuolaCongOpzioneField.setDescription(tooltip);
		checkScuolaAttPartField= new CheckBoxField();
		checkScuolaAttPartField.setDescription(tooltip);


		// checks invia al referente
		tooltip="Invia la mail al referente";
		checkRefInfoPrenField= new CheckBoxField();
		checkRefInfoPrenField.setDescription(tooltip);

		checkRefScadPrenField= new CheckBoxField();
		checkRefScadPrenField.setDescription(tooltip);

		checkRefConfPrenField= new CheckBoxField();
		checkRefConfPrenField.setDescription(tooltip);

		checkRefScadPagaField= new CheckBoxField();
		checkRefScadPagaField.setDescription(tooltip);

		checkRefConfPagaField= new CheckBoxField();
		checkRefConfPagaField.setDescription(tooltip);

		checkRefRegisPagaField= new CheckBoxField();
		checkRefRegisPagaField.setDescription(tooltip);

		checkRefCongOpzioneField= new CheckBoxField();
		checkRefCongOpzioneField.setDescription(tooltip);

		checkRefAttPartField= new CheckBoxField();
		checkRefAttPartField.setDescription(tooltip);




		// checks no privati
		tooltip="Non invia la mail se si tratta di un privato";
		checkNPInfoPrenField= new CheckBoxField();
		checkNPInfoPrenField.setDescription(tooltip);

		checkNPScadPrenField= new CheckBoxField();
		checkNPScadPrenField.setDescription(tooltip);

		checkNPConfPrenField= new CheckBoxField();
		checkNPConfPrenField.setDescription(tooltip);

		checkNPScadPagaField= new CheckBoxField();
		checkNPScadPagaField.setDescription(tooltip);

		checkNPConfPagaField= new CheckBoxField();
		checkNPConfPagaField.setDescription(tooltip);

		checkNPRegisPagaField= new CheckBoxField();
		checkNPRegisPagaField.setDescription(tooltip);

		checkNPCongOpzioneField= new CheckBoxField();
		checkNPCongOpzioneField.setDescription(tooltip);

		checkNPAttPartField= new CheckBoxField();
		checkNPAttPartField.setDescription(tooltip);



		// bind fields to properties
		getGroup().bind(senderField, KEY_SENDER);
		getGroup().bind(sendBackupMailField, KEY_BACKUP_EMAIL);
		getGroup().bind(backupEmailAddressField, KEY_BACKUP_ADDRESS);

		getGroup().bind(checkMailInfoPrenField, KEY_CHECK_MAIL_INFO_PREN);
		getGroup().bind(checkMailConfPrenField, KEY_CHECK_MAIL_CONF_PREN);
		getGroup().bind(checkMailConfPagaField, KEY_CHECK_MAIL_CONF_PAGA);
		getGroup().bind(checkMailRegisPagaField, KEY_CHECK_MAIL_REGIS_PAGA);
		getGroup().bind(checkMailCongOpzioneField, KEY_CHECK_MAIL_CONG_OPZ);

		getGroup().bind(checkRefInfoPrenField, KEY_REF_MAIL_INFO_PREN);
		getGroup().bind(checkRefScadPrenField, KEY_REF_MAIL_SCAD_PREN);
		getGroup().bind(checkRefConfPrenField, KEY_REF_MAIL_CONF_PREN);
		getGroup().bind(checkRefScadPagaField, KEY_REF_MAIL_SCAD_PAGA);
		getGroup().bind(checkRefConfPagaField, KEY_REF_MAIL_CONF_PAGA);
		getGroup().bind(checkRefRegisPagaField, KEY_REF_MAIL_REGIS_PAGA);
		getGroup().bind(checkRefCongOpzioneField, KEY_REF_MAIL_CONG_OPZ);
		getGroup().bind(checkRefAttPartField, KEY_REF_MAIL_ATT_PART);

		getGroup().bind(checkScuolaInfoPrenField, KEY_SCUOLA_MAIL_INFO_PREN);
		getGroup().bind(checkScuolaScadPrenField, KEY_SCUOLA_MAIL_SCAD_PREN);
		getGroup().bind(checkScuolaConfPrenField, KEY_SCUOLA_MAIL_CONF_PREN);
		getGroup().bind(checkScuolaScadPagaField, KEY_SCUOLA_MAIL_SCAD_PAGA);
		getGroup().bind(checkScuolaConfPagaField, KEY_SCUOLA_MAIL_CONF_PAGA);
		getGroup().bind(checkScuolaRegisPagaField, KEY_SCUOLA_MAIL_REGIS_PAGA);
		getGroup().bind(checkScuolaCongOpzioneField, KEY_SCUOLA_MAIL_CONG_OPZ);
		getGroup().bind(checkScuolaAttPartField, KEY_SCUOLA_MAIL_ATT_PART);

		getGroup().bind(checkNPInfoPrenField, KEY_NP_MAIL_INFO_PREN);
		getGroup().bind(checkNPScadPrenField, KEY_NP_MAIL_SCAD_PREN);
		getGroup().bind(checkNPConfPrenField, KEY_NP_MAIL_CONF_PREN);
		getGroup().bind(checkNPScadPagaField, KEY_NP_MAIL_SCAD_PAGA);
		getGroup().bind(checkNPConfPagaField, KEY_NP_MAIL_CONF_PAGA);
		getGroup().bind(checkNPRegisPagaField, KEY_NP_MAIL_REGIS_PAGA);
		getGroup().bind(checkNPCongOpzioneField, KEY_NP_MAIL_CONG_OPZ);
		getGroup().bind(checkNPAttPartField, KEY_NP_MAIL_ATT_PART);


	}

	@Override
	public void loadContent() {
		super.loadContent();

		// Inverte ogni check principale per 2 volte
		// per innescare un data change che a sua volta sincronizza
		// l'abilitazione o disabilitazione dei check secondari
		checkMailInfoPrenField.setValue(!checkMailInfoPrenField.getValue());
		checkMailInfoPrenField.setValue(!checkMailInfoPrenField.getValue());

		checkMailConfPrenField.setValue(!checkMailConfPrenField.getValue());
		checkMailConfPrenField.setValue(!checkMailConfPrenField.getValue());

		checkMailConfPagaField.setValue(!checkMailConfPagaField.getValue());
		checkMailConfPagaField.setValue(!checkMailConfPagaField.getValue());

		checkMailRegisPagaField.setValue(!checkMailRegisPagaField.getValue());
		checkMailRegisPagaField.setValue(!checkMailRegisPagaField.getValue());

		checkMailCongOpzioneField.setValue(!checkMailCongOpzioneField.getValue());
		checkMailCongOpzioneField.setValue(!checkMailCongOpzioneField.getValue());

	}


	private Component creaComponenteAzioni(){
		VerticalLayout comp = new VerticalLayout();
		comp.addComponent(new Label("<strong>Invio automatico email</strong>", ContentMode.HTML));
		comp.addComponent(checkMailInfoPrenField);
		comp.addComponent(checkMailConfPrenField);
		comp.addComponent(checkMailConfPagaField);
		comp.addComponent(checkMailRegisPagaField);
		comp.addComponent(checkMailCongOpzioneField);
		return comp;
	}

	
	// Crea il GridLayout con i check boxes di abilitazione delle varie spedizioni
	private Component creaComponenteDefaults(){
		Component comp;
		GridLayout layout = new GridLayout(4,9);
		layout.setSpacing(true);

		Alignment align=Alignment.MIDDLE_LEFT;

		comp = new Label("<strong>Destinatari di default</strong>", ContentMode.HTML);
		layout.addComponent(comp, 0, 0);
		layout.setComponentAlignment(comp, align);
		layout.addComponent(new Label("Mail Riepilogo Opzione"), 0, 1);
		layout.addComponent(new Label("Mail Sollecito Conferma Prenotazione"), 0, 2);
		layout.addComponent(new Label("Mail Conferma Prenotazione"), 0, 3);
		layout.addComponent(new Label("Mail Sollecito Conferma Pagamento"),0,4);
		layout.addComponent(new Label("Mail Conferma Pagamento"),0,5);
		layout.addComponent(new Label("Mail Registrazione Pagamento"),0,6);
		layout.addComponent(new Label("Mail Congelamento Prenotazione"),0,7);
		layout.addComponent(new Label("Mail Attestato Partecipazione"),0,8);

		comp=new Label("Alla scuola");
		comp.setWidth("80px");
		layout.addComponent(comp, 1, 0);
		layout.setComponentAlignment(comp, align);
		layout.addComponent(checkScuolaInfoPrenField, 1, 1);
		layout.setComponentAlignment(checkScuolaInfoPrenField, align);

		layout.addComponent(checkScuolaScadPrenField, 1, 2);
		layout.setComponentAlignment(checkScuolaScadPrenField, align);

		layout.addComponent(checkScuolaConfPrenField, 1, 3);
		layout.setComponentAlignment(checkScuolaConfPrenField, align);

		layout.addComponent(checkScuolaScadPagaField, 1, 4);
		layout.setComponentAlignment(checkScuolaScadPagaField, align);

		layout.addComponent(checkScuolaConfPagaField, 1, 5);
		layout.setComponentAlignment(checkScuolaConfPagaField, align);

		layout.addComponent(checkScuolaRegisPagaField, 1, 6);
		layout.setComponentAlignment(checkScuolaRegisPagaField, align);

		layout.addComponent(checkScuolaCongOpzioneField, 1, 7);
		layout.setComponentAlignment(checkScuolaCongOpzioneField, align);

		layout.addComponent(checkScuolaAttPartField, 1, 8);
		layout.setComponentAlignment(checkScuolaAttPartField, align);


		comp=new Label("Al referente");
		comp.setWidth("80px");
		layout.addComponent(comp, 2, 0);
		layout.setComponentAlignment(comp, align);
		layout.addComponent(checkRefInfoPrenField, 2, 1);
		layout.setComponentAlignment(checkRefInfoPrenField, align);

		layout.addComponent(checkRefScadPrenField, 2, 2);
		layout.setComponentAlignment(checkRefScadPrenField, align);

		layout.addComponent(checkRefConfPrenField, 2, 3);
		layout.setComponentAlignment(checkRefConfPrenField, align);

		layout.addComponent(checkRefScadPagaField, 2, 4);
		layout.setComponentAlignment(checkRefScadPagaField, align);

		layout.addComponent(checkRefConfPagaField, 2, 5);
		layout.setComponentAlignment(checkRefConfPagaField, align);

		layout.addComponent(checkRefRegisPagaField, 2, 6);
		layout.setComponentAlignment(checkRefRegisPagaField, align);

		layout.addComponent(checkRefCongOpzioneField, 2, 7);
		layout.setComponentAlignment(checkRefCongOpzioneField, align);

		layout.addComponent(checkRefAttPartField, 2, 8);
		layout.setComponentAlignment(checkRefAttPartField, align);


		comp=new Label("Non inviare a privati");
		comp.setWidth("100px");
		layout.addComponent(comp, 3, 0);
		layout.setComponentAlignment(comp, align);
		layout.addComponent(checkNPInfoPrenField, 3, 1);
		layout.setComponentAlignment(checkNPInfoPrenField, align);

		layout.addComponent(checkNPScadPrenField, 3, 2);
		layout.setComponentAlignment(checkNPScadPrenField, align);

		layout.addComponent(checkNPConfPrenField, 3, 3);
		layout.setComponentAlignment(checkNPConfPrenField, align);

		layout.addComponent(checkNPScadPagaField, 3, 4);
		layout.setComponentAlignment(checkNPScadPagaField, align);

		layout.addComponent(checkNPConfPagaField, 3, 5);
		layout.setComponentAlignment(checkNPConfPagaField, align);

		layout.addComponent(checkNPRegisPagaField, 3, 6);
		layout.setComponentAlignment(checkNPRegisPagaField, align);

		layout.addComponent(checkNPCongOpzioneField, 3, 7);
		layout.setComponentAlignment(checkNPCongOpzioneField, align);

		layout.addComponent(checkNPAttPartField, 3, 8);
		layout.setComponentAlignment(checkNPAttPartField, align);



		return layout;
	}
	
	// componente per le altre opzioni
	private Component creaComponenteOpzioni(){
		FormLayout layout = new FormLayout();
		layout.setSpacing(true);
		layout.addComponent(sendBackupMailField);
		layout.addComponent(backupEmailAddressField);
		layout.addComponent(senderField);
		return layout;
	}

	public PrefSetItem createItem() {
		return new EmailSetItem();
	}

	/**
	 * Item containing form data
	 */
	private class EmailSetItem extends PropertysetItem implements PrefSetItem {

		public EmailSetItem() {
			super();
			
			addItemProperty(KEY_SENDER, new ObjectProperty<String>(CompanyPrefs.senderEmailAddress.getString()));
			addItemProperty(KEY_BACKUP_EMAIL, new ObjectProperty<Boolean>(CompanyPrefs.backupEmail.getBool()));
			addItemProperty(KEY_BACKUP_ADDRESS, new ObjectProperty<String>(CompanyPrefs.backupEmailAddress.getString()));

			addItemProperty(KEY_CHECK_MAIL_INFO_PREN, new ObjectProperty<Boolean>(CompanyPrefs.sendMailInfoPren.getBool()));
			addItemProperty(KEY_CHECK_MAIL_CONF_PREN, new ObjectProperty<Boolean>(CompanyPrefs.sendMailConfPren.getBool()));
			addItemProperty(KEY_CHECK_MAIL_CONF_PAGA, new ObjectProperty<Boolean>(CompanyPrefs.sendMailConfPaga.getBool()));
			addItemProperty(KEY_CHECK_MAIL_REGIS_PAGA, new ObjectProperty<Boolean>(CompanyPrefs.sendMailRegisPaga.getBool()));
			addItemProperty(KEY_CHECK_MAIL_CONG_OPZ, new ObjectProperty<Boolean>(CompanyPrefs.sendMailCongOpzione.getBool()));

			addItemProperty(KEY_REF_MAIL_INFO_PREN, new ObjectProperty<Boolean>(CompanyPrefs.sendMailInfoPrenRef.getBool()));
			addItemProperty(KEY_REF_MAIL_SCAD_PREN, new ObjectProperty<Boolean>(CompanyPrefs.sendMailScadPrenRef.getBool()));
			addItemProperty(KEY_REF_MAIL_CONF_PREN, new ObjectProperty<Boolean>(CompanyPrefs.sendMailConfPrenRef.getBool()));
			addItemProperty(KEY_REF_MAIL_SCAD_PAGA, new ObjectProperty<Boolean>(CompanyPrefs.sendMailScadPagaRef.getBool()));
			addItemProperty(KEY_REF_MAIL_CONF_PAGA, new ObjectProperty<Boolean>(CompanyPrefs.sendMailConfPagaRef.getBool()));
			addItemProperty(KEY_REF_MAIL_REGIS_PAGA, new ObjectProperty<Boolean>(CompanyPrefs.sendMailRegisPagaRef.getBool()));
			addItemProperty(KEY_REF_MAIL_CONG_OPZ, new ObjectProperty<Boolean>(CompanyPrefs.sendMailCongOpzioneRef.getBool()));
			addItemProperty(KEY_REF_MAIL_ATT_PART, new ObjectProperty<Boolean>(CompanyPrefs.sendMailAttPartRef.getBool()));

			addItemProperty(KEY_SCUOLA_MAIL_INFO_PREN, new ObjectProperty<Boolean>(CompanyPrefs.sendMailInfoPrenScuola.getBool()));
			addItemProperty(KEY_SCUOLA_MAIL_SCAD_PREN, new ObjectProperty<Boolean>(CompanyPrefs.sendMailScadPrenScuola.getBool()));
			addItemProperty(KEY_SCUOLA_MAIL_CONF_PREN, new ObjectProperty<Boolean>(CompanyPrefs.sendMailConfPrenScuola.getBool()));
			addItemProperty(KEY_SCUOLA_MAIL_SCAD_PAGA, new ObjectProperty<Boolean>(CompanyPrefs.sendMailScadPagaScuola.getBool()));
			addItemProperty(KEY_SCUOLA_MAIL_CONF_PAGA, new ObjectProperty<Boolean>(CompanyPrefs.sendMailConfPagaScuola.getBool()));
			addItemProperty(KEY_SCUOLA_MAIL_REGIS_PAGA, new ObjectProperty<Boolean>(CompanyPrefs.sendMailRegisPagaScuola.getBool()));
			addItemProperty(KEY_SCUOLA_MAIL_CONG_OPZ, new ObjectProperty<Boolean>(CompanyPrefs.sendMailCongOpzioneScuola.getBool()));
			addItemProperty(KEY_SCUOLA_MAIL_ATT_PART, new ObjectProperty<Boolean>(CompanyPrefs.sendMailAttPartScuola.getBool()));

			addItemProperty(KEY_NP_MAIL_INFO_PREN, new ObjectProperty<Boolean>(CompanyPrefs.sendMailInfoPrenNP.getBool()));
			addItemProperty(KEY_NP_MAIL_SCAD_PREN, new ObjectProperty<Boolean>(CompanyPrefs.sendMailScadPrenNP.getBool()));
			addItemProperty(KEY_NP_MAIL_CONF_PREN, new ObjectProperty<Boolean>(CompanyPrefs.sendMailConfPrenNP.getBool()));
			addItemProperty(KEY_NP_MAIL_SCAD_PAGA, new ObjectProperty<Boolean>(CompanyPrefs.sendMailScadPagaNP.getBool()));
			addItemProperty(KEY_NP_MAIL_CONF_PAGA, new ObjectProperty<Boolean>(CompanyPrefs.sendMailConfPagaNP.getBool()));
			addItemProperty(KEY_NP_MAIL_REGIS_PAGA, new ObjectProperty<Boolean>(CompanyPrefs.sendMailRegisPagaNP.getBool()));
			addItemProperty(KEY_NP_MAIL_CONG_OPZ, new ObjectProperty<Boolean>(CompanyPrefs.sendMailCongOpzioneNP.getBool()));
			addItemProperty(KEY_NP_MAIL_ATT_PART, new ObjectProperty<Boolean>(CompanyPrefs.sendMailAttPartNP.getBool()));

		}

		public void persist() {
			Object obj;
			boolean cont = true;
			
			// se backup email attivo ci deve essere l'indirizzo
			boolean bkemail = (boolean)getItemProperty(KEY_BACKUP_EMAIL).getValue();
			if (bkemail) {
				String bkaddress = (String)getItemProperty(KEY_BACKUP_ADDRESS).getValue();
				if (bkaddress.equals("")) {
					Notification.show("Inserire l'indirizzo email per copie");
					cont=false;
				}
			}
			
			if (cont) {

				obj = getItemProperty(KEY_SENDER).getValue();
				CompanyPrefs.senderEmailAddress.put(obj);

				obj = getItemProperty(KEY_BACKUP_EMAIL).getValue();
				CompanyPrefs.backupEmail.put(obj);
				obj = getItemProperty(KEY_BACKUP_ADDRESS).getValue();
				CompanyPrefs.backupEmailAddress.put(obj);

				obj = getItemProperty(KEY_CHECK_MAIL_INFO_PREN).getValue();
				CompanyPrefs.sendMailInfoPren.put(obj);
				obj = getItemProperty(KEY_CHECK_MAIL_CONF_PREN).getValue();
				CompanyPrefs.sendMailConfPren.put(obj);
				obj = getItemProperty(KEY_CHECK_MAIL_CONF_PAGA).getValue();
				CompanyPrefs.sendMailConfPaga.put(obj);
				obj = getItemProperty(KEY_CHECK_MAIL_REGIS_PAGA).getValue();
				CompanyPrefs.sendMailRegisPaga.put(obj);
				obj = getItemProperty(KEY_CHECK_MAIL_CONG_OPZ).getValue();
				CompanyPrefs.sendMailCongOpzione.put(obj);

				obj = getItemProperty(KEY_REF_MAIL_INFO_PREN).getValue();
				CompanyPrefs.sendMailInfoPrenRef.put(obj);
				obj = getItemProperty(KEY_REF_MAIL_SCAD_PREN).getValue();
				CompanyPrefs.sendMailScadPrenRef.put(obj);
				obj = getItemProperty(KEY_REF_MAIL_CONF_PREN).getValue();
				CompanyPrefs.sendMailConfPrenRef.put(obj);
				obj = getItemProperty(KEY_REF_MAIL_SCAD_PAGA).getValue();
				CompanyPrefs.sendMailScadPagaRef.put(obj);
				obj = getItemProperty(KEY_REF_MAIL_CONF_PAGA).getValue();
				CompanyPrefs.sendMailConfPagaRef.put(obj);
				obj = getItemProperty(KEY_REF_MAIL_REGIS_PAGA).getValue();
				CompanyPrefs.sendMailRegisPagaRef.put(obj);
				obj = getItemProperty(KEY_REF_MAIL_CONG_OPZ).getValue();
				CompanyPrefs.sendMailCongOpzioneRef.put(obj);
				obj = getItemProperty(KEY_REF_MAIL_ATT_PART).getValue();
				CompanyPrefs.sendMailAttPartRef.put(obj);


				obj = getItemProperty(KEY_SCUOLA_MAIL_INFO_PREN).getValue();
				CompanyPrefs.sendMailInfoPrenScuola.put(obj);
				obj = getItemProperty(KEY_SCUOLA_MAIL_SCAD_PREN).getValue();
				CompanyPrefs.sendMailScadPrenScuola.put(obj);
				obj = getItemProperty(KEY_SCUOLA_MAIL_CONF_PREN).getValue();
				CompanyPrefs.sendMailConfPrenScuola.put(obj);
				obj = getItemProperty(KEY_SCUOLA_MAIL_SCAD_PAGA).getValue();
				CompanyPrefs.sendMailScadPagaScuola.put(obj);
				obj = getItemProperty(KEY_SCUOLA_MAIL_CONF_PAGA).getValue();
				CompanyPrefs.sendMailConfPagaScuola.put(obj);
				obj = getItemProperty(KEY_SCUOLA_MAIL_REGIS_PAGA).getValue();
				CompanyPrefs.sendMailRegisPagaScuola.put(obj);
				obj = getItemProperty(KEY_SCUOLA_MAIL_CONG_OPZ).getValue();
				CompanyPrefs.sendMailCongOpzioneScuola.put(obj);
				obj = getItemProperty(KEY_SCUOLA_MAIL_ATT_PART).getValue();
				CompanyPrefs.sendMailAttPartScuola.put(obj);


				obj = getItemProperty(KEY_NP_MAIL_INFO_PREN).getValue();
				CompanyPrefs.sendMailInfoPrenNP.put(obj);
				obj = getItemProperty(KEY_NP_MAIL_SCAD_PREN).getValue();
				CompanyPrefs.sendMailScadPrenNP.put(obj);
				obj = getItemProperty(KEY_NP_MAIL_CONF_PREN).getValue();
				CompanyPrefs.sendMailConfPrenNP.put(obj);
				obj = getItemProperty(KEY_NP_MAIL_SCAD_PAGA).getValue();
				CompanyPrefs.sendMailScadPagaNP.put(obj);
				obj = getItemProperty(KEY_NP_MAIL_CONF_PAGA).getValue();
				CompanyPrefs.sendMailConfPagaNP.put(obj);
				obj = getItemProperty(KEY_NP_MAIL_REGIS_PAGA).getValue();
				CompanyPrefs.sendMailRegisPagaNP.put(obj);
				obj = getItemProperty(KEY_NP_MAIL_CONG_OPZ).getValue();
				CompanyPrefs.sendMailCongOpzioneNP.put(obj);
				obj = getItemProperty(KEY_NP_MAIL_ATT_PART).getValue();
				CompanyPrefs.sendMailAttPartNP.put(obj);



			}

		}

	}

	@Override
	public String getTitle() {
		return "Configurazione invio email";
	}

}
