package org.warheim.net;

import java.util.Collection;

/**
 *
 * @author andy
 */
public class WrongStatusException extends Exception {
    public WrongStatusException(Collection<Integer> expected, int actual) {
        super("Expected " + expected.toString() + ", got " + actual + " exitting");
    }
}
