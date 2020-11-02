package it.algos.evento.entities.prenotazione.eventi;

import it.algos.evento.entities.lettera.ModelliLettere;

/**
 * Enum dei tipi di evento che si possono verificare e loggare poer una prenotazione
 */
public enum TipoEventoPren {

	invioIstruzioni(1, "Invio istruzioni", ModelliLettere.istruzioniPrenotazione),
	
	confermaPrenotazione(2, "Conferma prenotazione", ModelliLettere.confermaPrenotazione),

	confermaPagamento(3, "Conferma pagamento", ModelliLettere.confermaPagamento),

	registrazionePagamento(4, "Registrazione pagamento", ModelliLettere.registrazionePagamento),
	
	promemoriaScadenzaPagamento(5, "Promemoria scadenza pagamento", ModelliLettere.memoScadPagamento),
	
	promemoriaInvioSchedaPrenotazione(6, "Promemoria invio scheda prenotazione", ModelliLettere.memoScadPrenotazione),
	
	congelamentoOpzione(7, "Congelamento opzione", ModelliLettere.congelamentoOpzione),
	
	attestatoPartecipazione(8, "Attestato di partecipazione", ModelliLettere.attestatoPartecipazione);

	private int id;
	private String descrizione;
	private ModelliLettere modelloLettera;

	private TipoEventoPren(int id, String descrizione, ModelliLettere modelloLettera) {
		this.id = id;
		this.descrizione = descrizione;
		this.modelloLettera=modelloLettera;
	}

	public int getId() {
		return id;
	}

	public String getDescrizione() {
		return descrizione;
	}
	
	public ModelliLettere getModelloLettera() {
		return modelloLettera;
	}

	@Override
	public String toString() {
		return descrizione;
	}

	public static TipoEventoPren getItem(int id) {
		TipoEventoPren itemFound = null;
		for (TipoEventoPren item : TipoEventoPren.values()) {
			if (item.getId() == id) {
				itemFound = item;
				break;
			}
		}
		return itemFound;
	}

}
