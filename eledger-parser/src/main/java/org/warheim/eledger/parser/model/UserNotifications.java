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
    private Map<Subject, Set<Topic>> topicMap = new HashMap<>();
    private Map<Subject, Set<Grade>> gradeMap = new HashMap<>();
    private final Map<String, Message> msgMap = new HashMap<>();
    
    public UserNotifications() {
        
    }
    
    public boolean isEmpty() {
        return taskMap.isEmpty()
                &&
                testMap.isEmpty()
                &&
                topicMap.isEmpty()
                &&
                gradeMap.isEmpty()
                &&
                msgMap.isEmpty();
    }
    
    public Map<Subject, Set<Task>> getTaskMap() {
        return taskMap;
    }
    
    public Map<Subject, Set<Test>> getTestMap() {
        return testMap;
    }

    public Map<Subject, Set<Topic>> getTopicMap() {
        return topicMap;
    }

    public Map<Subject, Set<Grade>> getGradeMap() {
        return gradeMap;
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
    
    public void addGrade(Subject subject, Grade grade) {
        Set<Grade> list = gradeMap.get(subject);
        if (list==null) {
            list = new TreeSet<>();
            gradeMap.put(subject, list);
        }
        list.add(grade);
    }
    public void putGrades(Subject subject, Set<Grade> grades) {
        gradeMap.put(subject, grades);
    }
    public void putGrades(Map<Subject, Set<Grade>> gmap) {
        gradeMap = gmap;
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
    
    public void addTopic(Subject subject, Topic topic) {
        Set<Topic> list = topicMap.get(subject);
        if (list==null) {
            list = new TreeSet<>();
            topicMap.put(subject, list);
        }
        list.add(topic);
    }
    public void putTopics(Subject subject, Set<Topic> topics) {
        topicMap.put(subject, topics);
    }
    public void putTopics(Map<Subject, Set<Topic>> tcmap) {
        topicMap = tcmap;
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
    public Set<Topic> getTopicsForSubject(Subject subject) {
        Set<Topic> list = topicMap.get(subject);
        return list;
    }
    public Set<Grade> getGradesForSubject(Subject subject) {
        Set<Grade> list = gradeMap.get(subject);
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
        if (topicMap.containsKey(subject)) {
            for (Topic topic: topicMap.get(subject)) {
                list.add(topic);
            }
        }
        return list;
    }
    
    public Set<Subject> getTestSubjects() {
        Set<Subject> list = testMap.keySet();
        return list;
    }

    public Set<Subject> getTopicSubjects() {
        Set<Subject> list = topicMap.keySet();
        return list;
    }

    public Set<Subject> getGradeSubjects() {
        Set<Subject> list = gradeMap.keySet();
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
        if (!topicMap.isEmpty()) {
            retval.append("Topics\n");
            for (Subject subject: topicMap.keySet()) {
                retval.append(subject.getName()).append("\n");
                Set<Topic> list = getTopicsForSubject(subject);
                if (list!=null) {
                    for (Topic topic: list) {
                        retval.append(" ").append(topic.getDate())
                              .append(" ").append(topic.getContent())
                              .append("\n");
                    }
                }
            }
        }
        if (!gradeMap.isEmpty()) {
            retval.append("Grades\n");
            for (Subject subject: gradeMap.keySet()) {
                retval.append(subject.getName()).append("\n");
                Set<Grade> list = getGradesForSubject(subject);
                if (list!=null) {
                    for (Grade grade: list) {
                        retval.append(" ").append(grade.getDate())
                              .append(" ").append(grade.getName())
                              .append(" ").append(grade.getValue())
                              .append(" ").append(grade.getImportance())
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
