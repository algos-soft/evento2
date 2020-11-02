package it.algos.evento.entities.prenotazione;

import it.algos.evento.entities.insegnante.Insegnante;
import it.algos.evento.entities.rappresentazione.Rappresentazione;
import it.algos.evento.entities.rappresentazione.RappresentazioneModulo;
import it.algos.webbase.web.entity.EM;

import javax.persistence.EntityManager;
import java.util.ArrayList;

/**
 * Oggetto responsabile di verificare la fattibilità di uno spostamento
 * di prenotazioni e di eseguire l'operazione vera e propria.
 */
class PrenMover {
    private Prenotazione[] aPren;
    private ArrayList<String> warningRows = new ArrayList();
    private ArrayList<String> errorRows = new ArrayList();
    private ArrayList<String> infoRows = new ArrayList();
    private int totPersoneSpostate;
    private Rappresentazione destRapp;

    /**
     * Costruttore.
     *
     * @param aPren    l'array delle prenotazioni da spostare
     * @param destRapp la rappresentazione di destinazione
     */
    public PrenMover(Prenotazione[] aPren, Rappresentazione destRapp) {
        this.aPren = aPren;
        this.destRapp = destRapp;

        // processa le singole prenotazioni
        for (Prenotazione pren : aPren) {
            checkPren(pren);

            // incrementa il totale delle persone che verrebero spostate
            if (!pren.isCongelata()) {
                totPersoneSpostate += pren.getNumTotali();
            }
        }

        // processa l'operazione a livello generale
        checkOp();
    }

    /**
     * Processa una prenotazione e genera le righe di warning o di errore
     */
    private void checkPren(Prenotazione pren) {

        // controllo che la prenotazione non faccia già parte della rappresentazione destinazione
        if (pren.getRappresentazione().equals(destRapp)) {
            Insegnante ins = pren.getInsegnante();
            String s = "La prenotazione N. " + pren.getNumPrenotazione() + " di " + ins.getCognomeNome() + " è già nella rappresentazione selezionata.";
            errorRows.add(s);
        }


    }

    /**
     * Controlla l'operazione a livello generale
     */
    private void checkOp() {

        // controlla che numero di persone totali dopo lo spostamento
        // non ecceda la capienza della sala
        EntityManager em = EM.createEntityManager();
        int numPersoneDopo = RappresentazioneModulo.countPostiPrenotati(destRapp, em) + totPersoneSpostate;
        em.close();
        int capienza = destRapp.getCapienza();
        if (numPersoneDopo > capienza) {
            int diff = numPersoneDopo - capienza;
            String warn = "Attenzione: dopo lo spostamento, la capienza sarà superata";
            warn += " di " + diff + " posti (max=" + capienza + ", tot=" + numPersoneDopo + ")";
            warningRows.add(warn);
        }


    }


    /**
     * Ritorna il testo di preview degli warning in formato html
     *
     * @return il testo di warning
     */
    public String getHTMLWarnings() {
        String s = "";

        // righe di warning
        for (String row : warningRows) {
            if (!s.equals("")) {
                s += "<br>";
            }
            s += row;
        }


        return s;
    }


    /**
     * Ritorna il testo di preview degli errori in formato html
     *
     * @return il testo di errore
     */
    public String getHTMLErrors() {
        String s = "";

        // righe di errore
        for (String row : errorRows) {
            if (!s.equals("")) {
                s += "<br>";
            }
            s += row;
        }

        return s;
    }


    /**
     * @return true se l'operazione è effettuabile
     */
    boolean isEffettuabile() {
        return (errorRows.size() == 0);
    }

    /**
     * @return true se ci sono errori
     */
    boolean hasErrors() {
        return (errorRows.size() > 0);
    }

    /**
     * @return true se ci sono errori
     */
    boolean hasWarnings() {
        return (warningRows.size() > 0);
    }


}
