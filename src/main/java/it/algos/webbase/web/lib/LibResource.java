package it.algos.webbase.web.lib;

import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import it.algos.webbase.web.AlgosApp;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;



/**
 * Libreria per il recupero delle risorse dal server
 */
public class LibResource {


    /**
     * Ritorna una immagine come byte array.
     * <p>
     *
     * @param path percorso relativo fino alla directory dove si trova l'immagine
     * @param name nome dell'immagine
     * @return il corrispondente byte array.
     */
    public static byte[] getImgBytes(String path, String name) {
        byte[] bytes = null;
        Path fullpath = Paths.get(path, name);
        try {
            ServletContext sc = AlgosApp.getServletContext();
            if(sc!=null){
                String realpath = sc.getRealPath("/" + fullpath.toString());
                File f = new File(realpath);
                if(f.exists() && !f.isDirectory()) {
                    bytes = Files.readAllBytes(Paths.get(realpath));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }


    /**
     * Ritorna una immagine come resource.
     * <p>
     *
     * @param path percorso relativo fino alla directory dove si trova l'immagine
     * @param name nome dell'immagine
     * @return la Resource corrispondente.
     */
    public static Resource getImgResource(String path, String name) {
        Resource res = null;
        byte[] bytes = getImgBytes(path, name);
        if (bytes != null) {
            if (bytes.length > 0) {
                res = getStreamResource(bytes);
            }
        }
        return res;
    }


    /**
     * Ritorna una immagine dalla directory di default come byte array.
     * <p>
     *
     * @param name nome dell'immagine
     * @return il corrispondente byte array.
     */
    public static byte[] getImgBytes(String name) {
        return getImgBytes(AlgosApp.IMG_FOLDER_NAME, name);
    }

    /**
     * Ritorna una immagine dalla directory di default come resource.
     * <p>
     *
     * @param name nome dell'immagine
     * @return la Resource corrispondente.
     */
    public static Resource getImgResource(String name) {
        return getImgResource(AlgosApp.IMG_FOLDER_NAME, name);
    }


    /**
     * Create a StreamResource form a byte array
     * <p>
     *
     * @param bytes the byte array
     * @return the StreamResource
     */
    @SuppressWarnings("serial")
    public static StreamResource getStreamResource(final byte[] bytes) {
        StreamResource resource = null;

//        // Create a stream source returning the data from the byte array
//        StreamSource streamSource = new StreamSource(){
//            @Override
//            public InputStream getStream() {
//                return new ByteArrayInputStream(bytes);
//            }
//        };


        // Create a stream source returning the data from the byte array
        StreamSource streamSource = new ByteStreamResource(bytes);


        // create a StreamResource as data source for the image
        // the name of the stream must be changed or the browser cache will not update the image
        String resName = "" + System.currentTimeMillis();
        resource = new StreamResource(streamSource, resName);

        return resource;
    }


}
