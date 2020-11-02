package it.algos.evento.pref;

import it.algos.webbase.multiazienda.CompanyEntity_;

import javax.persistence.metamodel.SingularAttribute;

public class PrefEventoEntity_ extends CompanyEntity_ {
	public static volatile SingularAttribute<PrefEventoEntity, String> code;
	public static volatile SingularAttribute<PrefEventoEntity, byte[]> value;
}// end of entity class
