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

import java.util.List;

import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.ProcessConnector;
import org.ow2.bonita.facade.APIAccessor;
import org.ow2.bonita.facade.RuntimeAPI;

/**
 * 
 * @author Matthieu Chaffotte
 *
 */
public class GroovyConnector extends ProcessConnector{

  private String script;
  private Object result;

  public Object getResult() {
    return result;
  }

  public void setScript(String script) {
    this.script = script;
  }

  @Override
  protected void executeConnector() throws Exception {
    StringBuilder builder = new StringBuilder("${");
    builder.append(script);
    builder.append("}");
    final APIAccessor accessor = getApiAccessor();
    final RuntimeAPI runtimeAPI = accessor.getRuntimeAPI();
    if (getActivityInstanceUUID() != null) {
      result = runtimeAPI.evaluateGroovyExpression(builder.toString(), getActivityInstanceUUID(), false, true);
    } else {
      result = runtimeAPI.evaluateGroovyExpression(builder.toString(), getProcessInstanceUUID(), true);
    }
  }

  @Override
  protected List<ConnectorError> validateValues() {
    return null;
  }

}
