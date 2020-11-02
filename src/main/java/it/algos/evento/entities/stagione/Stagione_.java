package it.algos.evento.entities.stagione;

import it.algos.webbase.multiazienda.CompanyEntity_;

import javax.persistence.metamodel.SingularAttribute;
import java.util.Date;

/**
 * Created by Alex on 31/05/15.
 */
public class Stagione_ extends CompanyEntity_ {
    public static volatile SingularAttribute<Stagione, String> sigla;
    public static volatile SingularAttribute<Stagione, Boolean> corrente;
    public static volatile SingularAttribute<Stagione, Date> datainizio;
    public static volatile SingularAttribute<Stagione, Date> datafine;
}
