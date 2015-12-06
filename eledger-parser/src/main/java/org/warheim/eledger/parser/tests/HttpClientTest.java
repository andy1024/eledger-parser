/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.warheim.eledger.parser.tests;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;

/**
 *
 * @author andy
 */
public class HttpClientTest {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(HttpClientTest.class);
    
    /**
     * http client testing method
     * @param args
     * @throws Exception
     */
    public static final void main(String[] args) throws Exception {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpget = new HttpGet("http://httpbin.org/");
            logger.info("Executing request " + httpget.getRequestLine());
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                @Override
                public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }
            };
            String responseBody = httpclient.execute(httpget, responseHandler);
            logger.info("----------------------------------------");
            logger.info(responseBody);
        }
    }
    
}
