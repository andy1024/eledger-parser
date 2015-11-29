/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.warheim.eledger.parser.tests;

import java.io.*;
import javax.print.*;
import javax.print.attribute.*; 
import javax.print.attribute.standard.*; 
  
public class ListPrinters{
  
   public static void main(String args[]){
       
        DocFlavor psInFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        //PrintService[] services = PrintServiceLookup.lookupPrintServices(psInFormat, aset);
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, aset);
         
        // this step is necessary because I have several printers configured
        PrintService myPrinter = null;
        for (int i = 0; i < services.length; i++){
            String svcName = services[i].toString();           
            System.out.println("service found: "+svcName);
        }
   }
}