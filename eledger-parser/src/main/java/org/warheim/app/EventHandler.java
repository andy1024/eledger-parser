package org.warheim.app;

/**
 *
 * Event handler class
 * 
 * @author andy
 */
public interface EventHandler {

    /**
     * Handle event attached to the application events list
     * @throws EventHandlerException 
     */
    public void handle() throws EventHandlerException;
    
}
