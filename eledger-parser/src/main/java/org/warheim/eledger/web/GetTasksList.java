/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.warheim.eledger.web;

import org.warheim.net.ReturnWebPageCall;
import org.warheim.net.WebCall;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

/**
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
