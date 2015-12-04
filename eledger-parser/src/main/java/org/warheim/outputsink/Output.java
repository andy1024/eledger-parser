package org.warheim.outputsink;

import java.io.File;

/**
 * General output interface
 * 
 * @author andy
 */
public interface Output {
    
    public void setOutputDeviceID(String outputDeviceID);

    public void setInputFile(File inputFile);

    public boolean process() throws OutputException;
    
}
