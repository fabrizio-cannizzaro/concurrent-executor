package it.fabrix.executor;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fabrix
 */
public class HttpInvoker extends Invoker {

	private static final Logger LOGGER = Logger.getGlobal();

	public HttpInvoker(Command command) {
		super(command);
	}

	@Override
	public CommandResult exec() {
		LOGGER.log(Level.INFO, "EXEC: {0}", this.command);
		String commandString = command.getCommandString();
		Date executionTime = new Date();
		try {
			String result = new HttpClient().sendGet(super.command.getHost(), command.getTimeout() * 1000);
			return new CommandResult(super.command.getCommandString(), result, executionTime, new Date());
		} catch (Exception ex) {
			LOGGER.log(Level.SEVERE, null, ex);
			return new CommandResult(commandString, ex.getMessage(), executionTime, new Date(), true);
		}
	}

}
