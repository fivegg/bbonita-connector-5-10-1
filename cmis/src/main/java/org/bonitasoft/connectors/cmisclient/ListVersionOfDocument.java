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

package org.bonitasoft.connectors.cmisclient;

import java.util.ArrayList;
import java.util.List;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.commons.data.ObjectData;
import org.bonitasoft.connectors.cmisclient.common.AbstractCmisConnector;
import org.ow2.bonita.connector.core.ConnectorError;

/**
 * 
 * @author Frederic Bouquet
 * 
 */
public class ListVersionOfDocument extends AbstractCmisConnector {

	private String documentPath;
	private ArrayList<ObjectData> versions;

	@Override
	protected void executeConnector() throws Exception {
		versions = new ArrayList<ObjectData>(listVersionsOfObjectByPath(username, password,
				url, binding_type, repositoryName, documentPath));
	}

	@Override
	protected List<ConnectorError> validateValues() {
		return null;
	}

	public void setDocumentPath(final String documentPath) {
		this.documentPath = documentPath;
	}

	public ArrayList<ObjectData> getVersionsList() {
		return versions;
	}

	protected List<ObjectData> listVersionsOfObjectByPath(
			final String username, final String password, final String url,
			final String bindingType, final String repositoryName,
			final String documentPath) {

		if(session == null){
			session = createSessionByName(username, password, url, bindingType, repositoryName);
		}
		final String repositoryId = session.getRepositoryInfo().getId();

		final CmisObject obj = getObjectByPath(username, password, url,
				bindingType, repositoryName, documentPath);

		final List<ObjectData> versions = session
				.getBinding()
				.getVersioningService()
				.getAllVersions(repositoryId, obj.getId(), null, null, null,
						null);
		return versions;
	}
}
