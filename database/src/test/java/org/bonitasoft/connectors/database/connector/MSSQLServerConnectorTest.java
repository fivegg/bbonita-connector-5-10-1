package org.bonitasoft.connectors.database.connector;

import org.bonitasoft.connectors.database.LocalDatabaseConnector;
import org.bonitasoft.connectors.database.mssqlserver.MSSQLServerConnector;
import org.ow2.bonita.connector.core.Connector;

public class MSSQLServerConnectorTest extends RemoteDatabaseConnectorTest {

	@Override
	protected LocalDatabaseConnector getDatabaseConnector() {
		MSSQLServerConnector sqlserver = new MSSQLServerConnector();
    sqlserver.setDatabase("bonita_hist_linux");
    sqlserver.setHostName("192.168.1.210");
    sqlserver.setPort(1433);
    sqlserver.setQuery("SELECT * FROM Person");
    sqlserver.setUsername("test");
    sqlserver.setPassword("TopSecret");
    return sqlserver;
	}

	@Override
	protected Class<? extends Connector> getConnectorClass() {
		return MSSQLServerConnector.class;
	}

}
