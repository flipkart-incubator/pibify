package com.flipkart.pibify.paritychecker;

import com.flipkart.pibify.codegen.stub.AbstractPibifyHandlerCache;
import com.flipkart.pibify.codegen.stub.PibifyGenerated;
import com.flipkart.pibify.sampler.AbstractPibifySampler;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.RecursiveComparisonAssert;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
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
    protected final ExecutorService executorService;
    protected final AbstractPibifySampler sampler;

    public PibifyParityChecker(AbstractPibifyHandlerCache cache,
                               IParityCheckerListener parityCheckerListener,
                               Optional<Supplier<Object>> requestContextSupplier,
                               AbstractPibifySampler sampler) {
        this(cache, parityCheckerListener, requestContextSupplier, sampler, 10, 100);
    }

    /**
     * @param cache
     * @param parityCheckerListener
     * @param requestContextSupplier
     * @param corePoolSize           Minimum number of threads to keep in the pool
     * @param maxPoolSize            Maximum number of threads in the pool
     * @param sampler
     */
    public PibifyParityChecker(AbstractPibifyHandlerCache cache,
                               IParityCheckerListener parityCheckerListener,
                               Optional<Supplier<Object>> requestContextSupplier,
                               AbstractPibifySampler sampler,
                               int corePoolSize, int maxPoolSize) {
        this.cache = cache;
        this.parityCheckerListener = parityCheckerListener;
        this.sampler = sampler;
        if (requestContextSupplier != null && requestContextSupplier.isPresent()) {
            this.requestContextSupplier = requestContextSupplier.get();
        } else {
            this.requestContextSupplier = () -> null;
        }

        // Create a custom thread pool with a bounded queue and a drop policy
        this.executorService = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
                60L, TimeUnit.SECONDS,         // keep-alive time for idle threads
                new LinkedBlockingQueue<>(100), // bounded queue with 100 capacity
                new ThreadPoolExecutor.DiscardPolicy() // silently drop tasks if queue is full
        );
    }

    /**
     * @param object           The object to check for parity
     * @param skipSamplerCheck This flag is used to skip the internal sampler check
     */
    @Override
    public void checkParity(Object object, boolean skipSamplerCheck) {
        // skipSamplerCheck lets clients control the sampler behavior from outside
        // This is helpful in cases where the cost of preparing the object for parity check is high (recompute)
        // and clients want to incur that cost only when the object will be used for parity check.
        // In those cases, the `shouldSample` on this class can be used to check sampling state in the main thread
        // and prepare the object and submit for parity check only if needed.
        if (skipSamplerCheck || this.sampler.shouldSample()) {
            // Submit async task to process response
            executorService.submit(() -> checkParityImpl(object));
        }
    }

    @Override
    public boolean shouldSample() {
        return this.sampler.shouldSample();
    }

    @SuppressWarnings("unchecked")
    private void checkParityImpl(Object object) {
        Object requestContext = null;
        Object pibifiedObject = null;
        try {
            // Convert the object to a byte array via pibify
            Optional<? extends PibifyGenerated> handler = cache.getHandler(object.getClass());

            requestContext = this.requestContextSupplier.get();
            if (!handler.isPresent()) {
                parityCheckerListener.parityCheckError(object, null, requestContext,
                        new IllegalArgumentException("No handler found for object " + object.getClass()));
            } else {

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
        } catch (Exception e) {
            parityCheckerListener.parityCheckError(object, pibifiedObject, requestContext, e);
        }
    }
}
