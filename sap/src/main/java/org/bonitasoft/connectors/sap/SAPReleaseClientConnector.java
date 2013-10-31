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

import java.util.List;

import org.ow2.bonita.connector.core.ConnectorError;

import com.sap.mw.jco.JCO.Client;

/**
 * 
 * @author Charles Souillard
 *
 */
public class SAPReleaseClientConnector extends SAPUseConnectionAbstractConnector {

  @Override
  protected void executeConnector() throws Exception {
    final Client jcoClient = getJcoClient();
    releaseClient(jcoClient);
  }
  
  @Override
  protected List<ConnectorError> validateValues() {
    this.releaseClient = true;
    this.repository = null;
    return super.validateValues();
  }

}
