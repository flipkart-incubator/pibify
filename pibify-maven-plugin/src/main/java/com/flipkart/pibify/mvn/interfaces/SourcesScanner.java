package com.flipkart.pibify.mvn.interfaces;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import java.util.List;
import java.util.Set;

/**
 * This is the interface to scan sources.
 * Author bageshwar.pn
 * Date 28/09/24
 */
public abstract class SourcesScanner {

    private final Log log;

    protected SourcesScanner(Log log) {
        this.log = log;
    }

    protected Log getLog() {
        return log;
    }

    /**
     * This method should return a list of classes that have the pibify annotation in 1 or more fields.
     *
     * @return
     */
    public abstract Set<Class<?>> getPibifyAnnotatedClasses(String sourceDirectory, List<String> excludes) throws MojoExecutionException;
}
