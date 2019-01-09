package it.fabrix.executor;

import java.util.Objects;

/**
 *
 * @author fabrix
 */
public class Command {

	private final CommandType commandType;
	private final String host;
	private final int timeout;

	public Command(CommandType commandType, String host, int timeout) {
		this.commandType = commandType;
		this.host = host;
		this.timeout = timeout;
	}

	public String getCommandString() {
		return String.format("%s %s", commandType.value, host);
	}

	public CommandType getCommandType() {
		return commandType;
	}

	public String getHost() {
		return host;
	}

	public int getTimeout() {
		return timeout;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 67 * hash + Objects.hashCode(this.commandType);
		hash = 67 * hash + Objects.hashCode(this.host);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Command other = (Command) obj;
		if (!Objects.equals(this.host, other.host)) {
			return false;
		}
		if (this.commandType != other.commandType) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Command{" + "commandType=" + commandType + ", host=" + host + '}';
	}

}
