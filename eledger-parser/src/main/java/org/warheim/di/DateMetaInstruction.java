package org.warheim.di;

import org.joda.time.LocalDate;

/**
 *
 * @author andy
 */
public class DateMetaInstruction implements MetaInstruction {

    @Override
    public String execute() throws MetaInstructionException {
        LocalDate localDate = new LocalDate();
        return localDate.toString();
    }

}
