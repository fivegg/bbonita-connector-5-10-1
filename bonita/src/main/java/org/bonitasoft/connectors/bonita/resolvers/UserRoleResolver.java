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
import java.util.List;
import java.util.Set;

import org.ow2.bonita.connector.core.RoleResolver;
import org.ow2.bonita.facade.APIAccessor;
import org.ow2.bonita.facade.IdentityAPI;
import org.ow2.bonita.facade.identity.Role;
import org.ow2.bonita.facade.identity.User;

/**
 * 
 * @author Baptiste Mesta, Matthieu Chaffotte
 *
 */
public class UserRoleResolver extends RoleResolver {

	private String name;

	@Override
	protected Set<String> getMembersSet(String roleId) throws Exception {
		final Set<String> users = new HashSet<String>();
		final APIAccessor accessor = getApiAccessor();
    final IdentityAPI identityAPI = accessor.getIdentityAPI();
    final Role role = identityAPI.findRoleByName(name);
    List<User> userList = identityAPI.getAllUsersInRole(role.getUUID());
    for (User user : userList) {
			users.add(user.getUsername());
		}
		return users;
	}

	public void setName(String name) {
		this.name = name;
	}

}
