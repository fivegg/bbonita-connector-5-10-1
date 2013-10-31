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
import java.util.List;

import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.ProcessConnector;
import org.ow2.bonita.facade.APIAccessor;
import org.ow2.bonita.facade.QueryRuntimeAPI;
import org.ow2.bonita.facade.uuid.ProcessInstanceUUID;
import org.ow2.bonita.util.AccessorUtil;

/**
 * 
 * @author Nicolas Chabanoles
 *
 */
public class GetProcessInstanceInitiator extends ProcessConnector {

  private ProcessInstanceUUID instanceUUID;
  private String instanceInitiator;

  public void setInstanceUUID(String instanceUUID) {
    this.instanceUUID = new ProcessInstanceUUID(instanceUUID);
  }

  public void setInstanceUUID(ProcessInstanceUUID instanceUUID) {
    this.instanceUUID = instanceUUID;
  }

  public String getInstanceInitiator() {
    return instanceInitiator;
  }

  @Override
  protected void executeConnector() throws Exception {
    final APIAccessor accessor = getApiAccessor();
    final QueryRuntimeAPI queryRuntimAPI = accessor.getQueryRuntimeAPI(AccessorUtil.QUERYLIST_JOURNAL_KEY);
    instanceInitiator = queryRuntimAPI.getLightProcessInstance(instanceUUID).getStartedBy();
  }

  @Override
  protected List<ConnectorError> validateValues() {
    List<ConnectorError> errors = null;
    if(instanceUUID.getValue().length() == 0) {
      errors = new ArrayList<ConnectorError>();
      errors.add(new ConnectorError("instanceUUID", new IllegalArgumentException("The instance UUID cannot be empty.")));
    }
    return errors;
  }

}
