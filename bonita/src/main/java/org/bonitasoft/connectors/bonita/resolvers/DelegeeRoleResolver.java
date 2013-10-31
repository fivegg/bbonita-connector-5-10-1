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

import java.util.HashSet;
import java.util.Set;

import org.ow2.bonita.connector.core.RoleResolver;
import org.ow2.bonita.facade.APIAccessor;
import org.ow2.bonita.facade.IdentityAPI;
import org.ow2.bonita.facade.identity.User;

/**
 * 
 * @author Matthieu Chaffotte
 *
 */
public class DelegeeRoleResolver extends RoleResolver {

  private String userName;

  public void setUserName(String userName) {
    this.userName = userName;
  }

  @Override
  protected Set<String> getMembersSet(String roleId) throws Exception {
    final APIAccessor accessor = getApiAccessor();
    IdentityAPI identityAPI = accessor.getIdentityAPI();    
    User user = identityAPI.findUserByUserName(userName);
    String delegeeUUID = user.getDelegeeUUID();
    Set<String> members = new HashSet<String>();
    if (delegeeUUID != null) {
      User delegee = identityAPI.getUserByUUID(delegeeUUID);
      members.add(delegee.getUsername());
    }
    return members;
  }

}
