package org.warheim.eledger.parser;

import org.warheim.eledger.parser.model.Source;
import java.util.HashMap;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;
import org.warheim.eledger.parser.model.Subject;
import org.warheim.eledger.parser.model.Task;
import org.warheim.eledger.parser.model.UserNotifications;

/**
 * Parses the task list page source scraped from web
 * Inserts tasks into the UserNotifications object
 * 
 * @author andy
 */
public class TaskListParser implements SourcePageParser {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(TaskListParser.class);
    
    protected Map<String, Subject> getLiveTaskSubjects(Elements subjectHeaders) {
        Map<String, Subject>  subjects = new HashMap<>();
        for (Element e: subjectHeaders) {
            logger.debug(e.className());
            
            logger.debug(e.id());
            Elements subjectNameElements = e.select(".show_link_subject a");
            String id = stripSubjectId(e.id());
            Subject subject = new Subject(id, stripSubjectName(subjectNameElements.get(0).html()));
            subjects.put(id, subject);
        }
        return subjects;
    }
    
    protected String stripSubjectName(String name) {
        return name.replace("+ ", "");
    }
    
    protected String stripSubjectId(String id) {
        String retval = id.replace("subject_details_", "");
        retval = retval.replace("subject_", "");
        return retval;
    }

    @Override
    public void parse(Source source, UserNotifications un) {
        Document doc = Jsoup.parse(source.getContents());
        Elements tableRows = doc.select(".new_border tr");
        Elements subjectHeaders = tableRows.select(".subject_header");
        Map<String, Subject> subjects = getLiveTaskSubjects(subjectHeaders);

        Elements taskRows = doc.select(".subject_details");
        for (Element taskRow: taskRows) {
            Subject subject = null;

            for (String className: taskRow.classNames()) {
                if (className.startsWith("subject_details_")) {
                    String taskSubject = stripSubjectId(className);
                    subject = subjects.get(taskSubject);
                    break;
                }
            }
            if (subject==null) {
                logger.warn("Subject not found");
            } else {
                String taskDate = null;
                String taskValue = null;
                Elements taskDetails = taskRow.select("td");
                for (Element taskDetail: taskDetails) {
                    if (taskDetail.children().size()>0) {
                        if (taskDetail.child(0).tagName().equals("strong")) {
                            taskDate = taskDetail.child(0).html();
                        }
                    } else {
                        taskValue = taskDetail.html();
                        if (taskDate!=null) break;
                    }
                }
                if (taskValue==null || taskDate==null) {
                    logger.warn("Bad task attributes");
                } else {
                    Task task = new Task(taskDate, taskValue);
                    un.addTask(subject, task);
                }

            }
        }

    }
    
}
