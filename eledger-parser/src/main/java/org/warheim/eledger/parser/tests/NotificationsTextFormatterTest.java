package org.warheim.eledger.parser.tests;

import org.warheim.eledger.formatter.NotificationsTextFormatter;
import org.warheim.eledger.parser.model.MockData;
import org.warheim.eledger.parser.model.NotificationsData;

/**
 *
 * @author andy
 */
public class NotificationsTextFormatterTest {

    public static void main(String... args) throws Exception {
        NotificationsData nd = MockData.createTestData(4);
        NotificationsTextFormatter fmt = new NotificationsTextFormatter();
        fmt.setModel(nd);
        fmt.getFormattedDocumentFile();
    }
}
