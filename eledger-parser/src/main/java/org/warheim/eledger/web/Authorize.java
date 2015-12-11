package org.warheim.eledger.web;

import java.util.HashSet;
import org.warheim.net.RequestPreparationException;
import org.warheim.net.WebCall;
import org.warheim.net.ResponseHandlerException;
import org.slf4j.LoggerFactory;
import static org.warheim.eledger.web.HttpReqRespHandler.addCommonHeaders;
import static org.warheim.eledger.web.HttpReqRespHandler.addExtHeaders;
import org.warheim.net.WebRequest;
import org.warheim.net.WebRequestType;
import org.warheim.net.WebResponse;

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
        super("200,302", url, WebRequestType.POST);
        this.username = username;
        this.pass = pass;
        this.destPage = destPage;
        this.origin = origin;
        this.cookie = cookie;
        this.etag = etag;
    }

    @Override
    public void prepareRequest(WebRequest request) throws RequestPreparationException {
        addCommonHeaders(request);
        addExtHeaders(request, cookie, origin, destPage, etag);
        request.addDataEntry("referer", "1");
        request.addDataEntry("login", username);
        request.addDataEntry("password", pass);
    }

    @Override
    public String handleResponse(WebResponse response) throws ResponseHandlerException {
        return null;
    }
    
}
