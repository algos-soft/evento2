package it.algos.webbase.domain.ruolo;

import java.util.ArrayList;

public enum TipoRuolo {
	developer, admin, user, guest;

	public static ArrayList<String> getAllNames() {
		ArrayList<String> lista = new ArrayList<String>();

		for (TipoRuolo tipo : TipoRuolo.values()) {
			lista.add(tipo.toString());
		}// fine del ciclo for

		return lista;
	}// fine del metodo statico

}// end of enumeration
