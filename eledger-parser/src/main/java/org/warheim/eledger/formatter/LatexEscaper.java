package org.warheim.eledger.formatter;

/**
 *
 * @author andy
 */
public class LatexEscaper {
    /**
     * escapes input string by preceeding illegal characters with double backslash
     * two and four backslashes in its implementation come from regexp-escaping needs themselves
     * @param input
     * @return 
     */
    public static String escape(String input) {
        return input.replaceAll("\\&", "\\\\&");
    }
}
