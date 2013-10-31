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

import org.bonitasoft.connectors.sugar.common.soap.v2.Name_value;
import org.ow2.bonita.connector.core.ConnectorError;

/**
 * 
 * @author Jordi Anguela, Yanyan Liu
 * 
 */
public class CreateSugarOpportunity extends SetSugarEntry {

    private static final Logger LOG = Logger.getLogger(CreateSugarOpportunity.class.getName());

    private String name = "";
    private String accountId = "";
    private String expectedClosedDate = "";
    private String salesStage = "";
    private String amount = "";
    private String probability = "";

    @Override
    protected List<ConnectorError> validateValues() {
        if (LOG.isLoggable(Level.INFO)) {
            LOG.info("validatingValues");
        }

        List<ConnectorError> errors = new ArrayList<ConnectorError>();

        testEmptyVar(name, "name", errors);
        testEmptyVar(accountId, "accountId", errors);
        testEmptyVar(expectedClosedDate, "expectedClosedDate", errors);
        testEmptyVar(salesStage, "salesStage", errors);
        testEmptyVar(amount, "amount", errors);

        if (errors.size() == 0) {
            super.setModule("Opportunities");
            super.setNameValueList(new Name_value[1]);

            errors = super.validateValues();
        }
        return errors;
    }

    @Override
    protected void executeConnector() throws Exception {

        final Name_value nvName = new Name_value("name", name);
        final Name_value nvAccountId = new Name_value("account_id", accountId);
        final Name_value dateClosed = new Name_value("date_closed", expectedClosedDate);
        final Name_value nvSalesStage = new Name_value("sales_stage", salesStage);
        final Name_value nvAmount = new Name_value("amount", amount);
        final Name_value nvProbability = new Name_value("probability", probability);
        // Assign the new Contanct to the user (login already done)
        final Name_value assignedUserId = new Name_value("assigned_user_id", this.getUserId());
        final Name_value[] nameValuesList = new Name_value[] { nvName, nvAccountId, dateClosed, nvSalesStage, nvAmount, nvProbability, assignedUserId };
        super.setNameValueList(nameValuesList);

        super.executeConnector();
    }

    /**
     * set the name
     * 
     * @param name
     */
    public void setName(final String name) {
        this.name = name;
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
     * set the expectedClosedDate
     * 
     * @param expectedClosedDate the expected closed date
     */
    public void setExpectedClosedDate(final String expectedClosedDate) {
        this.expectedClosedDate = expectedClosedDate;
    }

    /**
     * set the salesStage
     * 
     * @param salesStage the sales stage
     */
    public void setSalesStage(final String salesStage) {
        this.salesStage = salesStage;
    }

    /**
     * set the amount
     * 
     * @param amount the amount
     */
    public void setAmount(final String amount) {
        this.amount = amount;
    }

    /**
     * set the probability
     * 
     * @param probability the probability
     */
    public void setProbability(final String probability) {
        this.probability = probability;
    }

}
