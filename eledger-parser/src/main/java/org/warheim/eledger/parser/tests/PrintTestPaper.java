/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.warheim.eledger.parser.tests;

import java.io.*;
import javax.print.*;
import javax.print.attribute.*;
import org.slf4j.LoggerFactory;

public class PrintTestPaper {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PrintTestPaper.class);

    public static void main(String args[]) throws Exception {

        FileInputStream psStream;
        psStream = new FileInputStream("/home/andy/Downloads/bzdet0.pdf");
        DocFlavor psInFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
        Doc myDoc = new SimpleDoc(psStream, psInFormat, null);
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        PrintService[] services = PrintServiceLookup.lookupPrintServices(psInFormat, aset);

        // this step is necessary because I have several printers configured
        PrintService myPrinter = null;
        for (PrintService service : services) {
            String svcName = service.toString();
            logger.info("service found: " + svcName);
            if (svcName.contains("Kyocera_Kyocera_FS-1010")) {
                myPrinter = service;
                logger.info("my printer found: " + svcName);
                break;
            }
        }

        if (myPrinter != null) {
            DocPrintJob job = myPrinter.createPrintJob();
            job.print(myDoc, aset);
        } else {
            logger.warn("no printer services found");
        }
    }
}
