package org.warheim.di;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Object factory class
 * Creates objects based on String parameter
 *
 * @author andy
 */
public class ObjectFactory {

    public static String getSetterName(String key) {
        return "set" + key.substring(0, 1).toUpperCase() + key.substring(1);
    }

    public static Object createObject(String objDef) throws ObjectCreationException {
        Logger.getLogger(ObjectFactory.class.getName()).log(Level.INFO, null, "DI-Constructing object");
        Object object = null;
        String classDescription = objDef;
        String[] str = classDescription.split("[\\(\\)]");
        String clazzStr = str[0];
        Class clazz;
        try {
            clazz = Class.forName(clazzStr);
            object = clazz.newInstance();
            Logger.getLogger(ObjectFactory.class.getName()).log(Level.INFO, null, clazz);
            if (str.length>1) { //check if there are any arguments
                String argumentList = str[1];
                String[] args = argumentList.split(",");
                Map<String, String> attrMap = new HashMap<>();
                for (String arg : args) {
                    String[] elements = arg.split("=");
                    String key = elements[0];
                    String value = elements[1];
                    String setterName = getSetterName(key);
                    Logger.getLogger(ObjectFactory.class.getName()).log(Level.INFO, null, 
                        setterName + "(" + value + ")");
                    attrMap.put(key, value);
                    Method m = clazz.getMethod(setterName, String.class);
                    m.invoke(object, value);
                }
            }
        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(ObjectFactory.class.getName()).log(Level.SEVERE, null, ex);
            throw new ObjectCreationException(ex);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(ObjectFactory.class.getName()).log(Level.SEVERE, null, ex);
            throw new ObjectCreationException(ex);
        }

        return object;
    }

}
