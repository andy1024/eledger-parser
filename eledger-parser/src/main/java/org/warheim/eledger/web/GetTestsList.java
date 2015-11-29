/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.warheim.eledger.web;

import org.apache.http.HttpRequest;
import org.warheim.net.RequestPreparationException;
import org.warheim.net.ReturnWebPageCall;
import org.warheim.net.WebCall;

/**
 *
 * @author andy
 */
public class GetTestsList extends ReturnWebPageCall {

    private final String cookie;
    private final String etag;

    public GetTestsList(String url, String cookie, String etag) {
        super(200, url, WebCall.REQUEST_TYPE_GET);
        this.cookie = cookie;
        this.etag = etag;
    }
    
    @Override
    public void prepareRequest(HttpRequest request) throws RequestPreparationException {
        HttpReqRespHandler.addCommonHeaders(request);
        HttpReqRespHandler.addExtHeaders(request, cookie, url, null, etag);
    }
    
}
