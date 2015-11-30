package org.warheim.eledger.parser.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Homework=task model
 *
 * @author andy
 */
public class Task extends InfoOnSubject implements Serializable {

    public Task(String date, String content) {
        super(date, content, InfoType.TASK);
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
    
}
