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
