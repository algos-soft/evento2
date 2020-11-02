package it.algos.evento.config;

import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.ui.Field;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;
import it.algos.evento.entities.sala.Sala;
import it.algos.evento.pref.CompanyPrefs;
import it.algos.webbase.multiazienda.ERelatedComboField;
import it.algos.webbase.web.field.DecimalField;

import java.math.BigDecimal;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class EventiConfigComponent extends BaseConfigPanel {

	private static final String KEY_INTERI = "impInteri";
	private static final String KEY_RIDOTTI = "impRidotti";
	private static final String KEY_DISABILI = "impDisabili";
	private static final String KEY_ACCOMP = "impAccomp";
	private static final String KEY_GRUPPI = "impGruppi";

	private static final String KEY_ID_SALA = "idSala";
	private static final String KEY_TIPO_PREZZI = "tipoPrezzi";

	@SuppressWarnings("rawtypes")
	public EventiConfigComponent() {
		super();
		
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);


		// create and add fields and other components

		// optionGroup che pilota i valori del campo prezzoPerGruppi
		ArrayList<String> values = new ArrayList(2);
		values.add("S");
		values.add("G");
		OptionGroup gprezzo = new OptionGroup("Tipo di prezzo di default per i nuovi eventi",values);
		gprezzo.setItemCaption("S", "Prezzo per singoli");
		gprezzo.setItemCaption("G", "Prezzo per gruppi");
		layout.addComponent(gprezzo);

//		Boolean b = (Boolean)(getItem().getItemProperty(KEY_TIPO_PREZZI).getValue());
		Boolean b = CompanyPrefs.prezzoPerGruppi.getBool();
		if(b){
			gprezzo.setValue("G");
		}else{
			gprezzo.setValue("S");
		}
		gprezzo.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
				Object value = valueChangeEvent.getProperty().getValue();
				getItem().getItemProperty(KEY_TIPO_PREZZI).setValue((value == "G"));
			}
		});
		layout.addComponent(gprezzo);

		Field importoInteri = new DecimalField("Importo default interi");
		layout.addComponent(importoInteri);
		Field importoRidotti = new DecimalField("Importo default ridotti");
		layout.addComponent(importoRidotti);
		Field importoDisabili = new DecimalField("Importo default disabili");
		layout.addComponent(importoDisabili);
		Field importoAccomp = new DecimalField("Importo default accompagnatori");
		layout.addComponent(importoAccomp);
		Field importoGruppi = new DecimalField("Importo default gruppi");
		layout.addComponent(importoGruppi);

		Field salaDefault = new ERelatedComboField(Sala.class, "Sala di default");
		layout.addComponent(salaDefault);
		layout.addComponent(createButtonPanel());
		
		addComponent(layout);

		// bind fields to properties
		getGroup().bind(importoInteri, KEY_INTERI);
		getGroup().bind(importoRidotti, KEY_RIDOTTI);
		getGroup().bind(importoDisabili, KEY_DISABILI);
		getGroup().bind(importoAccomp, KEY_ACCOMP);
		getGroup().bind(importoAccomp, KEY_ACCOMP);
		getGroup().bind(importoGruppi, KEY_GRUPPI);

		getGroup().bind(salaDefault, KEY_ID_SALA);

	}

	public PrefSetItem createItem() {
		return new EventoSetItem();
	}
	
	@Override
	public String getTitle() {
		return "Configurazione Eventi e Rappresentazioni";
	}


	/**
	 * Item containing form data
	 */
	private class EventoSetItem extends PropertysetItem implements PrefSetItem {

		public EventoSetItem() {
			super();
			addItemProperty(KEY_INTERI, new ObjectProperty<BigDecimal>(CompanyPrefs.importoBaseInteri.getDecimal()));
			addItemProperty(KEY_RIDOTTI, new ObjectProperty<BigDecimal>(CompanyPrefs.importoBaseRidotti.getDecimal()));
			addItemProperty(KEY_DISABILI, new ObjectProperty<BigDecimal>(CompanyPrefs.importoBaseDisabili.getDecimal()));
			addItemProperty(KEY_ACCOMP, new ObjectProperty<BigDecimal>(CompanyPrefs.importoBaseAccomp.getDecimal()));
			addItemProperty(KEY_GRUPPI, new ObjectProperty<BigDecimal>(CompanyPrefs.importoBaseGruppi.getDecimal()));
			Sala sala = Sala.getDefault();
			if (sala == null) {
				sala = new Sala();
			}
			addItemProperty(KEY_ID_SALA, new ObjectProperty<Sala>(sala));
			addItemProperty(KEY_TIPO_PREZZI, new ObjectProperty<Boolean>(CompanyPrefs.prezzoPerGruppi.getBool()));

		}

		public void persist() {
			CompanyPrefs.importoBaseInteri.put(getItemProperty(KEY_INTERI).getValue());
			CompanyPrefs.importoBaseRidotti.put(getItemProperty(KEY_RIDOTTI).getValue());
			CompanyPrefs.importoBaseDisabili.put(getItemProperty(KEY_DISABILI).getValue());
			CompanyPrefs.importoBaseAccomp.put(getItemProperty(KEY_ACCOMP).getValue());
			CompanyPrefs.importoBaseGruppi.put(getItemProperty(KEY_GRUPPI).getValue());

			int idSala = 0;
			Object obj = getItemProperty(KEY_ID_SALA).getValue();
			if ((obj != null) && (obj instanceof Sala)) {
				idSala = ((Sala) obj).getId().intValue();
			}
			CompanyPrefs.idSalaDefault.put(idSala);

			CompanyPrefs.prezzoPerGruppi.put(getItemProperty(KEY_TIPO_PREZZI).getValue());


		}

	}

}
