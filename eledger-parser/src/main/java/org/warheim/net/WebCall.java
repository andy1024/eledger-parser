/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.warheim.net;

import java.io.IOException;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 *
 * @author andy
 */
public abstract class WebCall {
    public static final String REQUEST_TYPE_GET = "GET";
    public static final String REQUEST_TYPE_POST = "POST";
    
    protected int expectedOutcomeStatus;
    protected String url;
    protected String requestType;
    
    public HttpUriRequest createRequest(String url) {
        if (REQUEST_TYPE_GET.equals(requestType)) {
            return new HttpGet(url);
        } else if (REQUEST_TYPE_POST.equals(requestType)) {
            return new HttpPost(url);
        }
        return null;
    }
    
    public WebCall(int expectedOutcomeStatus, String url, String requestType) {
        this.expectedOutcomeStatus = expectedOutcomeStatus;
        this.url = url;
        this.requestType = requestType;
    }

    protected void showRequest(HttpRequest req) {
        //if (1==1) return;
        System.out.println("Executing request " + req.getRequestLine());
        for (Header h: req.getAllHeaders()) {
            System.out.println(h);
        }
    }

    public abstract void prepareRequest(HttpRequest request) throws RequestPreparationException;
    
    public abstract String handleResponse(HttpResponse resp) throws ResponseHandlerException;
    
    public String doCall() throws IOException, WrongStatusException, ResponseHandlerException, RequestPreparationException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String result = null;
        try {
            HttpUriRequest httpget = createRequest(url);
            prepareRequest(httpget);
            showRequest(httpget);
            HttpResponse resp = httpclient.execute(httpget);
            if (resp.getStatusLine().getStatusCode()==expectedOutcomeStatus) {
                result = handleResponse(resp);
            } else {
                throw new WrongStatusException(expectedOutcomeStatus, resp.getStatusLine().getStatusCode());
            }
        } finally {
            httpclient.close();
        }
        return result;
    }
}