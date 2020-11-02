package it.algos.evento.entities.prenotazione.eventi;

import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.TableToolbar;

public class EventoPrenTablePortal extends TablePortal {

	public EventoPrenTablePortal(ModulePop modulo) {
		super(modulo);
	}

	public TableToolbar createToolbar() {
		TableToolbar toolbar = new EventoPrenTableToolbar(this);
		return toolbar;
	}// end of method

}
