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

public class CreateSugarAccountTest extends TestCase {
    private static final Logger LOG = Logger.getLogger(CreateSugarAccountTest.class.getName());

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
     * test add an account record successfully
     * 
     * @throws Exception
     */
    public void testAddAccount() throws Exception {
        final CreateSugarAccount createAccount = getCreateSugarAccount();
        List<ConnectorError> errors = createAccount.validateValues();
        assertNotNull(errors);
        assertEquals(0, errors.size());
        createAccount.executeConnector();
        assertNotNull("No ID returned", createAccount.getReturnedId());
    }

    /**
     * test missing account name when create an account
     * 
     */
    public void testMissingAccountName() {
        final CreateSugarAccount createAccount = getCreateSugarAccount();
        createAccount.setAccountName("");
        testMissingParam(createAccount, "accountName");
    }

    /**
     * test missing phone when create an account
     * 
     */
    public void testMissingPhone() {
        final CreateSugarAccount createAccount = getCreateSugarAccount();
        createAccount.setPhone("");
        testMissingParam(createAccount, "phone");
    }

    /**
     * test missing website when create an account
     * 
     */
    public void testMissingWebsite() {
        final CreateSugarAccount createAccount = getCreateSugarAccount();
        createAccount.setWebsite("");
        testMissingParam(createAccount, "website");
    }

    /**
     * test missing specified parameter
     * 
     * @param createAccount
     * @param param
     */
    private void testMissingParam(CreateSugarAccount createAccount, String param) {
        final List<ConnectorError> errors = createAccount.validateValues();
        assertTrue(errors.size() == 1);

        final ConnectorError error = errors.get(0);
        assertTrue(param.equals(error.getField()));
    }

    /**
     * 
     * @return CreateSugarAccount
     */
    private CreateSugarAccount getCreateSugarAccount() {
        final CreateSugarAccount createAccount = new CreateSugarAccount();

        final String sugarSoapPort = SugarTestUtil.getProperty("sugarSoapPort");
        final String user = SugarTestUtil.getProperty("user");
        final String password = SugarTestUtil.getProperty("password");
        final String applicationName = SugarTestUtil.getProperty("applicationName");

        final String accountName = SugarTestUtil.getProperty("accountName");
        final String phone = SugarTestUtil.getProperty("phone");
        final String website = SugarTestUtil.getProperty("website");

        // Common configuration
        createAccount.setSugarSoapPort(sugarSoapPort);
        createAccount.setApplicationName(applicationName);
        createAccount.setUser(user);
        createAccount.setPassword(password);

        // Specific configuration
        createAccount.setAccountName(accountName);
        createAccount.setPhone(phone);
        createAccount.setWebsite(website);
        return createAccount;
    }
}
