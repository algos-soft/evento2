package it.algos.evento.statistiche;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Table;
import it.algos.evento.statistiche.StatisticaBase.ElaboraListener;

@SuppressWarnings("serial")
public abstract class StatisticaGenerale extends CustomComponent implements Statistica {

	private Table table;

	protected void setTable(Table table) {
		this.table = table;
	}// end of method

	public Table getTable() {
		return table;
	}// end of method

	/**
	 * Listener notificato quando si preme il bottone elabora
	 */
	public void addElaboraListener(ElaboraListener listener) {
	}// end of method

}// end of class
