package it.algos.evento.entities.lettera;

import it.algos.evento.entities.prenotazione.Prenotazione;
import it.algos.evento.entities.scuola.Scuola;
import it.algos.evento.pref.CompanyPrefs;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.web.entity.BaseEntity;

import java.util.ArrayList;

public enum ModelliLettere {

    istruzioniPrenotazione("info_prenotazione", "Riepilogo opzione telefonica", CompanyPrefs.sendMailInfoPren, CompanyPrefs.sendMailInfoPrenRef, CompanyPrefs.sendMailInfoPrenScuola, CompanyPrefs.sendMailInfoPrenNP),

    confermaPrenotazione("conferma_prenotazione", "Conferma prenotazione", CompanyPrefs.sendMailConfPren, CompanyPrefs.sendMailConfPrenRef, CompanyPrefs.sendMailConfPrenScuola, CompanyPrefs.sendMailConfPrenNP),

    memoScadPagamento("memo_scadenza_pagamento", "Promemoria scadenza di pagamento", CompanyPrefs.sendMailScadPaga, CompanyPrefs.sendMailScadPagaRef, CompanyPrefs.sendMailScadPagaScuola, CompanyPrefs.sendMailScadPagaNP),

    confermaPagamento("conferma_pagamento", "Conferma del pagamento", CompanyPrefs.sendMailConfPaga, CompanyPrefs.sendMailConfPagaRef, CompanyPrefs.sendMailConfPagaScuola, CompanyPrefs.sendMailConfPagaNP),

    registrazionePagamento("registrazione_pagamento", "Registrazione del pagamento", CompanyPrefs.sendMailRegisPaga, CompanyPrefs.sendMailRegisPagaRef, CompanyPrefs.sendMailRegisPagaScuola, CompanyPrefs.sendMailRegisPagaNP),

    memoScadPrenotazione("memo_scadenza_prenotazione", "Promemoria invio scheda di prenotazione", CompanyPrefs.sendMailScadPren, CompanyPrefs.sendMailScadPrenRef, CompanyPrefs.sendMailScadPrenScuola, CompanyPrefs.sendMailScadPrenNP),

    congelamentoOpzione("congelamento_opzione", "Avviso di congelamento dell’opzione telefonica", CompanyPrefs.sendMailCongOpzione, CompanyPrefs.sendMailCongOpzioneRef, CompanyPrefs.sendMailCongOpzioneScuola, CompanyPrefs.sendMailCongOpzioneNP),

    attestatoPartecipazione("attestato_partecipazione", "Attestato di partecipazione", null, null, null, null),

    demoSostituzioni("demo_sostituzioni", "Demo sostituzioni supportate", null, null, null, null);

    private String dbCode;
    private String oggettoDefault; // usato per creare demo data
    private CompanyPrefs prefSend;    // riferimento alla preferenza che indica se questo tipo di mail va inviata in generale
    private CompanyPrefs prefReferente;    // riferimento alla preferenza che indica se va inviata al referente
    private CompanyPrefs prefScuola;    // riferimento alla preferenza che indica se va inviata alla scuola
    private CompanyPrefs prefNoPrivati;    // riferimento alla preferenza che indica di non inviare ai privati


    ModelliLettere(String dbCode, String oggettoDefault, CompanyPrefs prefSend, CompanyPrefs prefReferente, CompanyPrefs prefScuola, CompanyPrefs prefNoPrivati) {
        this.dbCode = dbCode;
        this.oggettoDefault = oggettoDefault;
        this.prefSend = prefSend;
        this.prefReferente = prefReferente;
        this.prefScuola = prefScuola;
        this.prefNoPrivati = prefNoPrivati;
    }// end of constructor

    public String getDbCode() {
        return dbCode;
    }// end of method

    public String getOggettoDefault() {
        return oggettoDefault;
    }

    /**
     * Ritorna l'oggetto della lettera
     */
    public String getOggetto() {
        String stringa = "";
        BaseEntity entity = CompanyQuery.getFirstEntity(Lettera.class, Lettera_.sigla, getDbCode());
        if (entity != null) {
            Lettera lett = (Lettera) entity;
            stringa = lett.getOggetto();
        }// end of if cycle

        return stringa;
    }// end of method

    /**
     * Ritorna il testo della lettera
     */
    public String getTesto() {
        String stringa = "";
        BaseEntity entity = CompanyQuery.getFirstEntity(Lettera.class, Lettera_.sigla, getDbCode());
        if (entity != null) {
            Lettera lett = (Lettera) entity;
            stringa = lett.getTesto();
        }// end of if cycle

        return stringa;
    }// end of method

    /**
     * Ritorna la lettera
     */
    public Lettera getLettera() {
        Lettera lettera = null;
        BaseEntity entity = CompanyQuery.getFirstEntity(Lettera.class, Lettera_.sigla, getDbCode());
        if (entity != null) {
            lettera = (Lettera) entity;
        }// end of if cycle

        return lettera;
    }// end of method



    /**
     * lista di tutti gli elementi dell'Enumeration
     */
    public static ArrayList<ModelliLettere> getAll() {
        ArrayList<ModelliLettere> lista = new ArrayList<ModelliLettere>();

        for (ModelliLettere modLettera : values()) {
            lista.add(modLettera);
        } // fine del ciclo for

        return lista;
    }// fine del metodo

    /**
     * lista di tutti i codici (database) dell'Enumeration
     */
    public static ArrayList<String> getAllDbCode() {
        ArrayList<String> lista = new ArrayList<String>();

        for (ModelliLettere modLettera : values()) {
            lista.add(modLettera.getDbCode());
        } // fine del ciclo for

        return lista;
    }// fine del metodo

    /**
     * elenco di tutti i codici (database) dell'Enumeration uno per riga
     */
    public static String getElencoDbCode() {
        String elenco = "";
        ArrayList<String> lista = getAllDbCode();
        String aCapo = "\n";

        if (lista != null) {
            for (String dbCode : lista) {
                elenco += dbCode;
                elenco += aCapo;
            }// end of for cycle
            elenco = elenco.trim();
        }// end of if cycle

        return elenco;
    }// fine del metodo


    /**
     * Controlla se deve inviare alla scuola di una data prenotazione
     */
    public boolean isSendScuola(Prenotazione pren) {
        boolean send = false;

        if (prefScuola != null && prefScuola.getBool(pren.getCompany())) {
            if (!pren.isPrivato()) {
                send = true;
            }
        }
        return send;
    }

    /**
     * Controlla se deve inviare al referente di una data prenotazione
     */
    public boolean isSendReferente(Prenotazione pren) {
        boolean send = false;

        BaseCompany company = pren.getCompany();

        if (prefReferente != null && prefReferente.getBool(company)) {
            send = true;
            if (pren.isPrivato()) {
                if (prefNoPrivati != null && prefNoPrivati.getBool(company)) {
                    send = false;
                }
            }
        }
        return send;
    }


    /**
     * Controlla se questo tipo di mail va mandato in assoluto
     * per una data prenotazione.
     * Basta che debba essere mandata a qualcuno.
     */
    public boolean isSend(Prenotazione pren) {
        boolean send = false;

        BaseCompany company = pren.getCompany();

        if (prefSend.getBool(company)) {

            // manda alla scuola, ma non se privato
            if (prefScuola.getBool(company)) {
                if (!pren.isPrivato()) {
                    send = true;
                }
            }

            // manda al referente, ma non se privato e opzione NO PRIVATI=ON
            if (prefReferente.getBool(company)) {
                if (prefNoPrivati.getBool(company)) {
                    if (!pren.isPrivato()) {
                        send = true;
                    }
                } else {
                    send = true;
                }
            }

        }
        return send;
    }

    /**
     * Data una prenotazione, ritorna un array degli
     * indirizzi email ai quali questa lettera va spedita.
     * Aggiunge l'indirizzo solo se è esistente.
     * Considera se il referente ha il flag privato, in tal caso usa la apposita preferenza.
     *
     * @return una stringa con gli indirizzi separati da virgola
     */
    public String getEmailDestinatari(Prenotazione pren) {
        Scuola scuola = pren.getScuola();
        //Insegnante ins = pren.getInsegnante();

        ArrayList<String> indirizzi = new ArrayList<>();
        String email;

        // controlla se inviare alla scuola
        if (isSendScuola(pren)) {
            if (scuola != null) {
                email = scuola.getEmail();
                if ((email != null) && (!email.equals(""))) {
                    indirizzi.add(email);
                }
            }
        }

        // controlla se inviare al referente
        if (isSendReferente(pren)) {
            email = pren.getEmailRiferimento();
            if ((email != null) && (!email.equals(""))) {
                indirizzi.add(email);
            }
        }

//        // controlla se inviare al referente
//        if (prefReferente != null && prefReferente.getBool()) {
//            email = pren.getEmailRiferimento();
//            if ((email != null) && (!email.equals(""))) {
//                if (pren.isPrivato()) {
//                    if (prefNoPrivati != null && !prefNoPrivati.getBool()) {
//                        indirizzi.add(email);
//                    }
//                } else {
//                    indirizzi.add(email);
//                }
//            }
//        }

        // compone la stringa in uscita
        String emails = "";
        for (String addr : indirizzi) {
            if (!emails.equals("")) {
                emails += ", ";
            }
            emails += addr;

        }

        return emails;
    }

}// end of entity class

