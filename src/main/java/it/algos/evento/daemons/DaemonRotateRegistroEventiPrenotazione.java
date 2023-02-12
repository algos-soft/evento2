package it.algos.evento.daemons;

import it.algos.webbase.web.bootstrap.ABootStrap;
import it.algos.webbase.web.lib.LibDate;
import it.sauronsoftware.cron4j.Scheduler;
import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;

import javax.servlet.ServletContext;
import java.time.LocalDate;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Questo daemon esegue una volta al giorno (di notte) e cancella
 * tutti gli elementi del registro eventi prenotazione più vecchi
 * di 2 anni per tutte le company
 */
public class DaemonRotateRegistroEventiPrenotazione extends Scheduler {

	private final static Logger logger = Logger.getLogger(DaemonRotateRegistroEventiPrenotazione.class.getName());

	private static DaemonRotateRegistroEventiPrenotazione instance;
	public static final String DAEMON_NAME = "daemon_rotate_eventi";

	private DaemonRotateRegistroEventiPrenotazione() {
		super();
	}

	class TaskRotateEventi extends Task {

		@Override
		public void execute(TaskExecutionContext context) throws RuntimeException {
			LocalDate date = LocalDate.now().minusYears(2);
			EventiPrenRotator rotator = new EventiPrenRotator(date);
			rotator.run();
		}
	}

	@Override
	public void start() throws IllegalStateException {
		if (!isStarted()) {
			super.start();

			// save daemon status flag into servlet context
			ServletContext svc = ABootStrap.getServletContext();
			svc.setAttribute(DAEMON_NAME, true);

			// Schedule task.
			schedule("0 2 * * *", new TaskRotateEventi());
			logger.log(Level.INFO, "Daemon rotazione eventi prenotazioni attivato (esegue tutti i giorni alle ore 02:00)");

		}

	}

	@Override
	public void stop() throws IllegalStateException {
		if (isStarted()) {
			super.stop();

			// save daemon status flag into servlet context
			ServletContext svc = ABootStrap.getServletContext();
			svc.setAttribute(DAEMON_NAME, false);

			logger.log(Level.INFO, "Daemon rotazione eventi disattivato.");

		}

	}

	public static DaemonRotateRegistroEventiPrenotazione getInstance() {
		if (instance == null) {
			instance = new DaemonRotateRegistroEventiPrenotazione();
		}
		return instance;
	}

}
