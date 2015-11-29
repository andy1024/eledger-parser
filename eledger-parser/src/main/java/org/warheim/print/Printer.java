package org.warheim.print;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

/**
 * Java printing support class
 *
 * @author andy
 */
public class Printer {
    protected String printerID;
    protected Doc doc;

    public Printer(String printerID, Doc doc) {
        this.printerID = printerID;
        this.doc = doc;
    }
    
    public boolean print() throws PrintingException {
        DocFlavor psInFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();

        PrintService[] services = PrintServiceLookup.lookupPrintServices(psInFormat, aset);
         
        PrintService myPrinter = null;
        for (PrintService service : services) {
            String svcName = service.toString();           
            System.out.println("service found: "+svcName);
            if (svcName.contains(printerID)) {
                myPrinter = service;
                System.out.println("Searched for printer found: "+svcName);
                break;
            }
        }
         
        if (myPrinter != null) {            
            DocPrintJob job = myPrinter.createPrintJob();
            try {
                job.print(doc, aset);
            } catch (PrintException ex) {
                Logger.getLogger(Printer.class.getName()).log(Level.SEVERE, null, ex);
                throw new PrintingException(ex);
            }
        } else {
            throw new PrintingException(new Exception("no printer services found"));
        }
        return true;
    }
    
}
