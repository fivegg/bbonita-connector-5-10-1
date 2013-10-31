/**
 * Copyright (C) 2010-2011 BonitaSoft S.A.
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.activation.MimetypesFileTypeMap;

import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.ProcessConnector;
import org.ow2.bonita.facade.RuntimeAPI;
import org.ow2.bonita.facade.exception.DocumentationCreationException;
import org.ow2.bonita.facade.uuid.DocumentUUID;
import org.ow2.bonita.services.Document;
import org.ow2.bonita.services.DocumentationManager;
import org.ow2.bonita.util.BonitaRuntimeException;
import org.ow2.bonita.util.DocumentService;
import org.ow2.bonita.util.EnvTool;

/**
 * 
 * @author Matthieu Chaffotte, Yanyan Liu
 * 
 */
public class AddAttachments extends ProcessConnector {

  private Map<String, String> attachments;

  public void setAttachments(final List<List<Object>> attachments) {
    this.attachments = this.bonitaListToMap(attachments, String.class, String.class);
  }

  @Override
  protected void executeConnector() throws Exception {
    final RuntimeAPI runtimeAPI = getApiAccessor().getRuntimeAPI();
    for (final Entry<String, String> attachment : attachments.entrySet()) {
      final String name = attachment.getKey();
      final File file = new File(attachment.getValue());
      final byte[] content = getContent(file);
      final String mimeType = getType(file);
      final DocumentationManager manager = EnvTool.getDocumentationManager();
      final List<Document> documents = DocumentService.getDocuments(manager, getProcessInstanceUUID(), name);
      try {
        if (documents.isEmpty()) {
          runtimeAPI.createDocument(name, getProcessInstanceUUID(), file.getName(), getType(file), content);
        } else {
          final DocumentUUID documentUUID = new DocumentUUID(documents.get(0).getId());
          runtimeAPI.addDocumentVersion(documentUUID, true, file.getName(), mimeType, content);
        }
      } catch (final DocumentationCreationException e) {
        throw new BonitaRuntimeException(e);
      }
    }
  }

  @Override
  protected List<ConnectorError> validateValues() {
    final List<ConnectorError> errors = new ArrayList<ConnectorError>();
    for (final Entry<String, String> attachment : attachments.entrySet()) {
      final ConnectorError error = checkFile(attachment.getKey(), attachment.getValue());
      if (error != null) {
        errors.add(error);
      }
    }
    return errors;
  }

  private byte[] getContent(final File file) throws IOException {
    final BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
    try {
      final int length = (int) file.length();
      final byte[] content = new byte[length];
      bis.read(content, 0, length);
      return content;
    } finally {
      if (bis != null) {
        bis.close();
      }
    }
  }

  private ConnectorError checkFile(final String fileName, final String filePath) {
    ConnectorError error = null;
    final File file = new File(filePath);
    if (!file.exists()) {
      error = new ConnectorError(fileName, new FileNotFoundException("Cannot access to " + filePath));
    } else if (!file.isFile()) {
      error = new ConnectorError(fileName, new FileNotFoundException(filePath + " is not a file"));
    } else if (!file.canRead()) {
      error = new ConnectorError(fileName, new FileNotFoundException("Cannot read " + filePath));
    }
    return error;
  }

  private String getType(final File file) {
    final MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
    return mimeTypesMap.getContentType(file);
  }

}
