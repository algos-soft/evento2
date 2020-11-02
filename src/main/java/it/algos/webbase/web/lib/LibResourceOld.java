package it.algos.webbase.web.lib;

import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import it.algos.webbase.web.AlgosApp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Classe statica (abstract) di libreria.
 */
public abstract class LibResourceOld {


//    /**
//     * Ritorna una immagine dalle risorse LOCALI come byte array.
//     * <p>
//     *
//     * @param name nome dell'immagine
//     * @return il corrispondente byte array.
//     * @deprecated
//     */
//    public static byte[] getLocImgBytes(String name) {
//        String prePath = AlgosApp.getLocImgFolderPath().toString();
//        return getImgBytes(prePath, name);
//    }// end of method

//    /**
//     * Ritorna una immagine dalle risorse BASE come byte array.
//     * <p>
//     *
//     * @param name nome dell'immagine
//     * @return il corrispondente byte array.
//     */
//    public static byte[] getImgBytes(String name) {
//        return getImgBytes(AlgosApp.getStrProjectPath(), name);
//    }// end of static method

//    /**
//     * Ritorna una immagine dalle risorse come byte array.
//     * <p>
//     *
//     * @param mediumPath path dell'immagine (esclusa la radice del progetto ed il nome del file)
//     * @param name       nome dell'immagine
//     * @return il corrispondente byte array.
//     */
//    public static byte[] getImgBytes(String mediumPath, String name) {
//        byte[] bytes = null;
//        Path path = null;
//        String tag = "/";
//
//        if (!mediumPath.equals("")) {
//            if (mediumPath.startsWith(tag)) {
//                path = Paths.get(mediumPath, name);
//            } else {
//                path = Paths.get(AlgosApp.getServletContext().getRealPath("/"), mediumPath, name);
//            }// fine del blocco if-else
//        }// fine del blocco if
//
//        if (path != null) {
//            try {
//                bytes = Files.readAllBytes(path);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }// fine del blocco try-catch
//        }// fine del blocco if
//
//        return bytes;
//    }// end of static method



//    /**
//     * Ritorna una immagine dalle risorse LOCALI come resource.
//     * <p>
//     *
//     * @param name nome dell'immagine
//     * @return la Resource corrispondente.
//     * @deprecated
//     */
//    public static Resource getLocImgResource(String name) {
//        return getImgResource(AlgosApp.getStrProjectPath(), name);
//    }// end of static method

//    /**
//     * Ritorna una immagine dalle risorse BASE come resource.
//     * <p>
//     *
//     * @param name nome dell'immagine
//     * @return la Resource corrispondente.
//     */
//    public static Resource getImgResource(String name) {
//        Path path = AlgosApp.getImgFolderPath();
//
//        if (path != null) {
//            return getImgResource(path.toString(), name);
//        } else {
//            return null;
//        }// fine del blocco if-else
//
//    }// end of static method

//    /**
//     * Ritorna una immagine dalle risorse come resource.
//     * <p>
//     *
//     * @param prePath path dell'immagine (escluso il nome del file)
//     * @param name    nome dell'immagine
//     * @return la Resource corrispondente.
//     */
//    public static Resource getImgResource(String prePath, String name) {
//        Resource res = null;
//        byte[] bytes = getImgBytes(prePath, name);
//        if (bytes != null) {
//            if (bytes.length > 0) {
//                res = getStreamResource(bytes);
//            }// fine del blocco if
//        }// fine del blocco if
//
//        return res;
//    }// end of static method

//    /**
//     * Create a StreamResource form a byte array
//     * <p>
//     *
//     * @param bytes the byte array
//     * @return the StreamResource
//     */
//    @SuppressWarnings("serial")
//    public static StreamResource getStreamResource(final byte[] bytes) {
//        StreamResource resource = null;
//
//        // Create a stream source returning the data from the byte array
//        StreamSource streamSource = new StreamSource() {
//            @Override
//            public InputStream getStream() {
//                return new ByteArrayInputStream(bytes);
//            }// end of inner method
//        };// end of inner anonimous class
//
//        // create a StreamResource as data source for the image
//        // the name of the stream must be changed or the browser cache will not update the image
//        String resName = "" + System.currentTimeMillis();
//        resource = new StreamResource(streamSource, resName);
//
//        return resource;
//    }// end of static method

}// end of abstract static class
