package org.warheim.eledger.parser;

import org.warheim.eledger.parser.model.Source;
import org.warheim.file.FileTool;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.warheim.eledger.parser.model.NotificationsData;
import org.warheim.eledger.parser.model.UserNotifications;

/**
 * Main parser class. Delegates parsing tasks to another elements of the software
 *
 * @author andy
 */
public class Parser {
    private final List<Source> sources;
    private final String diskStore;
    private NotificationsData dataFromServer = new NotificationsData();
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
            SourcePageParser spp = null;
            switch (source.getType()) {
                case TASKLIST:
                    spp = new TaskListParser();
                    break;
                case TESTLIST:
                    spp = new TestListParser();
                    break;
                case MESSAGES:
                    spp = new MessageListParser();
                    break;
                    
            }
            if (spp!=null) {
                spp.parse(source, un);
            }
            
        }
    }
    
    protected void buildDataFromDisk() {
        try {
            dataFromDisk = NotificationsData.deserializeFromJson(diskStore);
        } catch (IllegalStateException e) {
            System.err.println(e.getCause());
            throw e;
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
            FileTool.writeFile(Config.getx(Config.KEY_DATASTORE_DIR), Config.get(Config.KEY_DATASTORE_FILENAME), dataFromDisk.serializeToJson());
        } catch (IOException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, "Error while serializing json", ex);
            System.err.println("error while serializing json");
            throw ex;
        }
    }

}
