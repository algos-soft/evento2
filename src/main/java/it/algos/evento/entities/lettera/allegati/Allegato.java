package it.algos.evento.entities.lettera.allegati;

import it.algos.webbase.multiazienda.CompanyEntity;
import it.algos.webbase.web.lib.LibFile;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = {
	    @UniqueConstraint(columnNames = {"company_id", "name"})
	})
public class Allegato extends CompanyEntity {
	
	private static final long serialVersionUID = 4097054721132658859L;

	@NotEmpty
	private String name;
	
	private byte[] content;
	
	private String mimeType;
	
	private long bytes;


	public Allegato() {
		super();
	}// end of constructor


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the content
	 */
	public byte[] getContent() {
		return content;
	}


	/**
	 * @param content the content to set
	 */
	public void setContent(byte[] content) {
		this.content = content;
	}

	/**
	 * @return the mimeType
	 */
	public String getMimeType() {
		return mimeType;
	}


	/**
	 * @param mimeType the mimeType to set
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}


	@Override
	public String toString() {
		return getName()+" - "+LibFile.humanReadableByteCount(getBytes());
		}


	/**
	 * @return the bytes
	 */
	public long getBytes() {
		return bytes;
	}


	/**
	 * @param bytes the bytes to set
	 */
	public void setBytes(long bytes) {
		this.bytes = bytes;
	}

	

}
