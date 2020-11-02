package it.algos.webbase.multiazienda;

import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.domain.company.BaseCompany_;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.login.Login;
import it.algos.webbase.web.login.UserIF;

/**
 * Utility class to manage session stuff specific to this application.
 */
public class CompanySessionLib {

    private final static String KEY_COMPANY = "company";

    /**
     * Ritorna la Company corrente.
     *
     * @return la Company corrente
     */
    public static BaseCompany getCompany() {
        BaseCompany company = null;
        Object attr = LibSession.getAttribute(KEY_COMPANY);
        if ((attr != null) & (attr instanceof BaseCompany)) {
            company = (BaseCompany) attr;
        }// fine del blocco if

        return company;
    }// end of method

    /**
     * Ritorna il companyCode della company corrente.
     *
     * @return il companyCode corrente
     */
    public static String getCompanyCode() {
        String companyCode = "";
        BaseCompany company = getCompany();

        if (company != null) {
            companyCode = company.getCompanyCode();
        }// end of if cycle

        return companyCode;
    }// end of method

    public static void setCompany(BaseCompany company) {
        LibSession.setAttribute(KEY_COMPANY, company);
    }// end of method

    public static boolean isCompanySet() {
        return getCompany() != null;
    }// end of method


    /**
     * Individua la Company relativa a un dato utente
     * e la registra nella sessione corrente.
     * Se non è stata inviduata una Company ritorna false.
     */
    public static boolean registerCompanyByUser(UserIF user) {

        boolean success = false;

        // cerca una company con lo stesso nome
        String username = user.getNickname();
        BaseCompany company = BaseCompany.query.queryOne(BaseCompany_.companyCode, username);
        if (company != null) {
            setCompany(company);
            success = true;
        }
        return success;

    }

    /**
     * Registers the Login object in the session
     */
    public static void setLogin(Login login) {
        LibSession.setAttribute(Login.LOGIN_KEY_IN_SESSION, login);
    }

    /**
     * Returns a customized Login object for the Admin session.
     * (The Login object has a custom cookie prefix)
     * To retrieve Admin login, always use this method instead of Login.getLogin()
     *
     * @return the customized Login object
     */
    public static Login getAdminLogin() {
        Login login = Login.getLogin();
        login.setCookiePrefix("admin");
        return login;
    }


}// end of class
