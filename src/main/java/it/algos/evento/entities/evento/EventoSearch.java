package it.algos.evento.entities.evento;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.ui.FormLayout;
import it.algos.evento.entities.progetto.Progetto;
import it.algos.evento.entities.stagione.Stagione;
import it.algos.evento.entities.stagione.Stagione_;
import it.algos.evento.search.StagioneSearchManager;
import it.algos.webbase.multiazienda.ERelatedComboField;
import it.algos.webbase.web.field.RelatedComboField;
import it.algos.webbase.web.field.TextField;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class EventoSearch extends StagioneSearchManager {


	private TextField fSigla;
	private TextField fTitolo;
	private RelatedComboField fProgetto;
	private RelatedComboField fStagione;

	private Filter filtroPerStagione;


	public EventoSearch() {
		super();
	}

	// come default spazzola tutti i campi della Entity
	// aggiunge i campi al layout
	// può essere sovrascritto nelle sottoclassi specifiche
	protected void createFields(FormLayout layout) {

		fSigla = new TextField("Sigla");
		layout.addComponent(fSigla);

		fTitolo = new TextField("Titolo");
		layout.addComponent(fTitolo);

		fProgetto = new ERelatedComboField(Progetto.class, "Progetto");
		layout.addComponent(fProgetto);

		fStagione = new ERelatedComboField(Stagione.class, "Stagione");
		layout.addComponent(fStagione);


	}// end of method


	@Override
	public ArrayList<Filter> createFilters() {
		ArrayList<Filter> filters = new ArrayList<>();
		filters.add(createStringFilter(fSigla, Evento_.sigla, SearchType.STARTS_WITH));
		filters.add(createStringFilter(fTitolo, Evento_.titolo, SearchType.CONTAINS));
		filters.add(createBeanFilter(fProgetto, Evento_.progetto));
		filters.add(createBeanFilter(fStagione, Evento_.stagione));

		// filtro stagione corrente
		if(isStagioneCorrente()){
			Filter filter = new Compare.Equal(Evento_.stagione.getName(), Stagione.getStagioneCorrente());
			filters.add(filter);
		}

		return filters;
	}// end of method


	/**
	 * Invocato quando il checkbox "Solo stagione corrente" cambia
	 * @param newValue il vuovo valore
	 */
	public void checkStagioneChanged(boolean newValue){
		if(newValue){
			filtroPerStagione=new Compare.Equal(Stagione_.corrente.getName(), true);
			fStagione.getFilterableContainer().addContainerFilter(filtroPerStagione);

		}else{
			fStagione.getFilterableContainer().removeContainerFilter(filtroPerStagione);
		}
	}


}// end of class
