/**
 * Copyright (C) 2006  Bull S. A. S.
 * Bull, Rue Jean Jaures, B.P.68, 78340, Les Clayes-sous-Bois
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation
 * version 2.1 of the License.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA  02110-1301, USA.
 **/
package org.bonitasoft.connectors.email;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.MimetypesFileTypeMap;
import javax.mail.Multipart;
import javax.mail.internet.MimeMessage;

import junit.framework.TestCase;

import org.ow2.bonita.connector.core.Connector;
import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.facade.runtime.impl.AttachmentInstanceImpl;
import org.ow2.bonita.util.BonitaException;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

public class EmailConnectorTest extends TestCase {

  private static final String MULTIPART_MIXED = "multipart/mixed";
  private static final String TEXT_PLAIN = "text/plain";
  private static final String TEXT_HTML = "text/html";
  private static final String SMTP_HOST = "localhost";
  private static int smtpPort = 0;
  private static final String ADDRESSJOHN = "johndoe@bonita.org";
  private static final String ADDRESSPATTY = "patty.johnson@gmal.com";
  private static final String ADDRESSMARK = "mark.hunt@wahoo.nz";
  private static final String SUBJECT = "Testing EmailConnector";
  private static final String CYRILLIC_SUBJECT = "\u0416 \u0414 \u0431";
  private static final String PLAINMESSAGE = "Plain Message";
  private static final String HTMLMESSAGE = "<b><i>Html<i/> Message</b>";
  private static final String CYRILLIC_MESSAGE = "\u0416 \u0414 \u0431";
  private static final String XML_FILE = "save.xml";
  private static final String PNG_IMAGE = "bonitasoft-logo.png";

  private Wiser server;
  private EmailConnector email;

  protected static final Logger LOG = Logger.getLogger(EmailConnectorTest.class.getName());

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    if (EmailConnectorTest.LOG.isLoggable(Level.WARNING)) {
      EmailConnectorTest.LOG.warning("======== Starting test: " + this.getClass().getName() + "." + getName()
          + "() ==========");
    }
  }

  @Override
  protected void tearDown() throws Exception {
    if (EmailConnectorTest.LOG.isLoggable(Level.WARNING)) {
      EmailConnectorTest.LOG.warning("======== Ending test: " + getName() + " ==========");
    }
    super.tearDown();
  }

  public void testValidateConnector() throws BonitaException {
    final List<ConnectorError> errors = Connector.validateConnector(EmailConnector.class);
    for (final ConnectorError error : errors) {
      System.out.println(error.getField() + " " + error.getError());
    }
    assertTrue(errors.isEmpty());
  }

  public void testValidateValues() throws BonitaException {
    email = getBasicSettings();
    assertTrue(email.validate().isEmpty());

    email.setFrom(ADDRESSMARK);
    assertTrue(email.validate().isEmpty());
    email.setFrom(ADDRESSMARK + ", " + ADDRESSPATTY);
    assertFalse(email.validate().isEmpty());
    email.setFrom("bo nita");
    assertFalse(email.validate().isEmpty());
    email.setFrom("bonita@");
    assertFalse(email.validate().isEmpty());

    email = getBasicSettings();
    email.setTo(ADDRESSJOHN + ", " + ADDRESSPATTY);
    assertTrue(email.validate().isEmpty());
    email.setTo("test@");
    assertFalse(email.validate().isEmpty());
    email.setTo("@bonita");
    assertFalse(email.validate().isEmpty());
    email.setTo("@bonita.org");
    assertFalse(email.validate().isEmpty());
    email.setTo("test@bonita ; test@bonita");
    assertFalse(email.validate().isEmpty());

    email = getBasicSettings();
    email.setCc(ADDRESSJOHN + ", " + ADDRESSPATTY);
    assertTrue(email.validate().isEmpty());
    email.setCc("test@");
    assertFalse(email.validate().isEmpty());
    email.setCc("@bonita");
    assertFalse(email.validate().isEmpty());
    email.setCc("@bonita.org");
    assertFalse(email.validate().isEmpty());
    email.setCc("test@bonita ; test@bonita");
    assertFalse(email.validate().isEmpty());

    email = getBasicSettings();
    email.setBcc(ADDRESSJOHN + ", " + ADDRESSPATTY);
    assertTrue(email.validate().isEmpty());
    email.setBcc("test@");
    assertFalse(email.validate().isEmpty());
    email.setBcc("@bonita");
    assertFalse(email.validate().isEmpty());
    email.setBcc("@bonita.org");
    assertFalse(email.validate().isEmpty());
    email.setBcc("test@bonita ; test@bonita");
    assertFalse(email.validate().isEmpty());
  }

  public void testSetNullAttachments() throws BonitaException {
    email = getBasicSettings();
    final List<Object> attachments = null;
    email.setAttachments(attachments);
    assertTrue(email.validate().isEmpty());
    assertTrue(email.getAttachments().isEmpty());
    email = null;
  }
  
  public void testEmptyListAttachments() throws BonitaException {
	    email = getBasicSettings();
	    final List<Object> attachments = new ArrayList<Object>();
	    email.setAttachments(attachments);
	    assertTrue(email.validate().isEmpty());
	    assertTrue(email.getAttachments().isEmpty());
	    email = null;
	  }

  public void testSetAttachments() {
    email = getBasicSettings();
    final List<Object> attachments = new ArrayList<Object>();
    attachments.add("../test/test0.xml");
    attachments.add("../test/test1.xml");
    attachments.add("../test/test2.xml");
    email.setAttachments(attachments);
    assertNotNull(email.getAttachments());
    assertEquals(3, email.getAttachments().size());
    email = null;
  }

  public void testSetTwiceAttachments() {
    email = getBasicSettings();
    final List<Object> attachments = new ArrayList<Object>();
    attachments.add("../test/test0.xml");
    attachments.add("../test/test1.xml");
    attachments.add("../test/test2.xml");
    email.setAttachments(attachments);
    assertNotNull(email.getAttachments());
    assertEquals(3, email.getAttachments().size());
    attachments.clear();
    email.setAttachments(attachments);
    assertNotNull(email.getAttachments());
    assertTrue(email.getAttachments().isEmpty());
  }

  public void testSetBcc() throws BonitaException {
    email = getBasicSettings();
    email.setBcc(null);
    assertTrue(email.validate().isEmpty());
    email = null;
  }

  public void testSetCc() throws BonitaException {
    email = getBasicSettings();
    email.setCc(null);
    assertTrue(email.validate().isEmpty());
    email = null;
  }

  public void testSetFrom() throws BonitaException {
    email = getBasicSettings();
    email.setFrom(null);
    assertEquals(1, email.validate().size());
    email = null;
  }

  public void testSetNullHeaders() throws BonitaException {
    email = getBasicSettings();
    final Map<String, String> map = null;
    email.setHeaders(map);
    assertTrue(email.validate().isEmpty());
    assertTrue(email.getHeaders().isEmpty());
    email = null;
  }

  public void testSetHeadersTwice() throws BonitaException {
    email = getBasicSettings();
    final Map<String, String> firstSet = new HashMap<String, String>();
    firstSet.put("X-Mailer", "Bonita");
    firstSet.put("X-Sender", "Test");
    firstSet.put("WhatIwant", "WhatIwant");
    email.setHeaders(firstSet);
    assertTrue(email.validate().isEmpty());
    assertEquals(firstSet, email.getHeaders());
    final Map<String, String> secondSet = new HashMap<String, String>();
    secondSet.put("A-Mailer", "Bonita");
    secondSet.put("A-Sender", "Test");
    email.setHeaders(secondSet);
    assertTrue(email.validate().isEmpty());
    assertEquals(secondSet, email.getHeaders());
  }

  public void testSetNullImages() throws BonitaException {
    email = getBasicSettings();
    final Map<String, String> images = null;
    email.setImages(images);
    assertTrue(email.validate().isEmpty());
    assertTrue(email.getImages().isEmpty());
    email = null;
  }

  public void testSetImagesWithAnEmptyString() throws BonitaException {
    email = getBasicSettings();
    final Map<String, String> images = new HashMap<String, String>();
    email.setImages(images);
    assertTrue(email.validate().isEmpty());
    assertTrue(email.getImages().isEmpty());
    email = null;
  }

  public void testSetImagesTwice() throws BonitaException {
    email = getBasicSettings();
    final Map<String, String> firstSet = new HashMap<String, String>();
    firstSet.put("path_one", "firstAlias");
    firstSet.put("path_two", "secondAlias");
    firstSet.put("path_three", "thirdAlias");
    email.setImages(firstSet);
    assertTrue(email.validate().isEmpty());
    assertEquals(firstSet, email.getImages());
    final Map<String, String> secondSet = new HashMap<String, String>();
    secondSet.put("path1", "Alias A");
    secondSet.put("path2", "Alias B");
    email.setImages(secondSet);
    assertTrue(email.validate().isEmpty());
    assertEquals(secondSet, email.getImages());
  }

  public void testSetMessage() throws BonitaException {
    email = getBasicSettings();
    email.setMessage(null);
    assertTrue(email.validate().isEmpty());
    assertEquals("", email.getMessage());
    email = null;
  }

  public void testSetPassword() throws BonitaException {
    email = getBasicSettings();
    email.setUserName(null);
    email.setPassword(null);
    assertTrue(email.validate().isEmpty());
    assertFalse(email.isSslSupport());
    email = null;

    email = getBasicSettings();
    email.setUserName("");
    email.setPassword(null);
    final List<ConnectorError> validate = email.validate();
    assertEquals(1, validate.size());
    final ConnectorError error = validate.get(0);
    assertEquals("password", error.getField());
    assertEquals("This field is required so it must be set.", error.getError().getMessage());
  }

  public void testSetSmtpHost() throws BonitaException {
    email = getBasicSettings();
    email.setSmtpHost(null);
    final List<ConnectorError> validate = email.validate();
    assertEquals(1, validate.size());
    final ConnectorError error = validate.get(0);
    assertEquals("smtpHost", error.getField());
    assertEquals("This field is required so it must be set.", error.getError().getMessage());
    email = null;
  }

  public void testSetNullSmtpPort() throws BonitaException {
    email = getBasicSettings();
    email.setSmtpPort(null);
    final List<ConnectorError> validate = email.validate();
    assertEquals(1, validate.size());
    final ConnectorError error = validate.get(0);
    assertEquals("smtpPort", error.getField());
    assertEquals("cannot be less than 0!", error.getError().getMessage());
    email = null;
  }

  public void testSetWrappedSmtpPortWithLessThanRange() throws BonitaException {
    email = getBasicSettings();
    email.setSmtpPort(new Long(-1));
    final List<ConnectorError> validate = email.validate();
    assertEquals(1, validate.size());
    final ConnectorError error = validate.get(0);
    assertEquals("smtpPort", error.getField());
    assertEquals("cannot be less than 0!", error.getError().getMessage());
    email = null;
  }

  public void testSetWrappedSmtpPortWithGreaterThanRange() throws BonitaException {
    email = getBasicSettings();
    email.setSmtpPort(new Long(65536));
    final List<ConnectorError> validate = email.validate();
    assertEquals(1, validate.size());
    final ConnectorError error = validate.get(0);
    assertEquals("smtpPort", error.getField());
    assertEquals("cannot be greater than 65535!", error.getError().getMessage());
    email = null;
  }

  public void testSetSmtpPortWithLessThanRange() throws BonitaException {
    email = getBasicSettings();
    email.setSmtpPort(-1);
    final List<ConnectorError> validate = email.validate();
    assertEquals(1, validate.size());
    final ConnectorError error = validate.get(0);
    assertEquals("smtpPort", error.getField());
    assertEquals("cannot be less than 0!", error.getError().getMessage());
    email = null;
  }

  public void testSetSmtpPortWithGreaterThanRange() throws BonitaException {
    email = getBasicSettings();
    email.setSmtpPort(65536);
    final List<ConnectorError> validate = email.validate();
    assertEquals(1, validate.size());
    final ConnectorError error = validate.get(0);
    assertEquals("smtpPort", error.getField());
    assertEquals("cannot be greater than 65535!", error.getError().getMessage());
    email = null;
  }

  public void testSetSubject() throws BonitaException {
    email = getBasicSettings();
    email.setSubject(null);
    assertTrue(email.validate().isEmpty());
    email = null;
  }

  public void testSetTo() throws BonitaException {
    email = getBasicSettings();
    email.setTo(null);
    final List<ConnectorError> validate = email.validate();
    assertEquals(1, validate.size());
    final ConnectorError error = validate.get(0);
    assertEquals("to", error.getField());
    assertEquals("This field is required so it must be set.", error.getError().getMessage());
    email = null;
  }

  public void testSetUserName() throws BonitaException {
    email = getBasicSettings();
    email.setUserName(null);
    email.setPassword(null);
    assertTrue(email.validate().isEmpty());
    assertFalse(email.isSslSupport());
    email = null;

    email = getBasicSettings();
    email.setUserName(null);
    email.setPassword("");
    final List<ConnectorError> validate = email.validate();
    assertEquals(1, validate.size());
    final ConnectorError error = validate.get(0);
    assertEquals("userName", error.getField());
    assertEquals("This field is required so it must be set.", error.getError().getMessage());
  }

  public void testSetNullSsl() throws BonitaException {
    email = getBasicSettings();
    email.setSslSupport(null);
    assertTrue(email.validate().isEmpty());
    assertFalse(email.isSslSupport());
    email = null;
  }

  public void testSetSsl() {
    email = getBasicSettings();
    email.setSslSupport(new Boolean("true"));
    assertTrue(email.isSslSupport());
    email.setSslSupport(new Boolean("True"));
    assertTrue(email.isSslSupport());
    email.setSslSupport(new Boolean(""));
    assertFalse(email.isSslSupport());
    email.setSslSupport(new Boolean("false"));
    assertFalse(email.isSslSupport());
    email.setSslSupport(new Boolean("whatIwant"));
    assertFalse(email.isSslSupport());
  }

  public void testSetNullStarttlsSupport() throws BonitaException {
    email = getBasicSettings();
    email.setStarttlsSupport(null);
    assertTrue(email.validate().isEmpty());
    assertFalse(email.isSslSupport());
    email = null;
  }

  public void testSetStarttlsSupport() {
    email = getBasicSettings();
    email.setStarttlsSupport(new Boolean("true"));
    assertTrue(email.isStarttlsSupport());
    email.setStarttlsSupport(new Boolean("True"));
    assertTrue(email.isStarttlsSupport());
    email.setStarttlsSupport(new Boolean(""));
    assertFalse(email.isStarttlsSupport());
    email.setStarttlsSupport(new Boolean("false"));
    assertFalse(email.isStarttlsSupport());
    email.setStarttlsSupport(new Boolean("whatIwant"));
    assertFalse(email.isStarttlsSupport());
  }

  public void testSetNullHtml() throws BonitaException {
    email = getBasicSettings();
    email.setHtml(null);
    assertTrue(email.validate().isEmpty());
    assertFalse(email.isHtml());
    email = null;
  }

  public void testSetHtml() {
    email = getBasicSettings();
    email.setHtml(new Boolean("true"));
    assertTrue(email.isHtml());
    email.setHtml(new Boolean("True"));
    assertTrue(email.isHtml());
    email.setHtml(new Boolean(""));
    assertFalse(email.isHtml());
    email.setHtml(new Boolean("false"));
    assertFalse(email.isHtml());
    email.setHtml(new Boolean("whatIwant"));
    assertFalse(email.isHtml());
  }

  public void testSendEmail() throws Exception {
    try {
      startServer();
      email = getBasicSettings();
      email.execute();
      final List<WiserMessage> messages = server.getMessages();
      assertEquals(1, messages.size());
      final WiserMessage message = messages.get(0);
      assertNotNull(message.getEnvelopeSender());
      assertEquals(ADDRESSJOHN, message.getEnvelopeReceiver());
      final MimeMessage mime = message.getMimeMessage();
      assertEquals(SUBJECT, mime.getSubject());
      assertEquals(0, mime.getSize());
      email = null;
    } finally {
      stopServer();
    }
  }

  public void testSendEmailWithAutentication() throws Exception {
    try {
      startServer();
      email = getBasicSettings();
      email.setUserName("john");
      email.setPassword("doe");
      email.execute();
      final List<WiserMessage> messages = server.getMessages();
      assertEquals(1, messages.size());
      final WiserMessage message = messages.get(0);
      assertNotNull(message.getEnvelopeSender());
      assertEquals(ADDRESSJOHN, message.getEnvelopeReceiver());
      final MimeMessage mime = message.getMimeMessage();
      assertEquals(SUBJECT, mime.getSubject());
      assertEquals(0, mime.getSize());
      email = null;
    } finally {
      stopServer();
    }
  }

  public void testSendEmailWithFromAddress() throws Exception {
    try {
      startServer();
      email = getBasicSettings();
      email.setFrom(ADDRESSMARK);
      email.execute();
      final List<WiserMessage> messages = server.getMessages();
      assertEquals(1, messages.size());
      final WiserMessage message = messages.get(0);
      assertEquals(ADDRESSMARK, message.getEnvelopeSender());
      assertEquals(ADDRESSJOHN, message.getEnvelopeReceiver());
      final MimeMessage mime = message.getMimeMessage();
      assertEquals(SUBJECT, mime.getSubject());
      assertEquals(0, mime.getSize());
      email = null;
    } finally {
      stopServer();
    }
  }

  public void testSendEmailWithToRecipientsAddresses() throws Exception {
    try {
      startServer();
      email = getBasicSettings();
      email.setTo(ADDRESSJOHN + ", " + ADDRESSPATTY);
      email.setFrom(ADDRESSMARK);
      email.execute();
      final List<WiserMessage> messages = server.getMessages();
      assertEquals(2, messages.size());
      WiserMessage message = messages.get(0);
      assertEquals(ADDRESSMARK, message.getEnvelopeSender());
      assertEquals(ADDRESSJOHN, message.getEnvelopeReceiver());
      MimeMessage mime = message.getMimeMessage();
      assertEquals(SUBJECT, mime.getSubject());
      assertEquals(0, mime.getSize());

      message = messages.get(1);
      assertEquals(ADDRESSMARK, message.getEnvelopeSender());
      assertEquals(ADDRESSPATTY, message.getEnvelopeReceiver());
      mime = message.getMimeMessage();
      assertEquals(SUBJECT, mime.getSubject());
      assertEquals(0, mime.getSize());
      email = null;
    } finally {
      stopServer();
    }
  }

  public void testSendEmailWithCcRecipientsAddresses() throws Exception {
    try {
      startServer();
      email = getBasicSettings();
      email.setFrom(ADDRESSMARK);
      email.setCc(ADDRESSPATTY);
      email.execute();

      List<WiserMessage> messages = server.getMessages();
      assertEquals(2, messages.size());
      WiserMessage message = messages.get(0);
      assertEquals(ADDRESSMARK, message.getEnvelopeSender());
      assertEquals(ADDRESSJOHN, message.getEnvelopeReceiver());
      MimeMessage mime = message.getMimeMessage();
      assertEquals(SUBJECT, mime.getSubject());
      assertEquals(0, mime.getSize());

      message = messages.get(1);
      assertEquals(ADDRESSMARK, message.getEnvelopeSender());
      assertEquals(ADDRESSPATTY, message.getEnvelopeReceiver());
      mime = message.getMimeMessage();
      assertEquals(SUBJECT, mime.getSubject());
      assertEquals(0, mime.getSize());

      email.setCc(ADDRESSPATTY + ", " + ADDRESSMARK);
      email.execute();
      messages = server.getMessages();
      assertEquals(5, messages.size());
      message = messages.get(4);
      assertEquals(ADDRESSMARK, message.getEnvelopeSender());
      assertEquals(ADDRESSMARK, message.getEnvelopeReceiver());
      mime = message.getMimeMessage();
      assertEquals(SUBJECT, mime.getSubject());
      assertEquals(0, mime.getSize());
      email = null;
    } finally {
      stopServer();
    }
  }

  public void testSendEmailWithPlainMessage() throws Exception {
    try {
      startServer();
      email = getBasicSettings();
      email.setMessage(PLAINMESSAGE);
      email.setFrom(ADDRESSMARK);
      email.execute();
      final List<WiserMessage> messages = server.getMessages();
      assertEquals(1, messages.size());
      final WiserMessage message = messages.get(0);
      assertEquals(ADDRESSMARK, message.getEnvelopeSender());
      assertEquals(ADDRESSJOHN, message.getEnvelopeReceiver());
      final MimeMessage mime = message.getMimeMessage();
      assertEquals(SUBJECT, mime.getSubject());
      assertTrue(mime.getContentType().contains(TEXT_PLAIN));
      assertEquals(PLAINMESSAGE, mime.getContent());
      email = null;
    } finally {
      stopServer();
    }
  }

  public void testSendEmailWithHtmlMessage() throws Exception {
    try {
      startServer();
      email = getBasicSettings();
      email.setHtml(true);
      email.setMessage(HTMLMESSAGE);
      email.setFrom(ADDRESSMARK);
      email.execute();
      final List<WiserMessage> messages = server.getMessages();
      assertEquals(1, messages.size());
      final WiserMessage message = messages.get(0);
      assertEquals(ADDRESSMARK, message.getEnvelopeSender());
      assertEquals(ADDRESSJOHN, message.getEnvelopeReceiver());
      final MimeMessage mime = message.getMimeMessage();
      assertEquals(SUBJECT, mime.getSubject());
      assertTrue(mime.getContentType().contains(TEXT_HTML));
      assertEquals(HTMLMESSAGE, mime.getContent());
      email = null;
    } finally {
      stopServer();
    }
  }

  public void testSendEmailWithExtraHeaders() throws Exception {
    try {
      startServer();
      email = getBasicSettings();
      final HashMap<String, String> headers = new HashMap<String, String>();
      headers.put("X-Mailer", "Bonita Mailer");
      headers.put("Message-ID", "IWantToHackTheServer");
      headers.put("X-Priority", "2 (High)");
      headers.put("Content-Type", "video/mpeg");
      headers.put("WhatIWant", "anyValue");
      headers.put("From", "alice@bob.charly");
      headers.put(null, null);
      email.setHeaders(headers);
      email.execute();

      final List<WiserMessage> messages = server.getMessages();
      assertEquals(1, messages.size());
      final WiserMessage message = messages.get(0);
      assertNotNull(message.getEnvelopeSender());
      assertEquals(ADDRESSJOHN, message.getEnvelopeReceiver());
      final MimeMessage mime = message.getMimeMessage();
      assertEquals(SUBJECT, mime.getSubject());
      assertEquals(0, mime.getSize());
      assertEquals("Bonita Mailer", mime.getHeader("X-Mailer", ""));
      assertEquals("2 (High)", mime.getHeader("X-Priority", ""));
      assertEquals("anyValue", mime.getHeader("WhatIWant", ""));
      assertNotSame("alice@bob.charly", mime.getHeader("From"));
      assertFalse(mime.getContentType().contains("video/mpeg"));
      assertNotSame("IWantToHackTheServer", mime.getHeader("Message-ID"));
      email = null;
    } finally {
      stopServer();
    }
  }

  public void testSendEmailWithAttachments() throws Exception {
    try {
      startServer();
      email = getBasicSettings();
      final ArrayList<Object> attachments = getBasicAttachments();
      email.setAttachments(attachments);
      email.execute();
      final List<WiserMessage> messages = server.getMessages();
      assertEquals(1, messages.size());
      final WiserMessage message = messages.get(0);
      assertNotNull(message.getEnvelopeSender());
      assertEquals(ADDRESSJOHN, message.getEnvelopeReceiver());
      final MimeMessage mime = message.getMimeMessage();
      assertEquals(SUBJECT, mime.getSubject());
      assertTrue(mime.getContentType().contains(MULTIPART_MIXED));
      final Multipart m = (Multipart) mime.getContent();

      assertEquals("", m.getBodyPart(0).getContent());
      // attachments
      assertEquals(XML_FILE, m.getBodyPart(1).getFileName());
      email = null;
    } finally {
      stopServer();
    }
  }

  public void testSendEmailWithHtmlMessageAndAttachments() throws Exception {
    try {
      startServer();
      email = getBasicSettings();
      email.setHtml(true);
      email.setMessage(HTMLMESSAGE);
      final ArrayList<Object> attachments = getBasicAttachments();
      email.setAttachments(attachments);
      email.execute();

      final List<WiserMessage> messages = server.getMessages();
      assertEquals(1, messages.size());
      final WiserMessage message = messages.get(0);
      assertNotNull(message.getEnvelopeSender());
      assertEquals(ADDRESSJOHN, message.getEnvelopeReceiver());
      final MimeMessage mime = message.getMimeMessage();
      assertEquals(SUBJECT, mime.getSubject());
      assertTrue(mime.getContentType().contains(MULTIPART_MIXED));

      final Multipart m = (Multipart) mime.getContent();
      assertEquals(HTMLMESSAGE, m.getBodyPart(0).getContent());
      assertTrue(m.getBodyPart(0).getContentType().contains(TEXT_HTML));
      // attachments
      assertEquals(XML_FILE, m.getBodyPart(1).getFileName());
      email = null;
    } finally {
      stopServer();
    }
  }

  public void testSendEmailWithHtmlMessageAndAttachmentsIncludingImage() throws Exception {
    try {
      startServer();
      email = getBasicSettings();
      email.setHtml(true);
      email.setMessage(HTMLMESSAGE);
      final ArrayList<Object> attachments = getBasicAttachments();
      final URL gif = this.getClass().getResource(PNG_IMAGE);
      attachments.add(gif.getPath());
      email.setAttachments(attachments);
      email.execute();

      final List<WiserMessage> messages = server.getMessages();
      assertEquals(1, messages.size());
      final WiserMessage message = messages.get(0);
      assertNotNull(message.getEnvelopeSender());
      assertEquals(ADDRESSJOHN, message.getEnvelopeReceiver());
      final MimeMessage mime = message.getMimeMessage();
      assertEquals(SUBJECT, mime.getSubject());
      assertTrue(mime.getContentType().contains(MULTIPART_MIXED));
      final Multipart m = (Multipart) mime.getContent();
      assertEquals(HTMLMESSAGE, m.getBodyPart(0).getContent());
      assertTrue(m.getBodyPart(0).getContentType().contains(TEXT_HTML));
      // attachments
      assertEquals(XML_FILE, m.getBodyPart(1).getFileName());
      assertEquals(PNG_IMAGE, m.getBodyPart(2).getFileName());
      email = null;
    } finally {
      stopServer();
    }
  }

  public void testSendEmailWithPlainMessageAndEmbeddedImage() throws Exception {
    try {
      startServer();
      email = getBasicSettings();
      final String imageHtml = "<img src=\"cid:first\"><br/><H1>Hello World !</H1>";
      email.setMessage(imageHtml);
      final URL gif = this.getClass().getResource(PNG_IMAGE);
      final HashMap<String, String> images = new HashMap<String, String>();
      images.put("<first>", gif.getPath());
      images.put(null, gif.getPath());
      images.put("<second>", null);
      email.setImages(images);
      email.execute();

      final List<WiserMessage> messages = server.getMessages();
      assertEquals(1, messages.size());
      final WiserMessage message = messages.get(0);
      assertNotNull(message.getEnvelopeSender());
      assertEquals(ADDRESSJOHN, message.getEnvelopeReceiver());
      final MimeMessage mime = message.getMimeMessage();
      assertEquals(SUBJECT, mime.getSubject());
      assertTrue(mime.getContentType().contains(MULTIPART_MIXED));

      final Multipart m = (Multipart) mime.getContent();
      assertEquals(imageHtml, m.getBodyPart(0).getContent());
      assertTrue(m.getBodyPart(0).getContentType().contains(TEXT_PLAIN));
      // image
      assertEquals(new MimetypesFileTypeMap().getContentType(gif.getFile()), m.getBodyPart(1).getContentType());
      email = null;
    } finally {
      stopServer();
    }
  }

  public void testSendEmailWithHtmlMessageAndEmbeddedImage() throws Exception {
    try {
      startServer();
      email = getBasicSettings();
      email.setHtml(true);
      final String imageHtml = "<img src=\"cid:first\"><br/><H1>Hello World !</H1>";
      email.setMessage(imageHtml);
      final URL gif = this.getClass().getResource(PNG_IMAGE);
      final HashMap<String, String> images = new HashMap<String, String>();
      images.put("<first>", gif.getPath());
      email.setImages(images);
      email.execute();

      final List<WiserMessage> messages = server.getMessages();
      assertEquals(1, messages.size());
      final WiserMessage message = messages.get(0);
      assertNotNull(message.getEnvelopeSender());
      assertEquals(ADDRESSJOHN, message.getEnvelopeReceiver());
      final MimeMessage mime = message.getMimeMessage();
      assertEquals(SUBJECT, mime.getSubject());
      assertTrue(mime.getContentType().contains(MULTIPART_MIXED));

      final Multipart m = (Multipart) mime.getContent();
      assertEquals(imageHtml, m.getBodyPart(0).getContent());
      assertTrue(m.getBodyPart(0).getContentType().contains(TEXT_HTML));
      // image
      assertEquals(new MimetypesFileTypeMap().getContentType(gif.getFile()), m.getBodyPart(1).getContentType());
      final String[] content = m.getBodyPart(1).getHeader("Content-ID");
      assertEquals("<first>", content[0]);
      email = null;
    } finally {
      stopServer();
    }
  }

  public void testSendEmailWithHtmlMessageAndEmbeddedImagesAndAttachments() throws Exception {
    try {
      startServer();
      email = getBasicSettings();
      email.setHtml(true);
      final String imageHtml = "<img src=\"cid:first\"><br/><H1>Hello World !</H1>";
      email.setMessage(imageHtml);
      final URL gif = this.getClass().getResource(PNG_IMAGE);
      final HashMap<String, String> images = new HashMap<String, String>();
      images.put("<first>", gif.getPath());
      images.put(null, gif.getPath());
      images.put("<second>", null);
      email.setImages(images);
      final ArrayList<Object> attachments = getBasicAttachments();
      email.setAttachments(attachments);
      email.execute();

      final List<WiserMessage> messages = server.getMessages();
      assertEquals(1, messages.size());
      final WiserMessage message = messages.get(0);
      assertNotNull(message.getEnvelopeSender());
      assertEquals(ADDRESSJOHN, message.getEnvelopeReceiver());
      final MimeMessage mime = message.getMimeMessage();
      assertEquals(SUBJECT, mime.getSubject());
      assertTrue(mime.getContentType().contains(MULTIPART_MIXED));

      final Multipart m = (Multipart) mime.getContent();
      assertEquals(imageHtml, m.getBodyPart(0).getContent());
      assertTrue(m.getBodyPart(0).getContentType().contains(TEXT_HTML));
      // image
      final String contentTypeOfPng = new MimetypesFileTypeMap().getContentType(gif.getFile());
      assertEquals(contentTypeOfPng, m.getBodyPart(1).getContentType());
      final String[] content = m.getBodyPart(1).getHeader("Content-ID");
      assertEquals("<first>", content[0]);
      // attachment
      assertEquals(XML_FILE, m.getBodyPart(2).getFileName());
      final String contentTypeOfBodyPart2 = m.getBodyPart(2).getContentType();
      assertTrue("Expected to find " + "application/octet-stream" + " in " + contentTypeOfBodyPart2,
          contentTypeOfBodyPart2.contains("application/octet-stream"));
      email = null;
    } finally {
      stopServer();
    }
  }

  public void testSendEmailWithHtmlMessageAndAttachmentsIncludingImagesAndEmbeddedImage() throws Exception {
    try {
      startServer();
      email = getBasicSettings();
      email.setHtml(true);
      final String imageHtml = "<img src=\"cid:first\"><br/><H1>Hello World !</H1>";
      email.setMessage(imageHtml);

      final URL gif = this.getClass().getResource(PNG_IMAGE);
      final HashMap<String, String> images = new HashMap<String, String>();
      images.put("<first>", gif.getPath());
      images.put(null, gif.getPath());
      images.put("<second>", null);
      email.setImages(images);
      final ArrayList<Object> attachments = getBasicAttachments();
      attachments.add(gif.getPath());
      email.setAttachments(attachments);
      email.execute();

      final List<WiserMessage> messages = server.getMessages();
      assertEquals(1, messages.size());
      final WiserMessage message = messages.get(0);
      assertNotNull(message.getEnvelopeSender());
      assertEquals(ADDRESSJOHN, message.getEnvelopeReceiver());
      final MimeMessage mime = message.getMimeMessage();
      assertEquals(SUBJECT, mime.getSubject());
      assertTrue(mime.getContentType().contains(MULTIPART_MIXED));

      final Multipart m = (Multipart) mime.getContent();
      assertEquals(imageHtml, m.getBodyPart(0).getContent());
      assertTrue(m.getBodyPart(0).getContentType().contains(TEXT_HTML));
      // image
      final String contentTypeOfPng = new MimetypesFileTypeMap().getContentType(gif.getFile());
      assertEquals(contentTypeOfPng, m.getBodyPart(1).getContentType());
      final String[] content = m.getBodyPart(1).getHeader("Content-ID");
      assertEquals("<first>", content[0]);
      // attachment
      assertEquals(XML_FILE, m.getBodyPart(2).getFileName());
      assertEquals("application/octet-stream; name=" + XML_FILE, m.getBodyPart(2).getContentType());
      assertEquals(PNG_IMAGE, m.getBodyPart(3).getFileName());
      assertEquals(contentTypeOfPng + "; name=" + PNG_IMAGE, m.getBodyPart(3).getContentType());
      email = null;
    } finally {
      stopServer();
    }
  }
  
  public void testSendEmailWithHtmlMessageAndEmptyAttachmentsList() throws Exception {
	    fail("test not yet implemented");
  }

  public void testSendCyrillicEmail() throws Exception {
    try {
      startServer();
      email = getBasicSettings();
      email.setMessage(CYRILLIC_MESSAGE);
      email.setSubject(CYRILLIC_SUBJECT);
      email.execute();
      final List<WiserMessage> messages = server.getMessages();
      assertEquals(1, messages.size());
      final WiserMessage message = messages.get(0);
      assertNotNull(message.getEnvelopeSender());
      assertEquals(ADDRESSJOHN, message.getEnvelopeReceiver());
      final MimeMessage mime = message.getMimeMessage();
      assertEquals(CYRILLIC_SUBJECT, mime.getSubject());
      assertTrue(mime.getContentType().contains(TEXT_PLAIN));
      assertEquals(CYRILLIC_MESSAGE, mime.getContent());
      email = null;
    } finally {
      stopServer();
    }
  }

  public void testSendBadEncodingCyrillicEmail() throws Exception {
    try {
      startServer();
      email = getBasicSettings();
      email.setMessage(CYRILLIC_MESSAGE);
      email.setCharset("iso-8859-1");
      email.execute();
      final List<WiserMessage> messages = server.getMessages();
      assertEquals(1, messages.size());
      final WiserMessage message = messages.get(0);
      assertNotNull(message.getEnvelopeSender());
      assertEquals(ADDRESSJOHN, message.getEnvelopeReceiver());
      final MimeMessage mime = message.getMimeMessage();
      assertEquals(SUBJECT, mime.getSubject());
      assertTrue(mime.getContentType().contains(TEXT_PLAIN));
      assertEquals("? ? ?", mime.getContent());
      email = null;
    } finally {
      stopServer();
    }
  }

  private EmailConnector getBasicSettings() {
    final EmailConnector email = new EmailConnector();
    email.setSmtpHost(SMTP_HOST);
    email.setSmtpPort(smtpPort);
    email.setTo(ADDRESSJOHN);
    email.setSubject(SUBJECT);
    return email;
  }

  private ArrayList<Object> getBasicAttachments() {
    final URL xml = EmailConnector.class.getResource(XML_FILE);
    final ArrayList<Object> attachments = new ArrayList<Object>();
    attachments.add(xml.getPath());
    attachments.add(null);
    return attachments;
  }

  private static int findFreePort(int port) {
    boolean free = false;
    while (!free && port <= 65535) {
      if (isFreePort(port)) {
        free = true;
      } else {
        port++;
      }
    }
    return port;
  }

  private static boolean isFreePort(final int port) {
    try {
      final ServerSocket socket = new ServerSocket(port);
      socket.close();
      return true;
    } catch (final IOException e) {
      return false;
    }
  }

  private void startServer() {
    if (smtpPort == 0) {
      smtpPort = findFreePort(31025);
    }
    server = new Wiser();
    server.setPort(smtpPort);
    server.start();
  }

  private void stopServer() throws InterruptedException {
    server.stop();
    Thread.sleep(150);
    server = null;
  }

}
