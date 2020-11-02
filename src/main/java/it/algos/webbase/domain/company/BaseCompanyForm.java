package it.algos.webbase.domain.company;

import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Layout;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.field.EmailField;
import it.algos.webbase.web.field.TextField;
import it.algos.webbase.web.form.AFormLayout;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.module.ModulePop;

@SuppressWarnings("serial")
public class BaseCompanyForm extends ModuleForm {

//	public CompanyForm(ModulePop modulo) {
//		super(modulo);
//		doInit();
//	}// end of constructor

	public BaseCompanyForm(ModulePop modulo, Item item) {
		super(item, modulo);
		doInit();
	}// end of constructor

	private void doInit(){
		setWidth("500px");
	}
	
	@Override
	public void createFields() {
		@SuppressWarnings("rawtypes")
		Field field;

		field = new TextField("Id");
		addField(BaseCompany_.id, field);

		field = new TextField("Nome");
		addField(BaseCompany_.name, field);
		
		field = new EmailField("Email");
		addField(BaseCompany_.email, field);
		
		field = new TextField("Codice azienda");
		addField(BaseCompany_.companyCode, field);
		
		field = new TextField("Contatto");
		addField(BaseCompany_.contact, field);
		
		field = new TextField("Indirizzo1");
		addField(BaseCompany_.address1, field);
		
		field = new TextField("Indirizzo2");
		addField(BaseCompany_.address2, field);

	}

	protected Component createComponent() {
		AFormLayout layout = new AFormLayout();
		layout.setMargin(true);
		addComponents(layout);
		return layout;
	}// end of method


	protected void addComponents(Layout layout){
		Field<?> field=getField(BaseCompany_.id);
		field.setReadOnly(true);
		field.setEnabled(false);
		layout.addComponent(field);
		layout.addComponent(getField(BaseCompany_.name));
		layout.addComponent(getField(BaseCompany_.email));
		layout.addComponent(getField(BaseCompany_.companyCode));
		layout.addComponent(getField(BaseCompany_.contact));
		layout.addComponent(getField(BaseCompany_.address1));
		layout.addComponent(getField(BaseCompany_.address2));
	}



	private BaseCompany getCompany(){
		BaseEntity entity = getEntity();
		return (BaseCompany)entity;
	}

}
