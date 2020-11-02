package it.algos.webbase.web.pref;

import it.algos.webbase.web.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Entity for storage of preferences.
 * <p>
 * Preferences are stored with a String key and a byte[] value.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PREFS")
public class PrefEntity extends BaseEntity {

	private String code;
	private byte[] value;

	public PrefEntity() {
		super();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}
	
	
}
