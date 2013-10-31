/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
import org.ow2.bonita.facade.QueryRuntimeAPI;
import org.ow2.bonita.facade.RuntimeAPI;
import org.ow2.bonita.facade.exception.DocumentNotFoundException;
import org.ow2.bonita.facade.runtime.Document;
import org.ow2.bonita.facade.uuid.DocumentUUID;

/**
 * @author Yanyan Liu
 * 
 */
public class AddDocumentVersion extends ProcessConnector {
    private Document document;
    private String content;
    private String fileName;
    private boolean isMajorVersion;
    private String documentUUID;

    @Override
    protected void executeConnector() throws Exception {
        final APIAccessor accessor = getApiAccessor();
        final RuntimeAPI runtimeAPI = accessor.getRuntimeAPI();
        final DocumentUUID docUUID = new DocumentUUID(documentUUID);
        final Document doc = accessor.getQueryRuntimeAPI().getDocument(docUUID);
        String type = null;
        if (doc.getContentMimeType() == null) {
            type = "text/plain"; // set "text/plain" as default type for null content document
        } else {
            type = doc.getContentMimeType();
        }
        byte[] data = null;
        if (content != null) {
            data = content.getBytes();
        }
        document = runtimeAPI.addDocumentVersion(new DocumentUUID(documentUUID), isMajorVersion, fileName, type, data);
    }

    @Override
    protected List<ConnectorError> validateValues() {
        final List<ConnectorError> errors = new ArrayList<ConnectorError>();
        ConnectorError error = null;
        final QueryRuntimeAPI queryTimeAPI = getApiAccessor().getQueryRuntimeAPI();
        try {
            queryTimeAPI.getDocument(new DocumentUUID(documentUUID));
        } catch (final DocumentNotFoundException e) {
            error = new ConnectorError("document not found with UUID " + documentUUID, e);
            errors.add(error);
        }
        return errors;
    }

    /**
     * get document
     * 
     * @return
     */
    public Object getDocument() {
        return document;
    }

    /**
     * set content
     * 
     * @param content
     */
    public void setContent(final String content) {
        this.content = content;
    }

    /**
     * set file name
     * 
     * @param fileName
     */
    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    /**
     * set is major version
     * 
     * @param isMajorVersion
     */
    public void setIsMajorVersion(final boolean isMajorVersion) {
        this.isMajorVersion = isMajorVersion;
    }

    /**
     * set document UUID
     * 
     * @param documentUUID
     */
    public void setDocumentUUID(final String documentUUID) {
        this.documentUUID = documentUUID;
    }
}
