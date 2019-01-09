package it.fabrix.executor;

/**
 *
 * @author fabrix
 */
public class Configuration {

	private final String[] hosts;
	private final int delay;
	private final int timeout;
	private final String reportUrl;

	public Configuration(String[] hosts, int delay, int timeout, String reportUrl) {
		this.hosts = hosts;
		this.delay = delay;
		this.timeout = timeout;
		this.reportUrl = reportUrl;
	}

	public String[] getHosts() {
		return hosts;
	}

	public int getDelay() {
		return delay;
	}

	public int getTimeout() {
		return timeout;
	}

	public String getReportUrl() {
		return reportUrl;
	}

	@Override
	public String toString() {
		return "Configuration{" + "hosts=" + hosts + ", delay=" + delay + ", timeout=" + timeout + ", reportUrl=" + reportUrl + '}';
	}

}
