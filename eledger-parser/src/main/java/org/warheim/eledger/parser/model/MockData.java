package org.warheim.eledger.parser.model;

/**
 * Supplies data needed for debugging and testing
 *
 * @author andy
 */
public class MockData {
    public static final String USER_1_NAME = "Matthew C. Cornwallis";
    public static final String USER_2_NAME = "John B. Smith";
    
    public static final String SUBJ_1_ID = "m101";
    public static final String SUBJ_1_NAME = "Maths and algebra";
    public static final String SUBJ_2_ID = "e101";
    public static final String SUBJ_2_NAME = "English language";
    public static final String SUBJ_3_ID = "cs101";
    public static final String SUBJ_3_NAME = "Computer Science";    
    
    public static NotificationsData createTestData(int stage) {
        NotificationsData notificationsData = new NotificationsData();
        UserNotifications notifications = new UserNotifications();
        Subject maths = new Subject(SUBJ_1_ID, SUBJ_1_NAME);
        notifications.addTask(maths, new Task("2015-10-10", "Memorize multiplication matrix"));
        notifications.addTask(maths, new Task("2015-10-12", "Complex numbers calculus using algebraic theorems"));
        Subject english = new Subject(SUBJ_2_ID, SUBJ_2_NAME);
        notifications.addTask(english, new Task("2015-11-12", "Learn to cite any Shakespeare play"));
        
        notifications.addTest(maths, new Test("2015-11-30", "All algebra test"));
        
        notifications.putMessage(new Message("111", "advice", "john@mit.edu", "all-students", "2015-09-01", "No more jumping on the bed!"));
        User user = new User(USER_1_NAME);
        if (stage>0) {
            notifications.putMessage(new Message("112", "warning", "admin@mit.edu", "matt@cs.mit.edu", "2915-11-30", "You've been warned"));
        }
        notificationsData.putUserNotifications(user, notifications);
        UserNotifications un2 = new UserNotifications();
        if (stage>1) {
            User user2 = new User(USER_2_NAME);
            Subject sub2 = new Subject(SUBJ_3_ID, SUBJ_3_NAME);
            un2.addTask(sub2, new Task("2015-09-20", "Sorting algorithms"));
            
            notificationsData.putUserNotifications(user2, un2);
        }
        if (stage>2) {
            notifications.addTest(english, new Test("2915-12-06", "Old english roots"));
        }
        if (stage>3) {
            un2.putMessage(new Message("1150", "warning", "admin@mit.edu", "matt@cs.mit.edu", "2915-11-30", "You've been warned"));
        }
        return notificationsData;
    }
    
}
