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
package org.bonitasoft.connectors.drools;

import java.util.ArrayList;

import org.bonitasoft.connectors.drools.common.DroolsConnectorTest;
import org.ow2.bonita.connector.core.Connector;

/**
 * @author Yanyan Liu
 *
 */
public class DroolsQueryConnectorTest extends DroolsConnectorTest {
  private String queryName = "get All Messages";
  private String outIdentifier = "outIdentify";
  private ArrayList<Object> arguments;

  public void testOk() throws Exception{
    DroolsQueryConnector droolsQueryConnector = getDroolsQueryConnector();
    droolsQueryConnector.execute();
    assertEquals("200",droolsQueryConnector.getStatus());
  }

  /**
   * @return
   */
  private DroolsQueryConnector getDroolsQueryConnector() {
    DroolsQueryConnector droolsQueryConnector = new DroolsQueryConnector();
    DroolsConnectorTest.configDroolsConnector(droolsQueryConnector);
    droolsQueryConnector.setKsessionName(properties.getProperty("ksessionName"));
    droolsQueryConnector.setQueryName(queryName);
    droolsQueryConnector.setOutIdentifier(outIdentifier);
//    droolsQueryConnector.setKsessionName("ksession1");
//    arguments = new ArrayList();
//    Message message = new Message();
//    message.setText("echo:hello");
//    arguments.add(message);
//            arguments.add("insertMessage");
    droolsQueryConnector.setArguments(arguments);
    return droolsQueryConnector;
  }

  /* (non-Javadoc)
   * @see org.bonitasoft.connectors.drools.common.DroolsConnectorTest#getConnectorClass()
   */
  @Override
  protected Class<? extends Connector> getConnectorClass() {
    return DroolsQueryConnector.class;
  }
}
