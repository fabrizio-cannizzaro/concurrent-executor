package it.fabrix.executor;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fabrix
 */
public class Main {

	private static final Logger LOGGER = Logger.getGlobal();

	private static final String CONFIG_FILE = "config";
	private static final String TIMEOUT_KEY = "timeout";
	private static final String DELAY_KEY = "delay";
	private static final String HOST_KEY = "host";
	private static final String SEPARATOR = "\\|";
	private static final long SLEEP_TIME = 1;
	private static final String REPORT_URL = "report.url";

	private final Map<Command, ScheduledFuture<CommandResult>> handlerMap;
	private final Map<Command, CommandResult> resultStore;
	private Configuration configuration;
	private ScheduledExecutorService executor;

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		Main main = new Main();
		main.configure();
		main.createPool();
		while (true) {
			main.start();
			Thread.sleep(SLEEP_TIME * 1000);//just to avoid useless cpu consuming
		}
	}

	public Main() {
		this.handlerMap = Collections.synchronizedMap(new HashMap<>());
		this.resultStore = Collections.synchronizedMap(new HashMap<>());
	}

	private void configure() {
		ResourceBundle rb = ResourceBundle.getBundle(CONFIG_FILE);
		String[] hosts = rb.getString(HOST_KEY).split(SEPARATOR);
		int delay = Integer.parseInt(rb.getString(DELAY_KEY));
		int timeout = Integer.parseInt(rb.getString(TIMEOUT_KEY));
		String reportUrl = rb.getString(REPORT_URL);
		this.configuration = new Configuration(hosts, delay, timeout, reportUrl);
	}

	private void createPool() {
		this.executor = Executors.newScheduledThreadPool(configuration.getHosts().length * CommandType.values().length);
	}

	public void start() {
		String[] hosts = configuration.getHosts();
		for (String h : hosts) {
			for (CommandType ct : CommandType.values()) {
				Command c = new Command(ct, h, configuration.getTimeout());
				long delay = check(c);
				if (delay >= 0) {
					ScheduledFuture<CommandResult> sf = startInvoke(ct == CommandType.HTTP_PING ? new HttpInvoker(c) : new ProcessInvoker(c), delay);
					this.handlerMap.put(c, sf);
					startMonitor(c, sf);
				}
			}
		}
	}

	private long check(Command c) {
		ScheduledFuture<CommandResult> sf = this.handlerMap.get(c);
		long delay = 0;
		if (sf != null) {
			if (sf.isDone()) {
				delay = this.configuration.getDelay() - this.resultStore.get(c).getDuration() - SLEEP_TIME;
				delay = delay < 0 ? 0 : delay;
				this.handlerMap.remove(c);
				LOGGER.log(Level.INFO, "REMOVING: {0}, delay:{1}", new Object[]{c, delay});
			} else {
				delay = -1;
			}
		}
		return delay;
	}

	private ScheduledFuture<CommandResult> startInvoke(Invoker i, long delay) {
		return this.executor.schedule(() -> {
			return i.exec();
		}, delay, TimeUnit.SECONDS);
	}

	private void startMonitor(Command c, ScheduledFuture<CommandResult> sf) {
		ExecutorService exec = Executors.newSingleThreadExecutor();
		exec.submit(() -> {
			try {
				CommandResult result = sf.get();
				LOGGER.log(Level.INFO, "RESULT: {0}", result);
				this.resultStore.put(c, result);
				if (result.isReport()) {
					String json = new Report(resultStore).toJson();
					new HttpClient().sendJsonPost(this.configuration.getReportUrl(), json);
				}
			} catch (InterruptedException | ExecutionException | IOException ex) {
				LOGGER.log(Level.SEVERE, null, ex);
			}
		});
		exec.shutdown();
	}

	public void shutdown() {
		executor.shutdown();
	}

}
