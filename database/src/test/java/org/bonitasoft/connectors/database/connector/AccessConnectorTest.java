package org.bonitasoft.connectors.database.connector;

import org.bonitasoft.connectors.database.LocalDatabaseConnector;
import org.bonitasoft.connectors.database.access.AccessConnector;
import org.ow2.bonita.connector.core.Connector;

public class AccessConnectorTest extends RemoteDatabaseConnectorTest {

  @Override
  protected LocalDatabaseConnector getDatabaseConnector() {
    AccessConnector access = new AccessConnector();
    access.setDatabase("/home/ic/Desktop/Bonita.accdb");
    access.setUsername("");
    access.setQuery("SELECT * FROM Person");
    return access;
  }

  @Override
  protected Class<? extends Connector> getConnectorClass() {
    return AccessConnector.class;
  }

}
