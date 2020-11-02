package it.algos.webbase.web.lib;

import com.vaadin.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by alex on 20/12/15.
 */
public class ByteStreamResource implements StreamResource.StreamSource {

    byte[] bytes;

    public ByteStreamResource(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public InputStream getStream() {
        return new ByteArrayInputStream(bytes);
    }

}
