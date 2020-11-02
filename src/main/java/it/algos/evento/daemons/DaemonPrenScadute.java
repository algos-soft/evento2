package it.algos.evento.daemons;

import it.algos.webbase.web.bootstrap.ABootStrap;
import it.algos.webbase.web.lib.LibDate;
import it.sauronsoftware.cron4j.Scheduler;
import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;

import javax.servlet.ServletContext;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

//import org.mortbay.log.Log;

public class DaemonPrenScadute extends Scheduler {

	private final static Logger logger = Logger.getLogger(DaemonPrenScadute.class.getName());

	private static DaemonPrenScadute instance;
	public static final String DAEMON_NAME = "daemon_pren_scadute";

	private DaemonPrenScadute() {
		super();
	}

	class TaskPrenScadute extends Task {

		@Override
		public void execute(TaskExecutionContext context) throws RuntimeException {
			Date checkDate = LibDate.today();
			logger.log(Level.INFO, "Start controllo prenotazioni scadute al "+LibDate.toStringDDMMYYYY(checkDate));
			PrenChecker checker = new PrenChecker(checkDate);
			checker.run();
			logger.log(Level.INFO, "End controllo prenotazioni scadute...");
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
			schedule("0 * * * *", new TaskPrenScadute());
			logger.log(Level.INFO, "Daemon generale controllo scadenze attivato (ogni ora).");

		}

	}

	@Override
	public void stop() throws IllegalStateException {
		if (isStarted()) {
			super.stop();

			// save daemon status flag into servlet context
			ServletContext svc = ABootStrap.getServletContext();
			svc.setAttribute(DAEMON_NAME, false);

			logger.log(Level.INFO, "Daemon controllo prenotazioni disattivato.");

		}

	}


	public static DaemonPrenScadute getInstance() {
		if (instance == null) {
			instance = new DaemonPrenScadute();
		}
		return instance;
	}

}
