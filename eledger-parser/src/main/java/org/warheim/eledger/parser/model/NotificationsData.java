package org.warheim.eledger.parser.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.warheim.formatter.FormattableModel;

/**
 * All data that was scraped from the service is encapsulated in this model object
 * 
 * @author andy
 */
public class NotificationsData implements Serializable, FormattableModel  {
    private final Map<User, UserNotifications> dataMap = new HashMap<>();
    
    public boolean isEmpty() {
        return dataMap.isEmpty();
    }
    
    public void putUserNotifications(User user, UserNotifications userNotifications) {
        dataMap.put(user, userNotifications);
    }
        
    public UserNotifications getNotificationsForUser(User user) {
        return dataMap.get(user);
    }
    
    public UserNotifications getNotificationsForUser(String name) {
        for (User user: dataMap.keySet()) {
            if (user.getName().equals(name)) {
                return dataMap.get(user);
            }
        }
        return null;
    }
    
    public int getTotalCount() {
        int totalCount = 0;
        for (User user: dataMap.keySet()) {
            UserNotifications un = dataMap.get(user);
            totalCount += un.getTestMap().size();
            totalCount += un.getTopicMap().size();
            totalCount += un.getTaskMap().size();
            totalCount += un.getMessages().size();
            totalCount += un.getGradeMap().size();
        }
        return totalCount;
    }

    public Set<User> getUsers() {
        return dataMap.keySet();
    }
    
    public String serializeToJson() {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
        .setPrettyPrinting().create();
        String retval = gson.toJson(this);
        return retval;
    }
    
    public static NotificationsData deserializeFromJson(String jsonSource) {
        Gson gson = new Gson();
        Type type = new TypeToken<NotificationsData>(){}.getType();
        NotificationsData notificationsData = gson.fromJson(jsonSource, type);
        return notificationsData;
    }

     public String showAll() {
        StringBuilder retval = new StringBuilder("All notifications\n");
        for (User user: getUsers()) {
            UserNotifications un = getNotificationsForUser(user);
            if (un!=null) {
                retval.append("for user: ").append(user.getName()).append("\n");
                retval.append(un.showAll());
            }
        }
        return retval.toString();
    }
    
    /**
     * Calculates the difference between what is currently stored on disk and what comes from the server
     * As a side effect it supplements the disk storage to much the data from server
     * 
     * @param dataFromServer
     * @param dataFromDisk
     * @return diff tree
     */
    public static NotificationsData getDataDiff(NotificationsData dataFromServer, NotificationsData dataFromDisk) {
        //buildDataFromDisk();
        //buildDataFromServer();

        NotificationsData diffMap = new NotificationsData();
        for (User user: dataFromServer.getUsers()) {
            UserNotifications diskUN = dataFromDisk.getNotificationsForUser(user.getName());
            if (diskUN==null) {
                UserNotifications serverUN = dataFromServer.getNotificationsForUser(user.getName());
                diffMap.putUserNotifications(user, serverUN); //copy entire user data to diff map (for printout)
                dataFromDisk.putUserNotifications(user, serverUN); //add it to disk data store
            } else {
                UserNotifications userNotificationsDiffMap = new UserNotifications();
                UserNotifications serverUN = dataFromServer.getNotificationsForUser(user);
                //compare tasks/subjects
                for (Subject serverSubject: serverUN.getTaskSubjects()) {
                    Set<Task> serverTasks = serverUN.getTasksForSubject(serverSubject);
                    Set<Task> diskTasks = diskUN.getTasksForSubject(serverSubject);
                    if (diskTasks==null||diskTasks.isEmpty()) { //it is not known to the stored map
                        //process the entire subject
                        userNotificationsDiffMap.putTasks(serverSubject, serverTasks); //save it for printout
                        //FIX for bug #3
                        diskUN.putTasks(serverSubject, serverTasks); //copy it to disk store
                    } else { //subject is known, check each task
                        Set<Task> diffTasks = new TreeSet<>();
                        int newTaskInSubjectCount = 0;
                        for (Task task: serverTasks) {
                            if (diskTasks.contains(task)) { //task known, skip

                            } else {
                                newTaskInSubjectCount++;
                                diffTasks.add(task); //put task to diff set
                                diskTasks.add(task); //copy it to disk store
                            }
                        }
                        if (newTaskInSubjectCount>0) {
                            userNotificationsDiffMap.putTasks(serverSubject, diffTasks);//insert new task to diff map
                        }

                    }
                }
                //compare tests/subjects
                for (Subject serverSubject: serverUN.getTestSubjects()) {
                    Set<Test> serverTests = serverUN.getTestsForSubject(serverSubject);
                    Set<Test> diskTests = diskUN.getTestsForSubject(serverSubject);
                    if (diskTests==null||diskTests.isEmpty()) { //it is not known to the stored map
                        //process the entire subject
                        userNotificationsDiffMap.putTests(serverSubject, serverTests); //save it for printout
                        //FIX for bug #3
                        diskUN.putTests(serverSubject, serverTests); //copy it to disk store
                    } else { //subject is known, check each test
                        Set<Test> diffTests = new TreeSet<>();
                        int newTestInSubjectCount = 0;
                        for (Test test: serverTests) {
                            if (diskTests.contains(test)) { //test known, skip

                            } else {
                                newTestInSubjectCount++;
                                diffTests.add(test); //put test to diff set
                                diskTests.add(test); //copy it to disk store
                            }
                        }
                        if (newTestInSubjectCount>0) {
                            userNotificationsDiffMap.putTests(serverSubject, diffTests);//insert new test to diff map
                        }

                    }
                }
                //compare topics/subjects
                for (Subject serverSubject: serverUN.getTopicSubjects()) {
                    Set<Topic> serverTopics = serverUN.getTopicsForSubject(serverSubject);
                    Set<Topic> diskTopics = diskUN.getTopicsForSubject(serverSubject);
                    if (diskTopics==null||diskTopics.isEmpty()) { //it is not known to the stored map
                        //process the entire subject
                        userNotificationsDiffMap.putTopics(serverSubject, serverTopics); //save it for printout
                        //FIX for bug #3
                        diskUN.putTopics(serverSubject, serverTopics); //copy it to disk store
                    } else { //subject is known, check each topic
                        Set<Topic> diffTopics = new TreeSet<>();
                        int newTopicInSubjectCount = 0;
                        for (Topic topic: serverTopics) {
                            if (diskTopics.contains(topic)) { //topic known, skip

                            } else {
                                newTopicInSubjectCount++;
                                diffTopics.add(topic); //put topic to diff set
                                diskTopics.add(topic); //copy it to disk store
                            }
                        }
                        if (newTopicInSubjectCount>0) {
                            userNotificationsDiffMap.putTopics(serverSubject, diffTopics);//insert new topic to diff map
                        }

                    }
                }
                //compare grades/subjects
                for (Subject serverSubject: serverUN.getGradeSubjects()) {
                    Set<Grade> serverGrades = serverUN.getGradesForSubject(serverSubject);
                    Set<Grade> diskGrades = diskUN.getGradesForSubject(serverSubject);
                    if (diskGrades==null||diskGrades.isEmpty()) { //it is not known to the stored map
                        //process the entire subject
                        userNotificationsDiffMap.putGrades(serverSubject, serverGrades); //save it for printout
                        //FIX for bug #3
                        diskUN.putGrades(serverSubject, serverGrades); //copy it to disk store
                    } else { //subject is known, check each grade
                        Set<Grade> diffGrades = new TreeSet<>();
                        int newGradeInSubjectCount = 0;
                        for (Grade grade: serverGrades) {
                            if (diskGrades.contains(grade)) { //grade known, skip

                            } else {
                                newGradeInSubjectCount++;
                                diffGrades.add(grade); //put grade to diff set
                                diskGrades.add(grade); //copy it to disk store
                            }
                        }
                        if (newGradeInSubjectCount>0) {
                            userNotificationsDiffMap.putGrades(serverSubject, diffGrades);//insert new grade to diff map
                        }

                    }
                }
                //compare messages
                for (Message msg: serverUN.getMessages()) {
                    if (diskUN.getMessage(msg.getId())==null) {
                        userNotificationsDiffMap.putMessage(msg);
                        diskUN.putMessage(msg);
                    }
                }

                //compare outbound messages
                for (Message msg: serverUN.getMessagesSent()) {
                    if (diskUN.getMessageSent(msg.getId())==null) {
                        userNotificationsDiffMap.putMessageSent(msg);
                        diskUN.putMessageSent(msg);
                    }
                }
                if (!userNotificationsDiffMap.isEmpty()) {
                    diffMap.putUserNotifications(user, userNotificationsDiffMap);
                }
            }
            //dataFromDisk.putUserNotifications(user, diskUN); //rewrite disk UN's
        }
        return diffMap;
    }

}
