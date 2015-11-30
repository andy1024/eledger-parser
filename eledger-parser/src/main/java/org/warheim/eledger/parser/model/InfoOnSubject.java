package org.warheim.eledger.parser.model;

import java.io.Serializable;
import org.joda.time.DateTime;

/**
 *
 * @author andy
 */
public abstract class InfoOnSubject implements Serializable, Comparable<InfoOnSubject> {
    protected String date;
    protected String content;
    protected InfoType type;

    public InfoOnSubject(String date, String content, InfoType type) {
        this.date = date;
        this.content = content;
        this.type = type;
    }
    
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
    public int compareTo(InfoOnSubject o) {
        
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
    
    public InfoType getType() {
        return type;
    }

}
