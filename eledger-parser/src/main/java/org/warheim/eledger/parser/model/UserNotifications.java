package org.warheim.eledger.parser.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Collection;
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
    private Map<String, Message> msgMap = new HashMap<>();
    
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
        String retval = "";
        if (!taskMap.isEmpty()) {
            retval += "Tasks\n";
            for (Subject subject: taskMap.keySet()) {
                retval += subject.getName() + "\n";
                Set<Task> list = getTasksForSubject(subject);
                if (list!=null) {
                    for (Task task: list) {
                        retval += " " + task.getDate() + " " + task.getContent() + "\n";
                    }
                }
            }
        }
        if (!testMap.isEmpty()) {
            retval += "Tests\n";
            for (Subject subject: testMap.keySet()) {
                retval += subject.getName() + "\n";
                Set<Test> list = getTestsForSubject(subject);
                if (list!=null) {
                    for (Test test: list) {
                        retval += " " + test.getDate() + " " + test.getContent() + "\n";
                    }
                }
            }
        }
        if (!msgMap.isEmpty()) {
            retval += "Messages\n";
            Collection<Message> list = getMessages();
            if (list!=null) {
                for (Message msg: list) {
                retval += " " + msg.getId() + " " + msg.getDate() +
                          " " + msg.getSender() + " " + msg.getRecipients() +
                          " " + msg.getContent() + "\n";
                }
            }
        }
        return retval;
    }
}
