package org.warheim.net;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author andy
 */
public class WebResponseImpl implements WebResponse {
    private final Map<String, String> headers = new HashMap<>();
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
        str.append(status);
        str.append("\n");
        for (String key: headers.keySet()) {
            str.append(" ").append(key).append("=").append(headers.get(key));
        }
        str.append("\n");
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

}
