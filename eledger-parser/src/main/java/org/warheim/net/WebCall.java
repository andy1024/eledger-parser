package org.warheim.net;

import java.io.IOException;
import org.slf4j.LoggerFactory;
import org.warheim.di.ObjectCreationException;
import org.warheim.di.ObjectFactory;
import org.warheim.eledger.parser.Config;

/**
 * Base class for making web calls
 * Supports GET and POST, custom headers, custom request preparation and custom response handler
 *
 * @author andy
 */
public abstract class WebCall {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(WebCall.class);
    
    protected int expectedOutcomeStatus;
    protected String url;
    protected WebRequest request;
    
    public WebCall(int expectedOutcomeStatus, String url, WebRequestType requestType) {
        this.expectedOutcomeStatus = expectedOutcomeStatus;
        this.url = url;
        this.request = new WebRequestImpl(url, requestType);
    }

    public abstract void prepareRequest(WebRequest request) throws RequestPreparationException;
    
    public abstract String handleResponse(WebResponse response) throws ResponseHandlerException;
    
    public String doCall() throws IOException, WrongStatusException, ResponseHandlerException, 
            RequestPreparationException, ObjectCreationException, WebExecutionException {
        WebProcessor wp = (WebProcessor) ObjectFactory.createObject("org.warheim.net.WebProcessorApache()");
        prepareRequest(request);
        WebResponse response = wp.execute(request);
        String result;
        logger.debug(response.getBody());
        if (response.getStatus()==expectedOutcomeStatus) {
            result = handleResponse(response);
        } else {
            logger.warn("Expected status " + expectedOutcomeStatus + " got " + response.getStatus());
            throw new WrongStatusException(expectedOutcomeStatus, response.getStatus());
        }
        return result;
    }
}
