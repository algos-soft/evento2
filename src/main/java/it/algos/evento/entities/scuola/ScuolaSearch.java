package it.algos.evento.entities.scuola;

import com.vaadin.data.Container.Filter;
import com.vaadin.ui.FormLayout;
import it.algos.evento.entities.comune.Comune;
import it.algos.evento.entities.ordinescuola.OrdineScuola;
import it.algos.webbase.multiazienda.ERelatedComboField;
import it.algos.webbase.multiazienda.ESearchManager;
import it.algos.webbase.web.field.RelatedComboField;
import it.algos.webbase.web.field.TextField;
import it.algos.webbase.web.module.ModulePop;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class ScuolaSearch extends ESearchManager {

	private TextField fSigla;
	private TextField fNome;
	private RelatedComboField fOrdine;
	private RelatedComboField fComune;

	public ScuolaSearch(ModulePop module) {
		super(module);
	}// end of constructor

	// come default spazzola tutti i campi della Entity
	// aggiunge i campi al layout
	// può essere sovrascritto nelle sottoclassi specifiche
	protected void createFields(FormLayout layout) {
		fSigla = new TextField("Sigla");
		layout.addComponent(fSigla);

		fNome = new TextField("Nome");
		layout.addComponent(fNome);

		fOrdine = new ERelatedComboField(OrdineScuola.class, "Ordine");
		layout.addComponent(fOrdine);

		fComune = new ERelatedComboField(Comune.class, "Comune");
		layout.addComponent(fComune);
	}// end of method

	@Override
	public ArrayList<Filter> createFilters() {
		ArrayList<Filter> filters = new ArrayList<Filter>();
		filters.add(createStringFilter(fSigla, Scuola_.sigla, SearchType.STARTS_WITH));
		filters.add(createStringFilter(fNome, Scuola_.nome, SearchType.CONTAINS));
		filters.add(createBeanFilter(fOrdine, Scuola_.ordine));
		filters.add(createBeanFilter(fComune, Scuola_.comune));
		return filters;
	}// end of method

}// end of class
