package org.warheim.eledger.formatter;

import java.io.File;
import java.io.IOException;
import org.warheim.eledger.parser.model.InfoOnSubject;
import org.warheim.eledger.parser.model.Message;
import org.warheim.eledger.parser.model.Subject;
import org.warheim.eledger.parser.model.User;
import org.warheim.formatter.FormattingException;

/**
 *
 * @author andy
 */
public class NotificationsHTMLFormatter extends NotificationsFreeRollFormatter {

    @Override
    protected void addSeparator(StringBuilder str, SepType sepType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected File prepareSourceDocument(StringBuilder str) throws IOException, FormattingException, InterruptedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void putUser(StringBuilder str, User user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void putSubject(StringBuilder str, Subject subject) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void putInfoOnSubject(StringBuilder str, InfoOnSubject info) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void putMessage(StringBuilder str, Message msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void makeHeader(StringBuilder str) throws FormattingException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void makeFooter(StringBuilder str) throws FormattingException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
