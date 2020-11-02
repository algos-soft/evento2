package it.algos.evento.entities.mailing;

import it.algos.evento.entities.lettera.Lettera;

import java.util.ArrayList;

/**
 * Created by gac on 14 lug 2015.
 * <p>
 * Wrapper che raccoglie i dati necessari per effettuare un mailing
 * <p>
 * Contiene un titolo/nome del mailing
 * Contiene un riferimento alla Lettera da inviare
 * Contiene una lista di destinatari. Questa è composta da wrapper di tipo DestWrap (interno):
 * -   un indirizzo e-mail
 * -   una mappa di sostituzione (chiave-valore)
 * <p>
 * La lista dei destinatari viene ripulita dai valori doppi delle e-mail (si tiene il primo)
 */
public class MailWrap {

    //-- nome/titolo della spedizione
    private String titolo = "";

    //-- lettera di riferimento, da spedire
    private Lettera lettera = null;

    //-- lista (wrapper) destinatari
    private ArrayList<DestWrap> lista = null;

    //-- flag di controllo
    private boolean destinatariUnici = false;

    /**
     * Costruttore completo
     */
    public MailWrap(String titolo, Lettera lettera, ArrayList<DestWrap> lista) {
        this.titolo = titolo;
        this.lettera = lettera;
        this.lista = lista;

        this.inizializza();
    }// end of constructor

    /**
     * Metodo iniziale
     */
    private void inizializza() {
        this.regolazioneLista();
    }// end of method

    /**
     * Elaborazione per l'unicità della lista
     * Elimina gli elementi che hanno lo stesso valore di e-mail
     */
    private void regolazioneLista() {
        ArrayList<DestWrap> listaIn = this.getLista();
        ArrayList<DestWrap> listaOut = new ArrayList<DestWrap>();
        ArrayList<String> listaIndirizzi = new ArrayList<String>();
        DestWrap wrap;
        String indirizzo;

        if (listaIn != null && listaIn.size() > 0) {

            for (int k = 0; k < listaIn.size(); k++) {
                wrap = listaIn.get(k);
                indirizzo = wrap.getIndirizzo();
                if (!listaIndirizzi.contains(indirizzo)) {
                    listaIndirizzi.add(indirizzo);
                    listaOut.add(wrap);
                }// fine del blocco if
            } // fine del ciclo for

            this.lista = listaOut;
            destinatariUnici = true;
        }// fine del blocco if

    }// end of method

    public String getTitolo() {
        return titolo;
    }

    public Lettera getLettera() {
        return lettera;
    }

    public ArrayList<DestWrap> getLista() {
        return lista;
    }

    public boolean isDestinatariUnici() {
        return destinatariUnici;
    }

}// end of wrapper class
