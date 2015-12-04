package org.warheim.outputsink.printer;

import org.warheim.outputsink.OutputException;

/**
 *
 * @author andy
 */
public class PrintingException extends OutputException {

    public PrintingException(Throwable cause) {
        super(cause);
    }

    public PrintingException(String cause) {
        super(cause);
    }
    
}
