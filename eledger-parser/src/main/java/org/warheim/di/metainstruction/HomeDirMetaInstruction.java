package org.warheim.di.metainstruction;

import org.slf4j.LoggerFactory;
import org.warheim.di.ObjectFactory;

/**
 *
 * @author andy
 */
public class HomeDirMetaInstruction implements MetaInstruction {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(HomeDirMetaInstruction.class);
    public static final String KEY = "$HOME";

    @Override
    public void register() throws MetaInstructionException {
        try {
            ObjectFactory.registerMetaInstructionHandler(KEY, HomeDirMetaInstruction.class);
        } catch (InstantiationException | IllegalAccessException ex) {
            logger.error("Meta instruction handler registration error", ex);
            throw new MetaInstructionException(ex);
        }
    }
    
    @Override
    public String execute() throws MetaInstructionException {
        String retval = System.getProperty("user.home");
        return retval;
    }

}
