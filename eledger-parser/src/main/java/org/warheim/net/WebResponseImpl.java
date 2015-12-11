package org.warheim.net;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author andy
 */
public class WebResponseImpl implements WebResponse {
    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, String> cookies = new HashMap<>();
    private String body;
    private int status;

    public WebResponseImpl(int status, String body) {
        this.status = status;
        this.body = body;
    }
    
    @Override
    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public String getHeader(String key) {
        return headers.get(key);
    }

    @Override
    public String show() {
        StringBuilder str = new StringBuilder();
        str.append("Status=").append(status).append("\n");
        str.append("Headers:\n");
        for (String key: headers.keySet()) {
            str.append(" ").append(key).append("=").append(headers.get(key)).append("\n");
        }
        str.append("Cookies:\n");
        for (String key: cookies.keySet()) {
            str.append(" ").append(key).append("=").append(cookies.get(key)).append("\n");
        }
        str.append("Body:\n");
        str.append(body);
        str.append("\n");
        return str.toString();
    }

    @Override
    public String getBody() {
        return body;
    }

    @Override
    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String getCookie(String key) {
        return cookies.get(key);
    }

    @Override
    public void addCookie(String key, String value) {
        cookies.put(key, value);
    }

    @Override
    public Map<String, String> getCookies() {
        return cookies;
    }
}
