package io.yoshizaki2104.heavyjarbuildrx.cli.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HjbRxConfig implements Serializable {
    /**
     *
     */

    private static final long serialVersionUID = 1L;

    private String logDirectory;

    private Integer parallelSize;

    private List<ScriptDef> scripts;

    @JsonProperty("logDirectory")
    public String getLogDirectory() {
        return logDirectory;
    }

    public void setLogDirectory(String logDirectory) {
        this.logDirectory = logDirectory;
    }

    @JsonProperty("parallelSize")
    public Integer getParallelSize() {
        return parallelSize;
    }

    public void setParallelSize(Integer parallelSize) {
        this.parallelSize = parallelSize;
    }

    @JsonProperty("scripts")
    public List<ScriptDef> getScripts() {
        return scripts;
    }

    public void setScripts(List<ScriptDef> scripts) {
        this.scripts = scripts;
    }
}
