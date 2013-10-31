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
package org.bonitasoft.connectors.microsoft.sharepoint;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.connectors.microsoft.sharepoint.common.SharepointClient;
import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.ProcessConnector;

import com.microsoft.schemas.sharepoint.soap.ListsSoap;

/**
 * @author Jordi Anguela
 */
public class SharepointCheckout extends ProcessConnector {

	private String username;
	private String password;
	private String fileUrl;
	private boolean succeed = false;
	
	@Override
	protected List<ConnectorError> validateValues() {
		List<ConnectorError> errors = new ArrayList<ConnectorError>();
		if (username == null || username.length() <= 0 ) {
			errors.add(new ConnectorError("username", new IllegalArgumentException("cannot be empty!")));
		}
		if (password == null || password.length() <= 0 ) {
			errors.add(new ConnectorError("password", new IllegalArgumentException("cannot be empty!")));
		}
		if (fileUrl == null || fileUrl.length() <= 0 ) {
			errors.add(new ConnectorError("fileUrl", new IllegalArgumentException("cannot be empty!")));
		}
		return errors;
	}

	@Override
	protected void executeConnector() throws Exception {
		SharepointClient client = new SharepointClient();
		
        // Open the SOAP port of the Lists Web Service
        ListsSoap listPort = client.sharePointListsAuth(username, password);
        
        succeed = client.checkOutFile(listPort, fileUrl);
	}
	
	/**
	 * Setter for input argument 'username'
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Setter for input argument 'password'
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Setter for input argument 'fileUrl'
	 */
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	/**
	 * Getter for output argument 'succeed'
	 */
	public Boolean getSucceed() {
		return succeed;
	}
}