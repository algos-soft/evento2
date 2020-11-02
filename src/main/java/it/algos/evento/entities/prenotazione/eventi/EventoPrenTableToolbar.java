package it.algos.evento.entities.prenotazione.eventi;

import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.TableToolbar;

public class EventoPrenTableToolbar extends TableToolbar {

	public EventoPrenTableToolbar(TablePortal tablePortal) {
		super(tablePortal);
	}

	// no create button
	protected void addCreate() {
	}

	// no edit button
	protected void addEdit() {
	}

}
