package org.warheim.net;

/**
 *
 * @author andy
 */
public interface WebProcessor {
    public WebResponse execute(WebRequest request) throws WebExecutionException;
}
