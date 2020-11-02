package it.algos.webbase.web.lib;

import java.io.File;
import java.util.Collection;

import com.google.common.collect.Iterables;

import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil;

public abstract class LibFile {

	/**
	 * Returns the MIME type of a file
	 * <p>
	 * 
	 * @param file
	 *            the file
	 * @return the MIME type
	 */
	public static MimeType getMimeType(File file) {
		MimeType mimeType = null;
		MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
		Collection<?> mimeTypes = MimeUtil.getMimeTypes(file);
		if (mimeTypes.size() > 0) {
			Object obj = Iterables.get(mimeTypes, 0);
			if ((obj != null) && (obj instanceof MimeType)) {
				mimeType = (MimeType) obj;
			}
		}
		return mimeType;
	}// end of static method

	/**
	 * Converts a size in bytes in human readable form.
	 * <p>
	 * 
	 * @param bytes
	 *            the bytes count
	 * @return the human readable representation
	 */
	public static String humanReadableByteCount(long bytes) {
		boolean si = true;
		int unit = si ? 1000 : 1024;
		if (bytes < unit)
			return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}// end of static method

	/**
	 * Create directory.
	 * <p>
	 */
	public static boolean makeDir(String path) {
		boolean status;
		status = new File(path).mkdir();

		return status;
	}// end of static method

	/**
	 * Estrae il nome del file (finale) dal path.
	 * <p>
	 * Come separatori, riconosce sia lo slash (/) sia il punto (.) <br>
	 */
	public static String nomeFile(String path) {
		String nome = path;
		String tagSlash = "/";
		String tagPoint = ".";
		int pos = 0;

		if (!path.equals("")) {
			if (path.contains(tagSlash)) {
				pos = path.lastIndexOf(tagSlash);
				nome = path.substring(pos);
			}// end of if cycle
			if (path.contains(tagPoint)) {
				pos = path.lastIndexOf(tagPoint);
				nome = path.substring(pos);
			}// end of if cycle
		}// end of if cycle

		return nome;
	}// end of static method

}// end of abstract static class
