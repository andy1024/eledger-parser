package org.warheim.eledger.parser;

import org.warheim.eledger.parser.model.Source;
import org.warheim.file.FileTool;
import java.io.IOException;
import java.util.List;
import org.slf4j.LoggerFactory;
import org.warheim.eledger.parser.model.NotificationsData;
import org.warheim.eledger.parser.model.SourceType;
import org.warheim.eledger.parser.model.UserNotifications;

/**
 * Main parser class. Delegates parsing tasks to another elements of the software
 *
 * @author andy
 */
//TODO: switch to real database, possibly object-oriented
public class Parser {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Parser.class);

    private final List<Source> sources;
    private final String diskStore;
    private final NotificationsData dataFromServer = new NotificationsData();
    private NotificationsData dataFromDisk = new NotificationsData();
    
    protected void setDataFromDisk(NotificationsData notificationsData) {
        this.dataFromDisk = notificationsData;
    }
            
    public Parser(List<Source> sources, String diskStore) {
        this.sources = sources;
        this.diskStore = diskStore;
    }
    
    protected void buildDataFromServer() {
        for (Source source: sources) {
            UserNotifications un = dataFromServer.getNotificationsForUser(source.getUser());
            if (un==null) {
                un = new UserNotifications();
                dataFromServer.putUserNotifications(source.getUser(), un);
            }
            SourcePageParser spp;
            switch (source.getType()) {
                case TASKLIST:
                    spp = new TaskListParser();
                    break;
                case TESTLIST:
                    spp = new TestListParser();
                    break;
                case TOPICLIST:
                    spp = new TopicListParser();
                    break;
                case GRADELIST:
                    spp = new GradeListParser();
                    break;
                case MESSAGES:
                    spp = new MessageListParser();
                    break;
                default:
                    spp = null;
                    
            }
            if (spp!=null) {
                spp.parse(source, un);
            }
            
        }
    }
    
    public void supplementDataFromServer(List<Source> sources) {
        for (Source src: sources) {
            try {
                if (SourceType.MESSAGE_CONTENT.equals(src.getType())) {
                    UserNotifications un = dataFromServer.getNotificationsForUser(src.getUser());
                    SourcePageParser spp = new MessageParser();
                    spp.parse(src, un);
                }
            } catch (Exception e) {
                logger.error("Error while supplementing data from server", e);
                logger.debug("Source data:\n"+src);
                throw e;
            }
        }
    }
    
    protected void buildDataFromDisk() {
        if (diskStore==null||diskStore.isEmpty()) {
            dataFromDisk = new NotificationsData();
            } else {
            try {
                dataFromDisk = NotificationsData.deserializeFromJson(diskStore);
            } catch (IllegalStateException e) {
                logger.error("Error while building data from datastore", e);
                logger.debug("Source data:\n"+diskStore);
                throw e;
            }
        }
    }
    
    public NotificationsData getNewData() {
        buildDataFromServer();
        buildDataFromDisk();
        
        NotificationsData diffMap = NotificationsData.getDataDiff(dataFromServer, dataFromDisk);
        return diffMap;
    }
    
    public void storeUpdatedDiskMap() throws IOException {
        try {
            FileTool.writeFile(Config.get(Config.KEY_DATASTORE_DIR), Config.get(Config.KEY_DATASTORE_FILENAME), dataFromDisk.serializeToJson());
        } catch (IOException ex) {
            logger.error("Error while serializing json", ex);
            throw ex;
        }
    }

}
