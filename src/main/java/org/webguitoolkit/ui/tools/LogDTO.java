package org.webguitoolkit.ui.tools;

import java.io.Serializable;

public class LogDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8122613374440827847L;
	
	private String logCategory;
	private String logLevel;
	public String getLogCategory() {
		return logCategory;
	}
	public void setLogCategory(String logCategory) {
		this.logCategory = logCategory;
	}
	public String getLogLevel() {
		return logLevel;
	}
	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

}
