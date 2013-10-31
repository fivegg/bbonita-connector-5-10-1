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
 **/
package org.bonitasoft.connectors.database.connector;

import org.bonitasoft.connectors.database.LocalDatabaseConnector;
import org.bonitasoft.connectors.database.postgresql.PostgreSQLConnector;
import org.ow2.bonita.connector.core.Connector;

public class PostgreSQLConnectorTest extends RemoteDatabaseConnectorTest {

  @Override
  protected Class<? extends Connector> getConnectorClass() {
    return PostgreSQLConnector.class;
  }

  @Override
  protected LocalDatabaseConnector getDatabaseConnector() {
    PostgreSQLConnector database = new PostgreSQLConnector();
    database.setDatabase("bonita");
    database.setHostName("192.168.1.212");
    database.setPassword("admin");
    database.setPort(5432);
    database.setUsername("postgres");
    return database;
  }

  protected String getCreateTable() {
    StringBuilder builder = new StringBuilder("CREATE TABLE ");
    builder.append(getTableName());
    builder.append(" (id INT NOT NULL PRIMARY KEY, firstname TEXT, lastname VARCHAR(25), age INTEGER, average FLOAT)");
    return builder.toString();
  }

  protected String getFirstInsert() {
    StringBuilder builder = new StringBuilder("INSERT INTO ");
    builder.append(getTableName());
    builder.append(" (id, firstname, age, lastname, average) VALUES (1, 'John', 27, 'Doe', 15.4)");
    return builder.toString();
  }

  protected String getSecondInsert() {
    StringBuilder builder = new StringBuilder("INSERT INTO ");
    builder.append(getTableName());
    builder.append(" (id, firstname, age, lastname, average) VALUES (2, 'Jane', 31, 'Doe', 15.9)");
    return builder.toString();
  }
}
