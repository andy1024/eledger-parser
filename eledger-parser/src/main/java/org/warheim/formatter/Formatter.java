package org.warheim.formatter;

/**
 * Formatter interface
 *
 * @author andy
 */
public interface Formatter {
    
    public String getContentType();
    
    public void setModel(FormattableModel model);
    
    public void setPreprocessor(Preprocessor preprocessor);
    
    public FormattedDocument getFormattedDocument() throws FormattingException;
}
