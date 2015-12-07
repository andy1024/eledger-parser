package org.warheim.eledger.parser.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * User model, password is not serialized
 *
 * @author andy
 */
public class User implements Serializable, Comparable<User> {
    final private String name;
    final private transient String pass; //do not serialize this field
    private transient String fullname = null;

    public String getFullname() {
        return (fullname!=null?fullname:name);
    }
    
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    
    public String getName() {
        return name;
    }

    public String getPass() {
        return pass;
    }

    public User(String name) {
        this.name = name;
        this.pass = "";
    }

    public User(String name, String pass) {
        this.name = name;
        this.pass = pass;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.name);
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
        final User other = (User) obj;
        return Objects.equals(this.name, other.name);
    }

    @Override
    public int compareTo(User o) {
        return this.name.compareTo(o.name);
    }
    
    
}
