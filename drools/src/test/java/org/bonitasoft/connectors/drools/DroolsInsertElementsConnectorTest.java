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
package org.bonitasoft.connectors.drools;

import java.util.ArrayList;
import java.util.Collection;

import org.bonitasoft.connectors.drools.common.DroolsConnectorTest;
import org.ow2.bonita.connector.core.Connector;
import org.test.Message;

/**
 * @author Yanyan Liu
 * 
 */
public class DroolsInsertElementsConnectorTest extends DroolsConnectorTest {

  private final String outIdentifier = "insertMessage1";
  private final String entryPoint = "default";

  public void testOk() throws Exception {
    final DroolsInsertElementsConnector droolsInsertElementsConnector = getDroolsInsertElementsConnector();
    droolsInsertElementsConnector.execute();
    assertEquals("200", droolsInsertElementsConnector.getStatus());
  }

  /**
   * @return
   */
  private DroolsInsertElementsConnector getDroolsInsertElementsConnector() {
    final DroolsInsertElementsConnector droolsInsertElementsConnector = new DroolsInsertElementsConnector();
    DroolsConnectorTest.configDroolsConnector(droolsInsertElementsConnector);
    droolsInsertElementsConnector.setKsessionName(properties.getProperty("ksessionName"));
    final Message message = new Message();
    message.setText("hello");
    final Collection<Object> listOfFacts = new ArrayList<Object>();

    listOfFacts.add(message);
    droolsInsertElementsConnector.setFacts(listOfFacts);
    droolsInsertElementsConnector.setOutIdentifier(outIdentifier);
    droolsInsertElementsConnector.setEntryPoint(entryPoint);
    return droolsInsertElementsConnector;
  }

  /* (non-Javadoc)
   * @see org.bonitasoft.connectors.drools.common.DroolsConnectorTest#getConnectorClass()
   */
  @Override
  protected Class<? extends Connector> getConnectorClass() {
    return DroolsInsertElementsConnector.class;
  }
}
