package it.algos.webbase.web.email;

public class Attachment {

	// nome dell'allegato nella mail
	private String name;

	// contenuto in bytes
	private byte[] content;

	// tipo MIME dell'allegato
	private String mimeType;


	public Attachment(String name, byte[] content, String mimeType) {
		this.name = name;
		this.content = content;
		this.mimeType = mimeType;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the content
	 */
	public byte[] getContent() {
		return content;
	}

	/**
	 * @return the mimeType
	 */
	public String getMimeType() {
		return mimeType;
	}


	@Override
	public String toString() {
		return getName()+" "+getMimeType()+" "+content.length+" bytes";
	}


}
