/**
 * Copyright (C) 2011-2012 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.connectors.drools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.drools.command.Command;
import org.drools.command.CommandFactory;
import org.drools.command.runtime.rule.InsertElementsCommand;
import org.ow2.bonita.connector.core.ConnectorError;

/**
 * @author Yanyan Liu
 */
public class DroolsInsertElementsConnector extends DroolsCommandConnector {

    private Collection<Object> facts;

    private String outIdentifier;

    private String entryPoint = "default";

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.connectors.drools.common.DroolsConnector#validateSeparateParams(java.util.List)
     */
    @Override
    protected List<ConnectorError> validateCommandParams() {
        final List<ConnectorError> errors = new ArrayList<ConnectorError>();
        if (this.facts == null || this.facts.size() == 0) {
            errors.add(new ConnectorError("facts", new IllegalArgumentException("Cannot be empty!")));
        }
        return errors;
    }

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.connectors.drools.common.DroolsClient#getCommand()
     */
    @Override
    protected Command getSpecifiedCommand() {
        final InsertElementsCommand insertElementsCommand = (InsertElementsCommand) CommandFactory.newInsertElements(this.facts);
        if (this.outIdentifier != null && this.outIdentifier.trim().length() != 0) {
            insertElementsCommand.setOutIdentifier(this.outIdentifier);
        }
        if (this.entryPoint == null || this.entryPoint.trim().length() == 0) {
            this.entryPoint = "default";
        }
        insertElementsCommand.setEntryPoint(this.entryPoint);
        return insertElementsCommand;
    }

    /**
     * set facts
     * 
     * @param facts
     */
    public void setFacts(final List<Object> facts) {
        this.facts = facts;
    }

    public void setFacts(final Collection<Object> facts) {
        this.facts = facts;
    }

    /**
     * set outIdentifier
     * 
     * @param outIdentifier
     */
    public void setOutIdentifier(final String outIdentifier) {
        this.outIdentifier = outIdentifier;
    }

    /**
     * set entryPoint
     * 
     * @param entryPoint
     */
    public void setEntryPoint(final String entryPoint) {
        this.entryPoint = entryPoint;
    }

}
