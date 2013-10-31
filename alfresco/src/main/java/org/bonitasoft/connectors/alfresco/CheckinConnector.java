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
package org.bonitasoft.connectors.alfresco;

import java.util.List;

import org.bonitasoft.connectors.alfresco.common.AlfrescoConnector;
import org.bonitasoft.connectors.alfresco.common.AlfrescoRestClient;
import org.ow2.bonita.connector.core.ConnectorError;

/**
 * 
 * @author Jordi Anguela
 *
 */
public class CheckinConnector extends AlfrescoConnector {

	private String checkedOutFileId;
	private Boolean isMajorVersion;
	private String checkinComments;
	
	public void setCheckedOutFileId(String checkedOutFileId) {
		this.checkedOutFileId = checkedOutFileId;
	}

	public void setIsMajorVersion(Boolean isMajorVersion) {
		this.isMajorVersion = isMajorVersion;
	}

	public void setCheckinComments(String checkinComments) {
		this.checkinComments = checkinComments;
	}

	@Override
	protected void executeFunction(AlfrescoRestClient alfrescoClient) throws Exception {
		response = alfrescoClient.checkin(checkedOutFileId, isMajorVersion, checkinComments);
	}

	@Override
	protected List<ConnectorError> validateFunctionParameters() {
		return null;
	}
}
