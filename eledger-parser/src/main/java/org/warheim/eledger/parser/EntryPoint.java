package org.warheim.eledger.parser;

import java.io.File;
import org.warheim.eledger.parser.model.SourceType;
import org.warheim.eledger.parser.model.Source;
import org.warheim.file.FileTool;
import org.warheim.eledger.web.HttpReqRespHandler;
import org.warheim.formatter.FormattingException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.LoggerFactory;
import org.warheim.app.Application;
import org.warheim.app.Event;
import org.warheim.app.EventHandler;
import org.warheim.app.EventHandlerException;
import org.warheim.di.ObjectCreationException;
import org.warheim.di.ObjectFactory;
import org.warheim.eledger.parser.model.NotificationsData;
import org.warheim.eledger.parser.model.MockData;
import org.warheim.eledger.parser.model.User;
import org.warheim.net.RequestPreparationException;
import org.warheim.net.ResponseHandlerException;
import org.warheim.net.WrongStatusException;
import org.warheim.formatter.Formatter;
import org.warheim.outputsink.Output;
import org.warheim.outputsink.OutputException;

/**
 *
 * @author andy
 */
public class EntryPoint extends Application {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(EntryPoint.class);

    public static void main(String... args) throws Exception {
        Application app = new EntryPoint();
        app.run();
    }

    @Override
    public void run() throws IOException, WrongStatusException, ResponseHandlerException, RequestPreparationException, ObjectCreationException, FormattingException, EventHandlerException {
        List<Source> serverResponse = new ArrayList<>();
        String diskStore = "";
        NotificationsData newData = null;
        Parser parser = null;
        logger.info(Config.getStoreFileName());
        this.registerEventHandlers(Config.getProperties(), "app.event.");
        this.fire(Event.APP_EVENT_AFTER_CONFIG_READ);
        String debug = Config.get(Config.KEY_DEBUG);
        if (null != debug) {
            switch (debug) {
                case "1":
                    this.fire(Event.APP_EVENT_BEFORE_SERVER_DATA_GET);
                    //DEBUG:
                    serverResponse.add(
                            new Source(
                                    new User("testowy", "***"),
                                    SourceType.TASKLIST,
                                    FileTool.readFile("/home/andy/src/eledger-getter/y1")
                            )
                    );
                    this.fire(Event.APP_EVENT_AFTER_SERVER_DATA_GET);
                    this.fire(Event.APP_EVENT_BEFORE_DATA_STORE_READ);
                    diskStore = FileTool.readFile("/home/andy/src/eledger-getter/y0");
                    this.fire(Event.APP_EVENT_AFTER_DATA_STORE_READ);
                    Config.set(Config.KEY_PRINTER, "PDF");
                    parser = new Parser(serverResponse, diskStore);
                    newData = parser.getNewData();
                    break;
                case "2":
                    final NotificationsData mockData = MockData.createTestData(0);
                    parser = new Parser(new ArrayList<Source>(), diskStore);//mocked
                    parser.setDataFromDisk(mockData);
                    newData = mockData;
                    parser.buildDataFromServer();
                    break;
                default:
                    this.fire(Event.APP_EVENT_BEFORE_SERVER_DATA_GET);
                    Map<User, HttpReqRespHandler> sessions = new HashMap<>();
                    for (User user : Config.getUsers()) {
                        try {
                            HttpReqRespHandler h = new HttpReqRespHandler(user);
                            serverResponse.addAll(h.getAllData());
                            sessions.put(user, h);
                        } catch (java.io.IOException e) {
                            logger.error("Server request/respons error", e);
                            System.exit(2);
                        }
                    }
                    this.fire(Event.APP_EVENT_AFTER_SERVER_DATA_GET);
                    this.fire(Event.APP_EVENT_BEFORE_DATA_STORE_READ);
                    try {
                        diskStore = FileTool.readFile(Config.getStoreFileName());
                    } catch (FileNotFoundException fnfe) {
                        logger.warn("No datastore file, will be created upon exit", fnfe);
                        diskStore = "";
                    }
                    this.fire(Event.APP_EVENT_AFTER_DATA_STORE_READ);
                    parser = new Parser(serverResponse, diskStore);
                    newData = parser.getNewData();
                    this.fire(Event.APP_EVENT_BEFORE_SERVER_MESSAGE_CONTENTS_GET);
                    List<Source> messageDataServerResponse = new ArrayList<>();
                    for (User user : Config.getUsers()) {
                        try {
                            HttpReqRespHandler h = sessions.get(user);
                            messageDataServerResponse.addAll(h.getMessagesContents(newData.getNotificationsForUser(user).getMessageIDs()));
                            this.fire(Event.APP_EVENT_BEFORE_SINGLE_USER_LOGOUT);
                            h.logout();
                            this.fire(Event.APP_EVENT_AFTER_SINGLE_USER_LOGOUT);
                        } catch (java.io.IOException e) {
                            logger.error("Server request/response error", e);
                            System.exit(2);
                        }
                    }   //add message contents to the list
                    parser.supplementDataFromServer(messageDataServerResponse);
                    this.fire(Event.APP_EVENT_AFTER_SERVER_MESSAGE_CONTENTS_GET);
                    break;
            }
        }
        if (newData == null || newData.isEmpty()) {
            logger.info("No new data");//System.exit(1);
        } else {
            logger.info(newData.showAll());
            boolean outputOk = true;
            if ("1".equals(Config.get(Config.KEY_OUTPUT))) {
                this.fire(Event.APP_EVENT_BEFORE_FORMATTING);
                Formatter formatter = (Formatter) ObjectFactory.createObject(Config.get(Config.KEY_OUTPUT_FORMATTER));
                formatter.setModel(newData);
                File formattedDocumentFile = formatter.getFormattedDocumentFile();
                this.fire(Event.APP_EVENT_AFTER_FORMATTING);
                Output output = (Output) ObjectFactory.createObject(Config.get(Config.KEY_OUTPUT_SINK));
                output.setInputFile(formattedDocumentFile);

                try {
                    this.fire(Event.APP_EVENT_BEFORE_OUTPUT);
                    output.process();
                    this.fire(Event.APP_EVENT_AFTER_OUTPUT);
                } catch (OutputException ex) {
                    logger.error("Error while processing output", ex);
                    outputOk = false;
                }
            }
            if (outputOk && "1".equals(Config.get(Config.KEY_WRITE))) {
                try {
                    if (parser != null) {
                        this.fire(Event.APP_EVENT_BEFORE_DATA_STORE_WRITE);
                        parser.storeUpdatedDiskMap();
                        this.fire(Event.APP_EVENT_AFTER_DATA_STORE_WRITE);
                    }//only update map if output (printing) went fine
                } catch (IOException ex) {
                    logger.error("Error while updating datastore", ex);
                }
            }
        }
        this.fire(Event.APP_EVENT_FINISH);
    }
}
