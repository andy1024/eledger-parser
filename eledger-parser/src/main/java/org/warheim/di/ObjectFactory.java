package org.warheim.di;

import org.warheim.di.metainstruction.MetaInstructionException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.LoggerFactory;
import org.warheim.di.metainstruction.MetaInstructionProcessor;

/**
 *
 * Object factory class
 * Creates objects based on String parameter
 *
 * @author andy
 */
public class ObjectFactory {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ObjectFactory.class);
    
    protected static final ObjectCache cache = new ObjectCache();
    
    /**
     * returns setter method name for the specified key
     * @param key
     * @return 
     */
    public static String getSetterName(String key) {
        return "set" + key.substring(0, 1).toUpperCase() + key.substring(1);
    }
    
    
    public static Object createObject(String objDef) throws ObjectCreationException {
        return createObject(objDef, true);
    }
    /**
     * creates object based on String parameter
     * @param objDef object specification: package.class(parameter=value[,parameter=value])
     * @param required if false, it can be null
     * @return
     * @throws ObjectCreationException 
     */
    public static Object createObject(String objDef, boolean required) throws ObjectCreationException {
        logger.debug("DI-Constructing object");
        Object object = null;
        if (!required&&(objDef==null||objDef.isEmpty())) {
            logger.warn("Empty definition, object not constructed");
            return null;
        }
        String classDescription = objDef;
        String[] str = classDescription.split("[\\(\\)]");
        String clazzStr = str[0];
        Class clazz;
        try {
            clazz = Class.forName(clazzStr);
            ReturnedObject ro = cache.get(clazz, clazzStr);
            object = ro.getObject();
            logger.debug(clazz.toString());
            if (!ro.isFromCache()) {
                if (str.length>1) { //check if there are any arguments
                    String argumentList = str[1];
                    String[] args = argumentList.split(",");
                    Map<String, String> attrMap = new HashMap<>();
                    for (String arg : args) {
                        String[] elements = arg.split("=");
                        String key = elements[0];
                        String value = elements[1];
                        String miValue = MetaInstructionProcessor.runMetaInstructionHandlers(value);
                        String setterName = getSetterName(key);
                        logger.debug(setterName + "(" + miValue + ")");
                        attrMap.put(key, miValue);
                        Method m = clazz.getMethod(setterName, String.class);
                        m.invoke(object, miValue);
                    }
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
