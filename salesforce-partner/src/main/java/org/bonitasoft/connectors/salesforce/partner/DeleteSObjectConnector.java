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
public class DeleteSObjectConnector extends SalesforceConnector {

  //input parameters
  private String sObjectId;

  //output parameters
  private DeleteResult deleteResult;


  @Override
  protected List<ConnectorError> validateExtraValues() {
    final List<ConnectorError> errors = new ArrayList<ConnectorError>();
    ConnectorError emptyError = this.getErrorIfNullOrEmptyParam(sObjectId, "sObjectId");
    if(emptyError != null){
      errors.add(emptyError);
      return errors;
    }
    ConnectorError invalidIdLengthError = this.getErrorIfIdLengthInvalid(sObjectId);
    if(invalidIdLengthError != null){
      errors.add(invalidIdLengthError);
    }
    return errors;
  }


  @Override
  protected void executeFunction(final PartnerConnection connection) throws Exception {
    String[] ids = new String[]{sObjectId};
    List<DeleteResult> deleteResults = Arrays.asList(connection.delete(ids));
    if (deleteResults != null && !deleteResults.isEmpty()) {
      deleteResult = deleteResults.get(0);
    }
  }

  public void setSObjectId(final String sObjectId) {
    this.sObjectId = sObjectId;
  }

  public DeleteResult getDeleteResult() {
    return deleteResult;
  }

}
