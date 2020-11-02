package it.algos.evento.entities.mailing;

import it.algos.evento.entities.company.Company;
import it.algos.evento.entities.destinatario.Destinatario;
import it.algos.evento.entities.lettera.Lettera;
import it.algos.evento.entities.lettera.LetteraService;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.web.lib.LibDate;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("serial")
public class MailManager {

    //-- nome/titolo della spedizione
    private String titolo;

    //-- lettera di riferimento, da spedire
    private Lettera lettera;

    //-- lista (wrapper) destinatari
    private ArrayList<DestWrap> lista;


//    private Object[] ids;
//    private TextField titoloField;
//    private ArrayComboField letteraField;
//    private OptionGroup destinatariOptions;
//    private String itemReferente = "Referente della prenotazione";
//    private String itemScuola = "Mail della scuola";
//    private String itemEntrambi = "Entrambi gli indirizzi";
//    private String currentItem = "";


    public MailManager(MailWrap wrap) {

        if (wrap != null) {
            this.titolo = wrap.getTitolo();
            this.lettera = wrap.getLettera();
            this.lista = wrap.getLista();
        }// fine del blocco if

        this.gestione();
    }// end of constructor


    /**
     * Gestione <br>
     * <p>
     * Registra la mailing
     * Registra le spedizioni ai singoli destinatari
     * Spedisce effettivamente le mail
     * Conferma la spedizione nei singoli records
     */
    private void gestione() {
        Mailing mailing = null;

        mailing = this.registraMailing();
        if (mailing != null) {
            this.registraSpedisceConferma(mailing);
        }// fine del blocco if

    }// end of method

    /**
     * Registra la mailing
     * <p>
     * Creazione di 1 record di Mailing <br>
     * Viene aggiunta la data corrente
     */
    private Mailing registraMailing() {
        Mailing mailing = new Mailing();

        mailing.setTitolo(titolo);
        mailing.setLettera(lettera);
        mailing.save();

        return mailing;
    }// end of method


    /**
     * Registrazione
     * <p>
     * Creazione di n records di Destinatari <br>
     * Sono già stati eliminati gli indirizzi doppi
     * <p>
     * Spazzola la lista di destinatari e per ognuno:
     * Registra la spedizione
     * Spedisce effettivamente la mail
     * Conferma la spedizione nel flag del record
     */
    private void registraSpedisceConferma(Mailing mailing) {

        for (DestWrap wrap : lista) {
            regSpeConf(mailing, wrap);
        } // fine del ciclo for-each

    }// end of method


    /**
     * Registra la spedizione
     * Spedisce effettivamente la mail
     * Conferma la spedizione nel flag del record
     */
    private void regSpeConf(Mailing mailing, DestWrap wrap) {
        String indirizzo = "";
        Destinatario destinatario = null;
        boolean spedita = false;

        indirizzo = wrap.getIndirizzo();
        destinatario = registra(mailing, indirizzo);

        if (destinatario != null) {
            BaseCompany comp = mailing.getCompany();
            Company eComp = (Company)comp;
            spedita = spedisce(eComp, null, destinatario, wrap);
        }// fine del blocco if

        if (spedita) {
            conferma(destinatario);
        }// fine del blocco if

    }// end of method

    /**
     * Registra la spedizione (una per ogni destinatario)
     */
    private Destinatario registra(Mailing mailing, String indirizzo) {
        Destinatario destinatario = null;

        if (mailing != null && !indirizzo.equals("")) {
            destinatario = new Destinatario();
            destinatario.setMailing(mailing);
            destinatario.setIndirizzo(indirizzo);
            destinatario.save();
        }// fine del blocco if

        return destinatario;
    }// end of method

    /**
     * Spedisce la singola mail
     *
     * @param from         il mittente, se null o vuoto usa l'indirizzo della company corrente
     * @param destinatario il destinatario
     * @param wrap         il wrapper con indirizzo e mappa sostituzione
     */
    private boolean spedisce(BaseCompany company, String from, Destinatario destinatario, DestWrap wrap) {
        boolean spedita = false;
        String oggetto = "";
        String titolo = "";
        String dest = "";
        String testo = "";
        HashMap<String, String> mappa = wrap.getMappa();

        if (destinatario != null && wrap != null) {
            dest = destinatario.getIndirizzo();
            oggetto = destinatario.getOggetto();
            titolo = destinatario.getTitolo();
            testo = destinatario.getTesto(mappa);

            try { // prova ad eseguire il codice
                spedita = LetteraService.sendMail(company, from, dest, oggetto, testo);
            } catch (Exception e) { // intercetta l'errore
                e.printStackTrace();
            }// fine del blocco try-catch


        }// fine del blocco if

        return spedita;
    }// end of method


    /**
     * Conferma la singola spedizione (una per ogni destinatario)
     */
    private void conferma(Destinatario destinatario) {

        if (destinatario != null) {
            destinatario.setSpedita(true);
            destinatario.setDataSpedizione(LibDate.today());
            destinatario.save();
        }// fine del blocco if

    }// end of method

    private String getTitolo() {
        return titolo;
    }// end of method


    private Lettera getLettera() {
        return lettera;
    }// end of method


}// end of class
