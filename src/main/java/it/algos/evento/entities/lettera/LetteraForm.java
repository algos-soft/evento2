package it.algos.evento.entities.lettera;

import com.vaadin.data.Item;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import it.algos.evento.entities.lettera.allegati.Allegato;
import it.algos.evento.entities.lettera.allegati.AllegatoModulo;
import it.algos.evento.entities.lettera.allegati.GestoreAllegati;
import it.algos.webbase.web.dialog.PopDialog;
import it.algos.webbase.web.field.CheckBoxField;
import it.algos.webbase.web.field.TextArea;
import it.algos.webbase.web.field.TextField;
import it.algos.webbase.web.form.AFormLayout;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.module.ModulePop;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class LetteraForm extends ModuleForm {


//	public LetteraForm(ModulePop modulo) {
//		super(modulo);
//		doInit();
//	}// end of constructor

	public LetteraForm(ModulePop modulo, Item item) {
		super(item, modulo);
		doInit();
	}// end of constructor

	private void doInit(){
		//setMargin(true);
		setWidth("800px");
	}

	/**
	 * Populate the map to bind item properties to fields.
	 * 
	 * Crea e aggiunge i campi. Implementazione di default nella superclasse. I campi vengono recuperati dal Modello. I
	 * campi vengono creti del tipo grafico previsto nella Entity. Se si vuole aggiungere un campo (solo nel form e non
	 * nel Modello), usare il metodo sovrascritto nella sottoclasse richiamando prima il metodo della superclasse.
	 */
	@Override
	public void createFields() {
		@SuppressWarnings("rawtypes")
		Field field;

		field = new TextField("Sigla");
		field.focus();
		addField(Lettera_.sigla, field);

		field = new TextField("Oggetto");
		field.setWidth("500px");
		addField(Lettera_.oggetto, field);

		TextArea aField = new TextArea("Testo");
		aField.setColumns(54);
		aField.setRows(24);
		addField(Lettera_.testo, aField);
		
		CheckBoxField htmlField = new CheckBoxField("Testo HTML");
		addField(Lettera_.html, htmlField);

		field = new TextField("Allegati (elenco nomi dei file separati da virgola)");
		field.setWidth("350px");
		addField(Lettera_.allegati, field);
		
	}// end of method

	/**
	 * Create the UI component.
	 * 
	 * Retrieve the fields from the map and place them in the UI. Implementazione di default nella superclasse. I campi
	 * vengono allineati verticalmente. Se si vuole aggiungere un campo, usare il metodo sovrascritto nella sottoclasse
	 * richiamando prima il metodo della superclasse. Se si vuole un layout completamente differente, implementare il
	 * metodo sovrascritto da solo.
	 */
	@Override
	protected Component createComponent() {
		AFormLayout layout = new AFormLayout();
		layout.setMargin(true);

		layout.addComponent(getField(Lettera_.sigla));
		layout.addComponent(getField(Lettera_.oggetto));
		layout.addComponent(getField(Lettera_.testo));
		layout.addComponent(getField(Lettera_.html));
		layout.addComponent(createRigaAllegati());

		return layout;
	}// end of method

	/**
	 * Create the UI component. Sulla stessa riga campo texEdit e bottone di selezione.
	 */
	private HorizontalLayout createRigaAllegati() {
		HorizontalLayout layoutAllegati = new HorizontalLayout();
		layoutAllegati.setSpacing(true);
		layoutAllegati.setDefaultComponentAlignment(Alignment.BOTTOM_LEFT);

		layoutAllegati.addComponent(getField(Lettera_.allegati));
		layoutAllegati.addComponent(creaBottoneAllegato());
		layoutAllegati.addComponent(creaBottoneGestioneAllegati());

		return layoutAllegati;
	}// end of method

	/**
	 * Crea il bottone di selezione.
	 */
	private Button creaBottoneAllegato() {
		Button bottone = null;

		bottone = new Button("Aggiungi allegato...", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				creaDialogoAllegato();
			}// end of method
		});// end of anonymous class

		return bottone;
	}// end of method
	
	/**
	 * Crea il bottone di selezione.
	 */
	private Button creaBottoneGestioneAllegati() {
		Button bottone = null;

		bottone = new Button("Gestione allegati...", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				new GestoreAllegati().show(getUI());
			}// end of method
		});// end of anonymous class

		return bottone;
	}// end of method


	/**
	 * Crea il dialogo di selezione.
	 */
	private void creaDialogoAllegato() {
		String titoloDialogo = "Elenco allegati disponibili";
		String infoDialogo = "Seleziona e conferma l'allegato";
		ArrayList<Allegato> allegati = AllegatoModulo.getList();
		PopDialog dialog = new PopDialog(titoloDialogo, infoDialogo, allegati, new PopDialog.PopDialogSelectedListener() {
			@Override
			public void onPopupSelected(Object valueSelected) {
				if ((valueSelected != null) && (valueSelected instanceof Allegato)) {
					regolaAllegato((Allegato)valueSelected);					
				}
			}// end of method
		});// end of anonymous class
		dialog.show(getUI());
	}// end of method


	/**
	 * Recupera il valore del popup selezionato e lo aggiunge alla property.
	 */
	private void regolaAllegato(Allegato valueSelected) {
		String oldText = "";

		TextField field = (TextField) getField(Lettera_.allegati);
		oldText = (String) field.getValue();
		if (!oldText.equals("")) {
			oldText += ", ";
		}// end of if cycle
		oldText += valueSelected.getName();
		field.setValue(oldText);
		
	}// end of method

}// end of class

