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

import java.util.HashMap;
import java.util.List;

import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.bonitasoft.connectors.cmisclient.common.AbstractCmisConnector;
import org.ow2.bonita.connector.core.ConnectorError;

/**
 * 
 * @author Frederic Bouquet
 * 
 */
public class CreateSubfolder extends AbstractCmisConnector {

	private String parentFolderPath;
	private String subfolderName;

	@Override
	protected void executeConnector() throws Exception {
		createSubFolderByPath(username, password, url, binding_type,
				repositoryName, parentFolderPath, subfolderName);
	}

	@Override
	protected List<ConnectorError> validateValues() {
		return null;
	}

	public void setParentFolderPath(final String parentFolderPath) {
		this.parentFolderPath = parentFolderPath;
	}

	public void setSubfolderName(final String subfolderName) {
		this.subfolderName = subfolderName;
	}

	protected void createSubFolderByPath(final String username,
			final String password, final String url, final String binding,
			final String repositoryName, final String path,
			final String folderName) {
		final Session s = createSessionByName(username, password, url, binding,
				repositoryName);
		final Folder folder = (Folder) s.getObjectByPath(path);

		final HashMap<String, Object> properties = new HashMap<String, Object>();
		properties.put(PropertyIds.NAME, folderName);
		properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
		folder.createFolder(properties, null, null, null, s.getDefaultContext());
	}
}
