package it.algos.webbase.web.importexport;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.EntityItemProperty;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import it.algos.webbase.web.entity.EM;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.util.ArrayList;
import java.util.Set;

/**
 * Standard export provider for entities.
 * Uses property names as titles
 * Generates one export column for each property.
 * All exported values are converted toString().
 * Created by alex on 31-05-2015.
 */
public class EntityExportProvider extends ExportProvider {

    private Class clazz;

    /**
     * @param clazz the reference entity class
     */
    public EntityExportProvider(Class clazz) {
        super();
        this.clazz = clazz;
    }

    @Override
    public String[] getTitles() {
        ArrayList<String> titles = new ArrayList<>();
        Attribute[] attrs = getAttributes();
        for (Attribute attr : attrs) {
            titles.add(attr.getName());
        }
        return titles.toArray(new String[0]);
    }


    @Override
    public Object[] getExportValues(Item item) {
        Attribute[] attrs = getAttributes();
        Object[] objects = new Object[attrs.length];
        if (item != null) {
            int idx = 0;
            for (Attribute attr : attrs) {
                Property property=item.getItemProperty(attr.getName());
                //EntityItemProperty property = item.getItemProperty(attr.getName());
                Object value = property.getValue();
                String s = "";
                if(value!=null){
                    s=value.toString();
                }
                objects[idx] = s;
                idx++;
            }
        }
        return objects;
    }


    private Attribute[] getAttributes() {
        ArrayList<Attribute> attributes = new ArrayList();
        EntityManager manager = EM.createEntityManager();
        Metamodel mm = manager.getMetamodel();
        EntityType<?> type = mm.entity(clazz);
        Set attrs = type.getAttributes();
        for (Object obj : attrs) {
            Attribute attr = (Attribute) obj;
            attributes.add(attr);
        }
        manager.close();
        return attributes.toArray(new Attribute[0]);
    }


}
