package org.warheim.net;

/**
 *
 * @author andy
 */
public class ResponseHandlerException extends Exception {
    public ResponseHandlerException(String msg) {
        super(msg);
    }
    public ResponseHandlerException(Throwable ex) {
        super(ex);
    }
}
