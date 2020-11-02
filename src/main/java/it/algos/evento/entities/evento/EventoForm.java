package it.algos.evento.entities.evento;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.*;
import it.algos.evento.entities.progetto.Progetto;
import it.algos.evento.entities.progetto.ProgettoForm;
import it.algos.evento.entities.progetto.Progetto_;
import it.algos.evento.entities.stagione.Stagione;
import it.algos.evento.pref.CompanyPrefs;
import it.algos.webbase.multiazienda.ERelatedComboField;
import it.algos.webbase.web.field.*;
import it.algos.webbase.web.field.TextField;
import it.algos.webbase.web.form.AFormLayout;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.module.ModulePop;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class EventoForm extends ModuleForm {


	private FormLayout compPrezziSingoli;
	private FormLayout compPrezziGruppo;
	private HorizontalLayout placeholderPrezzi;


	public EventoForm(ModulePop modulo, Item item) {
		super(item, modulo);
		doInit();
	}

	private void doInit(){

		//setWidth("500px");

		// se nuovo record mette importi di default
		if (isNewRecord()) {

			Field fTipoPrezzo = getField(Evento_.prezzoPerGruppi);
			fTipoPrezzo.setValue(CompanyPrefs.prezzoPerGruppi.getBool());

			Field fIntero = getField(Evento_.importoIntero);
			fIntero.setValue(CompanyPrefs.importoBaseInteri.getDecimal());
			Field fRidotto = getField(Evento_.importoRidotto);
			fRidotto.setValue(CompanyPrefs.importoBaseRidotti.getDecimal());
			Field fDisabili = getField(Evento_.importoDisabili);
			fDisabili.setValue(CompanyPrefs.importoBaseDisabili.getDecimal());
			Field fAccomp = getField(Evento_.importoAccomp);
			fAccomp.setValue(CompanyPrefs.importoBaseAccomp.getDecimal());
			Field fGruppi = getField(Evento_.importoGruppo);
			fGruppi.setValue(CompanyPrefs.importoBaseGruppi.getDecimal());

		}

	}
	
	@Override
	public void createFields() {
		@SuppressWarnings("rawtypes")
		AbstractField field;
		TextField tfield;
		RelatedComboField combo;

		tfield = new TextField("Sigla");
		addField(Evento_.sigla, tfield);

		tfield = new TextField("Titolo");
		tfield.setWidth("300px");
		addField(Evento_.titolo, tfield);

		combo = new ERelatedComboField(Progetto.class, "Progetto");
		combo.setWidth("220px");
		combo.setNewItemHandler(ProgettoForm.class, Progetto_.descrizione);
		addField(Evento_.progetto, combo);

		combo = new ERelatedComboField(Stagione.class, "Stagione");
		combo.setWidth("220px");
		addField(Evento_.stagione, combo);

		field = new CheckBoxField("prezzopergruppi");
		addField(Evento_.prezzoPerGruppi, field);

		field = new DecimalField("Importo intero");
		addField(Evento_.importoIntero, field);

		field = new DecimalField("Importo ridotto");
		addField(Evento_.importoRidotto, field);
		
		field = new DecimalField("Importo disabili");
		addField(Evento_.importoDisabili, field);
		
		field = new DecimalField("Importo accompagnatori");
		addField(Evento_.importoAccomp, field);

		field = new DecimalField("Importo gruppi");
		addField(Evento_.importoGruppo, field);


	}

	protected Component createComponent() {
		AFormLayout layout = new AFormLayout();
		layout.setMargin(true);
		layout.addComponent(getField(Evento_.sigla));
		layout.addComponent(getField(Evento_.titolo));
		layout.addComponent(getField(Evento_.progetto));
		layout.addComponent(getField(Evento_.stagione));

		// optionGroup che pilota i valori del campo prezzoPerGruppi
		ArrayList<String> values = new ArrayList(2);
		values.add("S");
		values.add("G");
		OptionGroup gprezzo = new OptionGroup("Tipo di prezzo",values);
		gprezzo.setItemCaption("S", "Prezzo per singoli");
		gprezzo.setItemCaption("G", "Prezzo per gruppi");
		layout.addComponent(gprezzo);
		boolean gruppi;
		if(isNewRecord()){
			gruppi=CompanyPrefs.prezzoPerGruppi.getBool();
			getField(Evento_.prezzoPerGruppi).setValue(gruppi);
		}else{
			gruppi=!isPrezziSingoli();
		}
		if(gruppi){
			gprezzo.setValue("G");
		}else{
			gprezzo.setValue("S");
		}
		gprezzo.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
				Object value=valueChangeEvent.getProperty().getValue();
				getField(Evento_.prezzoPerGruppi).setValue(value=="G");
				syncCompPrezzi();
			}
		});

		creaComponentiPrezzo();
		layout.addComponent(placeholderPrezzi);
		syncCompPrezzi();

		return layout;
	}

	private void creaComponentiPrezzo(){
		placeholderPrezzi=new HorizontalLayout();
		placeholderPrezzi.setMargin(false);

		compPrezziSingoli=new FormLayout();
		compPrezziSingoli.setMargin(false);
		compPrezziSingoli.addComponent(getField(Evento_.importoIntero));
		compPrezziSingoli.addComponent(getField(Evento_.importoRidotto));
		compPrezziSingoli.addComponent(getField(Evento_.importoDisabili));
		compPrezziSingoli.addComponent(getField(Evento_.importoAccomp));

		compPrezziGruppo=new FormLayout();
		compPrezziGruppo.setMargin(false);
		compPrezziGruppo.addComponent(getField(Evento_.importoGruppo));

	}


	private void syncCompPrezzi(){
		placeholderPrezzi.removeAllComponents();
		if(isPrezziSingoli()){
			placeholderPrezzi.addComponent(compPrezziSingoli);
		}else{
			placeholderPrezzi.addComponent(compPrezziGruppo);
		}
	}


	private Evento getEvento(){
		return (Evento)getEntity();
	}

	private boolean isPrezziSingoli(){
		Object value = getField(Evento_.prezzoPerGruppi).getValue();
		return !(Boolean)value;
	}


}
