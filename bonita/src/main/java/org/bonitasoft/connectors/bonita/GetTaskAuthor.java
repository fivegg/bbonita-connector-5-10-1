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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.ProcessConnector;
import org.ow2.bonita.facade.APIAccessor;
import org.ow2.bonita.facade.QueryRuntimeAPI;
import org.ow2.bonita.light.LightActivityInstance;
import org.ow2.bonita.light.LightTaskInstance;
import org.ow2.bonita.util.AccessorUtil;

/**
 * 
 * @author Nicolas Chabanoles
 *
 */
public class GetTaskAuthor extends ProcessConnector {

  private String taskName;
  private String author;

  public void setTaskName(String taskName) {
    this.taskName = taskName;
  }

  public String getAuthor() {
    return author;
  }

  @Override
  protected void executeConnector() throws Exception {
    final APIAccessor accessor = getApiAccessor();
    final QueryRuntimeAPI queryRuntimeAPI = accessor.getQueryRuntimeAPI(AccessorUtil.QUERYLIST_JOURNAL_KEY);
    final Set<LightActivityInstance> activities = queryRuntimeAPI.getLightActivityInstances(getProcessInstanceUUID(), taskName);
    LightTaskInstance task;
    if (activities.size() == 0) {
      // No step found.
      author = null;
    } else if (activities.size() == 1) {
      // Only one step has the given name.
      final LightActivityInstance lightActivityInstance = activities.iterator().next(); 
      if (lightActivityInstance.isTask()){
        task = lightActivityInstance.getTask();
        author = task.getEndedBy();
      }
    } else {
      // More than one step have the same name.
      // This is possible in case of iterations.
      // Only consider the current iteration / the latest iteration.
      final List<LightTaskInstance> orderedActivities = new ArrayList<LightTaskInstance>();
      for (LightActivityInstance lightActivityInstance : activities) {
        if (lightActivityInstance.isTask() && lightActivityInstance.getTask().getEndedBy() != null) {
          orderedActivities.add(lightActivityInstance.getTask());
        }
      }
      Collections.sort(orderedActivities, new Comparator<LightTaskInstance>() {
        public int compare(LightTaskInstance o1, LightTaskInstance o2) {
          return o1.getEndedDate().compareTo(o2.getEndedDate());
        }
      });
      author = orderedActivities.iterator().next().getEndedBy();
    }
  }

  @Override
  protected List<ConnectorError> validateValues() {
    List<ConnectorError> errors = null;
    if (taskName.length() == 0) {
      errors = new ArrayList<ConnectorError>();
      errors.add(new ConnectorError("taskName", new IllegalArgumentException("is empty!")));
    }
    return errors;
  }

}
