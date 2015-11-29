/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.warheim.eledger.parser;

import org.warheim.eledger.parser.model.SourceType;
import org.warheim.eledger.parser.model.Source;
import org.warheim.print.PrintingException;
import org.warheim.print.Printer;
import org.warheim.file.FileTool;
import org.warheim.eledger.web.HttpReqRespHandler;
import org.warheim.print.FormattingException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.Doc;
import org.warheim.app.Application;
import org.warheim.app.EventHandlerException;
import org.warheim.di.ObjectCreationException;
import org.warheim.di.ObjectFactory;
import org.warheim.eledger.parser.model.NotificationsData;
import org.warheim.eledger.parser.model.MockData;
import org.warheim.eledger.parser.model.User;
import org.warheim.net.RequestPreparationException;
import org.warheim.net.ResponseHandlerException;
import org.warheim.net.WrongStatusException;
import org.warheim.print.Formatter;

/**
 *
 * @author andy
 */
public class EntryPoint extends Application {
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
        System.out.println(Config.getStoreFileName());
        this.registerEventHandlers(Config.getProperties(), "app.event.");
        this.fire("app.event.afterConfigRead");
        String debug = Config.get("sys.debug");
        if ("1".equals(debug)) {
            this.fire("app.event.beforeServerDataGet");
            //DEBUG:
            serverResponse.add(
                    new Source(
                        new User("testowy", "***"), 
                        SourceType.TASKLIST, 
                        FileTool.readFile("/home/andy/src/eledger-getter/y1")
                    )
            );
            this.fire("app.event.afterServerDataGet");
            this.fire("app.event.beforeDataStoreRead");
            diskStore = FileTool.readFile("/home/andy/src/eledger-getter/y0");
            this.fire("app.event.afterDataStoreRead");
            Config.set("sys.output.printer", "PDF");
            parser = new Parser(serverResponse, diskStore);
            newData = parser.getNewData();
        } else if ("2".equals(debug)) {
            final NotificationsData mockData = MockData.createTestData(0);
            parser = new Parser(new ArrayList<Source>(), diskStore);//mocked
            parser.setDataFromDisk(mockData);
            newData = mockData;
            parser.buildDataFromServer();
        } else {
            this.fire("app.event.beforeServerDataGet");
            Map<User, HttpReqRespHandler> sessions = new HashMap<>();
            for (User user: Config.getUsers()) {
                try {
                    HttpReqRespHandler h = new HttpReqRespHandler(user);
                    serverResponse.addAll(h.getAllData());
                    sessions.put(user, h);
                } catch (java.io.IOException e) {
                    Logger.getLogger(EntryPoint.class.getName()).log(Level.SEVERE, null, e);
                    System.exit(2);
                }
            }
            
            this.fire("app.event.afterServerDataGet");
            this.fire("app.event.beforeDataStoreRead");
            try {
                diskStore = FileTool.readFile(Config.getStoreFileName());
            } catch (FileNotFoundException fnfe) {
                Logger.getLogger(EntryPoint.class.getName()).log(Level.INFO, "No datastore file, will be created upon exit", fnfe);
                diskStore = "";
            }
            this.fire("app.event.afterDataStoreRead");
            parser = new Parser(serverResponse, diskStore);
            newData = parser.getNewData();
            this.fire("app.event.beforeServerMessageContentsGet");
            for (User user: Config.getUsers()) {
                try {
                    //TODO: implement message contents getting based on diff map
                    HttpReqRespHandler h = sessions.get(user);
                    serverResponse.addAll(h.getAllData());
                    sessions.put(user, h);
                    h.logout();
                } catch (java.io.IOException e) {
                    Logger.getLogger(EntryPoint.class.getName()).log(Level.SEVERE, null, e);
                    System.exit(2);
                }
            }
            this.fire("app.event.afterServerMessageContentsGet");
            
        }
        if (newData==null||newData.isEmpty()) {
            System.out.println("No new data");//System.exit(1);
        } else {
            System.out.println(newData.showAll());
            boolean printingOk = true;
            if ("1".equals(Config.get("sys.print"))) {
                this.fire("app.event.beforeFormatting");
                Formatter formatter = (Formatter)ObjectFactory.createObject(Config.get("sys.output.formatter"));
                formatter.setModel(newData);
                Doc formattedDocument = formatter.getDocument();
                this.fire("app.event.afterFormatting");
                Printer printer = new Printer(
                        Config.get("sys.output.printer"),
                        formattedDocument
                );

                try {
                    this.fire("app.event.beforePrinting");
                    printer.print();
                    this.fire("app.event.afterPrinting");
                } catch (PrintingException ex) {
                    Logger.getLogger(EntryPoint.class.getName()).log(Level.SEVERE, null, ex);
                    printingOk = false;
                }
            }
            if (printingOk&&"1".equals(Config.get("sys.write"))) {
                try {
                    if (parser!=null) {
                        this.fire("app.event.beforeDataStoreWrite");
                        parser.storeUpdatedDiskMap();
                        this.fire("app.event.afterDataStoreWrite");
                    }//only update map if printing went fine
                } catch (IOException ex) {
                    Logger.getLogger(EntryPoint.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        this.fire("app.event.finish");
    }
}
