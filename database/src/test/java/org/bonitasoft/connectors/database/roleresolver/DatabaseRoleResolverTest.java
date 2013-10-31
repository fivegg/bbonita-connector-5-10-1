package org.bonitasoft.connectors.database.roleresolver;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.bonitasoft.connectors.database.DatabaseConnector;
import org.bonitasoft.connectors.database.DatabaseRoleResolver;
import org.bonitasoft.connectors.database.DatabaseUtil;
import org.ow2.bonita.connector.core.Connector;
import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.util.BonitaException;

public abstract class DatabaseRoleResolverTest extends TestCase {

  protected static final Logger LOG = Logger.getLogger(DatabaseRoleResolverTest.class.getName());

  private String tableName;

  DatabaseConnector getConnector() {
    DatabaseConnector connector = getDatabaseConnector();
    return connector;
  }

  DatabaseRoleResolver getRoleResolver() {
    DatabaseRoleResolver connector = getDatabaseRoleResolver();
    connector.setQuery(getAllUsers());
    connector.setRoleId("database");
    return connector;
  }

  protected abstract DatabaseConnector getDatabaseConnector();
  protected abstract DatabaseRoleResolver getDatabaseRoleResolver();
  protected abstract Class<? extends Connector> getConnectorClass();
  
  protected final String getTableName() {
    return DatabaseUtil.getTableName(tableName);
  }
  
  protected String getAllUsers() {
    StringBuilder builder = new StringBuilder("SELECT userName FROM ");
    builder.append(getTableName());
    return builder.toString();
  }
  
  protected String getCreateTable() {
    StringBuilder builder = new StringBuilder("CREATE TABLE ");
    builder.append(getTableName());
    builder.append(" (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, username VARCHAR(25), firstname VARCHAR(25), lastname VARCHAR(25), age INT)");
    return builder.toString();
  }

  protected String getFirstInsert() {
    StringBuilder builder = new StringBuilder("INSERT INTO ");
    builder.append(getTableName());
    builder.append(" (username, firstname, lastname, age) VALUES ('john', 'John', 'Doe', 27)");
    return builder.toString();
  }

  protected String getSecondInsert() {
    StringBuilder builder = new StringBuilder("INSERT INTO ");
    builder.append(getTableName());
    builder.append(" (username, firstname, lastname, age) VALUES ('jane', 'Jane', 'Doe', 31)");
    return builder.toString();
  }

  protected String getDropTable() {
    StringBuilder builder = new StringBuilder("DROP TABLE ");
    builder.append(getTableName());
    return builder.toString();
  }
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    if (LOG.isLoggable(Level.WARNING)) {
      LOG.warning("======== Starting test: " + this.getClass().getName() + "." + this.getName() + "() ==========");
    }
    try {
      createTable();
      insertValues();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void tearDown() throws Exception {
    if (LOG.isLoggable(Level.WARNING)) {
      LOG.warning("======== Ending test: " + this.getName() + " ==========");
    }
    try {
      dropTable();
    } catch (SQLException e) {
    }
    super.tearDown();
  }
  
  private void createTable() throws Exception {
    DatabaseConnector database = getConnector();
    database.setQuery(getCreateTable());
    database.execute();
  }

  private void dropTable() throws Exception {
    DatabaseConnector database = getConnector();
    database.setQuery(getDropTable());
    database.execute();
  }

  private void insertValues() throws Exception {
    DatabaseConnector database = getConnector();
    database.setQuery(getFirstInsert());
    database.execute();
    database.setQuery(getSecondInsert());
    database.execute();
  }

  public void testValidateConnector() throws BonitaException {
    Class<? extends Connector> connectorClass = getConnectorClass();
    List<ConnectorError> errors = Connector.validateConnector(connectorClass);
    assertTrue(errors.isEmpty());
  }

  public void testGetAllUsers() throws Exception {
    DatabaseRoleResolver database = getRoleResolver();
    database.setQuery(getAllUsers());
    database.execute();
    Set<String> members = database.getMembers();
    assertTrue(members.contains("john"));
    assertTrue(members.contains("jane"));
  }

}
