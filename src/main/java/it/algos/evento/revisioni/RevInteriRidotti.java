package it.algos.evento.revisioni;

import it.algos.evento.entities.evento.Evento;
import it.algos.evento.entities.prenotazione.Prenotazione;
import it.algos.evento.multiazienda.EQuery;
import it.algos.webbase.multiazienda.CompanyQuery;

import java.math.BigDecimal;
import java.util.List;

/**
 * Revisione dati.
 * <p>
 * Negli Eventi, sposta il prezzo da Intero a Ridotto.<br>
 * Nelle Prenotazioni, sposta il numero di posti da Intero a Ridotto.<br>
 * I totali non cambiano.
 */
public class RevInteriRidotti implements Runnable {

	@Override
	public void run() {

		List<Evento> eventi = (List<Evento>) CompanyQuery.getList(Evento.class);
		for (Evento evento : eventi) {
			evento.setImportoRidotto(evento.getImportoIntero());
			evento.setImportoIntero(new BigDecimal(0));
			evento.save();
		}
		
		List<Prenotazione> prenotazioni = (List<Prenotazione>) CompanyQuery.getList(Prenotazione.class);
		for (Prenotazione pren : prenotazioni) {

			// aggiunge i ridotti (ex disabili) agli omaggi, aggiungendoli agli accompagnatori, e azzera i ridotti
			int exAccomp = pren.getNumDisabili();
			int exDisabili = pren.getNumRidotti();
			int newOmaggi = exAccomp+exDisabili;
			pren.setNumDisabili(newOmaggi);
			pren.setNumRidotti(0);
			
			// sposta gli interi (ex standard) nei ridotti, e azzera gli interi
			int exStandard = pren.getNumInteri();
			pren.setNumRidotti(exStandard);
			pren.setNumInteri(0);
			
			// registra
			pren.save();
		}

	}

}
