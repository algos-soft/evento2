package it.algos.webbase.web.query;

import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

import com.vaadin.data.Container.Filter;
import it.algos.webbase.web.entity.BaseEntity;

/**
 * Query class typed on a specific Entity type.
 * <p>
 * Routes the queries to the AQuery class adding the type paramenetr and casting
 * the return type.<br>
 * Used to perform queries directly on the Entity class.<br>
 * 
 * Usage: 1) add a static variable to the entity class<br>
 * <code>public static EntityQuery<MyClass> query = new EntityQuery(MyClass.class);</code>
 * 2) perform the query like this:<br>
 * <code>List<MyClass> entities = MyClass.query.queryList(MyClass_.myField, aValue);</code>
 */
public class EntityQuery<T extends BaseEntity> {

	final Class<BaseEntity> type;

	public EntityQuery(Class<BaseEntity> type) {
		this.type = type;
	}

	public List<T> queryList(SingularAttribute attr, Object value) {
		return (List<T>) AQuery.queryList(type, attr, value);
	}

	public T queryFirst(SingularAttribute attr, Object value) {
		return (T) AQuery.queryFirst(type, attr, value);
	}
	
	public T queryOne(SingularAttribute attr, Object value) {
		return (T) AQuery.queryOne(type, attr, value);
	}
	
	public long getCount() {
		return AQuery.getCount(type);
	}

	public List<T> getList() {
		return (List<T>) AQuery.getListOld(type);
	}

	public List<T> getList(Filter... filters) {
		return (List<T>) AQuery.getListOld(type, filters);
	}

	public T getEntity(Filter... filters) {
		return (T) AQuery.getEntity(type, filters);
	}

}
