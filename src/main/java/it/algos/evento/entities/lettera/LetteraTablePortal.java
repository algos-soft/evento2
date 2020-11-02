package it.algos.evento.entities.lettera;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import it.algos.evento.entities.lettera.allegati.GestoreAllegati;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.TableToolbar;

@SuppressWarnings("serial")
public class LetteraTablePortal extends TablePortal {


	public LetteraTablePortal(ModulePop modulo) {
		super(modulo);
	}// end of constructor

	public TableToolbar createToolbar() {
		final TableToolbar toolbar = super.createToolbar();

		MenuBar.MenuItem item = toolbar.addButton("Altro...", FontAwesome.BARS, null);

		
		item.addItem("Gestione allegati...", FontAwesome.PAPERCLIP, new MenuBar.Command() {
			public void menuSelected(MenuItem selectedItem) {
				new GestoreAllegati().show(getUI());
			}// end of method
		});// end of anonymous class


		return toolbar;
	}// end of method

}// end of class

