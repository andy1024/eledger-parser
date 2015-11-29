package org.warheim.net;

/**
 *
 * @author andy
 */
public class WrongStatusException extends Exception {
    public WrongStatusException(int expected, int actual) {
        super("Expected " + expected + ", got " + actual + " exitting");
    }
}
