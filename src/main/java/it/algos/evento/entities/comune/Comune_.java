package it.algos.evento.entities.comune;

import it.algos.evento.entities.evento.Evento;
import it.algos.webbase.multiazienda.CompanyEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Comune.class)
public class Comune_ extends CompanyEntity_ {
	public static volatile SingularAttribute<Evento, String> nome;
	public static volatile SingularAttribute<Evento, String> siglaProvincia;
}// end of entity class
