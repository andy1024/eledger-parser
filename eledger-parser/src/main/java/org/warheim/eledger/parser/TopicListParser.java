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
import org.warheim.eledger.parser.model.Topic;
import org.warheim.eledger.parser.model.UserNotifications;

/**
 * Parses the topic list page source scraped from web
 * Inserts topics into the UserNotifications object
 * 
 * @author andy
 */
public class TopicListParser implements SourcePageParser {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(TopicListParser.class);

    protected Map<String, Subject> getLiveTopicSubjects(Elements subjectHeaders) {
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
        Map<String, Subject> subjects = getLiveTopicSubjects(subjectHeaders);

        Elements topicRows = doc.select(".subject_details");
        for (Element topicRow: topicRows) {
            Subject subject = null;
            for (String className: topicRow.classNames()) {
                if (className.startsWith("subject_details_")) {
                    String topicSubject = stripSubjectId(className);
                    subject = subjects.get(topicSubject);
                    break;
                }
            }
            if (subject==null) {
                logger.warn("Subject not found");
            } else {
                String topicDate = null;
                String topicValue = null;
                Elements topicDetails = topicRow.select("td");
                for (Element topicDetail: topicDetails) {
                    if (topicDetail.children().size()>0) {
                        if (topicDetail.child(0).tagName().equals("strong")) {
                            topicDate = topicDetail.child(0).html();
                        }
                    } else {
                        topicValue = topicDetail.html();
                        if (topicDate!=null) break;
                    }
                }
                if (topicValue==null || topicDate==null) {
                    logger.warn("Bad topic attributes");
                } else {
                    Topic topic = new Topic(topicDate, topicValue);
                    un.addTopic(subject, topic);
                }

            }
        }

    }
    
}
