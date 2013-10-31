/**
 * Copyright (C) 2009 BonitaSoft S.A.
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
package org.bonitasoft.connectors.microsoft.exchange;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.connectors.microsoft.exchange.common.Constants;
import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.util.BonitaException;

import junit.framework.TestCase;

/**
 * @author Jordi Anguela
 */
public class CreateContactTest extends TestCase {
	
	protected static final Logger LOG = Logger.getLogger(CreateContactTest.class.getName());

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
	    if (LOG.isLoggable(Level.WARNING)) {
	    	LOG.warning("======== Starting test: " + this.getClass().getName() + "." + this.getName() + "() ==========");
	    }
	}
	
	@Override
	protected void tearDown() throws Exception {
		if (LOG.isLoggable(Level.WARNING)) {
	    	LOG.warning("======== Ending test: " + this.getName() + " ==========");
	    }
	    super.tearDown();
	}

	public void testValidParameters() throws BonitaException {
		CreateContact connector = getValidConnector();
		try {
			List<ConnectorError> errors =  connector.validateValues();
			assertTrue( errors.size() == 0 );
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
	
	public void testEmptyUsername() throws BonitaException {
		CreateContact connector = getValidConnector();
		connector.setUsername("");
		try {
			List<ConnectorError> errors =  connector.validateValues();
			assertTrue( errors.size() == 1 );
			ConnectorError error = errors.get(0);
			assertTrue("username".equals(error.getField()));
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
	
	public void testEmptyPassword() throws BonitaException {
		CreateContact connector = getValidConnector();
		connector.setPassword("");
		try {
			List<ConnectorError> errors =  connector.validateValues();
			assertTrue( errors.size() == 1 );
			ConnectorError error = errors.get(0);
			assertTrue("password".equals(error.getField()));
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
	
	public void testEmptySeverLocation() throws BonitaException {
		CreateContact connector = getValidConnector();
		connector.setServerLocation("");
		try {
			List<ConnectorError> errors =  connector.validateValues();
			assertTrue( errors.size() == 1 );
			ConnectorError error = errors.get(0);
			assertTrue("serverLocation".equals(error.getField()));
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
	
	public void testInvalidSeverLocation() throws BonitaException {
		CreateContact connector = getValidConnector();
		connector.setServerLocation("Invalid name");
		try {
			List<ConnectorError> errors =  connector.validateValues();
			assertTrue( errors.size() == 1 );
			ConnectorError error = errors.get(0);
			assertTrue("serverLocation".equals(error.getField()));
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
	
	public void testEmptyEmail() throws BonitaException {
		CreateContact connector = getValidConnector();
		connector.setEmail("");
		try {
			List<ConnectorError> errors =  connector.validateValues();
			assertTrue( errors.size() == 1 );
			ConnectorError error = errors.get(0);
			assertTrue("email".equals(error.getField()));
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
	
	public void testOtherParametersEmpty() throws BonitaException {
		CreateContact connector = getValidConnector();
		connector.setGivenName("");
		connector.setSurname("");
		connector.setCompanyName("");
		connector.setJobTitle("");
		connector.setPhonenumber("");
		try {
			List<ConnectorError> errors =  connector.validateValues();
			assertTrue( errors.size() == 0 );
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
	
	private CreateContact getValidConnector() {
		CreateContact connector = new CreateContact();
		// Exchange server parameters
		String username    = "USERNAME@COMPANY.emea.microsoftonline.com";
		String password    = "*********";
		String serverLocation = Constants.EXCHANGE_SERVER_LOCATION_EMEA;
		// New Contact parameters
		String givenName   = "Albert";
		String surname     = "Anguela";
		String companyName = "AlbertBass";
		String jobTitle    = "Bass player";
		String phonenumber = "676123456";
		String email       = "jordi@hotmail.com";
		connector.setUsername(username);
		connector.setPassword(password);
		connector.setServerLocation(serverLocation);
		connector.setGivenName(givenName);
		connector.setSurname(surname);
		connector.setCompanyName(companyName);
		connector.setJobTitle(jobTitle);
		connector.setPhonenumber(phonenumber);
		connector.setEmail(email);
		return connector;
	}
}
