package it.algos.evento.test;

import it.algos.evento.entities.lettera.LetteraService;
import org.apache.commons.mail.EmailException;
import org.junit.Test;

import static org.junit.Assert.fail;

public class LetteraServiceTest {

	@Test
	public void testGetTesto() {
		System.out.println("prova pippo");
	}// end of single test

	@Test
	public void testSend() {
		String from = "info@algos.it";
		String dest = "gac@algos.it";
		String oggetto = "prova";
		String testo = "testo senza allegati";
		String errore = "";

		try {
			if (LetteraService.sendMail(null, from, dest, oggetto, testo, false)) {
				System.out.println("spedita senza allegati");
			} else {
				System.out.println("Non spedita");
				fail("Non spedita");
			}// end of if/else cycle
		} catch (EmailException e) {
			errore = e.getMessage();
			System.out.println("Non spedita");
			fail("Non spedita");
		}
	}// end of single test

	@Test
	public void testSendMailAllegati() {
		String from = "info@algos.it";
		String dest = "gac@algos.it";
		String oggetto = "prova";
		String testo = "testo con allegati\n";
		String errore = "";

		try {
			if (LetteraService.sendMail(null, from, dest, oggetto, testo, false, null)) {
				System.out.println("spedita con allegati");
			} else {
				System.out.print("Non spedita");
				fail("Non spedita");
			}// end of if/else cycle
		} catch (EmailException e) {
			errore = e.getMessage();
			System.out.println("Non spedita");
			fail("Non spedita");
		}
	}// end of single test

}// end of test class