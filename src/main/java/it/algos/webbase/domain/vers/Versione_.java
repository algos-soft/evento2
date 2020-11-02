package it.algos.webbase.domain.vers;
/**
 * Created by Gac on 17 lug 2015.
 * Using specific Templates (Entity, Domain, Modulo)
 */

import it.algos.webbase.web.entity.BaseEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.sql.Timestamp;
import java.util.Date;

@StaticMetamodel(Versione.class)
public class Versione_ extends BaseEntity_ {
	public static volatile SingularAttribute<Versione, Integer> ordine;
	public static volatile SingularAttribute<Versione, String> titolo;
	public static volatile SingularAttribute<Versione, String> descrizione;
	public static volatile SingularAttribute<Versione, Timestamp> timestamp;
}// end of entity class
