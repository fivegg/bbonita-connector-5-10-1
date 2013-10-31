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
 * @author Jordi Anguelam, Yanyan Liu
 * 
 */
public class CreateSugarAccount extends SetSugarEntry {

    private static final Logger LOG = Logger.getLogger(CreateSugarAccount.class.getName());

    private String accountName = "";
    private String phone = "";
    private String website = "";

    @Override
    protected List<ConnectorError> validateValues() {
        if (LOG.isLoggable(Level.INFO)) {
            LOG.info("validatingValues");
        }

        List<ConnectorError> errors = new ArrayList<ConnectorError>();

        testEmptyVar(accountName, "accountName", errors);
        testEmptyVar(phone, "phone", errors);
        testEmptyVar(website, "website", errors);

        if (errors.size() == 0) {
            super.setModule("Accounts");
            super.setNameValueList(new Name_value[1]);

            errors = super.validateValues();
        }
        return errors;
    }

    @Override
    protected void executeConnector() throws Exception {

        final Name_value name = new Name_value("name", accountName);
        final Name_value phoneOffice = new Name_value("phone_office", phone);
        final Name_value web = new Name_value("website", website);
        // Assign the new Account to the user (login already done)
        final Name_value assignedUserId = new Name_value("assigned_user_id", this.getUserId());
        final Name_value[] nameValuesList = new Name_value[] { name, phoneOffice, web, assignedUserId };
        super.setNameValueList(nameValuesList);

        super.executeConnector();
    }

    /**
     * set the accountName
     * 
     * @param accountName the account name
     */
    public void setAccountName(final String accountName) {
        this.accountName = accountName;
    }

    /**
     * set the phone
     * 
     * @param phone
     */
    public void setPhone(final String phone) {
        this.phone = phone;
    }

    /**
     * set the website
     * 
     * @param website the web site URL
     */
    public void setWebsite(final String website) {
        this.website = website;
    }

}
