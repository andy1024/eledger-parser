package org.warheim.eledger.parser.tests;

import org.slf4j.LoggerFactory;
import static org.warheim.eledger.parser.NotificationsDataCombiner.combine;
import org.warheim.eledger.parser.model.MockData;
import org.warheim.eledger.parser.model.NotificationsData;

/**
 *
 * @author andy
 */
public class NotificationsDataCombinerTest {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(NotificationsDataCombinerTest.class);
    
    public static void main(String... args) throws Exception {
        NotificationsData data = MockData.createTestData(4);
        NotificationsData combined = combine(data);
        NotificationsData diff = NotificationsData.getDataDiff(combined, data);
        logger.info(data.showAll());
        logger.info(combined.showAll());
        logger.info(diff.showAll());
    }

}
