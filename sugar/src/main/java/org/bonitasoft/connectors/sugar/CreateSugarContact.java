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
public class CreateSugarContact extends SetSugarEntry {

    private static final Logger LOG = Logger.getLogger(CreateSugarContact.class.getName());

    private String firstName = "";
    private String lastName = "";
    private String phone = "";
    private String email = "";
    private String accountId = "";

    @Override
    protected List<ConnectorError> validateValues() {
        if (LOG.isLoggable(Level.INFO)) {
            LOG.info("validatingValues");
        }

        List<ConnectorError> errors = new ArrayList<ConnectorError>();

        testEmptyVar(firstName, "firstName", errors);
        testEmptyVar(lastName, "lastName", errors);
        testEmptyVar(phone, "phone", errors);
        testEmptyVar(email, "email", errors);

        if (errors.size() == 0) {
            super.setModule("Contacts");
            super.setNameValueList(new Name_value[1]);

            errors = super.validateValues();
        }
        return errors;
    }

    @Override
    protected void executeConnector() throws Exception {

        final Name_value firstNameValue = new Name_value("first_name", firstName);
        final Name_value lastNameValue = new Name_value("last_name", lastName);
        final Name_value phoneWork = new Name_value("phone_work", phone);
        final Name_value email1 = new Name_value("email1", email);
        final Name_value accountIdValue = new Name_value("account_id", accountId);
        // Assign the new Contanct to the user (login already done)
        final Name_value assignedUserId = new Name_value("assigned_user_id", this.getUserId());
        final Name_value[] nameValuesList = new Name_value[] { firstNameValue, lastNameValue, phoneWork, email1, accountIdValue, assignedUserId };
        super.setNameValueList(nameValuesList);

        super.executeConnector();
    }

    /**
     * set the firstName
     * 
     * @param firstName the firstName
     */
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    /**
     * set the lastName
     * 
     * @param lastName the last name
     */
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    /**
     * set the phone
     * 
     * @param phone the phone
     */
    public void setPhone(final String phone) {
        this.phone = phone;
    }

    /**
     * set the email
     * 
     * @param email the email address
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * set the accountId
     * 
     * @param accountId the account id
     */
    public void setAccountId(final String accountId) {
        this.accountId = accountId;
    }

}
