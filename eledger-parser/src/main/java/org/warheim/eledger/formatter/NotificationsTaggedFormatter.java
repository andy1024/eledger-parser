package org.warheim.eledger.formatter;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import org.slf4j.LoggerFactory;
import static org.warheim.eledger.parser.NotificationsDataCombiner.combine;
import org.warheim.eledger.parser.model.InfoOnSubject;
import org.warheim.eledger.parser.model.Message;
import org.warheim.eledger.parser.model.NotificationsData;
import org.warheim.eledger.parser.model.Subject;
import org.warheim.eledger.parser.model.User;
import org.warheim.eledger.parser.model.UserNotifications;
import org.warheim.formatter.FormattableModel;
import org.warheim.formatter.Formatter;
import org.warheim.formatter.FormattingException;

/**
 *
 * @author andy
 */
public abstract class NotificationsTaggedFormatter implements Formatter {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(NotificationsTaggedFormatter.class);

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
            logger.debug(outFile.getAbsolutePath());
        } catch (IOException | InterruptedException ex) {
            logger.error("Error while creating output file", ex);
            throw new FormattingException(ex);
        }
        return outFile;
    }

    protected abstract File prepareSourceDocument(StringBuilder str) throws IOException, FormattingException, InterruptedException;
    
    protected abstract void startUserTag(StringBuilder str, User user, int count);
    
    protected abstract void endUserTag(StringBuilder str, User user, int count);

    protected abstract void startSubjectTag(StringBuilder str, Subject subject, int count);
    
    protected abstract void endSubjectTag(StringBuilder str, Subject subject, int count);
    
    protected abstract void startInfoOnSubjectTag(StringBuilder str, InfoOnSubject info, int count);
    
    protected abstract void endInfoOnSubjectTag(StringBuilder str, InfoOnSubject info, int count);
    
    protected abstract void startMessageTag(StringBuilder str, Message msg, int count);
    
    protected abstract void endMessageTag(StringBuilder str, Message msg, int count);
    
    protected void makeBody(StringBuilder str) throws FormattingException {
        if (notificationsData!=null) {
            int userCount = 0;
            for (User user: notificationsData.getUsers()) {
                UserNotifications userNotifications = notificationsData.getNotificationsForUser(user);
                if (userNotifications.isEmpty()) {
                    continue;
                }
                startUserTag(str, user, userCount);
                //tasks and tests combined section
                int subjectCount = 0;
                for (Subject subject: userNotifications.getInfoSubjects()) {
                    startSubjectTag(str, subject, subjectCount);
                    Set<InfoOnSubject> list = userNotifications.getInfoForSubject(subject);
                    if (list!=null) {
                        int infoCount = 0;
                        for (InfoOnSubject info: list) {
                            startInfoOnSubjectTag(str, info, infoCount);
                            endInfoOnSubjectTag(str, info, infoCount++);
                        }
                    }
                    endSubjectTag(str, subject, subjectCount++);
                }
                //messages section
                int msgCount = 0;
                for (String msgId: userNotifications.getMessageIDs()) {
                    Message msg = userNotifications.getMessage(msgId);
                    startMessageTag(str, msg, msgCount);
                    endMessageTag(str, msg, msgCount++);
                }
                endUserTag(str, user, userCount++);
            }
        }

    }
    
    protected abstract void makeHeader(StringBuilder str) throws FormattingException;
    
    protected abstract void makeFooter(StringBuilder str) throws FormattingException;

}