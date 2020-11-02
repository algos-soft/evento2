package it.algos.evento.statistiche;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

import java.util.ArrayList;

/**
 * Pannello dei comandi per il modulo Statistiche
 */
@SuppressWarnings("serial")
public class ControlPanel extends HorizontalLayout {

	private static final boolean DEBUG_GUI = StatisticheModulo.DEBUG_GUI;

	private ComboBox comboStatistiche;
	private HorizontalLayout placeholderStatistica = new HorizontalLayout();
	private ArrayList<Statistica> statistiche = new ArrayList<Statistica>();
	private ArrayList<SelezioneStatisticaListener> listeners = new ArrayList<SelezioneStatisticaListener>();

	public ControlPanel() {
		super();

		if (DEBUG_GUI) {
			this.addStyleName("yellowBg");
		}// end of if statement

		init();
	}// end of method

	/**
	 * Layout orizzontale <br>
	 * Costruisce e visualizza a sinistra un ComboBox per la selezione delle statistiche <br>
	 * Predispone a destra un placeholder per il componente di controllo/elaborazione, gestito dalla statistica <br>
	 * -visualizza una selezione dell'intervallo di statistica (di solito) <br>
	 * -visualizza un bottone di elaborazione (di solito) <br>
	 */
	private void init() {
		creaComboStatistiche();

		addComponent(comboStatistiche);
		addComponent(placeholderStatistica);

		// allineamento dei componenti nella direzione opposta a quella del layout
		setComponentAlignment(comboStatistiche, Alignment.BOTTOM_CENTER);
		setComponentAlignment(placeholderStatistica, Alignment.BOTTOM_CENTER);

		setSpacing(true); // spazio tra i componenti
		setMargin(true); // margine interno
	}// end of method

	/**
	 * Visualizza un ComboBox con le statistiche <br>
	 */
	protected void creaComboStatistiche() {
		String width = "200px"; // TODO - rendere dinamico

		// aggiunge le statistiche disponibili
		statistiche.add(new StatisticaPerEvento());
		statistiche.add(new StatisticaPerRappresentazione());
		statistiche.add(new StatisticaPerScuola());
		statistiche.add(new StatisticaPerMese());
		statistiche.add(new StatisticaPerInsegnante());

		// popup scelta tipo di statistica
		comboStatistiche = new ComboBox("Tipo di statistica", statistiche);
		comboStatistiche.setWidth(width);
		comboStatistiche.setTextInputAllowed(false);
		comboStatistiche.setImmediate(true);

		comboStatistiche.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
				selezioneStatisticheModificata();
			}// end of inner method
		});// end of anonymous class

	}// end of method

	/**
	 * Evento generato dalla modifica del ComboBox per la selezione delle statistiche <br>
	 * 
	 * Recupera dalla statistica selezionato il componente di controllo e lo inserisce nel placeholder <br>
	 * Informa (tramite listener) il Modulo, che inserisce la statistica selezionata nel suo body <br>
	 */
	protected void selezioneStatisticheModificata() {
		setComponenteStatistica();
		setStatisticaModulo();
	}// end of method

	/**
	 * Recupera dalla statistica selezionato il componente di controllo e lo inserisce nel placeholder <br>
	 */
	protected void setComponenteStatistica() {
		Component componenteStatistica = null;
		Statistica statistica = getStatistica();

		if (statistica != null) {
			componenteStatistica = statistica.getComponenteStatistica();
			setComponenteStatistica(componenteStatistica);
		}// end of if statement
	}// end of method

	/**
	 * Informa (tramite listener) il Modulo, che inserisce la statistica selezionata nel suo body <br>
	 */
	protected void setStatisticaModulo() {
		for (SelezioneStatisticaListener listener : listeners) {
			listener.modificata(getStatistica());
		}// end of for cycle
	}// end of method

	/**
	 * Inserisce il componente di selezione/controllo della statistica nel placeholder
	 */
	public void setComponenteStatistica(Component comp) {
		placeholderStatistica.removeAllComponents();
		if (comp != null) {
			placeholderStatistica.addComponent(comp);
		}// end of if statement
	}// end of method

	/**
	 * Recupera la statistica
	 */
	public Statistica getStatistica() {
		Statistica stat = null;
		Object combo = comboStatistiche.getValue();
		
		if ((combo != null) && (combo instanceof Statistica)) {
			stat = (Statistica) combo;
		}// end of if statement

		return stat;
	}// end of method


	public ComboBox getComboStatistiche() {
		return comboStatistiche;
	}// end of method

	public void setComboStatistiche(ComboBox comboStatistiche) {
		this.comboStatistiche = comboStatistiche;
	}// end of method

	/**
	 * Listener notificato quando si modifica il popup elenco-statistiche
	 */
	public void addSelezioneStatisticaListener(SelezioneStatisticaListener listener) {
		listeners.add(listener);
	}// end of method

	public interface SelezioneStatisticaListener {
		public void modificata(Statistica statistica);
	}// end of method

}// end of class
