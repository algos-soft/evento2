package it.algos.webbase.web.email;

import org.apache.commons.mail.DataSourceResolver;

import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;

public class ImageResolver implements DataSourceResolver {

	public ImageResolver() {
	}

	@Override
	public DataSource resolve(String resourceLocation) throws IOException {
		ByteArrayDataSource bds=null;
//		bds = new ByteArrayDataSource(allegato.getContent(), allegato.getMimeType());
		return bds;
	}

	@Override
	public DataSource resolve(String resourceLocation, boolean isLenient) throws IOException {
		return resolve(resourceLocation);
	}

}
