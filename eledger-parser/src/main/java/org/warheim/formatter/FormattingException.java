package org.warheim.formatter;

/**
 *
 * @author andy
 */
public class FormattingException extends Exception {

    public FormattingException(Throwable cause) {
        super(cause);
    }

    public FormattingException(String msg) {
        super(msg);
    }
    
}
