package it.algos.webbase.domain.pref;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import it.algos.webbase.multiazienda.CompanyEntity_;
import it.algos.webbase.multiazienda.ETable;
import it.algos.webbase.web.field.CheckBoxField;
import it.algos.webbase.web.lib.LibDate;
import it.algos.webbase.web.module.ModulePop;

import java.util.Date;

/**
 * Created by gac on 30 lug 2016.
 * .
 */
public class PrefTable extends ETable {

    // id della colonna generata "Valore"
    protected static final String COL_VALUE = "valore";

    /**
     * Costruttore
     *
     * @param module di riferimento (obbligatorio)
     */
    public PrefTable(ModulePop module) {
        super(module);
    }// end of constructor


    /**
     * Create additional columns
     * (add generated columns, nested properties...)
     * <p>
     * Override in the subclass
     */
    @Override
    protected void createAdditionalColumns() {
        addGeneratedColumn(COL_VALUE, new ValueColumnGenerator());
    }// end of method


    /**
     * Returns an array of the visible columns ids. Ids might be of type String
     * or Attribute.<br>
     * This implementations returns all the columns (no order).
     *
     * @return the list
     */
    @Override
    protected Object[] getDisplayColumns() {
        return new Object[]{
                CompanyEntity_.company,
                Pref_.codeCompanyUnico,
                Pref_.ordine,
                Pref_.code,
                Pref_.descrizione,
                Pref_.tipo,
                COL_VALUE
        };// end of array
    }// end of method


    /**
     * Colonna generata: valore.
     */
    private class ValueColumnGenerator implements ColumnGenerator {

        public Component generateCell(Table source, Object itemId, Object columnId) {
            PrefType tipo;
            Pref pref = Pref.find((long) itemId);

            if (pref != null) {
                tipo = pref.getTipo();
                if (tipo != null) {
                    return generateCell(tipo, pref.getValore());
                }// end of if cycle
            }// end of if cycle

            return null;
        }// end of method


        /**
         * Costruisce il componente adeguato.
         */
        private Component generateCell(PrefType tipo, Object value) {

            switch (tipo) {
                case string:
                    return generateString(value);
                case bool:
                    return generateBoolean(value);
                case integer:
                    return generateInteger(value);
                case date:
                    return generateDate(value);
                case email:
                    return generateEmail(value);
                case decimal:
                    return generateDecimal(value);
                case bytes:
                    return generateBytes(value);
                default: // caso non definito
                    return null;
            } // fine del blocco switch
        }// end of method


        /**
         * Componente specifico.
         */
        private Component generateString(Object value) {
            return new Label(value.toString());
        }// end of method

        /**
         * Componente specifico.
         */
        private Component generateBoolean(Object value) {
            return new CheckBoxField((boolean) value);
        }// end of method

        /**
         * Componente specifico.
         */
        private Component generateInteger(Object value) {
            return new Label(value.toString());
        }// end of method

        /**
         * Componente specifico.
         */
        private Component generateDate(Object value) {
            return new Label(LibDate.toStringDDMMYYYYHHMM((Date)value));
        }// end of method

        /**
         * Componente specifico.
         */
        private Component generateEmail(Object value) {
            return new Label(value.toString());
        }// end of method

        /**
         * Componente specifico.
         */
        private Component generateDecimal(Object value) {
            return null;
        }// end of method


        /**
         * Componente specifico.
         */
        private Component generateBytes(Object value) {
            return null;
        }// end of method


    }// end of inner class


}// end of class
