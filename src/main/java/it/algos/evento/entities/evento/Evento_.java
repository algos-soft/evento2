package it.algos.evento.entities.evento;

import it.algos.evento.entities.progetto.Progetto;
import it.algos.evento.entities.stagione.Stagione;
import it.algos.webbase.multiazienda.CompanyEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.math.BigDecimal;

@StaticMetamodel(Evento.class)
public class Evento_ extends CompanyEntity_ {
	public static volatile SingularAttribute<Evento, Progetto> progetto;
	public static volatile SingularAttribute<Evento, Stagione> stagione;
	public static volatile SingularAttribute<Evento, String> sigla;
	public static volatile SingularAttribute<Evento, String> titolo;
	public static volatile SingularAttribute<Evento, Boolean> prezzoPerGruppi;
	public static volatile SingularAttribute<Evento, BigDecimal> importoIntero;
	public static volatile SingularAttribute<Evento, BigDecimal> importoRidotto;
	public static volatile SingularAttribute<Evento, BigDecimal> importoDisabili;
	public static volatile SingularAttribute<Evento, BigDecimal> importoAccomp;
	public static volatile SingularAttribute<Evento, BigDecimal> importoGruppo;
}// end of entity class
