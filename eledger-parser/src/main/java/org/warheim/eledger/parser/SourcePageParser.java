/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.warheim.eledger.parser;

import org.warheim.eledger.parser.model.Source;
import org.warheim.eledger.parser.model.UserNotifications;

/**
 *
 * @author andy
 */
public abstract class SourcePageParser {
    
    public abstract void parse(Source source, UserNotifications un);
}
