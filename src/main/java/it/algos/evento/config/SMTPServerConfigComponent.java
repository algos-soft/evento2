package it.algos.evento.config;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import it.algos.evento.pref.EventoPrefs;
import it.algos.webbase.web.field.CheckBoxField;
import it.algos.webbase.web.field.IntegerField;
import it.algos.webbase.web.field.PasswordField;
import it.algos.webbase.web.field.TextField;

@SuppressWarnings("serial")
public class SMTPServerConfigComponent extends BaseConfigPanel implements View {

	private static final String KEY_HOST = "smtp";
	private static final String KEY_USER = "user";
	private static final String KEY_PASSWORD = "password";
	private static final String KEY_USE_AUTH = "useauth";
	private static final String KEY_PORT = "port";

	private Field<?> smtpField;
	private Field<?> portField;
	private Field<?> useAuthField;
	private Field<?> smtpUserField;
	private Field<?> smtpPasswordField;

	public SMTPServerConfigComponent() {
		super();
		//addStyleName("yellowBg");

		
		// crea i fields
		createFields();
		
		// crea la UI
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.addComponent(new Label("Configurazione SMTP server utilizzato per tutta la posta in uscita"));
		layout.addComponent(createMainComponent());
		addComponent(layout);
		
		addComponent(createButtonPanel());

	}
	
	private Component createMainComponent(){
		FormLayout layout = new FormLayout();
		layout.addComponent(smtpField);
		layout.addComponent(portField);
		layout.addComponent(useAuthField);
		layout.addComponent(smtpUserField);
		layout.addComponent(smtpPasswordField);
		return layout;
	}
	
	

	// crea e registra i fields
	private void createFields(){
		// create and add fields and other components
		smtpField = new TextField("SMTP server");
		portField = new IntegerField("Porta");
		useAuthField = new CheckBoxField("Usa autenticazione SMTP");
		smtpUserField = new TextField("SMTP username");
		smtpPasswordField = new PasswordField("SMTP password");
		

		// bind fields to properties
		getGroup().bind(smtpField, KEY_HOST);
		getGroup().bind(smtpUserField, KEY_USER);
		getGroup().bind(smtpPasswordField, KEY_PASSWORD);
		getGroup().bind(useAuthField, KEY_USE_AUTH);
		getGroup().bind(portField, KEY_PORT);

	}
	
	
	public PrefSetItem createItem() {
		return new SMTPSetItem();
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
		loadContent();
	}

	/**
	 * Item containing form data
	 */
	private class SMTPSetItem extends PropertysetItem implements PrefSetItem {

		public SMTPSetItem() {
			super();
			
			addItemProperty(KEY_HOST, new ObjectProperty<String>(EventoPrefs.smtpServer.getString()));
			addItemProperty(KEY_USER, new ObjectProperty<String>(EventoPrefs.smtpUserName.getString()));
			addItemProperty(KEY_PASSWORD, new ObjectProperty<String>(EventoPrefs.smtpPassword.getString()));
			addItemProperty(KEY_PORT, new ObjectProperty<Integer>(EventoPrefs.smtpPort.getInt()));
			addItemProperty(KEY_USE_AUTH, new ObjectProperty<Boolean>(EventoPrefs.smtpUseAuth.getBool()));

		}

		public void persist() {
			Object obj;
			boolean cont = true;
			
			
			if (cont) {

				obj = getItemProperty(KEY_HOST).getValue();
				EventoPrefs.smtpServer.put(obj);

				obj = getItemProperty(KEY_USER).getValue();
				EventoPrefs.smtpUserName.put(obj);

				obj = getItemProperty(KEY_PASSWORD).getValue();
				EventoPrefs.smtpPassword.put(obj);
				
				obj = getItemProperty(KEY_PORT).getValue();
				EventoPrefs.smtpPort.put(obj);
				
				obj = getItemProperty(KEY_USE_AUTH).getValue();
				EventoPrefs.smtpUseAuth.put(obj);
				
			}

		}

	}

	@Override
	public String getTitle() {
		return "Configurazione server SMTP";
	}


}
