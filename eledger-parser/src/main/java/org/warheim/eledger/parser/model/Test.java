/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.warheim.eledger.parser.model;

import java.io.Serializable;
import org.joda.time.DateTime;

/**
 *
 * @author andy
 */
public class Test implements Serializable, Comparable<Test> {
    private String date;
    private String content;

    public Test(String date, String content) {
        this.date = date;
        this.content = content;
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
    public int compareTo(Test o) {
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
