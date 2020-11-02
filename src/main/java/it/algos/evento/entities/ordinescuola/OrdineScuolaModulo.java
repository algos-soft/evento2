package it.algos.evento.entities.ordinescuola;


import it.algos.webbase.multiazienda.CompanyModule;

import javax.persistence.metamodel.Attribute;

/**
 * Created by alex on 30-05-2015.
 *
 */
public class OrdineScuolaModulo extends CompanyModule {


    /**
     * Costruttore senza parametri
     */
    public OrdineScuolaModulo() {
        super(OrdineScuola.class);
    }// end of constructor


    protected Attribute<?, ?>[] creaFieldsAll() {
        return new Attribute[] {OrdineScuola_.sigla, OrdineScuola_.descrizione };
    }


}// end of class
