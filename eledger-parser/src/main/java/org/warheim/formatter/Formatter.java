package org.warheim.formatter;

import java.io.File;

/**
 * Formatter interface
 *
 * @author andy
 */
public interface Formatter {
    
    public void setModel(FormattableModel model);
    
    public void setPreprocessor(Preprocessor preprocessor);
    
    public File getFormattedDocumentFile() throws FormattingException;
}
