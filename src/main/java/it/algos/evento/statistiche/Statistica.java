package it.algos.evento.statistiche;

import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import it.algos.evento.statistiche.StatisticaBase.ElaboraListener;

/**
 * Interfaccia che definisce una Statistica per il modulo Statistiche
 */
public interface Statistica {

	/**
	 * @return il nome della statistica
	 */
	public String getName();

	/**
	 * @return il combo per la selezione del periodo e il bottone elabora (di solito)
	 */
	public Component getComponenteStatistica();

	/**
	 * Listener notificato quando si preme il bottone elabora
	 */
	public void addElaboraListener(ElaboraListener listener);

	/**
	 * @return la tavola della statistica da visualizzare
	 */
	public Table getTable();

}// end of interface
