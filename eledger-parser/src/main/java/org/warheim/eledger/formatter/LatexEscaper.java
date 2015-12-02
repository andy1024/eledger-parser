package org.warheim.eledger.formatter;

/**
 *
 * @author andy
 */
public class LatexEscaper {
    public static String escape(String input) {
        return input.replaceAll("&", "\\&");
    }
}
