package it.fabrix.executor;

/**
 *
 * @author fabrix
 */
public abstract class Invoker {

	protected final Command command;

	public Invoker(Command command) {
		this.command = command;
	}

	public abstract CommandResult exec() throws Exception;

}
