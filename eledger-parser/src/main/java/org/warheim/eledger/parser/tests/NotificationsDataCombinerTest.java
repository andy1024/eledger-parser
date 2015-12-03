package org.warheim.eledger.parser.tests;

import static org.warheim.eledger.parser.NotificationsDataCombiner.combine;
import org.warheim.eledger.parser.model.MockData;
import org.warheim.eledger.parser.model.NotificationsData;

/**
 *
 * @author andy
 */
public class NotificationsDataCombinerTest {
    public static void main(String... args) throws Exception {
        NotificationsData data = MockData.createTestData(4);
        NotificationsData combined = combine(data);
        NotificationsData diff = NotificationsData.getDataDiff(combined, data);
        System.out.println(data.showAll());
        System.out.println(combined.showAll());
        System.out.println(diff.showAll());
    }

}
