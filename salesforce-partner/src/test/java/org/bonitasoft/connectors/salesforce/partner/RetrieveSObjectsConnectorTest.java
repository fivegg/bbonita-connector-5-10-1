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

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.bonitasoft.connectors.salesforce.partner.CreateSObjectConnector;
import org.bonitasoft.connectors.salesforce.partner.DeleteSObjectsConnector;
import org.bonitasoft.connectors.salesforce.partner.RetrieveSObjectsConnector;
import org.ow2.bonita.connector.core.Connector;

/**
 * @author Charles Souillard
 * 
 */
public class RetrieveSObjectsConnectorTest extends SalesforceConnectorTest {

	public void testRetrieveAccounts() throws Exception {
		final CreateSObjectConnector createConnector1 = SalesforceTestTool.createAccount(SalesforceTestTool.getNewName());
		final String sObjectId1 = createConnector1.getSObjectId();
		final CreateSObjectConnector createConnector2 = SalesforceTestTool.createAccount(SalesforceTestTool.getNewName());
		final String sObjectId2 = createConnector2.getSObjectId();
		
		final List<String> allSObjectIds = new ArrayList<String>();
		allSObjectIds.add(sObjectId1);
		allSObjectIds.add(sObjectId2);
		
		final RetrieveSObjectsConnector retrieveConnector = SalesforceTestTool.retrieveAccounts(allSObjectIds);
		SalesforceTestTool.checkNumberOfNotNull(retrieveConnector, 2);
		Assert.assertTrue(allSObjectIds.contains(retrieveConnector.getSObjects().get(0).getId()));
		Assert.assertTrue(allSObjectIds.contains(retrieveConnector.getSObjects().get(1).getId()));
		
		final List<String> deletedSObjectIdsPhase1 = new ArrayList<String>();
		deletedSObjectIdsPhase1.add(sObjectId1);
		deletedSObjectIdsPhase1.add(sObjectId2);
		
		final DeleteSObjectsConnector deleteConnectorPhase1 = SalesforceTestTool.deleteSObjects(deletedSObjectIdsPhase1);
		SalesforceTestTool.checkDeletedObjectsSuccessfull(deletedSObjectIdsPhase1, deleteConnectorPhase1);
		
		retrieveConnector.execute();
		SalesforceTestTool.checkNumberOfNotNull(retrieveConnector, 0);
		SalesforceTestTool.checkNumberOfNull(retrieveConnector, 2);
	}

	public void testRetrieveCustomObjects() throws Exception {
		final CreateSObjectConnector createConnector1 = SalesforceTestTool.createCustomObject(SalesforceTestTool.getNewName());
		final String sObjectId1 = createConnector1.getSObjectId();
		final CreateSObjectConnector createConnector2 = SalesforceTestTool.createCustomObject(SalesforceTestTool.getNewName());
		final String sObjectId2 = createConnector2.getSObjectId();
		
		final List<String> allSObjectIds = new ArrayList<String>();
		allSObjectIds.add(sObjectId1);
		allSObjectIds.add(sObjectId2);
		
		final RetrieveSObjectsConnector retrieveConnector = SalesforceTestTool.retrieveCustomObjects(allSObjectIds);
		SalesforceTestTool.checkNumberOfNotNull(retrieveConnector, 2);
		Assert.assertTrue(allSObjectIds.contains(retrieveConnector.getSObjects().get(0).getId()));
		Assert.assertTrue(allSObjectIds.contains(retrieveConnector.getSObjects().get(1).getId()));
		
		final List<String> deletedSObjectIdsPhase1 = new ArrayList<String>();
		deletedSObjectIdsPhase1.add(sObjectId1);
		deletedSObjectIdsPhase1.add(sObjectId2);
		
		final DeleteSObjectsConnector deleteConnectorPhase1 = SalesforceTestTool.deleteSObjects(deletedSObjectIdsPhase1);
		SalesforceTestTool.checkDeletedObjectsSuccessfull(deletedSObjectIdsPhase1, deleteConnectorPhase1);
		
		retrieveConnector.execute();
		SalesforceTestTool.checkNumberOfNotNull(retrieveConnector, 0);
		SalesforceTestTool.checkNumberOfNull(retrieveConnector, 2);
	}
	
	@Override
	protected Class<? extends Connector> getConnectorClass() {
		return RetrieveSObjectsConnector.class;
	}

  /* (non-Javadoc)
   * @see org.bonitasoft.connectors.salesforce.partner.SalesforceConnectorTest#getSalesforceConnector()
   */
  @Override
  protected SalesforceConnector getSalesforceConnector() {
    RetrieveSObjectsConnector retrieveSObjectsConnector = new RetrieveSObjectsConnector();
    retrieveSObjectsConnector.setSObjectType("Account");
    List<String> sObjectIds = new ArrayList<String>();
    sObjectIds.add("id_not_empty");
    retrieveSObjectsConnector.setSObjectIds(sObjectIds);
    return retrieveSObjectsConnector;
  }
}
