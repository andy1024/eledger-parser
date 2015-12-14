package org.warheim.eledger.formatter;

import java.io.File;
import java.io.IOException;
import org.slf4j.LoggerFactory;
import static org.warheim.eledger.parser.NotificationsDataCombiner.combine;
import org.warheim.eledger.parser.model.InfoOnSubject;
import org.warheim.eledger.parser.model.Message;
import org.warheim.eledger.parser.model.NotificationsData;
import org.warheim.eledger.parser.model.Subject;
import org.warheim.eledger.parser.model.User;
import org.warheim.formatter.FormattableModel;
import org.warheim.formatter.FormattingException;

/**
 *
 * @author andy
 */
public abstract class NotificationsFreeRollFormatter extends NotificationsTaggedFormatter {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(NotificationsFreeRollFormatter.class);
    
    @Override
    public void setModel(FormattableModel model) {
        this.notificationsData = (NotificationsData) model;
    }

    @Override
    protected void startUserTag(StringBuilder str, User user, int count) {
        if (count>0) {
            addSeparator(str, SepType.NORMAL);
        }
        putUser(str, user);
    }
    
    @Override
    protected void endUserTag(StringBuilder str, User user, int count) {
    }

    @Override
    protected void startSubjectTag(StringBuilder str, Subject subject, int count) {
        if (count>0) {
            addSeparator(str, SepType.THIN);
        }
        putSubject(str, subject);
    }
    
    @Override
    protected void endSubjectTag(StringBuilder str, Subject subject, int count) {
    }
    
    @Override
    protected void startInfoOnSubjectTag(StringBuilder str, InfoOnSubject info, int count) {
        if (count>0) {
            str.append("\n");
        }
        putInfoOnSubject(str, info);
    }
    
    @Override
    protected void endInfoOnSubjectTag(StringBuilder str, InfoOnSubject info, int count) {
    }
    
    @Override
    protected void startMessageTag(StringBuilder str, Message msg, int count) {
        if (count>0) {
            addSeparator(str, SepType.THIN);
        }
        putMessage(str, msg);
    }
    
    @Override
    protected void endMessageTag(StringBuilder str, Message msg, int count) {
        
    }

    //protected File prepareSourceDocument(StringBuilder str) throws IOException, FormattingException, InterruptedException;
    
    protected abstract void putUser(StringBuilder str, User user);

    protected abstract void putSubject(StringBuilder str, Subject subject);
    
    protected abstract void putInfoOnSubject(StringBuilder str, InfoOnSubject info);
    
    protected abstract void putMessage(StringBuilder str, Message msg);

}
