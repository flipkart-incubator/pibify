package com.flipkart.pibify.paritychecker.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.pibify.paritychecker.IParityChecker;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Base abstract class for JSON response processing that can be extended
 * for both javax and jakarta filter implementations.
 *
 * @param <RequestContextType>  The request context type (differs between javax and jakarta)
 * @param <ResponseContextType> The response context type (differs between javax and jakarta)
 *                              <p>
 *                              Author bageshwar.pn
 *                              Date 12/12/24
 */
public abstract class AbstractJsonResponseFilter<RequestContextType, ResponseContextType> {
    private static final Logger LOGGER = Logger.getLogger(AbstractJsonResponseFilter.class.getName());

    protected final ObjectMapper objectMapper;
    protected final ExecutorService executorService;
    protected final IParityChecker parityChecker;

    /**
     * Constructor with configurable executor service and response processor
     *
     * @param parityChecker The processor to handle response objects
     * @param corePoolSize  Minimum number of threads to keep in the pool
     * @param maxPoolSize   Maximum number of threads in the pool
     */
    public AbstractJsonResponseFilter(IParityChecker parityChecker, int corePoolSize, int maxPoolSize) {
        this.objectMapper = new ObjectMapper();
        this.parityChecker = parityChecker;

        // Create a custom thread pool with a bounded queue and a drop policy
        this.executorService = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
                60L, TimeUnit.SECONDS,         // keep-alive time for idle threads
                new LinkedBlockingQueue<>(100), // bounded queue with 100 capacity
                new ThreadPoolExecutor.DiscardPolicy() // silently drop tasks if queue is full
        );
    }

    /**
     * Common method to check if the response is JSON
     *
     * @param responseContext The response context
     * @return true if the response is JSON, false otherwise
     */
    protected abstract boolean isJsonResponse(ResponseContextType responseContext);

    /**
     * Common method to get the entity from the response context
     *
     * @param responseContext The response context
     * @return The response entity
     */
    protected abstract Object getResponseEntity(ResponseContextType responseContext);

    /**
     * Process the response object using the provided processor
     * Uses unchecked cast, so requires careful usage
     */
    @SuppressWarnings("unchecked")
    protected <T> void processResponse(Object responseEntity) {
        if (responseEntity == null) {
            return;
        }

        try {
            // If it's already a JSON object, use as-is
            // Otherwise, attempt to convert
            T processableEntity = responseEntity instanceof String
                    ? (T) objectMapper.readValue((String) responseEntity, Object.class)
                    : (T) responseEntity;

            parityChecker.checkParity(processableEntity);
        } catch (Exception e) {
            // Silent failure as per requirements
            LOGGER.fine("Error processing response " + e.getMessage());
        }
    }

    /**
     * Common filter method to be implemented by subclasses
     *
     * @param requestContext  The request context
     * @param responseContext The response context
     * @throws IOException if an I/O error occurs
     */
    public void filter(RequestContextType requestContext, ResponseContextType responseContext) throws IOException {
        // Check if response is JSON
        if (isJsonResponse(responseContext)) {
            try {
                // Capture the response entity
                Object responseEntity = getResponseEntity(responseContext);

                // Submit async task to process response
                executorService.submit(() -> processResponse(responseEntity));
            } catch (Exception e) {
                // Log but do not rethrow to prevent impacting original response
                LOGGER.fine("Error in async response processing " + e.getMessage());
            }
        }
    }

    /**
     * Shutdown method to gracefully close the executor service
     */
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}