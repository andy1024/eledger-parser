package org.warheim.di;

import java.util.Date;

/**
 *
 * @author andy
 */
public class CachedObject {
    private Class clazz;
    private String definition;
    private Object object;
    private Date created;

    public CachedObject(Class clazz, String definition, Object object, Date created) {
        this.clazz = clazz;
        this.definition = definition;
        this.object = object;
        this.created = created;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
    
    
}
