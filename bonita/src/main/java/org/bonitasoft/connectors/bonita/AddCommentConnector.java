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
package org.bonitasoft.connectors.bonita;

import java.util.ArrayList;
import java.util.List;

import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.ProcessConnector;
import org.ow2.bonita.facade.APIAccessor;
import org.ow2.bonita.facade.ManagementAPI;
import org.ow2.bonita.facade.RuntimeAPI;
import org.ow2.bonita.facade.uuid.ActivityInstanceUUID;

/**
 * 
 * @author Matthieu Chaffotte
 *
 */
public class AddCommentConnector extends ProcessConnector {

	private String message;
	private boolean onActivity;

	public void setMessage(String message) {
		this.message = message;
	}

	public void setOnActivity(boolean onActivity) {
		this.onActivity = onActivity;
	}

	@Override
	protected void executeConnector() throws Exception {
		ActivityInstanceUUID activityUUID = null;
		if (onActivity) {
			activityUUID = getActivityInstanceUUID();
		}
		final APIAccessor accessor = getApiAccessor();
    final RuntimeAPI runtimeAPI = accessor.getRuntimeAPI();
    final ManagementAPI managementAPI = accessor.getManagementAPI();
    final String userId = managementAPI.getLoggedUser();
		if (onActivity) {
		  runtimeAPI.addComment(activityUUID, message, userId);
		} else {
		  runtimeAPI.addComment(getProcessInstanceUUID(), message, userId);
		}
	}

	@Override
	protected List<ConnectorError> validateValues() {
		final List<ConnectorError> errors = new ArrayList<ConnectorError>();
		if ("".equals(message.trim())) {
		  final ConnectorError error = new ConnectorError("message", new IllegalArgumentException("is empty"));
			errors.add(error);
		}
		return errors;
	}
}
