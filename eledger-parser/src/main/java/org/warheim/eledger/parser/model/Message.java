package org.warheim.eledger.parser.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Message model, describes message sent to the e-ledger internal inbox
 *
 * @author andy
 */
public class Message implements Serializable, Comparable<Message> {
    private String id;
    private String title;
    private String sender;
    private String recipients;
    private String date;
    private String content;
    //private transient User user;

    public Message(String id, String title, String sender, String recipients, String date, String content
            //, User user
    ) 
    {
        this.id = id;
        this.title = title;
        this.sender = sender;
        this.recipients = recipients;
        this.date = date;
        this.content = content;
        //this.user = user;
    }
    
//    public User getUser() {
//        return user;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipients() {
        return recipients;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.id);
        return hash;
    }
    
    //hashcode built on non-key fields
    public int flatCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.content);
        hash = 17 * hash + Objects.hashCode(this.sender);
        hash = 17 * hash + Objects.hashCode(this.title);
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
        final Message other = (Message) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public int compareTo(Message o) {
        return this.id.compareTo(o.id);
    }
    
}
