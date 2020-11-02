package it.algos.evento.entities.lettera;

import it.algos.webbase.multiazienda.CompanyEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Lettera.class)
public class Lettera_ extends CompanyEntity_ {
	public static volatile SingularAttribute<Lettera, String> sigla;
	public static volatile SingularAttribute<Lettera, String> oggetto;
	public static volatile SingularAttribute<Lettera, String> testo;
	public static volatile SingularAttribute<Lettera, String> allegati;
	public static volatile SingularAttribute<Lettera, Boolean> html;
}// end of entity class
