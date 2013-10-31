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
 * 
 * Modified by Matthieu Chaffotte - BonitaSoft S.A.
 **/
package org.bonitasoft.connectors.database.connector;

import org.bonitasoft.connectors.database.LocalDatabaseConnector;
import org.bonitasoft.connectors.database.h2.H2Connector;
import org.ow2.bonita.connector.core.Connector;

public class H2ConnectorTest extends RemoteDatabaseConnectorTest {

  @Override
  protected LocalDatabaseConnector getDatabaseConnector() {
    H2Connector h2 = new H2Connector();
    h2.setHostName("localhost");
    h2.setPort(9092);
    h2.setDatabase("~/test");
    h2.setUsername("sa");
    h2.setPassword("");
    h2.setQuery("SELECT * FROM Person");
    h2.setServer(true);
    h2.setLocal(false);
    return h2;
  }

  @Override
  protected Class<? extends Connector> getConnectorClass() {
    return H2Connector.class;
  }

}
