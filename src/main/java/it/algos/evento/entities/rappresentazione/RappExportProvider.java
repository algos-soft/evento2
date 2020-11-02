package it.algos.evento.entities.rappresentazione;

import it.algos.evento.entities.prenotazione.Prenotazione;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.importexport.BaseEntityExportProvider;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by alex on 31-05-2015.
 */
public class RappExportProvider extends BaseEntityExportProvider {

    ArrayList<Object> values;

    @Override
    public String[] getTitles() {
        String[] titles = new String[]{
                "Data",
                "Ora",
                "Evento",
                "Sala",
                "N.prenotazioni",
                "Interi",
                "Ridotti",
                "Disabili",
                "Accomp.",
                "Totale",
                "Capienza",
                "Disponibili",
        };
        return titles;
    }

    @Override
    public Object[] getExportValues(BaseEntity entity) {


        Rappresentazione rapp = (Rappresentazione)entity;

        // stringa per l'ora
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String time=dateFormat.format(rapp.getDataRappresentazione());

        // recupero le prenotazioni per avere i numeri
        Prenotazione[] prenotazioni = rapp.getPrenotazioni(false);
        int numInt=0, numRid=0, numDis=0, numAccomp=0;
        for(Prenotazione pren : prenotazioni){
            numInt+=pren.getNumInteri();
            numRid+=pren.getNumRidotti();
            numDis+=pren.getNumDisabili();
            numAccomp+=pren.getNumAccomp();
        }

        // final per usarli nelle lambda
        final int fNumInt=numInt;
        final int fNumRid=numRid;
        final int fNumDis=numDis;
        final int fNumAccomp=numAccomp;
        final int fNumTot=fNumInt+fNumRid+fNumDis+fNumAccomp;


        // uso variabile di istanza perché vi accede anche un altro metodo
        // ma la ricreo ogni volta qui
        values = new ArrayList<>();

        // che figata le Lambda!!
        // concateno le chiamate senza temere che falliscano
        // creo dei runnable e li invio al metodo add che gestisce l'errore
        add(() -> values.add(rapp.getDataRappresentazione()));
        add(() -> values.add(time));
        add(() -> values.add(rapp.getEvento().getTitolo()));
        add(() -> values.add(rapp.getSala().getNome()));
        add(() -> values.add(prenotazioni.length));
        add(() -> values.add(fNumInt));
        add(() -> values.add(fNumRid));
        add(() -> values.add(fNumDis));
        add(() -> values.add(fNumAccomp));
        add(() -> values.add(fNumTot));
        add(() -> values.add(rapp.getCapienza()));
        add(() -> values.add(rapp.getCapienza()-fNumTot));

        return values.toArray(new Object[0]);
    }

    /**
     * Esegue il runnable che aggiunge il valore
     * Se fallisce aggiunge un null
     * così non perdo la sincronizzazione
     */
    private void add(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            values.add(null);
        }
    }

}
