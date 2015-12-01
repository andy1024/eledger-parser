package org.warheim.eledger.parser.tests;

import org.warheim.eledger.parser.MessageParser;
import org.warheim.eledger.parser.SourcePageParser;
import org.warheim.eledger.parser.model.Message;
import org.warheim.eledger.parser.model.Source;
import org.warheim.eledger.parser.model.SourceType;
import org.warheim.eledger.parser.model.User;
import org.warheim.eledger.parser.model.UserNotifications;
import org.warheim.file.FileTool;

/**
 *
 * @author andy
 */
public class GetMessageTest {

    public static void main(String... args) throws Exception {
        String data = FileTool.readFile("/home/andy/src/eledger-getter/msg3");
        Source src = new Source(new User("test1"), SourceType.MESSAGE_CONTENT, data, "26638");
        SourcePageParser p = new MessageParser();
        UserNotifications un = new UserNotifications();
        un.putMessage(new Message("26638", null, "someone@domain.com", null, "2015-10-10", null));
        p.parse(src, un);
        System.out.println(un.showAll());

    }

}
