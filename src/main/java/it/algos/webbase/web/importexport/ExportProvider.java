package it.algos.webbase.web.importexport;

import com.vaadin.data.Item;

/**
 * Provides the column titles and the export values.
 * May return more than one title and value for one source column
 * because a single source column can be splitted to multiple export columns.
 * e.g. you can have a Person object as source, and the coulmns exported
 * might be "name", "age" and "address".
 * This class must be subclassed and the subclass should implement getExportValues()
 * Created by alex on 31-05-2015.
 */
public abstract class ExportProvider {

    /**
     * @return the column titles
     * (the order must match getExportValues)
     */
    public abstract String[] getTitles();


    /**
     * @return the indexes of the columns to totalize
     * (0-based array)
     */
    public int[] getTotalizableColumns(){
        return new int[0];
    }


    /**
     * Returns the export values for one source item.
     * @param item the source item
     * @return the values to be exported
     */
    public abstract Object[] getExportValues(Item item);


}
