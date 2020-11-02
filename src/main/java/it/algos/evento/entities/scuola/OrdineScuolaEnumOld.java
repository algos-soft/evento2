package it.algos.evento.entities.scuola;

public enum OrdineScuolaEnumOld {

	infanzia(1, "Infanzia"), elementare(2, "Elementare"), media(3, "Media"), superiore(4, "Superiore"), universita(5,
			"Università");

	private int id;
	private String descrizione;

	private OrdineScuolaEnumOld(int id, String descrizione) {
		this.id = id;
		this.descrizione = descrizione;
	}

	public int getId() {
		return id;
	}

	public String getDescrizione() {
		return descrizione;
	}

	@Override
	public String toString() {
		return descrizione;
	}

	public static OrdineScuolaEnumOld getOrdineScuola(int id) {
		OrdineScuolaEnumOld ordineFound = null;
		for (OrdineScuolaEnumOld ordine : OrdineScuolaEnumOld.values()) {
			if (ordine.getId() == id) {
				ordineFound = ordine;
				break;
			}
		}
		return ordineFound;
	}

}
