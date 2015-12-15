package org.warheim.eledger.parser.tests;

import org.warheim.di.ObjectFactory;
import org.warheim.eledger.parser.model.MockData;
import org.warheim.eledger.parser.model.NotificationsData;
import org.warheim.formatter.Formatter;

/**
 *
 * @author andy
 */
public class NotificationsFormatterTest {

    public static void main(String... args) throws Exception {
        NotificationsData nd = MockData.createTestData(4);
        Formatter fmt = (Formatter)ObjectFactory.createObject("org.warheim.eledger.formatter.NotificationsHTMLFormatter(css=/home/andy/.eledger/eledgerstyle.css)");
        fmt.setModel(nd);
        fmt.getFormattedDocument();
    }
}
