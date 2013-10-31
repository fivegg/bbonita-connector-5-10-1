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

import java.io.FileInputStream;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.jackrabbit.webdav.client.methods.PutMethod;
import org.bonitasoft.connectors.webdav.exo.common.ExoConnector;
import org.bonitasoft.connectors.webdav.exo.common.ExoConnectorUtil;
import org.ow2.bonita.connector.core.ConnectorError;

/**
 * @author Jordi Anguela, Yanyan Liu
 */
public class ExoUploadFile extends ExoConnector {

  private String destinationUri;

  private String file;

  private String contentType;

  /**
   * set the distinationUri
   * 
   * @param destinationUri
   *          the destination URI for the uploaded file
   */
  public void setDestinationUri(final String destinationUri) {
    this.destinationUri = destinationUri;
  }

  /**
   * set the file
   * 
   * @param file
   *          the absolute path for the file
   */
  public void setFile(final String file) {
    this.file = file;
  }

  /**
   * set the contentType
   * 
   * @param contentType
   *          set the content type
   */
  public void setContentType(final String contentType) {
    this.contentType = contentType;
  }

  @Override
  protected void validateFunctionParameters(final List<ConnectorError> errors) {
    validateUri(errors, destinationUri, "destinationUri");

    if (!(file.length() > 0)) {
      errors.add(new ConnectorError("file", new IllegalArgumentException("cannot be empty")));
    }

    if (!(contentType.length() > 0)) {
      errors.add(new ConnectorError("contentType", new IllegalArgumentException("cannot be empty")));
    }
  }

  @Override
  protected void executeFunction() throws Exception {
    uploadFile(destinationUri, file, contentType);
  }

  /**
   * upload the specified file to eXo
   * 
   * @param destinationUri
   *          -- file's URI in eXo
   * @param file
   *          -- file's local path
   * @param contentType
   * @throws Exception
   */
  public void uploadFile(final String destinationUri, final String file, final String contentType) throws Exception {

    if (LOGGER.isLoggable(Level.INFO)) {
      LOGGER.info("uploadFile '" + file + "' with mimeType '" + contentType + "' to folder '" + destinationUri + "'");
    }

    // parse and decode file name
    final String parentUri = destinationUri.substring(0, destinationUri.lastIndexOf('/') + 1);
    String filename = destinationUri.substring(destinationUri.lastIndexOf('/') + 1);
    filename = ExoConnectorUtil.encodeSpecialCharacter(filename);
    filename = java.net.URLEncoder.encode(filename, "UTF-8");

    final PutMethod httpMethod = new PutMethod(parentUri + filename);

    final FileInputStream fis = new FileInputStream(file);
    final RequestEntity requestEntity = new InputStreamRequestEntity(fis, contentType);
    httpMethod.setRequestEntity(requestEntity);

    client.executeMethod(httpMethod);

    processResponse(httpMethod, true);
    httpMethod.releaseConnection();
  }

}
