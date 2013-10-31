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
package org.bonitasoft.connectors.bonita.resolvers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.ow2.bonita.connector.core.RoleResolver;
import org.ow2.bonita.facade.APIAccessor;
import org.ow2.bonita.facade.RuntimeAPI;
import org.ow2.bonita.util.BonitaRuntimeException;
import org.ow2.bonita.util.GroovyExpression;

/**
 * 
 * @author Matthieu Chaffotte
 * 
 */
public class UserListRoleResolver extends RoleResolver {

  private String users;

  public String getUsers() {
    return users;
  }

  public void setUsers(final String users) {
    this.users = users;
  }

  public void setUsers(final Collection<String> users) {
    final StringBuilder builder = new StringBuilder();
    for (final String user : users) {
      builder.append(user).append(",");
    }
    final String tmp = builder.toString();
    this.users = tmp.substring(0, tmp.length() - 1);
  }

  private Set<String> setMembers(final String members) {
    final String[] tempMembers = members.split(",");
    final Set<String> membersSet = new HashSet<String>();
    for (int i = 0; i < tempMembers.length; i++) {
      membersSet.add(tempMembers[i].trim());
    }
    return membersSet;
  }

  @Override
  @SuppressWarnings("unchecked")
  protected Set<String> getMembersSet(final String roleId) throws Exception {
    if (GroovyExpression.isGroovyExpression(getUsers())) {
      final APIAccessor accessor = getApiAccessor();
      final RuntimeAPI runtimeAPI = accessor.getRuntimeAPI();
      final Object people = runtimeAPI.evaluateGroovyExpression(getUsers(), getProcessInstanceUUID(), true);
      if (people instanceof Collection<?>) {
        return new HashSet<String>((Collection<String>) people);
      } else if (people instanceof String) {
        return setMembers((String) people);
      } else {
        throw new BonitaRuntimeException("The Groovy expression returns neither a Collection nor a String.");
      }
    } else {
      return setMembers(getUsers());
    }
  }
}
