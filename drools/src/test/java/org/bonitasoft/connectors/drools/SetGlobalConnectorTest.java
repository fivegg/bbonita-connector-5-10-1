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

import org.bonitasoft.connectors.drools.common.DroolsConnectorTest;
import org.ow2.bonita.connector.core.Connector;
import org.test.Message;

/**
 * @author Yanyan Liu
 * 
 */
public class SetGlobalConnectorTest extends DroolsConnectorTest {

  private String identifier = "myGlobal";
  private Message object = new Message();//"hello";
  private Boolean out = true;
  private String outIdentifier = "globalOutIdentifier";

  public void testOk() throws Exception {
    SetGlobalConnector setGlobalConnector = getSetGlobalConnector();
    setGlobalConnector.execute();
    assertEquals("200", setGlobalConnector.getStatus());
  }

  /**
   * @return
   */
  private SetGlobalConnector getSetGlobalConnector() {
    SetGlobalConnector setGlobalConnector = new SetGlobalConnector();
    DroolsConnectorTest.configDroolsConnector(setGlobalConnector);
    setGlobalConnector.setKsessionName(properties.getProperty("ksessionName"));
    setGlobalConnector.setIdentifier(identifier);
    object.setText("hi");
    setGlobalConnector.setObject(object);
    setGlobalConnector.setOut(out);
    setGlobalConnector.setOutIdentifier(outIdentifier);
    return setGlobalConnector;
  }

  /* (non-Javadoc)
   * @see org.bonitasoft.connectors.drools.common.DroolsConnectorTest#getConnectorClass()
   */
  @Override
  protected Class<? extends Connector> getConnectorClass() {
    return SetGlobalConnector.class;
  }
}
