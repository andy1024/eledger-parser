/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.warheim.eledger.parser.model;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author andy
 */
public class User implements Serializable, Comparable<User> {
    private String name;
    private transient String pass; //do not serialize this field

    public String getName() {
        return name;
    }

    public String getPass() {
        return pass;
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
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(User o) {
        return this.name.compareTo(o.name);
    }
    
    
}
