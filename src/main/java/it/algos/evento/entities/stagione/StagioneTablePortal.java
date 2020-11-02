package it.algos.evento.entities.stagione;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.Toolbar;

@SuppressWarnings("serial")
public class StagioneTablePortal extends TablePortal {

	public static final String CMD_SET_CORRENTE = "Imposta come stagione corrente";
	public static final Resource ICON_SET_CORRENTE = FontAwesome.CHECK;

	public StagioneTablePortal(ModulePop modulo) {
		super(modulo);

		Toolbar toolbar = getToolbar();

		// bottone Set Stagione Corrente...
		MenuItem item = toolbar.addButton("Corrente", FontAwesome.CHECK, new MenuBar.Command() {
			public void menuSelected(MenuItem selectedItem) {
				Object id = getTable().getSelectedId();

				// controllo selezione
				if (id != null) {
					StagioneTable.setStagioneCorrente(id, getTable());
				} else {
					msgNoSelection();
				}

			}
		});// end of anonymous class);
		item.setDescription("Imposta la stagione selezionata come stagione corrente");


	}// end of method

	private void msgNoSelection() {
		Notification.show("Seleziona prima una stagione.");
	}// end of method

}
