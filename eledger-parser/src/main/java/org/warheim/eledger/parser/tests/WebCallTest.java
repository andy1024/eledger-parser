/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.warheim.eledger.parser.tests;

import java.io.IOException;
import org.apache.http.HttpMessage;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.warheim.net.RequestPreparationException;
import org.warheim.net.ResponseHandlerException;
import org.warheim.net.ReturnWebPageCall;
import org.warheim.net.WebCall;
import org.warheim.net.WrongStatusException;

/**
 *
 * @author andy
 */
public class WebCallTest extends ReturnWebPageCall {

    public WebCallTest(int expectedOutcomeStatus, String url) {
        super(expectedOutcomeStatus, url, WebCall.REQUEST_TYPE_GET);
    }

    @Override
    public void prepareRequest(HttpRequest request) {
        
    }

    public static void main(String... args) throws IOException, WrongStatusException, ResponseHandlerException, RequestPreparationException {
        WebCallTest wct = new WebCallTest(200, "http://192.168.1.29/printer/main.html");
        System.out.println(wct.doCall());
    }
    
}
