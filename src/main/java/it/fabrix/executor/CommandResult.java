package it.fabrix.executor;

import java.util.Date;

/**
 *
 * @author fabrix
 */
public class CommandResult {

	private final String command;
	private final String result;
	private final Date startTime;
	private final Date endTime;
	private final boolean report;

	public CommandResult(String command, String result, Date startTime, Date endTime, boolean report) {
		this.command = command;
		this.result = result;
		this.startTime = startTime;
		this.endTime = endTime;
		this.report = report;
	}

	public CommandResult(String command, String result, Date startTime, Date endTime) {
		this(command, result, startTime, endTime, false);
	}

	public String getCommand() {
		return command;
	}

	public String getResult() {
		return result;
	}

	public Date getStartTime() {
		return startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public long getDuration() {
		return (this.endTime.getTime() - this.startTime.getTime()) / 1000;
	}

	public boolean isReport() {
		return report;
	}

	@Override
	public String toString() {
		return "CommandResult{" + "command=" + command + ", result=" + result + ", startTime=" + startTime + ", endTime=" + endTime + ", report=" + report + '}';
	}

}
