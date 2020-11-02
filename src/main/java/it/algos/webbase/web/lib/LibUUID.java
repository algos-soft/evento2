package it.algos.webbase.web.lib;

import java.util.UUID;

public abstract class LibUUID {
	
	/**
	 * Checks if a given string represents a valid UUID
	 * <p>
	 * @return true if valid
	 */
	public static boolean validateUUID(String uuidStr){
		boolean valid = false;
		try {
		    UUID uuid = UUID.fromString(uuidStr);
		    valid=(uuidStr.equals(uuid.toString()));
		} catch (IllegalArgumentException e) {
		}
	    return valid;
	}// end of static method

}// end of abstract static class
