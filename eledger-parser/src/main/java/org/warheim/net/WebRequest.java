package org.warheim.net;

import java.util.Map;

/**
 *
 * @author andy
 */
public interface WebRequest {
    //FIXME: introduce cookies and adjust WebProcessorApache to handle them
    public String getUrl();
    public void setUrl(String url);
    public WebRequestType getType();
    public void setType(WebRequestType type);
    public void addHeader(String key, String value);
    public Map<String, String> getHeaders();
    public String getHeader(String key);
    public void addDataEntry(String key, String value);
    public Map<String, String> getDataEntries();
    public String show();
}
