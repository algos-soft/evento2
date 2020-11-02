package it.algos.webbase.web.login;

import java.util.EventObject;

/**
 * Login event object
 */
public class LoginEvent extends EventObject {
    private boolean success;
    private UserIF user;
    private LoginTypes loginType;
    boolean rememberOption;

    public LoginEvent(Object source, boolean success, UserIF user, LoginTypes loginType, boolean rememberOption) {
        super(source);
        this.success=success;
        this.user=user;
        this.loginType=loginType;
        this.rememberOption=rememberOption;
    }

    public boolean isSuccess() {
        return success;
    }

    public UserIF getUser() {
        return user;
    }

    public LoginTypes getLoginType() {
        return loginType;
    }

    public boolean isRememberOption() {
        return rememberOption;
    }
}
