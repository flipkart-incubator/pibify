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

package com.flipkart.pibify.sampler;

/**
 * This interface is designed for letting the clients configure Pibify sampling.
 * Can be used in Parity Checker as well as production for controlled roll-out
 * Author bageshwar.pn
 * Date 19/12/24
 */
public abstract class AbstractPibifySampler {

    private static final int MAX_SAMPLE = 1000;

    /**
     * To be used in cases the client is not expecting any sampling
     */
    public static final AbstractPibifySampler DEFAULT_SAMPLER = new AbstractPibifySampler() {

        @Override
        public int getSamplePercentage() {
            return 1000;
        }
    };

    /**
     * @return An int between 0 and 1000 to denote a 0.1 % traffic to flow to pibify, Sampled randomly
     */
    public abstract int getSamplePercentage();

    /**
     * Clients can override this method to implement their own sampling logic
     *
     * @return
     */
    public boolean shouldSample() {
        int samplePercentage = getSamplePercentage();
        if (samplePercentage != 0) {
            long current = System.currentTimeMillis();
            return current % (MAX_SAMPLE / samplePercentage) == 0;
        } else {
            return false;
        }
    }
}
