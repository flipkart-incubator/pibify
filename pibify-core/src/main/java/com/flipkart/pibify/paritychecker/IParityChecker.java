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

package com.flipkart.pibify.paritychecker;

/**
 * Functional Interface to check parity of pibified object
 * Author bageshwar.pn
 * Date 12/12/24
 */
public interface IParityChecker {

    /**
     * To be used in cases the client is not expecting any parity check
     */
    IParityChecker NO_OP = new IParityChecker() {
        @Override
        public void checkParity(Object object, boolean skipSamplerCheck) {
            // No-op
        }

        @Override
        public boolean shouldSample() {
            return false;
        }
    };

    /**
     * Checks the parity of the object. Internally ensures that the sampling check is performed.
     *
     * @param object
     */
    default void checkParity(Object object) {
        checkParity(object, false);
    }

    /**
     * Check parity of the supplied object and optionally skip the sampler check if it has been done beforehand
     *
     * @param object
     * @param skipSamplerCheck
     */
    void checkParity(Object object, boolean skipSamplerCheck);

    /**
     * Method exposed for clients to perform the sampling check before checking parity.
     *
     * @return true if this request should be sampled for parity check
     */
    boolean shouldSample();
}
