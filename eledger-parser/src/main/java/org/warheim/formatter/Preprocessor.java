package org.warheim.formatter;

/**
 *
 * @author andy
 */
public interface Preprocessor {
    public String process(String input) throws PreprocessorException;
}
