package com.flipkart.pibify.mvn.util;

import org.apache.maven.plugin.logging.Log;

/**
 * This class is used for logging with a prefix
 * Author bageshwar.pn
 * Date 29/09/24
 */
public class PrefixLog implements Log {
    private final Log underlying;
    private final String prefix;

    public PrefixLog(Log underlying, String prefix) {
        this.underlying = underlying;
        this.prefix = prefix + "\t";
    }


    @Override
    public boolean isDebugEnabled() {
        return underlying.isDebugEnabled();
    }

    @Override
    public void debug(CharSequence charSequence) {
        underlying.debug(prefix + charSequence);
    }

    @Override
    public void debug(CharSequence charSequence, Throwable throwable) {
        underlying.debug(prefix + charSequence, throwable);
    }

    @Override
    public void debug(Throwable throwable) {
        underlying.debug(throwable);
    }

    @Override
    public boolean isInfoEnabled() {
        return underlying.isInfoEnabled();
    }

    @Override
    public void info(CharSequence charSequence) {
        underlying.info(prefix + charSequence);
    }

    @Override
    public void info(CharSequence charSequence, Throwable throwable) {
        underlying.info(prefix + charSequence, throwable);
    }

    @Override
    public void info(Throwable throwable) {
        underlying.info(throwable);
    }

    @Override
    public boolean isWarnEnabled() {
        return underlying.isWarnEnabled();
    }

    @Override
    public void warn(CharSequence charSequence) {
        underlying.warn(prefix + charSequence);
    }

    @Override
    public void warn(CharSequence charSequence, Throwable throwable) {
        underlying.warn(prefix + charSequence, throwable);
    }

    @Override
    public void warn(Throwable throwable) {
        underlying.warn(throwable);
    }

    @Override
    public boolean isErrorEnabled() {
        return underlying.isErrorEnabled();
    }

    @Override
    public void error(CharSequence charSequence) {
        underlying.error(prefix + charSequence);
    }

    @Override
    public void error(CharSequence charSequence, Throwable throwable) {
        underlying.error(prefix + charSequence, throwable);
    }

    @Override
    public void error(Throwable throwable) {
        underlying.error(throwable);
    }
}
