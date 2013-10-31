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

import org.bonitasoft.connectors.salesforce.partner.CreateSObjectConnector;
import org.bonitasoft.connectors.salesforce.partner.DeleteSObjectConnector;
import org.bonitasoft.connectors.salesforce.partner.RetrieveSObjectConnector;
import org.ow2.bonita.connector.core.Connector;

/**
 * @author Charles Souillard
 * 
 */
public class RetrieveSObjectConnectorTest extends SalesforceConnectorTest {

	public void testRetrieveAccount() throws Exception {
		final String accountName = SalesforceTestTool.getNewName();
		
		final CreateSObjectConnector createConnector = SalesforceTestTool.createAccount(accountName);
		assertTrue(createConnector.getSaveResult().isSuccess());
		final String sObjectId = createConnector.getSObjectId();
		SalesforceTestTool.checkSObjectId(createConnector.getSObjectId());
		
		final RetrieveSObjectConnector retrieveConnector = SalesforceTestTool.retrieveAccount(sObjectId);
		SalesforceTestTool.checkAccount(retrieveConnector.getSObject(), accountName);

		final DeleteSObjectConnector deleteConnector = SalesforceTestTool.deleteSObject(sObjectId);
		SalesforceTestTool.checkDeletedObjectSuccessfull(sObjectId, deleteConnector);
		
		deleteConnector.execute();
		SalesforceTestTool.checkDeletedObjectFailure(deleteConnector);
	}
	
	public void testRetrieveCustomObject() throws Exception {
		final String fieldA = SalesforceTestTool.getNewName();
		
		final CreateSObjectConnector createConnector = SalesforceTestTool.createCustomObject(fieldA);
		assertTrue(createConnector.getSaveResult().isSuccess());
		final String sObjectId = createConnector.getSObjectId();
		SalesforceTestTool.checkSObjectId(createConnector.getSObjectId());
		
		final RetrieveSObjectConnector retrieveConnector = SalesforceTestTool.retrieveCustomObject(sObjectId);
		SalesforceTestTool.checkCustomObject(retrieveConnector.getSObject(), fieldA);

		final DeleteSObjectConnector deleteConnector = SalesforceTestTool.deleteSObject(sObjectId);
		SalesforceTestTool.checkDeletedObjectSuccessfull(sObjectId, deleteConnector);
		
		deleteConnector.execute();
		SalesforceTestTool.checkDeletedObjectFailure(deleteConnector);
	}

	@Override
	protected Class<? extends Connector> getConnectorClass() {
		return RetrieveSObjectConnector.class;
	}

  /* (non-Javadoc)
   * @see org.bonitasoft.connectors.salesforce.partner.SalesforceConnectorTest#getSalesforceConnector()
   */
  @Override
  protected SalesforceConnector getSalesforceConnector() {
    RetrieveSObjectConnector retrieveSObjectConnector = new RetrieveSObjectConnector();
    retrieveSObjectConnector.setSObjectId("id_not_empty");
    retrieveSObjectConnector.setSObjectType("Account");
    return retrieveSObjectConnector;
  }
}
