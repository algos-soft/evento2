package it.algos.evento.entities.insegnante;

import com.vaadin.data.Container.Filter;
import com.vaadin.ui.FormLayout;
import it.algos.evento.entities.ordinescuola.OrdineScuola;
import it.algos.evento.entities.scuola.Scuola;
import it.algos.webbase.multiazienda.ERelatedComboField;
import it.algos.webbase.multiazienda.ESearchManager;
import it.algos.webbase.web.component.YesNoCheckboxComponent;
import it.algos.webbase.web.field.CheckBoxField;
import it.algos.webbase.web.field.RelatedComboField;
import it.algos.webbase.web.field.TextField;
import it.algos.webbase.web.module.ModulePop;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class InsegnanteSearch extends ESearchManager {

	private TextField fCognome;
	private TextField fEmail;
	private ERelatedComboField fOrdineScuola;
	private TextField fMaterie;
	private TextField fIndirizzo;
	private TextField fLocalita;
	private TextField fNote;
	private YesNoCheckboxComponent fPrivato;


	public InsegnanteSearch(ModulePop module) {
		super(module);
	}// end of constructor

	protected void createFields(FormLayout layout) {
//		super.createFields(layout);



		fCognome = new TextField("Cognome");
		fEmail = new TextField("E-mail");
		fOrdineScuola = new ERelatedComboField(OrdineScuola.class, "Ordine Scuola");
		fMaterie = new TextField("Materie");
		fIndirizzo = new TextField("Indirizzo");
		fLocalita = new TextField("Località");
		fNote = new TextField("Note");
		fPrivato = new YesNoCheckboxComponent("Privato");

		layout.addComponent(fCognome);
		layout.addComponent(fEmail);
		layout.addComponent(fOrdineScuola);
		layout.addComponent(fMaterie);
		layout.addComponent(fIndirizzo);
		layout.addComponent(fLocalita);
		layout.addComponent(fNote);
		layout.addComponent(fPrivato);

	}// end of method

	public ArrayList<Filter> createFilters() {

		ArrayList<Filter> filters = new ArrayList<>();
		filters.add(createStringFilter(fCognome, Insegnante_.cognome));
		filters.add(createStringFilter(fEmail, Insegnante_.email, SearchType.CONTAINS));
		filters.add(createBeanFilter(fOrdineScuola, Insegnante_.ordineScuola));

		filters.add(createStringFilter(fMaterie, Insegnante_.materie, SearchType.CONTAINS));
		filters.add(createStringFilter(fIndirizzo, Insegnante_.indirizzo1, SearchType.CONTAINS));
		filters.add(createStringFilter(fLocalita, Insegnante_.indirizzo2, SearchType.CONTAINS));
		filters.add(createStringFilter(fNote, Insegnante_.note, SearchType.CONTAINS));
		CheckBoxField checkField = new CheckBoxField();
		checkField.setValue(fPrivato.getValue());
		filters.add(createBoolFilter(checkField, Insegnante_.privato));
		return filters;
	}// end of method

}// end of class
