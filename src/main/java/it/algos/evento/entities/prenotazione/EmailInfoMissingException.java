package it.algos.evento.entities.prenotazione;

public class EmailInfoMissingException extends Exception {

	// Parameterless Constructor
	public EmailInfoMissingException() {
	}

	// Constructor that accepts a message
	public EmailInfoMissingException(String message) {
		super(message);
	}

}
