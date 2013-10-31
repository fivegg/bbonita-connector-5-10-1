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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.bonitasoft.connectors.database.DatabaseConnector;
import org.bonitasoft.connectors.database.DatabaseUtil;
import org.bonitasoft.connectors.database.RowSet;
import org.ow2.bonita.connector.core.Connector;
import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.util.BonitaException;

public abstract class DatabaseConnectorTest extends TestCase {

  protected static final Logger LOG = Logger.getLogger(DatabaseConnectorTest.class.getName());
  
  private String tableName;

  DatabaseConnector getConnector() {
    DatabaseConnector connector = getDatabaseConnector();
    connector.setQuery(getSelectAll());
    return connector;
  }

  protected abstract DatabaseConnector getDatabaseConnector();

  protected List<List<Object>> getResult() {
    List<List<Object>> values = new ArrayList<List<Object>>();
    List<Object> row1 = new ArrayList<Object>();
    row1.add(1);
    row1.add("John");
    row1.add("Doe");
    row1.add(27);
    row1.add(15.4);
    List<Object> row2 = new ArrayList<Object>();
    row2.add(2);
    row2.add("Jane");
    row2.add("Doe");
    row2.add(31);
    row2.add(15.9);
    values.add(row1);
    values.add(row2);
    return values;
  }
  
  protected abstract Class<? extends Connector> getConnectorClass();

  public final String getTableName() {
    return DatabaseUtil.getTableName(tableName);
  }

  protected String getCreateTable() {
    StringBuilder builder = new StringBuilder("CREATE TABLE ");
    builder.append(getTableName());
    builder.append(" (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, firstname TEXT, lastname VARCHAR(25), age INT, average DOUBLE)");
    return builder.toString();
  }

  protected String getFirstInsert() {
    StringBuilder builder = new StringBuilder("INSERT INTO ");
    builder.append(getTableName());
    builder.append(" (firstname, age, lastname, average) VALUES ('John', 27, 'Doe', 15.4)");
    return builder.toString();
  }

  protected String getSecondInsert() {
    StringBuilder builder = new StringBuilder("INSERT INTO ");
    builder.append(getTableName());
    builder.append(" (firstname, age, lastname, average) VALUES ('Jane', 31, 'Doe', 15.9)");
    return builder.toString();
  }

  protected String getSelectAll() {
    StringBuilder builder = new StringBuilder("SELECT * FROM ");
    builder.append(getTableName());
    return builder.toString();
  }

  protected String getDeleteFirstInsert() {
    StringBuilder builder = new StringBuilder("DELETE FROM ");
    builder.append(getTableName());
    builder.append(" WHERE firstname = 'John'");
    return builder.toString();
  }

  protected String getDropTable() {
    StringBuilder builder = new StringBuilder("DROP TABLE ");
    builder.append(getTableName());
    return builder.toString();
  }

  protected String getInvalidSelectUserId() {
    StringBuilder builder = new StringBuilder("SELECT * FROM ");
    builder.append(getTableName());
    builder.append(" WHERE id = 3");
    return builder.toString();
  }

  protected String getFirstNameColumnName() {
    return "firstname";
  }

  protected String getIdColumnName() {
    return "id";
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    if (DatabaseConnectorTest.LOG.isLoggable(Level.WARNING)) {
      DatabaseConnectorTest.LOG.warning("======== Starting test: " + this.getClass().getName() + "." + this.getName() + "() ==========");
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
    if (DatabaseConnectorTest.LOG.isLoggable(Level.WARNING)) {
      DatabaseConnectorTest.LOG.warning("======== Ending test: " + this.getName() + " ==========");
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

  public void testGetAllValues() throws Exception {
    DatabaseConnector database = getConnector();
    database.execute();
    RowSet rowSet = database.getRowSet();
    List<List<Object>> expected = getResult();
    List<List<Object>> actual = rowSet.getValues();
    assertEquals(expected, actual);
  }

  public void testGetFirstNames() throws Exception {
    DatabaseConnector database = getConnector();
    database.execute();
    RowSet rowSet = database.getRowSet();
    List<Object> actual = rowSet.toList(2);
    List<Object> expected = new ArrayList<Object>();
    expected.add("John");
    expected.add("Jane");
    assertEquals(expected, actual);
  }

  public void testGetInBoundIndexes() throws Exception {
    DatabaseConnector database = getConnector();
    database.execute();
    RowSet rowSet = database.getRowSet();
    rowSet.toList(1);
    rowSet.toList(2);
    rowSet.toList(3);
    rowSet.toList(4);
  }

  public void getLowerBoundIndex() throws Exception {
    DatabaseConnector database = getDatabaseConnector();
    database.execute();
    RowSet rowSet = database.getRowSet();
    try {
      rowSet.toList(0);
      fail("outOfBound");
    } catch (SQLException e) {
    }
  }

  public void getUpperBoundIndex() throws Exception {
    DatabaseConnector database = getDatabaseConnector();
    database.execute();
    RowSet rowSet = database.getRowSet();
    try {
      rowSet.toList(5);
      fail("outOfBound");
    } catch (SQLException e) {
    }
  }

  public void testGetCaptionIds() throws Exception {
    DatabaseConnector database = getConnector();
    database.execute();
    RowSet rowSet = database.getRowSet();
    int i = rowSet.findColumn(getIdColumnName());
    assertEquals(1, i);
  }

  public void testInvalidColumnName() throws Exception {
    DatabaseConnector database = getConnector();
    database.execute();
    RowSet rowSet = database.getRowSet();
    try {
      rowSet.findColumn("state");
      fail("state is not a valid column name");
    } catch (SQLException e) {
    }
  }

  public void testGetListByColumnName() throws Exception {
    DatabaseConnector database = getConnector();
    database.execute();
    RowSet rowSet = database.getRowSet();
    List<Object> actual = rowSet.toList(getFirstNameColumnName());
    List<Object> expected = new ArrayList<Object>();
    expected.add("John");
    expected.add("Jane");
    assertEquals(expected, actual);
  }

  public void testListByInvalidColumnName() throws Exception {
    DatabaseConnector database = getConnector();
    database.execute();
    RowSet rowSet = database.getRowSet();
    try {
      rowSet.toList("name");
      fail("Column name is invalid");
    } catch (SQLException e) {
    }
  }

  public void testWrongTableQuery() throws Exception {
    DatabaseConnector database = getConnector();
    database.setQuery("SELECT * FROM Bonita");
    try {
      database.execute();
      fail("The table Bonita does not exist");
    } catch (SQLException e) {
    }
  }

  public void testWrongPersonIdQuery() throws Exception {
    DatabaseConnector database = getConnector();
    database.setQuery(getInvalidSelectUserId());
    database.execute();
    RowSet actual = database.getRowSet();
    assertTrue(actual.getValues().isEmpty());
  }

  public void testCreateSelectAndDropTable() throws Exception {
    DatabaseConnector database = getConnector();
    database.setQuery(getSelectAll());
    database.execute();
    RowSet rowSet = database.getRowSet();
    List<Object> actual = rowSet.toList(getFirstNameColumnName());
    List<Object> expected = new ArrayList<Object>();
    expected.add("John");
    expected.add("Jane");
    assertEquals(expected, actual);

    database.setQuery(getDeleteFirstInsert());
    database.execute();

    database.setQuery(getSelectAll());
    database.execute();
    rowSet = database.getRowSet();
    actual = rowSet.toList(getFirstNameColumnName());
    expected = new ArrayList<Object>();
    expected.add("Jane");
    assertEquals(expected, actual);
  }
}
