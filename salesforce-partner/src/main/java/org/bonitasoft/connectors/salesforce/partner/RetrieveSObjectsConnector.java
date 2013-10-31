/**
 * Copyright (C) 2011-2012 BonitaSoft S.A.
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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ow2.bonita.connector.core.ConnectorError;

import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.sobject.SObject;

/**
 * @author Charles Souillard, Matthieu Chaffotte
 * 
 */
public class RetrieveSObjectsConnector extends SalesforceConnector {

  private static final Logger LOGGER = Logger.getLogger(RetrieveSObjectsConnector.class.getName());

  // input parameters
  private List<String> fieldsToRetrieve;
  private String sObjectType;
  private List<String> sObjectIds;

  // output parameters
  private List<SObject> sObjects;

  @Override
  protected List<ConnectorError> validateExtraValues() {
    final List<ConnectorError> errors = new ArrayList<ConnectorError>();
    final ConnectorError typeEmptyError = this.getErrorIfNullOrEmptyParam(sObjectType, "sObjectType");
    if (typeEmptyError != null) {
      errors.add(typeEmptyError);
      return errors;
    }
    if (sObjectIds == null || sObjectIds.size() == 0) {
      errors.add(new ConnectorError("sObjectIds", new IllegalArgumentException("Cannot be empty!")));
      return errors;
    }
    for (final String id : sObjectIds) {
      final ConnectorError idEmptyError = this.getErrorIfNullOrEmptyParam(id, "id");
      if (idEmptyError != null) {
        errors.add(idEmptyError);
        return errors;
      }
      final ConnectorError invalidIdLengthError = this.getErrorIfIdLengthInvalid(id);
      if (invalidIdLengthError != null) {
        errors.add(invalidIdLengthError);
        return errors;
      }
    }
    return errors;
  }

  @Override
  protected void executeFunction(final PartnerConnection connection) throws Exception {
    final String fieldsToRetrieve = SalesforceTool.buildFields(this.fieldsToRetrieve);
    if (LOGGER.isLoggable(Level.FINE)) {
      LOGGER.fine("fieldsToRetrieve = " + fieldsToRetrieve);
    }
    final String[] ids = sObjectIds.toArray(new String[0]);
    sObjects = Arrays.asList(connection.retrieve(fieldsToRetrieve, sObjectType, ids));
  }

  /**
   * @return the sObject
   */
  public List<SObject> getSObjects() {
    return sObjects;
  }

  /**
   * @param sObjectIds
   *          the sObjectIds to set
   */
  public void setSObjectIds(final List<String> sObjectIds) {
    this.sObjectIds = sObjectIds;
  }

  /**
   * @param fieldsToRetrieve
   *          the fieldsToRetrieve to set
   */
  public void setFieldsToRetrieve(final List<String> fieldsToRetrieve) {
    this.fieldsToRetrieve = fieldsToRetrieve;
  }

  /**
   * @param sObjectType
   *          the sObjectType to set
   */
  public void setSObjectType(final String sObjectType) {
    this.sObjectType = sObjectType;
  }

}
