package it.algos.evento.entities.scuola;

import it.algos.evento.entities.comune.Comune;
import it.algos.webbase.multiazienda.CompanyEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Scuola.class)
public class Scuola_ extends CompanyEntity_ {
	public static volatile SingularAttribute<Scuola, String> sigla;
	public static volatile SingularAttribute<Scuola, String> nome;
	public static volatile SingularAttribute<Scuola, Integer> ordine;
	public static volatile SingularAttribute<Scuola, String> tipo;
	public static volatile SingularAttribute<Scuola, Comune> comune;
	public static volatile SingularAttribute<Scuola, String> indirizzo;
	public static volatile SingularAttribute<Scuola, String> cap;
	public static volatile SingularAttribute<Scuola, String> telefono;
	public static volatile SingularAttribute<Scuola, String> fax;
	public static volatile SingularAttribute<Scuola, String> email;
	public static volatile SingularAttribute<Scuola, String> note;
}// end of entity class
