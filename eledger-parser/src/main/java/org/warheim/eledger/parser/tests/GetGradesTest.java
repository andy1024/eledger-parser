package org.warheim.eledger.parser.tests;

import org.slf4j.LoggerFactory;
import org.warheim.eledger.parser.GradeListParser;
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
public class GetGradesTest {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(GetGradesTest.class);
    
    public static void main(String... args) throws Exception {
        String data = FileTool.readFile("/home/andy/src/eledger-getter/grades.html");
        Source src = new Source(new User("test1"), SourceType.GRADELIST, data);
        SourcePageParser p = new GradeListParser();
        UserNotifications un = new UserNotifications();
        p.parse(src, un);
        logger.info(un.showAll());
    }

}
