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
import static org.warheim.eledger.formatter.LatexEscaper.escape;
import org.warheim.eledger.parser.Config;
import static org.warheim.eledger.parser.NDCombiner.combine;
import org.warheim.eledger.parser.model.InfoOnSubject;
import org.warheim.eledger.parser.model.Message;
import org.warheim.eledger.parser.model.NotificationsData;
import org.warheim.eledger.parser.model.Subject;
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
        Integer maxContentLength = Config.getInt(Config.KEY_MAX_MSG_CONTENT_LENGTH);
        if (notificationsData!=null) {
            boolean firstUser = true;
            for (User user: notificationsData.getUsers()) {
                UserNotifications userNotifications = notificationsData.getNotificationsForUser(user);
                if (userNotifications.isEmpty()) {
                    continue;
                }
                if (!firstUser) {
                    addSeparator(str, SepType.NORMAL);
                }
                str.append("\\Info"); //man icon
                str.append("\\textbf{\\textsf{").append(user.getName()).append("}}\n");
                str.append("\\newline");
                //tasks and tests combined section
                boolean firstSubject = true;
                for (Subject subject: userNotifications.getInfoSubjects()) {
                    if (!firstSubject) {
                        addSeparator(str, SepType.THIN);
                    }
                    str.append("\\textbf{").append(escape(subject.getName())).append("}\n");
                    str.append("\\newline\n");
                    Set<InfoOnSubject> list = userNotifications.getInfoForSubject(subject);
                    if (list!=null) {
                        boolean firstInfo = true;
                        for (InfoOnSubject info: list) {
                            if (!firstInfo) {
                                str.append("\n");
                            }
                            switch (info.getType()) {
                                case TASK: str.append("\\Writinghand"); //writing hand icon
                                    break;
                                case TEST: str.append("\\Clocklogo"); //clock icon
                                    break;
                            }
                            
                            str.append("\\textsl{\\textsf{\\small{").append(info.getDate()).append("}}} ").append(escape(info.getContent()))
                               .append("\n");
                            str.append("\\newline\n");
                            firstInfo = false;
                        }
                    }
                    firstSubject=false;
                }
                if (!firstSubject) { //there were some tasks in the output, draw separator
                    addSeparator(str, SepType.THIN);
                }
                //TODO: handle message combination if more than one user is supposed to receive it
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
                    str.append("\\textsf{").append(escape(msg.getTitle())).append("} ");
                    if (Config.get(Config.KEY_MULTIPLE_RECIPIENTS).equals(msg.getRecipients())) {
                        str.append("$\\infty$ ");
                    } else {
                        str.append("\\textsl{").append(escape(msg.getRecipients())).append("} ");
                    }
                    
                    if (maxContentLength!=null && msg.getContent().length()>maxContentLength) {
                    str.append(escape(msg.getContent().substring(0, maxContentLength)))
                       .append("...")
                       .append("\n");
                    } else {
                    str.append(escape(msg.getContent()))
                       .append("\n");
                    }
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
        //combine common messages:
        setModel(combine(notificationsData));
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
