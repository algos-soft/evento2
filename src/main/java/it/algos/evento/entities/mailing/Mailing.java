package it.algos.evento.entities.mailing;

import it.algos.evento.entities.lettera.Lettera;
import it.algos.webbase.multiazienda.CompanyEntity;
import it.algos.webbase.web.lib.LibDate;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashMap;

@Entity
public class Mailing extends CompanyEntity {

//    public static EventoEntityQuery<Mailing> query = new EventoEntityQuery(Mailing.class);


    @Size(min = 2, max = 80)
    private String titolo = "";

    @NotNull
    @OneToOne
    private Lettera lettera = null;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCreazione = LibDate.today();

    public Mailing() {
        this("", null, LibDate.today());
    }// end of constructor

    public Mailing(String titolo, Lettera lettera, Date dataCreazione) {
        super();
        this.setTitolo(titolo);
        this.setLettera(lettera);
        this.setDataCreazione(dataCreazione);
    }// end of constructor

    @Override
    public String toString() {
        return getTitolo();
    }// end of method


    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public Lettera getLettera() {
        return lettera;
    }

    public void setLettera(Lettera lettera) {
        this.lettera = lettera;
    }

    public Date getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(Date dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public String getOggetto() {
        String oggetto = "";
        Lettera lettera = this.getLettera();

        if (lettera != null) {
            oggetto = lettera.getOggetto();
        }// fine del blocco if

        return oggetto;
    }// end of method

    public String getTestOut(HashMap<String, String> mappaEscape) {
        String testo = "";
        Lettera lettera = this.getLettera();

        if (lettera != null) {
            testo = lettera.getTestOut(mappaEscape);
        }// fine del blocco if

        return testo;
    }// end of method

}// end of entity class
