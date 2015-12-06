package org.warheim.net;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;
import org.warheim.eledger.web.GetTasksList;

/**
 * Base class for web calls that return response page contents
 *
 * @author andy
 */
public abstract class ReturnWebPageCall extends WebCall {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ReturnWebPageCall.class);

    public ReturnWebPageCall(int expectedOutcomeStatus, String url, String requestType) {
        super(expectedOutcomeStatus, url, requestType);
    }

    @Override
    public String handleResponse(HttpResponse resp) throws ResponseHandlerException {
        String retval = null;
        HttpEntity entity = resp.getEntity();
        try {
            retval = (entity != null ? EntityUtils.toString(entity) : null);
        } catch (IOException | ParseException ex) {
            logger.error("Error while reading response from server", ex);
            logger.debug("Entity:" + entity.toString());
            throw new ResponseHandlerException(ex);
        }
        return retval;
    }
    
}
