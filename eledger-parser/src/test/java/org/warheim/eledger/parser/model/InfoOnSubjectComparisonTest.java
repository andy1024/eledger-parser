package org.warheim.eledger.parser.model;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author andy
 */
public class InfoOnSubjectComparisonTest {

    @Test
    public void compareIdentical() {
        InfoOnSubject i1 = new Task("2015-02-02", "some content");
        Assert.assertEquals(0, i1.compareTo(i1));
    }

    @Test
    public void compareLeftLater() {
        InfoOnSubject i1 = new Task("2015-02-02", "some content");
        InfoOnSubject i2 = new Task("2010-01-01", "other content");
        Assert.assertTrue( i1.compareTo(i2)>0 );
    }

    @Test
    public void compareRightLater() {
        InfoOnSubject i1 = new Task("2012-02-02", "some content");
        InfoOnSubject i2 = new Task("2030-01-01", "other content");
        Assert.assertTrue( i1.compareTo(i2)<0 );
    }

    @Test
    public void compareIdenticalDateDifferentContent() {
        InfoOnSubject i1 = new Task("2012-02-02", "some content");
        InfoOnSubject i2 = new Task("2012-02-02", "other content");
        Assert.assertTrue( i1.compareTo(i2)!=0 );
    }

}
