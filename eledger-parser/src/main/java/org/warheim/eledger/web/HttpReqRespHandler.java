package org.warheim.eledger.web;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import org.warheim.net.RequestPreparationException;
import org.warheim.net.WrongStatusException;
import org.warheim.net.ResponseHandlerException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpMessage;
import org.warheim.eledger.parser.Config;
import org.warheim.eledger.parser.model.Source;
import org.warheim.eledger.parser.model.SourceType;
import org.warheim.eledger.parser.model.User;

/**
 *
 * @author andy
 */
public class HttpReqRespHandler {
    
    private String etag=null;
    private String cookie=null;

    private final User user;

    private final String base = Config.get("web.baseUrl");

    public HttpReqRespHandler(User user) {
        this.user=user;
    }

    protected void sleep() {
        Random r = new Random();
        try {
            Thread.sleep(
                    r.nextInt(Config.getInt("web.wait.random.max")) +
                            Config.getInt("web.wait.random.min")
            );
        } catch (InterruptedException ex) {
            Logger.getLogger(HttpReqRespHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Source> getAllData() throws IOException, WrongStatusException, ResponseHandlerException, RequestPreparationException {
        List<Source> retval = new ArrayList<>();
        
        String authPage = base + "/" + Config.get("web.authPage");
        String taskListPage = base + "/" + Config.get("web.taskListPage");
        String testListPage = base + "/" + Config.get("web.testListPage");
        String messagesListPage = base + "/" + Config.get("web.messagesListPage");
    
        InitiateConnection init = new InitiateConnection(taskListPage);
        init.doCall();
        etag = init.getEtag();
        cookie = init.getCookie();
        sleep();
        Authorize auth = new Authorize(authPage, taskListPage, base, user.getName(), user.getPass(), cookie, etag);
        auth.doCall();
        
        String taskSourcePage = null;
        GetTasksList taskListGetter = new GetTasksList(taskListPage, cookie, etag);
        taskSourcePage = taskListGetter.doCall();
        retval.add(new Source(user, SourceType.TASKLIST, taskSourcePage));
        sleep();
        
        String testSourcePage = null;
        GetTestsList testListGetter = new GetTestsList(testListPage, cookie, etag);
        testSourcePage = testListGetter.doCall();
        retval.add(new Source(user, SourceType.TESTLIST, testSourcePage));
        
        sleep();
        String messagesSourcePage = null;
        GetMessagesList messageListGetter = new GetMessagesList(messagesListPage, cookie, etag);
        messagesSourcePage = messageListGetter.doCall();
        retval.add(new Source(user, SourceType.MESSAGES, messagesSourcePage));
        
        sleep();
        return retval;
    }
    
    public static void addCommonHeaders(HttpMessage httpMessage) {
        /*
        -H "Accept: $ACCEPT" 
        -H 'Connection: keep-alive' 
        -H 'Accept-Encoding: gzip, deflate, sdch' 
        -H "Accept-Language: $ACCEPT_LANG" 
        -H 'Upgrade-Insecure-Requests: 1' 
        -H "User-Agent: $USER_AGENT"
        */
        httpMessage.addHeader(Config.get("header.accept.key"), Config.get("header.accept.value"));
        httpMessage.addHeader(Config.get("header.connection.key"), Config.get("header.connection.value"));
        httpMessage.addHeader(Config.get("header.acceptEncoding.key"), Config.get("header.acceptEncoding.value"));
        httpMessage.addHeader(Config.get("header.acceptLanguage.key"), Config.get("header.acceptLanguage.value"));
        httpMessage.addHeader(Config.get("header.upgradeInsecureRequests.key"), Config.get("header.upgradeInsecureRequests.value"));
        httpMessage.addHeader(Config.get("header.userAgent.key"), Config.get("header.userAgent.value"));
    }
    
    public static void addExtHeaders(HttpMessage httpMessage, String cookie,
            String origin, String referer, String etag
        ) {
        /*
        -H "Cookie: $AUTH_COOKIE_NAME=$COOKIE" 
        -H "Origin: $BASE_HTTP" 
        -H 'Content-Type: application/x-www-form-urlencoded' 
        -H 'Cache-Control: max-age=0' 
        -H "Referer: $BASE_HTTP/$DEST_PAGE" 
        -H 'Connection: keep-alive' 
        If-None-Match: \"$ETAG\"`
        */
        httpMessage.addHeader("Cookie", Config.get("web.authCookieName") + "=" + cookie);
        if (origin!=null) {
            httpMessage.addHeader("Origin", origin);
        }
        httpMessage.addHeader(Config.get("header.contentType.key"), Config.get("header.contentType.value"));
        httpMessage.addHeader(Config.get("header.cacheControl.key"), Config.get("header.cacheControl.value"));
        
        httpMessage.addHeader(Config.get("header.referer.key"), referer);
        httpMessage.addHeader(Config.get("header.ifNoneMatch.key"), etag);
    }
    
    protected static String extractCookieValue(String input) {
        return input.replaceFirst(".*" + Config.get("web.authCookieName") + "=(.*);.*","$1");
    }
    
    public void logout() {
        
    }
}
