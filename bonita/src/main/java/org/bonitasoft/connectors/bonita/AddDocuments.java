/**
 * Copyright (C) 2011-2012 BonitaSoft S.A.
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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.ProcessConnector;
import org.ow2.bonita.facade.RuntimeAPI;
import org.ow2.bonita.facade.runtime.Document;

/**
 * @author Yanyan Liu
 * 
 */
public class AddDocuments extends ProcessConnector {

  private Map<String, String> documents;
  private final List<Document> documentList = new ArrayList<Document>();
  private static final Log LOG = LogFactory.getLog(AddDocuments.class.getClass());

  @Override
  protected void executeConnector() throws Exception {
    final RuntimeAPI runtimeAPI = getApiAccessor().getRuntimeAPI();
    for (final Entry<String, String> doc : documents.entrySet()) {
      final File file = new File(doc.getValue());
      final BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
      final int length = (int) file.length();
      final byte[] content = new byte[length];
      try {
        bis.read(content, 0, length);
      } catch (final IOException e) {
        if (LOG.isErrorEnabled()) {
          LOG.error(e.getMessage());
        }
        throw e;
      } finally {
        if (bis != null) {
          bis.close();
        }
      }
      final Document document = runtimeAPI.createDocument(doc.getKey(), getProcessInstanceUUID(), file.getName(),
          getType(file), content);
      if (LOG.isDebugEnabled()) {
        LOG.debug("document.name = " + document.getName() + "\tdocument.ContentFileName"
            + document.getContentFileName() + "\tdocument.uuid = " + document.getUUID());
      }
      this.documentList.add(document);

    }
  }

  @Override
  protected List<ConnectorError> validateValues() {
    final List<ConnectorError> errors = new ArrayList<ConnectorError>();
    for (final Entry<String, String> doc : documents.entrySet()) {
      final ConnectorError error = checkFile(doc.getKey(), doc.getValue());
      if (error != null) {
        errors.add(error);
      }
    }
    return errors;
  }

  /**
   * get document list
   * 
   * @return List<Document>
   */
  public List<Document> getDocumentList() {
    return documentList;
  }

  /**
   * set documents
   * 
   * @param documents
   */
  public void setDocuments(final java.util.List<List<Object>> documents) {
    this.documents = this.bonitaListToMap(documents, String.class, String.class);
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

  /**
   * get file mimeType
   * 
   * @param file
   * @return String
   */
  private String getType(final File file) {
    final MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
    return mimeTypesMap.getContentType(file);
  }

}
