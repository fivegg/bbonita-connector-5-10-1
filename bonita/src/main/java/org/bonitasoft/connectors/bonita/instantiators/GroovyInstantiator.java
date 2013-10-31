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
package org.bonitasoft.connectors.bonita.instantiators;

import java.util.List;
import java.util.Map;

import org.ow2.bonita.connector.core.MultipleInstancesInstantiator;
import org.ow2.bonita.facade.APIAccessor;
import org.ow2.bonita.facade.RuntimeAPI;

/**
 * 
 * @author Matthieu Chaffotte
 *
 */
public class GroovyInstantiator extends MultipleInstancesInstantiator {

  private String script;

  public void setScript(String script) {
    this.script = script;
  }

  @Override @SuppressWarnings("unchecked")
  protected List<Map<String, Object>> defineActivitiesContext() throws Exception {
    final StringBuilder builder = new StringBuilder("${");
    builder.append(script);
    builder.append("}");
    final APIAccessor accessor = getApiAccessor();
    final RuntimeAPI runtimeAPI = accessor.getRuntimeAPI();
    return (List<Map<String, Object>>) runtimeAPI.evaluateGroovyExpression(builder.toString(), getProcessInstanceUUID(), false);
  }

}
