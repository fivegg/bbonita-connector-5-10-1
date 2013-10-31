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
import org.drools.command.CommandFactory;
import org.drools.command.runtime.SetGlobalCommand;
import org.ow2.bonita.connector.core.ConnectorError;

/**
 * @author Yanyan Liu
 * 
 */
public class SetGlobalConnector extends DroolsCommandConnector {

  private String identifier;
  private Object object;
  private boolean out;
  private String outIdentifier;

  /*
   * (non-Javadoc)
   * 
   * @see org.bonitasoft.connectors.drools.common.DroolsConnector#getSpecifiedCommand()
   */
  @Override
  protected Command getSpecifiedCommand() {
    final SetGlobalCommand setGlobalCommand = (SetGlobalCommand) CommandFactory.newSetGlobal(identifier, object, out);//new SetGlobalCommand();
    if (outIdentifier != null && outIdentifier.trim().length() > 0) {
      setGlobalCommand.setOutIdentifier(outIdentifier);
    }
    return setGlobalCommand;
  }

  /* (non-Javadoc)
   * @see org.bonitasoft.connectors.drools.common.DroolsConnector#validateSeparateParams(java.util.List)
   */
  @Override
  protected List<ConnectorError> validateCommandParams() {
    final List<ConnectorError> errors = new ArrayList<ConnectorError>();
    ConnectorError identifierEmptyError = this.getErrorIfNullOrEmptyParam(identifier, "identifier");
    if(identifierEmptyError != null){
      errors.add(identifierEmptyError);
    }
    return errors;
  }

  /**
   * set identifier
   * 
   * @param identifier
   */
  public void setIdentifier(final String identifier) {
    this.identifier = identifier;
  }

  /**
   * set object
   * 
   * @param object
   */
  public void setObject(final Object object) {
    this.object = object;
  }

  /*
   * set out
   */
  public void setOut(final boolean out) {
    this.out = out;
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
