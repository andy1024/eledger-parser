package org.warheim.eledger.web;

import org.warheim.net.ReturnWebPageCall;
import org.warheim.net.WebCall;
import org.apache.http.HttpRequest;

/**
 * Gets tasks
 *
 * @author andy
 * 
 * Example curl request:
 * $CMD "$BASE_HTTP/$DEST_PAGE" 
    -H "Cookie: $AUTH_COOKIE_NAME=$COOKIE"
    -H 'Accept-Encoding: gzip, deflate, sdch' -H "Accept-Language: $ACCEPT_LANG" 
    -H 'Upgrade-Insecure-Requests: 1' -H "User-Agent: $USER_AGENT" 
    -H "Accept: $ACCEPT" 
    -H 'Cache-Control: max-age=0' -H "Referer: $BASE_HTTP/$DEST_PAGE" 
    -H 'Connection: keep-alive' 
 * 
 */
public final class GetTasksList extends ReturnWebPageCall {

    private final String cookie;
    private final String etag;

    public GetTasksList(String url, String cookie, String etag) {
        super(200, url, WebCall.REQUEST_TYPE_GET);
        this.cookie = cookie;
        this.etag = etag;
    }

    @Override
    public void prepareRequest(HttpRequest request) {
        HttpReqRespHandler.addCommonHeaders(request);
        HttpReqRespHandler.addExtHeaders(request, cookie, url, null, etag);
    }

}
