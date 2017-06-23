package org.warheim.eledger.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import org.slf4j.LoggerFactory;
import org.warheim.di.metainstruction.MetaInstructionException;
import org.warheim.di.metainstruction.MetaInstructionProcessor;
import org.warheim.eledger.parser.model.User;

/**
 * Main configuration class
 * Supports external properties file with custom parameter values
 *
 * @author andy
 */
public class Config {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Config.class);

    //base configuration
    public static final String KEY_CUSTOM_CONFIG_FILENAME = "config.custom.filename";
    public static final String KEY_CONFIG_FILENAME = "config.properties";
    public static final String KEY_PRINTER = "sys.output.printer";
    public static final String KEY_OUTPUT_SINK = "sys.output.sink";
    public static final String KEY_OUTPUT_FORMATTER = "sys.output.formatter";
    public static final String KEY_OUTPUT_PREPROCESSOR = "sys.output.preprocessor";
    public static final String KEY_DEBUG = "sys.debug";
    public static final String KEY_OUTPUT = "sys.output";

    public static final String KEY_OUTPUT_LIMIT_MESSAGES = "sys.output.limit.messages";
    public static final String KEY_OUTPUT_LIMIT_NOTIFICATIONS_TOTAL = "sys.output.limit.notifications.total";
    
    public static final String KEY_WAIT_RANDOM_MIN = "web.wait.random.min";
    public static final String KEY_WAIT_RANDOM_MAX = "web.wait.random.max";
    public static final String KEY_WRITE = "sys.write";
    public static final String KEY_DATASTORE_FILENAME = "sys.datastore.filename";
    public static final String KEY_DATASTORE_DIR = "sys.datastore.dir";
    
    //urls
    public static final String KEY_AUTH_PAGE = "web.authPage";
    public static final String KEY_TASK_LIST_PAGE = "web.taskListPage";
    public static final String KEY_TEST_LIST_PAGE = "web.testListPage";
    public static final String KEY_TOPIC_LIST_PAGE = "web.topicListPage";
    public static final String KEY_GRADE_LIST_PAGE = "web.gradeListPage";
    public static final String KEY_BASE_URL = "web.baseUrl";
    public static final String KEY_MESSAGES_LIST_PAGE = "web.messagesListPage";
    public static final String KEY_MESSAGE_PAGE = "web.messagePage";
    public static final String KEY_LOGOUT_PAGE = "web.logoutPage";
    public static final String KEY_MAIN_PAGE = "web.mainPage";
    
    //request headers
    public static final String KEY_HEADER_ACCEPT_ENCODING_KEY = "header.acceptEncoding.key";
    public static final String KEY_HEADER_ACCEPT_ENCODING_VALUE = "header.acceptEncoding.value";
    public static final String KEY_HEADER_USER_AGENT_KEY = "header.userAgent.key";
    public static final String KEY_HEADER_USER_AGENT_VALUE = "header.userAgent.value";
    public static final String KEY_HEADER_ACCEPT_VALUE = "header.accept.value";
    public static final String KEY_HEADER_ACCEPT_LANGUAGE_KEY = "header.acceptLanguage.key";
    public static final String KEY_HEADER_ACCEPT_LANGUAGE_VALUE = "header.acceptLanguage.value";
    public static final String KEY_HEADER_UPGRADE_INSECURE_REQUESTS_VALUE = "header.upgradeInsecureRequests.value";
    public static final String KEY_HEADER_ACCEPT_KEY = "header.accept.key";
    public static final String KEY_HEADER_UPGRADE_INSECURE_REQUESTS_KEY = "header.upgradeInsecureRequests.key";
    public static final String KEY_HEADER_CONNECTION_KEY = "header.connection.key";
    public static final String KEY_HEADER_CONNECTION_VALUE = "header.connection.value";
    public static final String KEY_HEADER_CACHE_CONTROL_KEY = "header.cacheControl.key";
    public static final String KEY_HEADER_CACHE_CONTROL_VALUE = "header.cacheControl.value";
    public static final String KEY_HEADER_IF_NONE_MATCH_KEY = "header.ifNoneMatch.key";
    public static final String KEY_HEADER_CONTENT_TYPE_KEY = "header.contentType.key";
    public static final String KEY_HEADER_CONTENT_TYPE_VALUE = "header.contentType.value";
    public static final String KEY_HEADER_REFERER_KEY = "header.referer.key";
    public static final String KEY_HEADER_AJAX_REQUESTED_WITH_KEY = "header.ajax.requestedWith.key";
    public static final String KEY_HEADER_AJAX_REQUESTED_WITH_VALUE = "header.ajax.requestedWith.value";
    
    //web interaction
    public static final String KEY_AUTH_COOKIE_NAME = "web.authCookieName";
    public static final String KEY_AUTH_DATA = "auth.data";

    //additional config
    public static final String KEY_MULTIPLE_RECIPIENTS = "msg.multipleRecipients";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    
    private static class Inner {
        private static final Properties props = new Properties();
        
        static {
            String resourceName = KEY_CONFIG_FILENAME;
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            try (InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
                props.load(resourceStream);
                Enumeration e = props.propertyNames();
                while (e.hasMoreElements()) {
                    String key = (String) e.nextElement();
                    String value = props.getProperty(key);
                    String miValue = MetaInstructionProcessor.runMetaInstructionHandlers(value);
                    props.setProperty(key, miValue);
                }
            } catch (IOException ex) {
                logger.error("Main config not found", ex);
            } catch (MetaInstructionException ex) {
                logger.error("MI error", ex);
            }
            String customConfigFileName = props.getProperty(KEY_CUSTOM_CONFIG_FILENAME);
            if (customConfigFileName!=null && !customConfigFileName.isEmpty()) {
                FileInputStream fis;
                try {
                    fis = new FileInputStream(customConfigFileName);
                    props.load(fis);
                    Enumeration e = props.propertyNames();
                    while (e.hasMoreElements()) {
                        String key = (String) e.nextElement();
                        String value = props.getProperty(key);
                        String miValue = MetaInstructionProcessor.runMetaInstructionHandlers(value);
                        props.setProperty(key, miValue);
                    }
                    fis.close();
                } catch (FileNotFoundException ex) {
                    logger.error("Custom config not found", ex);
                } catch (IOException ex) {
                    logger.error("Custom config inaccesible", ex);
                } catch (MetaInstructionException ex) {
                    logger.error("MI error", ex);
                }
            }
        }
        private static Properties initialize() {
            return props;
        }
    }

    //this is not a singleton, new is never called
    private Config() {
    }
    
    
    public static final String get(String key) {
        return Inner.initialize().getProperty(key);
    }
    
    public static final void set(String key, String value) {
        Inner.initialize().setProperty(key, value);
    }

    public static final Integer getInt(String key) {
        String val = get(key);
        if (val==null) {
            return null;
        } else {
            return Integer.parseInt(val);
        }
    }

    public static final String getStoreFileName() {
        return get(KEY_DATASTORE_DIR) +java.io.File.separator + get(KEY_DATASTORE_FILENAME);
    }
    
    public static final List<User> getUsers() {
        List<User> users = new ArrayList<>();
        String authData = get(KEY_AUTH_DATA);
        String[] authChunks = authData.split(";");
        for (String authChunk: authChunks) {
            String[] els = authChunk.split(",");
            User user = new User(els[0], els[1]);
            if (els.length>2) {
                user.setFullname(els[2]);
            }
            users.add(user);
        }
        return users;
    }
    
    public static final Properties getProperties() {
        return Inner.initialize();
    }
    
    public static void main(String... args) throws Exception {
        System.out.println(Config.get(Config.KEY_DATASTORE_DIR));
    }
}
