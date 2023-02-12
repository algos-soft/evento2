package it.algos.evento.entities.prenotazione.eventi;

import com.vaadin.data.Property;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import it.algos.evento.EventoBootStrap;
import it.algos.evento.entities.prenotazione.EmailFailedException;
import it.algos.evento.entities.prenotazione.Prenotazione;
import it.algos.evento.entities.prenotazione.PrenotazioneModulo;
import it.algos.webbase.multiazienda.ETable;
import it.algos.webbase.web.dialog.AlertDialog;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.lib.LibDate;
import it.algos.webbase.web.module.ModulePop;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("serial")
public class EventoPrenTable extends ETable {

	private TipoEventoPrenConverter converter = new TipoEventoPrenConverter();
	protected static String colEmail="email";
	protected static String colEsito="esito";

	public EventoPrenTable(ModulePop module) {
		super(module);
		
		setColumnHeader(EventoPren_.timestamp, "Data e ora");
		setColumnHeader(EventoPren_.prenotazione, "Prenotazione");
		setColumnHeader(EventoPren_.tipo, "Tipo evento");
		setColumnHeader(EventoPren_.dettagli, "Dettagli");
		setColumnHeader(EventoPren_.user, "Utente");

		setColumnAlignment(EventoPren_.tipo, Align.LEFT);

	}


	@Override
	protected void createAdditionalColumns() {
		addGeneratedColumn(colEmail, new EmailColumnGenerator());
		addGeneratedColumn(colEsito, new EsitoColumnGenerator());
	}


	protected Object[] getDisplayColumns() {
		return new Object[] { EventoPren_.timestamp, EventoPren_.prenotazione, EventoPren_.tipo, EventoPren_.dettagli,
				EventoPren_.user , colEmail, colEsito};
	}// end of method

	@Override
	protected String formatPropertyValue(Object rowId, Object colId, Property property) {

		if (colId.equals(EventoPren_.tipo.getName())) {
			return converter.convertToPresentation(property.getValue());
		}

		if (colId.equals(EventoPren_.timestamp.getName())) {
			return LibDate.toStringDDMMYYYYHHMM((Date) property.getValue());
		}

		return super.formatPropertyValue(rowId, colId, property);

	}// end of method
	
	/**
	 * Genera la colonna bottone email.
	 */
	class EmailColumnGenerator implements ColumnGenerator {

		@SuppressWarnings("unchecked")
		public Component generateCell(Table source, Object itemId, Object columnId) {
			Component comp = null;

			final EventoPren evento = (EventoPren)getEntity(itemId);

			if (evento.isInvioEmail()) {
				Button bSend = new Button("Reinvia...");
				bSend.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						new DialogoConfermaReinvio(evento).show(getUI());
					}
				});
				comp = bSend;
			}
			return comp;
		}
	}
	
	
	/**
	 * Dialogo di conferma reinvio email
	 */
	class DialogoConfermaReinvio extends ConfirmDialog{
		private EventoPren evento;
		
		public DialogoConfermaReinvio(EventoPren evento) {
			super(null);
			this.evento=evento;
			setTitle("Reinvio email");
			setMessage("Vuoi reinviare questa email?");
		}

		@Override
		protected void onConfirm() {
			super.onConfirm();

			AlertDialog progressDialog = new AlertDialog("Invio mail in corso");
			progressDialog.show();

			NotifyingThread thread = new NotifyingThread() {

				@Override
				public void doRun() {
					Prenotazione pren = evento.getPrenotazione();
					TipoEventoPren tipo = TipoEventoPren.getItem(evento.getTipo());
					String user = EventoBootStrap.getUsername();
					String emails = extractEmails(evento.getDettagli());
					try {
						PrenotazioneModulo.sendEmailEvento(pren, tipo, user, emails);
						Notification.show("Invio mail eseguito");
					} catch (EmailFailedException e) {
						Notification.show("Invio email fallito", "\n"+e.getMessage(), Notification.Type.WARNING_MESSAGE);
					}
				}

			};

			thread.addListener(thread1 -> {
				progressDialog.close();
				refresh();
			});

			thread.start();

		}
		
	}

	abstract class NotifyingThread extends Thread {

		private final Set<ThreadCompleteListener> listeners = new CopyOnWriteArraySet<>();
		public final void addListener(final ThreadCompleteListener listener) {
			listeners.add(listener);
		}
		public final void removeListener(final ThreadCompleteListener listener) {
			listeners.remove(listener);
		}
		private final void notifyListeners() {
			for (ThreadCompleteListener listener : listeners) {
				listener.onThreadCompleted(this);
			}
		}

		@Override
		public final void run() {
			try {
				doRun();
			} finally {
				notifyListeners();
			}
		}
		public abstract void doRun();

	}


	public interface ThreadCompleteListener {
		void onThreadCompleted(NotifyingThread thread);
	}


	/**
	 * Genera la colonna esito email.
	 */
	class EsitoColumnGenerator implements ColumnGenerator {

		@SuppressWarnings("unchecked")
		public Component generateCell(Table source, Object itemId, Object columnId) {
			Component comp = null;

			EventoPren evento = (EventoPren)getEntity(itemId);

			if (evento.isInvioEmail()) {
				if (evento.isEmailInviata()) {
					comp = new Label( "\u2714");
				}else{
					comp = new Label( "\u2718");
				}
			}
			return comp;
		}
	}

	private String extractEmails(String s){
		List<String> emails = parseEmails(s);
		StringBuffer sb = new StringBuffer();
		int i=0;
		for(String email : emails){
			if(i!=0){
				sb.append(", ");
			}
			sb.append(email);
			i++;
		}
		return sb.toString();
	}

	private List<String> parseEmails(String s){
		List<String> emails = new ArrayList<>();
		Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(s);
		while (m.find()) {
			emails.add(m.group());
		}
		return emails;
	}







}
