package org.warheim.eledger.web;

import org.apache.http.HttpRequest;
import org.warheim.eledger.parser.Config;
import org.warheim.net.RequestPreparationException;
import org.warheim.net.ReturnWebPageCall;
import org.warheim.net.WebCall;

/**
 * Gets single message
 *
 * @author andy
 */
public class GetMessage extends ReturnWebPageCall {

    private final String cookie;
    private final String etag;
    private final String msgId;
    private final String referer;

    public GetMessage(String url, String msgId, String cookie, String etag, String referer) {
        super(200, url+"?id="+msgId, WebCall.REQUEST_TYPE_GET);
        this.cookie = cookie;
        this.etag = etag;
        this.msgId = msgId;
        this.referer = referer;
    }

    @Override
    public void prepareRequest(HttpRequest request) throws RequestPreparationException {
        HttpReqRespHandler.addCommonHeaders(request);
        HttpReqRespHandler.addExtHeaders(request, cookie, null, referer, null);
        request.addHeader(Config.get(Config.KEY_HEADER_AJAX_REQUESTED_WITH_KEY), Config.get(Config.KEY_HEADER_AJAX_REQUESTED_WITH_VALUE));
    }
    
}
