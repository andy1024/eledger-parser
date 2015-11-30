package org.warheim.eledger.formatter;

import org.warheim.print.FormattingException;
import org.warheim.print.Formatter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.SimpleDoc;
import org.warheim.eledger.parser.model.Message;
import org.warheim.eledger.parser.model.Task;
import org.warheim.eledger.parser.model.NotificationsData;
import org.warheim.eledger.parser.model.Subject;
import org.warheim.eledger.parser.model.Test;
import org.warheim.eledger.parser.model.User;
import org.warheim.eledger.parser.model.UserNotifications;
import org.warheim.print.FormattableModel;

/**
 * Notifications formatter that uses external PdfLaTeX installation
 * Use only if you meet its requirements (see README.MD)
 *
 * @author andy
 */
public class NotificationsPdfLatexFormatter implements Formatter {

    protected NotificationsData notificationsData;
    //values with defaults
    protected String fontSize="5pt";
    protected String width="54mm";
    protected String left="0.4cm";
    protected String right="0.3cm";
    protected String top="0.2cm";
    protected String bottom="0.8cm";
    protected String minimalHeight="2cm";
    protected String internalVerticalMargin="0.8cm";
    protected String strech="0.5";
    protected String languagePackage="polski";

    public NotificationsPdfLatexFormatter() {}

    /*
    public NotificationsPdfLatexFormatter(UserNotifications notificationsData, String fontSize, String width, 
            String left, String right, String top, String bottom,
            String minimalHeight, String internalVerticalMargin) {
        this.notificationsData = notificationsData;
        this.fontSize = fontSize;
        this.width = width;
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        this.minimalHeight = minimalHeight;
        this.internalVerticalMargin = internalVerticalMargin;
    }*/
    
    enum SepType {
        THIN, NORMAL, BOLD
    }
    
    private void addSeparator(StringBuilder str, SepType sepType) {
        switch (sepType) {
            case THIN : str.append("\\makebox[\\linewidth]{\\rule{\\paperwidth}{0.4pt}}\n");
                break;
            case NORMAL : str.append("\\makebox[\\linewidth]{\\rule{\\paperwidth}{0.5pt}}\n");
                break;
            case BOLD : str.append("\\makebox[\\linewidth]{\\rule{\\paperwidth}{0.6pt}}\n");
                break;
        }
        str.append("\\newline\n");
    }
    
    @Override
    public String format() {
        StringBuilder str = new StringBuilder();
        if (notificationsData!=null) {
            boolean firstUser = true;
            for (User user: notificationsData.getUsers()) {
                if (!firstUser) {
                    addSeparator(str, SepType.NORMAL);
                }
                str.append("\\Info"); //man icon
                str.append("\\textbf{\\textsf{").append(user.getName()).append("}}\n");
                UserNotifications userNotifications = notificationsData.getNotificationsForUser(user);
                //tasks section
                boolean firstTaskSubject = true;
                for (Subject subject: userNotifications.getTaskSubjects()) {
                    if (!firstTaskSubject) {
                        addSeparator(str, SepType.THIN);
                    }
                    str.append("\\textbf{").append(subject.getName()).append("}\n");
                    str.append("\\newline\n");
                    Set<Task> list = userNotifications.getTasksForSubject(subject);
                    if (list!=null) {
                        boolean firstTask = true;
                        for (Task task: list) {
                            if (!firstTask) {
                                str.append("\n");
                            }
                            str.append("\\Writinghand"); //writing hand icon
                            str.append("\\textsl{\\textsf{\\small{").append(task.getDate()).append("}}} ").append(task.getContent())
                               .append("\n");
                            str.append("\\newline\n");
                            firstTask = false;
                        }
                    }
                    firstTaskSubject=false;
                }
                if (!firstTaskSubject) { //there were some tasks in the output, draw separator
                    addSeparator(str, SepType.THIN);
                }
                //tests section
                boolean firstTestSubject = true;
                for (Subject subject: userNotifications.getTestSubjects()) {
                    if (!firstTestSubject) {
                        addSeparator(str, SepType.THIN);
                    }
                    str.append("\\textbf{").append(subject.getName()).append("}\n");
                    str.append("\\newline\n");
                    Set<Test> list = userNotifications.getTestsForSubject(subject);
                    if (list!=null) {
                        boolean firstTest = true;
                        for (Test test: list) {
                            if (!firstTest) {
                                str.append("\n");
                            }
                            str.append("\\Clocklogo"); //clock icon
                            str.append("\\textsl{\\textsf{\\small{").append(test.getDate()).append("}}} ").append(test.getContent())
                               .append("\n");
                            str.append("\\newline\n");
                            firstTest = false;
                        }
                    }
                    firstTestSubject=false;
                }
                if (!firstTestSubject) { //there were some tests in the output, draw separator
                    addSeparator(str, SepType.THIN);
                }
                //messages section
                boolean firstMessage = true;
                for (String msgId: userNotifications.getMessageIDs()) {
                    if (!firstMessage) {
                        addSeparator(str, SepType.THIN);
                    }
                    Message msg = userNotifications.getMessage(msgId);
                    str.append("\\Letter"); //letter icon
                    str.append("\\textsl{\\textsf{\\small{").append(msg.getDate()).append("}}} ");
                    str.append("\\textsl{\\small{").append(msg.getSender()).append("}} ");
                    str.append("\\textsf{").append(msg.getTitle()).append("} ");
                    str.append("\\textsl{").append(msg.getRecipients()).append("} ");
                    str.append(msg.getContent())
                       .append("\n");
                    str.append("\\newline\n");
                    firstMessage = false;
                }
                firstUser = false;
            }
        }
        return str.toString();
    }

    protected File prepareTex() throws IOException {
        File tempFile = File.createTempFile(this.getClass().getName(), ".tex"); 
        try (PrintWriter pw = new PrintWriter(tempFile, "UTF-8")) {
            pw.println("\\documentclass{article}");
            pw.println("\\usepackage{geometry}");
            pw.println("\\geometry{paperheight=\\maxdimen,paperwidth=" + width
                    + ",left=" + left + ",right=" + right + ",top=" + top + ",bottom=" + bottom +"}");
            pw.println("\\usepackage[utf8]{inputenc}");
            pw.println("\\usepackage{" + languagePackage + "}");
            pw.println("\\usepackage{setspace}");
            pw.println("\\usepackage{marvosym}");
            pw.println("\\setstretch{" + strech + "}");
            pw.println("\\begin{document}");
            pw.println("\\setbox0=\\vbox{");
            pw.println("\\setlength\\parindent{0pt}");
            pw.println(format());
            pw.println("}");
            pw.println("\\dimen0=\\dp0");
            pw.println("\\pdfpageheight=\\dimexpr\\ht0+" + internalVerticalMargin +"\\relax");
            pw.println("\\ifdim\\pdfpageheight<" + minimalHeight + " \\pdfpageheight=" + minimalHeight + " \\fi");
            pw.println("\\unvbox0\\kern-\\dimen0");
            pw.println("\\end{document}");
        }
        return tempFile;
   }
    
    @Override
    public Doc getDocument() throws FormattingException {
        File tempFile;
        Doc myDoc;
        try {
            tempFile = prepareTex();
            Process p = Runtime.getRuntime().exec("pdflatex -output-directory /tmp " + tempFile.getAbsolutePath());
            p.waitFor(60, TimeUnit.SECONDS);
            String outname = tempFile.getPath().replace(".tex", ".pdf");
            System.err.println(outname);
            FileInputStream psStream = null;
            psStream = new FileInputStream(outname);
            DocFlavor psInFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
            myDoc = new SimpleDoc(psStream, psInFormat, null);  
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(NotificationsPdfLatexFormatter.class.getName()).log(Level.SEVERE, null, ex);
            throw new FormattingException(ex);
        }
        return myDoc;
    }

    public NotificationsData getMmap() {
        return notificationsData;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getBottom() {
        return bottom;
    }

    public void setBottom(String bottom) {
        this.bottom = bottom;
    }

    public String getMinimalHeight() {
        return minimalHeight;
    }

    public void setMinimalHeight(String minimalHeight) {
        this.minimalHeight = minimalHeight;
    }

    public String getInternalVerticalMargin() {
        return internalVerticalMargin;
    }

    public void setInternalVerticalMargin(String internalVerticalMargin) {
        this.internalVerticalMargin = internalVerticalMargin;
    }

    public String getStrech() {
        return strech;
    }

    public void setStrech(String strech) {
        this.strech = strech;
    }

    public String getLanguagePackage() {
        return languagePackage;
    }

    public void setLanguagePackage(String languagePackage) {
        this.languagePackage = languagePackage;
    }

    @Override
    public void setModel(FormattableModel model) {
        this.notificationsData = (NotificationsData) model;
    }
    
    
    
}
