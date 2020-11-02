package it.algos.evento.statistiche;

import com.vaadin.data.Container;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickListener;
import it.algos.evento.entities.prenotazione.Prenotazione;
import it.algos.evento.entities.prenotazione.Prenotazione_;
import it.algos.evento.entities.rappresentazione.Rappresentazione;
import it.algos.evento.entities.rappresentazione.Rappresentazione_;
import it.algos.evento.entities.scuola.Scuola;
import it.algos.webbase.multiazienda.ELazyContainer;
import it.algos.webbase.web.component.DateRangeComponent;
import it.algos.webbase.web.entity.EM;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@SuppressWarnings("serial")
public abstract class StatisticaBase extends StatisticaGenerale {

	private static final boolean DEBUG_GUI = StatisticheModulo.DEBUG_GUI;

	protected Container container;
	protected int totInteri = 0;
	protected int totRidotti = 0;
	protected int totDisabili = 0;
	protected int totAccomp = 0;
	protected int totSpettatori = 0;
	protected int totCapienza = 0;
	protected int totScuole = 0;
	protected int totRappresentazioni=0;
	protected double totDaPagare = 0.0;
	protected double totGiaPagato = 0.0;

	private String titoloStatistica;
	protected Component componenteStatistica;
	protected Component componentePeriodo;
	protected DateRangeComponent dateComponent;
	private ArrayList<ElaboraListener> listeners = new ArrayList<ElaboraListener>();

	public StatisticaBase() {
		this("");
	}// end of constructor

	public StatisticaBase(String titoloStatistica) {
		super();
		this.titoloStatistica = titoloStatistica;
		init();
	}// end of constructor

	/**
	 * Costruisce un componente di controllo/elaborazione, che viene visualizzato dal ControlPanel <br>
	 * Costruisce il body che contiene la Table, inzialmente NON visibile <br>
	 */
	private void init() {
		creaComponentePannello();
	}// end of method

	/**
	 * Predispone un componente di controllo/elaborazione dell'intervallo di statistica <br>
	 * -visualizza una selezione dell'intervallo di statistica (di solito) <br>
	 * -visualizza un bottone di comando (di solito) <br>
	 */
	protected void creaComponentePannello() {
		HorizontalLayout layout = new HorizontalLayout();
		Component componentePeriodo = creaComponentePeriodo();
		Button buttonElabora = creaBottoneElabora();

		if (DEBUG_GUI) {
			layout.addStyleName("greenBg");
		}// end of if cycle

		layout.addComponent(componentePeriodo);
		layout.addComponent(buttonElabora);

		// allineamento dei componenti nella direzione opposta a quella del layout
		layout.setComponentAlignment(componentePeriodo, Alignment.BOTTOM_LEFT);
		layout.setComponentAlignment(buttonElabora, Alignment.BOTTOM_RIGHT);

		layout.setSpacing(true);
		layout.setVisible(true);
		componenteStatistica = layout;
	}// end of method

	/**
	 * Predispone un componente di selezione dell'intervallo di statistica (dipende dalla sottoclasse) <br>
	 */
	protected Component creaComponentePeriodo() {
		componentePeriodo = new DateRangeComponent(true);
		componentePeriodo.setVisible(true);

		return componentePeriodo;
	}// end of method

	public Table creaTable(Date data1, Date data2) {
		resetTotali();
		TableStat table = new TableStat();
		setTable(table);
		creaContainer();
		popola(data1, data2);
		table.setColumnFooter();
		table.setContainerDataSource(container);
		return table;
	}// end of method

	/**
	 * crea il container con le colonne
	 */
	protected void creaContainer() {
		addContainerProperty(Colonne.interi);
		addContainerProperty(Colonne.ridotti);
		addContainerProperty(Colonne.disabili);
		addContainerProperty(Colonne.accomp);
		addContainerProperty(Colonne.totSpettatori);
		addContainerProperty(Colonne.totPagare);
		addContainerProperty(Colonne.totPagato);
	}// end of method

	protected void addContainerProperty(Colonne colonna) {
		container.addContainerProperty(colonna.getTitolo(), colonna.getClazz(), colonna.getDefValue());
	}// end of method

	/**
	 * popola il container con i dati <br>
	 * reset iniziale, usato anche dalle sottoclassi <br>
	 */
	protected void popola(Date data1, Date data2) {
	}// end of method

	// resetta i totali
	protected void resetTotali() {
		totInteri = 0;
		totRidotti = 0;
		totDisabili = 0;
		totAccomp = 0;
		totSpettatori = 0;
		totCapienza=0;
		totScuole = 0;
		totRappresentazioni = 0;
		totDaPagare = 0.0;
		totGiaPagato = 0.0;
	}// end of method

	/**
	 * Recupera tutte le rappresentazioni nell'intervallo di date
	 */
	protected ArrayList<Rappresentazione> getRappresentazioni(Date data1, Date data2, String sortAttribute) {
		ArrayList<Rappresentazione> lista = new ArrayList<Rappresentazione>();
		String nomeAttributo = Rappresentazione_.dataRappresentazione.getName();
		Collection<Object> listaIds = null;

		EntityManager manager = EM.createEntityManager();
		ELazyContainer container = new ELazyContainer(manager, Rappresentazione.class);

		Filter filter1 = new Compare.GreaterOrEqual(nomeAttributo, data1);
		Filter filter2 = new Compare.LessOrEqual(nomeAttributo, data2);
		Filter filter = new And(filter1, filter2);
		container.addContainerFilter(filter);
		container.sort(new Object[] { sortAttribute }, new boolean[] { true });

		for (Object id : container.getItemIds()) {

			Rappresentazione rapp = (Rappresentazione)container.getEntity(id);

			lista.add(rapp);
		}
		manager.close();
		return lista;
	}// end of method
	
	/**
	 * Recupera tutte le rappresentazioni nell'intervallo di date<br>
	 * in ordine di data rappresentazione
	 */
	protected ArrayList<Rappresentazione> getRappresentazioni(Date data1, Date data2) {
		return getRappresentazioni(data1, data2, Rappresentazione_.dataRappresentazione.getName());
	}// end of method


	/**
	 * Analizza le prenotazioni per una data rappresentazione e ritorna un wrapper con totali.
	 * Esclude le prenotazioni congelate.
	 */
	protected WrapTotali analizzaPrenotazioni(Rappresentazione rapp) {
		int totInteri = 0;
		int totRidotti = 0;
		int totDisabili = 0;
		int totAccomp=0;
		BigDecimal totPagare = new BigDecimal(0);
		BigDecimal totPagato = new BigDecimal(0);
		BigDecimal importo;
		ArrayList<Scuola> scuole = new ArrayList<Scuola>();

		EntityManager manager = EM.createEntityManager();
		
		// container delle prenotazioni per la rappresentazione
//		JPAContainer prenotazioni = new EJPAContainer(Prenotazione.class, manager);
		ELazyContainer prenotazioni = new ELazyContainer(manager, Prenotazione.class);

		Filter filter;

		filter = new Compare.Equal(Prenotazione_.rappresentazione.getName(), rapp);
		prenotazioni.addContainerFilter(filter);

		filter = new Compare.Equal(Prenotazione_.congelata.getName(), false);
		prenotazioni.addContainerFilter(filter);


		// spazzola e calcola totali
		Collection ids = prenotazioni.getItemIds();
		for (Object itemId : ids) {

			Prenotazione pren = (Prenotazione)prenotazioni.getEntity(itemId);

			totInteri += pren.getNumInteri();
			totRidotti += pren.getNumRidotti();
			totDisabili += pren.getNumDisabili();
			totAccomp += pren.getNumAccomp();
			importo = pren.getImportoDaPagare();
			if (importo != null) {
				totPagare = totPagare.add(importo);
			}// end of if cycle
			importo = pren.getImportoPagato();
			if (importo != null) {
				totPagato = totPagato.add(importo);
			}// end of if cycle

			Scuola scuola = pren.getScuola();
			if (!scuole.contains(scuola)) {
				scuole.add(scuola);
			}// end of if cycle
		}// end of for cycle

		manager.close();
		
		return new WrapTotali(totInteri, totRidotti, totDisabili, totAccomp, rapp.getCapienza(), scuole.size(), totPagare, totPagato);
	}// end of method

	@SuppressWarnings("unchecked")
	protected void addRigaBase(WrapTotali wrap, Item item) {
		item.getItemProperty(Colonne.interi.getTitolo()).setValue(wrap.getTotInteri());
		item.getItemProperty(Colonne.ridotti.getTitolo()).setValue(wrap.getTotRidotti());
		item.getItemProperty(Colonne.disabili.getTitolo()).setValue(wrap.getTotDisabili());
		item.getItemProperty(Colonne.accomp.getTitolo()).setValue(wrap.getTotAccomp());
		item.getItemProperty(Colonne.totSpettatori.getTitolo()).setValue(wrap.getTotSpettatori());
		item.getItemProperty(Colonne.totPagare.getTitolo()).setValue(wrap.getTotPagareDouble());
		item.getItemProperty(Colonne.totPagato.getTitolo()).setValue(wrap.getTotPagatoDoble());
	}// end of method

	// incrementa i totali
	protected void addTotali(WrapTotali wrap) {

		totInteri += wrap.getTotInteri();
		totRidotti += wrap.getTotRidotti();
		totDisabili += wrap.getTotDisabili();
		totAccomp += wrap.getTotAccomp();
		totSpettatori += wrap.getTotSpettatori();
		totCapienza+= wrap.getTotCapienza();
		totScuole += wrap.getTotScuole();
		totRappresentazioni += wrap.getTotRappresentazioni();
		totDaPagare += wrap.getTotPagareDouble();
		totGiaPagato += wrap.getTotPagatoDoble();

	}// end of method

	/**
	 * 
	 */
	protected void elabora() {
		Date data1 = getData1();
		Date data2 = getData2();

		if (data1 != null && data2 != null) {
			creaTable(data1, data2);
		}// end of if statement
	}// end of method

	protected Statistica getStat() {
		return this;
	}// end of method

	/**
	 * Visualizza un bottone di comando <br>
	 */
	protected Button creaBottoneElabora() {
		Button button = new Button("Elabora");

		button.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				if (isValida()) {
					elabora();
					for (ElaboraListener listener : listeners) {
						listener.elabora(getStat());
					}// end of for cycle
				} else {
					Notification
							.show("Oops!\n", "Selezionare prima l'intervallo.", Notification.Type.HUMANIZED_MESSAGE);
				}// end of if/else statement
			}// end of inner method
		});// end of anonymous class

		return button;
	}// end of method

	protected Date getData1() {
		Date data = null;
		DateRangeComponent dataRange = null;

		if (componentePeriodo != null) {
			if (componentePeriodo instanceof DateRangeComponent) {
				dataRange = (DateRangeComponent) componentePeriodo;
				data = dataRange.getDate1();
			}// end of if cycle
		}// end of if cycle

		return data;
	}// end of method

	protected Date getData2() {
		Date data = null;
		DateRangeComponent dataRange = null;

		if (componentePeriodo != null) {
			if (componentePeriodo instanceof DateRangeComponent) {
				dataRange = (DateRangeComponent) componentePeriodo;
				data = dataRange.getDate2();
			}// end of if cycle
		}// end of if cycle

		return data;
	}// end of method

	/**
	 * @return il nome della statistica
	 */
	public String getName() {
		return this.titoloStatistica;
	}// end of method

	@Override
	public String toString() {
		return getName();
	}// end of method

	/**
	 * @return true se la selezione è valida (sono state scelte le date o il periodo o l'intervallo di statistica)
	 */
	public boolean isValida() {
		boolean valida = true;

		if (getData1() == null) {
			valida = false;
		}// end of if

		if (getData2() == null) {
			valida = false;
		}// end of if

		return valida;
	}// end of method

	/**
	 * @return il componente per la selezione del periodo e il bottone elabora (di solito)
	 */
	public Component getComponenteStatistica() {
		return componenteStatistica;
	}// end of method

	/**
	 * Listener notificato quando si preme il bottone elabora
	 */
	public void addElaboraListener(ElaboraListener listener) {
		listeners.add(listener);
	}// end of method

	public interface ElaboraListener {
		public void elabora(Statistica statistica);
	}// end of method

	/**
	 * Tabella specifica per effettuare gli override
	 */
	protected class TableStat extends Table {

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		DecimalFormat intFormatter = new DecimalFormat("###,###,##0");
		DecimalFormat decimalFormatter = new DecimalFormat("###,###,##0.00");

		public TableStat() {
			super();

			setFooterVisible(true);
			setColumnAlignment(Colonne.numscuole.getTitolo(), Table.Align.RIGHT);
			setColumnAlignment(Colonne.numrappresentazioni.getTitolo(), Table.Align.RIGHT);
			setColumnAlignment(Colonne.interi.getTitolo(), Table.Align.RIGHT);
			setColumnAlignment(Colonne.ridotti.getTitolo(), Table.Align.RIGHT);
			setColumnAlignment(Colonne.disabili.getTitolo(), Table.Align.RIGHT);
			setColumnAlignment(Colonne.accomp.getTitolo(), Table.Align.RIGHT);
			setColumnAlignment(Colonne.totSpettatori.getTitolo(), Table.Align.RIGHT);
			setColumnAlignment(Colonne.capienza.getTitolo(), Table.Align.RIGHT);
			setColumnAlignment(Colonne.totPagare.getTitolo(), Table.Align.RIGHT);
			setColumnAlignment(Colonne.totPagato.getTitolo(), Table.Align.RIGHT);

		}// end of constructor

		public void setColumnFooter() {

			setColumnFooter(Colonne.numscuole.getTitolo(), intFormatter.format(totScuole));
			setColumnFooter(Colonne.numrappresentazioni.getTitolo(), intFormatter.format(totRappresentazioni));
			setColumnFooter(Colonne.interi.getTitolo(), intFormatter.format(totInteri));
			setColumnFooter(Colonne.ridotti.getTitolo(), intFormatter.format(totRidotti));
			setColumnFooter(Colonne.disabili.getTitolo(),  intFormatter.format(totDisabili));
			setColumnFooter(Colonne.accomp.getTitolo(),  intFormatter.format(totAccomp));
			setColumnFooter(Colonne.totSpettatori.getTitolo(), intFormatter.format(totSpettatori));
			setColumnFooter(Colonne.capienza.getTitolo(), intFormatter.format(totCapienza));
			setColumnFooter(Colonne.totPagare.getTitolo(), decimalFormatter.format(totDaPagare));
			setColumnFooter(Colonne.totPagato.getTitolo(), decimalFormatter.format(totGiaPagato));
		}// end of method

		@Override
		protected String formatPropertyValue(Object rowId, Object colId, Property property) {
			String string = null;

			// Format for Dates
			if (property.getType() == Date.class) {
				try {
					string = this.dateFormat.format((Date) property.getValue());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// Format for Integers
			if (property.getType() == Integer.class) {
				string = intFormatter.format((Integer) property.getValue());
			}

			// Format for Doubles
			if (property.getType() == Double.class) {
				string = decimalFormatter.format((Double) property.getValue());
			}

			// none of the above
			if (string == null) {
				string = super.formatPropertyValue(rowId, colId, property);
			}

			return string;

		}// end of method

	}// end of inner class

}// end of class
