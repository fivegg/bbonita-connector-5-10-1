/**
 * Copyright (C) 2009-2010 BonitaSoft S.A.
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

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.ow2.bonita.connector.core.Connector;
import org.ow2.bonita.connector.core.ConnectorError;

/**
 * 
 * @author Matthieu Chaffotte
 *
 */
public abstract class DatabaseConnector extends Connector {

  private String query;
  private String username;
  private String password;
  private RowSet rowSet;

  public String getQuery() {
    return query;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public RowSet getRowSet() {
    return rowSet;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    if (password != null && "".equals(password)) {
      this.password = null;
    } else {
      this.password = password;
    }
  }

  @Override
  protected void executeConnector() throws Exception {
    Database database = new Database(this.getDriver());
    try {
      database.connect(this.getUrl(), username, password);
      String command = query.toUpperCase().trim();
      if (command.startsWith("SELECT")) {
        ResultSet data = database.select(query);
        rowSet = new RowSet(data);
      } else {
        database.executeCommand(query);
      }
    } finally {
      if (database != null) {
        database.disconnect();
      }
    }
  }

  public abstract String getUrl();

  public abstract String getDriver();

  @Override
  protected List<ConnectorError> validateValues() {
    return new ArrayList<ConnectorError>();
  }

}
