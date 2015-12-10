package org.warheim.net;

/**
 *
 * @author andy
 */
public enum WebRequestType {
    GET("GET"), POST("POST");
    
    private final String name;
    
    private WebRequestType(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
};
    
