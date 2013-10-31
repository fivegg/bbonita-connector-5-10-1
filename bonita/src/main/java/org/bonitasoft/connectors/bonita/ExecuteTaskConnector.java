/**
 * Copyright (C) 2009-2010 BonitaSoft S.A.
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

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.ProcessConnector;
import org.ow2.bonita.facade.APIAccessor;
import org.ow2.bonita.facade.QueryRuntimeAPI;
import org.ow2.bonita.facade.RuntimeAPI;
import org.ow2.bonita.facade.runtime.ActivityState;
import org.ow2.bonita.light.LightActivityInstance;
import org.ow2.bonita.util.AccessorUtil;

/**
 * 
 * @author Matthieu Chaffotte
 *
 */
public class ExecuteTaskConnector extends ProcessConnector {

  private String activityName;

  public void setActivityName(String activityName) {
    this.activityName = activityName;
  }

  @Override
  protected void executeConnector() throws Exception {
    final APIAccessor accessor = getApiAccessor();
    final RuntimeAPI runtimeAPI = accessor.getRuntimeAPI();
    if (activityName != null && activityName.length() > 0) {
      final QueryRuntimeAPI queryRuntimeAPI = accessor.getQueryRuntimeAPI(AccessorUtil.QUERYLIST_JOURNAL_KEY);
      final Set<LightActivityInstance> activities = queryRuntimeAPI.getLightActivityInstances(getProcessInstanceUUID(), activityName);
      final Iterator<LightActivityInstance> iter = activities.iterator();
      while (iter.hasNext()) {
        LightActivityInstance activity = iter.next();
        if (ActivityState.READY.equals(activity.getState())) {
          runtimeAPI.executeTask(activity.getUUID(), true);
        }
      }
    } else {
      runtimeAPI.executeTask(getActivityInstanceUUID(), true);
    }
  }

  @Override
  protected List<ConnectorError> validateValues() {
    return null;
  }
}
