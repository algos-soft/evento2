package it.algos.evento.entities.prenotazione;

import com.sibvisions.vaadin.server.DownloaderExtension;
import com.vaadin.ui.AbstractComponent;
import it.algos.webbase.web.importexport.ExportConfiguration;
import it.algos.webbase.web.importexport.ExportManager;
import it.algos.webbase.web.updown.ExportStreamResource;
import it.algos.webbase.web.updown.ExportStreamSource;

/**
 * Custom ExportManager to create a custom ExportStreamSource.
 * A custom ExportStreamSource is needed to access the worksheet and append the totals row
 */
public class PrenExportManager extends ExportManager {

    private PrenExportSource exportSource;

    public PrenExportManager(ExportConfiguration config, AbstractComponent comp) {
        super(config, comp);
        exportSource = new PrenExportSource(getConfig());
    }

    @Override
    protected void onConfirm() {
        ExportStreamResource streamResource=new ExportStreamResource(exportSource);
        DownloaderExtension downloader = new DownloaderExtension();
        downloader.extend(getComp());
        downloader.setDownloadResource(streamResource);
        close();
    }

    public PrenExportSource getExportSource() {
        return exportSource;
    }
}
