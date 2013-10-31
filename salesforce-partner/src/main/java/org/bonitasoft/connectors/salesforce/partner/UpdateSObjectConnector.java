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
import java.util.Arrays;
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
public class UpdateSObjectConnector extends SalesforceConnector {

  //input parameters
  private String sObjectId;
  private String sObjectType;
  private Map<String, Object> fieldValues;

  //output parameters
  private SaveResult saveResult;


  @Override
  protected List<ConnectorError> validateExtraValues() {
    final List<ConnectorError> errors = new ArrayList<ConnectorError>();
    ConnectorError idEmptyError = this.getErrorIfNullOrEmptyParam(sObjectId, "sObjectId");
    if(idEmptyError != null){
      errors.add(idEmptyError);
      return errors;
    }
    ConnectorError invalidIdLengthError = this.getErrorIfIdLengthInvalid(sObjectId);
    if(invalidIdLengthError != null){
      errors.add(invalidIdLengthError);
      return errors;
    }
    ConnectorError typeEmptyError = this.getErrorIfNullOrEmptyParam(sObjectType, "sObjectType");
    if(typeEmptyError != null){
      errors.add(typeEmptyError);
      return errors;
    }
    if (this.fieldValues == null) {
      errors.add(new ConnectorError("fieldValues", new Exception("fieldValues cannot be null")));
    }
    return errors;
  }

  @Override
  protected void executeFunction(final PartnerConnection connection) throws Exception {
    final SObject sObject = new SObject();
    sObject.setType(sObjectType);
    sObject.setId(sObjectId);
    for (final Map.Entry<String, Object> entry : this.fieldValues.entrySet()) {
      sObject.setField(entry.getKey(), entry.getValue());
    }

    final List<SaveResult> saveResults = Arrays.asList(connection.update(new SObject[]{sObject}));
    if (saveResults != null && !saveResults.isEmpty()) {
      saveResult = saveResults.get(0);
    }
  }

  public void setSObjectType(String sObjectType) {
    this.sObjectType = sObjectType;
  }

  public void setSObjectId(String sObjectId) {
    this.sObjectId = sObjectId;
  }

  public void setFieldValues(final Map<String, Object> fieldValues) {
    this.fieldValues = fieldValues;
  }

  public void setFieldValues(final List<List<Object>> fieldValues) {
    setFieldValues(bonitaListToMap(fieldValues, String.class, Object.class));
  }

  public SaveResult getSaveResult() {
    return saveResult;
  }

}
