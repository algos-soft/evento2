package it.algos.webbase.web.importexport;

import com.sibvisions.vaadin.server.DownloaderExtension;
import com.vaadin.ui.AbstractComponent;
import it.algos.webbase.web.updown.ExportStreamResource;
import it.algos.webbase.web.updown.ExportStreamSource;

/**
 * Classe factory per le esportazioni
 * Created by alex on 3-02-2016.
 */
public class ExportFactory {


    /**
     * Esegue una operazione di export ed effettua il download del file.
     * @param conf - la configurazione di esportazione
     * @param targetComp - il componente parente (il riferimento serve unicamente per rilasciare
     *                   le risorse di stream quando il componente viene rilasciato, il download
     *                   parte subito, non c'è alcun legame funzionale con il componente)
     */
    public static void doExport(ExportConfiguration conf, AbstractComponent targetComp){
        ExportStreamSource streamSource = new ExportStreamSource(conf);
        ExportStreamResource streamResource=new ExportStreamResource(streamSource);
        DownloaderExtension downloader = new DownloaderExtension();
        downloader.extend(targetComp);
        downloader.setDownloadResource(streamResource);
    }

}
