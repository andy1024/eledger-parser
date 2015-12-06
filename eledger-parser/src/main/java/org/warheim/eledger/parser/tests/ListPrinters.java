/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.warheim.eledger.parser.tests;

import javax.print.*;
import javax.print.attribute.*;
import org.slf4j.LoggerFactory;

public class ListPrinters {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ListPrinters.class);
    
    public static void main(String args[]) {

        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, aset);

        for (PrintService service : services) {
            String svcName = service.toString();
            logger.info("service found: " + svcName);
        }
    }
}
