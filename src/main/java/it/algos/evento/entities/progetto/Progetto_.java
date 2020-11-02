package it.algos.evento.entities.progetto;

import it.algos.webbase.multiazienda.CompanyEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Progetto.class)
public class Progetto_ extends CompanyEntity_ {
	public static volatile SingularAttribute<Progetto, String> descrizione;
}// end of entity class
