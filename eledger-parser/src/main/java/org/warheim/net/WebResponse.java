package org.warheim.net;

import java.util.Map;

/**
 *
 * @author andy
 */
public interface WebResponse {
    public void addHeader(String key, String value);
    public Map<String, String> getHeaders();
    public String getHeader(String key);
    public String show();
    public String getBody();
    public void setBody(String body);
    public int getStatus();
    public void setStatus(int status);
    public String getCookie(String key);
    public void addCookie(String key, String value);
    public Map<String, String> getCookies();
}
