package org.warheim.eledger.web;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.warheim.eledger.parser.Config;
import static org.warheim.eledger.web.HttpReqRespHandler.addCommonHeaders;
import org.warheim.net.RequestPreparationException;
import org.warheim.net.ResponseHandlerException;
import org.warheim.net.WebCall;

/**
 * Logout call
 * 
 * @author andy
 */
public final class Logout extends WebCall {

    private final String referer;
    private final String cookie;
    
    public Logout(String url, String referer, String cookie) {
        super(200, url, WebCall.REQUEST_TYPE_GET);
        this.referer = referer;
        this.cookie = cookie;
    }

    @Override
    public void prepareRequest(HttpRequest request) throws RequestPreparationException {
        addCommonHeaders(request);
        request.addHeader("Cookie", Config.get(Config.KEY_AUTH_COOKIE_NAME) + "=" + cookie);
        if (referer!=null) {
            request.addHeader(Config.get(Config.KEY_HEADER_REFERER_KEY), referer);
        }
    }

    @Override
    public String handleResponse(HttpResponse resp) throws ResponseHandlerException {
        return null;
    }

}