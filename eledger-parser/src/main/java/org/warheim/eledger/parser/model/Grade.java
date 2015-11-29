/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.warheim.eledger.parser.model;

import java.io.Serializable;
import java.util.Objects;
import org.joda.time.DateTime;

/**
 * TODO: this is too far fetched for the moment, do not implement yet
 *
 * @author andy
 */
public class Grade implements Serializable, Comparable<Grade> {
    private String importance;
    private String name;
    private String date;
    private String value;

    public Grade(String importance, String name, String date, String value) {
        if (1==1) throw new RuntimeException("Not yet implemented");
        this.importance = importance;
        this.name = name;
        this.date = date;
        this.value = value;
    }

    public String getImportance() {
        return importance;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final Grade other = (Grade) obj;
        if (!Objects.equals(this.importance, other.importance)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.date, other.date)) {
            return false;
        }
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        return true;
    }
    
    @Override
    public int compareTo(Grade o) {
        
        DateTime thisDate = new DateTime(this.date);
        DateTime oDate = new DateTime(o.date);
        if (thisDate.isBefore(oDate)) {
            return -1;
        } else if (thisDate.isAfter(oDate)) {
            return 1;
        } else {
            return this.value.compareTo(o.value);
        }
    }

}
