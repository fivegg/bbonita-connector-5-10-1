/**
 * Copyright (C) 2009-2011 BonitaSoft S.A.
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
import org.ow2.bonita.facade.runtime.AttachmentInstance;
import org.ow2.bonita.facade.runtime.Document;

/**
 * 
 * @author Jordi Anguela
 * 
 */
public class UploadFileConnector extends AlfrescoConnector {

    private Object fileObject;
    private String fileName;
    private String description;
    private String mimeType;
    private String destinationFolder;

    public void setFileObject(Object fileObject) {
        this.fileObject = fileObject;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void setDestinationFolder(String destinationFolder) {
        this.destinationFolder = destinationFolder;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    protected List<ConnectorError> validateFunctionParameters() {
        return null;
    }

    @Override
    protected void executeFunction(AlfrescoRestClient alfrescoClient) throws Exception {
        if (fileObject instanceof String)
            response = alfrescoClient.uploadFileByPath((String) fileObject, fileName, description, mimeType, destinationFolder);
        else if (fileObject instanceof AttachmentInstance)
            response = alfrescoClient.uploadFileFromAttachment((AttachmentInstance) fileObject, fileName, description, mimeType, destinationFolder);
        else if (fileObject instanceof Document)
            response = alfrescoClient.uploadFileFromDocument((Document) fileObject, fileName, description, mimeType, destinationFolder);
        else
            throw new Exception("Unsupported class for file upload: " + fileObject.getClass().getName() + ". Supported classes: String, AttachmentInstance, Document");
    }

}
