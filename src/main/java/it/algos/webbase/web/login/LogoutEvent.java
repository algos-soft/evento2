package it.algos.webbase.web.login;

import java.util.EventObject;

/**
 * Logout event object
 */
public class LogoutEvent extends EventObject {
    private UserIF user;

    public LogoutEvent(Object source, UserIF user) {
        super(source);
        this.user=user;
    }

    public UserIF getUser() {
        return user;
    }

}
