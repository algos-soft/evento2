package it.algos.webbase.multiazienda;

import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.web.entity.BaseEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(CompanyEntity.class)
public abstract class CompanyEntity_ extends BaseEntity_{
	
	public static volatile SingularAttribute<CompanyEntity, BaseCompany> company;

}// end of class
