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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bonitasoft.connectors.salesforce.partner.CreateSObjectConnector;
import org.bonitasoft.connectors.salesforce.partner.DeleteSObjectConnector;
import org.bonitasoft.connectors.salesforce.partner.DeleteSObjectsConnector;
import org.bonitasoft.connectors.salesforce.partner.QuerySObjectsConnector;
import org.bonitasoft.connectors.salesforce.partner.RetrieveSObjectConnector;
import org.bonitasoft.connectors.salesforce.partner.RetrieveSObjectsConnector;
import org.bonitasoft.connectors.salesforce.partner.UpdateSObjectConnector;

import junit.framework.Assert;

import com.sforce.soap.partner.DeleteResult;
import com.sforce.soap.partner.sobject.SObject;

/**
 * @author Charles Souillard
 * 
 */
public class SalesforceTestTool {

	public static String getNewName() {
		return UUID.randomUUID().toString();
	}
	
	public static final String ACCOUNT_OBJECT_TYPE = "Account";
	public static final String CUSTOM_OBJECT_OBJECT_TYPE = "BonitaSoft__c";
	
	public static final String SOBJECT_ID_FIELD = "Id";
	
	public static final String ACCOUNT_NAME_FIELD = "Name";
	public static final String ACCOUNT_PHONE_FIELD = "Phone";
	public static final String ACCOUNT_WEBSITE_FIELD = "Website";
	public static final String ACCOUNT_BOSREF_FIELD = "bosRef__c";
	
	public static final String ACCOUNT_PHONE_DEFAULT_VALUE = "010-123456789";
	public static final String ACCOUNT_WEBSITE_DEFAULT_VALUE = "http://www.bonitasoft.org";
	public static final String ACCOUNT_BOSREF_DEFAULT_VALUE = "bosRefValue";
	
	public static final String CUSTOM_OBJECT_FIELD_A_FIELD = "field_a__c";
	
	public static String getSelectAccountByIdQuery(final String sObjectId) {
		final StringBuilder sb = new StringBuilder();
		sb.append("SELECT");
		sb.append(" ");
		sb.append(ACCOUNT_NAME_FIELD);
		sb.append(",");
		sb.append(ACCOUNT_PHONE_FIELD);
		sb.append(",");
		sb.append(ACCOUNT_WEBSITE_FIELD);
		sb.append(",");
		sb.append(ACCOUNT_BOSREF_FIELD);
		sb.append(" ");
		sb.append("FROM");
		sb.append(" ");
		sb.append(ACCOUNT_OBJECT_TYPE);
		sb.append(" ");
		sb.append("WHERE");
		sb.append(" ");
		sb.append("id='" + sObjectId + "'");
		return sb.toString();
	}
	
	public static String getSelectCustomObjectByFieldAQuery(final String fieldA) {
		final StringBuilder sb = new StringBuilder();
		sb.append("SELECT");
		sb.append(" ");
		sb.append(SOBJECT_ID_FIELD);
		sb.append(",");
		sb.append(CUSTOM_OBJECT_FIELD_A_FIELD);
		sb.append(" ");
		sb.append("FROM");
		sb.append(" ");
		sb.append(CUSTOM_OBJECT_OBJECT_TYPE);
		sb.append(" ");
		sb.append("WHERE");
		sb.append(" ");
		sb.append(CUSTOM_OBJECT_FIELD_A_FIELD + "='" + fieldA + "'");
		return sb.toString();
	}
	
	
	
	public static CreateSObjectConnector createAccount(final String accountName) throws Exception {
		System.err.println("Creating a new account with name: " + accountName);

		final Map<String, Object> fields = new HashMap<String, Object>();
		fields.put(ACCOUNT_NAME_FIELD, accountName);
		fields.put(ACCOUNT_PHONE_FIELD, ACCOUNT_PHONE_DEFAULT_VALUE);
		fields.put(ACCOUNT_WEBSITE_FIELD, ACCOUNT_WEBSITE_DEFAULT_VALUE);
		fields.put(ACCOUNT_BOSREF_FIELD, ACCOUNT_BOSREF_DEFAULT_VALUE);

		final CreateSObjectConnector createConnector = new CreateSObjectConnector();
		SalesforceConnectorTest.setConfig(createConnector);
		createConnector.setSObjectType(ACCOUNT_OBJECT_TYPE);
		createConnector.setFieldValues(fields);

		createConnector.execute();

		System.err.println("Returned sObjectId: " + createConnector.getSObjectId());
		
		return createConnector;
	}
	
	public static CreateSObjectConnector createCustomObject(final String fieldA) throws Exception {
		System.err.println("Creating a new custom object of type BonitaSoft with fieldA: " + fieldA);

		final Map<String, Object> fields = new HashMap<String, Object>();
		fields.put(CUSTOM_OBJECT_FIELD_A_FIELD, fieldA);

		final CreateSObjectConnector createConnector = new CreateSObjectConnector();
		SalesforceConnectorTest.setConfig(createConnector);
		createConnector.setSObjectType(CUSTOM_OBJECT_OBJECT_TYPE);
		createConnector.setFieldValues(fields);

		createConnector.execute();

		System.err.println("Returned sObjectId: " + createConnector.getSObjectId());
		
		return createConnector;
	}
	
	public static void checkSObjectId(final String sObjectId) {
		Assert.assertNotNull(sObjectId);
		Assert.assertTrue(sObjectId instanceof String);
		Assert.assertTrue(sObjectId.length() == 15 || sObjectId.length() == 18);
	}
	
	public static DeleteSObjectConnector deleteSObject(final String sObjectId) throws Exception {
		final DeleteSObjectConnector deleteConnector = new DeleteSObjectConnector();

		SalesforceConnectorTest.setConfig(deleteConnector);
		deleteConnector.setSObjectId(sObjectId);
		deleteConnector.execute();
		
		return deleteConnector;
	}
	
	public static RetrieveSObjectConnector retrieveAccount(final String sObjectId) throws Exception {
		final RetrieveSObjectConnector retrieveConnector = new RetrieveSObjectConnector();

		final List<String> fields = new ArrayList<String>();
		fields.add(ACCOUNT_NAME_FIELD);
		fields.add(ACCOUNT_PHONE_FIELD);
		fields.add(ACCOUNT_WEBSITE_FIELD);
		fields.add(ACCOUNT_BOSREF_FIELD);

		SalesforceConnectorTest.setConfig(retrieveConnector);
		retrieveConnector.setSObjectId(sObjectId);
		retrieveConnector.setFieldsToRetrieve(fields);
		retrieveConnector.setSObjectType(ACCOUNT_OBJECT_TYPE);

		retrieveConnector.execute();
		
		return retrieveConnector;
	}
	
	public static RetrieveSObjectConnector retrieveCustomObject(final String sObjectId) throws Exception {
		final RetrieveSObjectConnector retrieveConnector = new RetrieveSObjectConnector();

		final List<String> fieldsToRetrieve = new ArrayList<String>();
		fieldsToRetrieve.add(CUSTOM_OBJECT_FIELD_A_FIELD);
		
		SalesforceConnectorTest.setConfig(retrieveConnector);
		retrieveConnector.setSObjectId(sObjectId);
		retrieveConnector.setFieldsToRetrieve(fieldsToRetrieve);
		retrieveConnector.setSObjectType(CUSTOM_OBJECT_OBJECT_TYPE);

		retrieveConnector.execute();
		
		return retrieveConnector;
	}
	
	public static RetrieveSObjectsConnector retrieveCustomObjects(final List<String> sObjectIds) throws Exception {
		final RetrieveSObjectsConnector retrieveConnector = new RetrieveSObjectsConnector();

		final List<String> fieldsToRetrieve = new ArrayList<String>();
		fieldsToRetrieve.add(CUSTOM_OBJECT_FIELD_A_FIELD);
		
		SalesforceConnectorTest.setConfig(retrieveConnector);
		retrieveConnector.setSObjectIds(sObjectIds);
		retrieveConnector.setFieldsToRetrieve(fieldsToRetrieve);
		retrieveConnector.setSObjectType(CUSTOM_OBJECT_OBJECT_TYPE);

		retrieveConnector.execute();
		
		return retrieveConnector;
	}
	
	public static void checkAccount(final SObject sObject, final String accountName) {
		Assert.assertNotNull(sObject);
		Assert.assertEquals(accountName, sObject.getField(ACCOUNT_NAME_FIELD));
		Assert.assertEquals(ACCOUNT_PHONE_DEFAULT_VALUE, sObject.getField(ACCOUNT_PHONE_FIELD));
		Assert.assertEquals(ACCOUNT_WEBSITE_DEFAULT_VALUE, sObject.getField(ACCOUNT_WEBSITE_FIELD));
		Assert.assertEquals(ACCOUNT_BOSREF_DEFAULT_VALUE, sObject.getField(ACCOUNT_BOSREF_FIELD));
	}
	
	public static void checkAccount(final SObject sObject, final Map<String, Object> fieldValues) {
		Assert.assertNotNull(sObject);
		for (Map.Entry<String, Object> entry : fieldValues.entrySet()) {
			Assert.assertEquals(entry.getValue(), sObject.getField(entry.getKey()));	
		}
	}
	
	public static void checkCustomObject(final SObject sObject, final String fieldA) {
		Assert.assertNotNull(sObject);
		Assert.assertEquals(fieldA, sObject.getField(CUSTOM_OBJECT_FIELD_A_FIELD));
	}
	
	public static void checkDeletedObjectSuccessfull(final String sObjectId, final DeleteSObjectConnector deleteConnector) {
		Assert.assertEquals(sObjectId, deleteConnector.getDeleteResult().getId());
		Assert.assertTrue(deleteConnector.getDeleteResult().getSuccess());
		Assert.assertEquals(0, deleteConnector.getDeleteResult().getErrors().length);
	}
	
	public static void checkDeletedObjectsSuccessfull(final List<String> sObjectIds, final DeleteSObjectsConnector deleteConnector) {
		Assert.assertEquals(sObjectIds.size(), deleteConnector.getDeleteResults().size());
		for (final DeleteResult deleteResult : deleteConnector.getDeleteResults()) {
			Assert.assertTrue(sObjectIds.contains(deleteResult.getId()));
			Assert.assertTrue(deleteResult.getSuccess());
			Assert.assertEquals(0, deleteResult.getErrors().length);	
		}
		
	}

	public static void checkDeletedObjectFailure(final DeleteSObjectConnector deleteConnector) {
		Assert.assertNull(deleteConnector.getDeleteResult().getId());
		Assert.assertFalse(deleteConnector.getDeleteResult().getSuccess());
		Assert.assertEquals(1, deleteConnector.getDeleteResult().getErrors().length);
	}

	public static RetrieveSObjectsConnector retrieveAccounts(final List<String> sObjectIds) throws Exception {
		final RetrieveSObjectsConnector retrieveConnector = new RetrieveSObjectsConnector();

		final List<String> fieldsToRetrieve = new ArrayList<String>();
		fieldsToRetrieve.add(ACCOUNT_NAME_FIELD);
		fieldsToRetrieve.add(ACCOUNT_PHONE_FIELD);
		fieldsToRetrieve.add(ACCOUNT_WEBSITE_FIELD);
		fieldsToRetrieve.add(ACCOUNT_BOSREF_FIELD);

		SalesforceConnectorTest.setConfig(retrieveConnector);
		retrieveConnector.setSObjectIds(sObjectIds);
		retrieveConnector.setFieldsToRetrieve(fieldsToRetrieve);
		retrieveConnector.setSObjectType(ACCOUNT_OBJECT_TYPE);

		retrieveConnector.execute();
		
		return retrieveConnector;
	}

	public static DeleteSObjectsConnector deleteSObjects(final List<String> sObjectIds) throws Exception {
		final DeleteSObjectsConnector deleteConnector = new DeleteSObjectsConnector();

		SalesforceConnectorTest.setConfig(deleteConnector);
		deleteConnector.setSObjectIds(sObjectIds);
		deleteConnector.execute();
		
		return deleteConnector;
	}
	
	public static void checkNumberOfNotNull(final RetrieveSObjectsConnector retrieveConnector, final int expectedNumberOfNotNull) {
		int numberOfNotNull = 0;
		for (final SObject sObject : retrieveConnector.getSObjects()) {
			if (sObject != null) {
				numberOfNotNull++;
			}
		}
		Assert.assertEquals(expectedNumberOfNotNull, numberOfNotNull);
	}
	
	public static void checkNumberOfNull(final RetrieveSObjectsConnector retrieveConnector, final int expectedNumberOfNull) {
		int numberOfNull = 0;
		for (final SObject sObject : retrieveConnector.getSObjects()) {
			if (sObject == null) {
				numberOfNull++;
			}
		}
		Assert.assertEquals(expectedNumberOfNull, numberOfNull);
	}

	public static QuerySObjectsConnector query(final String query) throws Exception {
		final QuerySObjectsConnector queryConnector = new QuerySObjectsConnector();

		SalesforceConnectorTest.setConfig(queryConnector);
		queryConnector.setQuery(query);
		
		queryConnector.execute();
		
		return queryConnector;
	}

	public static UpdateSObjectConnector update(final String sObjectType, final String sObjectId, final Map<String, Object> fieldValues) throws Exception {
		final UpdateSObjectConnector updateConnector = new UpdateSObjectConnector();
		SalesforceConnectorTest.setConfig(updateConnector);
		
		updateConnector.setSObjectType(sObjectType);
		updateConnector.setSObjectId(sObjectId);
		updateConnector.setFieldValues(fieldValues);
		
		updateConnector.execute();
		
		return updateConnector;
	}
}
