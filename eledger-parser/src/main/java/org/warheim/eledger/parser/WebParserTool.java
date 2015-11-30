package org.warheim.eledger.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author andy
 */
public class WebParserTool {
    
    public static String noHTML(String input) {
        Document doc = Jsoup.parse(input);
        return doc.text();
    }
    
    public static String noHTML(Element input) {
        return removeConsecutiveNewlines(removeConsecutiveSpaces(removeNbsp(Jsoup.parse(input.text()).text()))).trim();
    }

    protected static String removeConsecutiveNewlines(String input) {
        return input.replaceAll("[\n]+", "\n");
    }
    
    protected static String removeConsecutiveSpaces(String input) {
        return input.replaceAll("\\s+", " ");
    }
    
    protected static String removeNbsp(String input) {
        String retval = input.replaceAll("&nbsp;", " ");
        return retval.  replaceAll("\u00a0", " ");
    }
    
    public static String compact(String input) {
        return removeConsecutiveNewlines(removeConsecutiveSpaces(removeNbsp(input)));
    }
    
}
