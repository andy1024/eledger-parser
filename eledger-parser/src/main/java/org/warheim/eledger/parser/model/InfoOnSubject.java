package org.warheim.eledger.parser.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.LoggerFactory;
import org.warheim.eledger.parser.Config;

/**
 *
 * @author andy
 */
public abstract class InfoOnSubject implements Serializable, Comparable<InfoOnSubject> {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(InfoOnSubject.class);
    
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
        DateFormat formatter = new SimpleDateFormat(Config.DATE_FORMAT);
        Date thisDate;
        Date oDate;
        int dateComparison = 0;
        try {
            thisDate = formatter.parse(this.getDate());
            oDate = formatter.parse(o.getDate());
             dateComparison = thisDate.compareTo(oDate);
        } catch (ParseException ex) {
            logger.error("Wrong dates in subjects: " + this.getDate() + ", " + o.getDate(), ex);
        }

        if (dateComparison==0) {
            return this.getContent().compareTo(o.getContent());
        } else {
            return dateComparison;
        }
    }
    
    public InfoType getType() {
        return type;
    }

}
