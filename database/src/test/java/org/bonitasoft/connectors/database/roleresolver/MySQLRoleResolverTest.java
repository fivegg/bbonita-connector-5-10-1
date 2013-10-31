package org.bonitasoft.connectors.database.roleresolver;

import org.bonitasoft.connectors.database.DatabaseConnector;
import org.bonitasoft.connectors.database.DatabaseRoleResolver;
import org.bonitasoft.connectors.database.mysql.MySQLConnector;
import org.bonitasoft.connectors.database.mysql.MySQLRoleResolver;
import org.ow2.bonita.connector.core.Connector;

public class MySQLRoleResolverTest extends DatabaseRoleResolverTest {

  @Override
  protected DatabaseRoleResolver getDatabaseRoleResolver() {
    MySQLRoleResolver mysql = new MySQLRoleResolver();
    mysql.setDatabase("bonita");
    mysql.setHostName("192.168.1.212");
    mysql.setPassword("admin");
    mysql.setPort(3306);
    mysql.setUsername("monty");
    return mysql;
  }

  @Override
  protected Class<? extends Connector> getConnectorClass() {
    return MySQLRoleResolver.class;
  }

  @Override
  protected DatabaseConnector getDatabaseConnector() {
    MySQLConnector mysql = new MySQLConnector();
    mysql.setDatabase("bonita");
    mysql.setHostName("192.168.1.212");
    mysql.setPassword("admin");
    mysql.setPort(3306);
    mysql.setUsername("monty");
    return mysql;
  }

}
