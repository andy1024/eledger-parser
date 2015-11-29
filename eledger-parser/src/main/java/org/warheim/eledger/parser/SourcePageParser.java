package org.warheim.eledger.parser;

import org.warheim.eledger.parser.model.Source;
import org.warheim.eledger.parser.model.UserNotifications;

/**
 * Base source page parser
 *
 * @author andy
 */
public interface SourcePageParser {
    
    public void parse(Source source, UserNotifications un);
}
