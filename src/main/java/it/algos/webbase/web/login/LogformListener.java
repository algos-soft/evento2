package it.algos.webbase.web.login;

/**
 * Listener invoked for open a form
 * <p>
 * La classe LoginBar gestisce il bottone/menu per il login
 * Lancia il fire di questo evento, se si clicca il bottone/menu Login.
 * Nella classe LoginBar si registrano i listeners
 */
public interface LogformListener {
    public void onLogFormRequest();
}
