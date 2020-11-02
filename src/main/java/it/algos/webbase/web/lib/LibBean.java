package it.algos.webbase.web.lib;

import java.lang.annotation.Annotation;

import javax.persistence.metamodel.Attribute;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Table;
import it.algos.webbase.web.entity.BaseEntity;
import org.vaadin.addons.lazyquerycontainer.CompositeItem;

public abstract class LibBean {

    public static Annotation[] getAnnotations(java.lang.reflect.Field field) {
        Annotation[] annotations = new Annotation[0];
        return annotations;
    }// end of static method

    public static Annotation[] getAnnotations(Object bean, Attribute attribute) {
        Annotation[] annotations = new Annotation[0];
        Class type = attribute.getJavaType();
        bean.getClass().getDeclaredAnnotations();
        java.lang.reflect.Field[] fields = bean.getClass().getFields();
        for (java.lang.reflect.Field field : fields) {
            field.getDeclaredAnnotations();
        }
        return annotations;
    }// end of static method

//	public static Annotation getAnnotation(Attribute attribute) {
//		Annotation annotation = null;
//		Annotation[] annotations = getAnnotations(attribute);
//		return annotation;
//	}

    /**
     * Create field from property name
     */
    public static Field createField(Attribute attribute) {
        DefaultFieldFactory factory = DefaultFieldFactory.get();
        Class javaType = attribute.getJavaType();
        Field field = factory.createFieldByPropertyType(javaType);
        return field;
    }// end of static method

    // /**
    // * Create field from attribute
    // */
    // public static Field createField(Attribute attribute){
    // return null;
    // }

    /**
     * Extracts a BeanItem from am Item
     *
     * @param item the item
     * @return the bean, or null if failed
     */
    public static BeanItem fromItem(Item item) {
        if (item instanceof BeanItem) {   // it is a BeanItem itself
            return (BeanItem) item;
        }
        if (item instanceof CompositeItem) {    //Used by LazyQueryContainer
            CompositeItem cItem = (CompositeItem) item;
            return (BeanItem) cItem.getItem("bean");
        }
        if (item instanceof EntityItem) {    //Used by JPAContainer
            EntityItem eItem = (EntityItem) item;
            return new BeanItem(eItem.getEntity());
        }

        return null;
    }


    /**
     * Extracts a Entity from am Item
     *
     * @param item the item
     * @return the entity, or null if failed
     */
    public static BaseEntity entityFromItem(Item item) {
        BaseEntity entity = null;
        BeanItem bi = fromItem(item);
        if (bi != null) {
            entity = (BaseEntity) bi.getBean();
        }
        return entity;
    }


    /**
     * Extracts an Entity from am itemId of a table
     *
     * @param source  the table
     * @param itemId the itemId
     * @return the entity, or null if failed
     */
    public static BaseEntity getEntity(Table source, Object itemId) {
        BaseEntity entity = null;
        BeanItem bean = null;
        Item item = source.getItem(itemId);

        if (item != null) {
            bean = fromItem(item);
        }// end of if cycle

        if (bean != null && bean.getBean() instanceof BaseEntity) {
            entity = (BaseEntity) bean.getBean();
        }// end of if cycle

        return entity;
    }// end of inner method



}// end of abstract static class
