package org.warheim.eledger.parser.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import org.slf4j.LoggerFactory;
import org.warheim.eledger.parser.Config;

/**
 * Grade model, describes student's grades
 *
 * @author andy
 */
public class Grade implements Serializable, Comparable<Grade> {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Grade.class);

    private String importance;
    private String name;
    private String date;
    private String value;

    public Grade(String importance, String name, String date, String value) {
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
        return Objects.equals(this.value, other.value);
    }
    
    @Override
    public int compareTo(Grade o) {
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
            return this.getValue().compareTo(o.getValue());
        } else {
            return dateComparison;
        }
    }

}
