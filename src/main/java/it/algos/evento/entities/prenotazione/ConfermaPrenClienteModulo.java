package it.algos.evento.entities.prenotazione;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ConfermaPrenClienteModulo extends VerticalLayout{

	Prenotazione pren;

	public ConfermaPrenClienteModulo(Prenotazione pren) {
		super();
		this.pren = pren;
		init();
	}
	
	private void init(){
		Component comp=new Label("Conferma prenotazione "+pren);
		addComponent(comp);
	}
}
