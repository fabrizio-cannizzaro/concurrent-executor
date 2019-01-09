package it.fabrix.executor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fabrix
 */
public class ProcessInvoker extends Invoker {

	private static final Logger LOGGER = Logger.getGlobal();

	public ProcessInvoker(Command command) {
		super(command);
	}

	@Override
	public CommandResult exec() {
		LOGGER.log(Level.INFO, "EXEC: {0}", this.command);
		String commandString = command.getCommandString();
		Date executionTime = new Date();
		try {
			Process p = Runtime.getRuntime().exec(commandString);
			BufferedReader inputStream = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String s;
			StringBuilder sb = new StringBuilder();
			while ((s = inputStream.readLine()) != null) {
				sb.append(s).append("\n");
			}
			return new CommandResult(commandString, sb.toString(), executionTime, new Date());
		} catch (IOException | RuntimeException ex) {
			LOGGER.log(Level.SEVERE, null, ex);
			return new CommandResult(commandString, ex.getMessage(), executionTime, new Date(), true);
		}

	}

}
