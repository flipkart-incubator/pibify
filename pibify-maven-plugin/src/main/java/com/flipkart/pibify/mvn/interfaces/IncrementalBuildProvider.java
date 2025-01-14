package com.flipkart.pibify.mvn.interfaces;

import org.apache.maven.plugin.logging.Log;

import java.nio.file.Path;
import java.util.List;

/**
 * This interface is designed for detecting the files changed during an incremental build
 * Author bageshwar.pn
 * Date 13/01/25
 */
public interface IncrementalBuildProvider {
    List<Path> changedFiles(Log log, Path sourceDirector);
}
