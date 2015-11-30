package org.warheim.eledger.parser.tests;

import org.warheim.eledger.formatter.NotificationsPdfLatexFormatter;
import org.warheim.eledger.parser.model.MockData;
import org.warheim.eledger.parser.model.NotificationsData;

/**
 *
 * @author andy
 */
public class NotificationsPdfLatexFormatterTest {

    public static void main(String... args) throws Exception {
        NotificationsData nd = MockData.createTestData(3);
        NotificationsPdfLatexFormatter fmt = new NotificationsPdfLatexFormatter();
        fmt.setModel(nd);
        fmt.getDocument();
    }
}
