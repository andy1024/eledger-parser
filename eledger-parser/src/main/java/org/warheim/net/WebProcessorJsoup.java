package org.warheim.net;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Connection.Request;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.slf4j.LoggerFactory;

/**
 *
 * @author andy
 */
public class WebProcessorJsoup implements WebProcessor {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(WebProcessorJsoup.class);

    protected Connection getRequest(WebRequest request) throws WebExecutionException {
        Connection conn = null;
        conn = Jsoup.connect(request.getUrl());
        for (String key : request.getHeaders().keySet()) {
            conn = conn.header(key, request.getHeaders().get(key));
        }
        if (WebRequestType.GET == request.getType()) {
            conn = conn.method(Connection.Method.GET);
        } else if (WebRequestType.POST == request.getType()) {
            int dataEntriesSize = request.getDataEntries().size();
            if (dataEntriesSize > 0) {
                for (String key: request.getDataEntries().keySet()) {
                    
                    conn = conn.data(key, request.getDataEntries().get(key));
                }
            }
            conn = conn.method(Connection.Method.POST);
        }

        if (conn != null) {
            return conn;
        } else {
            throw new WebExecutionException("Bad request method");
        }
    }

    protected WebResponse getResponse(Response resp) throws WebExecutionException {
        String body;
        body = resp.body();
        WebResponse response = new WebResponseImpl(resp.statusCode(), body);
        for (String key: resp.headers().keySet()) {
            response.addHeader(key, resp.headers().get(key));
        }
        for (String key: resp.cookies().keySet()) {
            response.addHeader("Set-Cookie",  key + "=" + resp.cookies().get(key) + ";");
        }
        return response;
    }

    @Override
    public WebResponse execute(WebRequest request) throws WebExecutionException {
        WebResponse response = null;
        try {
            request.show();
            Connection conn = getRequest(request);
            response = getResponse(conn.execute());
        } catch (IOException ex) {
            logger.error("Error while reading response from server", ex);
            throw new WebExecutionException(ex);
        }
        return response;

    }

}
