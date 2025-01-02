package com.flipkart.pibify.paritychecker.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.pibify.paritychecker.IParityChecker;

import java.io.IOException;
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
    protected final IParityChecker parityChecker;

    /**
     * Constructor with configurable executor service and response processor
     *
     * @param parityChecker The processor to handle response objects
     */
    public AbstractJsonResponseFilter(IParityChecker parityChecker) {
        this.objectMapper = new ObjectMapper();
        this.parityChecker = parityChecker;
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
                // Capture the response entity and process it
                processResponse(getResponseEntity(responseContext));
            } catch (Exception e) {
                // Log but do not rethrow to prevent impacting original response
                LOGGER.fine("Error in async response processing " + e.getMessage());
            }
        }
    }
}