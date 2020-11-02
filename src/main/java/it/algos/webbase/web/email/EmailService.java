package it.algos.webbase.web.email;

import it.algos.webbase.domain.log.Log;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.ImageHtmlEmail;

import javax.mail.util.ByteArrayDataSource;

public class EmailService {

    /**
     * Send an email
     */
    public static boolean sendMail(String hostName, int smtpPort, boolean useAuth, String username,
                                   String password, String from, String to, String cc, String bcc, String subject,
                                   String text, boolean html, Attachment[] attachments) throws EmailException {
        boolean spedita = false;
        ImageHtmlEmail email;


        email = new ImageHtmlEmail();


        // adds attachments
        if (attachments != null) {
            for (Attachment attachment : attachments) {
                byte[] content = attachment.getContent();
                String mimeType = attachment.getMimeType();
                ByteArrayDataSource bds = new ByteArrayDataSource(content, mimeType);
                bds.setName(attachment.getName());
                String disposition = EmailAttachment.ATTACHMENT;
                email.attach(bds, attachment.getName(), attachment.getName(), disposition);
            }
        }

        // Create the email message
        if (hostName != null && !hostName.equals("")) {
            email.setHostName(hostName);
        }

        email.setSmtpPort(smtpPort);

        if (useAuth) {
            email.setAuthenticator(new DefaultAuthenticator(username, password));
            //email.setSSLOnConnect(false);
            email.setStartTLSEnabled(true);
        }

        // adds from
        if (from != null && !from.equals("")) {
            email.setFrom(from);
        }

        if (subject != null && !subject.equals("")) {
            email.setSubject(subject);
        }

        if (html) {
            email.setHtmlMsg(text);
        } else {
            email.setMsg(text);
        }

        // adds to(s)
        if (to != null && !to.equals("")) {
            String[] list = to.split(",");
            for (String addr : list) {
                addr = addr.trim();
                email.addTo(addr);
            }
        }

        // adds cc(s)
        if (cc != null && !cc.equals("")) {
            String[] list = cc.split(",");
            for (String addr : list) {
                addr = addr.trim();
                email.addCc(addr);
            }
        }

        // adds bcc(s)
        if (bcc != null && !bcc.equals("")) {
            String[] list = bcc.split(",");
            for (String addr : list) {
                addr = addr.trim();
                email.addBcc(addr);
            }
        }

        // set a data source resolver to resolve embedded images
        if (html) {
            ImageResolver resolver = new ImageResolver();
            email.setDataSourceResolver(resolver);
        }

        // send the email
        try {
            email.send();
            spedita = true;
            Log.debug("email", "email sent successfully");
        }catch (EmailException e){
            Log.error("email", "error sending email: " + e.getMessage());
            throw e;
        }

        return spedita;
    }


}