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

import java.util.List;

import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.bonitasoft.connectors.cmisclient.common.AbstractCmisConnector;
import org.ow2.bonita.connector.core.ConnectorError;

/**
 * 
 * @author Frederic Bouquet
 * 
 */
public class CheckIfObjectExist extends AbstractCmisConnector {
	private String objectPath;
	private Boolean exists;

	@Override
	protected void executeConnector() throws Exception {
		exists = checkIfObjectExists(username, password, url, binding_type,
				repositoryName, objectPath);
	}

	@Override
	protected List<ConnectorError> validateValues() {
		return null;
	}

	public void setObjectPath(final String objectPath) {
		this.objectPath = objectPath;
	}

	public Boolean getObjectExists() {
		return exists;
	}

	protected boolean checkIfObjectExists(final String username,
			final String password, final String url, final String bindingType,
			final String repositoryName, final String path) {
		try {
			final Session s = this.createSessionByName(username, password, url,
					bindingType, repositoryName);
			s.getObjectByPath(path);
		} catch (final CmisObjectNotFoundException e) {
			return false;
		}
		return true;
	}

}
