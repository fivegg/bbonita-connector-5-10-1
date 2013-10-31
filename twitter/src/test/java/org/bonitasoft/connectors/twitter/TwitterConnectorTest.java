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
package org.bonitasoft.connectors.twitter;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.ow2.bonita.connector.core.Connector;
import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.util.BonitaException;

public abstract class TwitterConnectorTest extends TestCase {

  protected static final Logger LOG = Logger.getLogger(TwitterConnectorTest.class.getName());

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    if (TwitterConnectorTest.LOG.isLoggable(Level.WARNING)) {
      TwitterConnectorTest.LOG.warning("======== Starting test: " + this.getClass().getName() + "." + this.getName() + "() ==========");
    }
  }

  @Override
  protected void tearDown() throws Exception {
    if (TwitterConnectorTest.LOG.isLoggable(Level.WARNING)) {
      TwitterConnectorTest.LOG.warning("======== Ending test: " + this.getName() + " ==========");
    }
    super.tearDown();
  }

  protected abstract Class<? extends Connector> getConnectorClass();

  public void testValidateConnector() throws BonitaException {
    Connector.validateConnector(getConnectorClass());
    Assert.assertTrue(Connector.validateConnector(getConnectorClass()).isEmpty());
  }

  public void testSetWrappedSmtpPortWithLessThanRange() throws BonitaException {
    TwitterConnector connector = getTwitterConnector();
    connector.setProxyHost("");
    connector.setProxyPort(new Long(-1));
    List<ConnectorError> validate = connector.validate();
    Assert.assertEquals(1, validate.size());
    ConnectorError error = validate.get(0);
    Assert.assertEquals("proxyPort", error.getField());
    Assert.assertEquals("cannot be less than 0!",
        error.getError().getMessage());
  }

  public void testSetWrappedSmtpPortWithGreaterThanRange() throws BonitaException {
    TwitterConnector connector = getTwitterConnector();
    connector.setProxyPort(new Long(65536));
    connector.setProxyHost("");
    List<ConnectorError> validate = connector.validate();
    Assert.assertEquals(1, validate.size());
    ConnectorError error = validate.get(0);
    Assert.assertEquals("proxyPort", error.getField());
    Assert.assertEquals("cannot be greater than 65535!",
        error.getError().getMessage());
  }

  public void testSetSmtpPortWithLessThanRange() throws BonitaException {
    TwitterConnector connector = getTwitterConnector();
    connector.setProxyPort(-1);
    connector.setProxyHost("");
    List<ConnectorError> validate = connector.validate();
    Assert.assertEquals(1, validate.size());
    ConnectorError error = validate.get(0);
    Assert.assertEquals("proxyPort", error.getField());
    Assert.assertEquals("cannot be less than 0!",
        error.getError().getMessage());
  }

  public void testSetSmtpPortWithGreaterThanRange() throws BonitaException {
    TwitterConnector connector = getTwitterConnector();
    connector.setProxyPort(65536);
    connector.setProxyHost("");
    List<ConnectorError> validate = connector.validate();
    Assert.assertEquals(1, validate.size());
    ConnectorError error = validate.get(0);
    Assert.assertEquals("proxyPort", error.getField());
    Assert.assertEquals("cannot be greater than 65535!",
        error.getError().getMessage());
  }

//  public void testExecute() {
//    TwitterConnector connector = getTwitterConnector();
//    try {
//      connector.execute();
//    } catch (Exception e) {
//      e.printStackTrace();
//      fail("Impossible! A Tweet must be sent");
//    }
//  }

  public abstract TwitterConnector getTwitterConnector(); 
}
