package it.algos.evento.entities.sala;

import it.algos.webbase.multiazienda.CompanyEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Sala.class)
public class Sala_ extends CompanyEntity_ {
	public static volatile SingularAttribute<Sala, String> nome;
	public static volatile SingularAttribute<Sala, Integer> capienza;
}// end of entity class
