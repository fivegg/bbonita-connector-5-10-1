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

import java.util.List;

import org.drools.command.Command;
import org.drools.command.runtime.rule.GetObjectsCommand;
import org.ow2.bonita.connector.core.ConnectorError;

/**
 * @author Yanyan Liu
 * 
 */
public class GetObjectsConnector extends DroolsCommandConnector {

  private String outIdentifier;

  /*
   * (non-Javadoc)
   * 
   * @see org.bonitasoft.connectors.drools.common.DroolsConnector#getSpecifiedCommand()
   */
  @Override
  protected Command getSpecifiedCommand() {
    GetObjectsCommand getObjectsCommand = new GetObjectsCommand();
    if (outIdentifier != null && outIdentifier.trim().length() > 0) {
      getObjectsCommand.setOutIdentifier(outIdentifier);
    }
    return getObjectsCommand;
  }

  /* (non-Javadoc)
   * @see org.bonitasoft.connectors.drools.common.DroolsConnector#validateSeparateParams(java.util.List)
   */
  @Override
  protected List<ConnectorError> validateCommandParams() {
    return null;
  }

  /**
   * set outIdentifier
   * 
   * @param outIdentifier
   */
  public void setOutIdentifier(final String outIdentifier) {
    this.outIdentifier = outIdentifier;
  }

}
