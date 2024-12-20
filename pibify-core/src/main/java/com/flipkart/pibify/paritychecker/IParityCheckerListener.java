package com.flipkart.pibify.paritychecker;

/**
 * This interface is used to hook-up callbacks for key events when running in pibify-shadow mode
 * Author bageshwar.pn
 * Date 12/12/24
 */
public interface IParityCheckerListener {

    // Fired when objects are same
    void parityCheckSucceeded(Object primary, Object pibified, Object requestContext);

    // Fired when objects are different
    void parityCheckFailed(Object primary, Object pibified, Object requestContext, AssertionError ae);

    // Fired when there is an exception during the parity check
    void parityCheckError(Object primary, Object pibified, Object requestContext, Throwable e);
}
