package org.warheim.eledger.web;

import org.warheim.eledger.parser.Config;
import org.warheim.net.WebRequest;

/**
 *
 * @author andy
 */
public class RequestDecorator {

    public static void addCommonHeaders(WebRequest request) {
        request.addHeader(Config.get(Config.KEY_HEADER_ACCEPT_KEY), Config.get(Config.KEY_HEADER_ACCEPT_VALUE));
        request.addHeader(Config.get(Config.KEY_HEADER_CONNECTION_KEY), Config.get(Config.KEY_HEADER_CONNECTION_VALUE));
        request.addHeader(Config.get(Config.KEY_HEADER_ACCEPT_ENCODING_KEY), Config.get(Config.KEY_HEADER_ACCEPT_ENCODING_VALUE));
        request.addHeader(Config.get(Config.KEY_HEADER_ACCEPT_LANGUAGE_KEY), Config.get(Config.KEY_HEADER_ACCEPT_LANGUAGE_VALUE));
        request.addHeader(Config.get(Config.KEY_HEADER_UPGRADE_INSECURE_REQUESTS_KEY), Config.get(Config.KEY_HEADER_UPGRADE_INSECURE_REQUESTS_VALUE));
        request.addHeader(Config.get(Config.KEY_HEADER_USER_AGENT_KEY), Config.get(Config.KEY_HEADER_USER_AGENT_VALUE));
    }

    public static void addExtHeaders(WebRequest request, String cookie, String origin, String referer, String etag) {
        request.addCookie(Config.get(Config.KEY_AUTH_COOKIE_NAME), cookie);
        if (origin != null) {
            request.addHeader("Origin", origin);
        }
        request.addHeader(Config.get(Config.KEY_HEADER_CONTENT_TYPE_KEY), Config.get(Config.KEY_HEADER_CONTENT_TYPE_VALUE));
        request.addHeader(Config.get(Config.KEY_HEADER_CACHE_CONTROL_KEY), Config.get(Config.KEY_HEADER_CACHE_CONTROL_VALUE));
        if (referer != null) {
            request.addHeader(Config.get(Config.KEY_HEADER_REFERER_KEY), referer);
        }
        if (etag != null) {
            request.addHeader(Config.get(Config.KEY_HEADER_IF_NONE_MATCH_KEY), etag);
        }
    }

}
