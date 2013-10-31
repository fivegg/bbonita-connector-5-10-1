/**
 * Copyright (C) 2009-2011 BonitaSoft S.A.
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
package org.bonitasoft.connectors.scripting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ow2.bonita.connector.core.ConnectorError;

/**
 * 
 * @author Matthieu Chaffotte, Yanyan Liu
 * 
 */
public class ShellConnector extends AbstractShellConnector {

  private String shell;

  public void setShell(final String shell) {
    this.shell = shell;
  }

  @Override
  protected List<ConnectorError> validateValues() {
    final List<ConnectorError> errors = new ArrayList<ConnectorError>();
    if ("".equals(shell.trim())) {
      final ConnectorError error = new ConnectorError("shell", new IllegalArgumentException("is empty"));
      errors.add(error);
    }
    return errors;
  }

  @Override
  protected Process executeShellCommand() throws IOException {
    final Runtime rt = Runtime.getRuntime();
    return rt.exec(shell);
  }

}
