package it.algos.evento.entities.prenotazione;

public class EmailFailedException extends Exception {

	// Parameterless Constructor
	public EmailFailedException() {
	}

	// Constructor that accepts a message
	public EmailFailedException(String message) {
		super(message);
	}

}
