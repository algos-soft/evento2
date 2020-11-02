package it.algos.webbase.web.updown;

import java.io.IOException;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;

/**
 * This specializes {@link FileDownloader} in a way, such that both the file name and content can be determined
 * on-demand, i.e. when the user has clicked the component.
 */
public class OnDemandFileDownloader extends FileDownloader {

	/**
	 * Provide both the {@link StreamSource} and the filename in an on-demand way.
	 */
	public interface OnDemandStreamResource extends StreamSource {
		public String getFilename();
	}

	private static final long serialVersionUID = 1L;
	private final OnDemandStreamResource onDemandStreamResource;

	public OnDemandFileDownloader(OnDemandStreamResource streamResource) {
		super(new StreamResource(streamResource, ""));
		this.onDemandStreamResource = streamResource;
		// this.onDemandStreamResource = checkNotNull(onDemandStreamResource,
		// "The given on-demand stream resource may never be null!");
	}

	@Override
	public boolean handleConnectorRequest(VaadinRequest request, VaadinResponse response, String path)
			throws IOException {
		getResource().setFilename(onDemandStreamResource.getFilename());
		return super.handleConnectorRequest(request, response, path);
	}

	private StreamResource getResource() {
		return (StreamResource) this.getResource("dl");
	}

}
