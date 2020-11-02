package it.algos.evento.entities.scuola;

import com.vaadin.data.Property;
import it.algos.evento.entities.ordinescuola.OrdineScuola;
import it.algos.webbase.multiazienda.ETable;
import it.algos.webbase.web.module.ModulePop;

@SuppressWarnings("serial")
public class ScuolaTable extends ETable {

	//private OrdineScuolaConverterOld ordineConverter = new OrdineScuolaConverterOld();

	public ScuolaTable(ModulePop modulo) {
		super(modulo);
		setColumnAlignment(Scuola_.ordine, Align.LEFT);

	}// end of constructor


	@Override
	protected String formatPropertyValue(Object rowId, Object colId, Property property) {
		String string = null;

		if (colId.equals(Scuola_.ordine.getName())) {
			Object value = property.getValue();
			if (value!=null && value instanceof OrdineScuola) {
				OrdineScuola ordine = (OrdineScuola)value;
				string=ordine.getSigla();
			} else {
				string="";
			}
			return string;
		}

		return super.formatPropertyValue(rowId, colId, property);
	}// end of method



}// end of class
