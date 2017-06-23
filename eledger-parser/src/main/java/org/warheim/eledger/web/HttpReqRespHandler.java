package org.warheim.eledger.web;

import org.warheim.net.RequestPreparationException;
import org.warheim.net.WrongStatusException;
import org.warheim.net.ResponseHandlerException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.slf4j.LoggerFactory;
import org.warheim.di.ObjectCreationException;
import org.warheim.eledger.parser.Config;
import org.warheim.eledger.parser.model.Source;
import org.warheim.eledger.parser.model.SourceType;
import org.warheim.eledger.parser.model.User;
import org.warheim.net.WebExecutionException;

/**
 * Main class for web interaction
 *
 * @author andy
 */
public class HttpReqRespHandler {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(HttpReqRespHandler.class);
    
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
            logger.warn("Error while taking a nap", ex);
        }
    }

    public List<Source> getAllData() throws IOException, WrongStatusException, ResponseHandlerException, RequestPreparationException, ObjectCreationException, WebExecutionException {
        List<Source> retval = new ArrayList<>();
        
        String authPage = base + "/" + Config.get(Config.KEY_AUTH_PAGE);
        String taskListPage = base + "/" + Config.get(Config.KEY_TASK_LIST_PAGE);
        String testListPage = base + "/" + Config.get(Config.KEY_TEST_LIST_PAGE);
        String topicListPage = base + "/" + Config.get(Config.KEY_TOPIC_LIST_PAGE);
        String gradeListPage = base + "/" + Config.get(Config.KEY_GRADE_LIST_PAGE);
        String messagesListPage = base + "/" + Config.get(Config.KEY_MESSAGES_LIST_PAGE);
    
        InitiateConnection init = new InitiateConnection(taskListPage);
        init.doCall();
        etag = init.getEtag();
        cookie = init.getCookie();
        sleep();
        Authorize auth = new Authorize(authPage, taskListPage, base, user.getName(), user.getPass(), cookie, etag);
        auth.doCall();

        String taskSourcePage;
        GetTasksList taskListGetter = new GetTasksList(taskListPage, cookie, etag);
        taskSourcePage = taskListGetter.doCall();
        retval.add(new Source(user, SourceType.TASKLIST, taskSourcePage));
        sleep();
        
        String testSourcePage;
        GetTestsList testListGetter = new GetTestsList(testListPage, cookie, etag);
        testSourcePage = testListGetter.doCall();
        retval.add(new Source(user, SourceType.TESTLIST, testSourcePage));
  
        String topicSourcePage;
        GetTopicsList topicListGetter = new GetTopicsList(topicListPage, cookie, etag);
        topicSourcePage = topicListGetter.doCall();
        retval.add(new Source(user, SourceType.TOPICLIST, topicSourcePage));
 
        String gradeSourcePage;
        GetGradesList gradeListGetter = new GetGradesList(gradeListPage, cookie, etag);
        gradeSourcePage = gradeListGetter.doCall();
        retval.add(new Source(user, SourceType.GRADELIST, gradeSourcePage));
        
        sleep();
        String messagesSourcePage;
        GetMessagesList messageListGetter = new GetMessagesList(messagesListPage, cookie, etag);
        messagesSourcePage = messageListGetter.doCall();
        retval.add(new Source(user, SourceType.MESSAGES, messagesSourcePage));
        
        sleep();
        return retval;
    }
    
    public List<Source> getMessagesContents(Set<String> msgIds) throws IOException, WrongStatusException, ResponseHandlerException, RequestPreparationException, ObjectCreationException, WebExecutionException {
        List<Source> retval = new ArrayList<>();
        String messagePageUrl = base + "/" + Config.get(Config.KEY_MESSAGE_PAGE);
        String messageInboxPageUrl = base + "/" + Config.get(Config.KEY_MESSAGES_LIST_PAGE);
        
        for (String msgId: msgIds) {
            String messagePageResponse;
            GetMessage messageGetter = new GetMessage(messagePageUrl, msgId, cookie, etag, messageInboxPageUrl);
            messagePageResponse = messageGetter.doCall();
            retval.add(new Source(user, SourceType.MESSAGE_CONTENT, messagePageResponse, msgId));
            sleep();
        }
        return retval;
    }
    
    public void logout() throws IOException, WrongStatusException, ResponseHandlerException, RequestPreparationException, ObjectCreationException, WebExecutionException {
        String logoutUrl = base + "/" + Config.get(Config.KEY_LOGOUT_PAGE);
        String referer = base + "/" + Config.get(Config.KEY_MAIN_PAGE);

        sleep();
        Logout logout = new Logout(logoutUrl, referer, cookie);
        logout.doCall();
        sleep();
    }
}
