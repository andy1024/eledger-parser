package org.warheim.net;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author andy
 */
public class WebRequestImpl implements WebRequest {
    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, String> dataEntries = new HashMap<>();
    private WebRequestType type;
    private String url;

    public WebRequestImpl(String url, WebRequestType type) {
        this.url = url;
        this.type = type;
    }
    
    @Override
    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    @Override
    public WebRequestType getType() {
        return type;
    }
    
    @Override
    public void setType(WebRequestType type) {
        this.type = type;
    }

    @Override
    public String show() {
        StringBuilder str = new StringBuilder();
        str.append(url).append(" ").append(type.getName());
        str.append("\n");
        for (String key: headers.keySet()) {
            str.append(" ").append(key).append("=").append(headers.get(key));
        }
        str.append("\n");
        return str.toString();
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
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
    public void addDataEntry(String key, String value) {
        dataEntries.put(key, value);
    }

    @Override
    public Map<String, String> getDataEntries() {
        return dataEntries;
    }

}
