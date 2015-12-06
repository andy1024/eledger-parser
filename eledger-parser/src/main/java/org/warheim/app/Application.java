package org.warheim.app;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.warheim.di.ObjectCreationException;
import org.warheim.di.ObjectFactory;

/**
 *
 * Main application class with event handling mechanism
 * 
 * @author andy
 */
public abstract class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);
   
    /**
     * Initializes application with basic event handler
     */
    public Application() {
        registerEventHandler(Event.APP_EVENT_START, new EventHandler() {

            @Override
            public void handle() throws EventHandlerException {
                logger.info("application start");
            }
        });
        try {
            fire(Event.APP_EVENT_START);
        } catch (EventHandlerException ex) {
            logger.error("Event handler problem", ex);
        }
    }
     
    /**
     * Event handlers map, customizable by registerEventHandler(s) methods
     */
    protected Map<String, EventHandler> eventHandlers = new HashMap<>();
    
    public final void fire(String event) throws EventHandlerException {
        logger.debug("Event fired: " + event);
        EventHandler handlerToFire = eventHandlers.get(event);
        if (handlerToFire!=null) {
            logger.debug("Event handler class " + handlerToFire.getClass().getName());
            handlerToFire.handle();
            logger.debug("Event handler finished");
        }
    }
    
    /**
     * register many handlers basing on properties file
     * @param props
     * @param prefix
     * @throws ObjectCreationException 
     */
    public final void registerEventHandlers(Properties props, String prefix) throws ObjectCreationException {
        if (props!=null) {
            Enumeration e = props.propertyNames();
            while(e.hasMoreElements())
            {
                String key = (String) e.nextElement();
                if (key.startsWith(prefix)) {
                    String value = (String) props.get(key);
                    String event = key;
                    EventHandler eventHandler = (EventHandler) ObjectFactory.createObject(value);
                    registerEventHandler(event, eventHandler);
                }
            }
        }
    }
    
    /**
     * register single event handler
     * @param event
     * @param handler 
     */
    public final void registerEventHandler(String event, EventHandler handler) {
        eventHandlers.put(event, handler);
    }

    /**
     * unregister single event handler
     * @param event 
     */
    public final void unregisterEventHandler(String event) {
        eventHandlers.remove(event);
    }

    /**
     * main application entry point
     * @throws Exception 
     */
    public abstract void run() throws Exception;
}
