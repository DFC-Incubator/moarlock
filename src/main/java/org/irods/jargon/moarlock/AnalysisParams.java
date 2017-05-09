package org.irods.jargon.moarlock;

import java.util.Properties;

public class AnalysisParams {

	private final Properties params = new Properties();
	private final String inputMount = "";
	private final String outputMount = "";
	private final String GUID = "";

	public Properties getParams() {
		return params;
	}

	public String getInputMount() {
		return inputMount;
	}

	public String getOutputMount() {
		return outputMount;
	}

	public String getGUID() {
		return GUID;
	}

	@Override
	public String toString() {
		return "AnalysisParams [params=" + params + ", inputMount=" + inputMount + ", outputMount=" + outputMount
				+ ", GUID=" + GUID + "]";
	}

}
