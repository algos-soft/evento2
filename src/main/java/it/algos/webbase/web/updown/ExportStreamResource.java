package it.algos.webbase.web.updown;

import com.vaadin.server.DownloadStream;
import com.vaadin.server.StreamResource;

/**
 * Created by alex on 3-02-2016.
 */
public class ExportStreamResource extends StreamResource {

    public ExportStreamResource(ExportStreamSource streamSource) {
        super(streamSource, null);
        setFilename(streamSource.getExportConfiguration().getFilename());
    }

    @Override
    public DownloadStream getStream() {
        DownloadStream ds = new DownloadStream(getStreamSource().getStream(), getMIMEType(), getFilename());

        // Content-Disposition: attachment generally forces download
        ds.setParameter("Content-Disposition", "attachment; filename=\"" + getFilename() + "\"");
        ds.setContentType("application/octet-stream;charset=UTF-8");

        return ds;
    }

}
