package it.algos.evento.statistiche;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import it.algos.evento.entities.prenotazione.Prenotazione;
import it.algos.evento.entities.prenotazione.Prenotazione_;
import it.algos.evento.entities.rappresentazione.Rappresentazione;
import it.algos.evento.entities.scuola.Scuola;
import it.algos.evento.multiazienda.EQuery;
import it.algos.webbase.multiazienda.CompanyQuery;

import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

@SuppressWarnings("serial")
public class StatisticaPerScuola extends StatisticaBase {

	public StatisticaPerScuola() {
		super("Statistica per scuola");
	}// end of constructor


	/**
	 * crea il container con le colonne
	 */
	protected void creaContainer() {
		container = new IndexedContainer();
		addContainerProperty(Colonne.scuolaNome);
		addContainerProperty(Colonne.scuolaOrdine);
		addContainerProperty(Colonne.comune);

		super.creaContainer();
	}// end of method

	/**
	 * popola il container con i dati
	 */
	protected void popola(Date data1, Date data2) {
		super.popola(data1, data2);

		// lista delle scuole in ordine di data
		LinkedHashMap<Scuola, ArrayList<Prenotazione>> mappa = getScuole(data1, data2);

		//
		WrapTotali wrapper = null;
		for (Scuola scuola : mappa.keySet()) {
			wrapper = analizzaScuole(scuola, mappa.get(scuola));
			if (wrapper != null) {
				addRiga(scuola, wrapper);
			}// end of if cycle
		}// end of for cycle

	}// end of method

	/**
	 * Recupera tutte le scuole nell'intervallo di date
	 * 
	 * Recupera prima tutte le rappresentazioni nell'intervallo <br>
	 * Recupera poi tutte le prenotazioni per ogni rappresentazione <br>
	 * Recupera poi ed accumula la scuola di ogni prenotazione <br>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private LinkedHashMap<Scuola, ArrayList<Prenotazione>> getScuole(Date data1, Date data2) {
		LinkedHashMap<Scuola, ArrayList<Prenotazione>> mappa = new LinkedHashMap<Scuola, ArrayList<Prenotazione>>();
		SingularAttribute attr = Prenotazione_.rappresentazione;
		ArrayList<Rappresentazione> rappresentazioni = getRappresentazioni(data1, data2);
		List<Prenotazione> prenotazioni;
		Scuola scuola;
		ArrayList<Prenotazione> listaPren;

		for (Rappresentazione rapp : rappresentazioni) {
			prenotazioni = (List<Prenotazione>) CompanyQuery.getList(Prenotazione.class, attr, rapp);
			if (prenotazioni != null) {
				for (Prenotazione prenot : prenotazioni) {
					scuola = null;
					scuola = prenot.getScuola();
					if (scuola != null) {
						if (mappa.containsKey(scuola)) {
							listaPren = mappa.get(scuola);
							listaPren.add(prenot);
						} else {
							listaPren = new ArrayList<Prenotazione>();
							listaPren.add(prenot);
							mappa.put(scuola, listaPren);
						}// end of if/else cycle
					}// end of if cycle
				}// end of for cycle
			}// end of if cycle
		}// end of for cycle

		return mappa;
	}// end of method

	/**
	 * Analizza le prenotazioni per una data scuola e ritorna un wrapper con totali
	 */
	private WrapTotali analizzaScuole(Scuola scuola, ArrayList<Prenotazione> prenotazioni) {
		WrapTotali wrap = new WrapTotali();

		for (Prenotazione prenotazione : prenotazioni) {
			wrap.addPren(prenotazione);
		}// end of for cycle

		return wrap;
	}// end of method

	@SuppressWarnings("unchecked")
	private void addRiga(Scuola scuola, WrapTotali wrap) {
		Item item = container.getItem(container.addItem());
		item.getItemProperty(Colonne.scuolaNome.getTitolo()).setValue(scuola.getNome());
		item.getItemProperty(Colonne.scuolaOrdine.getTitolo()).setValue(scuola.getOrdine().getSigla());
		item.getItemProperty(Colonne.comune.getTitolo()).setValue(scuola.getComune());

		// aggiunge le colonne standard
		super.addRigaBase(wrap, item);

		// incrementa i totali
		super.addTotali(wrap);
	}// end of method

}// end of class
