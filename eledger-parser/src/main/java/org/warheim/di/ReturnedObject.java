package org.warheim.di;

/**
 *
 * @author andy
 */
public class ReturnedObject {
    private Object object;
    private boolean fromCache;

    public ReturnedObject(Object object, boolean fromCache) {
        this.object = object;
        this.fromCache = fromCache;
    }
    
    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public boolean isFromCache() {
        return fromCache;
    }

    public void setFromCache(boolean fromCache) {
        this.fromCache = fromCache;
    }
    
    
}
