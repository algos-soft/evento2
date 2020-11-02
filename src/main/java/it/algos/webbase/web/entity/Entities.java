package it.algos.webbase.web.entity;

import com.vaadin.data.Container;
import org.vaadin.addons.lazyquerycontainer.LazyQueryContainer;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import java.util.Collection;
import java.util.Set;

/**
 * Collection of static utility methods operating on entities
 * Created by alex on 19/01/16.
 */
public class Entities {

    /**
     * Add all the properties of a given entity class to a given container.
     * If a property with the same name is already present in the container, it is not added again.
     *
     * @param cont        the container
     * @param entityClass the entity class
     */
    public static void addPropertiesToContainer(Container cont, Class entityClass) {
        EntityType<?> type = EM.getEntityType(entityClass);
        Set<?> attributes = type.getAttributes();
        Attribute<?, ?> attribute;

        Collection coll = cont.getContainerPropertyIds();

        for (Object ogg : attributes) {
            if (ogg instanceof Attribute<?, ?>) {
                attribute = (Attribute<?, ?>) ogg;
                String name = attribute.getName();

                if (!coll.contains(name)) {
                    Class clazz = attribute.getJavaType();
                    Object defaultValue = null;
                    try {
                        defaultValue = clazz.newInstance();
                    } catch (Exception e) {
                    }

                    // specific handling for LazyQueryContainer
                    if (cont instanceof LazyQueryContainer) {
                        LazyQueryContainer lqCont = (LazyQueryContainer) cont;
                        lqCont.addContainerProperty(name, clazz, defaultValue, true, true);
                    } else {
                        cont.addContainerProperty(name, clazz, defaultValue);
                    }

                }

            }
        }

    }
}
