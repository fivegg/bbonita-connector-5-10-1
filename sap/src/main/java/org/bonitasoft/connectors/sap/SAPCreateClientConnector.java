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
package org.bonitasoft.connectors.sap;

import java.util.ArrayList;
import java.util.List;

import org.ow2.bonita.connector.core.ConnectorError;

import com.sap.mw.jco.JCO.Client;

/**
 * 
 * @author Charles Souillard
 *
 */
public class SAPCreateClientConnector extends SAPAbstractConnector {

  private String createdConnectionName;
  
  @Override
  protected void executeConnector() throws Exception {
    final Client jcoClient = getJcoClient();
    this.createdConnectionName = SAPJcoClientRepository.addJcoClient(jcoClient);
  }

  @Override
  protected List<ConnectorError> validateValues() {
    this.useExitingConnection = false;
    final List<ConnectorError> errors = new ArrayList<ConnectorError>();
    errors.addAll(super.validateValues());
    if (useExitingConnection) {
      errors.add(new ConnectorError("isUseExistingConnection", new IllegalArgumentException("You CAN NOT use an existing connection to create a new connection.")));
    }
    return errors;
  }
  
  public String getCreatedConnectionName() {
    return createdConnectionName;
  }
}
