package org.warheim.eledger.web;

import org.warheim.net.WebCall;
import org.warheim.net.ResponseHandlerException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import static org.warheim.eledger.web.HttpReqRespHandler.addCommonHeaders;
import static org.warheim.eledger.web.HttpReqRespHandler.extractCookieValue;

/**
 * Initializes web session
 *
 * @author andy
 */
public final class InitiateConnection extends WebCall {
    private String etag = null;
    private String cookie = null;

    public InitiateConnection(String url) {
        super(200, url, WebCall.REQUEST_TYPE_GET);
    }

    @Override
    public void prepareRequest(HttpRequest request) {
        addCommonHeaders(request);
    }

    @Override
    public String handleResponse(HttpResponse resp) throws ResponseHandlerException {
        etag = resp.getFirstHeader("Etag").getValue();
        cookie = extractCookieValue(resp.getFirstHeader("Set-Cookie").getValue());
        return null;
    }

    public String getEtag() {
        return etag;
    }

    public String getCookie() {
        return cookie;
    }

}
