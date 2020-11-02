package it.algos.evento.statistiche;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import it.algos.evento.entities.rappresentazione.Rappresentazione;
import it.algos.webbase.web.lib.LibDate;

import java.util.ArrayList;
import java.util.Date;

@SuppressWarnings("serial")
public class StatisticaPerRappresentazione extends StatisticaBase {

	public StatisticaPerRappresentazione() {
		super("Statistica per rappresentazione");
	}// end of constructor

	/**
	 * crea il container con le colonne
	 */
	@Override
	protected void creaContainer() {
		container = new IndexedContainer();
		addContainerProperty(Colonne.siglaEvento);
		addContainerProperty(Colonne.titoloEvento);
		addContainerProperty(Colonne.data);
		addContainerProperty(Colonne.ora);
		addContainerProperty(Colonne.numscuole);
		addContainerProperty(Colonne.interi);
		addContainerProperty(Colonne.ridotti);
		addContainerProperty(Colonne.disabili);
		addContainerProperty(Colonne.accomp);
		addContainerProperty(Colonne.totSpettatori);
		addContainerProperty(Colonne.capienza);
		addContainerProperty(Colonne.totPagare);
		addContainerProperty(Colonne.totPagato);
	}// end of method

	/**
	 * popola il container con i dati
	 */
	@Override
	protected void popola(Date data1, Date data2) {
		super.popola(data1, data2);

		// lista delle rappresentazioni in ordine di data
		ArrayList<Rappresentazione> lista = super.getRappresentazioni(data1, data2);

		//
		WrapTotali wrapper;
		String sigla;
		String titolo;
		Date data;
		String ora;
		for (Rappresentazione rapp : lista) {
			wrapper = super.analizzaPrenotazioni(rapp);
			sigla = rapp.getEvento().getSigla();
			titolo = rapp.getEvento().getTitolo();
			data = rapp.getDataRappresentazione();
			ora = LibDate.toStringHHMM(data);
			addRiga(sigla, titolo, data, ora, wrapper);
		}// end of for cycle

	}// end of method

	@SuppressWarnings("unchecked")
	private void addRiga(String sigla, String titolo, Date data, String ora, WrapTotali wrap) {
		Item item = container.getItem(container.addItem());
		item.getItemProperty(Colonne.siglaEvento.getTitolo()).setValue(sigla);
		item.getItemProperty(Colonne.titoloEvento.getTitolo()).setValue(titolo);
		item.getItemProperty(Colonne.data.getTitolo()).setValue(data);
		item.getItemProperty(Colonne.ora.getTitolo()).setValue(ora);
		item.getItemProperty(Colonne.numscuole.getTitolo()).setValue(wrap.getTotScuole());
		item.getItemProperty(Colonne.capienza.getTitolo()).setValue(wrap.getTotCapienza());

		// aggiunge le colonne standard
		super.addRigaBase(wrap, item);

		// incrementa i totali
		super.addTotali(wrap);
	}// end of method

}// end of class
