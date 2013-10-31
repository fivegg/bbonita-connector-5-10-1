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
import java.util.Map;

import org.ow2.bonita.connector.core.ConnectorError;

import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.SaveResult;
import com.sforce.soap.partner.sobject.SObject;

/**
 * @author Charles Souillard
 * 
 */
public class CreateSObjectConnector extends SalesforceConnector {

	//input parameters
	private String sObjectType;
	private Map<String, Object> fieldValues;

	//output parameters
	protected String sObjectId;
	protected SaveResult saveResult;
		
	@Override
	protected final void executeFunction(final PartnerConnection connection) throws Exception {
		final SObject sObject = new SObject();
		sObject.setType(sObjectType);
		for (final Map.Entry<String, Object> field : fieldValues.entrySet()) {
			sObject.setField(field.getKey(), field.getValue());	
		}
		
		final SObject[] sObjects = new SObject[] {sObject};
		
		final SaveResult[] sResults = connection.create(sObjects);
		if (sResults != null && sResults.length > 0) {
			sObjectId = sResults[0].getId();
			saveResult = sResults[0];
		}
	}

	@Override
	protected List<ConnectorError> validateExtraValues() {
	  final List<ConnectorError> errors = new ArrayList<ConnectorError>();
	  ConnectorError emptyError = this.getErrorIfNullOrEmptyParam(this.sObjectType, "objectType");
	  if(emptyError != null){
	    errors.add(emptyError);
	  }
	  return errors;
	}

	public void setFieldValues(final Map<String, Object> fieldValues) {
		this.fieldValues = fieldValues;
	}
	
	public void setFieldValues(final List<List<Object>> fieldValues) {
		setFieldValues(bonitaListToMap(fieldValues, String.class, Object.class));
    }

	public void setSObjectType(final String sObjectType) {
		this.sObjectType = sObjectType;
	}

	public String getSObjectId() {
		return sObjectId;
	}

	public SaveResult getSaveResult() {
		return saveResult;
	}
}

