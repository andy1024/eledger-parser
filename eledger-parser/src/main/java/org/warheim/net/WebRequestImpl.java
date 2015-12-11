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
    private final Map<String, String> cookies = new HashMap<>();
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
        str.append("Request ").append(url).append(" ").append(type.getName()).append("\n");
        str.append("Headers:\n");
        for (String key: headers.keySet()) {
            str.append(" ").append(key).append("=").append(headers.get(key)).append("\n");
        }
        str.append("Cookies:\n");
        for (String key: cookies.keySet()) {
            str.append(" ").append(key).append("=").append(cookies.get(key)).append("\n");
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
