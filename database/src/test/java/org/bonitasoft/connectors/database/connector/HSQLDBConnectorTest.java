/**
 * Copyright (C) 2009  Bull S. A. S.
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

import org.bonitasoft.connectors.database.LocalDatabaseConnector;
import org.bonitasoft.connectors.database.hsqldb.HSQLDBConnector;
import org.ow2.bonita.connector.core.Connector;
import org.ow2.bonita.util.BonitaException;

public class HSQLDBConnectorTest extends RemoteDatabaseConnectorTest {

  @Override
  protected LocalDatabaseConnector getDatabaseConnector() {
    HSQLDBConnector hsqldb = new HSQLDBConnector();
    hsqldb.setDatabase("xdb");
    hsqldb.setHostName("localhost");
    hsqldb.setPassword("");
    hsqldb.setPort(9001);
    hsqldb.setQuery("SELECT * FROM Person");
    hsqldb.setServer(true);
    hsqldb.setLocal(false);
    hsqldb.setWebServer(false);
    hsqldb.setSsl(false);
    hsqldb.setUsername("sa");
    return hsqldb;
  }

  @Override
  protected Class<? extends Connector> getConnectorClass() {
    return HSQLDBConnector.class;
  }

  public void testValidateValues() throws BonitaException {
    HSQLDBConnector hsqldb = (HSQLDBConnector) getDatabaseConnector();
    hsqldb.setLocal(false);
    hsqldb.setServer(true);
    hsqldb.setWebServer(false);
    Assert.assertTrue(hsqldb.validate().isEmpty());
    
    hsqldb.setLocal(false);
    hsqldb.setServer(false);
    hsqldb.setWebServer(true);
    Assert.assertTrue(hsqldb.validate().isEmpty());
    
    hsqldb.setLocal(true);
    hsqldb.setServer(false);
    hsqldb.setWebServer(false);
    Integer port = null;
    hsqldb.setPort(port);
    hsqldb.setHostName(null);
    hsqldb.setSsl(null);
    Assert.assertTrue(hsqldb.validate().isEmpty());
  }

}
