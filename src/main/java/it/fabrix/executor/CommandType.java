package it.fabrix.executor;

/**
 *
 * @author fabrix
 */
public enum CommandType {
	PING("ping -c 5"),
	HTTP_PING("http-ping"),
	TRACERT("traceroute");

	public final String value;

	private CommandType(String value) {
		this.value = value;
	}

}
