package it.algos.evento.entities.rappresentazione;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.ui.FormLayout;
import it.algos.evento.entities.evento.Evento;
import it.algos.evento.entities.evento.Evento_;
import it.algos.evento.entities.sala.Sala;
import it.algos.evento.entities.stagione.Stagione;
import it.algos.evento.search.StagioneSearchManager;
import it.algos.webbase.multiazienda.ERelatedComboField;
import it.algos.webbase.web.component.DateRangeComponent;
import it.algos.webbase.web.field.RelatedComboField;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class RappresentazioneSearch extends StagioneSearchManager {

	private static final String PROP_EVENTO_STAGIONE =Evento.class.getSimpleName().toLowerCase()+"."+ Evento_.stagione.getName();

	private RelatedComboField fEvento;
	private RelatedComboField fSala;
	private DateRangeComponent fDateRange;

	private Filter filtroEventiPerStagione;

	public RappresentazioneSearch() {
		super();
		setCaption("Ricerca rappresentazioni");
	}// end of constructor

	// come default spazzola tutti i campi della Entity
	// aggiunge i campi al layout
	// può essere sovrascritto nelle sottoclassi specifiche
	protected void createFields(FormLayout layout) {
		fEvento = new ERelatedComboField(Evento.class, "Evento");
		layout.addComponent(fEvento);

		fSala = new ERelatedComboField(Sala.class, "Sala");
		layout.addComponent(fSala);

		fDateRange = new DateRangeComponent();
		layout.addComponent(fDateRange);
	}// end of method

	@Override
	public ArrayList<Filter> createFilters() {
		ArrayList<Filter> filters = new ArrayList<Filter>();
		filters.add(createBeanFilter(fEvento, Rappresentazione_.evento));
		filters.add(createBeanFilter(fSala, Rappresentazione_.sala));
		filters.add(fDateRange.getFilter(Rappresentazione_.dataRappresentazione.getName()));

		// filtro stagione corrente
		if(isStagioneCorrente()){
			Filter filter = new Compare.Equal(PROP_EVENTO_STAGIONE, Stagione.getStagioneCorrente());
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
			filtroEventiPerStagione=new Compare.Equal(Evento_.stagione.getName(), Stagione.getStagioneCorrente());
			fEvento.getFilterableContainer().addContainerFilter(filtroEventiPerStagione);

		}else{
			fEvento.getFilterableContainer().removeContainerFilter(filtroEventiPerStagione);
		}
	}


}// end of class
