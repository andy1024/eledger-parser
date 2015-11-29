package org.warheim.eledger.parser.model;

import java.io.Serializable;
import java.util.Objects;
import org.joda.time.DateTime;

/**
 * Homework=task model
 *
 * @author andy
 */
public class Task implements Serializable, Comparable<Task> {

    public Task(String date, String content) {
        this.date = date;
        this.content = content;
    }
    private String date;
    private String content;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Task{" + "date=" + date + ", content=" + content + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.date);
        hash = 83 * hash + Objects.hashCode(this.content);
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
        final Task other = (Task) obj;
        if (!Objects.equals(this.date, other.date)) {
            return false;
        }
        if (!Objects.equals(this.content, other.content)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Task o) {
        
        DateTime thisDate = new DateTime(this.getDate());
        DateTime oDate = new DateTime(o.getDate());
        if (thisDate.isBefore(oDate)) {
            return -1;
        } else if (thisDate.isAfter(oDate)) {
            return 1;
        } else {
            return this.getContent().compareTo(o.getContent());
        }
    }

    
}
