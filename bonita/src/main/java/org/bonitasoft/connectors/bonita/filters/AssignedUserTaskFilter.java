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
package org.bonitasoft.connectors.bonita.filters;

import java.util.HashSet;
import java.util.Set;

import org.ow2.bonita.connector.core.Filter;
import org.ow2.bonita.facade.APIAccessor;
import org.ow2.bonita.facade.QueryRuntimeAPI;
import org.ow2.bonita.light.LightActivityInstance;
import org.ow2.bonita.light.LightTaskInstance;
import org.ow2.bonita.util.AccessorUtil;

/**
 * 
 * @author Matthieu Chaffotte
 *
 */
public class AssignedUserTaskFilter extends Filter {

	private String activityName;

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	@Override
	protected Set<String> getCandidates(Set<String> members) throws Exception {
		try {
		  final APIAccessor accessor = getApiAccessor();
		  final QueryRuntimeAPI queryRuntimeAPI = accessor.getQueryRuntimeAPI(AccessorUtil.QUERYLIST_JOURNAL_KEY);
			final Set<LightActivityInstance> activityInstances = queryRuntimeAPI.getLightActivityInstances(getProcessInstanceUUID(), activityName);
			for (LightActivityInstance activityInstance : activityInstances) {
				final LightTaskInstance task = activityInstance.getTask();
				if (task != null && task.getTaskUser() != null) {
					final Set<String> users = new HashSet<String>();
					users.add(task.getTaskUser());
					return users;
				}
			}
			return members;
		} catch (Exception e) {
			return members;
		}
	}

}
