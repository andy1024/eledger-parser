package org.warheim.eledger.formatter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import static org.warheim.eledger.formatter.LatexEscaper.escape;
import org.warheim.eledger.parser.Config;
import org.warheim.eledger.parser.model.InfoOnSubject;
import org.warheim.eledger.parser.model.Message;
import org.warheim.eledger.parser.model.Subject;
import org.warheim.eledger.parser.model.User;
import org.warheim.print.FormattingException;

/**
 *
 * @author andy
 */
public class NotificationsTextFormatter extends NotificationsFreeRollFormatter {

    @Override
    protected void addSeparator(StringBuilder str, SepType sepType) {
        switch (sepType) {
            case THIN :   str.append("--------------------\n");
                break;
            case NORMAL : str.append("********************\n");
                break;
            case BOLD :   str.append("XXXXXXXXXXXXXXXXXXXX\n");
                break;
        }
        str.append("\n");
    }

    @Override
    protected File prepareSourceDocument(StringBuilder str) throws IOException, FormattingException, InterruptedException {
        File tempFile = File.createTempFile(this.getClass().getName(), ".txt"); 
        try (PrintWriter pw = new PrintWriter(tempFile, "UTF-8")) {
            pw.println(str.toString());
        }
        return tempFile;
    }

    @Override
    protected void putUser(StringBuilder str, User user) {
        str.append("User "); //man icon
        str.append("").append(user.getFullname()).append("\n");
    }

    @Override
    protected void putSubject(StringBuilder str, Subject subject) {
        str.append("Subject ").append(escape(subject.getName())).append(" \n");
    }

    @Override
    protected void putInfoOnSubject(StringBuilder str, InfoOnSubject info) {
        switch (info.getType()) {
            case TASK: str.append("Task "); //writing hand icon
                break;
            case TEST: str.append("Test "); //clock icon
                break;
        }

        str.append("").append(info.getDate()).append(" ").append(escape(info.getContent()))
           .append("\n");
    }

    @Override
    protected void putMessage(StringBuilder str, Message msg) {
        str.append("Message sent on ").append(msg.getDate()).append(" ");
        str.append("\nfrom ").append(msg.getSender()).append(" to ");
        if (Config.get(Config.KEY_MULTIPLE_RECIPIENTS).equals(msg.getRecipients())) {
            str.append("many recipients ");
        } else {
            str.append(escape(msg.getRecipients()));
        }
        str.append("\n").append(escape(msg.getTitle())).append("\n");

        str.append(escape(msg.getContent()));
        str.append("\n");
    }

    @Override
    protected void makeHeader(StringBuilder str) throws FormattingException {
        str.append("Notifications on users ").append(new Date()).append("\n");
    }

    @Override
    protected void makeFooter(StringBuilder str) throws FormattingException {
        str.append("End of notifications on users").append("\n");
    }

}
