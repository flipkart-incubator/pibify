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

package com.flipkart.pibify.core;

import java.util.logging.Logger;

/**
 * This class is used for configuring pibify
 * Author bageshwar.pn
 * Date 24/09/24
 */
public class PibifyConfiguration {

    private static final Logger logger = Logger.getLogger(PibifyConfiguration.class.getName());


    private static PibifyConfiguration INSTANCE;
    private boolean ignoreUnknownFields = true;
    private boolean ignoreUnknownEnums = true;

    private PibifyConfiguration() {
    }

    public static PibifyConfiguration instance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Attempt to access configuration before initialization");
        }
        return INSTANCE;
    }

    public static PibifyConfiguration.Builder builder() {
        return new Builder();
    }

    public boolean ignoreUnknownFields() {
        return ignoreUnknownFields;
    }

    public boolean ignoreUnknownEnums() {
        return ignoreUnknownEnums;
    }

    public static class Builder {
        private final PibifyConfiguration config;

        public Builder() {
            config = new PibifyConfiguration();
        }

        synchronized public void build() {
            // A way to keep the static instance pseudo-final
            if (PibifyConfiguration.INSTANCE != null) {
                logger.warning("Re-creating pibify configuration");
            }
            PibifyConfiguration.INSTANCE = config;
        }

        public Builder ignoreUnknownFields(boolean flag) {
            config.ignoreUnknownFields = flag;
            return this;
        }

        public Builder ignoreUnknownEnums(boolean flag) {
            config.ignoreUnknownEnums = flag;
            return this;
        }
    }

}
