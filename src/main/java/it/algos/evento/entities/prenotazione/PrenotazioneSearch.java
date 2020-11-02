package it.algos.evento.entities.prenotazione;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import it.algos.evento.entities.evento.Evento;
import it.algos.evento.entities.evento.Evento_;
import it.algos.evento.entities.insegnante.Insegnante;
import it.algos.evento.entities.progetto.Progetto;
import it.algos.evento.entities.rappresentazione.Rappresentazione;
import it.algos.evento.entities.scuola.Scuola;
import it.algos.evento.entities.stagione.Stagione;
import it.algos.evento.search.StagioneSearchManager;
import it.algos.webbase.multiazienda.ERelatedComboField;
import it.algos.webbase.web.component.DateRangeComponent;
import it.algos.webbase.web.field.IntegerField;
import it.algos.webbase.web.field.RelatedComboField;
import it.algos.webbase.web.field.YesNoField;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class PrenotazioneSearch extends StagioneSearchManager {

	private static final String PROP_RAPPRESENTAZIONE_STAGIONE =Evento.class.getSimpleName().toLowerCase()+"."+ Evento_.stagione.getName();


	private IntegerField fNumPren;
	private DateRangeComponent fRangeDataPren;
	private RelatedComboField fScuola;
	private RelatedComboField fInsegnante;
	private RelatedComboField fRappresentazione;
	private RelatedComboField fProgetto;
	private RelatedComboField fEvento;
	private YesNoField fPrivato;
	private YesNoField fConfermata;
	private YesNoField fCongelata;
	private YesNoField fPagConfermato;
	private YesNoField fPagRicevuto;
	private IntegerField fLvlSollecitoConf;
	private IntegerField fLvlSollecitoPaga;

	private Filter filtroEventiPerStagione;
	private Filter filtroRappresentazioniPerStagione;


	public PrenotazioneSearch() {
		super();
		setCaption("Ricerca prenotazioni");
	}// end of method

	// aggiunge i campi al layout
	// può essere sovrascritto nelle sottoclassi specifiche
	protected void createFields(FormLayout layout) {
		fNumPren = new IntegerField("N. prenotazione");
		fRangeDataPren = new DateRangeComponent("Data prenotazione", false, true);
		fScuola = new ERelatedComboField(Scuola.class, "Scuola");
		fInsegnante = new ERelatedComboField(Insegnante.class, "Insegnante");
		fRappresentazione = new ERelatedComboField(Rappresentazione.class, "Rappresentazione");
		fRappresentazione.getJPAContainer().addNestedContainerProperty(PROP_RAPPRESENTAZIONE_STAGIONE);
		fProgetto = new ERelatedComboField(Progetto.class, "Progetto");
		fEvento = new ERelatedComboField(Evento.class, "Evento");
		fPrivato = new YesNoField("Privato");
		fConfermata = new YesNoField("Confermata");
		fCongelata = new YesNoField("Congelata");
		fPagConfermato = new YesNoField("Pagam. confermato");
		fPagRicevuto = new YesNoField("Pagam. ricevuto");
		fLvlSollecitoConf = new IntegerField("Liv. sollecito conferma");
		fLvlSollecitoPaga = new IntegerField("Liv. sollecito pagamento");

		buildUI(layout);

	}// end of method


	@Override
	protected Component createDetailComponent() {
		return super.createDetailComponent();
	}

	private void buildUI(FormLayout layout){
		layout.addComponent(fNumPren);
		layout.addComponent(fRangeDataPren);
		layout.addComponent(fScuola);
		layout.addComponent(fInsegnante);
		layout.addComponent(fRappresentazione);
		layout.addComponent(fProgetto);
		layout.addComponent(fEvento);
		layout.addComponent(fPrivato);

		GridLayout grid = new GridLayout(3,2);
		grid.setSpacing(true);
		grid.addComponent(fConfermata, 0, 0);
		grid.addComponent(fCongelata, 1, 0);
		grid.addComponent(fLvlSollecitoConf, 2, 0);
		grid.addComponent(fPagConfermato, 0, 1);
		grid.addComponent(fPagRicevuto, 1, 1);
		grid.addComponent(fLvlSollecitoPaga, 2, 1);
		layout.addComponent(grid);

		layout.setMargin(false);

	}

	
	@Override
	public ArrayList<Filter> createFilters() {
		ArrayList<Filter> filters = new ArrayList<Filter>();
		filters.add(createIntegerFilter(fNumPren, Prenotazione_.numPrenotazione));
		filters.add(fRangeDataPren.getFilter(Prenotazione_.dataPrenotazione.getName()));
		filters.add(createBeanFilter(fScuola, Prenotazione_.scuola));
		filters.add(createBeanFilter(fInsegnante, Prenotazione_.insegnante));
		filters.add(createBeanFilter(fRappresentazione, Prenotazione_.rappresentazione));
		filters.add(createBeanFilter(fEvento, PrenotazioneModulo.PROP_EVENTO));
		filters.add(createBeanFilter(fProgetto, PrenotazioneModulo.PROP_PROGETTO));

		filters.add(createBoolFilter(fPrivato, Prenotazione_.privato));
		filters.add(createBoolFilter(fConfermata, Prenotazione_.confermata));
		filters.add(createBoolFilter(fCongelata, Prenotazione_.congelata));
		filters.add(createBoolFilter(fPagConfermato, Prenotazione_.pagamentoConfermato));
		filters.add(createBoolFilter(fPagRicevuto, Prenotazione_.pagamentoRicevuto));

		filters.add(createIntegerFilter(fLvlSollecitoConf, Prenotazione_.livelloSollecitoConferma));
		filters.add(createIntegerFilter(fLvlSollecitoPaga, Prenotazione_.livelloSollecitoPagamento));

		// filtro stagione corrente
		if(isStagioneCorrente()){
			Filter filter = new Compare.Equal(PrenotazioneModulo.PROP_STAGIONE, Stagione.getStagioneCorrente());
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
			fEvento.getJPAContainer().addContainerFilter(filtroEventiPerStagione);

			filtroRappresentazioniPerStagione=new Compare.Equal(PROP_RAPPRESENTAZIONE_STAGIONE, Stagione.getStagioneCorrente());
			fRappresentazione.getJPAContainer().addContainerFilter(filtroRappresentazioniPerStagione);

		}else{
			fEvento.getJPAContainer().removeContainerFilter(filtroEventiPerStagione);
			fRappresentazione.getJPAContainer().removeContainerFilter(filtroRappresentazioniPerStagione);
		}
	}


}// end of class
