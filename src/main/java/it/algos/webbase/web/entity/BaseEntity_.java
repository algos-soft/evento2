package it.algos.webbase.web.entity;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(BaseEntity.class)
public abstract class BaseEntity_ {
	public static volatile SingularAttribute<BaseEntity, Long> id;
}// end of class
