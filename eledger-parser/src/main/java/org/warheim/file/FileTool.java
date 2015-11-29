package org.warheim.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * File handling support class
 *
 * @author andy
 */
public class FileTool {
    public static String readFile(String name) throws FileNotFoundException, IOException {
        String doc;
        try (BufferedReader br = new BufferedReader(new FileReader(name))) {
            String line;
            doc = "";
            while ((line = br.readLine())!=null) {
                doc+=line;
            }
        }
        return doc;
    }
    public static void writeFile(String name, Object obj) throws FileNotFoundException, IOException {
        try (PrintWriter pw = new PrintWriter(name)) {
            pw.println(obj);
        }
    }
    public static void writeFile(String dir, String name, Object obj) throws FileNotFoundException, IOException {
        File fDir = new File(dir);
        if (!fDir.isDirectory()) {
            fDir.mkdir();
        }
        try (PrintWriter pw = new PrintWriter(dir + java.io.File.separator + name)) {
            pw.println(obj);
        }
    }
}
