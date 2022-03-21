package io.yoshizaki2104.heavyjarbuildrx.cli.config;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ScriptDef implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8316591114128571571L;

	private String scriptLabel;

	private String runtimeType;
	
	private String maxHeapSize;
	
	private String scriptPath;

	@JsonProperty("scriptLabel")
	public String getScriptLabel() {
		return scriptLabel;
	}

	public void setScriptLabel(String scriptLabel) {
		this.scriptLabel = scriptLabel;
	}

	@JsonProperty("runtimeType")
	public String getRuntimeType() {
		return runtimeType;
	}

	public void setRuntimeType(String runtimeType) {
		this.runtimeType = runtimeType;
	}

	@JsonProperty("maxHeapSize")
	public String getMaxHeapSize() {
		return maxHeapSize;
	}

	public void setMaxHeapSize(String maxHeapSize) {
		this.maxHeapSize = maxHeapSize;
	}

	@JsonProperty("scriptPath")
	public String getScriptPath() {
		return scriptPath;
	}

	public void setScriptPath(String scriptPath) {
		this.scriptPath = scriptPath;
	}
}
