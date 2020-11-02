package it.algos.evento.config;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.ui.Field;
import com.vaadin.ui.VerticalLayout;
import it.algos.evento.pref.CompanyPrefs;
import it.algos.webbase.web.field.IntegerField;

@SuppressWarnings("serial")
public class PrenConfigComponent extends BaseConfigPanel {

	private static final String KEY_NEXT_PREN = "nextpren";
	private static final String KEY_GG_CONF_PREN = "ggConfPren";
	private static final String KEY_GG_CONF_PAG = "ggConfPag";
	private static final String KEY_GG_PROLUNG_CONF = "ggProlungConf";
	private static final String KEY_GG_PROLUNG_PAGA = "ggProlungPaga";

	@SuppressWarnings("rawtypes")
	public PrenConfigComponent() {
		super();

		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		
		// create and add fields and other components
		Field nextNumPrenField = new IntegerField("Prossimo numero di prenotazione");
		layout.addComponent(nextNumPrenField);
		Field ggConfPrenField = new IntegerField("Conferma prenotazione: entro quanti giorni dalla richiesta");
		layout.addComponent(ggConfPrenField);
		Field ggConfPagaField = new IntegerField("Conferma pagamento: entro quanti giorni prima della rappresentazione");
		layout.addComponent(ggConfPagaField);
		Field ggProlungConfDopoSollecito = new IntegerField("Giorni prolungamento dopo sollecito conferma");
		layout.addComponent(ggProlungConfDopoSollecito);
		Field ggProlungPagaDopoSollecito = new IntegerField("Giorni prolungamento dopo sollecito pagamento");
		layout.addComponent(ggProlungPagaDopoSollecito);
		layout.addComponent(createButtonPanel());
		
		addComponent(layout);

		// bind fields to properties
		getGroup().bind(nextNumPrenField, KEY_NEXT_PREN);
		getGroup().bind(ggConfPrenField, KEY_GG_CONF_PREN);
		getGroup().bind(ggConfPagaField, KEY_GG_CONF_PAG);
		getGroup().bind(ggProlungConfDopoSollecito, KEY_GG_PROLUNG_CONF);
		getGroup().bind(ggProlungPagaDopoSollecito, KEY_GG_PROLUNG_PAGA);

	}

	public PrefSetItem createItem() {
		return new PrenSetItem();
	}

	@Override
	public String getTitle() {
		return "Configurazione Prenotazioni";
	}

	/**
	 * Item containing form data
	 */
	private class PrenSetItem extends PropertysetItem implements PrefSetItem {

		public PrenSetItem() {
			super();
			addItemProperty(KEY_NEXT_PREN, new ObjectProperty<Integer>(CompanyPrefs.nextNumPren.getInt()));
			addItemProperty(KEY_GG_CONF_PREN, new ObjectProperty<Integer>(CompanyPrefs.ggScadConfermaPrenotazione.getInt()));
			addItemProperty(KEY_GG_CONF_PAG, new ObjectProperty<Integer>(CompanyPrefs.ggScadConfermaPagamento.getInt()));
			addItemProperty(KEY_GG_PROLUNG_CONF,
					new ObjectProperty<Integer>(CompanyPrefs.ggProlungamentoConfDopoSollecito.getInt()));
			addItemProperty(KEY_GG_PROLUNG_PAGA,
					new ObjectProperty<Integer>(CompanyPrefs.ggProlungamentoPagamDopoSollecito.getInt()));
		}

		public void persist() {
			CompanyPrefs.nextNumPren.put(getItemProperty(KEY_NEXT_PREN).getValue());
			CompanyPrefs.ggScadConfermaPrenotazione.put(getItemProperty(KEY_GG_CONF_PREN).getValue());
			CompanyPrefs.ggScadConfermaPagamento.put(getItemProperty(KEY_GG_CONF_PAG).getValue());
			CompanyPrefs.ggProlungamentoConfDopoSollecito.put(getItemProperty(KEY_GG_PROLUNG_CONF).getValue());
			CompanyPrefs.ggProlungamentoPagamDopoSollecito.put(getItemProperty(KEY_GG_PROLUNG_PAGA).getValue());
		}

	}

}
