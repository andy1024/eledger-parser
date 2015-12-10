package org.warheim.net;

/**
 *
 * @author andy
 */
public class WebExecutionException extends Exception {
    public WebExecutionException(String msg) {
        super(msg);
    }
    public WebExecutionException(Throwable ex) {
        super(ex);
    }
}
