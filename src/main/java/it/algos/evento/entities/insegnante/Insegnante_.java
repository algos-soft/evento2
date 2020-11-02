package it.algos.evento.entities.insegnante;


import it.algos.webbase.multiazienda.CompanyEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Insegnante.class)
public class Insegnante_ extends CompanyEntity_ {
	public static volatile SingularAttribute<Insegnante, String> cognome;
	public static volatile SingularAttribute<Insegnante, String> nome;
	public static volatile SingularAttribute<Insegnante, String> titolo;
	public static volatile SingularAttribute<Insegnante, String> ordineScuola;
	public static volatile SingularAttribute<Insegnante, String> email;
	public static volatile SingularAttribute<Insegnante, String> telefono;
	public static volatile SingularAttribute<Insegnante, String> materie;
	public static volatile SingularAttribute<Insegnante, String> indirizzo1;
	public static volatile SingularAttribute<Insegnante, String> indirizzo2;
	public static volatile SingularAttribute<Insegnante, String> note;
	public static volatile SingularAttribute<Insegnante, Boolean> privato;
}// end of entity class
