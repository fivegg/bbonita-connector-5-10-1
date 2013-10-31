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

import org.bonitasoft.connectors.salesforce.partner.CreateSObjectConnector;
import org.bonitasoft.connectors.salesforce.partner.DeleteSObjectsConnector;
import org.bonitasoft.connectors.salesforce.partner.RetrieveSObjectsConnector;
import org.ow2.bonita.connector.core.Connector;

/**
 * @author Charles Souillard
 * 
 */
public class DeleteSObjectsConnectorTest extends SalesforceConnectorTest {

	public void testDeleteAccounts() throws Exception {
		final CreateSObjectConnector createConnector1 = SalesforceTestTool.createAccount(SalesforceTestTool.getNewName());
		final String sObjectId1 = createConnector1.getSObjectId();
		final CreateSObjectConnector createConnector2 = SalesforceTestTool.createAccount(SalesforceTestTool.getNewName());
		final String sObjectId2 = createConnector2.getSObjectId();
		final CreateSObjectConnector createConnector3 = SalesforceTestTool.createAccount(SalesforceTestTool.getNewName());
		final String sObjectId3 = createConnector3.getSObjectId();
		
		final List<String> allSObjectIds = new ArrayList<String>();
		allSObjectIds.add(sObjectId1);
		allSObjectIds.add(sObjectId2);
		allSObjectIds.add(sObjectId3);
		
		final RetrieveSObjectsConnector retrieveConnector = SalesforceTestTool.retrieveAccounts(allSObjectIds);
		SalesforceTestTool.checkNumberOfNotNull(retrieveConnector, 3);
		SalesforceTestTool.checkNumberOfNull(retrieveConnector, 0);
		
		final List<String> deletedSObjectIdsPhase1 = new ArrayList<String>();
		deletedSObjectIdsPhase1.add(sObjectId1);
		deletedSObjectIdsPhase1.add(sObjectId2);
		
		final DeleteSObjectsConnector deleteConnectorPhase1 = SalesforceTestTool.deleteSObjects(deletedSObjectIdsPhase1);
		SalesforceTestTool.checkDeletedObjectsSuccessfull(deletedSObjectIdsPhase1, deleteConnectorPhase1);
		
		retrieveConnector.execute();
		SalesforceTestTool.checkNumberOfNotNull(retrieveConnector, 1);
		SalesforceTestTool.checkNumberOfNull(retrieveConnector, 2);
		
		final List<String> deletedSObjectIdsPhase2 = new ArrayList<String>();
		deletedSObjectIdsPhase2.add(sObjectId3);
		
		final DeleteSObjectsConnector deleteConnectorPhase2 = SalesforceTestTool.deleteSObjects(deletedSObjectIdsPhase2);
		SalesforceTestTool.checkDeletedObjectsSuccessfull(deletedSObjectIdsPhase2, deleteConnectorPhase2);
		
		retrieveConnector.execute();
		SalesforceTestTool.checkNumberOfNotNull(retrieveConnector, 0);
		SalesforceTestTool.checkNumberOfNull(retrieveConnector, 3);
	}

	@Override
	protected Class<? extends Connector> getConnectorClass() {
		return DeleteSObjectsConnector.class;
	}

  /* (non-Javadoc)
   * @see org.bonitasoft.connectors.salesforce.partner.SalesforceConnectorTest#getSalesforceConnector()
   */
  @Override
  protected SalesforceConnector getSalesforceConnector() {
    DeleteSObjectsConnector deleteSObjectsConnector = new DeleteSObjectsConnector();
    List<String> sObjectIds = new ArrayList<String>();
    sObjectIds.add("id_not_empty");
    deleteSObjectsConnector.setSObjectIds(sObjectIds);
    return deleteSObjectsConnector;
  }
}
