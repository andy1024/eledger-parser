package org.warheim.outputsink;

import org.warheim.formatter.FormattedDocument;

/**
 * General output interface
 * 
 * @author andy
 */
public interface Output {
    
    public void setOutputDeviceID(String outputDeviceID);

    public void setFormattedDocument(FormattedDocument formattedDocument);

    public boolean process() throws OutputException;
    
}
