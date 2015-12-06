package org.warheim.eledger.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import org.slf4j.LoggerFactory;
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
    public static final String KEY_DEBUG = "sys.debug";
    public static final String KEY_OUTPUT = "sys.output";
    public static final String KEY_WAIT_RANDOM_MIN = "web.wait.random.min";
    public static final String KEY_WAIT_RANDOM_MAX = "web.wait.random.max";
    public static final String KEY_WRITE = "sys.write";
    public static final String KEY_DATASTORE_FILENAME = "sys.datastore.filename";
    public static final String KEY_DATASTORE_DIR = "sys.datastore.dir";
    
    //urls
    public static final String KEY_AUTH_PAGE = "web.authPage";
    public static final String KEY_TASK_LIST_PAGE = "web.taskListPage";
    public static final String KEY_TEST_LIST_PAGE = "web.testListPage";
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
    
    private Properties props;
    private static Config instance = new Config();

    private Config() {
        init();
    }
    
    private void init() {
        String resourceName = KEY_CONFIG_FILENAME;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        props = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
            props.load(resourceStream);
        } catch (IOException ex) {
            logger.error("Main config not found", ex);
        }
        String customConfigFileName = Config.getx(props, KEY_CUSTOM_CONFIG_FILENAME);
        if (customConfigFileName!=null && !customConfigFileName.isEmpty()) {
            FileInputStream fis;
            try {
                fis = new FileInputStream(customConfigFileName);
                props.load(fis);
                fis.close();
            } catch (FileNotFoundException ex) {
                logger.error("Custom config not found", ex);
            } catch (IOException ex) {
                logger.error("Custom config inaccesible", ex);
            }
        }
        
    }
    
    public static Config inst() {
        if (instance==null) {
            instance = new Config();
        }
        return instance;
    }
    
    public static final String get(String key) {
        Config inst = inst();
        return inst.props.getProperty(key);
    }
    
    public static final void set(String key, String value) {
        Config inst = inst();
        inst.props.setProperty(key, value);
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
        return getx(KEY_DATASTORE_DIR) +java.io.File.separator + get(KEY_DATASTORE_FILENAME);
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
    
    //TODO: letting use any environmental variable may be dangerous, rethink the strategy
    public static String getx(Properties props, String keyName) {
        String text = props.getProperty(keyName);
        Map<String, String> envMap = System.getenv();       
        for (Entry<String, String> entry : envMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            text = text.replaceAll("\\$" + key + "", value);
        }
        return text;
    }
    
    public static String getx(String keyName) {
        return getx(getProperties(), keyName);
    }
    
    public static final Properties getProperties() {
        Config inst = inst();
        return inst.props;
    }
}
