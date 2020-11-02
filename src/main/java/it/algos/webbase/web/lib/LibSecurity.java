package it.algos.webbase.web.lib;

import it.algos.webbase.domain.ruolo.Ruolo;
import it.algos.webbase.domain.ruolo.TipoRuolo;
import it.algos.webbase.domain.utente.Utente;
import it.algos.webbase.domain.utenteruolo.UtenteRuolo;

/**
 * Created by gac on 03 set 2015.
 * .
 */
public abstract class LibSecurity {

    /**
     * Controllo esistenza dei ruoli standard <br>
     * Se mancano, li crea <br>
     */
    public static void checkRuoliStandard() {
        for (String nome : TipoRuolo.getAllNames()) {
            creaRuolo(nome);
        }// fine del ciclo for
    }// end of static method

    /**
     * Controllo esistenza di un ruolo <br>
     * Se manca, lo crea <br>
     */
    public static void creaRuolo(String nome) {
        Ruolo ruolo = Ruolo.read(nome);

        if (ruolo == null) {
            ruolo = new Ruolo(nome);
            ruolo.save();
        }// end of if cycle
    }// end of static method


    /**
     * Crea un programmatore/developer
     */
    public static void creaDeveloper(String nickName, String password) {
        creaUtente(nickName, password, TipoRuolo.developer.toString());
    }// end of static method


    /**
     * Crea un programmatore/developer
     */
    public static void creaAdmin(String nickName, String password) {
        creaUtente(nickName, password, TipoRuolo.admin.toString());
    }// end of static method


    /**
     * Crea un programmatore/developer
     */
    public static void creaUser(String nickName, String password) {
        creaUtente(nickName, password, TipoRuolo.user.toString());
    }// end of static method


    /**
     * Crea un programmatore/developer
     */
    public static void creaGuest(String nickName, String password) {
        creaUtente(nickName, password, TipoRuolo.guest.toString());
    }// end of static method


    /**
     * Crea un utente con ruolo connesso
     *
     * @param nickName  dell'utente
     * @param password  dell'utente
     * @param nomeRuolo da assegnare
     */
    public static void creaUtente(String nickName, String password, String nomeRuolo) {
        Ruolo ruolo = Ruolo.read(nomeRuolo);

        if (ruolo != null) {
            creaUtente(nickName, password, ruolo);
        }// end of if cycle

    }// end of static method


    /**
     * Crea un utente con ruolo connesso
     *
     * @param nickName dell'utente
     * @param password in chiaro dell'utente
     * @param ruolo    da assegnare
     */
    public static void creaUtente(String nickName, String password, Ruolo ruolo) {
        Utente newUtente = Utente.read(nickName);
        UtenteRuolo userRole;

        if (newUtente == null) {
            password= LibCrypto.encrypt(password);
            newUtente = new Utente(nickName, password);
            newUtente.save();
        }// end of if cycle

        if (newUtente != null) {
            userRole = UtenteRuolo.read(newUtente, ruolo);
            if (userRole == null) {
                userRole = new UtenteRuolo(newUtente, ruolo);
                userRole.save();
            }// end of if cycle
        }// end of if cycle

    }// end of method

}// end of abstract static class
