package org.warheim.app;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.warheim.di.ObjectCreationException;
import org.warheim.di.ObjectFactory;

/**
 *
 * Main application class with event handling mechanism
 * 
 * @author andy
 */
public abstract class Application {
    protected static final String EVENT_APP_START = "app.event.start";
   
    public Application() {
        registerEventHandler(EVENT_APP_START, new EventHandler() {

            @Override
            public void handle() throws EventHandlerException {
                System.out.println("application start");
            }
        });
        try {
            fire("app.event.start");
        } catch (EventHandlerException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
    //TODO: consider changing String to Event interface as a key
    protected Map<String, EventHandler> eventHandlers = new HashMap<>();
    
    public final void fire(String event) throws EventHandlerException {
        System.out.println("Event fired: " + event);
        EventHandler handlerToFire = eventHandlers.get(event);
        if (handlerToFire!=null) {
            System.out.println("Event handler class " + handlerToFire.getClass().getName());
            handlerToFire.handle();
            System.out.println("Event handler finished");
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
    
    public final void registerEventHandler(String event, EventHandler handler) {
        eventHandlers.put(event, handler);
    }

    public final void unregisterEventHandler(String event) {
        eventHandlers.remove(event);
    }

    public abstract void run() throws Exception;
}
