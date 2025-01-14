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

package com.flipkart.pibify.codegen.log;

/**
 * This class is used as a base class to hold logs during code spec generation
 * Author bageshwar.pn
 * Date 13/09/24
 */
abstract public class SpecGenLog {
    protected String logMessage;
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

    public void prependMessage(String msg) {
        logMessage = msg + " " + logMessage;
    }
}
