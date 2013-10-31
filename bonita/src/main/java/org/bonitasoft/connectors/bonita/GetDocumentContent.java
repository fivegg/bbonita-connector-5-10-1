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
import org.ow2.bonita.facade.QueryRuntimeAPI;
import org.ow2.bonita.facade.exception.DocumentNotFoundException;
import org.ow2.bonita.facade.uuid.DocumentUUID;

/**
 * @author Yanyan Liu
 * 
 */
public class GetDocumentContent extends ProcessConnector {

  private String documentUUID;
  private String content;

  @Override
  protected void executeConnector() throws Exception {
    final QueryRuntimeAPI queryTimeAPI = getApiAccessor().getQueryRuntimeAPI();
    final byte[] byteContent = queryTimeAPI.getDocumentContent(new DocumentUUID(documentUUID));
    if (byteContent != null) {
      content = new String(byteContent);
    }
  }

  @Override
  protected List<ConnectorError> validateValues() {
    final List<ConnectorError> errors = new ArrayList<ConnectorError>();
    final QueryRuntimeAPI queryTimeAPI = getApiAccessor().getQueryRuntimeAPI();
    ConnectorError error = null;
    if (documentUUID.length() != 32) {
      error = new ConnectorError("documentUUID Error", new Exception("length of documentUUID should be 32"));
      errors.add(error);
    }
    try {
      queryTimeAPI.getDocument(new DocumentUUID(documentUUID));
    } catch (final DocumentNotFoundException e) {
      error = new ConnectorError("document not found with UUID " + documentUUID, e);
      errors.add(error);
    }
    return errors;
  }

  public void setDocumentUUID(final String documentUUID) {
    this.documentUUID = documentUUID;
  }

  public String getContent() {
    return content;
  }

}
