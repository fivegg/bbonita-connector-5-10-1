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
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.sobject.SObject;

/**
 * @author Charles Souillard, Matthieu Chaffotte
 * 
 */
public class QuerySObjectsConnector extends SalesforceConnector {

  private static final Logger LOGGER = Logger.getLogger(QuerySObjectsConnector.class.getName());

  // input parameters
  private String query;

  // output parameters
  private List<SObject> sObjects;
  private QueryResult queryResult;

  @Override
  protected List<ConnectorError> validateExtraValues() {
    final List<ConnectorError> errors = new ArrayList<ConnectorError>();
    final ConnectorError emptyError = this.getErrorIfNullOrEmptyParam(query, "query");
    if (emptyError != null) {
      errors.add(emptyError);
    }
    return errors;
  }

  @Override
  protected void executeFunction(final PartnerConnection connection) throws Exception {
    if (LOGGER.isLoggable(Level.FINE)) {
      LOGGER.fine("query = " + query);
    }
    queryResult = connection.query(query);
    sObjects = Arrays.asList(queryResult.getRecords());
  }

  public List<SObject> getSObjects() {
    return sObjects;
  }

  public QueryResult getQueryResult() {
    return queryResult;
  }

  public void setQuery(final String query) {
    this.query = query;
  }

}
