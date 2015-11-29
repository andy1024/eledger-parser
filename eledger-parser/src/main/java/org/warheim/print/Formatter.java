/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.warheim.print;

import javax.print.Doc;

/**
 *
 * @author andy
 */
public interface Formatter {
    
    public void setModel(FormattableModel model);
    
    public abstract String format();
    
    public abstract Doc getDocument() throws FormattingException;
}
