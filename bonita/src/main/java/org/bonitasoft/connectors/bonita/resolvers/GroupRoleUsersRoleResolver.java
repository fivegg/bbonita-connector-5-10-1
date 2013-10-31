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
package org.bonitasoft.connectors.bonita.resolvers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.RoleResolver;
import org.ow2.bonita.facade.APIAccessor;
import org.ow2.bonita.facade.IdentityAPI;
import org.ow2.bonita.facade.identity.Group;
import org.ow2.bonita.facade.identity.Role;
import org.ow2.bonita.facade.identity.User;

/**
 * 
 * @author Matthieu Chaffotte
 *
 */
public class GroupRoleUsersRoleResolver extends RoleResolver {

  private String groupPath;
  private String roleName;

  public void setGroupPath(String groupPath) {
    this.groupPath = groupPath;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }
  
  @Override
  protected List<ConnectorError> validateValues() {
    final List<ConnectorError> connectorErrors = new ArrayList<ConnectorError>();
    if (!groupPath.startsWith("/")) {
      //TODO : i18n of the validation message once it will be supported by the engine 
      connectorErrors.add(new ConnectorError("groupPath", new IllegalArgumentException("The group path should start with '/'")));
    }
    return connectorErrors;
  }

  @Override
  protected Set<String> getMembersSet(String roleId) throws Exception {
    final APIAccessor accessor = getApiAccessor();
    final IdentityAPI identityAPI = accessor.getIdentityAPI();
    final String[] groups = groupPath.split("/");
    List<String> path = new ArrayList<String>();
    int startIndex = 0;
    if (groupPath.startsWith("/")) {
      startIndex++;
    }
    for (int i = startIndex; i < groups.length; i++) {
      path.add(groups[i].trim());
    }
    Group group = identityAPI.getGroupUsingPath(path);
    final Set<String> users = new HashSet<String>();
    if (group == null) {
      return users;
    } else {
      Role role = identityAPI.findRoleByName(roleName);
      List<User> currentUsers = identityAPI.getAllUsersInRoleAndGroup(role.getUUID(), group.getUUID());
      for (User user : currentUsers) {
        users.add(user.getUsername());
      }
      return users;
    }
  }

}
