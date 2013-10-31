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

import java.util.List;

import org.ow2.bonita.connector.core.ConnectorError;

/**
 * 
 * @author Matthieu Chaffotte
 *
 */
public abstract class RemoteDatabaseRoleResolver extends SimpleRemoteDatabaseRoleResolver {

  private Integer port;

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public void setPort(Long port) {
    this.port = port.intValue();
  }

  @Override
  protected List<ConnectorError> validateValues() {
    List<ConnectorError> errors = super.validateValues();
    if (getPort() != null) {
      if (this.getPort() < 0) {
        errors.add(new ConnectorError("Port",
            new IllegalArgumentException("cannot be less than 0!")));
      } else if (this.getPort() > 65535) {
        errors.add(new ConnectorError("Port",
            new IllegalArgumentException("cannot be greater than 65535!")));
      }
    }
    return errors;
  }

}
