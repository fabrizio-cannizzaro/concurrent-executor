package it.fabrix.executor;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author fabrix
 */
public class Report {

	private Logger logger;
	String host;
	String icmpPing;
	String tcpPing;
	String trace;

	public Report(Map<Command, CommandResult> results) {
		this.initLogger();
		init();
		for (Entry<Command, CommandResult> e : results.entrySet()) {
			Command c = e.getKey();
			CommandResult cr = e.getValue();
			this.host = c.getHost();
			switch (c.getCommandType()) {
				case HTTP_PING:
					this.tcpPing = cr.getResult();
					break;
				case PING:
					this.icmpPing = cr.getResult();
					break;
				case TRACERT:
					this.trace = cr.getResult();
					break;
			}
		}
	}

	private void init() {
		host = "";
		icmpPing = "";
		tcpPing = "";
		trace = "";
	}

	private void initLogger() {
		try {
			new File("logs").mkdir();
			logger = Logger.getLogger("ReportLog");
			logger.setLevel(Level.WARNING);
			logger.setUseParentHandlers(false);
			FileHandler fh = new FileHandler("logs/report.log", true);
			fh.setFormatter(new SimpleFormatter());
			logger.addHandler(fh);
		} catch (IOException | SecurityException e) {
			System.out.println(e);
		}
	}

	public void log() {
		logger.warning(toJson());
	}

	@Override
	public String toString() {
		return "Report{" + "host=" + host + ", icmpPing=" + icmpPing + ", tcpPing=" + tcpPing + ", trace=" + trace + '}';
	}

	public String toJson() {
		String json = String.format("{\"host\":\"%s\", \"icmp_ping\":\"%s\", \"tcp_ping\":\"%s\", \"trace\":\"%s\"}", host, icmpPing, tcpPing, trace);
		json = json.replace("\n", " ");
		return json;
	}

}
