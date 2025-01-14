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

package com.flipkart.pibify.resources;

import com.flipkart.pibify.core.Pibify;

/**
 * This class is used for test
 * Author bageshwar.pn
 * Date 02/12/24
 */
public class SampleResponse {

    @Pibify(1)
    private long at;

    @Pibify(2)
    private String content;

    public SampleResponse() {
        // Jackson deserialization
    }

    public SampleResponse(long at, String content) {
        this.at = at;
        this.content = content;
    }

    public long getAt() {
        return at;
    }

    public void setAt(long at) {
        this.at = at;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
