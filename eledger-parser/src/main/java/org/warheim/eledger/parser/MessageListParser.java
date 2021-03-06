package org.warheim.eledger.parser;

import org.warheim.eledger.parser.model.Source;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.warheim.eledger.parser.model.UserNotifications;

/**
 * Parses the message list page source scraped from web
 * Inserts empty messages into the UserNotifications object
 * Message contents need to be scraped and parsed separately 
 * as they are accessible as separate web resources
  * @author andy
 */
public class MessageListParser implements SourcePageParser {

    protected String stripMessageHeaderId(String id) {
        String retval = id.replace("message_row_", "");
        return retval;
    }
    
    @Override
    public void parse(Source source, UserNotifications un) {
        Document doc = Jsoup.parse(source.getContents());
        Elements msgHeaders = doc.select("tr.messages_hover");
        for (Element msgHeader: msgHeaders) {
            String id = stripMessageHeaderId(msgHeader.id());
            Elements msgAttrs = msgHeader.select("td");
            String title = WebParserTool.noHTML(msgAttrs.get(1));
            String sender = WebParserTool.noHTML(msgAttrs.get(2));
            String date = WebParserTool.noHTML(msgAttrs.get(3));
            un.putMessageWithoutContents(id, title, sender, date);

        }
        
    }
    
}
