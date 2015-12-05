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
import org.warheim.formatter.FormattableModel;
import org.warheim.formatter.Formatter;
import org.warheim.formatter.FormattingException;

/**
 *
 * @author andy
 */
public abstract class NotificationsFreeRollFormatter extends NotificationsTaggedFormatter {

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
