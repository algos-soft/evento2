package it.algos.evento.entities.scuola;

import com.vaadin.data.Item;
import com.vaadin.ui.Notification;
import it.algos.evento.entities.prenotazione.Prenotazione;
import it.algos.evento.entities.prenotazione.Prenotazione_;
import it.algos.webbase.multiazienda.CompanyModule;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.search.SearchManager;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.table.TablePortal;

import javax.persistence.metamodel.Attribute;
import java.util.List;

@SuppressWarnings("serial")
public class ScuolaModulo extends CompanyModule {

	/**
	 * Costruttore senza parametri
	 */
	public ScuolaModulo() {
		super(Scuola.class);
	}// end of constructor


	// come default usa il titolo standard
	// può essere sovrascritto nelle sottoclassi specifiche
	protected String getCaptionSearch() {
		return "scuole";
	}// end of method

	// come default spazzola tutti i campi della Entity
	// può essere sovrascritto nelle sottoclassi specifiche
	// serve anche per l'ordine con cui vengono presentati i campi
	protected Attribute<?, ?>[] creaFieldsList() {
		return new Attribute[] { Scuola_.sigla, Scuola_.nome, Scuola_.ordine, Scuola_.tipo, Scuola_.comune, Scuola_.telefono };
	}// end of method

	// come default spazzola tutti i campi della Entity
	// può essere sovrascritto nelle sottoclassi specifiche
	// serve anche per l'ordine con cui vengono presentati i campi
	protected Attribute<?, ?>[] creaFieldsForm() {
		return new Attribute[] { Scuola_.sigla, Scuola_.nome, Scuola_.ordine, Scuola_.email, Scuola_.telefono };
	}// end of method

	// come default spazzola tutti i campi della Entity
	// può essere sovrascritto nelle sottoclassi specifiche
	// serve anche per l'ordine con cui vengono presentati i campi
	protected Attribute<?, ?>[] creaFieldsSearch() {
		return new Attribute[] { Scuola_.sigla, Scuola_.nome, Scuola_.ordine, Scuola_.comune };
	}// end of method


	@Override
	public ATable createTable() {
		return (new ScuolaTable(this));
	}// end of method
	
	/**
	 * Create the Table Portal
	 * 
	 * @return the TablePortal
	 */
	public TablePortal createTablePortal() {
		return new ScuolaTablePortal(this);
	}// end of method


	@Override
	public ModuleForm createForm(Item item) {
		return (new ScuolaForm(item, this));
	}// end of method

	@Override
	public SearchManager createSearchManager() {
		return new ScuolaSearch(this);
	}// end of method

	/**
	 * Delete selected items button pressed
	 */
	public void delete() {
		
		// prima controlla se ci sono prenotazioni collegate
		boolean cont=true;
		for (Object id : getTable().getSelectedIds()) {
			BaseEntity entity=getTable().getEntity((Long)id);
			List listaPren = CompanyQuery.getList(Prenotazione.class, Prenotazione_.scuola, entity);
			if (listaPren.size()>0) {
				Notification.show("Impossibile eliminare le scuole selezionate perché ci sono delle prenotazioni.\nEliminate prima le prenotazioni collegate.", Notification.Type.WARNING_MESSAGE);
				cont=false;
				break;
			}
		}

		// se tutto ok ritorna il controllo alla superclasse
		if (cont) {
			super.delete();
		}
	}// end of method

}// end of class
