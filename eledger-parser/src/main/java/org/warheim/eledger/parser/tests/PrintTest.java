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
import org.warheim.eledger.parser.Config;

public class PrintTest {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PrintTest.class);

    public static void main(String args[]) throws Exception {

        FileInputStream psStream;
        psStream = new FileInputStream("/tmp/eledger7882184143513440386.pdf");
        DocFlavor psInFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
        Doc myDoc = new SimpleDoc(psStream, psInFormat, null);
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        //aset.add(MediaSizeName.ISO_B8);

        PrintService[] services = PrintServiceLookup.lookupPrintServices(psInFormat, aset);

        // this step is necessary because I have several printers configured
        PrintService myPrinter = null;
        for (PrintService service : services) {
            String svcName = service.toString();
            logger.info("service found: " + svcName);
            if (svcName.contains(Config.get(Config.KEY_PRINTER))) {
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
