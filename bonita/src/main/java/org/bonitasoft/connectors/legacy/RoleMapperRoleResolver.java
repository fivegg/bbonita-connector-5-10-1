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
package org.bonitasoft.connectors.legacy;

import java.util.Set;

import org.ow2.bonita.connector.core.RoleResolver;
import org.ow2.bonita.definition.RoleMapper;
import org.ow2.bonita.facade.QueryAPIAccessor;
import org.ow2.bonita.facade.impl.StandardQueryAPIAccessorImpl;

/**
 * 
 * @author Mickael Istria, Matthieu Chaffotte
 *
 */
public class RoleMapperRoleResolver extends RoleResolver {

  private String className;

  @Override @SuppressWarnings("unchecked")
  protected Set<String> getMembersSet(String roleId) throws Exception {
    Class<? extends RoleMapper> mapperClass = (Class<? extends RoleMapper>)
    Class.forName(className, true, RoleMapperRoleResolver.class.getClassLoader()); 
    RoleMapper mapper = mapperClass.newInstance();
    final QueryAPIAccessor queryAccessor = new StandardQueryAPIAccessorImpl();
    return mapper.searchMembers(queryAccessor, getProcessInstanceUUID(), roleId);
  }

  public void setClassName(String className) {
    this.className = className;
  }

}
