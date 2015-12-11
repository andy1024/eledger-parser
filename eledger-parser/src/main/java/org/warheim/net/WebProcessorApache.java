package org.warheim.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;
import org.warheim.eledger.parser.Config;

/**
 *
 * @author andy
 */
public class WebProcessorApache implements WebProcessor {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(WebProcessorApache.class);

    protected HttpUriRequest getRequest(WebRequest request) throws WebExecutionException {
        HttpUriRequest hreq=null;
        if (WebRequestType.GET == request.getType()) {
            hreq = new HttpGet(request.getUrl());
        } else if (WebRequestType.POST == request.getType()) {
            hreq = new HttpPost(request.getUrl());
            int dataEntriesSize = request.getDataEntries().size();
            if (dataEntriesSize>0) {
                List<NameValuePair> nameValuePairs = new ArrayList<>(dataEntriesSize);
                for (String key: request.getDataEntries().keySet()) {
                    nameValuePairs.add(new BasicNameValuePair(key, request.getDataEntries().get(key)));
                }
                HttpPost post = (HttpPost)hreq;
                try {
                    post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                } catch (UnsupportedEncodingException ex) {
                    logger.error("Error while preparing request data for post call", ex);
                    throw new WebExecutionException(ex);
                }
            }
        }

        if (hreq!=null) {
            for (String key: request.getHeaders().keySet()) {
                hreq.addHeader(key, request.getHeaders().get(key));
            }
            for (String key: request.getCookies().keySet()) {
                hreq.addHeader("Cookie", key + "=" + request.getCookies().get(key) + ";");
            }
        } else {
            throw new WebExecutionException("Bad request method");
        }
        return hreq;
    }
    
    protected WebResponse getResponse(HttpResponse hresp) throws WebExecutionException {
        String body;
        HttpEntity entity = hresp.getEntity();
        try {
            body = (entity != null ? EntityUtils.toString(entity) : null);
        } catch (IOException | ParseException ex) {
            logger.error("Error while reading response from server", ex);
            logger.debug("Entity:" + entity.toString());
            throw new WebExecutionException(ex);
        }
        WebResponse response = new WebResponseImpl(hresp.getStatusLine().getStatusCode(), body);

        for (Header header: hresp.getAllHeaders()) {
            if ("Set-Cookie".equals(header.getName())) {
                processCookie(response, header.getValue());
            } else {
                response.addHeader(header.getName(), header.getValue());
            }
        }
        return response;
    }
    
    @Override
    public WebResponse execute(WebRequest request) throws WebExecutionException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        WebResponse response;
        try {
            HttpUriRequest hreq = getRequest(request);
            logger.debug(request.show());
            HttpResponse resp = httpclient.execute(hreq);
            response = getResponse(resp);
            logger.debug(response.show());
            httpclient.close();
        } catch (IOException ex) {
            logger.error("Error while reading response from server", ex);
            throw new WebExecutionException(ex);
        }
        return response;

    }

    protected static void processCookie(WebResponse response, String input) {
        String[] entryElements = input.split("=");
        switch (entryElements.length) {
            case 0:
                logger.warn("Empty cookie: " + input);
                break;
            case 1: //no value cookie
                response.addCookie(entryElements[0], null);
                break;
            default:
                String[] valueElements = entryElements[1].split(";");
                if (valueElements==null||valueElements.length<2) {
                    logger.warn("Cookie format error: " + input);
                }
                if (entryElements.length>2) {
                    logger.warn("Too many elements in cookie description: " + input);
                }
                response.addCookie(entryElements[0], valueElements[0]);
                break;
        }
    }
    
}
