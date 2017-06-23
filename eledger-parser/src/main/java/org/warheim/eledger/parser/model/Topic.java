package org.warheim.eledger.parser.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Topic model
 *
 * @author andy
 */
public class Topic extends InfoOnSubject implements Serializable {

    public Topic(String date, String content) {
        super(date, content, InfoType.TOPIC);
    }
    @Override
    public String toString() {
        return "Topic{" + "date=" + date + ", content=" + content + '}';
    }

    @Override
    public int hashCode() {
        //TODO: set different seed for this
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
        final Topic other = (Topic) obj;
        if (!Objects.equals(this.date, other.date)) {
            return false;
        }
        return Objects.equals(this.content, other.content);
    }
    
}
