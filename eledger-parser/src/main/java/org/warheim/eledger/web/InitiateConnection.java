package org.warheim.eledger.web;

import org.warheim.eledger.parser.Config;
import org.warheim.net.WebCall;
import org.warheim.net.ResponseHandlerException;
import org.warheim.net.WebRequest;
import org.warheim.net.WebRequestType;
import org.warheim.net.WebResponse;

/**
 * Initializes web session
 *
 * @author andy
 */
public final class InitiateConnection extends WebCall {
    private String etag = null;
    private String cookie = null;

    public InitiateConnection(String url) {
        super(200, url, WebRequestType.GET);
    }

    @Override
    public void prepareRequest(WebRequest request) {
        RequestDecorator.addCommonHeaders(request);
    }

    @Override
    public String handleResponse(WebResponse resp) throws ResponseHandlerException {
        etag = resp.getHeader("Etag");
        cookie = resp.getCookie(Config.get(Config.KEY_AUTH_COOKIE_NAME));
        return null;
    }

    public String getEtag() {
        return etag;
    }

    public String getCookie() {
        return cookie;
    }

}
