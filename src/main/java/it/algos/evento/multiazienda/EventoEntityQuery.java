package it.algos.evento.multiazienda;

import com.vaadin.data.Container.Filter;
import it.algos.webbase.multiazienda.CompanyEntity;
import it.algos.webbase.multiazienda.CompanyQuery;

import javax.persistence.metamodel.SingularAttribute;
import java.util.List;

/**
 * Query helper class typed on a specific EventoEntity type.
 * <p>
 * Routes the queries to the EQuery class adding the type paramenetr and casting
 * the return type.<br>
 * Used to perform queries directly on the Entity classes.<br>
 * 
 * Usage: 1) add a static variable to the entity class<br>
 * <code>public static EventoEntityQuery<MyClass> query = new EventoEntityQuery(MyClass.class);</code>
 * 2) perform the query like this:<br>
 * <code>List<MyClass> entities = MyClass.query.queryList(MyClass_.myField, aValue);</code>
 */
public class EventoEntityQuery <T extends CompanyEntity>{

	final Class<CompanyEntity> type;

	public EventoEntityQuery(Class<CompanyEntity> type) {
		this.type = type;
	}

	public List<T> queryList(SingularAttribute attr, Object value) {
		return (List<T>) CompanyQuery.getList(type, attr, value);
	}

	public T getFirstEntity(SingularAttribute attr, Object value) {
		return (T) CompanyQuery.getFirstEntity(type, attr, value);
	}
	
	public T queryOne(SingularAttribute attr, Object value) {
		return (T) CompanyQuery.getEntity(type, attr, value);
	}
	
	public long getCount() {
		return CompanyQuery.count(type);
	}

	public List<T> getList() {
		return (List<T>) CompanyQuery.getList(type);
	}

	public List<T> getList(Filter... filters) {
		return (List<T>) CompanyQuery.getList(type, filters);
	}

	public T getEntity(Filter... filters) {
		return (T) CompanyQuery.getFirstEntity(type, filters);
	}

}
