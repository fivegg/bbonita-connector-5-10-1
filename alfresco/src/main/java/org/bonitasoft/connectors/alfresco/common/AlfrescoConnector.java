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
package org.bonitasoft.connectors.alfresco.common;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.abdera.model.Document;
import org.apache.abdera.model.Element;
import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.ProcessConnector;

/**
 * 
 * @author Jordi Anguela
 *
 */
public abstract class AlfrescoConnector extends ProcessConnector {

  private static Logger LOGGER = Logger.getLogger(AlfrescoConnector.class.getName());
  public AlfrescoResponse response = new AlfrescoResponse();

  private String host;
  private Long port;
  private String username;
  private String password;

  @Override
  protected void executeConnector() throws Exception {
    if(LOGGER.isLoggable(Level.INFO)){
      LOGGER.info("executing AlfrescoConnector with params: " + host + ":" + port.toString() + " " + username + ":" + password);
    }
    AlfrescoRestClient alfrescoClient = new AlfrescoRestClient(host, port.toString(), username, password);
    executeFunction(alfrescoClient);
  }

  protected abstract void executeFunction(AlfrescoRestClient alfrescoClient) throws Exception;

  @Override
  protected List<ConnectorError> validateValues() {
    List<ConnectorError> errors = new ArrayList<ConnectorError>();
    if (port != null) {
      if (port < 0) {
        errors.add(new ConnectorError("proxyPort",
            new IllegalArgumentException("cannot be less than 0!")));
      } else if (port > 65535) {
        errors.add(new ConnectorError("proxyPort",
            new IllegalArgumentException("cannot be greater than 65535!")));
      }
    }
    List<ConnectorError> specificErrors = validateFunctionParameters();
    if (specificErrors != null) {
      errors.addAll(specificErrors);
    }
    return errors;
  }

  protected abstract List<ConnectorError> validateFunctionParameters();

  public void setPort(Long port) {
    this.port = port;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Document<Element> getResponseDocument() {
    return response.getDocument();
  }
  
  public String getResponseType() {
    return response.getResponseType();
  }

  public String getStatusCode() {
    return response.getStatusCode();
  }

  public String getStatusText() {
    return response.getStatusText();
  }

  public String getStackTrace() {
    return response.getStackTrace();
  }

}
