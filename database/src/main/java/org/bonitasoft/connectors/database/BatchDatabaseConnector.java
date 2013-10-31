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

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.ProcessConnector;

/**
 * 
 * @author Matthieu Chaffotte
 *
 */
public abstract class BatchDatabaseConnector extends ProcessConnector {

  private String username;
  private String password;

  private String script;
  private String separator;

  public void setScript(String script) {
    this.script = script;
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

  public void setSeparator(String separator) {
    this.separator = separator;
  }

  @Override
  protected void executeConnector() throws Exception {
    List<String> commands = getScriptCommands();
    Database database = new Database(this.getDriver());
    try {
      database.connect(this.getUrl(), username, password);
      database.executeBatch(commands, true);
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
    return null;
  }

  private List<String> getScriptCommands() {
    StringTokenizer tokenizer = new StringTokenizer(script, separator);
    List<String> commands = new ArrayList<String>();
    while (tokenizer.hasMoreTokens()) {
      String command = tokenizer.nextToken();
      commands.add(command.trim());
    }
    return commands;
  }

}
