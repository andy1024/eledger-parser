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
public class RequestPreparationException extends Exception {
    public RequestPreparationException(String msg) {
        super(msg);
    }
    public RequestPreparationException(Throwable ex) {
        super(ex);
    }
}
