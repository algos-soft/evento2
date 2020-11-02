package it.algos.webbase.domain.utente;

import it.algos.webbase.domain.ruolo.Ruolo;
import it.algos.webbase.domain.ruolo.TipoRuolo;
import it.algos.webbase.domain.utenteruolo.UtenteRuolo;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.lib.LibCrypto;
import it.algos.webbase.web.login.UserIF;
import it.algos.webbase.web.query.AQuery;
import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Utente extends BaseEntity implements UserIF {

    private static final long serialVersionUID = -8963424744780350658L;

    @NotEmpty
    private String nickname = "";

    private String password = "";

    private boolean enabled = true;

    @OneToMany(mappedBy = "utente")
    @CascadeOnDelete
    private List<UtenteRuolo> ruoliutente;

    public Utente() {
        this("", "");
    }// end of constructor

    public Utente(String nickname, String password) {
        this.setNickname(nickname);
        this.setPassword(password);
    }// end of constructor

    /**
     * Recupera l'utente usando la query specifica
     *
     * @param id key id
     * @return l'utente, null se non trovato
     */
    public static Utente read(long id) {
        Utente instance = null;
        BaseEntity entity = AQuery.queryById(Utente.class, id);

        if (entity != null) {
            if (entity instanceof Utente) {
                instance = (Utente) entity;
            }// end of if cycle
        }// end of if cycle

        return instance;
    }// end of method

    /**
     * Recupera l'utente usando la query specifica
     *
     * @param nickname dell'utente
     * @return l'utente, null se non trovato
     */
    public static Utente read(String nickname) {
        Utente instance = null;
        BaseEntity entity = AQuery.queryOne(Utente.class, Utente_.nickname, nickname);

        if (entity != null) {
            if (entity instanceof Utente) {
                instance = (Utente) entity;
            }// end of if cycle
        }// end of if cycle

        return instance;
    }// end of method

    /**
     * Valida nome e password e ritorna l'utente corrispondente.
     * Se le credenziali sono nulle, errate o l'utente è disabilitato ritorna null
     *
     * @param nickname dell'utente
     * @param password dell'utente - in chiaro!
     * @return l'utente, null se non trovato
     */
    public static Utente validate(String nickname, String password) {
        Utente user = null;

        if((nickname!=null) && (password!=null)){
            if (!nickname.equals("")) {
                Utente aUser = read(nickname);
                if (aUser != null) {
                    if (aUser.isEnabled()) {
                        String clearPass= aUser.getPassword();
                        if(clearPass!=null){
                            if (clearPass.equals(password)) {
                                user = aUser;
                            }
                        }
                    }
                }
            }
        }

        return user;
    }

    @Override
    /**
     * @inheritDoc
     */
    public boolean validatePassword(String password) {
        boolean valid=false;
        if (isEnabled()) {
            String clearPass=getPassword();
            if(clearPass!=null){
                if (clearPass.equals(password)) {
                    valid = true;
                }
            }
        }
        return valid;
    }

    @Override
    public String toString() {
        return nickname;
    }// end of method

    public String getNickname() {
        return nickname;
    }// end of getter method

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }//end of setter method

    public String getPassword() {
        String pass=null;
        if(password!=null){
            pass=LibCrypto.decrypt(password);
        }
        return pass;
    }

    public void setPassword(String password) {
        String pass=null;
        if(password!=null){
            pass = LibCrypto.encrypt(password);
        }
        this.password=pass;
    }

    public String getEncryptedPassword(){
        return password;
    }

    public boolean isEnabled() {
        return enabled;
    }// end of getter method

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }//end of setter method


    /**
     * Checks if this user has a given Role.
     *
     * @param role the role
     * @return true if this user has the role
     */
    public boolean hasRole(Ruolo role) {
        // controlla se l'utente ha ruolo di admin
        boolean found=false;
        if (role!=null){
            ArrayList<UtenteRuolo> urs = UtenteRuolo.findUtente(this);
            if(urs.size()>0){
                for(UtenteRuolo uruolo : urs){
                    if(uruolo.getRuolo().equals(role)){
                        found=true;
                        break;
                    }
                }
            }
        }

        return found;

    }


    /**
     * @return true if this user is an admin
     */
    public boolean isAdmin(){
        Ruolo adminRole = Ruolo.read(TipoRuolo.admin);
        return hasRole(adminRole);
    }


}// end of entity class
