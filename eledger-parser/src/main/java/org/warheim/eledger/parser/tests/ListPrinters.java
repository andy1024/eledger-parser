/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.warheim.eledger.parser.tests;

import javax.print.*;
import javax.print.attribute.*;

public class ListPrinters {

    public static void main(String args[]) {

        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        //PrintService[] services = PrintServiceLookup.lookupPrintServices(psInFormat, aset);
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, aset);

        // this step is necessary because I have several printers configured
        PrintService myPrinter = null;
        for (PrintService service : services) {
            String svcName = service.toString();
            System.out.println("service found: " + svcName);
        }
    }
}
