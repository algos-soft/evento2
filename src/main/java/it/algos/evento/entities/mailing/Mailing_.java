package it.algos.evento.entities.mailing;

import it.algos.evento.entities.destinatario.Destinatario;
import it.algos.evento.entities.lettera.Lettera;
import it.algos.webbase.multiazienda.CompanyEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

@StaticMetamodel(Mailing.class)
public class Mailing_ extends CompanyEntity_ {
    public static volatile SingularAttribute<Mailing, String> titolo;
    public static volatile SingularAttribute<Mailing, Lettera> lettera;
    public static volatile SingularAttribute<Destinatario, Date> dataCreazione;
}// end of entity class
