package org.warheim.print;

import javax.print.Doc;

/**
 * Formatter interface
 *
 * @author andy
 */
public interface Formatter {
    
    public void setModel(FormattableModel model);
    
    public abstract String format();
    
    public abstract Doc getDocument() throws FormattingException;
}
