/**
 * Copyright (C) 2009-2012 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.connectors.email;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.ProcessConnector;
import org.ow2.bonita.facade.QueryRuntimeAPI;
import org.ow2.bonita.facade.exception.DocumentNotFoundException;
import org.ow2.bonita.facade.runtime.AttachmentInstance;

/**
 * This connector provides an email sending service.
 * 
 * @author Matthieu Chaffotte
 * @author Yanyan Liu
 */
public class EmailConnector extends ProcessConnector {

  /**
   * The name or the IP address of the SMTP server.
   */
  private String smtpHost = "";

  /**
   * The port number of the SMTP server.
   */
  private int smtpPort = 25;

  /**
   * Indicates whether the SMTP server uses an SSL support.
   */
  private boolean sslSupport = false;

  /**
   * Indicates whether the SMTP server uses a STARTTLS support.
   */
  private boolean starttlsSupport = false;

  /**
   * The user name used for authentication.
   */
  private String userName = "";

  /**
   * The password used for authentication.
   */
  private String password = "";

  /**
   * The sender's email address.
   */
  private String from = "";

  /**
   * The reply address.
   */
  private String replyTo = "";

  /**
   * The "to" recipient(s) email address(es).
   */
  private String to = "";

  /**
   * The "cc" recipient(s) email address(es).
   */
  private String cc = "";

  /**
   * The "bcc" recipient(s) email address(es).
   */
  private String bcc = "";

  /**
   * The subject of the email.
   */
  private String subject = "";

  /**
   * Indicates whether the content of the email is in HTML format.
   */
  private boolean html = false;

  /**
   * The message content of the email.
   */
  private String message = "";

  /**
   * Charset of the message
   */
  private String charset = "utf-8";

  /**
   * The extra header fields of the email.
   */
  private Map<String, String> headers = new HashMap<String, String>();

  /**
   * The files to attach to the email.
   */
  private List<Object> attachments = new ArrayList<Object>();

  /**
   * Embedded images in the HTML message. (Key = alias; Value = image path)
   */
  private Map<String, String> images = new HashMap<String, String>();

  /**
   * Connection timeout value in milliseconds.
   */
  private int timeout = 30000;

  /**
   * The name or the IP address of the SMTP server.
   * 
   * @return the name or the IP address of the SMTP server
   */
  public final String getSmtpHost() {
    return smtpHost;
  }

  /**
   * The port number of the SMTP server.
   * 
   * @return the port number of the SMTP server
   */
  public final int getSmtpPort() {
    return smtpPort;
  }

  /**
   * Tests whether the SMTP server supports SSL.
   * 
   * @return true if the SMTP server supports SSL; false otherwise
   */
  public final boolean isSslSupport() {
    return sslSupport;
  }

  /**
   * Tests whether the SMTP server supports STARTTLS.
   * 
   * @return true if the SMTP server supports STARTTLS; false otherwise
   */
  public final boolean isStarttlsSupport() {
    return starttlsSupport;
  }

  /**
   * The user name used for authentication.
   * 
   * @return the user name used for authentication
   */
  public final String getUserName() {
    return userName;
  }

  /**
   * The password used for authentication.
   * 
   * @return the password used for authentication
   */
  public final String getPassword() {
    return password;
  }

  /**
   * The sender's email address.
   * 
   * @return the sender's email address
   */
  public final String getFrom() {
    return from;
  }

  public final String getReplyTo() {
    return replyTo;
  }

  /**
   * Returns the "to" recipient(s) email address(es).
   * 
   * @return the "to" recipient(s) email address(es)
   */
  public final String getTo() {
    return to;
  }

  /**
   * Returns the "cc" recipient(s) email address(es).
   * 
   * @return the "cc" recipient(s) email address(es)
   */
  public final String getCc() {
    return cc;
  }

  /**
   * Returns the "bcc" recipient(s) email address(es).
   * 
   * @return the "bcc" recipient(s) email address(es)
   */
  public final String getBcc() {
    return bcc;
  }

  /**
   * A brief summary of the contents of the email.
   * 
   * @return subject the subject of the email
   */
  public final String getSubject() {
    return subject;
  }

  /**
   * Checks whether the message is in HTML format.
   * 
   * @return true if the message is in HTML format; false otherwise
   */
  public final boolean isHtml() {
    return html;
  }

  /**
   * The message content of the email.
   * 
   * @return the message content of the email
   */
  public final String getMessage() {
    return message;
  }

  /**
   * The extra headers to put in the email.
   * 
   * @return the extra headers to put in the email
   */
  public final Map<String, String> getHeaders() {
    return headers;
  }

  /**
   * The list of all attachments of the email.
   * 
   * @return the list of all attachments of the email
   */
  public final List<Object> getAttachments() {
    return attachments;
  }

  /**
   * The HashMap containing aliases as Key and image file paths as value to embed in the HTML message.
   * 
   * @return the HashMap containing image file paths and aliases
   */
  public final Map<String, String> getImages() {
    return images;
  }

  /**
   * Sets the name or IP address of the SMTP server.
   * 
   * @param host
   *          The name or IP address of the SMTP server
   */
  public void setSmtpHost(final String host) {
    smtpHost = host;
  }

  /**
   * Sets the port number of the SMTP server.
   * 
   * @param port
   *          The port number of the SMTP server
   */
  public void setSmtpPort(final int port) {
    smtpPort = port;
  }

  /**
   * Sets the port number of the SMTP server.
   * 
   * @param port
   *          The port number of the SMTP server
   */
  public void setSmtpPort(final Long port) {
    if (port == null) {
      setSmtpPort(Integer.MIN_VALUE);
    } else {
      setSmtpPort(port.intValue());
    }
  }

  /**
   * Sets if the SMTP server supports SSL.
   * 
   * @param ssl
   *          true if the SMTP server supports SSL.
   */
  public void setSslSupport(final boolean ssl) {
    sslSupport = ssl;
  }

  /**
   * Sets if the SMTP server supports SSL.
   * 
   * @param ssl
   *          true if the SMTP server supports SSL.
   */
  public void setSslSupport(final Boolean ssl) {
    if (ssl == null) {
      setSslSupport(false);
    } else {
      setSslSupport(ssl.booleanValue());
    }
  }

  /**
   * Sets if the SMTP server supports STARTTLS.
   * 
   * @param starttls
   *          true if the SMTP server supports STRARTTLS.
   */
  public void setStarttlsSupport(final boolean starttls) {
    starttlsSupport = starttls;
  }

  /**
   * Sets if the SMTP server supports STARTTLS.
   * 
   * @param starttls
   *          true if the SMTP server supports STRARTTLS.
   */
  public void setStarttlsSupport(final Boolean starttls) {
    if (starttls == null) {
      setStarttlsSupport(false);
    } else {
      setStarttlsSupport(starttls.booleanValue());
    }
  }

  /**
   * Sets the user name used for authentication.
   * 
   * @param username
   *          the user name used for authentication
   */
  public void setUserName(final String username) {
    userName = username;
  }

  /**
   * Sets the password used for authentication.
   * 
   * @param pwd
   *          the password used for authentication
   */
  public void setPassword(final String pwd) {
    password = pwd;
  }

  /**
   * Sets the sender's email address.
   * 
   * @param from
   *          the sender's email address
   */
  public void setFrom(final String from) {
    this.from = from;
  }

  public void setReplyTo(final String replyTo) {
    this.replyTo = replyTo;
  }

  /**
   * Sets the "to" recipient(s) email address(es).
   * 
   * @param to
   *          the "to" recipient(s) email address(es) separated by commas
   */
  public void setTo(final String to) {
    this.to = to;
  }

  /**
   * Sets the "cc" recipient(s) email address(es).
   * 
   * @param cc
   *          the "cc" recipient(s) email address(es) separated by commas
   */
  public void setCc(final String cc) {
    this.cc = cc;
  }

  /**
   * Sets the "bcc" recipient(s) email address(es).
   * 
   * @param bcc
   *          the "bcc" recipient(s) email address(es) separated by commas
   */
  public void setBcc(final String bcc) {
    this.bcc = bcc;
  }

  /**
   * Sets the subject of the email. It is a brief summary of the contents of the email.
   * 
   * @param subject
   *          the subject of the email
   */
  public void setSubject(final String subject) {
    this.subject = subject;
  }

  /**
   * Sets if the message will be displayed in an HTML format.
   * 
   * @param html
   *          true if the message is in HTML format
   */
  public void setHtml(final boolean html) {
    this.html = html;
  }

  /**
   * Sets if the message will be displayed in an HTML format.
   * 
   * @param html
   *          true if the message is in HTML format
   */
  public void setHtml(final Boolean html) {
    if (html == null) {
      setHtml(false);
    } else {
      setHtml(html.booleanValue());
    }
  }

  /**
   * Sets the message content of the email.
   * 
   * @param message
   *          the message content of the email.
   */
  public void setMessage(final String message) {
    if (message == null) {
      this.message = "";
    } else {
      this.message = message;
    }
  }

  /**
   * Sets the charset of the message
   * 
   * @param charset
   *          the charset of the message
   */
  public void setCharset(final String charset) {
    if (charset == null || "".equals(charset.trim())) {
      this.charset = "utf-8";
    } else {
      this.charset = charset;
    }
  }

  /**
   * Sets extra headers to put in the email.
   * 
   * @param headers
   *          the extra headers
   */
  public void setHeaders(final Map<String, String> headers) {
    if (headers == null) {
      this.headers = new HashMap<String, String>();
    } else {
      this.headers = headers;
    }
  }

  /**
   * Sets extra headers from Bonita Studio format
   * 
   * @param headers
   */
  public void setHeaders(final List<List<Object>> headers) {
    try {
      setHeaders(bonitaListToMap(headers, String.class, String.class));
    } catch (final IllegalArgumentException e) {
      throw new IllegalArgumentException("Unable to set the email headers", e);
    }
  }

  /**
   * Sets files to be attached with the email.
   * 
   * @param attachments
   *          file paths of each attachment
   */
  public void setAttachments(final List<Object> attachments) {
    if (attachments == null) {
      this.attachments = new ArrayList<Object>();
    } else {
      this.attachments = attachments;
    }
  }

  /**
   * Sets image file paths and aliases to be embedded in the HTML message.
   * 
   * @param images
   *          HashMap containing image file paths and aliases
   */
  public void setImages(final Map<String, String> images) {
    if (images == null) {
      this.images = new HashMap<String, String>();
    } else {
      this.images = images;
    }
  }

  public int getTimeout() {
    return timeout;
  }

  public void setTimeout(final Long timeout) {
    if (timeout == null) {
      setTimeout(30000);
    } else {
      setTimeout(timeout.intValue());
    }
  }

  public void setTimeout(final int timeout) {
    this.timeout = timeout;
  }

  @Override
  protected void executeConnector() throws Exception {
    final Session session = getSession();
    final Message email = getEmail(session);
    Transport.send(email);
  }

  @Override
  protected List<ConnectorError> validateValues() {
    final List<ConnectorError> errors = new ArrayList<ConnectorError>();
    ConnectorError error = null;

    if (getSmtpPort() < 0) {
      errors.add(new ConnectorError("smtpPort", new IllegalArgumentException("cannot be less than 0!")));
    } else if (getSmtpPort() > 65535) {
      errors.add(new ConnectorError("smtpPort", new IllegalArgumentException("cannot be greater than 65535!")));
    }

    if (getFrom() != null && getFrom().length() > 0) {
      error = checkAddress(getFrom());
      if (error != null) {
        errors.add(error);
      }
    }
    if (getTo().length() > 0) {
      error = checkAddresses("to", getTo());
      if (error != null) {
        errors.add(error);
      }
    }
    if (getCc() != null && getCc().length() > 0) {
      error = checkAddresses("cc", getCc());
      if (error != null) {
        errors.add(error);
      }
    }
    if (getBcc() != null && getBcc().length() > 0) {
      error = checkAddresses("bcc", getBcc());
      if (error != null) {
        errors.add(error);
      }
    }
    if (getTimeout() < 0) {
      errors.add(new ConnectorError("timeout", new IllegalArgumentException("cannot be less than 0!")));
    }
    return errors;
  }

  /**
   * Checks if the address is well-formed.
   * 
   * @param address
   *          the address to be checked
   * @return a <code>ConnectorError</code> if address is not well-formed; null otherwise
   */
  private ConnectorError checkAddress(final String address) {
    try {
      new InternetAddress(address);
    } catch (final AddressException e) {
      return new ConnectorError("from", e);
    }
    return null;
  }

  /**
   * Checks if addresses are well-formed.
   * 
   * @param fieldName
   *          the field name
   * @param addresses
   *          the addresses to be checked
   * @return a <code>ConnectorError</code> if addresses are not well-formed; null otherwise
   */
  private ConnectorError checkAddresses(final String fieldName, final String addresses) {
    try {
      InternetAddress.parse(addresses);
    } catch (final AddressException e) {
      return new ConnectorError(fieldName, e);
    }
    return null;
  }

  /**
   * Returns an unshared email session from the SMTP server's properties.
   * 
   * @return an unshared email session from the SMTP server's properties
   */
  private Session getSession() {
    final Properties properties = new Properties();
    properties.put("mail.smtp.host", getSmtpHost());
    properties.put("mail.smtp.port", String.valueOf(getSmtpPort()));
    // Using STARTTLS
    if (isStarttlsSupport()) {
      properties.put("mail.smtp.starttls.enable", "true");
    }
    // Using SSL
    if (isSslSupport()) {
      properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
      properties.put("mail.smtp.socketFactory.fallback", "false");
      properties.put("mail.smtp.socketFactory.port", String.valueOf(getSmtpPort()));
    }
    properties.put("mail.smtp.timeout", getTimeout());

    Session session = null;
    if (getUserName() != null && !getUserName().isEmpty() && getPassword() != null) {
      properties.put("mail.smtp.auth", "true");
      final SMTPAuthenticator authenticator = new SMTPAuthenticator(getUserName(), getPassword());
      session = Session.getInstance(properties, authenticator);
    } else {
      session = Session.getInstance(properties, null);
    }
    return session;
  }

  /**
   * Get a MimeMessage from email properties.
   * 
   * @param emailSession
   *          the email session
   * @return a MimeMessage from email properties
   * @throws Exception
   *           if an exception occurs
   */
  private MimeMessage getEmail(final Session emailSession) throws Exception {
    final MimeMessage mimeMessage = new MimeMessage(emailSession);
    if (getFrom() != null && getFrom().length() != 0) {
      mimeMessage.setFrom(new InternetAddress(getFrom()));
    } else {
      mimeMessage.setFrom();
    }
    if (getReplyTo() != null && getReplyTo().length() != 0) {
      mimeMessage.setReplyTo(InternetAddress.parse(getReplyTo(), false));
    }
    mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(getTo(), false));
    if (getCc() != null) {
      mimeMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(getCc(), false));
    }
    if (getBcc() != null) {
      mimeMessage.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(getBcc(), false));
    }
    mimeMessage.setSubject(getSubject(), charset);
    // Headers
    if (getHeaders() != null) {
      for (final Map.Entry<String, String> h : getHeaders().entrySet()) {
        if (h.getKey() != null && h.getValue() != null) {
          if (!h.getKey().equals("Content-ID")) {
            mimeMessage.setHeader(h.getKey(), h.getValue());
          }
        }
      }
    }
    if (getImages() == null || getImages().isEmpty()) {
      // the simplest message
      if (getAttachments() == null || getAttachments().isEmpty()) {
        if (isHtml()) {
          mimeMessage.setText(getMessage(), charset, "html");
        } else {
          mimeMessage.setText(getMessage(), charset);
        }
      } else {
        // simple message with attachments
        final Multipart multipart = getMultipart();
        mimeMessage.setContent(multipart);
      }
    } else {
      final Multipart multipart = getMultipart();
      mimeMessage.setContent(multipart);
    }
    mimeMessage.setSentDate(new Date());
    return mimeMessage;
  }

  /**
   * Get the <code>Multipart</code> of the email.
   * 
   * @return the <code>Multipart</code> of the email.
   * @throws MessagingException
   *           if a MessageException occurs
   * @throws IOException
   *           if an IOException occurs
   * @throws DocumentNotFoundException
   */
  private Multipart getMultipart() throws MessagingException, IOException, DocumentNotFoundException {
    final Multipart multipart = new MimeMultipart();
    MimeBodyPart messageBodyPart = new MimeBodyPart();
    if (isHtml()) {
      messageBodyPart.setText(getMessage(), charset, "html");
    } else {
      messageBodyPart.setText(getMessage(), charset);
    }
    multipart.addBodyPart(messageBodyPart);
    // Part two is embedded images with HTML message
    if (getImages() != null) {
      for (final Map.Entry<String, String> image : getImages().entrySet()) {
        final String alias = image.getKey();
        final String imagePath = image.getValue();
        if (alias != null && imagePath != null) {
          final File img = new File(imagePath);
          if (!img.isDirectory() && img.exists()) {
            messageBodyPart = new MimeBodyPart();
            final DataSource fds = new FileDataSource(img);
            messageBodyPart.setDataHandler(new DataHandler(fds));
            if (isHtml()) {
              messageBodyPart.setHeader("Content-ID", alias);
            }
            multipart.addBodyPart(messageBodyPart);
          }
        }
      }
    }
    // Part three is attachments
    for (final Object attachment : getAttachments()) {
      DataSource source = null;
      if (attachment instanceof AttachmentInstance) {
        final AttachmentInstance attachmentInstance = (AttachmentInstance) attachment;
        final MimeBodyPart attachmentPart = getAttachmentPart(attachmentInstance);
        if(attachmentPart != null){
        	multipart.addBodyPart(attachmentPart);
        }
      } else if (attachment instanceof String) {
        final String filePath = (String) attachment;
        final File file = new File(filePath);
        if (!file.isDirectory() && file.exists()) {
          source = new FileDataSource(file);
          final MimeBodyPart attachmentPart = getAttachmentPart(source, file.getName());
          multipart.addBodyPart(attachmentPart);
        }
      } else if (attachment instanceof List) {
        final List<AttachmentInstance> attachmentInstances = (List<AttachmentInstance>) attachment;
        for (final AttachmentInstance attachmentInstance : attachmentInstances) {
          final MimeBodyPart attachmentPart = getAttachmentPart(attachmentInstance);
          if(attachmentPart != null){
        	  multipart.addBodyPart(attachmentPart);
          }
        }
      }
    }
    return multipart;
  }

  private MimeBodyPart getAttachmentPart(final AttachmentInstance attachmentInstance) throws DocumentNotFoundException,
      MessagingException, UnsupportedEncodingException {
    final QueryRuntimeAPI queryRuntimeAPI = getApiAccessor().getQueryRuntimeAPI();
    final byte[] content = queryRuntimeAPI.getDocumentContent(attachmentInstance.getUUID());
    if(content != null){
    	final String mimeType = attachmentInstance.getMetaData().get("content-type");
    	final DataSource source = new ByteArrayDataSource(content, mimeType);
    	return getAttachmentPart(source, attachmentInstance.getFileName());
    } else {
    	return null;
    }
  }

  private MimeBodyPart getAttachmentPart(final DataSource source, final String fileName) throws MessagingException,
      UnsupportedEncodingException {
    final MimeBodyPart attachmentBodyPart = new MimeBodyPart();
    final DataHandler dataHandler = new DataHandler(source);
    attachmentBodyPart.setDataHandler(dataHandler);
    attachmentBodyPart.setFileName(MimeUtility.encodeText(fileName, charset, "B"));
    return attachmentBodyPart;
  }

}
