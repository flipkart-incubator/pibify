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
 * This class is used for holding logs during Code Spec gen for a class
 * Author bageshwar.pn
 * Date 13/09/24
 */
public class CodeSpecGenLog extends SpecGenLog {

    public CodeSpecGenLog(SpecGenLogLevel logLevel, String logMessage) {
        super(logLevel, logMessage);
    }
}
