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
     * @return true if Pibify should be enabled
     */
    public abstract boolean enabled();

    /**
     * @return An int between 0 and 1000 to denote a 0.1 % traffic to flow to pibify, Sampled randomly
     */
    public abstract int getSamplePercentage();

    public final boolean shouldSample() {
        if (enabled()) {
            long current = System.currentTimeMillis();
            return current % (MAX_SAMPLE / getSamplePercentage()) == 0;
        } else {
            return false;
        }
    }
}
