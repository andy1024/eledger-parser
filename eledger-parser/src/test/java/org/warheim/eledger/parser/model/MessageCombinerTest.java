package org.warheim.eledger.parser.model;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import org.junit.Assert;
import org.junit.Test;
import org.warheim.eledger.parser.NotificationsDataCombiner;

/**
 *
 * @author andy
 */
public class MessageCombinerTest {

    /**
     * test combining messages for one user (null-operation)
     */
    @Test
    public void oneUserTestCombinerT() {
        oneUserTestCombiner();
    }
    
    public NotificationsData oneUserTestCombiner() {
        NotificationsData sourceData = new NotificationsData();
        User u1 = new User(C_KRYSTY_WROTH);
        UserNotifications un1 = new UserNotifications();
        Message m1 = new Message("71", M_TITLE_1, S_NAME_1, C_KRYSTY_WROTH, "2097-11-21", M_CONTENTS_1);
        un1.putMessage(m1);
        sourceData.putUserNotifications(u1, un1);
        NotificationsData combinedData = NotificationsDataCombiner.combine(sourceData);
        Assert.assertEquals(NotificationsData.getDataDiff(sourceData, combinedData).isEmpty(), true);
        return sourceData;
    }
    
    /**
     * test combining messages for two users, without extra data
     */
    @Test
    public void twoUsersTestCombinerT() {
        twoUsersTestCombiner();
    }
    
    public NotificationsData twoUsersTestCombiner() {
        NotificationsData sourceData = new NotificationsData();
        User u1 = new User(C_DEAN_CAWDOR);
        User u2 = new User(C_LORI_QUINT);
        UserNotifications un1 = new UserNotifications();
        UserNotifications un2 = new UserNotifications();
        Message m1 = new Message("13", M_TITLE_2, C_DOC, R_EVERYONE, "2099-03-01", M_CONTENTS_2);
        un1.putMessage(m1);
        Message m2 = new Message("15", M_TITLE_2, C_DOC, R_EVERYONE, "2099-03-01", M_CONTENTS_2);
        un2.putMessage(m2);
        sourceData.putUserNotifications(u1, un1);
        sourceData.putUserNotifications(u2, un2);
        NotificationsData combinedData = NotificationsDataCombiner.combine(sourceData);
        Assert.assertEquals(1, combinedData.getUsers().size());
        User uc = combinedData.getUsers().toArray(new User[1])[0];
        for (Message mc: combinedData.getNotificationsForUser(uc).getMessages()) {
            Assert.assertEquals(M_TITLE_2, mc.getTitle());
            break;
        }
        return sourceData;
    }

    /**
     * test combining messages for many users, without extra data
     * for various recipients combination
     */
    @Test
    public void manyUsersTestCombinerT() {
        manyUsersTestCombiner();
    }
    
    public NotificationsData manyUsersTestCombiner() {
        NotificationsData sourceData = new NotificationsData();
        User u1 = new User(C_JAK);
        User u2 = new User(C_RYAN);
        User u3 = new User(C_MILLIE);
        UserNotifications un1 = new UserNotifications();
        UserNotifications un2 = new UserNotifications();
        UserNotifications un3 = new UserNotifications();
        Message m1 = new Message("1", M_TITLE_3, C_JB, R_COMPANIONS, "2098-03-01", M_CONTENTS_3);
        un1.putMessage(m1);
        Message m2 = new Message("2", M_TITLE_3, C_JB, R_COMPANIONS, "2098-03-01", M_CONTENTS_3);
        un2.putMessage(m2);
        Message m3 = new Message("3", M_TITLE_4, C_RYAN, R_COMPANIONS, "2098-04-05", M_CONTENTS_4);
        un1.putMessage(m3);
        Message m4 = new Message("4", M_TITLE_4, C_RYAN, R_COMPANIONS, "2098-04-05", M_CONTENTS_4);
        un3.putMessage(m4);
        sourceData.putUserNotifications(u1, un1);
        sourceData.putUserNotifications(u2, un2);
        sourceData.putUserNotifications(u3, un3);
        NotificationsData combinedData = NotificationsDataCombiner.combine(sourceData);
        Assert.assertEquals(2, combinedData.getUsers().size());
        for (User uc: combinedData.getUsers()) {
            Assert.assertEquals(1, combinedData.getNotificationsForUser(uc).getMessages().size());
        }
        return sourceData;
    }

    /**
     * Test combining various users data, not just messages
     */
    @Test
    public void manyUsersTestCombinerWithExtraDataT() {
        manyUsersTestCombinerWithExtraData();
    }
    
    public NotificationsData manyUsersTestCombinerWithExtraData() {
        NotificationsData sourceData = new NotificationsData();
        User u1 = new User(C_TRADER);
        User u2 = new User(C_JB);
        User u3 = new User(C_RYAN);
        UserNotifications un1 = new UserNotifications();
        UserNotifications un2 = new UserNotifications();
        UserNotifications un3 = new UserNotifications();
        Subject s1 = new Subject("90", S_ARMOURY);
        Subject s2 = new Subject("91", S_SECURITY);
        Subject s3 = new Subject("92", S_FUN);
        Task k1 = new Task("2090-12-12", TASK_1);
        Task k2 = new Task("2091-02-17", TASK_2);
        Task k3 = new Task("2091-02-18", TASK_3);
        org.warheim.eledger.parser.model.Test t1 
                = new org.warheim.eledger.parser.model.Test("2090-08-30", TEST_1);
        un3.addTask(s2, k2);
        un2.addTask(s1, k1);
        un3.addTest(s3, t1);
        un1.addTask(s3, k3);
        Message m1 = new Message("33", M_TITLE_5, C_TRADER, C_JB, "2090-12-11", M_CONTENTS_5);
        un2.putMessage(m1);
        Message m2 = new Message("34", M_TITLE_6, C_TRADER, C_JB, "2090-12-13", M_CONTENTS_6);
        un2.putMessage(m2);
        Message m3 = new Message("36", M_TITLE_6, C_TRADER, C_RYAN, "2090-12-13", M_CONTENTS_6);
        un3.putMessage(m3);
        Message m4 = new Message("37", M_TITLE_7, C_TRADER, "to self", "2090-01-26", M_CONTENTS_7);
        un1.putMessage(m4);
        sourceData.putUserNotifications(u1, un1);
        sourceData.putUserNotifications(u2, un2);
        sourceData.putUserNotifications(u3, un3);
        NotificationsData combinedData = NotificationsDataCombiner.combine(sourceData);
        Assert.assertEquals(4, combinedData.getUsers().size());
        for (User uc: combinedData.getUsers()) {
            UserNotifications un = combinedData.getNotificationsForUser(uc);
            switch (uc.getName()) {
                case C_TRADER:
                    Assert.assertEquals(1, un.getTaskMap().size());
                    Assert.assertEquals(0, un.getTestMap().size());
                    Assert.assertEquals(1, un.getMessages().size());
                    break;
                case C_JB:
                    Assert.assertEquals(1, un.getTaskMap().size());
                    Assert.assertEquals(0, un.getTestMap().size());
                    Assert.assertEquals(1, un.getMessages().size());
                    break;
                case C_RYAN:
                    Assert.assertEquals(1, un.getTaskMap().size());
                    Assert.assertEquals(1, un.getTestMap().size());
                    Assert.assertEquals(0, un.getMessages().size());
                    break;
                default:
                    Assert.assertEquals(0, un.getTaskMap().size());
                    Assert.assertEquals(0, un.getTestMap().size());
                    Assert.assertEquals(1, un.getMessages().size());
                    break;
            }
        }
        return sourceData;
    }
    
    //yeah, I really like this bunch;)
    //@Test
    public void makeMuchData() throws IOException {
        NotificationsData dataStore = new NotificationsData();
        NotificationsData.getDataDiff(oneUserTestCombiner(), dataStore);
        NotificationsData.getDataDiff(twoUsersTestCombiner(), dataStore);
        NotificationsData.getDataDiff(manyUsersTestCombiner(), dataStore);
        NotificationsData.getDataDiff(manyUsersTestCombinerWithExtraData(), dataStore);
        String outstr = dataStore.serializeToJson();
        File tempFile = File.createTempFile(this.getClass().getName(), ".txt"); 
        try (PrintWriter pw = new PrintWriter(tempFile, "UTF-8")) {
            pw.println(outstr);
        }

    }
    
    public static final String C_TRADER = "Trader";
    public static final String C_RYAN = "Ryan Cawdor";
    public static final String C_JB = "JB Dix";
    public static final String C_KRYSTY_WROTH = "Krysty Wroth";
    public static final String C_DOC = "Theophilus Algernon Tanner";
    public static final String C_JAK = "Jak Lauren";
    public static final String C_LORI_QUINT = "Lori Quint";
    public static final String C_MILLIE = "Mildred Wyeth";
    public static final String C_DEAN_CAWDOR = "Dean Cawdor";

    public static final String M_TITLE_1 = "Power of Gaia";
    public static final String S_NAME_1 = "Mother Sonia";
    public static final String M_CONTENTS_1 = "With great power comes great responsibility";

    public static final String M_TITLE_2 = "Walking cane";
    public static final String R_EVERYONE = "everyone";
    public static final String M_CONTENTS_2 = "Has anyone, perchance, seen my walking stick?";
    public static final String M_TITLE_3 = "Stickies!";
    public static final String R_COMPANIONS = "companions";
    public static final String M_CONTENTS_3 = "Dark night, those goddamn stickies!";
    public static final String M_TITLE_4 = "Fireblast";
    public static final String M_CONTENTS_4 = "Triple red, people!";
    
    public static final String TASK_1 = "Clean all the ordinance";
    public static final String TASK_2 = "Night watch, 4 hours";
    public static final String TASK_3 = "Move forward";
    public static final String TEST_1 = "Shooting competition";
    public static final String S_ARMOURY = "Armoury";
    public static final String S_SECURITY = "Security";
    public static final String S_FUN = "Fun";
    public static final String M_TITLE_5 = "Maintenance";
    public static final String M_CONTENTS_5 = "Don't forget to clean all the firearms";
    public static final String M_TITLE_6 = "Watch out";
    public static final String M_CONTENTS_6 = "Forgetting even minor details can chill you";
    public static final String M_TITLE_7 = "Note";
    public static final String M_CONTENTS_7 = "A man has to realise the difference between a threat and a promise.";

}
