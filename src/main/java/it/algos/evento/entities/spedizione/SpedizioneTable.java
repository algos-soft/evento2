package it.algos.evento.entities.spedizione;

import com.vaadin.data.Property;
import it.algos.webbase.multiazienda.ETable;
import it.algos.webbase.web.lib.LibDate;
import it.algos.webbase.web.module.ModulePop;

import java.util.Date;

@SuppressWarnings("serial")
public class SpedizioneTable extends ETable {

	public SpedizioneTable(ModulePop module) {
		super(module);

		setColumnHeader(Spedizione_.dataSpedizione, "Data e ora");
		setColumnHeader(Spedizione_.destinatario, "Destinatario");
		setColumnHeader(Spedizione_.lettera, "Tipo lettera");
		setColumnHeader(Spedizione_.operatore, "Operatore");
		setColumnHeader(Spedizione_.spedita, "Sped");
		setColumnHeader(Spedizione_.errore, "Errore");

	}


	
	protected Object[] getDisplayColumns() {
		return new Object[] { 
				Spedizione_.dataSpedizione, 
				Spedizione_.destinatario, 
				Spedizione_.lettera, 
				Spedizione_.operatore,
				Spedizione_.spedita, 
				Spedizione_.errore
				};
	}// end of method
	
	
	@Override
	protected String formatPropertyValue(Object rowId, Object colId, Property property) {

		if (colId.equals(Spedizione_.dataSpedizione.getName())) {
			return LibDate.toStringDDMMYYYYHHMM((Date) property.getValue());
		}

		return super.formatPropertyValue(rowId, colId, property);

	}// end of method


}
