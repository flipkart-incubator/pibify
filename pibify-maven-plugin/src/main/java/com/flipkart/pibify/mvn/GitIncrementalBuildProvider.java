/*
 *
 *  *Copyright [2025] [Original Author]
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

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
                        // Filter out modified and added files from git status
                        .filter(s -> s.contains("modified:") || s.contains("added:"))
                        .filter(s -> s.contains(".java"))
                        // `git status` prints in the format modified:<filename> , hence the split
                        .map(s -> s.split(":")[1])
                        // to remove leading and trailing spaces
                        .map(String::trim)
                        // there can be cases if the added/modified file could optionally be staged.
                        // In those cases `git status` reports it twice
                        .distinct()
                        .map(f -> Paths.get(f).toAbsolutePath())
                        // to filter out files that have been modified in the maven module where the plugin is configured
                        .filter(p -> p.startsWith(sourceDirectory))
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
