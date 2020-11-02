package it.algos.webbase.web.updown;

import it.algos.webbase.web.updown.OnDemandFileDownloader.OnDemandStreamResource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import com.vaadin.server.StreamResource.StreamSource;

@SuppressWarnings("serial")
public class StringStreamResource implements OnDemandStreamResource, StreamSource {

	private String filename;
	private String content;

	public StringStreamResource(String content, String filename) {
		super();
		this.content = content;
		this.filename = filename;
	}

	@Override
	public InputStream getStream() {
		ByteArrayInputStream is = null;

		byte[] bytes;
		try {
			bytes = content.getBytes("UTF-8");
			ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length);
			baos.write(bytes, 0, bytes.length);
			is = new ByteArrayInputStream(baos.toByteArray());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return is;
	}

	@Override
	public String getFilename() {
		return filename;
	}

}
