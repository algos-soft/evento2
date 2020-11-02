package it.algos.webbase.web.importexport;

import com.vaadin.data.Item;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.lib.LibBean;
import org.vaadin.addons.lazyquerycontainer.CompositeItem;

/**
 * Created by alex on 3-02-2016.
 * Redefines getExportValues() extracting the Entity from the Item.
 * Subclasses receive Entities instead of Items.
 */
public abstract class BaseEntityExportProvider extends ExportProvider {

    @Override
    public Object[] getExportValues(Item item) {
        BaseEntity entity = LibBean.entityFromItem(item);
        return getExportValues(entity);
    }

    public abstract Object[] getExportValues(BaseEntity entity);

}
