package org.warheim.eledger.parser;

import org.warheim.eledger.parser.model.Source;
import java.util.HashMap;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;
import org.warheim.eledger.parser.model.Grade;
import org.warheim.eledger.parser.model.Subject;
import org.warheim.eledger.parser.model.UserNotifications;

/**
 * Parses the grades list page source scraped from web
 * Inserts grades into the UserNotifications object
 * 
 * @author andy
 */
public class GradeListParser implements SourcePageParser {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(GradeListParser.class);

    protected Map<String, Subject> getLiveGradeSubjects(Elements subjectHeaders) {
        Map<String, Subject>  subjects = new HashMap<>();
        for (Element e: subjectHeaders) {
            logger.debug(e.className());
            logger.debug(e.id());
            Elements subjectNameElements = e.select(".subject_1line");

            String id = stripSubjectId(e.id());
            Subject subject = new Subject(id, stripSubjectName(subjectNameElements.get(0).html()));
            subjects.put(id, subject);
        }
        return subjects;
    }
    
    protected String stripSubjectName(String name) {
        return name.trim();
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
        Map<String, Subject> subjects = getLiveGradeSubjects(subjectHeaders);

        Elements gradeRows = doc.select(".subject_details");
        for (Element gradeRow: gradeRows) {
            Subject subject = null;
            for (String className: gradeRow.classNames()) {
                if (className.startsWith("subject_details_")) {
                    String gradeSubject = stripSubjectId(className);
                    subject = subjects.get(gradeSubject);
                    break;
                }
            }
            if (subject==null) {
                logger.warn("Subject not found");
            } else {
                String gradeDate = null;
                String gradeImportance = null;
                String gradeName = null;
                String gradeValue = null;
                Elements gradeLines = gradeRow.select(".mark_line");
                for (Element gradeLine: gradeLines) {
                    Elements gradeDetails = gradeLine.select("div");
                    for (Element gradeDetail: gradeDetails) {
                        if (gradeDetail.className().equals("mark_list_importance")) {
                            gradeImportance = gradeDetail.html();
                            continue;
                        }
                        if (gradeDetail.className().equals("mark_list_date")) {
                            gradeDate = gradeDetail.html();
                            continue;
                        }
                        if (gradeDetail.className().equals("mark_name")) {
                            gradeName = gradeDetail.html();
                            continue;
                        }
                        if (gradeDetail.className().equals("mark_value")) {
                            gradeValue = gradeDetail.html();
                        }
                    }
                    if (gradeValue==null || gradeDate==null) {
                        logger.warn("Bad grade attributes");
                    } else {
                        Grade grade = new Grade(gradeImportance, gradeName, gradeDate, gradeValue);
                        un.addGrade(subject, grade);
                    }
                }
            }
        }

    }
    
}
