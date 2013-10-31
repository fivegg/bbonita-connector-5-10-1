package org.bonitasoft.connectors.database.roleresolver;

import org.bonitasoft.connectors.database.DatabaseConnector;
import org.bonitasoft.connectors.database.DatabaseRoleResolver;
import org.bonitasoft.connectors.database.postgresql.PostgreSQLConnector;
import org.bonitasoft.connectors.database.postgresql.PostgreSQLRoleResolver;
import org.ow2.bonita.connector.core.Connector;

public class PostgreSQLRoleResolverTest extends DatabaseRoleResolverTest {

  @Override
  protected DatabaseConnector getDatabaseConnector() {
    PostgreSQLConnector database = new PostgreSQLConnector();
    database.setDatabase("bonita");
    database.setHostName("192.168.1.212");
    database.setPassword("admin");
    database.setPort(5432);
    database.setUsername("postgres");
    return database;
  }

  @Override
  protected DatabaseRoleResolver getDatabaseRoleResolver() {
    PostgreSQLRoleResolver database = new PostgreSQLRoleResolver();
    database.setDatabase("bonita");
    database.setHostName("192.168.1.212");
    database.setPassword("admin");
    database.setPort(5432);
    database.setUsername("postgres");
    return database;
  }

  @Override
  protected Class<? extends Connector> getConnectorClass() {
    return PostgreSQLRoleResolver.class;
  }

  protected String getCreateTable() {
    StringBuilder builder = new StringBuilder("CREATE TABLE ");
    builder.append(getTableName());
    builder.append(" (id INT NOT NULL PRIMARY KEY, username VARCHAR(25), firstname VARCHAR(25), lastname VARCHAR(25), age INTEGER)");
    return builder.toString();
  }

  protected String getFirstInsert() {
    StringBuilder builder = new StringBuilder("INSERT INTO ");
    builder.append(getTableName());
    builder.append(" (id, username, firstname, lastname, age) VALUES (1, 'john', 'John', 'Doe', 27)");
    return builder.toString();
  }

  protected String getSecondInsert() {
    StringBuilder builder = new StringBuilder("INSERT INTO ");
    builder.append(getTableName());
    builder.append(" (id, username, firstname, lastname, age) VALUES (2, 'jane', 'Jane', 'Doe', 31)");
    return builder.toString();
  }

}
