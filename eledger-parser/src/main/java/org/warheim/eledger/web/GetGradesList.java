package org.warheim.eledger.web;

import org.warheim.net.RequestPreparationException;
import org.warheim.net.ReturnWebPageCall;
import org.warheim.net.WebRequest;
import org.warheim.net.WebRequestType;

/**
 * Gets grades
 *
 * @author andy
 */
public class GetGradesList extends ReturnWebPageCall {

    private final String cookie;
    private final String etag;

    public GetGradesList(String url, String cookie, String etag) {
        super(200, url, WebRequestType.GET);
        this.cookie = cookie;
        this.etag = etag;
    }
    
    @Override
    public void prepareRequest(WebRequest request) throws RequestPreparationException {
        RequestDecorator.addCommonHeaders(request);
        RequestDecorator.addExtHeaders(request, cookie, url, null, etag);
    }
    
}
