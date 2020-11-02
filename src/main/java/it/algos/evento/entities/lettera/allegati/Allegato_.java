package it.algos.evento.entities.lettera.allegati;

import it.algos.webbase.multiazienda.CompanyEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Allegato.class)
public class Allegato_ extends CompanyEntity_ {
	public static volatile SingularAttribute<Allegato_, String> name;
	public static volatile SingularAttribute<Allegato_, byte[]> content;
	public static volatile SingularAttribute<Allegato_, String> mimeType;
	public static volatile SingularAttribute<Allegato_, Long> bytes;
}
