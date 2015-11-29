/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.warheim.eledger.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import org.warheim.eledger.parser.model.User;

//TODO: implement reading custom file from user directory

/**
 *
 * @author andy
 */
public class Config {
    private Properties props;
    private static Config instance = null;

    private Config() {
        init();
    }
    
    private void init() {
        String resourceName = "config.properties";
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        props = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
            props.load(resourceStream);
        } catch (IOException ex) {
            ex.printStackTrace();
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
        Config inst = inst();
        String val = get(key);
        return Integer.parseInt(val);
    }

    public static final String getStoreFileName() {
        return getx("sys.datastore.dir") +java.io.File.separator + get("sys.datastore.filename");
    }
    
    public static final List<User> getUsers() {
        List<User> users = new ArrayList<>();
        String authData = get("auth.data");
        String[] authChunks = authData.split(";");
        for (String authChunk: authChunks) {
            String[] els = authChunk.split(",");
            User user = new User(els[0], els[1]);
            users.add(user);
        }
        return users;
    }
    
    //TODO: letting use any environmental variable may be dangerous, rethink the strategy
    public static String getx(String keyName) {
        Config inst = inst();
        String text = inst.props.getProperty(keyName);

        Map<String, String> envMap = System.getenv();       
        for (Entry<String, String> entry : envMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            text = text.replaceAll("\\$" + key + "", value);
        }
        return text;
    }
    
    public static final Properties getProperties() {
        Config inst = inst();
        return inst.props;
    }
}
