package org.irods.jargon.moarlock;

import java.util.Properties;

public class AnalysisParams {

	private Properties params = new Properties();
	private String inputMount = "";
	private String outputMount = "";
	private String GUID = "";

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

	public void setParams(final Properties params) {
		this.params = params;
	}

	public void setInputMount(final String inputMount) {
		this.inputMount = inputMount;
	}

	public void setOutputMount(final String outputMount) {
		this.outputMount = outputMount;
	}

	public void setGUID(final String gUID) {
		GUID = gUID;
	}

}
