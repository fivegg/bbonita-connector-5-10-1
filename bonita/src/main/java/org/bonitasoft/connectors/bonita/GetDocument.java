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

import java.util.List;

import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.ProcessConnector;
import org.ow2.bonita.facade.QueryRuntimeAPI;
import org.ow2.bonita.facade.runtime.Document;
import org.ow2.bonita.facade.uuid.DocumentUUID;

/**
 * @author Yanyan Liu
 * 
 */
public class GetDocument extends ProcessConnector {

  private String documentUUID;

  private Document document;

  @Override
  protected void executeConnector() throws Exception {
    final QueryRuntimeAPI queryTimeAPI = getApiAccessor().getQueryRuntimeAPI();
    document = queryTimeAPI.getDocument(new DocumentUUID(documentUUID));
  }

  @Override
  protected List<ConnectorError> validateValues() {
    return null;
  }

  public void setDocumentUUID(final String documentUUID) {
    this.documentUUID = documentUUID;
  }

  public Document getDocument() {
    return document;
  }

}
