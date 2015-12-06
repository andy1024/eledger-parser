/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.warheim.eledger.parser.tests;

import java.io.*;
import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.PrinterResolution;

public class PrintTestUsingPdfLatex {

    //5pt, 0.1in 2.0in 0.05in
    public static File prepareTex(String text, String fontSize, String height, String width, String margin) throws IOException {
        File tempFile = File.createTempFile("printTest", ".tex");
        try (PrintWriter pw = new PrintWriter(tempFile, "UTF-8")) {
            pw.println("\\documentclass[a4paper," + fontSize + "]{article}");
            pw.println("\\usepackage[paperheight=" + height + ",paperwidth=" + width
                    + ",margin=" + margin + ",heightrounded]{geometry}");
            pw.println("\\usepackage[utf8]{inputenc}");
            pw.println("\\usepackage{polski}");
            pw.println("\\begin{document}");
            pw.println(text);
            pw.println("\\end{document}");
        }
        return tempFile;
    }

    public static void main(String args[]) throws Exception {

        File tempFile = prepareTex("Zażółć gęślą jaźń", "5pt", "0.5in", "2.0in", "0.05in");
        Process p = Runtime.getRuntime().exec("pdflatex -output-directory /tmp " + tempFile.getAbsolutePath());
        p.waitFor();
        String outname = tempFile.getPath().replace(".tex", ".pdf");
        System.err.println(outname);
        FileInputStream psStream;
        psStream = new FileInputStream(outname);
        DocFlavor psInFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
        Doc myDoc = new SimpleDoc(psStream, psInFormat, null);
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        //aset.add(MediaSizeName.ISO_B8);

        PrintService[] services = PrintServiceLookup.lookupPrintServices(psInFormat, aset);

        // this step is necessary because I have several printers configured
        PrintService myPrinter = null;
        for (PrintService service : services) {
            String svcName = service.toString();
            System.out.println("service found: " + svcName);
            if (svcName.contains("650Brother_QL-710W")) {
                myPrinter = service;
                System.out.println("my printer found: " + svcName);
                break;
            }
        }

        if (myPrinter != null) {
            DocPrintJob job = myPrinter.createPrintJob();
            aset.add(new PrinterResolution(300, 300, PrinterResolution.DPI));
            job.print(myDoc, aset);
        } else {
            System.out.println("no printer services found");
        }
    }
}
