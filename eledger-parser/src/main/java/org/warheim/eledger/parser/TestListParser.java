/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.warheim.eledger.parser;

import org.warheim.eledger.parser.model.Source;
import java.util.HashMap;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.warheim.eledger.parser.model.Subject;
import org.warheim.eledger.parser.model.Test;
import org.warheim.eledger.parser.model.UserNotifications;

/**
 * Parses the test list page source scraped from web
 * Inserts tests into the UserNotifications object
 * @author andy
 */
public class TestListParser extends SourcePageParser {

    protected Map<String, Subject> getLiveTestSubjects(Elements subjectHeaders) {
        Map<String, Subject>  subjects = new HashMap<>();
        for (Element e: subjectHeaders) {
//            System.out.println(e.className());
//            System.out.println(e.id());
            Elements subjectNameElements = e.select(".show_link_subject a");
//            System.out.println();
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
        Map<String, Subject> subjects = getLiveTestSubjects(subjectHeaders);
//        for (String id: subjects.keySet()) {
//            System.out.println(subjects.get(id));
//        }
        Elements testRows = doc.select(".subject_details");
        for (Element testRow: testRows) {
            Subject subject = null;
            for (String className: testRow.classNames()) {
                if (className.startsWith("subject_details_")) {
                    String testSubject = stripSubjectId(className);
                    subject = subjects.get(testSubject);
                    break;
                }
            }
            if (subject==null) {
                System.err.println("Subject not found");
            } else {
                String testDate = null;
                String testValue = null;
                Elements testDetails = testRow.select("td");
                for (Element testDetail: testDetails) {
                    if (testDetail.children().size()>0) {
                        if (testDetail.child(0).tagName().equals("strong")) {
                            testDate = testDetail.child(0).html();
                        }
                    } else {
                        testValue = testDetail.html();
                        if (testDate!=null) break;
                    }
                }
                if (testValue==null || testDate==null) {
                    System.err.println("Bad test attributes");
                } else {
                    Test test = new Test(testDate, testValue);
                    un.addTest(subject, test);
                }

            }
        }

    }
    
}
