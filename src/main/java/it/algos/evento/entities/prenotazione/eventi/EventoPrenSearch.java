package it.algos.evento.entities.prenotazione.eventi;

import com.vaadin.data.Container.Filter;
import com.vaadin.ui.FormLayout;
import it.algos.evento.entities.prenotazione.Prenotazione;
import it.algos.webbase.multiazienda.ERelatedComboField;
import it.algos.webbase.multiazienda.ESearchManager;
import it.algos.webbase.web.component.DateRangeComponent;
import it.algos.webbase.web.field.ArrayComboField;
import it.algos.webbase.web.field.RelatedComboField;
import it.algos.webbase.web.field.TextField;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class EventoPrenSearch extends ESearchManager {

	private DateRangeComponent fRangeDataEve;
	private RelatedComboField fPrenotazione;
	private ArrayComboField fTipoEvento;
	private TextField fDettagli;
	private TextField fUtente;

	public EventoPrenSearch() {
		super();
		setCaption("Ricerca eventi");
	}


	// come default spazzola tutti i campi della Entity
	// aggiunge i campi al layout
	// può essere sovrascritto nelle sottoclassi specifiche
	protected void createFields(FormLayout layout) {

		fRangeDataEve = new DateRangeComponent("Data evento");
		layout.addComponent(fRangeDataEve);

		fPrenotazione = new ERelatedComboField(Prenotazione.class, "Prenotazione");
		layout.addComponent(fPrenotazione);

		fTipoEvento = new ArrayComboField(TipoEventoPren.values(), "Tipo evento");
		layout.addComponent(fTipoEvento);

		fDettagli = new TextField("Dettagli");
		layout.addComponent(fDettagli);

		fUtente = new TextField("Utente");
		layout.addComponent(fUtente);


	}// end of method


	@Override
	public ArrayList<Filter> createFilters() {
		ArrayList<Filter> filters = new ArrayList<Filter>();
		filters.add(fRangeDataEve.getFilter(EventoPren_.timestamp.getName()));
		filters.add(createBeanFilter(fPrenotazione, EventoPren_.prenotazione));
		filters.add(createArrayFilter(fTipoEvento, EventoPren_.tipo));
		filters.add(createStringFilter(fDettagli, EventoPren_.dettagli, SearchType.CONTAINS));
		filters.add(createStringFilter(fUtente, EventoPren_.user, SearchType.STARTS_WITH));

		return filters;
	}

}
