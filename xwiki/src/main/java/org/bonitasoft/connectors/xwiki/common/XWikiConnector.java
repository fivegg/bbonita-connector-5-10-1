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
package org.bonitasoft.connectors.xwiki.common;

import java.util.ArrayList;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.ProcessConnector;

/**
 * 
 * @author Jordi Anguela
 *
 */
public abstract class XWikiConnector extends ProcessConnector {

	  private static Logger LOGGER = Logger.getLogger(XWikiConnector.class.getName());

	  protected String host;
	  protected Integer port;
	  protected String username;
	  protected String password;

          protected String response;
          protected boolean status;

	  @Override
	  protected void executeConnector() throws Exception {
	    if(LOGGER.isLoggable(Level.INFO)){
	      LOGGER.info("executing XWikiConnector with params: " + host + ":" + port.toString() + " " + username + ":" + password);
	    }
	    XWikiRestClient xwikiClient = new XWikiRestClient(host, port, username, password);
	    executeFunction(xwikiClient);
	  }

	  protected abstract void executeFunction(XWikiRestClient xwikiClient) throws Exception;

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

	  public void setPort(Integer port) {
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

          public void setResponse(String response) {
            this.response = response;
          }
 
          public String getResponse() {
            return this.response;
          }

          public void setStatus(boolean status) {
             this.status = status;
          }

          public Boolean getStatus() {
            return this.status;
          }

}

