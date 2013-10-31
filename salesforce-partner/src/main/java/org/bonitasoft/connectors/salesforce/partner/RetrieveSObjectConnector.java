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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ow2.bonita.connector.core.ConnectorError;

import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.sobject.SObject;

/**
 * @author Charles Souillard, Matthieu Chaffotte
 * 
 */
public class RetrieveSObjectConnector extends SalesforceConnector {

  private static final Logger LOGGER = Logger.getLogger(RetrieveSObjectConnector.class.getName());

  // input parameters
  private List<String> fieldsToRetrieve;
  private String sObjectType;
  private String sObjectId;

  // output paraleters
  private SObject sObject;

  @Override
  protected List<ConnectorError> validateExtraValues() {
    final List<ConnectorError> errors = new ArrayList<ConnectorError>();
    final ConnectorError idEmptyError = this.getErrorIfNullOrEmptyParam(sObjectId, "sObjectId");
    if (idEmptyError != null) {
      errors.add(idEmptyError);
      return errors;
    }
    final ConnectorError invalidIdLengthError = this.getErrorIfIdLengthInvalid(sObjectId);
    if (invalidIdLengthError != null) {
      errors.add(invalidIdLengthError);
      return errors;
    }
    final ConnectorError typeEmptyError = this.getErrorIfNullOrEmptyParam(sObjectType, "sObjectType");
    if (typeEmptyError != null) {
      errors.add(typeEmptyError);
    }
    return errors;
  }

  @Override
  protected void executeFunction(final PartnerConnection connection) throws Exception {
    final String fieldsToRetrieve = SalesforceTool.buildFields(this.fieldsToRetrieve);
    if (LOGGER.isLoggable(Level.FINE)) {
      LOGGER.fine("fieldsToRetrieve = " + fieldsToRetrieve);
    }
    final String[] ids = new String[] { sObjectId };
    final SObject[] sObjects = connection.retrieve(fieldsToRetrieve, sObjectType, ids);
    if (sObjects != null && sObjects.length > 0) {
      sObject = sObjects[0];
    }
  }

  public void setSObjectId(final String sObjectId) {
    this.sObjectId = sObjectId;
  }

  public void setFieldsToRetrieve(final List<String> fieldsToRetrieve) {
    this.fieldsToRetrieve = fieldsToRetrieve;
  }

  public void setSObjectType(final String sObjectType) {
    this.sObjectType = sObjectType;
  }

  public SObject getSObject() {
    return sObject;
  }

}
