package org.warheim.di;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Object factory class
 * Creates objects based on String parameter
 *
 * @author andy
 */
public class ObjectFactory {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ObjectFactory.class);
    
    public static final String MI_TAG_CHARACTER = "%";
    
    protected static final Map<String, MetaInstruction> metaInstructionHandlers = new HashMap<>();

    //TODO: replace it with some more sophisticated solution like autoregistering of handlers
    static {
        metaInstructionHandlers.put(MI_TAG_CHARACTER + "CURRENT_DATE" + MI_TAG_CHARACTER, new DateMetaInstruction());
    }
    
    /**
     * returns setter method name for the specified key
     * @param key
     * @return 
     */
    public static String getSetterName(String key) {
        return "set" + key.substring(0, 1).toUpperCase() + key.substring(1);
    }
    
    public static String runMetaInstructionHandlers(String value) throws MetaInstructionException {
        String x = value;
        if (x.contains(MI_TAG_CHARACTER)) {
            for (String miKey: metaInstructionHandlers.keySet()) {
                MetaInstruction mi = metaInstructionHandlers.get(miKey);
                String miResult = mi.execute();
                x = x.replaceAll(miKey, miResult);
            }
        }
        return x;
    }

    /**
     * creates object based on String parameter
     * @param objDef object specification: package.class(parameter=value[,parameter=value])
     * @return
     * @throws ObjectCreationException 
     */
    public static Object createObject(String objDef) throws ObjectCreationException {
        logger.debug("DI-Constructing object");
        Object object = null;
        String classDescription = objDef;
        String[] str = classDescription.split("[\\(\\)]");
        String clazzStr = str[0];
        Class clazz;
        try {
            clazz = Class.forName(clazzStr);
            object = clazz.newInstance();
            logger.debug(clazz.toString());
            if (str.length>1) { //check if there are any arguments
                String argumentList = str[1];
                String[] args = argumentList.split(",");
                Map<String, String> attrMap = new HashMap<>();
                for (String arg : args) {
                    String[] elements = arg.split("=");
                    String key = elements[0];
                    String value = elements[1];
                    String miValue = runMetaInstructionHandlers(value);
                    String setterName = getSetterName(key);
                    logger.debug(setterName + "(" + miValue + ")");
                    attrMap.put(key, miValue);
                    Method m = clazz.getMethod(setterName, String.class);
                    m.invoke(object, miValue);
                }
            }
        } catch (NoSuchMethodException | SecurityException ex) {
            logger.error("Error while creating object " + objDef, ex);
            throw new ObjectCreationException(ex);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                IllegalArgumentException | InvocationTargetException ex) {
            logger.error("Error while creating object " + objDef, ex);
            throw new ObjectCreationException(ex);
        } catch (MetaInstructionException ex) {
            logger.error("Error running meta instruction handler for " + objDef, ex);
        }

        return object;
    }

}
