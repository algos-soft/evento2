package it.algos.webbase.web.lib;

import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Image;

public abstract class LibImage {

	/**
	 * Create an Image form a resource
	 * <p>
	 * @param resource the resource
	 * @return the image
	 */
	public static Image getImage(final Resource resource){
		Image image = new Image(null, resource);
	    return image;
	}// end of static method

	/**
	 * Create an Image form a byte array
	 * <p>
	 * @param bytes the byte array
	 * @return the image
	 */
	public static Image getImage(final byte[] bytes){
		Image image = new Image();
	    StreamResource resource = LibResource.getStreamResource(bytes);
	    image.setSource(resource);
	    return image;
	}// end of static method



}// end of abstract static class
