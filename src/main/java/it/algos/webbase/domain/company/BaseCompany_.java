package it.algos.webbase.domain.company;

import it.algos.webbase.web.entity.BaseEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

@StaticMetamodel(BaseCompany.class)
public class BaseCompany_ extends BaseEntity_ {
	public static volatile SingularAttribute<BaseCompany, String> companyCode;
	public static volatile SingularAttribute<BaseCompany, String> name;
	public static volatile SingularAttribute<BaseCompany, String> email;
	public static volatile SingularAttribute<BaseCompany, String> address1;
	public static volatile SingularAttribute<BaseCompany, String> address2;
	public static volatile SingularAttribute<BaseCompany, String> contact;
	public static volatile SingularAttribute<BaseCompany, String> note;
	public static volatile SingularAttribute<BaseCompany, String> contractType;
	public static volatile SingularAttribute<BaseCompany, Date> contractStart;
	public static volatile SingularAttribute<BaseCompany, Date> contractEnd;
}// end of entity class
