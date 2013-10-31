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

import junit.framework.TestCase;

import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.util.BonitaException;

public class CreateSugarOpportunityTest extends TestCase {
    private static final Logger LOG = Logger.getLogger(CreateSugarOpportunityTest.class.getName());

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
     * testMissingParam name, accountId, expectedClosedDate, salesStage, amount
     * 
     * @param createOpportunity
     * @param param
     */
    private void testMissingParam(CreateSugarOpportunity createOpportunity, String param) {
        final List<ConnectorError> errors = createOpportunity.validateValues();
        assertTrue(errors.size() == 1);

        final ConnectorError error = errors.get(0);
        assertTrue(param.equals(error.getField()));
    }

    /**
     * test missing name when create an sugar opportunity.
     * 
     * @throws BonitaException
     */
    public void testMissingName() throws BonitaException {
        final CreateSugarOpportunity createOpportunity = getCreateSugarOpportunity();
        createOpportunity.setName("");
        testMissingParam(createOpportunity, "name");
    }

    /**
     * test missing account id when create an sugar opportunity.
     * 
     * @throws BonitaException
     */
    public void testMissingAccountId() throws BonitaException {
        final CreateSugarOpportunity createOpportunity = getCreateSugarOpportunity();
        createOpportunity.setAccountId("");
        testMissingParam(createOpportunity, "accountId");
    }

    /**
     * test missing expected closed date when create an sugar opportunity.
     * 
     * @throws BonitaException
     */
    public void testMissingExpectedClosedDate() throws BonitaException {
        final CreateSugarOpportunity createOpportunity = getCreateSugarOpportunity();
        createOpportunity.setExpectedClosedDate("");
        testMissingParam(createOpportunity, "expectedClosedDate");
    }

    /**
     * test missing sales stage when create an sugar opportunity.
     * 
     * @throws BonitaException
     */
    public void testMissingSalesStage() throws BonitaException {
        final CreateSugarOpportunity createOpportunity = getCreateSugarOpportunity();
        createOpportunity.setSalesStage("");
        testMissingParam(createOpportunity, "salesStage");
    }

    /**
     * test missing amount when create an sugar opportunity.
     * 
     * @throws BonitaException
     */
    public void testMissingAmount() throws BonitaException {
        final CreateSugarOpportunity createOpportunity = getCreateSugarOpportunity();
        createOpportunity.setAmount("");
        testMissingParam(createOpportunity, "amount");
    }

    /**
     * test add opportunity to account successfully.
     * 
     * @throws Exception
     */
    public void testAddOpportunityToAccount() throws Exception {
        final CreateSugarOpportunity createOpportunity = getCreateSugarOpportunity();
        final List<ConnectorError> errors = createOpportunity.validateValues();
        assertNotNull(errors);
        assertEquals(0, errors.size());
        createOpportunity.executeConnector();
        assertNotNull("No ID returned", createOpportunity.getReturnedId());
    }

    /**
     * 
     * @return CreateSugarOpportunity
     */
    private CreateSugarOpportunity getCreateSugarOpportunity() {
        final CreateSugarOpportunity createOpportunity = new CreateSugarOpportunity();

        final String sugarSoapPort = SugarTestUtil.getProperty("sugarSoapPort");
        final String user = SugarTestUtil.getProperty("user");
        final String password = SugarTestUtil.getProperty("password");
        final String applicationName = SugarTestUtil.getProperty("applicationName");

        final String name = SugarTestUtil.getProperty("name");
        final String accountId = SugarTestUtil.getProperty("accountId");
        final String expectedClosedDate = SugarTestUtil.getProperty("expectedClosedDate");
        final String salesStage = SugarTestUtil.getProperty("salesStage");
        final String amount = SugarTestUtil.getProperty("amount");
        final String probability = SugarTestUtil.getProperty("probability");

        // Common configuration
        createOpportunity.setSugarSoapPort(sugarSoapPort);
        createOpportunity.setApplicationName(applicationName);
        createOpportunity.setUser(user);
        createOpportunity.setPassword(password);

        // Specific configuration
        createOpportunity.setName(name);
        createOpportunity.setAccountId(accountId);
        createOpportunity.setExpectedClosedDate(expectedClosedDate);
        createOpportunity.setSalesStage(salesStage);
        createOpportunity.setAmount(amount);
        createOpportunity.setProbability(probability);
        return createOpportunity;
    }

}
