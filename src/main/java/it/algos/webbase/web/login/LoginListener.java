package it.algos.webbase.web.login;

/**
 * Listener invoked on a successful login.
 * <p>
 * La classe Login gestisce il form ed alla chiusura controlla la validità del nuovo utente
 * Lancia il fire di questo evento, se l'utente è valido.
 * La classe Login deve prevedere i metodi addLoginListener(), setLoginListener() e removeAllLoginListeners()
 * Nella classe Login si registrano i listeners (istanze di classi che sono interessate all'evento)
 * Le classi che si registrano devono implementare questa interfaccia
 * I listeners verrano informati dell'evento tramite invocazione del metodo di questa interfaccia
 */
public interface LoginListener {
    public void onUserLogin(LoginEvent e);
}// end of interface
