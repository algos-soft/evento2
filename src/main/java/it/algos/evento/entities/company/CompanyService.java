package it.algos.evento.entities.company;

import com.vaadin.ui.Notification;
import it.algos.evento.demo.DemoDataGenerator;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.domain.utente.Utente;
import it.algos.webbase.web.lib.LibCrypto;

/**
 * Created by Alex on 03/10/15.
 */
public class CompanyService {

    /**
     * Activate a Company.
     * - create a corresponding user with the given password
     * - create demo data for the new company
     *
     * @param company    the company
     * @param password   the password for the user (in clear text)
     * @param createData true to create demo data
     */
    public static boolean activateCompany(BaseCompany company, String password, boolean createData) {

        // create a new user or update existing user
        String username=company.getCompanyCode();
        Utente user = Utente.read(username);
        if(user==null){
            user = new Utente();
        }
        user.setNickname(company.getCompanyCode());
        user.setPassword(LibCrypto.encrypt(password));
        user.setEnabled(true);
        user.save();

        // create demo data in background
        if (createData) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean success=DemoDataGenerator.createDemoData(company);
                    if(success){
                        Notification.show("Creazione dati demo completata correttamente per l'azienda " + company + ".");
                    }else{
                        Notification.show("Errore nella creazione dati demo della azienda " + company + ".", Notification.Type.ERROR_MESSAGE);
                    }
                }
            }).start();
        }

        return true;
    }


}
