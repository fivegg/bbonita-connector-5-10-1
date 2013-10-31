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
import org.drools.command.BatchExecutionCommand;
import org.drools.command.Command;
import org.drools.command.CommandFactory;
import org.drools.command.runtime.rule.FireAllRulesCommand;
import org.drools.runtime.help.BatchExecutionHelper;
import org.ow2.bonita.connector.core.ConnectorError;

/**
 * @author Yanyan Liu
 * 
 */
public abstract class DroolsCommandConnector extends DroolsConnector {

  private String ksessionName;

  @Override
  protected String getRequest() {
    final List<Command> cmds = new ArrayList<Command>();
    final Command command = getSpecifiedCommand();
    if (command != null) {
      cmds.add(command);
    }
    final FireAllRulesCommand fireAllRulesCommand = new FireAllRulesCommand();
    cmds.add(fireAllRulesCommand);
    final BatchExecutionCommand batchExecutionCommand = CommandFactory.newBatchExecution(cmds, ksessionName);
    return BatchExecutionHelper.newXStreamMarshaller().toXML(batchExecutionCommand);
  }

  protected abstract Command getSpecifiedCommand();

  protected abstract List<ConnectorError> validateCommandParams();

  @Override
  protected List<ConnectorError> validateExtraParams() {
    final List<ConnectorError> errors = new ArrayList<ConnectorError>();
    // validate ksessionName
    final ConnectorError ksessionNameEmptyError = getErrorIfNullOrEmptyParam(ksessionName, "ksessionName");
    if (ksessionNameEmptyError != null) {
      errors.add(ksessionNameEmptyError);
      return errors;
    }
    // validate other command parameters
    final List<ConnectorError> commandParamErrors = validateCommandParams();
    if (commandParamErrors != null) {
      errors.addAll(commandParamErrors);
    }
    return errors;
  }

  public void setKsessionName(final String ksessionName) {
    this.ksessionName = ksessionName;
  }

}
