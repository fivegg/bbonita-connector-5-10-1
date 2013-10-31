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

import org.ow2.bonita.connector.core.ConnectorError;

import com.sforce.soap.partner.DeleteResult;
import com.sforce.soap.partner.PartnerConnection;

/**
 * @author Charles Souillard
 * 
 */
public class DeleteSObjectsConnector extends SalesforceConnector {

  //input parameters
  private List<String> sObjectIds;

  //output parameters
  private List<DeleteResult> deleteResults;


  @Override
  protected List<ConnectorError> validateExtraValues() {
    final List<ConnectorError> errors = new ArrayList<ConnectorError>();
    if (sObjectIds == null || sObjectIds.size() == 0) {
      errors.add(new ConnectorError("sObjectIds", new IllegalArgumentException("Cannot be empty!")));
      return errors;
    }
    for (String id : sObjectIds) {
      ConnectorError emptyError = this.getErrorIfNullOrEmptyParam(id, "id");
      if(emptyError != null){
        errors.add(emptyError);
        return errors;
      }
      ConnectorError invalidIdLengthError = this.getErrorIfIdLengthInvalid(id);
      if(invalidIdLengthError != null){
        errors.add(invalidIdLengthError);
        return errors;
      }
    }
    return errors;
  }


  @Override
  protected void executeFunction(final PartnerConnection connection) throws Exception {
    String[] ids = sObjectIds.toArray(new String[0]);
    deleteResults = Arrays.asList(connection.delete(ids));
  }

  public void setSObjectIds(final List<String> sObjectIds) {
    this.sObjectIds = sObjectIds;
  }

  public List<DeleteResult> getDeleteResults() {
    return deleteResults;
  }

}
