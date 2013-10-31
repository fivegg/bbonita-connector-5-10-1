/**
 * Copyright (C) 2009-2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.connectors.webdav.exo;

import java.util.List;
import java.util.logging.Level;

import org.apache.jackrabbit.webdav.client.methods.MkColMethod;
import org.bonitasoft.connectors.webdav.exo.common.ExoConnector;
import org.bonitasoft.connectors.webdav.exo.common.ExoConnectorUtil;
import org.ow2.bonita.connector.core.ConnectorError;

/**
 * @author Jordi Anguela, Yanyan Liu
 */
public class ExoCreateFolder extends ExoConnector {

    private String parentUri;

    private String newFoldersName;

    /**
     * set the parentUri
     * 
     * @param parentUri
     *            the parent URI
     */
    public void setParentUri(String parentUri) {
        this.parentUri = parentUri;
    }

    /**
     * set the newFolderName
     * 
     * @param newFoldersName
     *            the new folder name
     */
    public void setNewFoldersName(String newFoldersName) {
        this.newFoldersName = newFoldersName;
    }

    @Override
    protected void validateFunctionParameters(List<ConnectorError> errors) {
        this.validateUri(errors, parentUri, "parentUri");
        if (!(newFoldersName.length() > 0)) {
            errors.add(new ConnectorError("newFoldersName", new IllegalArgumentException("cannot be empty")));
        }
    }

    @Override
    protected void executeFunction() throws Exception {
        this.createFolder(parentUri, newFoldersName);
    }

    /**
     * create folder in eXo
     * 
     * @param parentUri
     * @param newFoldersName
     * @throws Exception
     */
    public void createFolder(final String parentUri, final String newFoldersName) throws Exception {

        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("createFolder '" + newFoldersName + "' into '" + parentUri + "'");
        }
        // handle special characters for newFolderName
        String folderName = ExoConnectorUtil.encodeSpecialCharacter(newFoldersName);
        folderName = java.net.URLEncoder.encode(folderName, "UTF-8");

        final MkColMethod httpMethod = new MkColMethod(parentUri + folderName);
        client.executeMethod(httpMethod);

        processResponse(httpMethod, true);
        httpMethod.releaseConnection();
    }
}
