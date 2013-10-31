/**
 * Copyright (C) 2009-2011  Bull S. A. S.
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
 * 
 * Modified by Matthieu Chaffotte - BonitaSoft S.A.
 **/
package org.bonitasoft.connectors.scripting;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.ow2.bonita.connector.core.Connector;
import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.util.BonitaException;

/**
 * 
 * @author Matthieu Chaffotte, Yanyan Liu
 *
 */
public abstract class ConnectorTest extends TestCase {

  protected static final Logger LOG = Logger.getLogger(ConnectorTest.class.getName());
  private String os = System.getProperty("os.name").toLowerCase();

  protected boolean isWindows() {
    return os.contains("win");
  }

  protected boolean isMac() {
    return os.contains("mac");
  }

  protected boolean isUnix() {
    return os.contains("nix") || os.contains("nux");
  }
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    if (ConnectorTest.LOG.isLoggable(Level.WARNING)) {
      ConnectorTest.LOG.warning("======== Starting test: " + this.getClass().getName() + "." + this.getName() + "() ==========");
    }
  }

  @Override
  protected void tearDown() throws Exception {
    if (ConnectorTest.LOG.isLoggable(Level.WARNING)) {
      ConnectorTest.LOG.warning("======== Ending test: " + this.getName() + " ==========");
    }
    super.tearDown();
  }

  public void testValidateConnector() throws BonitaException {
    List<ConnectorError> errors = Connector.validateConnector(getConnectorClass());
    Assert.assertTrue(errors.isEmpty());
  }

  protected abstract Class<? extends Connector> getConnectorClass();
}
