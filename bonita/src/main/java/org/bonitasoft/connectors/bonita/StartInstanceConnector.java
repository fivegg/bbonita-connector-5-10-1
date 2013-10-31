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

import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.ProcessConnector;
import org.ow2.bonita.facade.APIAccessor;
import org.ow2.bonita.facade.QueryDefinitionAPI;
import org.ow2.bonita.facade.RuntimeAPI;
import org.ow2.bonita.facade.exception.DataFieldNotFoundException;
import org.ow2.bonita.facade.exception.ProcessNotFoundException;
import org.ow2.bonita.facade.uuid.ProcessDefinitionUUID;
import org.ow2.bonita.facade.uuid.ProcessInstanceUUID;
import org.ow2.bonita.util.AccessorUtil;

/**
 * @author Anthony Birembaut
 *
 */
public class StartInstanceConnector extends ProcessConnector {

  private List<List<Object>> processVariables;
  private String processVersion;
  private String processName;

  private ProcessDefinitionUUID processToStartDefinitionUUID;
  private Map<String, Object> variablesMap = new HashMap<String, Object>();
  private ProcessInstanceUUID createdProcessInstanceUUID;

  public void setProcessVariables(List<List<Object>> processVariables) {
    this.processVariables = processVariables;
  }

  public void setProcessVersion(String processVersion) {
    this.processVersion = processVersion;
  }

  public void setProcessName(String processName) {
    this.processName = processName;
  }

  public String getCreatedProcessInstanceUUID() {
    if (createdProcessInstanceUUID != null) {
      return createdProcessInstanceUUID.getValue();
    } else {
      return null;
    }
  }

  @Override
  protected void executeConnector() throws Exception {
    final APIAccessor accessor = getApiAccessor();
    final QueryDefinitionAPI queryDefinitionAPI = accessor.getQueryDefinitionAPI(AccessorUtil.QUERYLIST_JOURNAL_KEY);
    if(processVersion != null && processVersion.length() > 0) {
      try {
        processToStartDefinitionUUID = new ProcessDefinitionUUID(processName, processVersion);
        queryDefinitionAPI.getLightProcess(processToStartDefinitionUUID);
      } catch (Exception e) {
        throw new ProcessNotFoundException("PNFE1", processName, processVersion);
      }
    } else {
      try {
        processToStartDefinitionUUID = queryDefinitionAPI.getLastLightProcess(processName).getUUID();
      } catch (Exception e) {
        throw new ProcessNotFoundException("PNFE1", processName);
      }
    }
    if(processVariables != null && processVariables.size() > 0) {
      for (List<Object> processVariablesRow : processVariables) {
        final String variableName = (String) processVariablesRow.get(0);
        if (variableName != null && variableName.length() > 0) {
          try {
            queryDefinitionAPI.getProcessDataField(processToStartDefinitionUUID, variableName);
            final Object variableValue = processVariablesRow.get(1);
            if (variableValue != null){
              variablesMap.put(variableName, variableValue);
            }
          } catch (Exception e) {
            throw new DataFieldNotFoundException("DFNFE1", variableName, processToStartDefinitionUUID);
          }
        }
      }
    }
    if (processToStartDefinitionUUID != null) {
      RuntimeAPI runtimeAPI = accessor.getRuntimeAPI();
      createdProcessInstanceUUID = runtimeAPI.instantiateProcess(processToStartDefinitionUUID, variablesMap);
    } else {
      throw new ProcessNotFoundException("PNFE1", processName);
    }
  }

  @Override
  protected List<ConnectorError> validateValues() {
    final List<ConnectorError> connectorErrors = new ArrayList<ConnectorError>();
    final QueryDefinitionAPI queryDefinitionAPI = getApiAccessor().getQueryDefinitionAPI(AccessorUtil.QUERYLIST_JOURNAL_KEY);
    if(processVersion != null && processVersion.length() > 0) {
      try {
        processToStartDefinitionUUID = new ProcessDefinitionUUID(processName, processVersion);
        queryDefinitionAPI.getLightProcess(processToStartDefinitionUUID);
      } catch (Exception e) {
        connectorErrors.add(new ConnectorError("processVersion", e));
        return connectorErrors;
      }
    } else {
      try {
        processToStartDefinitionUUID = queryDefinitionAPI.getLastLightProcess(processName).getUUID();
      } catch (Exception e) {
        connectorErrors.add(new ConnectorError("processName", e));
        return connectorErrors;
      }
    }
    if(processVariables != null && processVariables.size() > 0) {
      for (List<Object> processVariablesRow : processVariables) {
        final String variableName = (String) processVariablesRow.get(0);
        if (variableName != null && variableName.length() > 0) {
          try {
            queryDefinitionAPI.getProcessDataField(processToStartDefinitionUUID, variableName);
            final Object variableValue = processVariablesRow.get(1);
            if (variableValue != null){
              variablesMap.put(variableName, variableValue);
            }
          } catch (Exception e) {
            connectorErrors.add(new ConnectorError("processVariables", e));
          }
        }
      }
    }
    return connectorErrors;
  }

}
