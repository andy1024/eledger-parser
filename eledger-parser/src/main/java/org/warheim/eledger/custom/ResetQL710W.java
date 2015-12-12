package org.warheim.eledger.custom;

import org.slf4j.LoggerFactory;
import org.warheim.net.RequestPreparationException;
import org.warheim.net.ResponseHandlerException;
import org.warheim.net.WebCall;
import org.warheim.net.WebRequest;
import org.warheim.net.WebRequestType;
import org.warheim.net.WebResponse;

/**
 *
 * @author andy
 */
public class ResetQL710W extends WebCall {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ResetQL710W.class);
    
    public ResetQL710W(String url) {
        super(200, url, WebRequestType.POST);
    }

    @Override
    public void prepareRequest(WebRequest request) throws RequestPreparationException {
        request.addDataEntry("EXECUTE1", "RESET");
    }

    @Override
    public String handleResponse(WebResponse resp) throws ResponseHandlerException {
        return null;
    }
    
}
