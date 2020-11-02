package it.algos.evento.entities.ordinescuola;

import it.algos.webbase.multiazienda.CompanyEntity_;

import javax.persistence.metamodel.SingularAttribute;

/**
 * Created by alex on 30-05-2015.
 */
public class OrdineScuola_ extends CompanyEntity_ {
    public static volatile SingularAttribute<OrdineScuola, String> sigla;
    public static volatile SingularAttribute<OrdineScuola, String> descrizione;
}
