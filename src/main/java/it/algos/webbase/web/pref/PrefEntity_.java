package it.algos.webbase.web.pref;

import it.algos.webbase.web.entity.BaseEntity_;

import javax.persistence.metamodel.SingularAttribute;

public class PrefEntity_ extends BaseEntity_ {
	public static volatile SingularAttribute<PrefEntity, String> code;
	public static volatile SingularAttribute<PrefEntity, byte[]> value;
}// end of entity class
