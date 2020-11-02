package it.algos.evento.entities.prenotazione;

import it.algos.evento.pref.CompanyPrefs;

/**
 * Created by alex on 24-12-2015.
 * Dialogo di conferma congelamento prenotazione.
 * Consente di procedere anche senza inviare email.
 *
 */
public class DialogoCongelaPrenotazione extends DialogoConfermaInvioManuale{

    public DialogoCongelaPrenotazione(Prenotazione pren, String titolo, String messaggio) {
        super(pren, titolo, messaggio);
        setConfirmButtonText("Congela");
    }


    @Override
    protected void syncUI() {
        super.syncUI();

        // qui si può confermare anche senza spedizioni
        if(getDestinatari().equals("")){
            getConfirmButton().setEnabled(true);
        }

    }

    @Override
    // se la spedizione mail al congelamento non è abilitata nelle preferenze spegne le spunte
    protected void populateUI(){

        super.populateUI();

        if(!CompanyPrefs.sendMailCongOpzione.getBool()) {
            sendRef.setValue(false);
            sendScuola.setValue(false);
        }else{

            // qui l'opzione generale è attiva, copia i flag per Referente e Scuola
            sendRef.setValue(CompanyPrefs.sendMailCongOpzioneRef.getBool());
            sendScuola.setValue(CompanyPrefs.sendMailCongOpzioneScuola.getBool());

            // controllo opzione No Privati
            if(getPrenotazione().isPrivato()){
                if(CompanyPrefs.sendMailCongOpzioneNP.getBool()){
                    sendRef.setValue(false);
                }
            }

        }

    }




}
