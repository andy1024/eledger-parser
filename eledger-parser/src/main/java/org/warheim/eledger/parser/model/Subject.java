package org.warheim.eledger.parser.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Task & test subject model
 *
 * @author andy
 */
public class Subject implements Serializable, Comparable<Subject> {

    public Subject(String id, String name) {
        this.id = id;
        this.name = name;
    }

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return "Subject{" + "id=" + id + ", name=" + name + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
        hash = 37 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Subject other = (Subject) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return Objects.equals(this.name, other.name);
    }

    @Override
    public int compareTo(Subject o) {
        int nameCmp = this.name.compareTo(o.name);
        if (nameCmp==0) {
            return this.id.compareTo(o.id);
        }
        else return nameCmp;
    }
    
    
}
