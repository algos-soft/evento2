package it.algos.evento.entities.lettera.allegati;

import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import it.algos.evento.entities.lettera.allegati.AllegatoModulo.AllegatoListener;
import it.algos.webbase.web.dialog.AlertDialog;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.field.TextField;

@SuppressWarnings("serial")
public class GestoreAllegati extends AlertDialog {
	
	AllegatoModulo modulo;
	private GTable table;

	public GestoreAllegati() {
		super("Gestione allegati");
		
		// crea il modulo allegati e attacca un listener
		modulo = new AllegatoModulo();
		modulo.addAllegatoListener(new AllegatoListener() {
			
			@Override
			public void renamed_() {
				table.showAll();
			}
			
			@Override
			public void deleted_() {
				table.showAll();
			}
			
			@Override
			public void added_() {
				table.showAll();
			}
		});
		
		// crea la table
		table = new GTable();

		// aggiunge la GUI
		addComponent(createMainComponent());
	}

	private Component createMainComponent() {
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.addComponent(table);
		layout.addComponent(createPanBottoni());
		return layout;
	}


	private Component createPanBottoni() {

		Button bAdd = new GButton("Aggiungi");
		bAdd.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				modulo.addAllegato();
			}
		});

		Button bRemove = new GButton("Rimuovi");
		bRemove.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				String nome = getNomeSelezionato();
				if (!nome.equals("")) {
					new DialogoRimuovi().show(UI.getCurrent());
				} else {
					Notification.show("Selezionare prima un elemento");
				}
			}
		});

		Button bRename = new GButton("Rinomina");
		bRename.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				String nome = getNomeSelezionato();
				if (!nome.equals("")) {
					new DialogoRinomina().show(UI.getCurrent());
				} else {
					Notification.show("Selezionare prima un elemento");
				}
			}
		});

		HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(true);
		layout.setHeight("100%");
		Label label = new Label(); // elastic spacer
		layout.addComponent(label);
		layout.setExpandRatio(label, 1f);
		layout.addComponent(bAdd);
		layout.addComponent(bRemove);
		layout.addComponent(bRename);

		return layout;
	}

	
	private class DialogoRimuovi extends ConfirmDialog{
		
		public DialogoRimuovi() {
			super(null);
			setMessage("Confermi la cancellazione dell'allegato '"+getNomeSelezionato()+"'?");
		}

		@Override
		protected void onConfirm() {
			modulo.deleteAllegato(getNomeSelezionato());
			super.onConfirm();
		}
		
	}
	
	private class DialogoRinomina extends ConfirmDialog{
		private TextField field;
		
		public DialogoRinomina() {
			super(null);
			field = new TextField("nome");
			field.setValue(getNomeSelezionato());
			field.setWidth("200px");
			addComponent(field);
		}

		@Override
		protected void onConfirm() {
			String newName = field.getValue();
			if ((newName!= null) && (!newName.equals(""))) {
				modulo.renameAllegato(getNomeSelezionato(), newName);
				super.onConfirm();
			} else {
				Notification.show("Nome non valido");
			}

		}
		
	}
	
	/**
	 * Ritorna il nome dell'allegato correntemente selezionato.
	 * <p>
	 * @return il nome
	 */
	private String getNomeSelezionato(){
		String name = "";
		BaseEntity entity=table.getSelectedEntity();
		if ((entity!=null) && (entity instanceof Allegato)) {
			Allegato allegato = (Allegato)entity;
			name=allegato.getName();
		}
		return name;
	}


	private class GButton extends Button {

		public GButton(String title) {
			super(title);
			setWidth("120px");
		}

	}
	
	private AllegatoModulo getModulo(){
		return modulo;
	}
	
	private class GTable extends AllegatoTable{

		public GTable() {
			super(getModulo());
			setCaption("Allegati disponibili");
			setPageLength(12);
			alwaysRecalculateColumnWidths = true;
		}
		
		public void reload(){
			table.refreshRowCache();
		}
		
	}


}
