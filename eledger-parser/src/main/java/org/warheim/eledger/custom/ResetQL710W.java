package org.warheim.eledger.custom;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.LoggerFactory;
import org.warheim.net.RequestPreparationException;
import org.warheim.net.ResponseHandlerException;
import org.warheim.net.WebCall;

/**
 *
 * @author andy
 */
public class ResetQL710W extends WebCall {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ResetQL710W.class);
    
    public ResetQL710W(String url) {
        super(200, url, WebCall.REQUEST_TYPE_POST);
    }

    //TODO: check why printer still freezes sometimes
    @Override
    public void prepareRequest(HttpRequest request) throws RequestPreparationException {
            List<NameValuePair> nameValuePairs = new ArrayList<>(3);
            nameValuePairs.add(new BasicNameValuePair("EXECUTE1", "RESET"));
            HttpPost post = (HttpPost)request;
        try {
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        } catch (UnsupportedEncodingException ex) {
            logger.error("Error while preparing request headers for call", ex);
            throw new RequestPreparationException(ex);
        }
    }

    @Override
    public String handleResponse(HttpResponse resp) throws ResponseHandlerException {
        return null;
    }
    
}
