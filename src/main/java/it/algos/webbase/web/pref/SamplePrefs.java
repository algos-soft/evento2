package it.algos.webbase.web.pref;

import it.algos.webbase.web.pref.AbsPref.PrefType;

import java.math.BigDecimal;
import java.util.Date;

import com.vaadin.server.Resource;
import com.vaadin.ui.Image;



/**
 * Sample enum for preferences.
 * <p>
 * Defines the preferences and the methods to access them.<br>
 * Each preference has a key, a type and a default value.
 * Copy and paste this enum to your specific application, rename it and define<br>
 * your preferences. The class should work out of the box.
 */

public enum SamplePrefs implements PrefIF {
	myStringPreference("myStringPref", PrefType.string, "Hello!"),
	myIntegerPreference("myIntPref", PrefType.integer, 12),
	;
	
	private String code;
	private PrefType type;
	private Object defaultValue;

	private SamplePrefs(String key, PrefType type, Object defaultValue) {
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
