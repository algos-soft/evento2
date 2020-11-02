package it.algos.webbase.web.login;

import it.algos.webbase.web.entity.BaseEntity;

/**
 * Interface to a User entity
 * Created by alex on 25-05-2016.
 */
public interface UserIF {

    /**
     * @return the user name
     */
    String getNickname();

    /**
     * @return true if this user is admin
     */
    boolean isAdmin();

    /**
     * @return the password (in clear text)
     */
    String getPassword();

    /**
     * Sets a new password
     * @param password the password (in clear text)
     */
    void setPassword(String password);

    /**
     * @return the password (encrypted)
     */
    String getEncryptedPassword();

    /**
     * Validate a password for this current user.
     *
     * @param password the password
     * @return true if valid
     */
    boolean validatePassword(String password);

    /**
     * Persist the user in the storage
     */
    BaseEntity save();

}
