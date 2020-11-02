package it.algos.evento.entities.lettera.allegati;

import com.vaadin.data.Item;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.module.ModulePop;

@SuppressWarnings("serial")
public class AllegatoForm extends ModuleForm {

//	public AllegatoForm(ModulePop modulo) {
//		super(modulo);
//	}// end of constructor

	public AllegatoForm(ModulePop modulo, Item item) {
		super(item, modulo);
	}
	
}
