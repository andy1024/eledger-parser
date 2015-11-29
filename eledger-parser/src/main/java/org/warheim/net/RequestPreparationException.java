package org.warheim.net;

/**
 *
 * @author andy
 */
public class RequestPreparationException extends Exception {
    public RequestPreparationException(String msg) {
        super(msg);
    }
    public RequestPreparationException(Throwable ex) {
        super(ex);
    }
}
