package it.algos.evento.statistiche;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import it.algos.evento.entities.rappresentazione.Rappresentazione;
import it.algos.webbase.web.lib.LibDate;
import it.algos.webbase.web.lib.MeseEnum;

import java.util.ArrayList;
import java.util.Date;

@SuppressWarnings("serial")
public class StatisticaPerMese extends StatisticaBase {

	private static final boolean DEBUG_GUI = StatisticheModulo.DEBUG_GUI;

	private ComboBox comboMeseInizio;
	private ComboBox comboAnnoInizio;
	private ComboBox comboMeseFine;
	private ComboBox comboAnnoFine;

	public StatisticaPerMese() {
		super("Statistica per mese");
	}// end of constructor

	/**
	 * Predispone un componente di selezione dell'intervallo di statistica (dipende dalla sottoclasse) <br>
	 */
	@Override
	protected Component creaComponentePeriodo() {
		HorizontalLayout layout = new HorizontalLayout();

		if (DEBUG_GUI) {
			layout.addStyleName("yellowBg");
		}// end of if statement

		comboMeseInizio = getComboMesi("Dal mese...");
		comboAnnoInizio = getComboAnni();
		comboMeseFine = getComboMesi("Al mese...");
		comboAnnoFine = getComboAnni();

		layout.addComponent(comboMeseInizio);
		layout.addComponent(comboAnnoInizio);
		layout.addComponent(comboMeseFine);
		layout.addComponent(comboAnnoFine);

		// Valori inziali dei combobox, in funzione del perido dell'anno scolastico
		setInizialePeriodo();

		layout.setSpacing(true); // spazio tra i componenti
		componentePeriodo = layout;

		return componentePeriodo;
	}// end of method

	/**
	 * Valori inziali dei combobox, in funzione del perido dell'anno scolastico <br>
	 */
	private void setInizialePeriodo() {
		comboMeseInizio.setValue("settembre");
		comboAnnoInizio.setValue(2014);
		comboMeseFine.setValue("febbraio");
		comboAnnoFine.setValue(2015);
	}// end of method

	/**
	 * crea il container con le colonne
	 */
	@Override
	protected void creaContainer() {
		container = new IndexedContainer();
		addContainerProperty(Colonne.mese);
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
	 * popola il container con i dati <br>
	 * crea una lista di mesi compresi tra il mese iniziale e quello finale <br>
	 * per ogni mese recupera le rappresentazioni <br>
	 * per ogni rappresentazione recupera le prenotazioni e le accumula in un wrapper <br>
	 * somma i wrapper di tutte le rappresentazioni del mese <br>
	 * aggiunge la riga del mese <br>
	 */
	@Override
	protected void popola(Date data1, Date data2) {
		super.popola(data1, data2);

		ArrayList<WrapMese> listaMesi = getListaMesi();

		WrapTotali wrapTotali;
		for (WrapMese wrapMese : listaMesi) {
			wrapTotali = analizzaMese(wrapMese);
			addRiga(wrapMese.getTitolo(), wrapTotali);
		}// end of for cycle

	}// end of method

	/**
	 * crea una lista di mesi compresi tra il mese iniziale e quello finale <br>
	 * recupera i valori dei combobox <br>
	 */
	private ArrayList<WrapMese> getListaMesi() {
		ArrayList<WrapMese> listaMesi = new ArrayList<WrapMese>();
		int numMese = 0;
		String meseTmp;
		int annoTmp = 0;
		int mesiRichiesti;

		String meseIniziale = (String) comboMeseInizio.getValue();
		int annoIniziale = (int) comboAnnoInizio.getValue();
		String meseFinale = (String) comboMeseFine.getValue();
		int annoFinale = (int) comboAnnoFine.getValue();

		mesiRichiesti = deltaMesi(meseIniziale, annoIniziale, meseFinale, annoFinale);

		meseTmp = meseIniziale;
		annoTmp = annoIniziale;

		for (int i = 1; i <= mesiRichiesti; i++) {
			listaMesi.add(new WrapMese(meseTmp, annoTmp));
			numMese = MeseEnum.getOrd(meseTmp);
			numMese++;
			meseTmp = MeseEnum.getLong(numMese);
			if (numMese > 12) {
				annoTmp++;
				meseTmp = MeseEnum.getLong(1);
			}// fine del blocco if
		}// end of for cycle

		return listaMesi;
	}// end of method

	/**
	 * Analizza le prenotazioni per un dato mese e ritorna un wrapper con totali
	 */
	private WrapTotali analizzaMese(WrapMese wrapMese) {
		WrapTotali wrapTotali = new WrapTotali();
		WrapTotali wrapTmp = null;
		Date inizio = wrapMese.getDataIniziole();
		Date fine = wrapMese.getDateFinale();

		// lista delle rappresentazioni in ordine di data
		ArrayList<Rappresentazione> lista = super.getRappresentazioni(inizio, fine);

		for (Rappresentazione rapp : lista) {
			wrapTmp = super.analizzaPrenotazioni(rapp);
            int tot = wrapTmp.getTotSpettatori();

            wrapTotali.addWrap(wrapTmp);

		}// end of for cycle

		return wrapTotali;
	}// end of method

	@SuppressWarnings("unchecked")
	private void addRiga(String mese, WrapTotali wrap) {
		Item item = container.getItem(container.addItem());
		item.getItemProperty(Colonne.mese.getTitolo()).setValue(mese);
		item.getItemProperty(Colonne.capienza.getTitolo()).setValue(wrap.getTotCapienza());


		// aggiunge le colonne standard
		super.addRigaBase(wrap, item);

		// incrementa i totali
		super.addTotali(wrap);
	}// end of method

	/**
	 * Crea un combobox di mesi <br>
	 * Di norma partendo da ottobre <br>
	 * Di norma 12 mesi <br>
	 */
	private ComboBox getComboMesi(String titolo) {
		return getComboMesi(titolo, "ottobre", 12);
	}// end of method

	/**
	 * Crea un combobox di mesi <br>
	 * Di norma partendo da ottobre <br>
	 * Di norma 12 mesi <br>
	 */
	private ComboBox getComboMesi(String titolo, String meseIniziale, int numMesi) {
		ComboBox combo = null;
		String width = "105px";

		combo = new ComboBox(titolo, MeseEnum.getAllLongList());
		combo.setWidth(width);
		combo.setInvalidAllowed(false);
		combo.setNullSelectionAllowed(false);
		combo.setTextInputAllowed(false);
		combo.setVisible(true);
		combo.setValue(meseIniziale);

		return combo;
	}// end of method

	/**
	 * Crea un combobox di anni <br>
	 * Di norma partendo dal 2014 <br>
	 * Di norma 10 anni indietro dalla data attuale <br>
	 */
	private ComboBox getComboAnni() {
		return getComboAnni("dell'anno...");
	}// end of method

	/**
	 * Crea un combobox di anni <br>
	 * Di norma partendo dal 2014 <br>
	 * Di norma 10 anni indietro dalla data attuale <br>
	 */
	private ComboBox getComboAnni(String titolo) {
		return getComboAnni(titolo, 2003, 20);
	}// end of method

	/**
	 * Crea un combobox di anni <br>
	 * Di norma partendo dal 2014 <br>
	 * Di norma 10 anni indietro dalla data attuale <br>
	 */
	private ComboBox getComboAnni(String titolo, int annoIniziale, int numAnni) {
		ComboBox combo = null;
		ArrayList<Integer> anni = new ArrayList<Integer>();
		String width = "90px";

		for (int i = annoIniziale; i < (annoIniziale + numAnni); i++) {
			anni.add(i);
		}// end of for cycle

		// componente anni
		combo = new ComboBox(titolo, anni);
		combo.setWidth(width);
		combo.setInvalidAllowed(false);
		combo.setNullSelectionAllowed(false);
		combo.setTextInputAllowed(false);
		combo.setVisible(true);
		combo.setValue(annoIniziale);

		return combo;
	}// end of method

	protected Date getData1() {
		Date data = null;
		String mese = null;
		int anno = 0;

		if (comboMeseInizio != null) {
			mese = (String) comboMeseInizio.getValue();
		}// end of if statement

		if (comboAnnoInizio != null) {
			anno = (int) comboAnnoInizio.getValue();
		}// end of if statement

		if (mese != null && anno > 0) {
			data = LibDate.fromInizioMeseAnno(mese, anno);
		}// end of if statement

		return data;
	}// end of method

	protected Date getData2() {
		Date data = null;
		String mese = null;
		int anno = 0;

		if (comboMeseFine != null) {
			mese = (String) comboMeseFine.getValue();
		}// end of if statement

		if (comboAnnoFine != null) {
			anno = (int) comboAnnoFine.getValue();
		}// end of if statement

		if (mese != null && anno > 0) {
			data = LibDate.fromFineMeseAnno(mese, anno);
		}// end of if statement

		return data;
	}// end of method

	/**
	 * Numero del mese prgressivo dalla nascita di Cristo <br>
	 */
	private int mesiCristo(String mese, int anno) {
		int mesiCristo = 0;
		int numMese = MeseEnum.getOrd(mese);

		return anno * 12 + numMese;
	}// end of method

	/**
	 * Numero del mese prgressivo dalla nascita di Cristo <br>
	 */
	private int deltaMesi(String meseIniziale, int annoIniziale, String meseFinale, int annoFinale) {
		int mesi = 0;
		int meseInizialeCristo = mesiCristo(meseIniziale, annoIniziale);
		int meseFinaleCristo = mesiCristo(meseFinale, annoFinale);

		mesi = meseFinaleCristo - meseInizialeCristo;
		mesi++;
		return mesi;
	}// end of method

	/**
	 * Raggruppa mese, anno, data iniziale e data finale (di ogni mese) <br>
	 */
	protected class WrapMese {

		private String mese;
		private int anno;

		public WrapMese(String mese, int anno) {
			this.mese = mese;
			this.anno = anno;
		}// end of constructor

		public String getTitolo() {
			return mese + " - " + anno;
		}// end of method

		public Date getDataIniziole() {
			return LibDate.fromInizioMeseAnno(mese, anno);
		}// end of method

		public Date getDateFinale() {
			return LibDate.fromFineMeseAnno(mese, anno);
		}// end of method
	}// end of inner class

}// end of class
