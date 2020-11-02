package it.algos.evento.entities.insegnante;

import com.vaadin.data.Property;
import com.vaadin.ui.Image;
import it.algos.evento.EventoApp;
import it.algos.evento.entities.ordinescuola.OrdineScuola;
import it.algos.webbase.multiazienda.ETable;
import it.algos.webbase.web.lib.Lib;
import it.algos.webbase.web.lib.LibResource;
import it.algos.webbase.web.module.ModulePop;

@SuppressWarnings("serial")
public class InsegnanteTable extends ETable {

	// id della colonna generata "referente"
	private static final String COL_PRIVATO ="tipo";

	public InsegnanteTable(ModulePop modulo) {
		super(modulo);

		setColumnHeader(Insegnante_.ordineScuola, "Ordine");

		// colonne collapsed di default
		setColumnCollapsingAllowed(true);
		setColumnCollapsed(Insegnante_.indirizzo1.getName(), true);
		setColumnCollapsed(Insegnante_.indirizzo2.getName(), true);
		setColumnCollapsed(Insegnante_.note.getName(), true);
		setColumnCollapsed(Insegnante_.telefono.getName(), true);
		setColumnCollapsed(Insegnante_.titolo.getName(), true);
	}// end of constructor


	protected Object[] getDisplayColumns() {
		return new Object[] {
				COL_PRIVATO,
				Insegnante_.cognome,
				Insegnante_.nome,
				Insegnante_.ordineScuola,
				Insegnante_.materie,
				Insegnante_.email,

				Insegnante_.indirizzo1,
				Insegnante_.indirizzo2,
				Insegnante_.note,
				Insegnante_.telefono,
				Insegnante_.titolo
		};
	}// end of method

	protected void createAdditionalColumns() {

		addGeneratedColumn(COL_PRIVATO, (source, itemId, columnId) -> {
            boolean priv = Lib.getBool(getContainerProperty(itemId, Insegnante_.privato.getName()).getValue());
            String img_name=(priv? "person_20px.png":"teacher_20px.png");
            return new Image(null, LibResource.getImgResource(EventoApp.IMG_FOLDER_NAME, img_name));
        });

	}


	@Override
	protected String formatPropertyValue(Object rowId, Object colId, Property property) {
		String string = null;

		if (colId.equals(Insegnante_.ordineScuola.getName())) {
			Object value = property.getValue();
			if (value!=null && value instanceof OrdineScuola) {
				OrdineScuola ord = (OrdineScuola)value;
				string=ord.getSigla();
			} else {
				string="";
			}
			return string;
		}

		return super.formatPropertyValue(rowId, colId, property);
	}// end of method

}// end of Table class
