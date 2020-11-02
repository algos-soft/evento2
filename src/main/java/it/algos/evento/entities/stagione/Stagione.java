package it.algos.evento.entities.stagione;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import it.algos.webbase.multiazienda.CompanyEntity;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.web.entity.DefaultSort;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by Alex on 31/05/15.
 */
@Entity
@DefaultSort({"datainizio"})
public class Stagione extends CompanyEntity {

    @NotEmpty
    @Size(min = 2, max = 30)
    private String sigla = "";

    private boolean corrente;

    @Temporal(TemporalType.DATE)
    @NotNull
    private Date datainizio = new Date(); // giorno inizio stagione

    @Temporal(TemporalType.DATE)
    @NotNull
    private Date datafine = new Date(); // giorno fine stagione



    public Stagione() {
    }


    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public Date getDatainizio() {
        return datainizio;
    }

    public void setDatainizio(Date datainizio) {
        this.datainizio = datainizio;
    }

    public Date getDatafine() {
        return datafine;
    }

    public void setDatafine(Date datafine) {
        this.datafine = datafine;
    }

    public boolean isCorrente() {
        return corrente;
    }

    public void setCorrente(boolean corrente) {
        this.corrente = corrente;
    }

    @Override
    public String toString() {
        return sigla;
    }

    /**
     * Ritorna la stagione marcata come corrente,
     * o null se non trovata
     */
    public static Stagione getStagioneCorrente(){
        Stagione stagione=null;
        try {
//            String attrName=Stagione_.corrente.getName();
//            Container.Filter filter =  new Compare.Equal(attrName, true);
            //todo eliminato il filtro (gac)
            CompanyEntity e = CompanyQuery.getEntity(Stagione.class, Stagione_.corrente,true);
            if (e!=null){
                stagione=(Stagione)e;
            }
        }catch (Exception e){}
        return stagione;
    }
}
