package org.warheim.eledger.web;

import org.warheim.net.RequestPreparationException;
import org.warheim.net.WebCall;
import org.warheim.net.ResponseHandlerException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.LoggerFactory;
import static org.warheim.eledger.web.HttpReqRespHandler.addCommonHeaders;
import static org.warheim.eledger.web.HttpReqRespHandler.addExtHeaders;

/**
 * Authorization web handler
 *
 * @author andy
 * $CMD "$BASE_HTTP/$AUTH_PAGE" 
    -H "Cookie: $AUTH_COOKIE_NAME=$COOKIE" -H "Origin: $BASE_HTTP" 
    -H 'Accept-Encoding: gzip, deflate' -H "Accept-Language: $ACCEPT_LANG" 
    -H 'Upgrade-Insecure-Requests: 1' -H "User-Agent: $USER_AGENT" 
    -H 'Content-Type: application/x-www-form-urlencoded' -H "Accept: $ACCEPT" 
    -H 'Cache-Control: max-age=0' -H "Referer: $BASE_HTTP/$DEST_PAGE" 
    -H 'Connection: keep-alive' --data "referer=1&login=$USER_NAME&password=$PASS" 
    --compressed -s -D response1-header -o response1
 */
public final class Authorize extends WebCall {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Authorize.class);

    private final String username;
    private final String pass;
    private final String destPage;
    private final String origin;
    private final String cookie;
    private final String etag;
    
    public Authorize(String url, String destPage, String origin, String username, String pass,
        String cookie, String etag) {
        super(302, url, WebCall.REQUEST_TYPE_POST);
        this.username = username;
        this.pass = pass;
        this.destPage = destPage;
        this.origin = origin;
        this.cookie = cookie;
        this.etag = etag;
    }

    @Override
    public void prepareRequest(HttpRequest request) throws RequestPreparationException {
            addCommonHeaders(request);
            addExtHeaders(request, cookie, origin, destPage, etag);
            
            List<NameValuePair> nameValuePairs = new ArrayList<>(3);
            nameValuePairs.add(new BasicNameValuePair("referer", "1"));
            nameValuePairs.add(new BasicNameValuePair("login", username));
            nameValuePairs.add(new BasicNameValuePair("password", pass));
            HttpPost post = (HttpPost)request;
        try {
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        } catch (UnsupportedEncodingException ex) {
            logger.error("Error while preparing request call headers", ex);
            throw new RequestPreparationException(ex);
        }
    }

    @Override
    public String handleResponse(HttpResponse resp) throws ResponseHandlerException {
        return null;
    }
    
}
