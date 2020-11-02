package it.algos.webbase.web.query;

import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;

/**
 * Created by gac on 03 feb 2016.
 * Wrapper con due liste sincronizzate:
 * a) nome del parametro
 * b) booleano per ordinamento ascendente (ASC) o discendente (DESC)
 */
public class SortProperty {

    private ArrayList<String> properties = new ArrayList<String>();
    private ArrayList<Boolean> ordinamenti = new ArrayList<Boolean>();


    /**
     * Costruttore base
     */
    public SortProperty() {
    }// fine del metodo costruttore


    /**
     * Costruttore
     *
     * @param properties nomi delle property (quantità variabile)
     */
    public SortProperty(String... properties) {
        doInit(properties);
    }// fine del metodo costruttore


    /**
     * Costruttore
     *
     * @param attribute   singola property
     * @param ordinamento booleano per (true) ordinamento ascendente (ASC) o (false) discendente (DESC)
     */
    public SortProperty(SingularAttribute attribute, boolean ordinamento) {
        this.add(attribute.getName(), ordinamento);
    }// fine del metodo costruttore


    /**
     * Costruttore
     *
     * @param attributes property (quantità variabile)
     */
    public SortProperty(SingularAttribute... attributes) {
        String[] properties;

        if (attributes != null && attributes.length > 0) {
            properties = new String[attributes.length];
            for (int k = 0; k < attributes.length; k++) {
                properties[k] = attributes[k].getName();
            }// end of for cycle

            doInit(properties);
        }// end of if cycle
    }// fine del metodo costruttore

    /**
     * Costruttore completo
     *
     * @param properties nomi (quantità variabile) delle property
     */
    public SortProperty(ArrayList<String> properties) {
        if (properties != null && properties.size() > 0) {
            for (String property : properties) {
                this.add(property);
            }// end of for cycle
        }// end of if cycle
    }// fine del metodo costruttore completo


    /**
     * Costruttore completo
     *
     * @param properties  nomi (quantità variabile) delle property
     * @param ordinamenti booleani per ordinamento ascendente (ASC) o discendente (DESC)
     */
    public SortProperty(ArrayList<String> properties, ArrayList<Boolean> ordinamenti) {
        this.properties = properties;
        this.ordinamenti = ordinamenti;
    }// fine del metodo costruttore completo


    /**
     * @param properties nomi (quantità variabile) delle property
     */
    private void doInit(String... properties) {
        if (properties != null && properties.length > 0) {
            for (String property : properties) {
                this.add(property);
            }// end of for cycle
        }// end of if cycle
    }// fine del metodo

    /**
     * @param attribute della property
     */
    public void add(SingularAttribute attribute) {
        this.add(attribute.getName());
    }// fine del metodo


    /**
     * @param property nome della property
     */
    public void add(String property) {
        this.add(property, true);
    }// fine del metodo


    /**
     * @param property    nome della property
     * @param ordinamento booleano per ordinamento ascendente (ASC) o discendente (DESC)
     */
    public void add(String property, boolean ordinamento) {
        this.properties.add(property);
        this.ordinamenti.add(ordinamento);
    }// fine del metodo


    public ArrayList<String> getListProperties() {
        return this.properties;
    }// end of getter method

    public ArrayList<Boolean> getListOrdinamenti() {
        return this.ordinamenti;
    }// end of getter method

    public String[] getProperties() {
        String[] proprieta = new String[properties.size()];

        for (int k = 0; k < this.properties.size(); k++) {
            proprieta[k] = this.properties.get(k);
        }// end of for cycle

        return proprieta;
    }// end of getter method

    public boolean[] getOrdinamenti() {
        boolean[] ordinamenti = new boolean[this.ordinamenti.size()];

        for (int k = 0; k < this.ordinamenti.size(); k++) {
            ordinamenti[k] = this.ordinamenti.get(k);
        }// end of for cycle

        return ordinamenti;
    }// end of getter method

} // fine della classe
