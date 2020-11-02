package it.algos.webbase.web.lib;

import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import it.algos.webbase.web.login.Login;
import it.algos.webbase.web.login.UserIF;

/**
 * Created by gac on 26/05/15.
 * .
 */
public abstract class LibSession {

    /**
     * Recupera dalla sessione l'attributo developer
     */
    public static boolean isDeveloper() {
        return isBool(Attribute.developer);
    }// end of static method

    /**
     * Regola per la sessione corrente l'attributo developer
     */
    public static void setDeveloper(boolean status) {
        setBool(Attribute.developer, status);
    }// end of static method

    /**
     * Recupera dalla sessione l'attributo debug
     */
    public static boolean isDebug() {
        return isBool(Attribute.debug);
    }// end of static method

    /**
     * Regola per la sessione corrente l'attributo debug
     */
    public static void setDebug(boolean status) {
        setBool(Attribute.debug, status);
    }// end of static method


    /**
     * Recupera dalla sessione l'attributo login
     */
    public static Login getLogin() {
        Login login = null;
        Object value = null;
        VaadinSession session = VaadinSession.getCurrent();

        if (session != null) {
            value = session.getAttribute(Attribute.login.toString());
        }// fine del blocco if

        if (value != null && value instanceof Login) {
            login = (Login) value;
        }// fine del blocco if

        return login;
    }// end of static method

    /**
     * Regola per la sessione corrente l'attributo login
     */
    public static void setLogin(Login login) {
        VaadinSession sessione = VaadinSession.getCurrent();

        if (sessione != null) {
            sessione.setAttribute(Attribute.login.toString(), login);
        }// fine del blocco if
    }// end of static method


    /**
     * Recupera dalla request l'attributo developer (se esiste)
     * Regola la variabile statica
     */
    public static void checkDeveloper(VaadinRequest request) {
        LibSession.checkBool(request, Attribute.developer);
    }// end of static method

    /**
     * Recupera dalla request l'attributo debug (se esiste)
     * Regola la variabile statica
     */
    public static void checkDebug(VaadinRequest request) {
        LibSession.checkBool(request, Attribute.debug);
    }// end of static method


    /**
     * Recupera dalla sessione l'attributo admin
     */
    public static boolean isAdmin() {
        boolean admin = false;
        Login login;
        UserIF user = null;

        if (isDeveloper()) {
            return true;
        }// end of if cycle

        login = Login.getLogin();

        if (login != null) {
            user = login.getUser();
            if (user != null) {
                admin = user.isAdmin();
            }// fine del blocco if
        }// fine del blocco if

        return admin;
    }// end of static method

//    /**
//     * Regola per la sessione corrente l'attributo admin
//     */
//    public static void setAdmin(boolean status) {
//        setBool(Attribute.admin, status);
//    }// end of static method


//    /**
//     * Recupera dalla sessione l'attributo firstTime
//     * Se l'attributo manca, di default ritorna 'true'
//     *
//     * @return false se manca l'attributo
//     */
//    public static boolean isFirstTime() {
//        boolean status = true;
//        Object devObj = null;
//        VaadinSession sessione = VaadinSession.getCurrent();
//
//        if (sessione != null) {
//            devObj = sessione.getAttribute(Attribute.firstTime.toString());
//            if (devObj != null) {
//                if (devObj instanceof Boolean) {
//                    status = (Boolean) devObj;
//                }// fine del blocco if
//            }// fine del blocco if
//        }// fine del blocco if
//
//        return status;
//    }// end of static method

//    /**
//     * Regola per la sessione corrente l'attributo firstTime
//     */
//    public static void setFirstTime(boolean status) {
//        setBool(Attribute.firstTime, status);
//    }// end of static method

    /**
     * @return true if a user is logged in
     */
    public static boolean isLogged() {
        boolean logged = false;
        Object obj = getAttribute(Login.LOGIN_KEY_IN_SESSION);
        if (obj != null) {
            Login login = (Login) obj;
            logged = login.isLogged();
        }
        return logged;
    }

    /**
     * Recupera dalla sessione l'attributo specifico
     */
    public static boolean isBool(Attribute attributo) {
        boolean status = false;
        Object devObj = null;
        VaadinSession sessione = VaadinSession.getCurrent();

        if (attributo != null && sessione != null) {
            devObj = sessione.getAttribute(attributo.toString());
            if (devObj != null) {
                if (devObj instanceof Boolean) {
                    status = (Boolean) devObj;
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        return status;
    }// end of static method

    /**
     * Regola lo specifico attributo
     */
    public static void setBool(Attribute attributo, boolean status) {
        VaadinSession sessione = VaadinSession.getCurrent();

        if (attributo != null && sessione != null) {
            sessione.setAttribute(attributo.toString(), status);
        }// fine del blocco if
    }// end of static method

    /**
     * Regola lo specifico attributo della sessione.
     * Qualsiasi applicazione può registrare dei propri attributi nella session.
     */
    public static void setAttribute(String name, Object value) {
        VaadinSession session = VaadinSession.getCurrent();

        if (session != null) {
            session.setAttribute(name, value);
        }// fine del blocco if
    }// end of static method

    /**
     * Recupera un attributo dalla sessione.
     */
    public static Object getAttribute(String name) {
        Object value = null;
        VaadinSession session = VaadinSession.getCurrent();

        if (session != null) {
            value = session.getAttribute(name);
        }// fine del blocco if

        return value;
    }// end of static method


    /**
     * Verifica se un attributo esiste nella sessione.
     */
    public static boolean isAttribute(String name) {
        Object value = null;
        VaadinSession session = VaadinSession.getCurrent();

        if (session != null) {
            value = session.getAttribute(name);
        }// fine del blocco if

        return (value != null);
    }// end of static method


    /**
     * Recupera un attributo dalla sessione.
     */
    public static String getAttributeStr(String name) {
        String value = "";
        Object obj = getAttribute(name);

        if (obj instanceof String) {
            value = (String) obj;
        }// fine del blocco if

        return value;
    }// end of static method

    /**
     * Recupera un attributo dalla sessione.
     */
    public static boolean getAttributeBool(String name) {
        boolean value = false;
        Object obj = getAttribute(name);

        if (obj instanceof Boolean) {
            value = (Boolean) obj;
        }// fine del blocco if

        return value;
    }// end of static method

    /**
     * Recupera dalla request l'attributo booleano indicato (se esiste)
     * Regola la variabile statica
     */
    public static void checkBool(VaadinRequest request, Attribute attribute) {
        boolean status = LibSession.getBool(request, attribute);
        LibSession.setBool(attribute, status);
    }// end of static method

    /**
     * Recupera dalla request l'attributo booleano indicato (se esiste)
     * Restituisce il valore booleano
     */
    public static boolean getBool(VaadinRequest request, Attribute attribute) {
        boolean status = false;
        Object obj = null;
        String objValue = "";
        String attributeTxt = attribute.toString();

        if (request != null) {
            obj = request.getParameter(attributeTxt);
        }// fine del blocco if

        if (obj != null) {
            objValue = (String) obj;
            if (objValue.equals("False") || objValue.equals("Falso") || objValue.equals("false") || objValue.equals("falso)")) {
                status = false;
            } else {
                status = true;
            }// fine del blocco if-else
        }// fine del blocco if

        return status;
    }// end of static method

    public enum Attribute {
        developer, admin, debug, firstTime, login
    }// end of inner enumeration

}// end of abstract static class
