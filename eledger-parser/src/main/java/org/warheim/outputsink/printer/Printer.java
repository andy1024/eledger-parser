package org.warheim.outputsink.printer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import org.slf4j.LoggerFactory;
import org.warheim.formatter.FormattedDocument;
import org.warheim.outputsink.Output;

/**
 * Java printing support class
 *
 * @author andy
 */
public class Printer implements Output {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Printer.class);    
    
    protected String outputDeviceID;
    protected FormattedDocument formattedDocument;
    protected Doc doc;

    @Override
    public void setOutputDeviceID(String outputDeviceID) {
        this.outputDeviceID = outputDeviceID;
    }

    public Printer() {
        
    }
    
    protected Doc getDocument() throws PrintingException {
        Doc myDoc;
        try {
            FileInputStream psStream;
            psStream = new FileInputStream(formattedDocument.getFile());
            DocFlavor psInFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
            myDoc = new SimpleDoc(psStream, psInFormat, null);  
        } catch (IOException ex) {
            logger.error("Error while getting document", ex);
            throw new PrintingException(ex);
        }
        return myDoc;
    }
    
    @Override
    public boolean process() throws PrintingException {
        if (outputDeviceID==null||outputDeviceID.isEmpty()||formattedDocument.getFile()==null) {
            throw new PrintingException("No printer or inputFile defined");
        }
        DocFlavor psInFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();

        PrintService[] services = PrintServiceLookup.lookupPrintServices(psInFormat, aset);
         
        PrintService myPrinter = null;
        for (PrintService service : services) {
            String svcName = service.toString();           
            logger.debug("service found: "+svcName);
            if (svcName.contains(outputDeviceID)) {
                myPrinter = service;
                logger.info("Printer found: "+svcName);
                break;
            }
        }
         
        if (myPrinter != null) {
            doc = getDocument();
            DocPrintJob job = myPrinter.createPrintJob();
            try {
                job.print(doc, aset);
            } catch (PrintException ex) {
                logger.error("Error while printing", ex);
                throw new PrintingException(ex);
            }
        } else {
            throw new PrintingException(new Exception("no printer services found"));
        }
        return true;
    }

    @Override
    public void setFormattedDocument(FormattedDocument formattedDocument) {
        this.formattedDocument = formattedDocument;
    }
    
}
