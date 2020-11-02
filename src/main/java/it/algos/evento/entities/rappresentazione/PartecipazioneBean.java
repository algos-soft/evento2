package it.algos.evento.entities.rappresentazione;

import it.algos.evento.entities.insegnante.Insegnante;
import it.algos.webbase.web.lib.LibDate;

import java.io.Serializable;
import java.util.Date;

/**
 * Bean rappresentativo di una Partecipazione.
 * Per accedere alle proprietà del bean si usano i getter.
 * Non è quindi necessario avere le relative variabili di istanza.
 * Per essere un bean, basta che ci sia il costruttore vuoto e i getter, e che sia serializable.
 * Ci possono essere costruttori aggiuntivi.
 * In questo caso i setter non servono perché i dati sono passati nel costruttore e non più modificati.
 * I nomi delle proprietà sono desunti dai nomi dei getter.
 * Quindi se da fuori richedo la proprietà "nome" verrà chiamato il metodo "getNome()" .
 * Created by alex on 17-06-2015.
 */
public class PartecipazioneBean  implements Serializable {

    private Insegnante insegnante;
    private Rappresentazione rappresentazione;

    public PartecipazioneBean() {
    }

    public PartecipazioneBean(Insegnante insegnante, Rappresentazione rappresentazione) {
        this.insegnante=insegnante;
        this.rappresentazione=rappresentazione;
    }


    public String getData() {
        Date date=rappresentazione.getDataRappresentazione();
        return LibDate.toStringDDMMYYYY(date);
    }

    public String getNomeEvento() {
        return rappresentazione.getEvento().getTitolo();
    }


    public String getEvento() {
        return rappresentazione.getEvento().toString();
    }

    public String getRappresentazione() {
        return rappresentazione.toString();
    }

    public String getNome() {
        return insegnante.getNome();
    }

    public String getCognome() {
        return insegnante.getCognome();
    }

    public String getEmail() {
        return  insegnante.getEmail();
    }

}
