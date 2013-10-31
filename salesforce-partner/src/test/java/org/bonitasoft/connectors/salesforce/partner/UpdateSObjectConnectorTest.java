/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.connectors.salesforce.partner;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.bonitasoft.connectors.salesforce.partner.CreateSObjectConnector;
import org.bonitasoft.connectors.salesforce.partner.DeleteSObjectConnector;
import org.bonitasoft.connectors.salesforce.partner.RetrieveSObjectConnector;
import org.bonitasoft.connectors.salesforce.partner.UpdateSObjectConnector;
import org.ow2.bonita.connector.core.Connector;

/**
 * @author Charles Souillard
 * 
 */
public class UpdateSObjectConnectorTest extends SalesforceConnectorTest {

	public void testUpdateAccount() throws Exception {
		final String accountName = SalesforceTestTool.getNewName();

		final CreateSObjectConnector createConnector = SalesforceTestTool.createAccount(accountName);
		assertTrue(createConnector.getSaveResult().isSuccess());
		final String sObjectId = createConnector.getSObjectId();
		SalesforceTestTool.checkSObjectId(createConnector.getSObjectId());

		final RetrieveSObjectConnector retrieveConnector1 = SalesforceTestTool.retrieveAccount(sObjectId);
		SalesforceTestTool.checkAccount(retrieveConnector1.getSObject(), accountName);

		final Map<String, Object> fieldValues = new HashMap<String, Object>();
		fieldValues.put(SalesforceTestTool.ACCOUNT_PHONE_FIELD, "newPhoneValue");
		fieldValues.put(SalesforceTestTool.ACCOUNT_BOSREF_FIELD, "newBosRefValue");
		
		final UpdateSObjectConnector updateConnector = SalesforceTestTool.update(SalesforceTestTool.ACCOUNT_OBJECT_TYPE, sObjectId, fieldValues);
		
		Assert.assertTrue(updateConnector.getSaveResult().getSuccess());
		
		final Map<String, Object> expectedNewValues = new HashMap<String, Object>();
		expectedNewValues.put(SalesforceTestTool.SOBJECT_ID_FIELD, sObjectId);
		expectedNewValues.put(SalesforceTestTool.ACCOUNT_NAME_FIELD, accountName);
		expectedNewValues.put(SalesforceTestTool.ACCOUNT_PHONE_FIELD, "newPhoneValue");
		expectedNewValues.put(SalesforceTestTool.ACCOUNT_WEBSITE_FIELD, SalesforceTestTool.ACCOUNT_WEBSITE_DEFAULT_VALUE);
		expectedNewValues.put(SalesforceTestTool.ACCOUNT_BOSREF_FIELD, "newBosRefValue");
		
		final RetrieveSObjectConnector retrieveConnector2 = SalesforceTestTool.retrieveAccount(sObjectId);
		SalesforceTestTool.checkAccount(retrieveConnector2.getSObject(), expectedNewValues);
		
		final DeleteSObjectConnector deleteConnector = SalesforceTestTool.deleteSObject(sObjectId);
		SalesforceTestTool.checkDeletedObjectSuccessfull(sObjectId, deleteConnector);

		deleteConnector.execute();
		SalesforceTestTool.checkDeletedObjectFailure(deleteConnector);
	}

	public void testUpdateCustomObject() throws Exception {
		final String fieldA = SalesforceTestTool.getNewName();

		final CreateSObjectConnector createConnector = SalesforceTestTool.createCustomObject(fieldA);
		assertTrue(createConnector.getSaveResult().isSuccess());
		final String sObjectId = createConnector.getSObjectId();
		SalesforceTestTool.checkSObjectId(createConnector.getSObjectId());

		final RetrieveSObjectConnector retrieveConnector1 = SalesforceTestTool.retrieveCustomObject(sObjectId);
		SalesforceTestTool.checkCustomObject(retrieveConnector1.getSObject(), fieldA);

		final Map<String, Object> fieldValues = new HashMap<String, Object>();
		fieldValues.put(SalesforceTestTool.CUSTOM_OBJECT_FIELD_A_FIELD, "newFieldA");
		
		final UpdateSObjectConnector updateConnector = SalesforceTestTool.update(SalesforceTestTool.CUSTOM_OBJECT_OBJECT_TYPE, sObjectId, fieldValues);
		
		Assert.assertTrue(updateConnector.getSaveResult().getSuccess());
		
		final Map<String, Object> expectedNewValues = new HashMap<String, Object>();
		expectedNewValues.put(SalesforceTestTool.SOBJECT_ID_FIELD, sObjectId);
		expectedNewValues.put(SalesforceTestTool.CUSTOM_OBJECT_FIELD_A_FIELD, "newFieldA");
		
		final RetrieveSObjectConnector retrieveConnector2 = SalesforceTestTool.retrieveCustomObject(sObjectId);
		SalesforceTestTool.checkAccount(retrieveConnector2.getSObject(), expectedNewValues);
		
		final DeleteSObjectConnector deleteConnector = SalesforceTestTool.deleteSObject(sObjectId);
		SalesforceTestTool.checkDeletedObjectSuccessfull(sObjectId, deleteConnector);

		deleteConnector.execute();
		SalesforceTestTool.checkDeletedObjectFailure(deleteConnector);
	}

	@Override
	protected Class<? extends Connector> getConnectorClass() {
		return UpdateSObjectConnector.class;
	}

  /* (non-Javadoc)
   * @see org.bonitasoft.connectors.salesforce.partner.SalesforceConnectorTest#getSalesforceConnector()
   */
  @Override
  protected SalesforceConnector getSalesforceConnector() {
    UpdateSObjectConnector updateSObjectConnector = new UpdateSObjectConnector();
    updateSObjectConnector.setSObjectId("id_not_empty");
    updateSObjectConnector.setSObjectType("Account");
    return updateSObjectConnector;
  }
}
