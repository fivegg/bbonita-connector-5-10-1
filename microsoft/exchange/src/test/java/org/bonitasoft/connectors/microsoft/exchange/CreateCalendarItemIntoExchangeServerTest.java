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
 * Configure the Exchange Server parameters: username and password 
 * before testing this class.
 * 
 * @author Jordi Anguela
 */
public class CreateCalendarItemIntoExchangeServerTest extends TestCase {
	
	// Exchange server parameters
	private String username    = "USERNAME@COMPANY.emea.microsoftonline.com";
	private String password    = "*********";
	private String serverLocation = Constants.EXCHANGE_SERVER_LOCATION_EMEA;
	
	protected static final Logger LOG = Logger.getLogger(CreateCalendarItemIntoExchangeServerTest.class.getName());

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
		CreateCalendarItem connector = getValidConnector();
		try {
			List<ConnectorError> errors =  connector.validateValues();
			assertTrue( errors.size() == 0 );
			
			connector.executeConnector();
			assertTrue( connector.getStatusCode().equals("NoError") );
			assertTrue( connector.getStatusText().equals("Success") );
			assertTrue( connector.getResponse().length() > 0 );
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
	
	private CreateCalendarItem getValidConnector() {
		CreateCalendarItem connector = new CreateCalendarItem();
		// New CalendarItem parameters
		String subject   = "Saint George book day";
		String body      = "Meeting : book selling strategy during Saint George's day<br>www.bonitasoft.com";
		String startTime = "2010-04-23T09:00:00";
		String endTime   = "2010-04-23T11:00:00";
		String location  = "Conference room 007";
		String requiredAttendeesEmails = "jordi@hotmail.com";
		connector.setUsername(username);
		connector.setPassword(password);
		connector.setServerLocation(serverLocation);		
		connector.setSubject(subject);
		connector.setBody(body);
		connector.setStartTime(startTime);
		connector.setEndTime(endTime);
		connector.setLocation(location);
		connector.setRequiredAttendeesEmails(requiredAttendeesEmails);
		return connector;
	}
}
