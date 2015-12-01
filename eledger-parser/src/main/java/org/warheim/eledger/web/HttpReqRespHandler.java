package org.warheim.eledger.web;

import org.warheim.net.RequestPreparationException;
import org.warheim.net.WrongStatusException;
import org.warheim.net.ResponseHandlerException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpMessage;
import org.warheim.eledger.parser.Config;
import org.warheim.eledger.parser.model.Source;
import org.warheim.eledger.parser.model.SourceType;
import org.warheim.eledger.parser.model.User;

/**
 * Main class for web interaction
 *
 * @author andy
 */
public class HttpReqRespHandler {
    
    private String etag=null;
    private String cookie=null;

    private final User user;

    private final String base = Config.get(Config.KEY_BASE_URL);

    public HttpReqRespHandler(User user) {
        this.user=user;
    }

    protected void sleep() {
        Random r = new Random();
        try {
            Thread.sleep(r.nextInt(Config.getInt(Config.KEY_WAIT_RANDOM_MAX)) +
                            Config.getInt(Config.KEY_WAIT_RANDOM_MIN)
            );
        } catch (InterruptedException ex) {
            Logger.getLogger(HttpReqRespHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Source> getAllData() throws IOException, WrongStatusException, ResponseHandlerException, RequestPreparationException {
        List<Source> retval = new ArrayList<>();
        
        String authPage = base + "/" + Config.get(Config.KEY_AUTH_PAGE);
        String taskListPage = base + "/" + Config.get(Config.KEY_TASK_LIST_PAGE);
        String testListPage = base + "/" + Config.get(Config.KEY_TEST_LIST_PAGE);
        String messagesListPage = base + "/" + Config.get(Config.KEY_MESSAGES_LIST_PAGE);
    
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
    
    public List<Source> getMessagesContents(Set<String> msgIds) throws IOException, WrongStatusException, ResponseHandlerException, RequestPreparationException {
        List<Source> retval = new ArrayList<>();
        String messagePageUrl = base + "/" + Config.get(Config.KEY_MESSAGE_PAGE);
        String messageInboxPageUrl = base + "/" + Config.get(Config.KEY_MESSAGES_LIST_PAGE);
        
        for (String msgId: msgIds) {
            String messagePageResponse = null;
            GetMessage messageGetter = new GetMessage(messagePageUrl, msgId, cookie, etag, messageInboxPageUrl);
            messagePageResponse = messageGetter.doCall();
            retval.add(new Source(user, SourceType.MESSAGE_CONTENT, messagePageResponse, msgId));
            sleep();
        }
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
        httpMessage.addHeader(Config.get(Config.KEY_HEADER_ACCEPT_KEY), Config.get(Config.KEY_HEADER_ACCEPT_VALUE));
        httpMessage.addHeader(Config.get(Config.KEY_HEADER_CONNECTION_KEY), Config.get(Config.KEY_HEADER_CONNECTION_VALUE));
        httpMessage.addHeader(Config.get(Config.KEY_HEADER_ACCEPT_ENCODING_KEY), Config.get(Config.KEY_HEADER_ACCEPT_ENCODING_VALUE));
        httpMessage.addHeader(Config.get(Config.KEY_HEADER_ACCEPT_LANGUAGE_KEY), Config.get(Config.KEY_HEADER_ACCEPT_LANGUAGE_VALUE));
        httpMessage.addHeader(Config.get(Config.KEY_HEADER_UPGRADE_INSECURE_REQUESTS_KEY), Config.get(Config.KEY_HEADER_UPGRADE_INSECURE_REQUESTS_VALUE));
        httpMessage.addHeader(Config.get(Config.KEY_HEADER_USER_AGENT_KEY), Config.get(Config.KEY_HEADER_USER_AGENT_VALUE));
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
        httpMessage.addHeader("Cookie", Config.get(Config.KEY_AUTH_COOKIE_NAME) + "=" + cookie);
        if (origin!=null) {
            httpMessage.addHeader("Origin", origin);
        }
        httpMessage.addHeader(Config.get(Config.KEY_HEADER_CONTENT_TYPE_KEY), Config.get(Config.KEY_HEADER_CONTENT_TYPE_VALUE));
        httpMessage.addHeader(Config.get(Config.KEY_HEADER_CACHE_CONTROL_KEY), Config.get(Config.KEY_HEADER_CACHE_CONTROL_VALUE));
        if (referer!=null) {
            httpMessage.addHeader(Config.get(Config.KEY_HEADER_REFERER_KEY), referer);
        }
        if (etag!=null) {
            httpMessage.addHeader(Config.get(Config.KEY_HEADER_IF_NONE_MATCH_KEY), etag);
        }
    }
    
    protected static String extractCookieValue(String input) {
        return input.replaceFirst(".*" + Config.get(Config.KEY_AUTH_COOKIE_NAME) + "=(.*);.*","$1");
    }
    
    public void logout() throws IOException, WrongStatusException, ResponseHandlerException, RequestPreparationException {
        String logoutUrl = base + "/" + Config.get(Config.KEY_LOGOUT_PAGE);
        String referer = base + "/" + Config.get(Config.KEY_MAIN_PAGE);

        sleep();
        Logout logout = new Logout(logoutUrl, referer, cookie);
        logout.doCall();
        sleep();
    }
}
