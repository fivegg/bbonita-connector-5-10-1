/**
 * Created by Jordi Anguela,  Yanyan Liu
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

public class CreateSugarContactTest extends TestCase {
    private static final Logger LOG = Logger.getLogger(CreateSugarContactTest.class.getName());

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

    public void testAddContact() throws Exception {
        final String accountId = "";
        addContactToAccount(accountId);
    }

    public void testAddContactToAccount() throws Exception {
        final String accountId = SugarTestUtil.getProperty("accountId");
        addContactToAccount(accountId);
    }

    /**
     * add contact to an id specified account
     * 
     * @param accountId the account id
     * @throws Exception
     */
    private void addContactToAccount(final String accountId) throws Exception {
        final CreateSugarContact createContact = getCreateSugarContact();
        createContact.setAccountId(accountId);
        final List<ConnectorError> errors = createContact.validateValues();
        assertNotNull(errors);
        assertEquals(0, errors.size());
        createContact.executeConnector();
        assertNotNull("No ID returned", createContact.getReturnedId());
    }

    /**
     * test missing first name when create a contact.
     * 
     * @throws BonitaException
     */
    public void testMissingFirstName() throws BonitaException {
        final CreateSugarContact createContact = getCreateSugarContact();
        createContact.setFirstName("");
        testMissingParam(createContact, "firstName");
    }

    /**
     * test missing last name when create a contact.
     * 
     * @throws BonitaException
     */
    public void testMissingLastName() throws BonitaException {
        final CreateSugarContact createContact = getCreateSugarContact();
        createContact.setLastName("");
        testMissingParam(createContact, "lastName");
    }

    /**
     * test missing email when create a contact.
     * 
     * @throws BonitaException
     */
    public void testMissingEmail() throws BonitaException {
        final CreateSugarContact createContact = getCreateSugarContact();
        createContact.setEmail("");
        testMissingParam(createContact, "email");
    }

    /**
     * test missing phone when create a contact.
     * 
     * @throws BonitaException
     */
    public void testMissingPhone() throws BonitaException {
        final CreateSugarContact createContact = getCreateSugarContact();
        createContact.setPhone("");
        testMissingParam(createContact, "phone");
    }

    /**
     * @param createContact
     * @param string
     */
    private void testMissingParam(CreateSugarContact createContact, String param) {
        final List<ConnectorError> errors = createContact.validateValues();
        assertTrue(errors.size() == 1);

        final ConnectorError error = errors.get(0);
        assertTrue(param.equals(error.getField()));
    }

    /**
     * 
     * @return CreateSugarContact
     */
    private CreateSugarContact getCreateSugarContact() {
        final CreateSugarContact createContact = new CreateSugarContact();

        final String sugarSoapPort = SugarTestUtil.getProperty("sugarSoapPort");
        final String user = SugarTestUtil.getProperty("user");
        final String password = SugarTestUtil.getProperty("password");
        final String applicationName = SugarTestUtil.getProperty("applicationName");

        final String firstName = SugarTestUtil.getProperty("firstName");
        final String lastName = SugarTestUtil.getProperty("lastName");
        final String phone = SugarTestUtil.getProperty("phone");
        final String email = SugarTestUtil.getProperty("email");

        // Common configuration
        createContact.setSugarSoapPort(sugarSoapPort);
        createContact.setApplicationName(applicationName);
        createContact.setUser(user);
        createContact.setPassword(password);

        // Specific configuration
        createContact.setFirstName(firstName);
        createContact.setLastName(lastName);
        createContact.setPhone(phone);
        createContact.setEmail(email);
        createContact.setAccountId("");

        return createContact;
    }
}
