package org.warheim.di;

import org.joda.time.LocalDate;
import org.slf4j.LoggerFactory;

/**
 *
 * @author andy
 */
public class DateMetaInstruction implements MetaInstruction {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DateMetaInstruction.class);
    public static final String KEY = "%CURRENT_DATE%";

    @Override
    public void register() throws MetaInstructionException {
        try {
            ObjectFactory.registerMetaInstructionHandler(KEY, DateMetaInstruction.class);
        } catch (InstantiationException | IllegalAccessException ex) {
            logger.error("Meta instruction handler registration error", ex);
            throw new MetaInstructionException(ex);
        }
    }
    
    @Override
    public String execute() throws MetaInstructionException {
        LocalDate localDate = new LocalDate();
        return localDate.toString();
    }

}
