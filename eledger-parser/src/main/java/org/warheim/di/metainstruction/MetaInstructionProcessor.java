package org.warheim.di.metainstruction;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.filters.FilterBasedOnInheritance;
import com.openpojo.reflection.impl.PojoClassFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.LoggerFactory;

/**
 *
 * @author andy
 */
public class MetaInstructionProcessor {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MetaInstructionProcessor.class);
    
    protected static final Map<String, MetaInstruction> metaInstructionHandlers = new HashMap<>();
    public static final String MI_TAG_CHARACTER = "$";

    static {
        List<PojoClass> classes = PojoClassFactory.getPojoClassesRecursively("org.warheim",
            new FilterBasedOnInheritance(MetaInstruction.class));
        for (PojoClass pc: classes) {
            try {
                MetaInstruction mi = (MetaInstruction)pc.getClazz().newInstance();
                mi.register();
            } catch (InstantiationException | IllegalAccessException | MetaInstructionException ex) {
                logger.error("Meta instruction handler instantiation error", ex);
            }
        }
    }

    public static void registerMetaInstructionHandler(String key, Class<? extends MetaInstruction> handler) throws InstantiationException, IllegalAccessException {
        metaInstructionHandlers.put(key, handler.newInstance());
    }

    public static String runMetaInstructionHandlers(String value) throws MetaInstructionException {
        String x = value;
        if (x.contains(MI_TAG_CHARACTER)) {
            for (String miKey : metaInstructionHandlers.keySet()) {
                MetaInstruction mi = metaInstructionHandlers.get(miKey);
                String miResult = mi.execute();
                x = x.replaceAll("\\" + miKey, miResult);
            }
        }
        return x;
    }

}
