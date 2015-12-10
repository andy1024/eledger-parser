package org.warheim.eledger.web;

import org.warheim.net.WebCall;
import org.warheim.net.ResponseHandlerException;
import static org.warheim.eledger.web.HttpReqRespHandler.addCommonHeaders;
import static org.warheim.eledger.web.HttpReqRespHandler.extractCookieValue;
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
        addCommonHeaders(request);
    }

    @Override
    public String handleResponse(WebResponse resp) throws ResponseHandlerException {
        etag = resp.getHeader("Etag");
        cookie = extractCookieValue(resp.getHeader("Set-Cookie"));
        return null;
    }

    public String getEtag() {
        return etag;
    }

    public String getCookie() {
        return cookie;
    }

}
