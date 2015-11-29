package org.warheim.net;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.warheim.eledger.web.GetTasksList;

/**
 * Base class for web calls that return response page contents
 *
 * @author andy
 */
public abstract class ReturnWebPageCall extends WebCall {

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
            Logger.getLogger(GetTasksList.class.getName()).log(Level.SEVERE, null, ex);
            throw new ResponseHandlerException(ex);
        }
        return retval;
    }
    
}
