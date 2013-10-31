/**
 * Copyright (C) 2009-2011 BonitaSoft S.A.
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
package org.bonitasoft.connectors.sugar;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.connectors.sugar.common.soap.v2.Link_name_to_fields_array;
import org.ow2.bonita.connector.core.ConnectorError;

/**
 * 
 * @author Jordi Anguela, Yanyan Liu
 * 
 */
public class GetAccountOpportunities extends GetSugarEntries {

    private static final Logger LOG = Logger.getLogger(CreateSugarContact.class.getName());

    // Input parameters
    private String accountId = "";
    private String accountName = "";

    // Predefined parameters
    private String module = "Accounts";
    private String query = "";
    private String orderBy = "name";
    private String[] opportunityFileds = { "id", "name", "amount", "date_closed", "sales_stage", "probability" };

    @Override
    protected List<ConnectorError> validateValues() {
        if (LOG.isLoggable(Level.INFO)) {
            LOG.info("validatingValues");
        }

        List<ConnectorError> errors = new ArrayList<ConnectorError>();

        if (accountId.length() == 0 && accountName.length() == 0) {
            errors.add(new ConnectorError("accountId", new IllegalArgumentException("Either an Account name or id must be sent!")));
        }

        if (errors.size() == 0) {
            super.setModule(module);
            errors = super.validateValues();
        }
        return errors;
    }

    @Override
    protected void executeConnector() throws Exception {

        if (accountId.length() > 0) {
            query = "accounts.id = '" + accountId + "'";
        } else if (accountName.length() > 0) {
            query = "accounts.name = '" + accountName + "'";
        } else {
            throw new IllegalArgumentException("Either an Account name or id must be sent!");
        }
        final Link_name_to_fields_array link = new Link_name_to_fields_array("opportunities", opportunityFileds);
        final Link_name_to_fields_array[] links = new Link_name_to_fields_array[] { link };
        final String[] fieldsToRetrieve = new String[] { "id", "name" };

        super.setQuery(query);
        super.setOrderBy(orderBy);
        super.setFieldsToRetrieve(fieldsToRetrieve);
        super.setLinks(links);

        super.executeConnector();
    }

    /**
     * set the accountId
     * 
     * @param accountId the account id
     */
    public void setAccountId(final String accountId) {
        this.accountId = accountId;
    }

    /**
     * set the account name
     * 
     * @param accountName the account name
     */
    public void setAccountName(final String accountName) {
        this.accountName = accountName;
    }

    /**
     * @return
     */
    public String[] getOpportunityFileds() {
        return opportunityFileds;
    }

}
