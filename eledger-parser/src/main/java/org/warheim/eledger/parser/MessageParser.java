package org.warheim.eledger.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.warheim.eledger.parser.model.Message;
import org.warheim.eledger.parser.model.Source;
import org.warheim.eledger.parser.model.SourceType;
import org.warheim.eledger.parser.model.User;
import org.warheim.eledger.parser.model.UserNotifications;
import org.warheim.file.FileTool;

/**
 * Parses the single message page source scraped from web
 * TODO: implement message content parser
 * 
 * this page is called using ajax, so it contains just the message data, no excess html
 * @author andy
 */
public class MessageParser implements SourcePageParser {

    @Override
    public void parse(Source source, UserNotifications un) {
        Document doc = Jsoup.parse(source.getContents());
        Elements msgHeaders = doc.select("tr.message_header td");
        Message msg = un.getMessage(source.getId());
        String title = WebParserTool.noHTML(msgHeaders.get(0));
        String recipients = WebParserTool.noHTML(msgHeaders.get(2));
        Elements msgContents = doc.select("#message_show_content");
        String contents = WebParserTool.noHTML(msgContents.get(0));
        msg.setContent(contents);
        msg.setTitle(title);
        msg.setRecipients(recipients);
        System.out.println(msg);
    }

    public static void main(String... args) throws Exception {
        String data = FileTool.readFile("/home/andy/src/eledger-getter/msg1");
        Source src = new Source(new User("test1"), SourceType.MESSAGE_CONTENT, data, "26638");
        SourcePageParser p = new MessageParser();
        UserNotifications un = new UserNotifications();
        un.putMessage(new Message("26638", null, "someone@domain.com", null, "2015-10-10", null));
        p.parse(src, un);
        System.out.println(un.showAll());

    }
    
}
