package org.warheim.outputsink.email;

import com.sun.mail.smtp.SMTPTransport;
import java.io.File;
import java.io.IOException;
import java.security.Security;
import java.util.*;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

import javax.mail.*;
import javax.mail.internet.*;
import org.slf4j.LoggerFactory;
import org.warheim.file.FileTool;
import org.warheim.outputsink.Output;
import org.warheim.outputsink.OutputException;

public class MailSender implements Output {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MailSender.class);

    private String host = "smtp.gmail.com";
    private String domain = "gmail.com";

    private String port = "465";
    private String user;
    private String pass;
    private String recipient;
    private String title;
    private String asAttachment="true";

    protected String outputDeviceID;
    protected File inputFile;

    protected void send(String contents) throws AddressException, MessagingException {
        send(user, pass, recipient, title, contents, null, null);
    }

    protected void send(String message, String file, String fileNameToBeShown) throws AddressException, MessagingException {
        send(user, pass, recipient, title, message, file, fileNameToBeShown);
    }

    protected void addAttachment(Message msg, String file, String fileNameToBeShown) throws MessagingException {
        MimeBodyPart messageBodyPart;

        Multipart multipart = new MimeMultipart();

        messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(file);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(fileNameToBeShown);
        multipart.addBodyPart(messageBodyPart);

        msg.setContent(multipart);
    }

    /**
     * Send email using GMail SMTP server.
     *
     * @param user GMail username
     * @param pass GMail password
     * @param recipient TO recipient
     * @param title title of the message
     * @param contents message to be sent
     * @param file file to attach, no attachments if null
     * @param fileNameToBeShown how the attached file is supposed to named inside email
     * @throws AddressException if the email address parse failed
     * @throws MessagingException if the connection is dead or not in the connected state or if the message is not a MimeMessage
     */
    protected void send(final String user, final String pass, String recipient, String title, String contents, String file, String fileNameToBeShown) throws AddressException, MessagingException {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        // Get a Properties object
        Properties props = System.getProperties();
        props.setProperty("mail.smtps.host", host);
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", port);
        props.setProperty("mail.smtp.socketFactory.port", port);
        props.setProperty("mail.smtps.auth", "true");

        props.put("mail.smtps.quitwait", "false");

        Session session = Session.getInstance(props, null);

        // -- Create a new message --
        final Message msg = new MimeMessage(session);

        // -- Set the FROM and TO fields --
        msg.setFrom(new InternetAddress(user + "@" + domain));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient, false));

        msg.setSubject(title);
        msg.setText(contents);
        msg.setSentDate(new Date());

        if (file!=null) {
            addAttachment(msg, file, fileNameToBeShown);
        }
        
        SMTPTransport t = (SMTPTransport)session.getTransport("smtps");

        t.connect(host, user, pass);
        t.sendMessage(msg, msg.getAllRecipients());      
        t.close();
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setOutputDeviceID(String outputDeviceID) {
        this.outputDeviceID = outputDeviceID;
    }

    @Override
    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    public String getAsAttachment() {
        return asAttachment;
    }

    public void setAsAttachment(String asAttachment) {
        this.asAttachment = asAttachment;
    }

    @Override
    public boolean process() throws OutputException {
        try {
            if ("true".equals(asAttachment)) {
                send(title, inputFile.getAbsolutePath(), title+"."+FileTool.getExtension(inputFile));
            } else {
                String contents = FileTool.readFile(inputFile);
                send(contents);
            }
            logger.info("Email sent to " + recipient);
        } catch (MessagingException ex) {
            logger.error("Error while sending mail", ex);
            throw new OutputException(ex);
        } catch (IOException ex) {
            logger.error("Can't read file " + inputFile, ex);
            throw new OutputException(ex);
        }
        return true;
    }

}