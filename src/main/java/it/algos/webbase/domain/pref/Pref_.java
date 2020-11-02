package it.algos.webbase.domain.pref;
/**
 * Created by Gac on 17 lug 2015.
 * Using specific Templates (Entity, Domain, Modulo)
 */

import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.web.entity.BaseEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

@StaticMetamodel(Pref.class)
public class Pref_ extends BaseEntity_ {
    public static volatile SingularAttribute<Pref, String> code;
    public static volatile SingularAttribute<Pref, PrefType> tipo;
    public static volatile SingularAttribute<Pref, byte[]> value;
    public static volatile SingularAttribute<Pref, BaseCompany> company;
    public static volatile SingularAttribute<Pref, String> codeCompanyUnico;

    public static volatile SingularAttribute<Pref, String> descrizione;
    public static volatile SingularAttribute<Pref, Integer> ordine;

    @Deprecated
    public static volatile SingularAttribute<Pref, String> classe;
    @Deprecated
    public static volatile SingularAttribute<TypePref, String> type;
    @Deprecated
    public static volatile SingularAttribute<Pref, Date> dateCreated;
    @Deprecated
    public static volatile SingularAttribute<Pref, Date> lastUpdated;
    @Deprecated
    public static volatile SingularAttribute<Pref, String> stringa;
    @Deprecated
    public static volatile SingularAttribute<Pref, Boolean> bool;
    @Deprecated
    public static volatile SingularAttribute<Pref, Integer> intero;
    @Deprecated
    public static volatile SingularAttribute<Pref, Long> lungo;
    @Deprecated
    public static volatile SingularAttribute<Pref, Float> reale;
    @Deprecated
    public static volatile SingularAttribute<Pref, Double> doppio;
    @Deprecated
    public static volatile SingularAttribute<Pref, BigDecimal> decimale;
    @Deprecated
    public static volatile SingularAttribute<Pref, Date> data;
    @Deprecated
    public static volatile SingularAttribute<Pref, ArrayList> lista;
    @Deprecated
    public static volatile SingularAttribute<Pref, String> testo;
}// end of entity class

//TypePref type;

//String code;
//String descrizione;
//Date dateCreated;
//Date lastUpdated;
//String stringa;      // VARCHAR(255)

//Boolean bool;     // BIT(1)
//Integer intero;    // INTEGER
//Long lungo;   // BIGINT(20)
//Float reale;  // FLOAT
//Double doppio; // DOUBLE
//BigDecimal decimale;// DECIMAL(19,2)
//Date data;// DATETIME
//String testo;// LONGTEXT
