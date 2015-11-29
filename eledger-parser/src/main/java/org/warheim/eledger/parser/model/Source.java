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

    public Source(User user, SourceType type, String contents) {
        this.user = user;
        this.type = type;
        this.contents = contents;
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
    
    
}
