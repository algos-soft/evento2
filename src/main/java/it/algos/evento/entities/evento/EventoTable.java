package it.algos.evento.entities.evento;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import it.algos.evento.entities.stagione.Stagione;
import it.algos.webbase.multiazienda.ETable;
import it.algos.webbase.web.converter.StringToBigDecimalConverter;
import it.algos.webbase.web.module.ModulePop;

import java.math.BigDecimal;
import java.util.Locale;

@SuppressWarnings("serial")
public class EventoTable extends ETable {

	private static final StringToBigDecimalConverter conv = new StringToBigDecimalConverter(2);
	private static final Locale locale = Locale.getDefault();
	private static final String colPrezzi = "Prezzo";

	public EventoTable(ModulePop modulo) {
		super(modulo);
	}

	/**
	 * Creates the container
	 * <p>
	 *
	 * @return un container RW filtrato sulla azienda corrente
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Container createContainer() {
		// aggiunge un filtro sulla stagione corrente
		Container cont = super.createContainer();
		Filter filter=new Compare.Equal(Evento_.stagione.getName(), Stagione.getStagioneCorrente());
		if(cont instanceof Filterable){
			Filterable fcont=(Filterable)cont;
			fcont.addContainerFilter(filter);
		}
		return cont;
	}// end of method


	@Override
	protected void createAdditionalColumns() {
		addGeneratedColumn(colPrezzi, new PrezziColumnGenerator());
	}

	@Override
	protected Object[] getDisplayColumns() {
		return new Object[] { Evento_.sigla, Evento_.titolo, colPrezzi, Evento_.progetto, Evento_.stagione};
	}
	


	/** Formats the value in a column containing Double objects. */
	class PrezziColumnGenerator implements ColumnGenerator {

		/**
		 * Genera la cella dei prezzi.
		 */
		public Component generateCell(Table source, Object itemId, Object columnId) {

			Property prop;
			Item item = source.getItem(itemId);

			prop = item.getItemProperty(Evento_.prezzoPerGruppi.getName());
			boolean gruppi = (Boolean)prop.getValue();

			String string;
			if(gruppi){
				prop = item.getItemProperty(Evento_.importoGruppo.getName());
				String importo = conv.convertToPresentation((BigDecimal) prop.getValue(), String.class, locale);
				if (importo==null)importo="0";
				string = importo;
			}else{
				prop = item.getItemProperty(Evento_.importoIntero.getName());
				String intero = conv.convertToPresentation((BigDecimal) prop.getValue(), String.class, locale);
				if (intero==null)intero="0";
				prop = item.getItemProperty(Evento_.importoRidotto.getName());
				String ridotto = conv.convertToPresentation((BigDecimal) prop.getValue(), String.class, locale);
				if (ridotto==null)intero="0";
				prop = item.getItemProperty(Evento_.importoDisabili.getName());
				String disabile = conv.convertToPresentation((BigDecimal) prop.getValue(), String.class, locale);
				if (disabile==null)disabile="0";
				prop = item.getItemProperty(Evento_.importoAccomp.getName());
				String accomp = conv.convertToPresentation((BigDecimal) prop.getValue(), String.class, locale);
				if (accomp==null)accomp="0";
				string = intero + " | " + ridotto+" | "+disabile+" | "+accomp;
			}

			return new Label(string);
		}
	}

}
