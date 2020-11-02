package it.algos.evento.entities.lettera;

import java.util.ArrayList;

/**
 * Enum delle informazioni disponibili per la sostituzione nelle lettere
 */
public enum LetteraKeys {


	numeroPrenotazione("numeroPrenotazione","Numero di riferimento prenotazione"),

	dataPrenotazione("dataPrenotazione","Data della prenotazione"),
	
	titoloInsegnante("titoloInsegnante","Titolo dell'insegnante che ha prenotato"),
	
	nomeInsegnante("nomeInsegnante","Nome dell'insegnante che ha prenotato"),
	
	cognomeInsegnante("cognomeInsegnante","Cognome dell'insegnante che ha prenotato"),
	
	telReferente("telReferente","Telefono del referente"),
	
	nomeScuola("nomeScuola","Nome della scuola"),
	
	indirizzoScuola("indirizzoScuola","Indirizzo della scuola (via, piazza...)"),
	
	localitaScuola("localitaScuola","Località delle scuola (CAP, comune, provincia)"),
	
	telefonoScuola("telefonoScuola","Telefono della scuola"),
	
	faxScuola("faxScuola","Fax della scuola"),
	
	emailScuola("emailScuola","E-mail della scuola"),

	classe("classe","Classe"),

	titoloEvento("titoloEvento","Titolo dell'evento"),
	
	dataRappresentazione("dataRappresentazione","Data della rappresentazione"),
	
	oraRappresentazione("oraRappresentazione","Ora della rappresentazione"),
	
	nomeSala("nomeSala","Nome della sala"),

	numPostiInteri("numPostiInteri","Numero di posti interi"),
	
	numPostiRidotti("numPostiRidotti","Numero di posti ridotti"),
	
	numPostiDisabili("numPostiDisabili","Numero di posti per disabili"),
	
	numPostiAccomp("numPostiAccomp","Numero di posti accompagnatore"),
	
	numPostiTotali("numPostiTotali","Numero totale di spettatori"),

	dataScadenzaConfermaPrenotazione("dataScadenzaConfermaPrenotazione", "Data scadenza invio scheda di conferma prenotazione"),

	importoIntero("importoIntero","Importo posto intero"),

	importoRidotto("importoRidotto","Importo posto ridotto"),

	importoDisabile("importoDisabile","Importo posto disabile"),

	importoAccomp("importoAccomp","Importo posto accompagnatore"),

	importoGruppo("importoGruppo","Importo per gruppo"),

	importoTotale("importoTotale","Importo totale a pagare"),

	modoPagamento("modoPagamento","Modalità di pagamento"),
	
	dataScadenzaPagamento("dataScadenzaPagamento","Data di scadenza conferma pagamento"),
	
	dataCorrente("dataCorrente","La data odierna (di invio mail)"),

	dataPagamentoRicevuto("dataPagamentoRicevuto","La data di ricevimento del pagamento"),

	importoPagato("importoPagato","L'importo totale effettivamente pagato"),

	;
	
	
	


	private String key;
	private String descrizione;

	private LetteraKeys(String key, String descrizione) {
		this.key = key;
		this.descrizione=descrizione;
	}// end of constructor

	public String getKey() {
		return key;
	}
	
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * lista di tutti gli elementi dell'Enumeration
	 */
	public static ArrayList<LetteraKeys> getAll() {
		ArrayList<LetteraKeys> lista = new ArrayList<LetteraKeys>();

		for (LetteraKeys key : values()) {
			lista.add(key);
		} // fine del ciclo for

		return lista;
	}// fine del metodo

	/**
	 * elenco di tutte le chiavi dell'Enumeration una per riga
	 */
	public static String getElenco() {
		String elenco = "";
		String aCapo = "\n";

		for (LetteraKeys key : values()) {
			elenco += key;
			elenco += aCapo;
		} // fine del ciclo for
		elenco = elenco.trim();

		return elenco;
	}// fine del metodo
	
	/**
	 * Ritorna un testo demo contenente un esempio di tutte le sostituzioni supportate
	 * @return il testo demo
	 */
	public static String getTestoDemo() {
		String elenco = "";
		elenco+="Elenco delle chiavi di sostituzione disponibili per la composizione delle email.<p>\n";
		elenco+="Le chiavi vanno inserite tra parentesi graffe (es. {keyname}) e vengono sostituite dai valori reali.<p><p>\n";

		for (LetteraKeys item : values()) {
			elenco += "<strong>"+item.getKey()+ "</strong> ["+item.getDescrizione()+"] -> ${"+item.getKey()+"}<br>\n";
		} // fine del ciclo for
		elenco = elenco.trim();

		return elenco;
	}// fine del metodo


}// end of entity class
