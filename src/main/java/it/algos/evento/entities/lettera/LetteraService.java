package it.algos.evento.entities.lettera;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Compare;
import it.algos.evento.entities.lettera.allegati.Allegato;
import it.algos.evento.entities.lettera.allegati.Allegato_;
import it.algos.evento.entities.spedizione.Spedizione;
import it.algos.evento.pref.CompanyPrefs;
import it.algos.evento.pref.EventoPrefs;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.AQuery;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.ImageHtmlEmail;

import javax.mail.util.ByteArrayDataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LetteraService {

    private static final String ESCAPE_INI = "${";
    private static final String ESCAPE_END = "}";

    public static String getTesto(String testoIn, HashMap<String, String> mappaEscape) {
        String testoOut = testoIn;
        String chiave = "";
        String valore = "";
        int posIni;
        int posEnd;
        String prima;
        String dopo;

        if (mappaEscape != null) {
            for (Object esc : mappaEscape.keySet()) {
                chiave = ESCAPE_INI + esc + ESCAPE_END;
                valore = mappaEscape.get(esc);

                if (testoOut.contains(chiave)) {
                    do {
                        posIni = testoOut.indexOf(chiave);
                        posEnd = posIni + chiave.length();
                        prima = testoOut.substring(0, posIni);
                        dopo = testoOut.substring(posEnd);
                        testoOut = prima + valore + dopo;
                    } while (testoOut.contains(chiave));
                }// end of if cycle

            }// end of for cycle
        }// end of if cycle

        return testoOut;
    }// end of method

    public static Spedizione spedisci(Lettera lettera, LetteraMap mappaEscape, HashMap<String, Object> mappaMail) {
        Spedizione spedizione = new Spedizione();
        spedizione.setCompany(lettera.getCompany());
        boolean spedita = false;
        String testoMail = null;
        HashMap<String, String> mappaEsc = mappaEscape.getEscapeMap();
        String errore = null;
        Object obj;

        testoMail = lettera.getTestOut(mappaEsc);

        String from = null;
        obj = mappaMail.get(MailKeys.from.getKey());
        if (obj != null) {
            from = (String) obj;
        }

        String destinatario = null;
        obj = mappaMail.get(MailKeys.destinatario.getKey());
        if (obj != null) {
            destinatario = (String) obj;
        }

        String oggetto = "";
        obj = mappaMail.get(MailKeys.oggetto.getKey());
        if (obj != null) {
            oggetto = (String) obj;
        } else {
            oggetto = lettera.getOggetto();
        }

        // crea l'array degli allegati
        // solo quelli che sono elencati nella stringa e che esistono in gestione allegati
        Allegato[] allegati=null;
        String sNomi=lettera.getAllegati();
        if(sNomi!=null && !sNomi.equals("")){
            String[] aNomi=sNomi.split(",");
            ArrayList<Allegato> listaAlleg = new ArrayList<>();
            for(int i=0; i<aNomi.length; i++){
                String nome = aNomi[i].trim();
                Allegato allegato = null;
                Container.Filter f1 = new Compare.Equal(Allegato_.name.getName(), nome);
                Container.Filter f2 = new Compare.Equal(Allegato_.company.getName(), lettera.getCompany());
                Container.Filter filter = new And(f1, f2);
                List<? extends BaseEntity> listAllegati = AQuery.getList(Allegato.class, filter);
                if(listAllegati.size()==1){
                    BaseEntity entity=listAllegati.get(0);
                    allegato = (Allegato) entity;
                    listaAlleg.add(allegato);
                }

            }

            // list to array
            allegati=listaAlleg.toArray(new Allegato[0]);

        }

        try {
            spedita = LetteraService.sendMail(lettera.getCompany(), from, destinatario, oggetto,
                    testoMail, lettera.isHtml(), allegati);
        } catch (EmailException e) {
            errore = e.getMessage();
            spedizione.setErrore(errore);
        }
        spedizione.setLettera(lettera);
        spedizione.setDestinatario(destinatario);
        spedizione.setSpedita(spedita);

        spedizione.save();

        return spedizione;
    }// end of method

    /**
     * Invia una email.
     *
     * @param company  l'azienda per conto della quale si spedisce la mail
     * @param from    il mittente, se null o vuoto usa l'indirizzo della company corrente
     * @param dest    il destinatario
     * @param oggetto l'oggetto della mail
     * @param testo   il corpo della mail
     * @return true se spedita correttamente
     */
    public static boolean sendMail(BaseCompany company, String from, String dest, String oggetto, String testo) throws EmailException {
        return sendMail(company, from, dest, oggetto, testo, true);
    }// end of method

    /**
     * Invia una email.
     *
     * @param company  l'azienda per conto della quale si spedisce la mail
     * @param from    il mittente, se null o vuoto usa l'indirizzo della company corrente
     * @param dest    il destinatario
     * @param oggetto l'oggetto della mail
     * @param testo   il corpo della mail
     * @param html   true se è una mail html
     * @return true se spedita correttamente
     */
    public static boolean sendMail(BaseCompany company, String from, String dest, String oggetto, String testo, boolean html) throws EmailException {
        return sendMail(company, from, dest, oggetto, testo, html, null);
    }// end of method


    /**
     * Invia una email.
     *
     * @param company  l'azienda per conto della quale si spedisce la mail
     * @param from    il mittente, se null o vuoto usa l'indirizzo della company corrente
     * @param dest    il destinatario
     * @param oggetto l'oggetto della mail
     * @param testo   il corpo della mail
     * @param html   true se è una mail html
     * @param allegati elenco degli allegati
     * @return true se spedita correttamente
     */
    public static boolean sendMail(BaseCompany company, String from, String dest, String oggetto, String testo, boolean html, Allegato[] allegati) throws EmailException {
        boolean spedita = false;
        String hostName = "";
        String username = "";
        String password = "";
        boolean useAuth = false;
        int smtpPort;


        // --legge dalle preferenze
        hostName = EventoPrefs.smtpServer.getString();
        username = EventoPrefs.smtpUserName.getString();
        password = EventoPrefs.smtpPassword.getString();
        useAuth = EventoPrefs.smtpUseAuth.getBool();
        smtpPort = EventoPrefs.smtpPort.getInt();


        // se from non è specificato usa quello della company
        if ((from == null) || (from.equals(""))){
            if(company!=null){
                from = CompanyPrefs.senderEmailAddress.getString(company);
            }
        }

        // spedisce
        spedita = sendMail(company, hostName, smtpPort, useAuth, username, password, from, dest, oggetto, testo, html, allegati);

        return spedita;
    }// end of method


    public static boolean sendMail(BaseCompany company, String hostName, int smtpPort, boolean useAuth, String nickName,
                                   String password, String from, String dest, String oggetto,
                                   String testo, boolean html, Allegato[] allegati) throws EmailException {
        boolean spedita = false;
        ImageHtmlEmail email;


        email = new ImageHtmlEmail();

//        //adds attachments
//        if (allegati != null && !allegati.equals("")) {
//            ArrayList<String> listaAllegati = Lib.getArrayDaTesto(allegati);
//            for (String name : listaAllegati) {
//                DataSource ds = AllegatoModulo.getDataSource(name);
//                if(ds!=null){
//                    String disposition = EmailAttachment.ATTACHMENT;
//                    email.attach(ds, name, name, disposition);
//                }
//            }
//        }

        // adds attachments
        if(allegati!=null){
            for(Allegato allegato : allegati){
                byte[] content=allegato.getContent();
                String mimeType=allegato.getMimeType();
                ByteArrayDataSource bds = new ByteArrayDataSource(content, mimeType);
                bds.setName(allegato.getName());
                String disposition = EmailAttachment.ATTACHMENT;
                email.attach(bds, allegato.getName(), allegato.getName(), disposition);
            }
        }

        // Create the email message
        if (hostName != null && !hostName.equals("")) {
            email.setHostName(hostName);
        }

        email.setSmtpPort(smtpPort);

        if (useAuth) {
            email.setAuthenticator(new DefaultAuthenticator(nickName, password));
            //email.setSSLOnConnect(false);
            email.setStartTLSEnabled(true);
        }

        if (from != null && !from.equals("")) {
            email.setFrom(from);
        }

        if (oggetto != null && !oggetto.equals("")) {
            email.setSubject(oggetto);
        }

        // aggiunge email di backup se configurato
        if (CompanyPrefs.backupEmail.getBool(company)) {
            String backupAddress = CompanyPrefs.backupEmailAddress.getString(company);
            if (!(backupAddress.equals(""))) {
                email.addBcc(backupAddress);
            }
        }


        if (html) {
            email.setHtmlMsg(testo);
        } else {
            email.setMsg(testo);
        }

        if (dest != null && !dest.equals("")) {
            String[] list = dest.split(",");
            for (String addr : list) {
                addr = addr.trim();
                email.addTo(addr);
            }
        }

        // set a data source resolver to resolve embedded images
        if (html) {
            ImageResolver resolver = new ImageResolver(company);
            email.setDataSourceResolver(resolver);
        }

        // send the email
        email.send();
        spedita = true;

        return spedita;
    }// end of method


}// end of class