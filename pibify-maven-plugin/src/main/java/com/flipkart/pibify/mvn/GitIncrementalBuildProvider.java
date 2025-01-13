package com.flipkart.pibify.mvn;

import com.flipkart.pibify.mvn.interfaces.IncrementalBuildProvider;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * This implementation of IncrementalBuildProvider finds the modified/added files via git
 * Author bageshwar.pn
 * Date 13/01/25
 */
public class GitIncrementalBuildProvider implements IncrementalBuildProvider {

    private static final String gitCommand = "git status";

    public static void main(String[] args) {
        GitIncrementalBuildProvider gitIncrementalBuildProvider = new GitIncrementalBuildProvider();
        System.out.println(gitIncrementalBuildProvider.changedFiles(new SystemStreamLog(), Paths.get("/Users/bageshwar.pn/src/pibify/pibify-maven-plugin")));
    }

    @Override
    public List<Path> changedFiles(Log log, Path sourceDirectory) {
        // execute gitCommand and get hold of the output stream
        try {
            Process process = Runtime.getRuntime().exec(gitCommand);
            boolean waited = process.waitFor(10, TimeUnit.SECONDS);
            if (waited && process.exitValue() == 0) {
                // read the output stream and return the list of files
                String output = new String(IOUtils.readFully(process.getInputStream(), process.getInputStream().available()));
                return Arrays.stream(output.split("\n"))
                        .filter(s -> s.contains("modified:") || s.contains("added:"))
                        .filter(s -> s.contains(".java"))
                        .map(s -> s.split(":")[1])
                        .map(String::trim)
                        .distinct()
                        .map(Paths::get)
                        .collect(Collectors.toList());
            } else {
                log.error(new String(IOUtils.readFully(process.getErrorStream(), process.getErrorStream().available())));
                log.error("Could not get status via git status");
            }
        } catch (InterruptedException | IOException e) {
            log.error(e);
        }

        return new ArrayList<>();
    }
}
