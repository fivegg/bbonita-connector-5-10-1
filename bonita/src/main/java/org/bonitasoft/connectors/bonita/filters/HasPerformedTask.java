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
package org.bonitasoft.connectors.bonita.filters;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.Filter;
import org.ow2.bonita.facade.APIAccessor;
import org.ow2.bonita.facade.QueryRuntimeAPI;
import org.ow2.bonita.light.LightTaskInstance;
import org.ow2.bonita.util.AccessorUtil;

/**
 * 
 * @author Romain Bioteau, Matthieu Chaffotte
 *
 */
public class HasPerformedTask extends Filter {

  private Set<String> taskNames;

  public void setTaskNames(Set<String> taskNames) {
    this.taskNames = taskNames;
  }

  public void setTaskNames(Collection<String> taskNames) {
    this.taskNames = new HashSet<String>(taskNames);
  }

  @Override
  protected Set<String> getCandidates(Set<String> members) throws Exception {
    final APIAccessor accessor = getApiAccessor();
    final QueryRuntimeAPI queryRuntimeAPI = accessor.getQueryRuntimeAPI(AccessorUtil.QUERYLIST_JOURNAL_KEY);
    Set<LightTaskInstance> tasks = queryRuntimeAPI.getLightTasks(getProcessInstanceUUID(), taskNames);
    final Set<String> result = new HashSet<String>();
    for (String member : members) {
      if (hasPerformedAllTasks(member, tasks)) {
        result.add(member);
      }
    }
    return result;
  }

  private boolean hasPerformedAllTasks(String user, Set<LightTaskInstance> tasks) {
    boolean hasPerfomedAllTasks = true;
    Iterator<LightTaskInstance> iterator = tasks.iterator();
    while(hasPerfomedAllTasks && iterator.hasNext()) {
      LightTaskInstance task = iterator.next();
      if (!user.equals(task.getEndedBy())) {
        hasPerfomedAllTasks = false;
      }
    }
    return hasPerfomedAllTasks;
  }

  @Override
  protected List<ConnectorError> validateValues() {
    List<ConnectorError> errors = super.validateValues();
    if (taskNames.isEmpty()) {
      ConnectorError error = new ConnectorError("taskNames", new IllegalArgumentException("No task is defined"));
      errors.add(error);
    }
    return errors;
  }

}
