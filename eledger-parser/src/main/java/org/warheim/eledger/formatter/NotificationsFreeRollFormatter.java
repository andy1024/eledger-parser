package org.warheim.eledger.formatter;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.warheim.eledger.parser.Config;
import static org.warheim.eledger.parser.NotificationsDataCombiner.combine;
import org.warheim.eledger.parser.model.InfoOnSubject;
import org.warheim.eledger.parser.model.Message;
import org.warheim.eledger.parser.model.NotificationsData;
import org.warheim.eledger.parser.model.Subject;
import org.warheim.eledger.parser.model.User;
import org.warheim.eledger.parser.model.UserNotifications;
import org.warheim.print.FormattableModel;
import org.warheim.print.Formatter;
import org.warheim.print.FormattingException;

/**
 *
 * @author andy
 */
public abstract class NotificationsFreeRollFormatter implements Formatter {
    protected NotificationsData notificationsData;

    public NotificationsData getMmap() {
        return notificationsData;
    }
    
    protected abstract void addSeparator(StringBuilder str, SepType sepType);

    @Override
    public void setModel(FormattableModel model) {
        this.notificationsData = (NotificationsData) model;
    }

    @Override
    public File getFormattedDocumentFile() throws FormattingException {
        //combine common messages:
        setModel(combine(notificationsData));
        File outFile;
        try {
            StringBuilder str = new StringBuilder();
            makeHeader(str);
            makeBody(str);
            makeFooter(str);
            outFile = prepareSourceDocument(str);
            System.out.println(outFile);
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(NotificationsPdfLatexFormatter.class.getName()).log(Level.SEVERE, null, ex);
            throw new FormattingException(ex);
        }
        return outFile;
    }

    protected abstract File prepareSourceDocument(StringBuilder str) throws IOException, FormattingException, InterruptedException;
    
    protected abstract void putUser(StringBuilder str, User user);

    protected abstract void putSubject(StringBuilder str, Subject subject);
    
    protected abstract void putInfoOnSubject(StringBuilder str, InfoOnSubject info);
    
    protected abstract void putMessage(StringBuilder str, Message msg);
    
    protected void makeBody(StringBuilder str) throws FormattingException {
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
                putUser(str, user);
                //tasks and tests combined section
                boolean firstSubject = true;
                for (Subject subject: userNotifications.getInfoSubjects()) {
                    if (!firstSubject) {
                        addSeparator(str, SepType.THIN);
                    }
                    putSubject(str, subject);
                    Set<InfoOnSubject> list = userNotifications.getInfoForSubject(subject);
                    if (list!=null) {
                        boolean firstInfo = true;
                        for (InfoOnSubject info: list) {
                            if (!firstInfo) {
                                str.append("\n");
                            }
                            putInfoOnSubject(str, info);
                            firstInfo = false;
                        }
                    }
                    firstSubject=false;
                }
                if (!firstSubject
                        && !userNotifications.getInfoSubjects().isEmpty()
                        ) { //there were some tasks in the output, draw separator
                    addSeparator(str, SepType.THIN);
                }
                //messages section
                boolean firstMessage = true;
                for (String msgId: userNotifications.getMessageIDs()) {
                    if (!firstMessage) {
                        addSeparator(str, SepType.THIN);
                    }
                    Message msg = userNotifications.getMessage(msgId);
                    putMessage(str, msg);
                    firstMessage = false;
                }
                firstUser = false;
            }
        }

    }
    
    protected abstract void makeHeader(StringBuilder str) throws FormattingException;
    
    protected abstract void makeFooter(StringBuilder str) throws FormattingException;

}
