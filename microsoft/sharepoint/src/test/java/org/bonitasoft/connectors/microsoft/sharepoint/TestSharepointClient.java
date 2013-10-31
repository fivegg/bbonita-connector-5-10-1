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

import java.util.List;

import org.ow2.bonita.connector.core.ConnectorError;

/**
 * @author Jordi Anguela
 */
public class TestSharepointClient {

	public static void main(String[] args) {
	    try {
	        //Authentication parameters
	    	String username = "username";
			String password = "password";
			String fileUrl  = "https://your_host.sharepoint.emea.microsoftonline.com/Folder/File";
			
			SharepointCheckin connector = new SharepointCheckin();
			
			connector.setUsername(username);
			connector.setPassword(password);
			connector.setFileUrl(fileUrl);

			List<ConnectorError> errors = connector.validateValues();
			if (errors == null || errors.isEmpty()) {
				connector.executeConnector();
				System.out.println("Succeed = " + connector.getSucceed());
			}

	    } catch (Exception ex) {
	        System.err.println(ex);
	    }
	}


}
