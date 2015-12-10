/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.warheim.eledger.parser.tests;

import java.io.IOException;
import org.slf4j.LoggerFactory;
import org.warheim.di.ObjectCreationException;
import org.warheim.net.RequestPreparationException;
import org.warheim.net.ResponseHandlerException;
import org.warheim.net.ReturnWebPageCall;
import org.warheim.net.WebExecutionException;
import org.warheim.net.WebRequest;
import org.warheim.net.WebRequestType;
import org.warheim.net.WrongStatusException;

/**
 *
 * @author andy
 */
public class WebCallTest extends ReturnWebPageCall {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(WebCallTest.class);

    public WebCallTest(int expectedOutcomeStatus, String url) {
        super(expectedOutcomeStatus, url, WebRequestType.GET);
    }

    @Override
    public void prepareRequest(WebRequest request) {
        
    }

    public static void main(String... args) throws IOException, WrongStatusException, ResponseHandlerException, RequestPreparationException, ObjectCreationException, WebExecutionException {
        WebCallTest wct = new WebCallTest(200, "http://192.168.1.29/printer/main.html");
        logger.info(wct.doCall());
    }
    
}
