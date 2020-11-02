package it.algos.evento.statistiche;

import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import it.algos.evento.statistiche.ControlPanel.SelezioneStatisticaListener;
import it.algos.evento.statistiche.StatToolbar.TableToolbarListener;
import it.algos.evento.statistiche.StatisticaBase.ElaboraListener;
import it.algos.webbase.web.module.Module;
import it.algos.webbase.web.module.ModulePop;

@SuppressWarnings("serial")
public class StatisticheModulo extends Module implements SelezioneStatisticaListener, ElaboraListener {

	public static final boolean DEBUG_GUI = false;

	private ControlPanel controlPanel;
	private Placeholder placeholderStatistica;
	private StatToolbar toolbar;

	public StatisticheModulo() {
		super();
		init();
	}// end of constructor


	/**
	 * Visualizza un ControlPanel in alto <br>
	 * Visualizza un componente di tipo Placeholder al centro <br>
	 * Visualizza una Toolbar in basso
	 */
	protected void init() {
		VerticalLayout main = new VerticalLayout();
		main.setWidth("100%");
		main.setHeight("100%");
		if (DEBUG_GUI) {
			main.addStyleName("redBg");
		}// end of if cycle


		creaControlPanel();
		
		// crea il placeholder che conterrà la statistica
		placeholderStatistica = new Placeholder();
		placeholderStatistica.setWidth("100%");
		placeholderStatistica.setHeight("100%");
		
		creaToolbar();

		main.addComponent(controlPanel);
		main.addComponent(placeholderStatistica);
		main.addComponent(toolbar);

		main.setExpandRatio(controlPanel, 0f);
		main.setExpandRatio(placeholderStatistica, 1f);
		main.setExpandRatio(toolbar, 0f);
		
		setCompositionRoot(main);

	}// end of method

	/**
	 * Crea e visualizza un pannello comandi <br>
	 * Aggiungo un listener per essere informato ad ogni cambio del popup di selezione delle statistiche <br>
	 */
	protected void creaControlPanel() {
		controlPanel = new ControlPanel();
		controlPanel.addSelezioneStatisticaListener(this);
	}// end of method


	/**
	 * Crea e visualizza una Toolbar
	 */
	protected void creaToolbar() {
		toolbar = new StatToolbar();
		toolbar.setWidth("100%");

		toolbar.addToolbarListener(new TableToolbarListener() {
			@Override
			public void export_() {
				exportStatistica();
			}// end of method
		});// end of anonymous class

	}// end of method

	/**
	 * Esporta la statistica correntemente visualizzata
	 */
	private void exportStatistica() {
		Table table = null;
		Statistica stat = getStatistica();

		if (stat != null) {
			table = stat.getTable();
		}// end of if statement

		if (table != null) {
			final ExcelExport export;
			export = new ExcelExport(table);
			export.setReportTitle(stat.getName());
			export.setExportFileName(stat.getName() + ".xls");
			export.export();
		} else {
			Notification.show("Oops!\n", "Devi elaborare una statistica, prima.", Notification.Type.HUMANIZED_MESSAGE);
		}// end of if/else cycle
	}// end of method

	public Statistica getStatistica() {
		Statistica stat = null;

		if (placeholderStatistica != null) {
			stat = placeholderStatistica.getStatistica();
		}// end of if statement

		return stat;
	}// end of method

	/**
	 * Arriva un evento di modifica del popup di selezione delle statistiche <br>
	 * Inserisce la statistica selezionata nel placeholderStatistica <br>
	 * Aggiungo alla statistica un listener per essere informato del click sul bottone Elabora delle statistiche <br>
	 */
	@Override
	public void modificata(Statistica statistica) {
		placeholderStatistica.setStatistica(statistica);

		if (statistica != null) {
			statistica.addElaboraListener(this);
		}// end of if statement
	}// end of method

	@Override
	public void elabora(Statistica statistica) {
		placeholderStatistica.setStatistica(statistica);
	}// end of method

	/**
	 * Placeholder contenente una Statistica
	 */
	class Placeholder extends CustomComponent {
		// private static final boolean DEBUG_GUI = false;

		private Statistica statistica;

		public Placeholder() {
			super();
			if (DEBUG_GUI) {
				this.addStyleName("pinkBg");
			}// end of if cycle
		}// end of constructor

		/**
		 * Inserisce una statistica nel placeholder <br>
		 * Visualizza la table <br>
		 */
		public void setStatistica(Statistica statistica) {
			Table table = null;
			this.statistica = statistica;
			setCompositionRoot(null);

			if (statistica != null) {
				table = statistica.getTable();
			}// end of if statement

			if (table != null) {
				table.setWidth("100%");
				table.setHeight("100%");
				setCompositionRoot(table);
			}// end of if cycle
		}// end of method

		public Statistica getStatistica() {
			return statistica;
		}// end of method
	}// end of inner class

}// end of class
