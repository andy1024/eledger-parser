package org.warheim.eledger.formatter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import org.warheim.eledger.parser.model.InfoOnSubject;
import org.warheim.eledger.parser.model.Message;
import org.warheim.eledger.parser.model.Subject;
import org.warheim.eledger.parser.model.User;
import org.warheim.formatter.FormattingException;

/**
 *
 * @author andy
 */
public class NotificationsHTMLFormatter extends NotificationsTaggedFormatter {
    //TODO: put css contents into outputfile
    protected String css = null;

    public void setCss(String css) {
        this.css = css;
    }

    @Override
    protected void makeHeader(StringBuilder str) throws FormattingException {
        str.append("<html>");
        str.append("<head>");
        if (css!=null) {
            str.append("<link rel=\"stylesheet\" href=\"").append(css).append("\">");
        }
        str.append("</head>");
        str.append("<body>");
        str.append("<div class='notif-data'>");
        str.append("<div class='notif-header-title'>");
        str.append("Notifications for users ").append(new Date().toString());
        str.append("</div>");
    }

    @Override
    protected void makeFooter(StringBuilder str) throws FormattingException {
        str.append("</div>");
        str.append("</body>");
        str.append("</html>");
    }

    @Override
    protected void addSeparator(StringBuilder str, SepType sepType) {
    }

    @Override
    protected File prepareSourceDocument(StringBuilder str) throws IOException, FormattingException, InterruptedException {
        File tempFile = File.createTempFile(this.getClass().getName(), ".html"); 
        try (PrintWriter pw = new PrintWriter(tempFile, "UTF-8")) {
            pw.println(str.toString());
        }
        return tempFile;
    }

    @Override
    protected void startUserTag(StringBuilder str, User user, int count) {
        str.append("<div class='notif-user' id='usr_").append(user.getName()).append("'>");
        str.append("<div class='notif-user-title'>");
        str.append(user.getFullname());
        str.append("</div>");
    }

    @Override
    protected void endUserTag(StringBuilder str, User user, int count) {
        str.append("</div>");
    }

    @Override
    protected void startSubjectTag(StringBuilder str, Subject subject, int count) {
        str.append("<div class='notif-subject' id='subject_").append(subject.getId()).append("'>");
        str.append("<div class='notif-subject-title'>");
        str.append(subject.getName());
        str.append("</div>");
    }

    @Override
    protected void endSubjectTag(StringBuilder str, Subject subject, int count) {
        str.append("</div>");
    }

    @Override
    protected void startInfoOnSubjectTag(StringBuilder str, InfoOnSubject info, int count) {
        str.append("<div ");
        str.append(" class='notif-info-").append(info.getType().toString().toLowerCase()).append("'");
        str.append(" id='info_").append(count).append("'>");
        str.append("<div class='notif-info-date'>");
        str.append(info.getDate());
        str.append("</div>");
        str.append("<div class='notif-info-content'>");
        str.append(info.getContent());
        str.append("</div>");
    }

    @Override
    protected void endInfoOnSubjectTag(StringBuilder str, InfoOnSubject info, int count) {
        str.append("</div>");
    }

    @Override
    protected void startMessageTag(StringBuilder str, Message msg, int count) {
        str.append("<div class='notif-msg' id='msg_").append(msg.getId()).append("'>");
        str.append("<div class='notif-msg-date'>");
        str.append(msg.getDate());
        str.append("</div>");
        str.append("<div class='notif-msg-sender'>");
        str.append(msg.getSender());
        str.append("</div>");
        str.append("<div class='notif-msg-recipients'>");
        str.append(msg.getRecipients());
        str.append("</div>");
        str.append("<div class='notif-msg-title'>");
        str.append(msg.getTitle());
        str.append("</div>");
        str.append("<div class='notif-msg-content'>");
        str.append(msg.getContent());
        str.append("</div>");
    }

    @Override
    protected void endMessageTag(StringBuilder str, Message msg, int count) {
        str.append("</div>");
    }

}
