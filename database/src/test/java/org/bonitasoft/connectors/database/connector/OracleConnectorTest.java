/**
 * Copyright (C) 2009  BonitaSoft S.A..
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.connectors.database.LocalDatabaseConnector;
import org.bonitasoft.connectors.database.oracle.OracleConnector;
import org.ow2.bonita.connector.core.Connector;

/**
 * @author Matthieu Chaffotte
 *
 */
public class OracleConnectorTest extends RemoteDatabaseConnectorTest {

  @Override
  protected LocalDatabaseConnector getDatabaseConnector() {
	  OracleConnector oracle = new OracleConnector();
	  oracle.setOracleSID(true);
	  oracle.setOracleServiceName(false);
	  oracle.setOracleOCI(false);
	  oracle.setDatabase("XE");
	  oracle.setHostName("192.168.1.212");
	  oracle.setPassword("admin");
	  oracle.setPort(1521);
	  oracle.setUsername("hr");
	  return oracle;
  }

  @Override
  protected Class<? extends Connector> getConnectorClass() {
	  return OracleConnector.class;
  }

  @Override
  protected String getCreateTable() {
    StringBuilder builder = new StringBuilder("CREATE TABLE ");
    builder.append(getTableName());
    builder.append(" (identifier NUMBER(6) PRIMARY KEY, firstname VARCHAR2(10 CHAR), lastname VARCHAR(15), age NUMBER(2), avrg NUMBER)");
    return builder.toString();
  }

  @Override
  protected String getFirstInsert() {
    StringBuilder builder = new StringBuilder("INSERT INTO ");
    builder.append(getTableName());
    builder.append(" (identifier, firstname, lastname, avrg, age) VALUES (1, 'John', 'Doe', 15.4, 27)");
    return builder.toString();
  }

  @Override
  protected String getSecondInsert() {
    StringBuilder builder = new StringBuilder("INSERT INTO ");
    builder.append(getTableName());
    builder.append(" (identifier, firstname, lastname, avrg, age) VALUES (2, 'Jane', 'Doe', 15.9, 31)");
    return builder.toString();
  }

  @Override
  protected String getInvalidSelectUserId() {
    StringBuilder builder = new StringBuilder("SELECT * FROM ");
    builder.append(getTableName());
    builder.append(" WHERE identifier = 3");
    return builder.toString();
  }

  @Override
  protected String getFirstNameColumnName() {
    return "FIRSTNAME";
  }

  @Override
  protected String getIdColumnName() {
    return "IDENTIFIER";
  }

  @Override
  protected List<List<Object>> getResult() {
    List<List<Object>> values = new ArrayList<List<Object>>();
    List<Object> row1 = new ArrayList<Object>();
    row1.add(new BigDecimal("1"));
    row1.add("John");
    row1.add("Doe");
    row1.add(new BigDecimal("27"));
    row1.add(new BigDecimal("15.4"));
    List<Object> row2 = new ArrayList<Object>();
    row2.add(new BigDecimal("2"));
    row2.add("Jane");
    row2.add("Doe");
    row2.add(new BigDecimal("31"));
    row2.add(new BigDecimal("15.9"));
    values.add(row1);
    values.add(row2);
    return values;
  }
}
