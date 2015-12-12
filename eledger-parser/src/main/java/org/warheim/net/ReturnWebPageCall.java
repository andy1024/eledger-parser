package org.warheim.net;

import org.slf4j.LoggerFactory;

/**
 * Base class for web calls that return response page contents
 *
 * @author andy
 */
public abstract class ReturnWebPageCall extends WebCall {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ReturnWebPageCall.class);

    public ReturnWebPageCall(int expectedOutcomeStatus, String url, WebRequestType requestType) {
        super(expectedOutcomeStatus, url, requestType);
    }

    @Override
    public String handleResponse(WebResponse resp) throws ResponseHandlerException {
        return resp.getBody();
    }
    
}
