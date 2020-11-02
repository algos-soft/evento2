package it.algos.evento.statistiche;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import it.algos.webbase.web.toolbar.Toolbar;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class StatToolbar extends Toolbar {

	private ArrayList<TableToolbarListener> listeners = new ArrayList<TableToolbarListener>();

	public StatToolbar() {
		super();

		addExport();

	}

	protected void addExport() {
		addButton("Esporta", FontAwesome.DOWNLOAD, new MenuBar.Command() {
			public void menuSelected(MenuItem selectedItem) {
				fire(Bottoni.export);
			}// end of method
		});// end of anonymous class
	}// end of method


	public void addToolbarListener(TableToolbarListener listener) {
		this.listeners.add(listener);
	}

	private void fire(Bottoni bottone) {
		for (TableToolbarListener l : listeners) {
			switch (bottone) {
			case export:
				l.export_();
				break;
			default:
				break;
			}
		}

	}// end of method

	public interface TableToolbarListener {
		public void export_();
	}// end of method

	public enum Bottoni {
		export;
	}// end of method

}
