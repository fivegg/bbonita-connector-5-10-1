/**
 * Created by Jordi Anguela, Yanyan Liu
 * 
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

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.ow2.bonita.connector.core.ConnectorError;

public class GetAccountOpportunitiesTest extends TestCase {
    protected static final Logger LOG = Logger.getLogger(GetAccountOpportunitiesTest.class.getName());

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        if (LOG.isLoggable(Level.WARNING)) {
            LOG.warning("======== Starting test: " + this.getClass().getName() + "." + this.getName() + "() ==========");
        }
    }

    @Override
    protected void tearDown() throws Exception {
        if (LOG.isLoggable(Level.WARNING)) {
            LOG.warning("======== Ending test: " + this.getName() + " ==========");
        }
        super.tearDown();
    }

    /**
     * only 1 account expected with at least 1 opportunity defined
     * 
     * @throws Exception
     */
    public void testAccountOpportunitiesByAccountId() throws Exception {

        final GetAccountOpportunities connector = getWorkingConnector();
        final String accountId = SugarTestUtil.getProperty("accountId");
        final List<ConnectorError> validation = connector.validateValues();
        Assert.assertEquals(0, validation.size());
        connector.executeConnector();

        final List<List<Object>> result = connector.getResponse();
        assertNotNull(result);

        final List<Object> accountFields = result.get(0);
        assertNotNull(accountFields);

        final String returnedAccountId = (String) accountFields.get(0);
        assertNotNull(accountId);
        assertEquals(accountId, returnedAccountId);

        final List<Object> opportunitiesList = (List<Object>) accountFields.get(2);
        assertNotNull(opportunitiesList);

        for (int x = 0; x < opportunitiesList.size(); x++) {
            final List<Object> opportunity = (List<Object>) opportunitiesList.get(x);
            assertNotNull(opportunity);

            final String[] opportunityFields = connector.getOpportunityFileds();
            for (int i = 0; i < opportunity.size(); i++) {
                if (LOG.isLoggable(Level.INFO)) {
                    LOG.info(opportunityFields[i] + "=" + opportunity.get(i));
                }
            }
        }
    }

    /**
     * @return GetAccountOpportunities
     */
    private GetAccountOpportunities getWorkingConnector() {
        final GetAccountOpportunities connector = new GetAccountOpportunities();

        final String sugarSoapPort = SugarTestUtil.getProperty("sugarSoapPort");
        final String user = SugarTestUtil.getProperty("user");
        final String password = SugarTestUtil.getProperty("password");
        final String applicationName = SugarTestUtil.getProperty("applicationName");

        final String accountId = SugarTestUtil.getProperty("accountId");

        connector.setSugarSoapPort(sugarSoapPort);
        connector.setApplicationName(applicationName);
        connector.setUser(user);
        connector.setPassword(password);

        connector.setAccountId(accountId);
        return connector;
    }
}
