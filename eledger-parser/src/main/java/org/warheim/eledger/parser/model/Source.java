package org.warheim.eledger.parser.model;

/**
 * Source page model, describes full source scraped from web
 *
 * @author andy
 */
public class Source {
    private User user;
    private SourceType type;
    private String contents;
    private String id=null; //optional

    public Source(User user, SourceType type, String contents) {
        this.user = user;
        this.type = type;
        this.contents = contents;
    }

    public Source(User user, SourceType type, String contents, String id) {
        this(user, type, contents);
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SourceType getType() {
        return type;
    }

    public void setType(SourceType type) {
        this.type = type;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Source{" + "user=" + user + ", type=" + type + ", contents=" + contents + ", id=" + id + '}';
    }

}
