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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.ProcessConnector;
import org.ow2.bonita.facade.QueryRuntimeAPI;
import org.ow2.bonita.facade.RuntimeAPI;
import org.ow2.bonita.facade.exception.DocumentNotFoundException;
import org.ow2.bonita.facade.uuid.DocumentUUID;

/**
 * @author Yanyan Liu
 * 
 */
public class DeleteDocuments extends ProcessConnector {

  private static final Logger LOGGER = Logger.getLogger(DeleteDocument.class.getName());

  private boolean success = false;

  private Map<String, String> parameters = null;

  @Override
  protected void executeConnector() throws Exception {
    final RuntimeAPI runTimeAPI = getApiAccessor().getRuntimeAPI();
    String documentUUID = null;
    boolean allVersions = false;
    for (final Entry<String, String> entry : parameters.entrySet()) {
      allVersions = Boolean.parseBoolean(entry.getValue());
      documentUUID = entry.getKey();
      if (LOGGER.isLoggable(Level.FINE)) {
        LOGGER.fine("deleting documentUUID = " + documentUUID + " , isDeleteAllVersions = " + allVersions);
      }
      runTimeAPI.deleteDocuments(allVersions, new DocumentUUID(documentUUID));
    }
    success = true;
  }

  @Override
  protected List<ConnectorError> validateValues() {
    final List<ConnectorError> errors = new ArrayList<ConnectorError>();
    final QueryRuntimeAPI queryTimeAPI = getApiAccessor().getQueryRuntimeAPI();
    ConnectorError error = null;
    for (final String documentUUID : parameters.keySet()) {
      if (documentUUID.length() != 32) {
        error = new ConnectorError("documentUUID Error", new Exception("length of documentUUID should be 32"));
        errors.add(error);
      }
      final String value = parameters.get(documentUUID);
      if (!"true".equalsIgnoreCase(value) && !"false".equalsIgnoreCase(value)) {
        error = new ConnectorError("allVersions field Error", new Exception(
            "the field allVersions should only be 'true' or 'false'"));
        errors.add(error);
      }
      try {
        queryTimeAPI.getDocument(new DocumentUUID(documentUUID));
      } catch (final DocumentNotFoundException e) {
        error = new ConnectorError("document not found with UUID " + documentUUID, e);
        errors.add(error);
      }
    }
    return errors;
  }

  /**
   * set the parameters
   * 
   * @param parameters
   *          the parameters
   */
  public void setParameters(final Map<String, String> parameters) {
    this.parameters = parameters;
  }

  /**
   * set the parameters
   * 
   * @param parameters
   *          the parameters
   */
  public void setParameters(final List<List<Object>> parameters) {
    setParameters(bonitaListToMap(parameters, String.class, String.class));
  }

  /**
   * get success
   * 
   * @return Boolean
   */
  public boolean getSuccess() {
    return success;
  }

}
