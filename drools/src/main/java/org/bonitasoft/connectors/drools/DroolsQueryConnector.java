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

import org.drools.command.Command;
import org.drools.command.runtime.rule.QueryCommand;
import org.ow2.bonita.connector.core.ConnectorError;

/**
 * @author Yanyan Liu
 * 
 */
public class DroolsQueryConnector extends DroolsCommandConnector {

  private String queryName;
  private String outIdentifier;
  private ArrayList<Object> arguments;

  /*
   * (non-Javadoc)
   * 
   * @see org.bonitasoft.connectors.drools.common.DroolsClient#getCommand()
   */
  @Override
  protected Command getSpecifiedCommand() {
    QueryCommand queryCommand = new QueryCommand();
    queryCommand.setName(queryName);
    if (outIdentifier != null) {
      queryCommand.setOutIdentifier(outIdentifier);
    }
    if (arguments != null) {
      queryCommand.setArguments(arguments);
    }
    return queryCommand;
  }

  /* (non-Javadoc)
   * @see org.bonitasoft.connectors.drools.common.DroolsConnector#validateSeparateParams(java.util.List)
   */
  @Override
  protected List<ConnectorError> validateCommandParams() {
    final List<ConnectorError> errors = new ArrayList<ConnectorError>();
    ConnectorError queryNameEmptyError = this.getErrorIfNullOrEmptyParam(queryName, "queryName");
    if(queryNameEmptyError != null){
      errors.add(queryNameEmptyError);
    }
    return errors;
  }

  /**
   * set query name
   * 
   * @param queryObjectName
   */
  public void setQueryName(final String queryName) {
    this.queryName = queryName;
  }

  /**
   * set arguments for query
   * 
   * @param arguments
   */
  public void setArguments(final ArrayList<Object> arguments) {
    this.arguments = arguments;
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
