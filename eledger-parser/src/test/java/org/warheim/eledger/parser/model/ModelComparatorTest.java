/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.warheim.eledger.parser.model;

import java.util.Collection;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;
/**
 *
 * @author andy
 */
public class ModelComparatorTest {

    @Test
    public void testCompareMsgOnly() {
        NotificationsData notifDisk = MockData.createTestData(0);
        //System.out.println(notifDisk.showAll());
        NotificationsData notifServ = MockData.createTestData(1);
        //System.out.println(notifServ.showAll());
        NotificationsData diff = NotificationsData.getDataDiff(notifServ, notifDisk);
        Assert.assertNotNull(diff);
        UserNotifications un = diff.getNotificationsForUser(MockData.USER_1_NAME);
        Assert.assertNotNull(un);
        Collection<Message> msgsDiff = un.getMessages();
        Assert.assertNotNull(msgsDiff);
        Assert.assertEquals(1, msgsDiff.size());
        Assert.assertEquals("112", msgsDiff.toArray(new Message[1])[0].getId());
        
        Collection<Message> msgsDisk = notifDisk.getNotificationsForUser(MockData.USER_1_NAME).getMessages();
        Assert.assertNotNull(msgsDisk);
        Assert.assertEquals(2, msgsDisk.size());

        //Assert.assertEquals(notifDisk.getNotificationsForUser("Matthew C. Cornwallis").get
        //System.out.println(diff.showAll());
    }
    @Test
    public void testCompareMoreCategories() {
        NotificationsData notifDisk = MockData.createTestData(0);
        //System.out.println(notifDisk.showAll());
        NotificationsData notifServ = MockData.createTestData(3);
        //System.out.println(notifServ.showAll());
        NotificationsData diff = NotificationsData.getDataDiff(notifServ, notifDisk);
        Assert.assertNotNull(diff);
        Subject subject = new Subject(MockData.SUBJ_3_ID, MockData.SUBJ_3_NAME);
        
        UserNotifications un = diff.getNotificationsForUser(MockData.USER_2_NAME);
        Assert.assertNotNull(un);
        Set<Task> diffTasks = un.getTasksForSubject(subject);
        Assert.assertNotNull(diffTasks);
        Assert.assertEquals(1, diffTasks.size());
        System.out.println(diff.showAll());
        
        Set<Task> diskTasks = notifDisk.getNotificationsForUser(MockData.USER_2_NAME).getTasksForSubject(subject);
        Assert.assertNotNull(diskTasks);
        Assert.assertEquals(1, diskTasks.size());
        
        
        
    }
    
}
