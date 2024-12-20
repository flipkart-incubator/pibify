package com.flipkart.pibify.paritychecker;

import com.flipkart.pibify.codegen.stub.AbstractPibifyHandlerCache;
import com.flipkart.pibify.codegen.stub.PibifyGenerated;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.RecursiveComparisonAssert;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * This class hosts the logic to perform the actual parity and trigger relevant callbacks
 * Author bageshwar.pn
 * Date 12/12/24
 */
public class PibifyParityChecker implements IParityChecker {

    private final AbstractPibifyHandlerCache cache;
    private final IParityCheckerListener parityCheckerListener;
    private final Supplier<Object> requestContextSupplier;

    public PibifyParityChecker(AbstractPibifyHandlerCache cache,
                               IParityCheckerListener parityCheckerListener,
                               Optional<Supplier<Object>> requestContextSupplier) {
        this.cache = cache;
        this.parityCheckerListener = parityCheckerListener;
        if (requestContextSupplier != null && requestContextSupplier.isPresent()) {
            this.requestContextSupplier = requestContextSupplier.get();
        } else {
            this.requestContextSupplier = () -> null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void checkParity(Object object) {
        // Convert the object to a byte array via pibify
        Optional<? extends PibifyGenerated> handler = cache.getHandler(object.getClass());

        Object requestContext = this.requestContextSupplier.get();
        if (!handler.isPresent()) {
            parityCheckerListener.parityCheckError(object, null, requestContext,
                    new IllegalArgumentException("No handler found for object " + object.getClass()));
        } else {
            Object pibifiedObject = null;
            try {
                byte[] pibified = handler.get().serialize(object);
                // Convert the byte array back to an object via pibify
                pibifiedObject = handler.get().deserialize(pibified, object.getClass());

                // Compare the original object with the pibified object
                RecursiveComparisonAssert<?> assertionObject = Assertions.assertThat(object).usingRecursiveComparison().isEqualTo(pibifiedObject);
                parityCheckerListener.parityCheckSucceeded(object, pibifiedObject, requestContext);
            } catch (AssertionError ae) {
                parityCheckerListener.parityCheckFailed(object, pibifiedObject, requestContext, ae);
            } catch (Throwable t) {
                parityCheckerListener.parityCheckError(object, pibifiedObject, requestContext, t);
            }
        }
    }
}
