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

import java.util.List;

import junit.framework.TestCase;

import org.ow2.bonita.connector.core.Connector;
import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.util.BonitaException;


/**
 * @author Charles Souillard
 * 
 */
public abstract class SalesforceConnectorTest extends TestCase {

	private static final String username = System.getProperty("bonita.connectors.salesforce.username");
	private static final String password_securityToken = System.getProperty("bonita.connectors.salesforce.password_securityToken");
	private static final String authEndpoint = null;
	private static final String serviceEndpoint = null;
	private static final String restEndpoint = null;
	private static final String proxyHost = null;
	private static final int proxyPort = 0;
	private static final String proxyUsername = null;
	private static final String proxyPassword = null;
	private static final int connectionTimeout = 0;
	private static final int readTimeout = 0;

	public static void setConfig(final SalesforceConnector sForceConnector) {
		sForceConnector.setUsername(username);
		sForceConnector.setPassword_securityToken(password_securityToken);
		sForceConnector.setAuthEndpoint(authEndpoint);
		sForceConnector.setServiceEndpoint(serviceEndpoint);
		sForceConnector.setRestEndpoint(restEndpoint);
		sForceConnector.setProxyHost(proxyHost);
		sForceConnector.setProxyPort(proxyPort);
		sForceConnector.setProxyUsername(proxyUsername);
		sForceConnector.setProxyPassword(proxyPassword);
		sForceConnector.setConnectionTimeout(connectionTimeout);
		sForceConnector.setReadTimeout(readTimeout);
	}

	public void testValidateConnector() throws BonitaException {
		Class<? extends Connector> connectorClass = getConnectorClass();
		List<ConnectorError> errors = Connector.validateConnector(connectorClass);
		for (ConnectorError error : errors) {
			System.out.println(error.getField() + " " + error.getError());
		}
		assertTrue(errors.isEmpty());
	}

	/**
	 * @return
	 */
	protected abstract Class<? extends Connector> getConnectorClass();
	
	public void testEmptyUserName(){
	  SalesforceConnector salesForceConnector = getSalesforceConnector();
	  SalesforceConnectorTest.setConfig(salesForceConnector);
	  // test null username
	  salesForceConnector.setUsername(null);
	  List<ConnectorError> errors = salesForceConnector.validate();
	  assertEquals(1, errors.size());
	  assertEquals("username", errors.get(0).getField());
	  // test empty username
	  salesForceConnector.setUsername("  ");
    errors = salesForceConnector.validate();
    assertEquals(1, errors.size());
    assertEquals("username", errors.get(0).getField());
	}
	
	public void testEmptyPassword_securityToken(){
	  SalesforceConnector salesForceConnector = getSalesforceConnector();
    SalesforceConnectorTest.setConfig(salesForceConnector);
    // test null password_securityToken
    salesForceConnector.setPassword_securityToken(null);
    List<ConnectorError> errors = salesForceConnector.validate();
    assertEquals(1, errors.size());
    assertEquals("password_securityToken", errors.get(0).getField());
    // test null password_securityToken
    salesForceConnector.setPassword_securityToken(null);
    errors = salesForceConnector.validate();
    assertEquals(1, errors.size());
    assertEquals("password_securityToken", errors.get(0).getField());
	}
	
	public void testProxyPort(){
	  SalesforceConnector salesForceConnector = getSalesforceConnector();
    SalesforceConnectorTest.setConfig(salesForceConnector);
    // negative number port test
    salesForceConnector.setProxyPort(-1);
    List<ConnectorError> errors = salesForceConnector.validate();
    assertEquals(1, errors.size());
    assertEquals("proxyPort", errors.get(0).getField());
    // large number port test
    salesForceConnector.setProxyPort(65537);
    errors = salesForceConnector.validate();
    assertEquals(1, errors.size());
    assertEquals("proxyPort", errors.get(0).getField());
	}
	
	public void testConnectionTimeout(){
	  SalesforceConnector salesForceConnector = getSalesforceConnector();
    SalesforceConnectorTest.setConfig(salesForceConnector);
    salesForceConnector.setConnectionTimeout(-100);
    List<ConnectorError> errors = salesForceConnector.validate();
    assertEquals(1, errors.size());
    assertEquals("connectionTimeout", errors.get(0).getField());
	}
	
	public void testReadTimeout(){
	  SalesforceConnector salesForceConnector = getSalesforceConnector();
    SalesforceConnectorTest.setConfig(salesForceConnector);
    salesForceConnector.setReadTimeout(-100);
    List<ConnectorError> errors = salesForceConnector.validate();
    assertEquals(1, errors.size());
    assertEquals("readTimeout", errors.get(0).getField());
	}
	
	/**
   * @return
   */
  protected abstract SalesforceConnector getSalesforceConnector();
}
