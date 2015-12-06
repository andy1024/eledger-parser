package org.warheim.eledger.parser.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Single user notifications container
 *
 * @author andy
 */
public class UserNotifications implements Serializable {
    private Map<Subject, Set<Task>> taskMap = new HashMap<>();
    private Map<Subject, Set<Test>> testMap = new HashMap<>();
    private final Map<String, Message> msgMap = new HashMap<>();
    
    public UserNotifications() {
        
    }
    
    public boolean isEmpty() {
        if (
            taskMap.isEmpty()
                &&
            testMap.isEmpty()
                &&
            msgMap.isEmpty()
            ) {
            return true;
        }
        return false;
    }
    
    public Map<Subject, Set<Task>> getTaskMap() {
        return taskMap;
    }
    
    public Map<Subject, Set<Test>> getTestMap() {
        return testMap;
    }

    public void addTask(Subject subject, Task task) {
        Set<Task> list = taskMap.get(subject);
        if (list==null) {
            list = new TreeSet<>();
            taskMap.put(subject, list);
        }
        list.add(task);
    }
    public void putTasks(Subject subject, Set<Task> tasks) {
        taskMap.put(subject, tasks);
    }
    public void putTasks(Map<Subject, Set<Task>> tmap) {
        taskMap = tmap;
    }
    
    public void putMessage(Message msg) {
        msgMap.put(msg.getId(), msg);
    }
    
    public void putMessageWithoutContents(String msgId, String title, String sender, String date) {
        msgMap.put(msgId, new Message(msgId, title, sender, null, date, null)); //to be filled later
    }

    public void addTest(Subject subject, Test task) {
        Set<Test> list = testMap.get(subject);
        if (list==null) {
            list = new TreeSet<>();
            testMap.put(subject, list);
        }
        list.add(task);
    }
    public void putTests(Subject subject, Set<Test> tests) {
        testMap.put(subject, tests);
    }
    public void putTests(Map<Subject, Set<Test>> tsmap) {
        testMap = tsmap;
    }
    
    public Set<Task> getTasksForSubject(Subject subject) {
        Set<Task> list = taskMap.get(subject);
        return list;
    }
    
    public Set<Subject> getTaskSubjects() {
        Set<Subject> list = taskMap.keySet();
        return list;
    }
    public Set<Test> getTestsForSubject(Subject subject) {
        Set<Test> list = testMap.get(subject);
        return list;
    }
    
    public Set<Subject> getInfoSubjects() {
        Set<Subject> list = new TreeSet<>(taskMap.keySet()); //do not write to internal keySet, that's why I'm copying it
        list.addAll(testMap.keySet());
        return list;
    }
    
    public Set<InfoOnSubject> getInfoForSubject(Subject subject) {
        Set<InfoOnSubject> list = new TreeSet<>();
        if (taskMap.containsKey(subject)) {
            for (Task task: taskMap.get(subject)) {
                list.add(task);
            }
        }
        if (testMap.containsKey(subject)) {
            for (Test test: testMap.get(subject)) {
                list.add(test);
            }
        }
        return list;
    }
    
    public Set<Subject> getTestSubjects() {
        Set<Subject> list = testMap.keySet();
        return list;
    }

    public Set<String> getMessageIDs() {
        Set<String> list = msgMap.keySet();
        return list;
    }
    
    public Message getMessage(String id) {
        return msgMap.get(id);
    }
    
    public Collection<Message> getMessages() {
        return msgMap.values();
    }

    @Deprecated
    public String serializeToJson() {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
        .setPrettyPrinting().create();
        String retval = gson.toJson(taskMap);//, fooType);
        return retval;
    }
    
    @Deprecated
    public static Map<Subject, Set<Task>> deserializeFromJson(String jsonSource) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<Subject, Set<Task>>>(){}.getType();
        Map<Subject, Set<Task>> taskMap = gson.fromJson(jsonSource, type);
        return taskMap;
    }

    public String showAll() {
        StringBuilder retval = new StringBuilder();
        if (!taskMap.isEmpty()) {
            retval.append("Tasks\n");
            for (Subject subject: taskMap.keySet()) {
                retval.append(subject.getName()).append("\n");
                Set<Task> list = getTasksForSubject(subject);
                if (list!=null) {
                    for (Task task: list) {
                        retval.append(" ").append(task.getDate())
                              .append(" ").append(task.getContent())
                              .append("\n");
                    }
                }
            }
        }
        if (!testMap.isEmpty()) {
            retval.append("Tests\n");
            for (Subject subject: testMap.keySet()) {
                retval.append(subject.getName()).append("\n");
                Set<Test> list = getTestsForSubject(subject);
                if (list!=null) {
                    for (Test test: list) {
                        retval.append(" ").append(test.getDate())
                              .append(" ").append(test.getContent())
                              .append("\n");
                    }
                }
            }
        }
        if (!msgMap.isEmpty()) {
            retval.append("Messages\n");
            Collection<Message> list = getMessages();
            if (list!=null) {
                for (Message msg: list) {
                retval.append(" ").append(msg.getId())
                      .append(" ").append(msg.getTitle())
                      .append(" ").append(msg.getDate())
                      .append(" ").append(msg.getSender())
                      .append(" ").append(msg.getRecipients())
                      .append(" ").append(msg.getContent())
                      .append("\n");
                }
            }
        }
        return retval.toString();
    }
}
