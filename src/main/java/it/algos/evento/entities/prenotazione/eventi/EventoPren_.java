package it.algos.evento.entities.prenotazione.eventi;

import it.algos.evento.entities.prenotazione.Prenotazione;
import it.algos.webbase.multiazienda.CompanyEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

@StaticMetamodel(EventoPren.class)
public class EventoPren_ extends CompanyEntity_ {
	public static volatile SingularAttribute<EventoPren, Prenotazione> prenotazione;
	public static volatile SingularAttribute<EventoPren, Integer> tipo;
	public static volatile SingularAttribute<EventoPren, Date> timestamp;
	public static volatile SingularAttribute<EventoPren, String> user;
	public static volatile SingularAttribute<EventoPren, String> dettagli;
	public static volatile SingularAttribute<EventoPren, Boolean> invioEmail;
	public static volatile SingularAttribute<EventoPren, Boolean> emailInviata;
}// end of entity class
