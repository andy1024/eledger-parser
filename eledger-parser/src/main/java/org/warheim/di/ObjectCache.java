package org.warheim.di;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Object factory cache
 * only stateless-session-beans-like objects should be stored here
 * mark them with Cacheable interface
 *
 * @author andy
 */
public class ObjectCache {
    protected final Map<String, CachedObject> cache = new HashMap<>();

    public static boolean isCacheable(Class clazz) {
        for (Class i: clazz.getInterfaces()) {
            if (i.isAssignableFrom(Cacheable.class)) {
                return true;
            }
        }
        return false;
    }

    //TODO: handle garbage collection of not needed objects
    public ReturnedObject get(Class clazz, String definition) throws InstantiationException, IllegalAccessException {
        Object object = null;
        CachedObject cachedObject = null;
        boolean foundInCache = false;
        if (isCacheable(clazz)) {
            cachedObject = cache.get(definition);
            if (cachedObject==null) {
                object = clazz.newInstance();
                cachedObject = new CachedObject(clazz, definition, object, new Date());
            } else {
                //TODO: implement TTL handling
                object = cachedObject.getObject();
                cachedObject.setCreated(new Date());
                foundInCache = true;
            }
            cache.put(definition, cachedObject);
        } else {
            object = clazz.newInstance();
        }
        
        return new ReturnedObject(object, foundInCache);
    }
}
