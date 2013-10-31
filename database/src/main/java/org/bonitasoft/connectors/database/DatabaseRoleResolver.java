/**
 * Copyright (C) 2010 BonitaSoft S.A.
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.RoleResolver;

/**
 * 
 * @author Matthieu Chaffotte
 * 
 */
public abstract class DatabaseRoleResolver extends RoleResolver {

  private String query;
  private String username;
  private String password;

  @Override
  protected List<ConnectorError> validateValues() {
    final List<ConnectorError> errors = super.validateValues();
    final String select = query.trim().toLowerCase();
    ConnectorError error = null;
    if (select.length() == 0) {
      error = new ConnectorError("query", new IllegalArgumentException("is empty"));
    } else if (!select.startsWith("select")) {
      error = new ConnectorError("query", new IllegalArgumentException("must begin with SELECT"));
    } else {
      final int fromIndex = select.indexOf("from");
      if (fromIndex == 8) {
        error = new ConnectorError("query", new IllegalArgumentException("must contain a column in the selection"));
      }
    }
    if (error != null) {
      errors.add(error);
    }
    return errors;
  }

  @Override
  protected Set<String> getMembersSet(final String arg0) throws Exception {
    final Database database = new Database(this.getDriver());
    final Set<String> members = new HashSet<String>();
    try {
      database.connect(this.getUrl(), username, password);
      final ResultSet data = database.select(query);
      if (data != null) {
        while (data.next()) {
          final String member = data.getString(1);
          members.add(member);
        }
      }
    } finally {
      if (database != null) {
        database.disconnect();
      }
    }
    return members;
  }

  public void setQuery(final String query) {
    this.query = query;
  }

  public void setUsername(final String username) {
    this.username = username;
  }

  public void setPassword(final String password) {
    if (password != null && "".equals(password)) {
      this.password = null;
    } else {
      this.password = password;
    }
  }

  public abstract String getUrl();

  public abstract String getDriver();

}
