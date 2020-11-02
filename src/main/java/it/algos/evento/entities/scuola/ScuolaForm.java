package it.algos.evento.entities.scuola;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.ui.*;
import it.algos.evento.entities.comune.Comune;
import it.algos.evento.entities.ordinescuola.OrdineScuola;
import it.algos.evento.entities.prenotazione.PrenotazioneBaseTable;
import it.algos.evento.entities.prenotazione.PrenotazioneModulo;
import it.algos.evento.entities.prenotazione.Prenotazione_;
import it.algos.evento.entities.rappresentazione.Rappresentazione;
import it.algos.evento.entities.rappresentazione.RappresentazioneModulo;
import it.algos.evento.entities.rappresentazione.RappresentazioneTable;
import it.algos.evento.entities.rappresentazione.Rappresentazione_;
import it.algos.webbase.multiazienda.ELazyContainer;
import it.algos.webbase.multiazienda.ERelatedComboField;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.entity.BaseEntity_;
import it.algos.webbase.web.entity.SortProperties;
import it.algos.webbase.web.field.ArrayComboField;
import it.algos.webbase.web.field.EmailField;
import it.algos.webbase.web.field.TextField;
import it.algos.webbase.web.form.AFormLayout;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.ATable;

import javax.persistence.EntityManager;

@SuppressWarnings("serial")
public class ScuolaForm extends ModuleForm {


	// Modulo Prenotazioni interno per la gestione
	// della lista prenotazioni interna alla scheda
	private PrenotazioneModulo modPren;


	public ScuolaForm( Item item, ModulePop modulo) {
		super(item, modulo);
	}

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
		ArrayComboField arrayCombo;

		field = new TextField("Sigla");
		field.setWidth("120px");
		addField(Scuola_.sigla, field);

		field = new TextField("Nome");
		field.setWidth("300px");
		addField(Scuola_.nome, field);

//		arrayCombo = new ArrayComboField(OrdineScuolaEnumOld.values(), "Ordine");
//		Converter converter = new OrdineScuolaConverterOld();
//		arrayCombo.setConverter(converter);
//		addField(Scuola_.ordine, arrayCombo);

		field = new ERelatedComboField(OrdineScuola.class, "Ordine");
		field.setWidth("180px");
		addField(Scuola_.ordine, field);

		field = new TextField("Tipo");
		field.setWidth("200px");
		addField(Scuola_.tipo, field);

		field = new TextField("Indirizzo");
		field.setWidth("200px");
		addField(Scuola_.indirizzo, field);
		
		field = new TextField("Cap");
		field.setWidth("60px");
		addField(Scuola_.cap, field);

		field = new ERelatedComboField(Comune.class, "Comune");
		field.setWidth("200px");
		addField(Scuola_.comune, field);

		field = new TextField("Telefono");
		addField(Scuola_.telefono, field);

		field = new TextField("Fax");
		addField(Scuola_.fax, field);

		field = new EmailField("Email");
		addField(Scuola_.email, field);

		field = new TextField("Note");
		field.setWidth("300px");
		addField(Scuola_.note, field);

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

		FormLayout layout1 = new AFormLayout();
		layout1.addComponent(getField(Scuola_.sigla));
		layout1.addComponent(getField(Scuola_.nome));
		layout1.addComponent(getField(Scuola_.ordine));
		layout1.addComponent(getField(Scuola_.tipo));
		layout1.addComponent(getField(Scuola_.email));
		layout1.addComponent(getField(Scuola_.note));

		FormLayout layout2 = new AFormLayout();
		layout2.addComponent(getField(Scuola_.indirizzo));
		layout2.addComponent(getField(Scuola_.cap));
		layout2.addComponent(getField(Scuola_.comune));
		layout2.addComponent(getField(Scuola_.telefono));
		layout2.addComponent(getField(Scuola_.fax));

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
			return ScuolaForm.this.getUI();
		}

		/**
		 * Questo modulo lo stesso EntityManager della
		 * scheda che lo contiene
		 */
		@Override
		public EntityManager getEntityManager() {
			return ScuolaForm.this.getEntityManager();
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
			Scuola scuola = getScuola();
			cont.addContainerFilter(new Compare.Equal(Prenotazione_.scuola.getName(), scuola));
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
					Prenotazione_.insegnante,
					Prenotazione_.numTotali,
					COL_STATUS,
					COL_PAGAM,
			};
		}


	}

	/**
	 * Ritorna la Scuola gestita da questa scheda
	 */
	private Scuola getScuola() {
		Scuola scuola = null;
		BaseEntity entity = getEntity();
		if (entity != null) {
			scuola = (Scuola) entity;
		}
		return scuola;
	}



}
