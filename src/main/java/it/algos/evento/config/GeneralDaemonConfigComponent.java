package it.algos.evento.config;

import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import it.algos.evento.EventoApp;
import it.algos.evento.daemons.DaemonPrenScadute;
import it.algos.evento.pref.EventoPrefs;
import it.algos.webbase.web.field.CheckBoxField;

import javax.servlet.ServletContext;

@SuppressWarnings("serial")
public class GeneralDaemonConfigComponent extends BaseConfigPanel implements View {

	private static final String KEY_SERVICE_START = "servicestart";



	private Label serviceStatus;
	private Button bStartDaemon;
	private Button bStopDaemon;
	private CheckBoxField checkbox;

	public GeneralDaemonConfigComponent() {
		super();

		// crea e registra i fields
		creaFields();
		
		serviceStatus = new Label("", ContentMode.HTML);

		bStartDaemon = new Button("Attiva il servizio");
		bStartDaemon.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				DaemonPrenScadute.getInstance().start();
				refreshStatus();
			}
		});

		bStopDaemon = new Button("Ferma il servizio");
		bStopDaemon.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				DaemonPrenScadute.getInstance().stop();
				refreshStatus();
			}
		});
		// crea la UI
		
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);

		String title = "<b>Servizio di controllo delle posizioni scadute</b><p>";
		title += "Ogni ora esegue i check per tutte le aziende abilitate per quell'ora.<br>"
				+ "(Vedi abilitazione specifiche nelle preferenze delle singole aziende).";
		Label infoLabel = new Label(title, ContentMode.HTML);
		layout.addComponent(infoLabel);
		layout.addComponent(serviceStatus);
		layout.addComponent(bStartDaemon);
		layout.addComponent(bStopDaemon);
		layout.addComponent(checkbox);


		addComponent(layout);
		addComponent(createButtonPanel());

		refreshStatus();

	}
	
	// crea e registra i fields
	private void creaFields(){
		
		// check box servizio attivo
		checkbox=new CheckBoxField("Attiva il servizio all'avvio del server");

		// bind fields to properties
		getGroup().bind(checkbox, KEY_SERVICE_START);

	}
	

	private void refreshStatus() {

		boolean serviceIsOn = false;
		ServletContext svc=EventoApp.getServletContext();

		Object obj = svc.getAttribute(DaemonPrenScadute.DAEMON_NAME);
		if ((obj != null) && (obj instanceof Boolean)) {
			boolean flag = (Boolean) obj;
			if (flag) {
				serviceIsOn = true;
			}
		}

		if (serviceIsOn) {
			serviceStatus.setValue("Il servizio è <b>ATTIVO</b>");
			bStartDaemon.setEnabled(false);
			bStopDaemon.setEnabled(true);
		} else {
			serviceStatus.setValue("Il servizio è <b>FERMO<b>");
			bStartDaemon.setEnabled(true);
			bStopDaemon.setEnabled(false);
		}

	}



	@Override
	public Component getUIComponent() {
		return this;
	}

	@Override
	public String getTitle() {
		return "Controllo daemon check posizioni scadute";
	}
	
	
	public PrefSetItem createItem() {
		return new DaemonSetItem();
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
		loadContent();
	}

	/**
	 * Item containing form data
	 */
	private class DaemonSetItem extends PropertysetItem implements PrefSetItem {

		public DaemonSetItem() {
			super();
			
			addItemProperty(KEY_SERVICE_START, new ObjectProperty<Boolean>(EventoPrefs.startDaemonAtStartup.getBool()));
			
		}
		
		

		public void persist() {
			Object obj = getItemProperty(KEY_SERVICE_START).getValue();
			EventoPrefs.startDaemonAtStartup.put(obj);

		}

	}





}
