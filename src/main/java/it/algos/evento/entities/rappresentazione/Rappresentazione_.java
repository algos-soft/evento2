package it.algos.evento.entities.rappresentazione;

import it.algos.evento.entities.evento.Evento;
import it.algos.evento.entities.insegnante.Insegnante;
import it.algos.evento.entities.sala.Sala;
import it.algos.webbase.multiazienda.CompanyEntity_;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

@StaticMetamodel(Rappresentazione.class)
public class Rappresentazione_ extends CompanyEntity_ {
	public static volatile SingularAttribute<Rappresentazione, Evento> evento;
	public static volatile SingularAttribute<Rappresentazione, Sala> sala;
	public static volatile SingularAttribute<Rappresentazione, Integer> capienza;
	public static volatile SingularAttribute<Rappresentazione, Date> dataRappresentazione;
	public static volatile SingularAttribute<Rappresentazione, String> note;
	public static volatile ListAttribute<Rappresentazione, Insegnante> insegnanti;
	public static volatile SingularAttribute<Rappresentazione, String> pippo;
}// end of entity class
