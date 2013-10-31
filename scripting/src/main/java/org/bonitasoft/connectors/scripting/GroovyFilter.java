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
package org.bonitasoft.connectors.scripting;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.ow2.bonita.connector.core.Filter;
import org.ow2.bonita.facade.APIAccessor;
import org.ow2.bonita.facade.RuntimeAPI;

/**
 * 
 * @author Romain Bioteau
 *
 */
public class GroovyFilter extends Filter {

  private String script;

  @Override @SuppressWarnings({ "unchecked", "rawtypes" })
  protected Set<String> getCandidates(Set<String> arg0) throws Exception {
    Object result = new Object(); 
    Map<String,Object> context = new HashMap<String, Object>();
    context.put("candidates", arg0);
    final APIAccessor accessor = getApiAccessor();
    final RuntimeAPI runtimeAPI = accessor.getRuntimeAPI();
    if (getActivityInstanceUUID() != null) {
      result = runtimeAPI.evaluateGroovyExpression("${"+script+"}", getActivityInstanceUUID(), context, true, false);
    } else {
      result = runtimeAPI.evaluateGroovyExpression("${"+script+"}", getProcessInstanceUUID(), context, true);
    }
    if(result instanceof Set<?>) {
      return (Set<String>) result;
    }
    Set<String> resultSet = new HashSet<String>();
    if (result instanceof String) {
      resultSet.add((String) result);
      return resultSet;
    }
    if (result instanceof Collection) {
      resultSet.addAll((Collection) result);
      return resultSet;
    }
    return Collections.emptySet();
  }

  public void setScript(String script) {
    this.script = script;
  }

}
