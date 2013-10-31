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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.ProcessConnector;
import org.ow2.bonita.facade.APIAccessor;
import org.ow2.bonita.facade.RuntimeAPI;

/**
 * 
 * @author Matthieu Chaffotte
 *
 */
public class SetVarsConnector extends ProcessConnector {

  private Map<String, Object> variables;

  public void setVariables(final Map<String, Object> variables) {
    if (variables == null) {
      this.variables = new HashMap<String, Object>();
    } else {
      this.variables = variables;
    }
  }

  public void setVariables(final List<List<Object>> variables) {
    setVariables(bonitaListToMap(variables, String.class, Object.class));
  }

  @Override
  protected void executeConnector() throws Exception {
    final APIAccessor accessor = getApiAccessor();
    final RuntimeAPI runtimeAPI = accessor.getRuntimeAPI();
    for (Entry<String, Object> variable : variables.entrySet()) {
      final String variableName = variable.getKey();
      final Object variableValue = variable.getValue();
      if(getActivityInstanceUUID() != null) {
        runtimeAPI.setVariable(getActivityInstanceUUID(), variableName, variableValue);
      } else {
        runtimeAPI.setProcessInstanceVariable(getProcessInstanceUUID(), variableName, variableValue);
      }
    }
  }

  @Override
  protected List<ConnectorError> validateValues() {
    final List<ConnectorError> errors = new ArrayList<ConnectorError>();
    ConnectorError error = null;
    if (variables != null) {
      for (Entry<String, Object> variable : variables.entrySet()) {
        String variableName = variable.getKey();
        if (variableName == null) {
          error = new ConnectorError("variables", new IllegalArgumentException("A variable name is null"));
        } else if("".equals(variableName.trim())) {
          error = new ConnectorError("variables", new IllegalArgumentException("A variable name is empty"));
          errors.add(error);
        }
      }
    }
    return errors;
  }

}