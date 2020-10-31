package com.vmware.vslm.kibishii.core;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import org.json.simple.JSONObject;

public abstract class Results {
	protected boolean completed = false;
	protected Date completedTime = null;
	protected Throwable error = null;
	
	public abstract JSONObject toJSON();

	public void setCompleted() {
		completed = true;
		completedTime = new Date();
	}

	public boolean isCompleted() {
		return completed;
	}
	
	public void setError(Throwable error) {
		this.error = error;
		completed = true;
		completedTime = new Date();
	}

	public Throwable getError() {
		return error;
	}
	
	@SuppressWarnings("unchecked")
	protected void addCompletionAndErrorInfo(JSONObject retJSON) {
		retJSON.put("completed", completed);
		if (completed)
			retJSON.put("completedTime", completedTime.toString());
		if (error != null) {
			retJSON.put("error", error.getMessage());
			StringWriter stackTraceWriter = new StringWriter();
			PrintWriter stackTracePrintWriter = new PrintWriter(stackTraceWriter);
			error.printStackTrace(stackTracePrintWriter);
			stackTracePrintWriter.flush();
			retJSON.put("errorStackTrace", stackTraceWriter.toString());
		}
	}
}
