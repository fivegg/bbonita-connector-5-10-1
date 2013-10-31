/**
 * Copyright (C) 2009 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.connectors.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * 
 * @author Matthieu Chaffotte
 *
 */
public class Database {

  private Connection connection;

  public Database(String driver) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    Class.forName(driver, true, Thread.currentThread().getContextClassLoader()).newInstance();
  }

  public void connect(String url, String username, String password) throws SQLException {
    connection = DriverManager.getConnection(url, username, password);
  }

  public void disconnect() throws SQLException {
    if (connection != null && !connection.isClosed()) {
      connection.close();
    }
  }

  public ResultSet select(String query) throws SQLException {
    Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
    return statement.executeQuery(query);
  }

  public int executeCommand(String command) throws SQLException {
    Statement statement = connection.createStatement(
        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
    return statement.executeUpdate(command);
  }

  public void executeBatch(List<String> commands, boolean commit) throws SQLException {
    connection.setAutoCommit(false);
    Statement statement = connection.createStatement();
    for (String command : commands) {
      statement.addBatch(command);
    }
    statement.executeBatch();
    if (commit) {
      connection.commit();
    }
  }

}
