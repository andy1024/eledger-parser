package org.warheim.eledger.parser.tests;

import org.slf4j.LoggerFactory;
import org.warheim.eledger.parser.MessageListParser;
import org.warheim.eledger.parser.SourcePageParser;
import org.warheim.eledger.parser.model.Source;
import org.warheim.eledger.parser.model.SourceType;
import org.warheim.eledger.parser.model.User;
import org.warheim.eledger.parser.model.UserNotifications;
import org.warheim.file.FileTool;

/**
 *
 * @author andy
 */
public class GetMessagesTest {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(GetMessagesTest.class);
    
    public static void main(String... args) throws Exception {
        String data = FileTool.readFile("/home/andy/src/eledger-getter/msginbox");
        Source src = new Source(new User("test1"), SourceType.MESSAGES, data);
        SourcePageParser p = new MessageListParser();
        UserNotifications un = new UserNotifications();
        p.parse(src, un);
        logger.info(un.showAll());
    }

}
