/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
