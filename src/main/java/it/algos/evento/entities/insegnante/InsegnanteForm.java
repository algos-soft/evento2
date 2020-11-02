package it.algos.evento.entities.insegnante;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.ui.*;
import it.algos.evento.entities.ordinescuola.OrdineScuola;
import it.algos.evento.entities.prenotazione.PrenotazioneBaseTable;
import it.algos.evento.entities.prenotazione.PrenotazioneModulo;
import it.algos.evento.entities.prenotazione.Prenotazione_;
import it.algos.webbase.multiazienda.ERelatedComboField;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.field.*;
import it.algos.webbase.web.field.TextArea;
import it.algos.webbase.web.field.TextField;
import it.algos.webbase.web.form.AFormLayout;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.ATable;

import javax.persistence.EntityManager;

@SuppressWarnings("serial")
public class InsegnanteForm extends ModuleForm {

	private CheckBoxField fieldPrivato;
	private ERelatedComboField fieldOrdineScuola;
	private TextField fieldMaterie;

	// Modulo Prenotazioni interno per la gestione
	// della lista prenotazioni interna alla scheda
	private PrenotazioneModulo modPren;

	public InsegnanteForm(Item item, ModulePop modulo) {
		super(item, modulo);
	}// end of constructor

	@Override
	protected void init() {

		// crea un nuovo modulo prenotazioni per mostrare la pagina prenotazioni
		// (ma se questo modulo è null la scheda è creata da un combo e in questo caso non lo crea)
		if(getModule()!=null){
			modPren = new PrenotazioneModuloInterno();
		}
		super.init();
	}

	@Override
	public void createFields() {
		@SuppressWarnings("rawtypes")
		Field field;

		field = new TextField("Titolo");
		field.setWidth("80px");
		field.focus();
		addField(Insegnante_.titolo, field);

		field = new TextField("Cognome");
		field.setWidth("200px");
		field.setRequired(true);
		field.setRequiredError("Il cognome è obbligatorio");
		addField(Insegnante_.cognome, field);

		field = new TextField("Nome");
		field.setWidth("200px");
		field.setRequired(true);
		field.setRequiredError("Il nome è obbligatorio");
		addField(Insegnante_.nome, field);

		fieldOrdineScuola = new ERelatedComboField(OrdineScuola.class, "Ordine scuola");
		fieldOrdineScuola.setWidth("180px");
		addField(Insegnante_.ordineScuola, fieldOrdineScuola);

		fieldMaterie = new TextField("Materie");
		fieldMaterie.setWidth("200px");
		addField(Insegnante_.materie, fieldMaterie);

		field = new EmailField("E-mail");
		addField(Insegnante_.email, field);

		field = new TextField("Telefono");
		field.setWidth("300px");
		addField(Insegnante_.telefono, field);

		field = new TextField("Indirizzo");
		field.setWidth("300px");
		addField(Insegnante_.indirizzo1, field);

		field = new TextField("Località");
		field.setWidth("300px");
		addField(Insegnante_.indirizzo2, field);

		field = new TextArea("Note");
		field.setHeight("10em");
		field.setWidth("300px");
		addField(Insegnante_.note, field);

		fieldPrivato = new CheckBoxField("Privato");
		addField(Insegnante_.privato, fieldPrivato);
		fieldPrivato.addValueChangeListener(event -> {
			onPrivatoChange();
		});



	}



	/**
	 * Crea il componente.
	 * Se c'è il modulo, crea anche la pagina per la lista prenotazioni.
	 * Se manca il modulo (scheda creata da combo) crea solo la pagina Generale.
	 */
	protected Component createComponent() {

		Component comp;

		if(getModule()!=null){
			TabSheet tabsheet = new TabSheet();
			tabsheet.setWidth("60em");

			Component tab;

			tab = creaTabGenerale();
			tabsheet.addTab(tab, "Generale");

			tab = creaTabPrenotazioni();
			//tab.setHeight("36em");
			tabsheet.addTab(tab, "Prenotazioni");
			comp=tabsheet;

		}else{
			comp=creaTabGenerale();
		}

		return comp;

	}

	private Component creaTabGenerale() {

		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		hl.setMargin(true);

		AFormLayout layout1 = new AFormLayout();
		layout1.addComponent(getField(Insegnante_.titolo));
		layout1.addComponent(getField(Insegnante_.cognome));
		layout1.addComponent(getField(Insegnante_.nome));
		layout1.addComponent(getField(Insegnante_.privato));
		layout1.addComponent(fieldOrdineScuola);
		layout1.addComponent(fieldMaterie);

		AFormLayout layout2 = new AFormLayout();
		layout2.addComponent(getField(Insegnante_.email));
		layout2.addComponent(getField(Insegnante_.telefono));
		layout2.addComponent(getField(Insegnante_.indirizzo1));
		layout2.addComponent(getField(Insegnante_.indirizzo2));
		layout2.addComponent(getField(Insegnante_.note));

		// dopo aver creato i componenti simula un change di privato per sincronizzare la UI
		onPrivatoChange();

		hl.addComponent(layout1);
		hl.addComponent(layout2);

		return hl;
	}


	private Component creaTabPrenotazioni() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);

		ATable tablePrenotazioni = modPren.getTable();

		//layout.addComponent(new Label("Elenco delle prenotazioni"));
		tablePrenotazioni.setWidth("100%");
		layout.addComponent(tablePrenotazioni);
		layout.setExpandRatio(tablePrenotazioni, 1);

		return layout;
	}


	/**
	 * Invocato quando il valore del flag privato cambia.
	 * Sincronizza i campi dipendenti.
	 */
	private void onPrivatoChange(){
		boolean privato=fieldPrivato.getValue();
		fieldOrdineScuola.setVisible(!privato);
		fieldOrdineScuola.removeAllValidators();
		fieldMaterie.setVisible(!privato);
		if(!privato){
			fieldOrdineScuola.addValidator(new NullValidator("L'ordine della scuola è obbligatorio", false));
		}
	}


	/**
	 * Modulo Prenotazioni dedicato alla gestione Prenotazioni
	 * all'interno della scheda Scuola.
	 * Usato per la gestione della lista e della scheda interne.
	 */
	private class PrenotazioneModuloInterno extends PrenotazioneModulo{

		/**
		 * Usa una tabella specifica
		 */
		@Override
		public ATable createTable() {
			return new TablePrenotazioniInterna(this);
		}

		/**
		 * Questo modulo non è inserito graficamente in nessuna UI
		 * perciò ritorna la UI della scheda che lo contiene.
		 * La UI è richiesta quando deve mostrare la scheda.
		 */
		@Override
		public UI getUI() {
			return InsegnanteForm.this.getUI();
		}

		/**
		 * Questo modulo lo stesso EntityManager della
		 * scheda che lo contiene
		 */
		@Override
		public EntityManager getEntityManager() {
			return InsegnanteForm.this.getEntityManager();
		}
	}

	/**
	 * Tabella Prenotazioni del modulo Prenotazioni interno
	 */
	private class TablePrenotazioniInterna extends PrenotazioneBaseTable {

		public TablePrenotazioniInterna(PrenotazioneModulo modulo) {
			super(modulo);
		}

		/**
		 * Filtra il container sulla prenotazione corrente
		 */
		@Override
		public Container createContainer() {
			Filterable cont = (Filterable)super.createContainer();
			cont.addContainerFilter(new Compare.Equal(Prenotazione_.insegnante.getName(), getInsegnante()));
			return cont;
		}

		/**
		 * Custom sort order
		 */
		@Override
		protected void sortContainer() {
			Container cont = getContainerDataSource();
			if (cont instanceof com.vaadin.data.Container.Sortable) {
				com.vaadin.data.Container.Sortable csortable = (com.vaadin.data.Container.Sortable) cont;
				csortable.sort(new String[]{Prenotazione_.dataPrenotazione.getName()}, new boolean[]{false});
			}
		}

		/**
		 * Mostra solo alcune colonne
		 */
		protected Object[] getDisplayColumns() {
			return new Object[]{
					Prenotazione_.numPrenotazione,
					Prenotazione_.dataPrenotazione,
					Prenotazione_.rappresentazione,
					Prenotazione_.scuola,
					Prenotazione_.numTotali,
					COL_STATUS,
					COL_PAGAM,
			};
		}


	}

	/**
	 * Ritorna il referente gestito da questa scheda
	 */
	private Insegnante getInsegnante() {
		Insegnante ref = null;
		BaseEntity entity = getEntity();
		if (entity != null) {
			ref = (Insegnante) entity;
		}
		return ref;
	}


}
