package it.algos.evento.entities.modopagamento;

import it.algos.webbase.multiazienda.CompanyEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(ModoPagamento.class)
public class ModoPagamento_ extends CompanyEntity_ {
	public static volatile SingularAttribute<ModoPagamento_, String> sigla;
	public static volatile SingularAttribute<ModoPagamento_, String> descrizione;
}// end of entity class
