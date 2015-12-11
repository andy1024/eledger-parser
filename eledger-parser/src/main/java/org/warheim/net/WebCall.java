package org.warheim.net;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import org.slf4j.LoggerFactory;
import org.warheim.di.ObjectCreationException;
import org.warheim.di.ObjectFactory;

/**
 * Base class for making web calls
 * Supports GET and POST, custom headers, custom request preparation and custom response handler
 *
 * @author andy
 */
public abstract class WebCall {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(WebCall.class);
    
    protected Collection<Integer> expectedOutcomeStatus = new HashSet<>();
    protected String url;
    protected WebRequest request;
    
    public WebCall(String expectedOutcomeStatiList, String url, WebRequestType requestType) {
        String[] expArr = expectedOutcomeStatiList.split(",");
        for (String exp: expArr) {
            this.expectedOutcomeStatus.add(Integer.parseInt(exp));
        }
        this.url = url;
        this.request = new WebRequestImpl(url, requestType);
    }

    public WebCall(Integer expectedOutcomeStatus, String url, WebRequestType requestType) {
        this.expectedOutcomeStatus.add(expectedOutcomeStatus);
        this.url = url;
        this.request = new WebRequestImpl(url, requestType);
    }

    public abstract void prepareRequest(WebRequest request) throws RequestPreparationException;
    
    public abstract String handleResponse(WebResponse response) throws ResponseHandlerException;
    
    public String doCall() throws IOException, WrongStatusException, ResponseHandlerException, 
            RequestPreparationException, ObjectCreationException, WebExecutionException {
        WebProcessor wp = (WebProcessor) ObjectFactory.createObject("org.warheim.net.WebProcessorJsoup()");
        prepareRequest(request);
        WebResponse response = wp.execute(request);
        String result;
        logger.debug(response.getBody());
        if (expectedOutcomeStatus.contains(response.getStatus())) {
            result = handleResponse(response);
        } else {
            logger.warn("Expected status " + expectedOutcomeStatus.toString() + " got " + response.getStatus());
            throw new WrongStatusException(expectedOutcomeStatus, response.getStatus());
        }
        return result;
    }
}
