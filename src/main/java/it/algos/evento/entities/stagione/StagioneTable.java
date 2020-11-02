package it.algos.evento.entities.stagione;

import com.vaadin.data.Item;
import com.vaadin.event.Action;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import it.algos.webbase.multiazienda.ETable;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.lib.Lib;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.query.AQuery;
import it.algos.webbase.web.table.ATable;

@SuppressWarnings("serial")
public class StagioneTable extends ETable {

	public StagioneTable(ModulePop modulo) {
		super(modulo);

		setColumnHeader(Stagione_.sigla, "Stagione");
		setColumnHeader(Stagione_.datainizio, "data inizio");
		setColumnHeader(Stagione_.datafine, "data fine");
		setColumnHeader(Stagione_.corrente, "stagione corrente");


		//setColumnWidth(Prenotazione_.dataPrenotazione, 80);

		//setColumnAlignment(Prenotazione_.dataPrenotazione, Align.CENTER);

		// comandi contestuali aggiuntivi
		addActionHandler(new Action.Handler() {

			private final Action actSetCorrente = new Action(StagioneTablePortal.CMD_SET_CORRENTE,
					StagioneTablePortal.ICON_SET_CORRENTE);


			public Action[] getActions(Object target, Object sender) {
				Action[] actions = null;
				actions = new Action[1];
				actions[0] = actSetCorrente;
				return actions;
			}

			public void handleAction(Action action, Object sender, Object target) {
				Item rowItem = getTable().getItem(target);
				if (rowItem != null) {
					Object value = rowItem.getItemProperty("id").getValue();
					long id = Lib.getLong(value);
					if (id > 0) {

						if (action.equals(actSetCorrente)) {
							setStagioneCorrente(id, getTable());
						}


					}
				}

			}
		});

//		StagioneModulo.addStatusChangeListener(new StatusChangeListener() {
//
//			@Override
//			public void statusChanged(TipoEventoPren tipoEvento) {
//				refreshRowCache();
//			}
//		});



	}// end of constructor




	/**
	 * Imposta la stagione corrente
	 */
	public static void setStagioneCorrente(Object id, ATable table) {

		boolean cont = true;

		final Stagione stagione = (Stagione)AQuery.find(Stagione.class, (long)id);
		cont=(stagione!=null);

		// controllo che sia confermata
		if (cont) {
			if (stagione.isCorrente()) {
				cont = false;
				Notification.show("Questa è già la stagione corrente");
			}
		}

		// chiede conferma ed esegue
		if (cont) {
			String title="Stagione corrente";
			String msg="Vuoi impostare la stagione selezionata come stagione corrente?";
			ConfirmDialog dialog = new ConfirmDialog(title, msg, new ConfirmDialog.Listener() {
				@Override
				public void onClose(ConfirmDialog dialog, boolean confirmed) {
					if(confirmed){
						StagioneModulo.cmdSetCorrente(stagione, table);
					}
				}
			});
			dialog.show(UI.getCurrent());
		}


	}


	protected Object[] getDisplayColumns() {
		return new Object[] { Stagione_.sigla, Stagione_.datainizio,
				Stagione_.datafine, Stagione_.corrente };
	}// end of method



}
