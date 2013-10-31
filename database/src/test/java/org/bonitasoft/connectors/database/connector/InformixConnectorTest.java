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

import org.bonitasoft.connectors.database.LocalDatabaseConnector;
import org.bonitasoft.connectors.database.informix.InformixConnector;
import org.ow2.bonita.connector.core.Connector;

public class InformixConnectorTest extends RemoteDatabaseConnectorTest {

  @Override
  protected LocalDatabaseConnector getDatabaseConnector() {
    InformixConnector informix = new InformixConnector();
    informix.setDatabase("bonita");
    informix.setDbServer("INFO");
    informix.setHostName("localhost");
    informix.setPassword("");
    informix.setPort(4545);
    informix.setQuery("SELECT * FROM Person");
    informix.setUsername("admin");
    return informix;
  }

  @Override
  protected Class<? extends Connector> getConnectorClass() {
    return InformixConnector.class;
  }

}
