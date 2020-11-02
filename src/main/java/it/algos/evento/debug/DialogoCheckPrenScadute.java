package it.algos.evento.debug;

import it.algos.evento.daemons.PrenChecker;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.field.CheckBoxField;
import it.algos.webbase.web.field.DateField;
import it.algos.webbase.web.lib.LibDate;

import java.util.Date;

@SuppressWarnings("serial")
public class DialogoCheckPrenScadute extends ConfirmDialog {
	
	private DateField dField;
	CheckBoxField checkConfLevel1;
	CheckBoxField checkConfLevel2;
	CheckBoxField checkScadPaga;
	
	
	public DialogoCheckPrenScadute(Listener closeListener) {
		super(closeListener);
		setTitle("Debug controllo pren scadute");
		
		dField = new DateField("Controlla alla data:");
		dField.setValue(LibDate.today());
		addComponent(dField);
		
		checkConfLevel1 = new CheckBoxField("Controlla conferma prenotazione liv. 1");
		addComponent(checkConfLevel1);

		checkConfLevel2 = new CheckBoxField("Controlla conferma prenotazione liv. 2");
		addComponent(checkConfLevel2);
		
		checkScadPaga = new CheckBoxField("Controlla scadenza pagamento");
		addComponent(checkScadPaga);


	}

	@Override
	protected void onConfirm() {
		super.onConfirm();
		
		Date checkDate = dField.getValue();
		if (checkDate!=null) {
			PrenChecker checker = new PrenChecker(checkDate);
			checker.setCheckConfLevel1(checkConfLevel1.getValue());
			checker.setCheckConfLevel2(checkConfLevel2.getValue());
			checker.setCheckScadPagamento(checkScadPaga.getValue());
			checker.run();
		}

	}
	
	

}
