package it.algos.webbase.web.login;

import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import it.algos.webbase.domain.utente.Utente;
import it.algos.webbase.web.AlgosApp;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.lib.LibCookie;
import it.algos.webbase.web.lib.LibCrypto;
import it.algos.webbase.web.lib.LibSession;

import javax.servlet.http.Cookie;
import java.util.ArrayList;

/**
 * Main Login object (Login logic).
 * An instance of this object is created and stored in the current session
 * when getLogin() in invoked. Subsequent calls to getLogin() return the same object
 * from the session.
 */

public class Login {

    // key to store the Login object in the session
    public static final String LOGIN_KEY_IN_SESSION = "login";

    // defaults
    private static final int DEFAULT_EXPIRY_TIME_SEC = 604800;    // 1 week
    private static final boolean DEFAULT_RENEW_COOKIES_ON_LOGIN = true;    // renews the cookies on login

    // default cookie names
    public static final String COOKIENAME_LOGIN = "login_username";
    public static final String COOKIENAME_PASSWORD = "login_password";
    public static final String COOKIENAME_REMEMBER = "login_remember";

    /**
     * Evento lanciato quando viene tentato un login.
     * I listeners ricevono un LoginEvent con i dettagli sull'evento.
     */
    private ArrayList<LoginListener> loginListeners = new ArrayList<>();

    // listeners notificati al logout
    private ArrayList<LogoutListener> logoutListeners = new ArrayList<>();

    // listeners notificati quando si modifica il profilo utente
    private ArrayList<ProfileChangeListener> profileListeners = new ArrayList<>();

    private UserIF user;

    private String cookiePrefix = "";
    private int expiryTime = DEFAULT_EXPIRY_TIME_SEC;
    private boolean renewCookiesOnLogin = DEFAULT_RENEW_COOKIES_ON_LOGIN;

    private AbsLoginForm loginForm;
    private AbsUserProfileForm profileForm;

    public Login() {
        loginForm = new DefaultLoginForm();
        profileForm = new DefaultUserProfileForm();
    }

    /**
     * Recupera l'oggetto Login dalla sessione.
     * Se manca lo crea ora e lo registra nella sessione.
     */
    public static Login getLogin() {
        Login login = null;
        Object obj = LibSession.getAttribute(Login.LOGIN_KEY_IN_SESSION);
        if (obj == null) {
            login = new Login();
            LibSession.setAttribute(Login.LOGIN_KEY_IN_SESSION, login);
        } else {
            try {
                login = (Login) obj;
            } catch (Exception e) {
            }
        }

        return login;
    }


    /**
     * Registra un oggetto Login nella sessione.
     */
    public static void setLogin(Login login) {
        LibSession.setAttribute(Login.LOGIN_KEY_IN_SESSION, login);
    }

    /**
     * Verifica se l'oggetto Login esiste nella sessione.
     *
     * @return true se esiste.
     */
    public static boolean isEsisteLoginInSessione() {
        Object obj = LibSession.getAttribute(Login.LOGIN_KEY_IN_SESSION);
        return obj != null;
    }

    /**
     * Retrieve the Login form
     *
     * @return the Login form to show
     */
    public AbsLoginForm getLoginForm() {
        return loginForm;
    }

    /**
     * Retrieve the User Profile form
     *
     * @return the User Profile form to show
     */
    public AbsUserProfileForm getUserProfileForm() {
        return profileForm;
    }


    /**
     * Displays the Login form
     */
    public void showLoginForm() {

        AbsLoginForm loginForm = getLoginForm();

        // set the login listener in the form
        loginForm.setLoginListener(new LoginListener() {
            @Override
            public void onUserLogin(LoginEvent e) {
                attemptLogin();
            }
        });

        // retrieve login data from the cookies
        readCookies();

        Window window = loginForm.getWindow();
        window.center();
        UI.getCurrent().addWindow(window);

    }

    /**
     * Displays the user profile
     */
    public void showUserProfile() {

        AbsUserProfileForm profileForm = getUserProfileForm();
        profileForm.setUser(Login.getLogin().getUser());

        // sets the confirm listener in the form
        profileForm.setConfirmListener(new ConfirmDialog.ConfirmListener() {
            @Override
            public void confirmed(ConfirmDialog dialog) {
                ProfileChangeEvent e = new ProfileChangeEvent(Login.this, user);
                for (ProfileChangeListener listener : profileListeners) {
                    listener.profileChanged(e);
                }
            }
        });

        Window window = profileForm.getWindow();
        window.center();
        UI.getCurrent().addWindow(window);

    }


    /**
     * Tenta il login con i dati presenti nela UI.
     * Se riesce, notifica i LoginListener(s)
     *
     * @return true se riuscito
     */
    public boolean attemptLogin() {
        boolean riuscito = false;
        boolean remember = false;

        UserIF user = getLoginForm().getSelectedUser();
        if (user != null) {
            String pass = getLoginForm().getPassField().getValue();
            if (pass != null && !pass.isEmpty()) {
                if (user.validatePassword(pass)) {

                    setUser(user);
                    remember = getLoginForm().getRememberField().getValue();

                    // create/update the cookies
                    if (remember) {
                        writeCookies();
                    } else {
                        deleteCookies();
                    }

                    riuscito = true;
                }

            }
        }

        LoginEvent e = new LoginEvent(this, riuscito, user, null, remember);
        fireLoginListeners(e);

        return riuscito;
    }


    /**
     * Logout the current user
     */
    public void logout() {
        UserIF oldUser = this.user;
        this.user = null;
        LogoutEvent e = new LogoutEvent(this, oldUser);
        for (LogoutListener l : logoutListeners) {
            l.onUserLogout(e);
        }
        deleteCookies();//@todo aggiunta di gac 12.2.17
    }


    /**
     * Reads data from the fields and writes the cookies
     */
    public void writeCookies() {
        AbsLoginForm loginForm = getLoginForm();
        boolean remember = loginForm.getRememberField().getValue();
        UserIF user = loginForm.getSelectedUser();

        LibCookie.setCookie(getLoginKey(), user.getNickname(), getLoginPath(), expiryTime);
        LibCookie.setCookie(getPasswordKey(), user.getEncryptedPassword(), getLoginPath(), expiryTime);
        if (remember) {
            LibCookie.setCookie(getRememberKey(), "true", getLoginPath(), expiryTime);
        } else {
            LibCookie.deleteCookie(getRememberKey());
        }

    }


    /**
     * Reads the cookies and puts the data in the fields
     */
    public void readCookies() {

        String username = LibCookie.getCookieValue(getLoginKey());
        String encPass = LibCookie.getCookieValue(getPasswordKey());
        String clearPass = LibCrypto.decrypt(encPass);
        String rememberStr = LibCookie.getCookieValue(getRememberKey());
        boolean remember = (rememberStr.equalsIgnoreCase("true"));

        AbsLoginForm loginForm = getLoginForm();
        loginForm.setUsername(username);
        loginForm.setPassword(clearPass);
        loginForm.setRemember(remember);

    }


    /**
     * Renew the expiry time for all the cookies
     */
    private void renewCookies() {
        Cookie cookie;
        cookie = LibCookie.getCookie(getLoginKey());
        LibCookie.setCookie(getLoginKey(), cookie.getValue(), getLoginPath(), expiryTime);
        cookie = LibCookie.getCookie(getPasswordKey());
        LibCookie.setCookie(getPasswordKey(), cookie.getValue(), getLoginPath(), expiryTime);
        cookie = LibCookie.getCookie(getRememberKey());
        LibCookie.setCookie(getRememberKey(), cookie.getValue(), getLoginPath(), expiryTime);
    }

    /**
     * Delete all the cookies
     */
    private void deleteCookies() {
        LibCookie.deleteCookie(getLoginKey(), getLoginPath());
        LibCookie.deleteCookie(getPasswordKey(), getLoginPath());
        LibCookie.deleteCookie(getRememberKey(), getLoginPath());
    }


    /**
     * Attempts a login directly from the cookies without showing the login form.
     *
     * @return true if success
     */
    public boolean loginFromCookies() {
        boolean success = false;
        String username = LibCookie.getCookieValue(getLoginKey());
        String encPassword = LibCookie.getCookieValue(getPasswordKey());
        String clearPass = LibCrypto.decrypt(encPassword);

        UserIF user = userFromNick(username);

        if (user != null) {
            if (user.validatePassword(clearPass)) {
                this.user = user;
                success = true;
            }
        }

        // if success, renew the cookies (if the option is on)
        // if failed, delete the cookies
        if (success) {
            if (renewCookiesOnLogin) {
                renewCookies();
            }
            LoginEvent e = new LoginEvent(this, true, user, LoginTypes.TYPE_COOKIES, false);
            fireLoginListeners(e);

        } else {
            deleteCookies();
        }


        return success;
    }


    /**
     * Retrieves the user from a given username
     *
     * @return the user corresponding to a given username
     */
    protected UserIF userFromNick(String username) {
        Utente aUser = Utente.read(username);
        return aUser;
    }


    /**
     * @return the expiry time of the cookies in seconds
     */
    public int getExpiryTime() {
        return expiryTime;
    }

    /**
     * Sets the expiry time for the cookies
     *
     * @param expiryTime the expiry time of the cookies in seconds
     */
    public void setExpiryTime(int expiryTime) {
        this.expiryTime = expiryTime;
    }

    /**
     * Whether the cookies are renewed after a successful login.
     *
     * @return true if the cookies are renewed
     */
    public boolean isRenewCookiesOnLogin() {
        return renewCookiesOnLogin;
    }// end of method

    /**
     * Whether the cookies are renewed after a successful login.
     *
     * @param renewCookiesOnLogin true to renew the expiry time on each login
     */
    public void setRenewCookiesOnLogin(boolean renewCookiesOnLogin) {
        this.renewCookiesOnLogin = renewCookiesOnLogin;
    }// end of method

    public void setCookiePrefix(String cookiePrefix) {
        this.cookiePrefix = cookiePrefix;
    }

    protected String getLoginPath() {
        return AlgosApp.COOKIES_PATH;
    }// end of method

    protected String getLoginKey() {
        String name = "";
        if (!cookiePrefix.equals("")) {
            name += cookiePrefix + ".";
        }
        return name += COOKIENAME_LOGIN;
    }// end of method

    protected String getPasswordKey() {
        String name = "";
        if (!cookiePrefix.equals("")) {
            name += cookiePrefix + ".";
        }
        return name += COOKIENAME_PASSWORD;
    }

    protected String getRememberKey() {
        String name = "";
        if (!cookiePrefix.equals("")) {
            name += cookiePrefix + ".";
        }
        return name += COOKIENAME_REMEMBER;
    }

    /**
     * Removes all the login listeners
     */
    public void removeAllLoginListeners() {
        loginListeners.clear();
    }// end of method

    /**
     * Adds a LoginListener
     */
    public void addLoginListener(LoginListener l) {
        loginListeners.add(l);
    }// end of method

    /**
     * Registers a unique LoginListener.
     * All the previous LoginListeners are deleted
     */
    public void setLoginListener(LoginListener l) {
        removeAllLoginListeners();
        addLoginListener(l);
    }// end of method

    /**
     * Adds a LogoutListener
     */
    public void addLogoutListener(LogoutListener l) {
        logoutListeners.add(l);
    }

    /**
     * Adds a ProfileChangeListener
     * Warning! Login is static - avoid leaving listeners attached!
     */
    public void addProfileListener(ProfileChangeListener l) {
        profileListeners.add(l);
    }

    /**
     * Sets the ProfileChangeListener
     */
    public void setProfileListener(ProfileChangeListener l) {
        profileListeners.clear();
        addProfileListener(l);
    }

    /**
     * @return true if a user is logged
     */
    public boolean isLogged() {
        return (getUser() != null);
    }

    public UserIF getUser() {
        return user;
    }

    public void setUser(UserIF user) {
        this.user = user;
    }


    /**
     * Notifica i LoginListeners.
     * Esegue l'iterazione su una copia della ArrayList.
     * Inquesto modo chi viene notificato può aggiungere dei LoginListeners
     * senza causare una ConcurrentModificationException
     *
     * @param e il LoginEvent
     */
    private void fireLoginListeners(LoginEvent e) {
        if (loginListeners != null) {
            ArrayList<LoginListener> listenersCopy = (ArrayList<LoginListener>) loginListeners.clone();
            for (LoginListener listener : listenersCopy) {
                listener.onUserLogin(e);
            }
        }
    }


}
