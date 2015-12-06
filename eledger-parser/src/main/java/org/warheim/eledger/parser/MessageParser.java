package org.warheim.eledger.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;
import org.warheim.eledger.parser.model.Message;
import org.warheim.eledger.parser.model.Source;
import org.warheim.eledger.parser.model.UserNotifications;

/**
 * Parses the single message page source scraped from web
 * 
 * this page is called using ajax, so it contains just the message data, no excess html
 * @author andy
 */
public class MessageParser implements SourcePageParser {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MessageParser.class);

    @Override
    public void parse(Source source, UserNotifications un) {
        Document doc = Jsoup.parse(source.getContents());
        Elements msgHeaders = doc.select("tr.message_header td");
        Message msg = un.getMessage(source.getId());
        if (msg==null) {//should only happen when testing with not properly initialized datastore
            logger.warn(String.format("Message %s not available in UN, skipping", source.getId()));
            return;
        }
        //String title = WebParserTool.noHTML(msgHeaders.get(0));
        String recipients = WebParserTool.noHTML(msgHeaders.get(2));
        if (recipients.length()>35 && recipients.contains(",")) {
            //truncate too long recipients lists
            recipients = Config.get(Config.KEY_MULTIPLE_RECIPIENTS);
        }
        Elements msgContents = doc.select("#message_show_content");
        String contents = WebParserTool.noHTML(msgContents.get(0));
        msg.setContent(contents);
        //msg.setTitle(title);
        msg.setRecipients(recipients);
        logger.debug(msg.toString());
    }

}
