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
package org.bonitasoft.connectors.bonita.joincheckers;

import java.util.List;
import java.util.Set;

import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.MultipleInstancesJoinChecker;
import org.ow2.bonita.facade.APIAccessor;
import org.ow2.bonita.facade.QueryRuntimeAPI;
import org.ow2.bonita.facade.runtime.ActivityState;
import org.ow2.bonita.light.LightActivityInstance;
import org.ow2.bonita.util.AccessorUtil;

/**
 * 
 * @author Matthieu Chaffotte
 *
 */
public class FixedNumberJoinChecker extends MultipleInstancesJoinChecker {

  private int activityNumber;

  public void setActivityNumber(int number) {
    this.activityNumber = number;
  }
  
  public void setActivityNumber(long number) {
    this.activityNumber = (int) number;
  }

  @Override
  protected boolean isJoinOK() throws Exception {
    final APIAccessor accessor = getApiAccessor();
    final QueryRuntimeAPI queryRuntimeAPI = accessor.getQueryRuntimeAPI(AccessorUtil.QUERYLIST_JOURNAL_KEY);
    final Set<LightActivityInstance> activities = queryRuntimeAPI.getLightActivityInstances(getProcessInstanceUUID(), getActivityName(), getIterationId());
    int finishedActivities = 0;
    for (LightActivityInstance activityInstance : activities) {
      if (ActivityState.FINISHED.equals(activityInstance.getState())) {
        finishedActivities ++;
      }
    }
    return activityNumber <= finishedActivities;
  }

  @Override
  protected List<ConnectorError> validateValues() {
    List<ConnectorError> errors = super.validateValues();
    if (activityNumber < 0) {
      ConnectorError error = new ConnectorError("activityNumber", new IllegalArgumentException("The number of activities cannot be less than 0"));
      errors.add(error);
    }
    return errors;
  }

}
