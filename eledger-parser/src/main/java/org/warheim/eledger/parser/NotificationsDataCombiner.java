package org.warheim.eledger.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.warheim.eledger.parser.model.Message;
import org.warheim.eledger.parser.model.NotificationsData;
import org.warheim.eledger.parser.model.User;
import org.warheim.eledger.parser.model.UserNotifications;

/**
 * combine multiple messages sent to many users into one message to save output space
 *
 * @author andy
 */
class MessageForUsers {
    public Message msg;
    public Set<User> users = new TreeSet<>();
    MessageForUsers(Message msg, User user) {
        this.msg = msg;
        this.users.add(user);
    }
    void addUser(User user) {
        this.users.add(user);
    }
    Message getMessage() {
        return msg;
    }
    Set<User> getUsersForMsg() {
        return users;
    }
    String getUserNames() {
        StringBuilder retval = new StringBuilder();
        boolean first = true;
        for (User user: users) {
            if (!first) {
                retval.append(";");
            }
            retval.append(user.getFullname());
            first = false;
        }
        return retval.toString();
    }
}

public class NotificationsDataCombiner {
    public static NotificationsData combine(NotificationsData src) {
        NotificationsData nd = new NotificationsData();
        Map<Integer, MessageForUsers> mm = new HashMap<>();
        for (User user: src.getUsers()) {
            UserNotifications un = src.getNotificationsForUser(user);
            for (Message msg: un.getMessages()) {
                MessageForUsers mfu = mm.get(msg.flatCode());
                if (mfu!=null) {
                    mfu.addUser(user);
                } else {
                    mm.put(msg.flatCode(), new MessageForUsers(msg, user));
                }
            }
        }
        //outbound
        Map<Integer, MessageForUsers> mmOutbound = new HashMap<>();
        for (User user: src.getUsers()) {
            UserNotifications un = src.getNotificationsForUser(user);
            for (Message msg: un.getMessages()) {
                MessageForUsers mfu = mmOutbound.get(msg.flatCode());
                if (mfu!=null) {
                    mfu.addUser(user);
                } else {
                    mmOutbound.put(msg.flatCode(), new MessageForUsers(msg, user));
                }
            }
        }

        Map<String, User> virtualUsersMap = new HashMap<>();
        //handle not combined messages:
        for (User user: src.getUsers()) {
            //rewrite tasks & tests & topics & grades for non-combined users
            UserNotifications un = src.getNotificationsForUser(user);
            //but only if specific user has any tasks, tests, topics or grades
            if (!un.getTaskMap().isEmpty()||!un.getTestMap().isEmpty()||!un.getTopicMap().isEmpty()||!un.getGradeMap().isEmpty()) {
                virtualUsersMap.put(user.getFullname(), user);
                UserNotifications ndUn = new UserNotifications();
                ndUn.putTasks(un.getTaskMap());
                ndUn.putTests(un.getTestMap());
                ndUn.putTopics(un.getTopicMap());
                ndUn.putGrades(un.getGradeMap());
                nd.putUserNotifications(user, ndUn);
            }
        }
        //inbound messages
        Map<User, Set<Message>> combinedMsgsForUsers = new HashMap<>();
        for (Integer fc: mm.keySet()) {
            MessageForUsers mfu = mm.get(fc);
            String vuName = mfu.getUserNames();
            User vu = virtualUsersMap.get(vuName);
            if (vu==null) {
                vu = new User(vuName);
                virtualUsersMap.put(vuName, vu);
            }
            Set<Message> msgList = combinedMsgsForUsers.get(vu);
            if (msgList==null) {
                msgList = new TreeSet<>();
                combinedMsgsForUsers.put(vu, msgList);
            }
            Message msg = mm.get(fc).getMessage();
            msgList.add(msg);
            UserNotifications un = nd.getNotificationsForUser(vu);
            if (un==null) {
                un = new UserNotifications();
                nd.putUserNotifications(vu, un);
            }
            un.putMessage(msg);
        }
        //outbound messages
        Map<User, Set<Message>> combinedSentMsgsForUsers = new HashMap<>();
        for (Integer fc: mmOutbound.keySet()) {
            MessageForUsers mfu = mmOutbound.get(fc);
            String vuName = mfu.getUserNames();
            User vu = virtualUsersMap.get(vuName);
            if (vu==null) {
                vu = new User(vuName);
                virtualUsersMap.put(vuName, vu);
            }
            Set<Message> msgList = combinedSentMsgsForUsers.get(vu);
            if (msgList==null) {
                msgList = new TreeSet<>();
                combinedSentMsgsForUsers.put(vu, msgList);
            }
            Message msg = mmOutbound.get(fc).getMessage();
            msgList.add(msg);
            UserNotifications un = nd.getNotificationsForUser(vu);
            if (un==null) {
                un = new UserNotifications();
                nd.putUserNotifications(vu, un);
            }
            un.putMessageSent(msg);
        }

        return nd;
    }
    
}
