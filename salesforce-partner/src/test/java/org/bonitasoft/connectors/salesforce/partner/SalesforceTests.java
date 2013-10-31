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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Charles Souillard
 * 
 */
public class SalesforceTests extends TestCase {

  public static Test suite() throws Exception {
    final TestSuite suite = new TestSuite("Partner Salesforce Tests");
    suite.addTestSuite(CreateSObjectConnectorTest.class);
    suite.addTestSuite(DeleteSObjectConnectorTest.class);
    suite.addTestSuite(DeleteSObjectsConnectorTest.class);
    suite.addTestSuite(QuerySObjectsConnectorTest.class);
    suite.addTestSuite(RetrieveSObjectConnectorTest.class);
    suite.addTestSuite(RetrieveSObjectsConnectorTest.class);
    suite.addTestSuite(UpdateSObjectConnectorTest.class);
    return suite;
  }

}
