package it.algos.webbase.domain.utente;

import it.algos.webbase.web.entity.BaseEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Utente.class)
public class Utente_ extends BaseEntity_ {
	public static volatile SingularAttribute<Utente, String> nickname;
	public static volatile SingularAttribute<Utente, String> password;
	public static volatile SingularAttribute<Utente, Boolean> enabled;
}// end of entity class
