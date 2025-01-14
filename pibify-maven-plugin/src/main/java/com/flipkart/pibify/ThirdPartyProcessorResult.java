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

package com.flipkart.pibify;

import com.flipkart.pibify.codegen.log.SpecGenLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class is used for sending data from the third party processor to parent class.
 * Author bageshwar.pn
 * Date 31/10/24
 */
public class ThirdPartyProcessorResult {
    private Object data;
    private List<SpecGenLog> logs;

    public ThirdPartyProcessorResult() {
        logs = new ArrayList<>();
        data = null;
    }

    public Optional<Object> getData() {
        if (data == null) {
            return Optional.empty();
        } else {
            return Optional.of(data);
        }
    }

    public void setData(Object data) {
        this.data = data;
    }

    public List<SpecGenLog> getLogs() {
        return logs;
    }

    public void addLog(SpecGenLog log) {
        logs.add(log);
    }
}
