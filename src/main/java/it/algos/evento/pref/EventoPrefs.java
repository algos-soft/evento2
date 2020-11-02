package it.algos.evento.pref;

import com.vaadin.server.Resource;
import com.vaadin.ui.Image;
import it.algos.webbase.web.pref.AbsPref;
import it.algos.webbase.web.pref.AbsPref.PrefType;
import it.algos.webbase.web.pref.PrefIF;

import java.math.BigDecimal;
import java.util.Date;

/**
 * General application preferences.
 * <p>
 * (Company preferences are defined in the CompanyPrefs enum)<br>
 * Defines the preferences and the methods to access them.<br>
 * Each preference has a key, a type and a default value.
 */

public enum EventoPrefs implements PrefIF {
	smtpServer("smtpServer",PrefType.string, ""),
	
	smtpPort("smtpPort", PrefType.integer, 25),
	
	smtpUseAuth("smtpUseAuth", PrefType.bool, false),

	smtpPassword("smtpPassword", PrefType.string, ""),

	smtpUserName("smtpUser", PrefType.string, ""),
	
	startDaemonAtStartup("startDaemonAtStartup", PrefType.bool, false),

	autoLoginCompany("autoLoginCompany", PrefType.integer, 0),

	;

	private String code;
	private PrefType type;
	private Object defaultValue;

	private EventoPrefs(String key, PrefType type, Object defaultValue) {
		this.code = key;
		this.type = type;
		this.defaultValue = defaultValue;
	}

	public String getCode() {
		return code;
	}

	public PrefType getType() {
		return type;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}
	
	

	/**
	 * Checks if this preference exists in the storage.
	 * <p>
	 * @return true if the preference exists
	 */
	public boolean exists(){
		return AbsPref.exists(this);
	}

	/**
	 * Retrieves this preference's value as boolean
	 */
	public boolean getBool(){
		return AbsPref.getBool(this);
	}

	/**
	 * Retrieves this preference's value as byte[]
	 */
	public byte[] getBytes(){
		return AbsPref.getBytes(this);
	}

	/**
	 * Retrieves this preference's value as Date
	 */
	public Date getDate(){
		return AbsPref.getDate(this);
	}
	
	/**
	 * Retrieves this preference's value as BigDecimal
	 */
	public BigDecimal getDecimal(){
		return AbsPref.getDecimal(this);
	}

	/**
	 * Retrieves this preference's value as int
	 */
	public int getInt(){
		return AbsPref.getInt(this);
	}

	/**
	 * Retrieves this preference's value as String
	 */
	public String getString(){
		return AbsPref.getString(this);
	}
	
	/**
	 * Retrieves this preference's value as Image
	 */
	public Image getImage(){
		return AbsPref.getImage(this);
	}

	/**
	 * Retrieves this preference's value as Resource
	 */
	public Resource getResource() {
		return AbsPref.getResource(this);
	}

	/**
	 * Writes a value in the storage for this preference
	 * <p>
	 * If the preference does not exist it is created now.
	 *
	 * @param value
	 *            the value
	 */
	public void put(Object value) {
		AbsPref.put(this, value);
	}
	
	/**
	 * Removes this preference from the storage.
	 * <p>
	 */
	public void remove(){
		AbsPref.remove(this);
	}
	
	/**
	 * Resets this preference to its default value .
	 * <p>
	 */
	public void reset(){
		AbsPref.reset(this);
	}
	


}
