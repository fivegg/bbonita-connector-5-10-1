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
package org.bonitasoft.connectors.drools;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.connectors.drools.common.DroolsConnector;
import org.ow2.bonita.connector.core.ConnectorError;

/**
 * @author Yanyan Liu
 * 
 */
public class DroolsClientConnector extends DroolsConnector {

  private String request;

  protected List<ConnectorError> validateExtraParams() {
    final List<ConnectorError> errors = new ArrayList<ConnectorError>();
    ConnectorError requestEmptyError = this.getErrorIfNullOrEmptyParam(request, "request");
    if(requestEmptyError != null){
      errors.add(requestEmptyError);
    }
    return errors;
  }

  /**
   * set request; argument 'request' xml format.
   */
  public void setRequest(final String request) {
    this.request = request;
  }

  /**
   * @return the request
   */
  @Override
  protected String getRequest() {
    return request;
  }

}
