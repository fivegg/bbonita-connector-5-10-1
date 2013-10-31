package org.bonitasoft.connectors.database.connector;

import org.bonitasoft.connectors.database.DatabaseConnector;
import org.bonitasoft.connectors.database.GenericJDBCConnector;
import org.ow2.bonita.connector.core.Connector;

public class GenericJDBCConnectorTest extends DatabaseConnectorTest {

  @Override
  protected DatabaseConnector getDatabaseConnector() {
    GenericJDBCConnector jdbc = new GenericJDBCConnector();
    jdbc.setDriver("org.gjt.mm.mysql.Driver");
    jdbc.setUrl("jdbc:mysql://192.168.1.212:3306/bonita");
    jdbc.setUsername("monty");
    jdbc.setPassword("admin");
    jdbc.setQuery("SELECT * FROM Person");
    return jdbc;
  }

  @Override
  protected Class<? extends Connector> getConnectorClass() {
    return GenericJDBCConnector.class;
  }

}
