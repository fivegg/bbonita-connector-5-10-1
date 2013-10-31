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
package org.bonitasoft.connectors.database.connector;

import junit.framework.Assert;

import org.bonitasoft.connectors.database.RemoteDatabaseConnector;
import org.ow2.bonita.util.BonitaException;

public abstract class RemoteDatabaseConnectorTest extends SimpleRemoteDatabaseConnectorTest {

  public void testNegativePort() throws BonitaException {
    RemoteDatabaseConnector database =
      (RemoteDatabaseConnector) getConnector();
    database.setPort(-1);
    Assert.assertFalse(database.validate().isEmpty());
  }

  public void testMinLimitAvailablePort() throws BonitaException {
    RemoteDatabaseConnector database =
      (RemoteDatabaseConnector) getConnector();
    database.setPort(0);
    Assert.assertTrue(database.validate().isEmpty());
  }

  public void testMaxLimitAvailablePort() throws BonitaException {
    RemoteDatabaseConnector database =
      (RemoteDatabaseConnector) getConnector();
    database.setPort(65535);
    Assert.assertTrue(database.validate().isEmpty());
  }

  public void testGreaterThanMaxLimitPort() throws BonitaException {
    RemoteDatabaseConnector database =
      (RemoteDatabaseConnector) getConnector();
    database.setPort(65536);
    Assert.assertFalse(database.validate().isEmpty());
  }

  public void testMaxIntegerPort() throws BonitaException {
    RemoteDatabaseConnector database =
      (RemoteDatabaseConnector) getConnector();
    database.setPort(Integer.MAX_VALUE);
    Assert.assertFalse(database.validate().isEmpty());
  }

}
