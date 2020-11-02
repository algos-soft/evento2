package it.algos.webbase.web.login;

import java.util.EventObject;

/**
 * User profile change event object
 */
public class ProfileChangeEvent extends EventObject {
    private UserIF user;

    public ProfileChangeEvent(Object source, UserIF user) {
        super(source);
        this.user = user;
    }


    public UserIF getUser() {
        return user;
    }

}
