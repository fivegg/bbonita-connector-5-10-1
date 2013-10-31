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
package org.bonitasoft.connectors.bonita;

import java.util.ArrayList;
import java.util.List;

import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.ProcessConnector;
import org.ow2.bonita.facade.APIAccessor;
import org.ow2.bonita.facade.IdentityAPI;
import org.ow2.bonita.facade.identity.User;

/**
 * 
 * @author Nicolas Chabanoles
 *
 */
public class GetUser extends ProcessConnector {

  private String username;
  private User user;

  public void setUsername(String username) {
    this.username = username;
  }

  public User getUser() {
    return user;
  }

  @Override
  protected void executeConnector() throws Exception {
    final APIAccessor accessor = getApiAccessor();
    final IdentityAPI identityAPI = accessor.getIdentityAPI();
    user = identityAPI.findUserByUserName(username);
  }

  @Override
  protected List<ConnectorError> validateValues() {
    List<ConnectorError> errors = null;
    if (username.length() == 0) {
      errors = new ArrayList<ConnectorError>();
      errors.add(new ConnectorError("username", new IllegalArgumentException("is empty")));
    }
    return errors;
  }

}
