/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.warheim.eledger.parser.tests;

import javax.print.Doc;
import org.warheim.eledger.formatter.NotificationsPdfLatexFormatter;
import org.warheim.print.Formatter;
import org.warheim.print.Printer;

/**
 *
 * @author andy
 */
public class PdfLatexPrinterTest {
    public static void main(String... args) throws Exception {
        final String text = "Let me inform you, that you have some surplus money on your account";
//        Formatter fmt = new NotificationsPdfLatexFormatter() {
//            @Override
//            public String format() {
//                return text;
//            }
//        };
//        Doc doc = fmt.getDocument();
//        Printer pr = new Printer("650Brother_QL-710W", doc);
//        pr.print();
    }
}
