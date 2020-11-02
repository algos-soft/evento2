package it.algos.evento.entities.prenotazione;

import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import it.algos.evento.EventoBootStrap;
import it.algos.evento.entities.spedizione.Spedizione;
import it.algos.evento.pref.CompanyPrefs;
import it.algos.webbase.web.field.DateField;

import javax.persistence.EntityManager;
import java.util.Date;

/**
 * Created by alex on 4-11-2015.
 */
public class DialogoConfermaPrenotazione  extends DialogoConfermaInvioManuale {
    private DateField dateField;
    private EntityManager entityManager;
    private PrenotazioneConfermataListener pcListener;
    private boolean confermata=false;
    private Spedizione spedizione;

    public DialogoConfermaPrenotazione(Prenotazione pren, EntityManager em, Date dataConfermaDefault) {
        super(pren, "Conferma prenotazione", "");
        this.entityManager=em;

        setConfirmButtonText("Conferma");

        dateField=new DateField();
        dateField.setValue(dataConfermaDefault);

        // aggiunge una riga al GridLayout della superclasse
        Label label=new Label("Data conferma");
        getGridLayout().addComponent(label);
        getGridLayout().setComponentAlignment(label, Alignment.MIDDLE_LEFT);
        getGridLayout().addComponent(dateField);
        getGridLayout().setComponentAlignment(dateField, Alignment.MIDDLE_LEFT);

    }

    public Date getDataConferma(){
        return dateField.getValue();
    }

    @Override
    protected void onConfirm() {

        boolean cont = true;

        // check validators
        String error = checkDataValid();
        if (!error.equals("")) {
            Notification.show(null, error, Notification.Type.WARNING_MESSAGE);
            cont = false;
        }


        if(cont){
            super.onConfirm();

            // esegue l'operazione di conferma e l'invio mail in un thread separato
            new Thread(
                    () -> {

                        try {
                            spedizione=PrenotazioneModulo.doConfermaPrenotazione(
                                    getPrenotazione(),
                                    entityManager,
                                    getDataConferma(),
                                    EventoBootStrap.getUsername(),
                                    getDestinatari());

                            // se arriva qui ha confermato correttamente
                            confermata=true;

                        } catch (EmailFailedException e) {
                            PrenotazioneModulo.notifyEmailFailed(e);
                        }

                        // notifica il listener se registrato
                        if (pcListener != null) {
                            pcListener.prenotazioneConfermata();
                        }

                    }

            ).start();

        }

    }


    /**
     * Valida tutti i field validabili e accumula tutti i messaggi in una stringa
     * <p>
     * Se la stringa ritornata è vuota la validazione è passata
     */
    private String checkDataValid() {
        String string = "";

        if(getDataConferma()==null){
            string="Data di conferma non valida o mancante.";
        }

        return string;
    }

    @Override
    // se la spedizione mail alla conferma prenotazione
    // non è abilitata nelle preferenze, spegne le spunte
    protected void populateUI(){

        super.populateUI();

        if(!CompanyPrefs.sendMailConfPren.getBool()) {
            sendRef.setValue(false);
            sendScuola.setValue(false);
        }else{

            // qui l'opzione generale è attiva, copia i flag per Referente e Scuola
            sendRef.setValue(CompanyPrefs.sendMailConfPrenRef.getBool());
            sendScuola.setValue(CompanyPrefs.sendMailConfPrenScuola.getBool());

            // controllo opzione No Privati
            if(getPrenotazione().isPrivato()){
                if(CompanyPrefs.sendMailConfPrenNP.getBool()){
                    sendRef.setValue(false);
                }
            }

        }

    }


    @Override
    protected void syncUI() {
        super.syncUI();

        // questo dialogo si può confermare anche senza effettuare spedizioni
        if(getDestinatari().equals("")){
            getConfirmButton().setEnabled(true);
        }

    }

    /**
     * Mostra una notifica con l'esito della operazione
     */
    public void notificaEsito() {
        String msg = "";
        String mailDetails="";
        if (confermata) {
            msg = "Prenotazione confermata";
        }

        if (spedizione!=null){
            if(spedizione.isSpedita()) {
                mailDetails += "e-mail inviata";
            }else{
                mailDetails += "Invio e-mail fallito";
            }
        }

        Notification notification = new Notification(msg, mailDetails, Notification.Type.HUMANIZED_MESSAGE);
        notification.setDelayMsec(-1);
        notification.show(Page.getCurrent());

    }


    public void setPrenotazioneConfermataListener(PrenotazioneConfermataListener l){
        this.pcListener =l;
    }

    public interface PrenotazioneConfermataListener {
        void prenotazioneConfermata();
    }

}
