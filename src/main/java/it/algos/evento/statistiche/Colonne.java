package it.algos.evento.statistiche;

import it.algos.evento.entities.comune.Comune;
import it.algos.evento.entities.scuola.Scuola;

import java.util.Date;

/**
 * Enum descrittiva delle colonne Ogni statistica può usare ANCHE PARZIALMENTE questa Enum
 */
public enum Colonne {
	
	siglaEvento("Sigla", String.class, ""),
	
	titoloEvento("Evento", String.class, ""),

	data("Data", Date.class, ""),
	
	ora("Ora", String.class, ""),

	mese("Mese", String.class, ""),

	scuola("Scuola", Scuola.class, 0),

	scuolaNome("Scuola", String.class, 0),

	scuolaOrdine("Ordine", String.class, 0),

	comune("Comune", Comune.class, ""),

	nomeInsegnante("Insegnante", String.class, ""),
	
	materie("Materie", String.class, ""),

	interi("Interi", Integer.class, 0),

	ridotti("Ridotti", Integer.class, 0),

	disabili("Disabili", Integer.class, 0),
	
	accomp("Accomp.", Integer.class, 0),
	
	totSpettatori("Totale", Integer.class, 0),
	
	capienza("Capienza", Integer.class, 0),

	numscuole("N. scuole", Integer.class, 0),
	
	numrappresentazioni("N. rappresentazioni", Integer.class, 0),

	totPagare("A pagare", Double.class, 0.0),

	totPagato("Pagato", Double.class, 0.0),

	totale("Tot.", Integer.class, 0),
	;

	String titolo;
	Class clazz;
	Object defValue;

	private Colonne(String titolo, Class clazz, Object defValue) {
		this.titolo = titolo;
		this.clazz = clazz;
		this.defValue = defValue;
	}// end of constructor

	public String getTitolo() {
		return titolo;
	}

	public Class getClazz() {
		return clazz;
	}

	public Object getDefValue() {
		return defValue;
	}

}// end of Enumeration
