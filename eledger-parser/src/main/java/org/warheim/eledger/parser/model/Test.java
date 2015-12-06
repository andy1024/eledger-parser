package org.warheim.eledger.parser.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Test or exam model
 *
 * @author andy
 */
public class Test extends InfoOnSubject implements Serializable {

    public Test(String date, String content) {
        super(date, content, InfoType.TEST);
    }
    @Override
    public String toString() {
        return "Test{" + "date=" + date + ", content=" + content + '}';
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
        final Test other = (Test) obj;
        if (!Objects.equals(this.date, other.date)) {
            return false;
        }
        return Objects.equals(this.content, other.content);
    }
    
}
