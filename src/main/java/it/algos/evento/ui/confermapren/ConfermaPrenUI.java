package it.algos.evento.ui.confermapren;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import it.algos.evento.entities.prenotazione.ConfermaPrenClienteModulo;
import it.algos.evento.entities.prenotazione.Prenotazione;
import it.algos.evento.entities.prenotazione.Prenotazione_;
import it.algos.webbase.web.lib.LibUUID;
import it.algos.webbase.web.login.LoginEvent;
import it.algos.webbase.web.query.AQuery;
import it.algos.webbase.web.ui.AlgosUI;

@SuppressWarnings("serial")
@Theme("evento")
@Title("eVento - conferma prenotazione")
public class ConfermaPrenUI extends AlgosUI {

	@Override
	protected void init(VaadinRequest request) {

		String error = "";
		Prenotazione pren=null;

		// legge il parametro "uuid" e lo valida, recupera la prenotazione
		String uuid = request.getParameter("uuid");
		if (uuid != null) {
			if (LibUUID.validateUUID(uuid)) {
				pren = (Prenotazione) AQuery.getEntity(Prenotazione.class,
						Prenotazione_.uuid, uuid);
				if (pren == null) {
					error = "prenotazione non trovata";
				}
			} else {
				error = "uuid non valido";
			}
		} else {
			error = "parametro uuid mancante";
		}

		
		// presenta il form o visualizza l'errore
		Component comp;
		if (pren!=null) {
			comp = new ConfermaPrenClienteModulo(pren);
		} else {
			comp = new Label(error);
		}

		setContent(comp);
	}

	@Override
	public void onUserLogin(LoginEvent e) {

	}
}
