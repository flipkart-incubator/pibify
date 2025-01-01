package com.flipkart.pibify.paritychecker;

/**
 * Functional Interface to check parity of pibified object
 * Author bageshwar.pn
 * Date 12/12/24
 */
public interface IParityChecker {

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
