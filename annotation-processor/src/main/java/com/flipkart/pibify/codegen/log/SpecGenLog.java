package com.flipkart.pibify.codegen.log;

/**
 * This class is used as a base class to hold logs during code spec generation
 * Author bageshwar.pn
 * Date 13/09/24
 */
abstract public class SpecGenLog {
    protected final String logMessage;
    private final SpecGenLogLevel logLevel;

    public SpecGenLog(SpecGenLogLevel logLevel, String logMessage) {
        this.logLevel = logLevel;
        this.logMessage = logMessage;
    }

    public SpecGenLogLevel getLogLevel() {
        return logLevel;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
