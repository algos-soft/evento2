package it.algos.webbase.domain.ruolo;

import it.algos.webbase.web.entity.BaseEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Ruolo.class)
public class Ruolo_ extends BaseEntity_ {
	public static volatile SingularAttribute<Ruolo, String> nome;
}// end of entity class
