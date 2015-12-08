package org.warheim.di.metainstruction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.LoggerFactory;
import org.warheim.di.ObjectFactory;
import org.warheim.eledger.parser.Config;

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
        DateFormat formatter = new SimpleDateFormat(Config.DATE_FORMAT);
        String retval = formatter.format(new Date());
        return retval;
    }

}
