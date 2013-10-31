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

import org.bonitasoft.connectors.sugar.common.soap.v2.Link_name_to_fields_array;
import org.ow2.bonita.connector.core.ConnectorError;

public class GetSugarEntriesTest extends TestCase {
    private static final Logger LOG = Logger.getLogger(GetSugarEntriesTest.class.getName());

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
     * only 1 account expected
     * 
     * @throws Exception
     */
    public void testAccountOpportunitiesByAccountId() throws Exception {

        final String accountId = SugarTestUtil.getProperty("accountId");

        final String query = "accounts.id = '" + accountId + "'";

        GetSugarEntries connector = getWorkingConnector(query);
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
    }

    /**
     * 
     * @param query
     * @return GetSugarEntries
     */
    public GetSugarEntries getWorkingConnector(final String query) {

        final GetSugarEntries connector = new GetSugarEntries();

        final String sugarSoapPort = SugarTestUtil.getProperty("sugarSoapPort");
        final String user = SugarTestUtil.getProperty("user");
        final String password = SugarTestUtil.getProperty("password");
        final String applicationName = SugarTestUtil.getProperty("applicationName");

        final String module = SugarTestUtil.getProperty("module");
        final String orderBy = SugarTestUtil.getProperty("orderBy");

        connector.setSugarSoapPort(sugarSoapPort);
        connector.setApplicationName(applicationName);
        connector.setUser(user);
        connector.setPassword(password);

        connector.setModule(module);

        final Link_name_to_fields_array link = new Link_name_to_fields_array("opportunities", new String[] { "id", "name", "amount", "date_closed", "sales_stage", "probability" });
        final Link_name_to_fields_array[] links = new Link_name_to_fields_array[] { link };
        final String[] fieldsToRetrieve = new String[] { "id", "name" };

        connector.setQuery(query);
        connector.setOrderBy(orderBy);
        connector.setFieldsToRetrieve(fieldsToRetrieve);
        connector.setLinks(links);

        return connector;
    }

}
