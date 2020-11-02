package it.algos.webbase.web.lib;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Table;

import java.sql.Timestamp;

/**
 * Created by gac on 06 set 2015.
 * .
 */
public abstract class LibTable {

    /**
     * Recupera dalla tavola il valore di una cella di tipo Timestamp
     */
    public static long getTime(Table source, Object itemId, Object columnId) {
        long value = 0;
        Timestamp timestamp = getTimestamp(source, itemId, columnId);

        if (timestamp != null) {
            value = timestamp.getTime();
        }// fine del blocco if

        return value;
    }// end of static method

    /**
     * Recupera dalla tavola il valore di una cella di tipo Timestamp
     */
    public static Timestamp getTimestamp(Table source, Object itemId, Object columnId) {
        Timestamp value = null;
        Property property = getProperty(source, itemId, columnId);
        Object objValue;

        if (property != null) {
            objValue = property.getValue();

            if (objValue != null && objValue instanceof Timestamp) {
                value = ((Timestamp) objValue);
            }// fine del blocco if
        }// fine del blocco if

        return value;
    }// end of static method

    /**
     * Recupera la property di una cella della tavola
     */
    public static Property getProperty(Table source, Object itemId, Object columnId) {
        return source.getContainerProperty(itemId, columnId);
    }// end of static method


    /**
     * Recupera dalla tavola il valore di una cella di tipo booleano
     */
    public static boolean getBool(Table source, Object itemId, Object columnId) {
        boolean value = false;
        Object objValue;

        objValue = getPropValue(source, itemId, columnId);

        if (objValue != null && objValue instanceof Boolean) {
            value = ((Boolean) objValue);
        }// fine del blocco if

        return value;
    }// end of static method

    /**
     * Recupera dalla tavola il valore di una cella di tipo int
     */
    public static int getInt(Table source, Object itemId, Object columnId) {
        int value = 0;
        Object objValue;

        objValue = getPropValue(source, itemId, columnId);

        if (objValue != null && objValue instanceof Integer) {
            value = ((Integer) objValue);
        }// fine del blocco if

        return value;
    }// end of static method

    /**
     * Recupera dalla tavola il valore generico di una property
     *
     * @param source   the table
     * @param itemId   the itemId
     * @param propName the name of the property - must be String
     */
    public static Object getPropValue(Table source, Object itemId, Object propName) {
        Object value = null;
        Item item = null;
        Property prop = null;

        if (propName instanceof String) {
            item = source.getItem(itemId);
        }// end of if cycle

        if (item != null) {
            prop = item.getItemProperty(propName);
        }// end of if cycle

        if (prop != null) {
            value = prop.getValue();
        }// end of if cycle

        return value;
    }// end of inner method

}// end of abstract static class
