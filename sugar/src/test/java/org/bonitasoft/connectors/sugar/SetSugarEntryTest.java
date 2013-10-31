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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.ow2.bonita.connector.core.ConnectorError;

public class SetSugarEntryTest extends TestCase {
    private static final Logger LOG = Logger.getLogger(SetSugarEntryTest.class.getName());

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
     * test set sugar entry. here use adding an account as example
     * 
     * @throws Exception
     */
    public void testAddAccount() throws Exception {
        final SetSugarEntry createAccount = getWorkingSugarEntry();
        final List<ConnectorError> errors = createAccount.validateValues();
        assertNotNull(errors);
        assertEquals(0, errors.size());
        createAccount.executeConnector();
        assertNotNull("No ID returned", createAccount.getReturnedId());
    }

    /**
     * test wrong soap port. make sure provide a wrong port in config.SugarTestUtil.
     * 
     */
    public void testWrongSoapPort() {
        final SetSugarEntry createAccount = getWorkingSugarEntry();
        final String wrongSugarSoapPort = SugarTestUtil.getProperty("wrongSugarSoapPort");
        createAccount.setSugarSoapPort(wrongSugarSoapPort);
        testWrongParam(createAccount, "sugarSoapPort");
    }

    /**
     * test wrong user. make sure provide a wrong user name in config.SugarTestUtil.
     * 
     */
    public void testWrongUser() {
        final SetSugarEntry createAccount = getWorkingSugarEntry();
        final String wrongUser = SugarTestUtil.getProperty("wrongUser");
        createAccount.setUser(wrongUser);
        testWrongParam(createAccount, "user");
    }

    /**
     * the same with testWrongUser.
     * 
     */
    public void testWrongPassword() {
        final SetSugarEntry createAccount = getWorkingSugarEntry();
        final String wrongPassword = SugarTestUtil.getProperty("wrongPassword");
        createAccount.setPassword(wrongPassword);
        testWrongParam(createAccount, "user");
    }

    /**
     * @param createAccount
     * @param string
     */
    private void testWrongParam(SetSugarEntry createAccount, String wrongParam) {
        final List<ConnectorError> errors = createAccount.validateValues();
        assertTrue(errors.size() == 1);
        final ConnectorError error = errors.get(0);
        assertEquals(wrongParam, error.getField());
    }

    /**
     * 
     * @return SetSugarEntry
     */
    public SetSugarEntry getWorkingSugarEntry() {
        final SetSugarEntry createAccount = new SetSugarEntry();

        final String sugarSoapPort = SugarTestUtil.getProperty("sugarSoapPort");
        final String user = SugarTestUtil.getProperty("user");
        final String password = SugarTestUtil.getProperty("password");
        final String applicationName = SugarTestUtil.getProperty("applicationName");

        final String accountName = SugarTestUtil.getProperty("accountName");
        final String phone = SugarTestUtil.getProperty("phone");
        final String website = SugarTestUtil.getProperty("website");
        final String module = SugarTestUtil.getProperty("module");// "Accounts";

        final List<List<Object>> arrayList = new ArrayList<List<Object>>();
        final List<Object> nameList = new ArrayList<Object>();
        nameList.add("name");
        nameList.add(accountName);
        arrayList.add(nameList);
        final List<Object> phoneList = new ArrayList<Object>();
        phoneList.add("phone_office");
        phoneList.add(phone);
        arrayList.add(phoneList);
        final List<Object> websiteList = new ArrayList<Object>();
        websiteList.add("website");
        websiteList.add(website);
        arrayList.add(websiteList);

        // Common configuration
        createAccount.setSugarSoapPort(sugarSoapPort);
        createAccount.setApplicationName(applicationName);
        createAccount.setUser(user);
        createAccount.setPassword(password);

        // Specific configuration
        createAccount.setModule(module);
        createAccount.setNameValueList(arrayList);

        return createAccount;
    }
}
