package org.warheim.eledger.custom;

import org.warheim.app.EventHandler;
import org.warheim.app.EventHandlerException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.warheim.net.RequestPreparationException;
import org.warheim.net.ResponseHandlerException;
import org.warheim.net.WebCall;
import org.warheim.net.WrongStatusException;

/**
 * Custom printing event handler
 * Sends reset command to the printer after the printing job
 * It is needed because of an error in printer driver causing printer hang
 * 
 * @author andy
 */
public class QL710WPrintingEventHandler implements EventHandler {

    private int sleepTime = 10000;
    private String resetUrl;
    
    @Override
    public void handle() throws EventHandlerException {
        WebCall resetPrinter = new ResetQL710W(resetUrl);
        try {
            Thread.sleep(sleepTime);
            resetPrinter.doCall();
        } catch (IOException | WrongStatusException | ResponseHandlerException | RequestPreparationException | InterruptedException ex) {
            Logger.getLogger(QL710WPrintingEventHandler.class.getName()).log(Level.SEVERE, null, ex);
            throw new EventHandlerException(ex);
        }
    }

    public void setSleepTime(String sleepTime) {
        this.sleepTime = Integer.parseInt(sleepTime);
    }

    public void setResetUrl(String resetUrl) {
        this.resetUrl = resetUrl;
    }
    
    
}
