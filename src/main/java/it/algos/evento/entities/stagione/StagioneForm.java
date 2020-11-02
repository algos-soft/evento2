package it.algos.evento.entities.stagione;

import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import it.algos.webbase.web.field.DateField;
import it.algos.webbase.web.field.TextField;
import it.algos.webbase.web.form.AFormLayout;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.module.ModulePop;

@SuppressWarnings("serial")
public class StagioneForm extends ModuleForm {

//	public StagioneForm(Item item) {
//		super(item);
//		doInit();
//	}

//	public StagioneForm(ModulePop modulo) {
//		super(modulo);
//		doInit();
//	}

	public StagioneForm(ModulePop modulo, Item item) {
		super(item, modulo);
		doInit();
	}
	
	private void doInit(){
		//setMargin(true);
	}

	@Override
	public void createFields() {
		@SuppressWarnings("rawtypes")
		Field field;

		field = new TextField("Sigla stagione");
		field.setWidth("120px");
		field.focus();
		addField(Stagione_.sigla, field);

		field = new DateField("Data inizio");
		addField(Stagione_.datainizio, field);

		field = new DateField("Data fine");
		addField(Stagione_.datafine, field);


	}

	protected Component createComponent() {
		FormLayout layout = new AFormLayout();
		layout.setMargin(true);

		layout.addComponent(getField(Stagione_.sigla));
		layout.addComponent(getField(Stagione_.datainizio));
		layout.addComponent(getField(Stagione_.datafine));
		return layout;
	}

}
