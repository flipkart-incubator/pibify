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

package com.flipkart.pibify.mvn.util;

import org.apache.maven.plugin.logging.Log;

/**
 * This class is used for logging with a prefix
 * Author bageshwar.pn
 * Date 29/09/24
 */
public class PrefixLog implements Log {

    // ANSI escape code constants for text colors
    String RESET = "\u001B[0m";
    String RED = "\u001B[31m";
    String GREEN = "\u001B[32m";
    String BLUE = "\u001B[34m";
    String YELLOW = "\u001B[33m";

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
        underlying.info(BLUE + prefix + RESET + charSequence);
    }

    @Override
    public void info(CharSequence charSequence, Throwable throwable) {
        underlying.info(BLUE + prefix + RESET + charSequence, throwable);
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
        underlying.warn(YELLOW + prefix + RESET + charSequence);
    }

    @Override
    public void warn(CharSequence charSequence, Throwable throwable) {
        underlying.warn(YELLOW + prefix + RESET + charSequence, throwable);
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
        underlying.error(RED + prefix + RESET + charSequence);
    }

    @Override
    public void error(CharSequence charSequence, Throwable throwable) {
        underlying.error(RED + prefix + RESET + charSequence, throwable);
    }

    @Override
    public void error(Throwable throwable) {
        underlying.error(throwable);
    }
}
