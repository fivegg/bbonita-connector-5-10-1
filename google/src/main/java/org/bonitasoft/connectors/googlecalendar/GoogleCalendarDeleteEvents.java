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
package org.bonitasoft.connectors.googlecalendar;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.connectors.googlecalendar.common.GoogleCalendarClient;
import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.ProcessConnector;

import com.google.gdata.util.AuthenticationException;

/**
 * 
 * @author Jordi Anguela
 *
 */
public class GoogleCalendarDeleteEvents extends ProcessConnector {

	private String userEmail;
	private String password;
	private String calendarUrl;
	private String searchString;
	
	GoogleCalendarClient gcc;

	@Override
	protected List<ConnectorError> validateValues() {
		List<ConnectorError> errors = new ArrayList<ConnectorError>();
		
		if ( !(searchString.length() > 0) ) {
			errors.add(new ConnectorError("searchString", new IllegalArgumentException("cannot be empty! (it would delete all Events in your calendar)")));
		}
		
		try {
			gcc = new GoogleCalendarClient(userEmail, password, calendarUrl); 
		} 
		catch (AuthenticationException e) {
			errors.add(new ConnectorError("userEmail", new IllegalArgumentException("User credentials not valid!")));
			errors.add(new ConnectorError("password", new IllegalArgumentException("User credentials not valid!")));
		} 
		catch (MalformedURLException e) {
			errors.add(new ConnectorError("calendarURL", new IllegalArgumentException("Malformed URL!")));
		}
		
		return errors;
	}

	@Override
	protected void executeConnector() throws Exception {
		if (gcc != null) {
			gcc.deleteEnvents(searchString);
		}
	}


	/**
	 * Setter for field 'searchString'
	 * DO NOT RENAME THIS SETTER, unless you also change the related field in XML descriptor file
	 */
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	/**
	 * Setter for field 'userEmail'
	 * DO NOT RENAME THIS SETTER, unless you also change the related field in XML descriptor file
	 */
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	/**
	 * Setter for field 'calendarUrl'
	 * DO NOT RENAME THIS SETTER, unless you also change the related field in XML descriptor file
	 */
	public void setCalendarUrl(String calendarUrl) {
		this.calendarUrl = calendarUrl;
	}

	/**
	 * Setter for field 'password'
	 * DO NOT RENAME THIS SETTER, unless you also change the related field in XML descriptor file
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}
