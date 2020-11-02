package it.algos.evento.entities.insegnante;

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
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.datasource.DRDataSource;

import javax.persistence.metamodel.Attribute;
import java.io.ByteArrayOutputStream;
import java.util.List;

@SuppressWarnings("serial")
public class InsegnanteModulo extends CompanyModule {

	/**
	 * Costruttore senza parametri
	 */
	public InsegnanteModulo() {
		super(Insegnante.class);
	}// end of constructor


	// come default usa il titolo standard
	// può essere sovrascritto nelle sottoclassi specifiche
	protected String getCaptionSearch() {
		return "referenti";
	}// end of method

	// come default spazzola tutti i campi della Entity
	// può essere sovrascritto nelle sottoclassi specifiche
	// serve anche per l'ordine con cui vengono presentati i campi
	protected Attribute<?, ?>[] creaFieldsList() {
		return new Attribute[] { Insegnante_.titolo, Insegnante_.cognome, Insegnante_.nome, Insegnante_.materie,
				Insegnante_.telefono, Insegnante_.email };
	}// end of method

	// come default spazzola tutti i campi della Entity
	// può essere sovrascritto nelle sottoclassi specifiche
	// serve anche per l'ordine con cui vengono presentati i campi
	protected Attribute<?, ?>[] creaFieldsForm() {
		return new Attribute[] { Insegnante_.cognome, Insegnante_.nome, Insegnante_.titolo, Insegnante_.email,
				Insegnante_.telefono, Insegnante_.materie, Insegnante_.indirizzo1, Insegnante_.indirizzo2,
				Insegnante_.note };
	}// end of method


	@Override
	public ATable createTable() {
		return (new InsegnanteTable(this));
	}// end of method

	@Override
	public ModuleForm createForm(Item item) {
		return (new InsegnanteForm(item, this));
	}// end of method

	@Override
	public SearchManager createSearchManager() {
		return new InsegnanteSearch(this);
	}// end of method

	/**
	 * Create the Table Portal
	 * 
	 * @return the TablePortal
	 */
	public TablePortal createTablePortal() {
		return new InsegnanteTablePortal(this);
	}// end of method

	/**
	 * Delete selected items button pressed
	 */
	public void delete() {
		
		// prima controlla se ci sono prenotazioni collegate
		boolean cont=true;
		for (Object id : getTable().getSelectedIds()) {
			BaseEntity entity=getTable().getEntity((Long)id);
			List listaPren = CompanyQuery.getList(Prenotazione.class, Prenotazione_.insegnante, entity);
			if (listaPren.size()>0) {
				Notification.show("Impossibile eliminare i referenti selezionati perché hanno delle prenotazioni.\nEliminate prima le prenotazioni collegate o assegnatele a un altro referente.", Notification.Type.WARNING_MESSAGE);
				cont=false;
				break;
			}
		}

		// se tutto ok ritorna il controllo alla superclasse
		if (cont) {
			super.delete();
		}
	}// end of method

	
	public static JasperReportBuilder createPdfReport() {
		ByteArrayOutputStream os = null;
		JasperReportBuilder report = DynamicReports.report();
		// report.addColumn(Columns.column("Item", "item",
		// DataTypes.stringType()));
		// report.addColumn(Columns.column("Quantity", "quantity",
		// DataTypes.integerType()));
		report.addTitle(Components.text("Report title"));
		DRDataSource ds = new DRDataSource();
		report.setDataSource(ds);
		return report;
	}// end of method

}// end of class
