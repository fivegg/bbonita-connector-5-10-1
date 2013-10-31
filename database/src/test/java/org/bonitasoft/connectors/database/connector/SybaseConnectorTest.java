package org.bonitasoft.connectors.database.connector;

import org.bonitasoft.connectors.database.LocalDatabaseConnector;
import org.bonitasoft.connectors.database.sybase.SybaseConnector;
import org.ow2.bonita.connector.core.Connector;

public class SybaseConnectorTest extends RemoteDatabaseConnectorTest {

	@Override
  protected Class<? extends Connector> getConnectorClass() {
    return SybaseConnector.class;
  }

  @Override
  protected LocalDatabaseConnector getDatabaseConnector() {
  	SybaseConnector sybase = new SybaseConnector();
    sybase.setDatabase("bonita_core_linux");
    sybase.setHostName("192.168.1.212");
    sybase.setPassword("password");
    sybase.setPort(5000);
    sybase.setQuery("CREATE TABLE customer (First_Name char(50), Last_Name char(50), Address char(50), City char(50), Country char(25), Birth_Date date)");
    sybase.setUsername("sybase");
    return sybase;
  }

}
